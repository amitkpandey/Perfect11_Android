package com.perfect11.home;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
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
import com.perfect11.home.childFragments.FixturesFragment;
import com.perfect11.home.childFragments.LiveFragment;
import com.perfect11.home.childFragments.ResultsFragment;
import com.perfect11.home.dto.JoinContestCallBackDto;
import com.perfect11.home.dto.TeamIDDto;
import com.perfect11.home.wrapper.CreateTeamCallBackWrapper;
import com.perfect11.login_signup.dto.UserDto;
import com.perfect11.payment.paytm.Api;
import com.perfect11.payment.paytm.Checksum;
import com.perfect11.payment.paytm.Constants;
import com.perfect11.payment.paytm.Paytm;
import com.perfect11.payment.wrapper.TransactionWrapper;
import com.perfect11.team_create.dto.ContestDto;
import com.perfect11.team_create.dto.PlayerDto;
import com.perfect11.upcoming_matches.dto.UpComingMatchesDto;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.utility.AlertDialogCallBack;
import com.utility.DialogUtility;
import com.utility.PreferenceUtility;
import com.utility.customView.CustomTextView;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.utility.Constants.TAG;

public class HomeFragment extends BaseFragment implements PaytmPaymentTransactionCallback, PaymentResultListener {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private boolean flag;
    private ContestDto contestDto;
    private ArrayList<PlayerDto> selectedTeam;
    private UpComingMatchesDto upComingMatchesDto;
    private ApiInterface apiInterface;

    private int bowler_size, batsman_size, allrounder_size, keeper_size;
    private String[] bowler;
    private String[] batsman;
    private String[] allrounder;
    private String[] keeper;

//    private String bowler = "";
//    private String batsman = "";
//    private String allrounder = "";
//    private String keeper = "";

    private String captain, vcaptain, contestId, amount;
    private UserDto userDto;
    public HttpLoggingInterceptor interceptor = null;
    public OkHttpClient client = null;
    public Gson gson;
    private TeamIDDto teamIDDto;
    private String paymentGateway;

