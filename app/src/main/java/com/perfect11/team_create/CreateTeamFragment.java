package com.perfect11.team_create;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.perfect11.R;
import com.perfect11.base.ApiClient;
import com.perfect11.base.ApiInterface;
import com.perfect11.base.BaseFragment;
import com.perfect11.base.BaseHeaderActivity;
import com.perfect11.contest.dto.TeamDto;
import com.perfect11.home.HomeFragment;
import com.perfect11.home.dto.JoinContestCallBackDto;
import com.perfect11.login_signup.dto.UserDto;
import com.perfect11.payment.paytm.Api;
import com.perfect11.payment.paytm.Checksum;
import com.perfect11.payment.paytm.Constants;
import com.perfect11.payment.paytm.Paytm;
import com.perfect11.team_create.adapter.CreateTeamAdapter;
import com.perfect11.team_create.dto.ContestDto;
import com.perfect11.upcoming_matches.dto.UpComingMatchesDto;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.utility.AlertDialogCallBack;
import com.utility.DialogUtility;
import com.utility.PreferenceUtility;
import com.utility.customView.CustomButton;
import com.utility.customView.CustomTextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.utility.Constants.TAG;


public class CreateTeamFragment extends BaseFragment implements PaytmPaymentTransactionCallback, PaymentResultListener {
    private RecyclerView rv_team;
    private ArrayList<TeamDto> teamDtoArrayList;
    private CustomButton btn_create;
    private CustomTextView tv_match, tv_status;
    private String team1, teamA, team2, teamB, matchStatus;
    private UpComingMatchesDto upComingMatchesDto;
    private ContestDto contestDto;
    private boolean isJoiningContest = false;
    private ApiInterface apiInterface;
    private UserDto userDto;
    public static HttpLoggingInterceptor interceptor = null;
    public static OkHttpClient client = null;
    public static Gson gson;
    private TeamDto teamDto;
    private String paymentGateway;