    private float player_amount_count = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_home, container, false);
        setHeader("All Matches");
        initView();
        readFromBundle();
        return view;
    }

    public static Fragment newInstance() {
        return new HomeFragment();
    }

    private void readFromBundle() {
        userDto = (UserDto) PreferenceUtility.getObjectInAppPreference(getActivity(), PreferenceUtility.APP_PREFERENCE_NAME);
        try {
            flag = getArguments().getBoolean("flag");
            contestDto = (ContestDto) getArguments().getSerializable("contestDto");
            selectedTeam = (ArrayList<PlayerDto>) getArguments().getSerializable("selectedTeam");
            upComingMatchesDto = (UpComingMatchesDto) getArguments().getSerializable("upComingMatchesDto");
            System.out.println(contestDto.toString());
//            Log.e("BaseHeaderActivity:", contestDto.toString() + upComingMatchesDto.toString() + selectedTeam.size() + "" + flag);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (flag) {
            final Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.custom_dialog_conformation);
            dialog.show();
            TextView header = dialog.findViewById(R.id.ctv_title);
            TextView content = dialog.findViewById(R.id.ctv_body);

            CustomTextView ctv_joining_amount =dialog.findViewById(R.id.ctv_joining_amount);
            ctv_joining_amount.setText("Joining Amount: Rs. "+contestDto.entryfee);

            CustomTextView ctv_body =dialog.findViewById(R.id.ctv_body);
            ctv_body.setText("Contest Name: "+contestDto.room_name);

            Button cbtn_join = dialog.findViewById(R.id.cbtn_join);
            Button cbtn_cancel = dialog.findViewById(R.id.cbtn_cancel);
            cbtn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    flag=false;
                    dialog.dismiss();
                }
            });

            cbtn_join.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                    callCreateTeamAPI();
                    flag=false;
                    dialog.dismiss();
                }
            });

        }
    }

    private void callCreateTeamAPI() {
        //API

        Log.d("API", "Create Team");
        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();

        getSizeOfArray(selectedTeam);
        bowler = new String[bowler_size];
        batsman = new String[batsman_size];
        allrounder = new String[allrounder_size];
        keeper = new String[keeper_size];
        selectPlayerList(selectedTeam);

//        System.out.println("allRounder " + Arrays.toString(allrounder));
//        System.out.println("batsman " + Arrays.toString(batsman));
//        System.out.println("bowler " + Arrays.toString(bowler));
//        System.out.println("keeper " + Arrays.toString(keeper));
        ArrayList<String> batsmanList = new ArrayList<>();
        ArrayList<String> allRounderList = new ArrayList<>();
        ArrayList<String> bowlerList = new ArrayList<>();
        ArrayList<String> keeperList = new ArrayList<>();
        for (String batsman : batsman) {
            batsmanList.add(batsman);
        }
        for (String allRounder : allrounder) {
            allRounderList.add(allRounder);
        }
        for (String bowler : bowler) {
            bowlerList.add(bowler);
        }
        for (String keeper : keeper) {
            keeperList.add(keeper);
        }
        Call<CreateTeamCallBackWrapper> call = apiInterface.createTeamAPI(batsmanList, allRounderList, bowlerList, keeperList, captain,
                player_amount_count, upComingMatchesDto.key_name, vcaptain, userDto.member_id,"Abcd");
        call.enqueue(new Callback<CreateTeamCallBackWrapper>() {
            @Override
            public void onResponse(Call<CreateTeamCallBackWrapper> call, Response<CreateTeamCallBackWrapper> response) {
                CreateTeamCallBackWrapper callBackDto = response.body();
                teamIDDto = callBackDto.data;


                Log.e("CreateTeamCallBack", callBackDto.toString());
                if (callBackDto.status) {
                    Log.e("CreateTeamCallBack", callBackDto.message);
//                    if (!userDto.total_balance.equalsIgnoreCase("0.00")) {
//                        callAPIJoinContest(teamIDDto.team_id);
//                    } else if (userDto.total_balance.equalsIgnoreCase("0.00")) {
//                        final Dialog dialog = new Dialog(getActivity());
//                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                        dialog.setCancelable(false);
//                        dialog.setContentView(R.layout.custom_dialog_payment);
//                        dialog.show();
//                        final RadioGroup rg_01 = dialog.findViewById(R.id.rg_01);
//                        rg_01.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//                            @Override
//                            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                                switch (checkedId) {
//                                    case R.id.rb_paytm:
//                                        paymentGateway = "Paytm";
//                                        break;
//                                    case R.id.rb_razorpay:
//                                        paymentGateway = "Razorpay";
//                                        break;
//                                }
//                            }
//                        });
//                        Button btn_ok = dialog.findViewById(R.id.btn_ok);
//                        btn_ok.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                if (paymentGateway.equalsIgnoreCase("Paytm")) {
//                                    generateCheckSum(contestDto.entryfee);
//                                } else {
//                                    startPayment(contestDto.entryfee);
////                                ActivityController.startNextActivity(getActivity(), PaymentRazorPayActivity.class, true);
//                                }
//                                dialog.dismiss();
//                            }
//                        });
//                    } else if (Integer.parseInt(userDto.total_balance) < Integer.parseInt(contestDto.entryfee)) {
//                        final String amount = String.valueOf(Integer.parseInt(contestDto.entryfee) - Integer.parseInt(userDto.total_balance));
//                        final Dialog dialog = new Dialog(getActivity());
//                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                        dialog.setCancelable(false);
//                        dialog.setContentView(R.layout.custom_dialog_payment);
//                        dialog.show();
//                        final RadioGroup rg_01 = dialog.findViewById(R.id.rg_01);
//                        rg_01.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//                            @Override
//                            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                                switch (checkedId) {
//                                    case R.id.rb_paytm:
//                                        paymentGateway = "Paytm";
//                                        break;
//                                    case R.id.rb_razorpay:
//                                        paymentGateway = "Razorpay";
//                                        break;
//                                }
//                            }
//                        });
//                        Button btn_ok = dialog.findViewById(R.id.btn_ok);
//                        btn_ok.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                if (paymentGateway.equalsIgnoreCase("Paytm")) {
//                                    generateCheckSum(amount);
//                                } else {
//                                    startPayment(amount);
////                                ActivityController.startNextActivity(getActivity(), PaymentRazorPayActivity.class, true);
//                                }
//                                dialog.dismiss();
//                            }
//                        });
//                    }
                    callAPIJoinContest(teamIDDto.team_id);
                } else {
                    DialogUtility.showMessageWithOk(callBackDto.message, getActivity());
                }
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<CreateTeamCallBackWrapper> call, Throwable t) {
                Log.e("TAG", t.toString());
                if (t instanceof IOException) {
                    DialogUtility.showConnectionErrorDialogWithOk(getActivity());
                    // logging probably not necessary
                }
                else {
                    Toast.makeText(getActivity(), "Conversion issue! big problems :(", Toast.LENGTH_SHORT).show();
                    // todo log to some central bug tracking service
                }
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }

    private void callAPIJoinContest(int team_id) {
        //API
        Log.d("API", "Join Contest");
        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();

        Call<JoinContestCallBackDto> call = apiInterface.joinContest(contestDto.id, userDto.reference_id, userDto.member_id, upComingMatchesDto.key_name,
                String.valueOf(team_id));
        call.enqueue(new Callback<JoinContestCallBackDto>() {
            @Override
            public void onResponse(Call<JoinContestCallBackDto> call, Response<JoinContestCallBackDto> response) {
                final JoinContestCallBackDto callBackDto = response.body();

                Log.e("UpcomingMatchesAPI", callBackDto.toString());

                contestId = callBackDto.contest_id;
                amount = String.valueOf(callBackDto.amount_to_paid);
                if (callBackDto.amount_to_paid == 0) {
                    DialogUtility.showMessageOkWithCallback(callBackDto.msg, getActivity(), new AlertDialogCallBack() {
                        @Override
                        public void onSubmit() {
                            DialogUtility.showMessageWithOk(callBackDto.message, getActivity());
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                } else {
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(true);
                    dialog.setCanceledOnTouchOutside(true);
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
                            if (rg_01.getCheckedRadioButtonId() == -1) {
                                DialogUtility.showMessageWithOk("Please select any one payment gateway", getActivity());
                            } else {
                                if (paymentGateway.equalsIgnoreCase("Paytm")) {
                                    generateCheckSum(String.valueOf(callBackDto.amount_to_paid));
                                } else {
                                    startPayment(String.valueOf(callBackDto.amount_to_paid));
//                                ActivityController.startNextActivity(getActivity(), PaymentRazorPayActivity.class, true);
                                }
                                dialog.dismiss();
                            }
                        }
                    });
                }
//                if (callBackDto.status) {
//                    Log.e("UpcomingMatchesAPI", callBackDto.message);
//                    DialogUtility.showMessageWithOk(callBackDto.message, getActivity());
//                } else {
//                    DialogUtility.showMessageWithOk(callBackDto.message, getActivity());
//                }
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<JoinContestCallBackDto> call, Throwable t) {
                Log.e("TAG", t.toString());
                if (t instanceof IOException) {
                    DialogUtility.showConnectionErrorDialogWithOk(getActivity());
                    // logging probably not necessary
                }
                else {
                    Toast.makeText(getActivity(), "Conversion issue! big problems :(", Toast.LENGTH_SHORT).show();
                    // todo log to some central bug tracking service
                }

                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }

    private void selectPlayerList(ArrayList<PlayerDto> data) {
        int bowler_i, batsman_i, allrounder_i, keeper_i;
        bowler_i = 0;
        batsman_i = 0;
        allrounder_i = 0;
        keeper_i = 0;

        for (PlayerDto playerDto : data) {
            player_amount_count = player_amount_count + Float.parseFloat(playerDto.credit);
            /* Divided  players*/
            switch (playerDto.seasonal_role) {
                case "bowler":
                    bowler[bowler_i] = playerDto.short_name;
                    bowler_i++;
                    break;
                case "batsman":
                    batsman[batsman_i] = playerDto.short_name;
                    batsman_i++;
                    break;
                case "allrounder":

                    allrounder[allrounder_i] = playerDto.short_name;
                    allrounder_i++;
                    break;
                case "keeper":
                    keeper[keeper_i] = playerDto.short_name;
                    keeper_i++;
                    break;
            }
            if (playerDto.isC) {
                captain = playerDto.short_name;
            }

            if (playerDto.isCV) {
                vcaptain = playerDto.short_name;
            }
        }
    }

    private void getSizeOfArray(ArrayList<PlayerDto> data) {

        bowler_size = 0;
        batsman_size = 0;
        allrounder_size = 0;
        keeper_size = 0;
        for (PlayerDto playerDto : data) {

            /* Divided  players*/
            switch (playerDto.seasonal_role) {
                case "bowler":
                    bowler_size++;
                    break;
                case "batsman":
                    batsman_size++;
                    break;
                case "allrounder":
                    allrounder_size++;
                    break;
                case "keeper":
                    keeper_size++;
                    break;
            }
            if (playerDto.isC) {
                captain = playerDto.short_name;
            }

            if (playerDto.isCV) {
                vcaptain = playerDto.short_name;
            }
        }
    }

    private void initView() {
        viewPager = view.findViewById(R.id.view_pager);
        ((BaseHeaderActivity) getActivity()).slideMenu.addIgnoredView(viewPager);
        tabLayout = view.findViewById(R.id.tabs);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        System.out.println("List");
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new FixturesFragment(), "Fixtures");
        adapter.addFragment(new LiveFragment(), "Live");
        adapter.addFragment(new ResultsFragment(), "Results");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
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
            options.put("amount", Float.valueOf(amount) * 100);
            options.put("theme",new JSONObject("{color: '#E93D29'}"));

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
        String transactionId = bundle.getString("TXNID");
        String bankTransactionId = bundle.getString("BANKTXNID");
        callAPITransactionJoinContest(transactionId, amount, "paytm", "success");
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
        String transactionId = bundle.getString("TXNID");
        callAPITransactionJoinContest(transactionId, amount, "paytm", "failure");
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
            callAPITransactionJoinContest(razorpayPaymentID, amount, "razorpay", "success");
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

    private void callAPITransactionJoinContest(String paymentId, String amount, String type, String status) {
        //API
        Log.d("API", "Join Contest");
        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<TransactionWrapper> call = apiInterface.paymentForJoinContest(contestId, userDto.member_id, paymentId, type, status, amount);
        call.enqueue(new Callback<TransactionWrapper>() {
            @Override
            public void onResponse(Call<TransactionWrapper> call, Response<TransactionWrapper> response) {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();

                final TransactionWrapper callBackDto = response.body();
                Log.e("UpcomingMatchesAPI", callBackDto.toString());
                DialogUtility.showMessageOkWithCallback(callBackDto.message, getActivity(), new AlertDialogCallBack() {
                    @Override
                    public void onSubmit() {
                        DialogUtility.showMessageWithOk(callBackDto.message, getActivity());
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }

            @Override
            public void onFailure(Call<TransactionWrapper> call, Throwable t) {
                Log.e("TAG", t.toString());
                if (t instanceof IOException) {
                    DialogUtility.showConnectionErrorDialogWithOk(getActivity());
                    // logging probably not necessary
                }
                else {
                    Toast.makeText(getActivity(), "Conversion issue! big problems :(", Toast.LENGTH_SHORT).show();
                    // todo log to some central bug tracking service
                }
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }

}