    public static CreateTeamFragment newInstance() {
        return new CreateTeamFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.create_team, container, false);
        readFromBundle();
        setInnerHeader("My Team List");
        initView();
        return view;
    }

    private void readFromBundle() {
        userDto = (UserDto) PreferenceUtility.getObjectInAppPreference(getActivity(), PreferenceUtility.APP_PREFERENCE_NAME);

        teamDtoArrayList = (ArrayList<TeamDto>) getArguments().getSerializable("teamDto");
        team1 = getArguments().getString("team1");
        team2 = getArguments().getString("team2");
        upComingMatchesDto = (UpComingMatchesDto) getArguments().getSerializable("upComingMatchesDto");
        teamA = upComingMatchesDto.teama;
        teamB = upComingMatchesDto.teamb;
        matchStatus = upComingMatchesDto.matchstatus;

        try {
            contestDto = (ContestDto) getArguments().getSerializable("contestDto");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (contestDto != null) {
            isJoiningContest = true;
        }
    }

    private void initView() {
        rv_team = view.findViewById(R.id.rv_team);
        btn_create = view.findViewById(R.id.btn_create);
        tv_match = view.findViewById(R.id.tv_match);
        tv_status = view.findViewById(R.id.tv_status);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_team.setLayoutManager(layoutManager);
        CreateTeamAdapter createTeamAdapter = new CreateTeamAdapter(getActivity(), teamDtoArrayList, isJoiningContest);
        rv_team.setAdapter(createTeamAdapter);
        createTeamAdapter.setOnButtonListener(new CreateTeamAdapter.OnButtonListener() {

            @Override
            public void onEditClick(int position) {
                SelectPlayersFragment selectPlayersFragment = new SelectPlayersFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("upComingMatchesDto", upComingMatchesDto);
                if (contestDto != null) {
                    bundle.putSerializable("contestDto", contestDto);
                }
                TeamDto teamDto = teamDtoArrayList.get(position);
                bundle.putSerializable("teamDto", teamDto);
                selectPlayersFragment.setArguments(bundle);
                ((BaseHeaderActivity) getActivity()).addFragment(selectPlayersFragment, true, SelectPlayersFragment.class.getName());
            }

            @Override
            public void onPreviewClick(int position) {
                TeamDto teamDto = teamDtoArrayList.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("team1", team1);
                bundle.putString("team2", team2);
                bundle.putString("teamA", teamA);
                bundle.putString("teamB", teamB);
                bundle.putSerializable("teamDto", teamDto);
                PreviewTeamFragment previewTeamFragment = PreviewTeamFragment.newInstance();
                previewTeamFragment.setArguments(bundle);
                ((BaseHeaderActivity) getActivity()).addFragment(previewTeamFragment, true, PreviewTeamFragment.class.getName());
            }

            @Override
            public void onJoinClick(int position) {
                teamDto = teamDtoArrayList.get(position);
                if (!userDto.total_balance.equalsIgnoreCase("0.00")) {
                    callAPIJoinContest(Integer.parseInt(teamDto.team_id));
                } else if (userDto.total_balance.equalsIgnoreCase("0.00")) {
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.custom_dialog_payment);
                    dialog.show();
                    final RadioGroup rg_01 = dialog.findViewById(R.id.rg_01);
                    rg_01.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            switch (checkedId) {
                                case R.id.rb_paytm:
                                    paymentGateway = "Paytm";
                                    break;
                                case R.id.rb_razorpay:
                                    paymentGateway = "Razorpay";
                                    break;
                            }
                        }
                    });
                    Button btn_ok = dialog.findViewById(R.id.btn_ok);
                    btn_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (paymentGateway.equalsIgnoreCase("Paytm")) {
                                generateCheckSum(contestDto.entryfee);
                            } else {
                                startPayment(contestDto.entryfee);
//                                ActivityController.startNextActivity(getActivity(), PaymentRazorPayActivity.class, true);
                            }
                            dialog.dismiss();
                        }
                    });
                } else if (Integer.parseInt(userDto.total_balance) < Integer.parseInt(contestDto.entryfee)) {
                    final String amount = String.valueOf(Integer.parseInt(contestDto.entryfee) - Integer.parseInt(userDto.total_balance));
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.custom_dialog_payment);
                    dialog.show();
                    final RadioGroup rg_01 = dialog.findViewById(R.id.rg_01);
                    rg_01.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            switch (checkedId) {
                                case R.id.rb_paytm:
                                    paymentGateway = "Paytm";
                                    break;
                                case R.id.rb_razorpay:
                                    paymentGateway = "Razorpay";
                                    break;
                            }
                        }
                    });
                    Button btn_ok = dialog.findViewById(R.id.btn_ok);
                    btn_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (paymentGateway.equalsIgnoreCase("Paytm")) {
                                generateCheckSum(amount);
                            } else {
                                startPayment(amount);
//                                ActivityController.startNextActivity(getActivity(), PaymentRazorPayActivity.class, true);
                            }
                            dialog.dismiss();
                        }
                    });
                }
            }
        });
        btn_create.setText("Create Team " + (teamDtoArrayList.size() + 1));
        tv_match.setText(team1 + " vs " + team2);
        tv_status.setText(matchStatus);
    }

    @Override
    public void onButtonClick(View view) {
        super.onButtonClick(view);
        switch (view.getId()) {
            case R.id.img_back:
                getActivity().onBackPressed();
                break;
            case R.id.btn_create:
                SelectPlayersFragment selectPlayersFragment = new SelectPlayersFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("upComingMatchesDto", upComingMatchesDto);
                if (contestDto != null) {
                    bundle.putSerializable("contestDto", contestDto);
                }
                selectPlayersFragment.setArguments(bundle);
                ((BaseHeaderActivity) getActivity()).addFragment(selectPlayersFragment, true, SelectPlayersFragment.class.getName());
                break;
        }
    }

    private void callAPIJoinContest(int team_id) {
        //API
        Log.d("API", "Join Contest");
        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<JoinContestCallBackDto> call = apiInterface.joinContest(contestDto.id, userDto.reference_id, userDto.member_id, upComingMatchesDto.key_name,
                String.valueOf(team_id));
        call.enqueue(new Callback<JoinContestCallBackDto>() {
            @Override
            public void onResponse(Call<JoinContestCallBackDto> call, Response<JoinContestCallBackDto> response) {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();

                JoinContestCallBackDto callBackDto = response.body();
                Log.e("UpcomingMatchesAPI", callBackDto.toString());
                DialogUtility.showMessageOkWithCallback(callBackDto.message, getActivity(), new AlertDialogCallBack() {
                    @Override
                    public void onSubmit() {
                        ((BaseHeaderActivity) getActivity()).removeAllFragment();
                        ((BaseHeaderActivity) getActivity()).replaceFragment(HomeFragment.newInstance(), false, HomeFragment.class.getName());
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }

            @Override
            public void onFailure(Call<JoinContestCallBackDto> call, Throwable t) {
                Log.e("TAG", t.toString());
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }

    private void startPayment(String amount) {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Stake For Win");
            options.put("description", "Create Contest");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://rzp-mobile.s3.amazonaws.com/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", Float.parseFloat(amount) * 100);

           /* JSONObject preFill = new JSONObject();
            preFill.put("email", "test@razorpay.com");
            preFill.put("contact", "9876543210");

            options.put("prefill", preFill);*/

            co.open(getActivity(), options);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    private void generateCheckSum(String amount) {
        //getting the tax amount first.

        if (interceptor == null) {
            interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        }
        if (client == null) {
            client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        }

        if (gson == null) {
            gson = new GsonBuilder()
                    .setLenient()
                    .create();
        }

        //creating a retrofit object.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //creating the retrofit api service
        Api apiService = retrofit.create(Api.class);

        //creating paytm object
        //containing all the values required
        final Paytm paytm = new Paytm(Constants.M_ID, Constants.CHANNEL_ID, amount, Constants.WEBSITE, Constants.CALLBACK_URL, Constants.INDUSTRY_TYPE_ID);

        //creating a call object from the apiService
        Call<Checksum> call = apiService.getChecksum(paytm.getmId(), paytm.getOrderId(), paytm.getCustId(), paytm.getChannelId(), paytm.getTxnAmount(),
                paytm.getWebsite(), paytm.getCallBackUrl(), paytm.getIndustryTypeId());

        //making the call to generate checksum
        call.enqueue(new Callback<Checksum>() {
            @Override
            public void onResponse(Call<Checksum> call, Response<Checksum> response) {

                //once we get the checksum we will initiailize the payment.
                //the method is taking the checksum we got and the paytm object as the parameter
                initializePaytmPayment(response.body().getChecksumHash(), paytm);
            }

            @Override
            public void onFailure(Call<Checksum> call, Throwable t) {

            }
        });
    }

    private void initializePaytmPayment(String checksumHash, Paytm paytm) {

        //getting paytm service
        PaytmPGService Service = PaytmPGService.getStagingService();

        //use this when using for production
        //PaytmPGService Service = PaytmPGService.getProductionService();

        //creating a hashmap and adding all the values required
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("MID", Constants.M_ID);
        paramMap.put("ORDER_ID", paytm.getOrderId());
        paramMap.put("CUST_ID", paytm.getCustId());
        paramMap.put("CHANNEL_ID", paytm.getChannelId());
        paramMap.put("TXN_AMOUNT", paytm.getTxnAmount());
        paramMap.put("WEBSITE", paytm.getWebsite());
        paramMap.put("CALLBACK_URL", paytm.getCallBackUrl());
        paramMap.put("CHECKSUMHASH", checksumHash);
        paramMap.put("INDUSTRY_TYPE_ID", paytm.getIndustryTypeId());


        //creating a paytm order object using the hashmap
        PaytmOrder order = new PaytmOrder(paramMap);

        //intializing the paytm service
        Service.initialize(order, null);

        //finally starting the payment transaction
        Service.startPaymentTransaction(getActivity(), true, true, this);

    }

    //all these overriden method is to detect the payment result accordingly
    @Override
    public void onTransactionResponse(Bundle bundle) {
        callAPIJoinContest(Integer.parseInt(teamDto.team_id));
        String transactionId = bundle.getString("TXNID");
        String bankTransactionId = bundle.getString("BANKTXNID");
        System.out.println("transactionId " + transactionId + " bankTransactionId " + bankTransactionId);
//        Toast.makeText(getActivity(), bundle.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void networkNotAvailable() {
        Toast.makeText(getActivity(), "Network error", Toast.LENGTH_LONG).show();
    }

    @Override
    public void clientAuthenticationFailed(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void someUIErrorOccurred(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onErrorLoadingWebPage(int i, String s, String s1) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressedCancelTransaction() {
        Toast.makeText(getActivity(), "Back Pressed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTransactionCancel(String s, Bundle bundle) {
        Toast.makeText(getActivity(), s + bundle.toString(), Toast.LENGTH_LONG).show();
    }

    /**
     * The name of the function has to be onPaymentSuccess
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            callAPIJoinContest(Integer.parseInt(teamDto.team_id));
            Toast.makeText(getActivity(), "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }

    /**
     * The name of the function has to be onPaymentError
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(getActivity(), "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
        }
    }
}
