package com.perfect11.contest;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.perfect11.contest.adapter.ContestWinnerAdapter;
import com.perfect11.contest.dto.ContestCallBackDto;
import com.perfect11.contest.dto.ContestWinnerDto;
import com.perfect11.login_signup.dto.UserDto;
import com.perfect11.payment.paytm.Api;
import com.perfect11.payment.paytm.Checksum;
import com.perfect11.payment.paytm.Constants;
import com.perfect11.payment.paytm.Paytm;
import com.perfect11.payment.wrapper.TransactionWrapper;
import com.perfect11.upcoming_matches.dto.UpComingMatchesDto;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.utility.AlertDialogCallBack;
import com.utility.CommonUtility;
import com.utility.DialogUtility;
import com.utility.PreferenceUtility;
import com.utility.customView.CustomButton;
import com.utility.customView.CustomEditText;
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


public class CreateContestFragment extends BaseFragment implements PaytmPaymentTransactionCallback, PaymentResultListener {
    private CustomTextView tv_match, tv_status, tv_amount;
    private CustomEditText et_name, et_winning_amount, et_contest_size, et_no_winners;
    private CheckBox cb_allow_multiple_team;
    private ApiInterface apiInterface;
    private UpComingMatchesDto upComingMatchesDto;
    private LinearLayout ll_winner_details;
    private ContestWinnerAdapter contestWinnerAdapter = null;
    private ListView lv_winner;
    private CustomButton btn_set;
    private ArrayList<ContestWinnerDto> contestWinnerDtoArrayList;
    private ArrayList<Float> winnerAmount = null;
    private ArrayList<Float> winnerPercent = null;
    public HttpLoggingInterceptor interceptor = null;
    public OkHttpClient client = null;
    public Gson gson;
    private String paymentGateway, contestId, amount;
    private ContestCallBackDto contestCallBackDto;

    public static CreateContestFragment newInstance() {
        return new CreateContestFragment();
    }

    private UserDto userDto;
    private int totalAmount = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.create_contest, container, false);
        setInnerHeader("Create Contest");
        readFromBundle();
        initView();
        setValues();
        return view;
    }

    private void readFromBundle() {
        upComingMatchesDto = (UpComingMatchesDto) getArguments().getSerializable("upComingMatchesDto");
        userDto = (UserDto) PreferenceUtility.getObjectInAppPreference(getActivity(), PreferenceUtility.APP_PREFERENCE_NAME);
    }

    private void initView() {
        tv_match = view.findViewById(R.id.tv_match);
        tv_status = view.findViewById(R.id.tv_status);
        et_name = view.findViewById(R.id.et_name);
        et_winning_amount = view.findViewById(R.id.et_winning_amount);
        et_contest_size = view.findViewById(R.id.et_contest_size);
        tv_amount = view.findViewById(R.id.tv_amount);

        //cb_allow_Customize = view.findViewById(R.id.cb_allow_Customize);
        cb_allow_multiple_team = view.findViewById(R.id.cb_allow_multiple_team);

        lv_winner = view.findViewById(R.id.lv_winner);
        ll_winner_details = view.findViewById(R.id.ll_winner_details);

        et_no_winners = view.findViewById(R.id.et_no_winners);
        btn_set = view.findViewById(R.id.btn_set);
    }

    private void setAdapter(int n_winner) {
        contestWinnerDtoArrayList = new ArrayList<>();
        float percentage = (100 / n_winner);
        float amount = Float.parseFloat(et_winning_amount.getText().toString().trim()) * (percentage / 100);

        System.out.println(et_winning_amount.getText().toString() + "amount: " + amount);
        for (int i = 0; i < n_winner; i++) {
            ContestWinnerDto contestWinnerDto = new ContestWinnerDto();
            contestWinnerDto.amount = amount;
            contestWinnerDto.percentage = percentage;
            contestWinnerDto.position = (i + 1);
            contestWinnerDtoArrayList.add(contestWinnerDto);
        }

        contestWinnerAdapter = new ContestWinnerAdapter(getActivity(), contestWinnerDtoArrayList,
                Float.parseFloat(et_winning_amount.getText().toString().trim()));
        lv_winner.setAdapter(contestWinnerAdapter);
        contestWinnerAdapter.setOnTextChangeListener(new ContestWinnerAdapter.OnTextChangeListener() {
            @Override
            public void onTextChange(CharSequence text, CustomEditText editText, CustomTextView textView, int position) {
                System.out.println("percentage: " + text);

                float percentage;
                try {
                    percentage = Float.parseFloat(text.toString());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    percentage = 0;
                }
                System.out.println("percentage: " + text);
                if (position != 0) {
                    if (contestWinnerDtoArrayList.get(position - 1).percentage < percentage) {
                        Toast.makeText(getActivity(), "Wrong input", Toast.LENGTH_SHORT).show();
                        editText.setText("");
                    } else {
                        contestWinnerDtoArrayList.get(position).percentage = percentage;
                        contestWinnerDtoArrayList.get(position).amount = Float.parseFloat(et_winning_amount.getText().toString().trim()) * (percentage / 100);
                    }
                } else {
                    contestWinnerDtoArrayList.get(position).percentage = percentage;
                    contestWinnerDtoArrayList.get(position).amount = Float.parseFloat(et_winning_amount.getText().toString().trim()) * (percentage / 100);
                }

                textView.setText("" + contestWinnerDtoArrayList.get(position).amount);
//                if (position == 0) {
//                    System.out.println("");
//                    EditText editText1 = lv_winner.getChildAt(position).findViewById(R.id.et_percent);
//                    editText1.setText(String.valueOf(Float.parseFloat(et_winning_amount.getText().toString().trim()) - Float.parseFloat(editText.getText().toString().trim())));
//                }
//                for (int i = 0; i < contestWinnerDtoArrayList.size() - 1; i++) {
//                    ContestWinnerDto contestWinnerDto = new ContestWinnerDto();
//                    contestWinnerDto.amount = Float.parseFloat(et_winning_amount.getText().toString().trim()) -
//                            Float.parseFloat(editText.getText().toString().trim());
//                    contestWinnerDto.percentage = percentage;
//                    contestWinnerDto.poistion = (i + 1);
//                    contestWinnerDtoArrayList.add(contestWinnerDto);
//                }
//                contestWinnerAdapter.notifyDataSetChanged();
            }

        });

        CommonUtility.setListViewHeightBasedOnChildren(lv_winner);
    }

    private void setValues() {
        tv_match.setText(upComingMatchesDto.teama + " vs " + upComingMatchesDto.teamb);
        tv_status.setText(upComingMatchesDto.matchstatus);

        et_contest_size.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (!et_contest_size.getText().toString().equalsIgnoreCase("") ||
                            !et_winning_amount.getText().toString().equalsIgnoreCase("")) {
                        int contest = Integer.parseInt(et_contest_size.getText().toString().trim());
                        int amount = Integer.parseInt(et_winning_amount.getText().toString().trim());
                        int percentage = (int) (amount * 15f / 100f);
                        int total = (amount + percentage) / contest;
//                        System.out.println("percentage " + percentage);
//                        System.out.println("total " + total);
                        totalAmount = total;
                        tv_amount.setText(getActivity().getResources().getString(R.string.Rs) + " " + total);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_winning_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (!et_contest_size.getText().toString().equalsIgnoreCase("") ||
                            !et_winning_amount.getText().toString().equalsIgnoreCase("")) {
                        int contest = Integer.parseInt(et_contest_size.getText().toString().trim());
                        int amount = Integer.parseInt(et_winning_amount.getText().toString().trim());
                        int percentage = (int) (amount * 15f / 100f);
                        int total = (amount + percentage) / contest;
//                        System.out.println("percentage " + percentage);
//                        System.out.println("total " + total);
                        totalAmount = total;
                        tv_amount.setText(getActivity().getResources().getString(R.string.Rs) + " " + total);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void onButtonClick(View view) {
        super.onButtonClick(view);
        switch (view.getId()) {
            case R.id.img_back:
                getActivity().onBackPressed();
                break;
            case R.id.btn_create:
                if (et_name.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Enter Contest Name", Toast.LENGTH_SHORT).show();
                    et_name.requestFocus();
                } else if (et_winning_amount.getText().toString().equalsIgnoreCase("") || Integer.parseInt(et_winning_amount.getText().toString()) < 0) {
                    Toast.makeText(getActivity(), "Enter Winning Amount", Toast.LENGTH_SHORT).show();
                    et_winning_amount.requestFocus();
                } else if (et_contest_size.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Enter Contest Size", Toast.LENGTH_SHORT).show();
                    et_contest_size.requestFocus();
                } else if (Integer.parseInt(et_contest_size.getText().toString()) <= 1) {
                    Toast.makeText(getActivity(), "Contest Size should be minimum 2", Toast.LENGTH_SHORT).show();
                    et_contest_size.requestFocus();
                } else if (et_no_winners.getText().toString().trim().equals("")) {
                    Toast.makeText(getActivity(), "Enter number of winners.", Toast.LENGTH_SHORT).show();
                    et_no_winners.requestFocus();
                } else if (Integer.parseInt(et_no_winners.getText().toString().trim()) == 0) {
                    Toast.makeText(getActivity(), "Number of winners should not be 0", Toast.LENGTH_SHORT).show();
                    et_no_winners.requestFocus();
                } else if (Integer.parseInt(et_no_winners.getText().toString().trim()) >= Integer.parseInt(et_contest_size.getText().toString().trim())) {
                    Toast.makeText(getActivity(), "Number of winners should not be greater then contest size", Toast.LENGTH_SHORT).show();
                    et_no_winners.requestFocus();
                } else if (contestWinnerAdapter == null) {
                    Toast.makeText(getActivity(), "Set Number of winner", Toast.LENGTH_SHORT).show();
                } else if (checkArray()) {
                    callAPI();
                }
//                else if (userDto.total_balance.equalsIgnoreCase("0.00")) {
//                    final Dialog dialog = new Dialog(getActivity());
//                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                    dialog.setCancelable(false);
//                    dialog.setContentView(R.layout.custom_dialog_payment);
//                    dialog.show();
//                    final RadioGroup rg_01 = dialog.findViewById(R.id.rg_01);
//                    rg_01.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//                        @Override
//                        public void onCheckedChanged(RadioGroup group, int checkedId) {
//                            switch (checkedId) {
//                                case R.id.rb_paytm:
//                                    paymentGateway = "Paytm";
//                                    break;
//                                case R.id.rb_razorpay:
//                                    paymentGateway = "Razorpay";
//                                    break;
//                            }
//                        }
//                    });
//                    Button btn_ok = dialog.findViewById(R.id.btn_ok);
//                    btn_ok.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            if (paymentGateway.equalsIgnoreCase("Paytm")) {
//                                generateCheckSum(String.valueOf(totalAmount));
//                            } else {
//                                startPayment(String.valueOf(totalAmount));
////                                ActivityController.startNextActivity(getActivity(), PaymentRazorPayActivity.class, true);
//                            }
//                            dialog.dismiss();
//                        }
//                    });
//                } else if (Integer.parseInt(userDto.total_balance) < totalAmount) {
//                    final String amount = String.valueOf(totalAmount - Integer.parseInt(userDto.total_balance));
//                    final Dialog dialog = new Dialog(getActivity());
//                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                    dialog.setCancelable(false);
//                    dialog.setContentView(R.layout.custom_dialog_payment);
//                    dialog.show();
//                    final RadioGroup rg_01 = dialog.findViewById(R.id.rg_01);
//                    rg_01.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//                        @Override
//                        public void onCheckedChanged(RadioGroup group, int checkedId) {
//                            switch (checkedId){
//                                case R.id.rb_paytm:
//                                    paymentGateway = "Paytm";
//                                    break;
//                                case R.id.rb_razorpay:
//                                    paymentGateway = "Razorpay";
//                                    break;
//                            }
//                        }
//                    });
//                    Button btn_ok = dialog.findViewById(R.id.btn_ok);
//                    btn_ok.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            if (paymentGateway.equalsIgnoreCase("Paytm")) {
//                                generateCheckSum(amount);
//                            } else {
//                                startPayment(amount);
////                                ActivityController.startNextActivity(getActivity(), PaymentRazorPayActivity.class, true);
//                            }
//                            dialog.dismiss();
//                        }
//                    });
//                }
                break;
            case R.id.btn_set:
                if (isValidData()) {
                    if (et_no_winners.getText().toString().trim().equals("")) {
                        Toast.makeText(getActivity(), "Enter number of winners.", Toast.LENGTH_SHORT).show();
                        et_no_winners.requestFocus();
                    } else if (Integer.parseInt(et_no_winners.getText().toString().trim()) == 0) {
                        Toast.makeText(getActivity(), "Number of winners should not be 0", Toast.LENGTH_SHORT).show();
                        et_no_winners.requestFocus();
                    } else if (Integer.parseInt(et_no_winners.getText().toString().trim()) >= Integer.parseInt(et_contest_size.getText().toString().trim())) {
                        Toast.makeText(getActivity(), "Number of winners should not be greater then contest size", Toast.LENGTH_SHORT).show();
                        et_no_winners.requestFocus();
                    } else {
                        lv_winner.setVisibility(View.VISIBLE);
                        setAdapter(Integer.parseInt(et_no_winners.getText().toString().trim()));
                    }
                }
                break;
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

    private void callAPI() {
        //API
        /*
         * Collecting data*/
        Log.d("API", "Get Player");
        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<ContestCallBackDto> call = apiInterface.createContest(Integer.parseInt(et_contest_size.getText().toString()), 1,
                totalAmount, cb_allow_multiple_team.isChecked() ? 1 : 0, upComingMatchesDto.key_name, et_name.getText().toString(), winnerAmount,
                winnerPercent, Integer.parseInt(et_no_winners.getText().toString()), Integer.parseInt(et_winning_amount.getText().toString().trim()),
                userDto.member_id, userDto.reference_id
        );
        call.enqueue(new Callback<ContestCallBackDto>() {
            @Override
            public void onResponse(Call<ContestCallBackDto> call, Response<ContestCallBackDto> response) {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                contestCallBackDto = response.body();
                contestId = contestCallBackDto.contestId;
                amount = String.valueOf(contestCallBackDto.amount_to_paid);
                if (contestCallBackDto.amount_to_paid == 0) {
                    ContestInviteFragment contestInviteFragment = new ContestInviteFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("contestCallBackDto", contestCallBackDto);
                    contestInviteFragment.setArguments(bundle);
                    ((BaseHeaderActivity) getActivity()).addFragment(contestInviteFragment, false, ContestInviteFragment.class.getName());
                } else {
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
                                generateCheckSum(String.valueOf(totalAmount));
                            } else {
                                startPayment(String.valueOf(totalAmount));
//                                ActivityController.startNextActivity(getActivity(), PaymentRazorPayActivity.class, true);
                            }
                            dialog.dismiss();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<ContestCallBackDto> call, Throwable t) {
                Log.e("TAG", t.toString());
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                DialogUtility.showMessageWithOk(t.getMessage(), getActivity());
            }
        });

    }

    private boolean checkArray() {

        winnerAmount = new ArrayList<>();
        winnerPercent = new ArrayList<>();

        contestWinnerDtoArrayList = contestWinnerAdapter.getArray();
        float amount = 0;
        for (int i = 0; i < contestWinnerDtoArrayList.size(); i++) {
            winnerAmount.add(contestWinnerDtoArrayList.get(i).amount);
            winnerPercent.add(contestWinnerDtoArrayList.get(i).percentage);
            amount += contestWinnerDtoArrayList.get(i).amount;
            if (i != 0) {
                if (contestWinnerDtoArrayList.get(i).amount > contestWinnerDtoArrayList.get(i - 1).amount) {
                    Toast.makeText(getActivity(), "Winner \"+i+\" should get more then winner " + (i + 1), Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }


        if (amount > Integer.parseInt(et_winning_amount.getText().toString())) {
            Toast.makeText(getActivity(), "You crossed winner's amount limit.\n Winner amount=" + Integer.parseInt(et_winning_amount.getText().toString()) + "\n Used=" + amount, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (amount < Integer.parseInt(et_winning_amount.getText().toString())) {
            Toast.makeText(getActivity(), "You did not use all amount of Winners.\n Winner amount=" + Integer.parseInt(et_winning_amount.getText().toString()) + "\n Used=" + amount, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean isValidData() {
        if (et_name.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(getActivity(), "Enter Contest Name", Toast.LENGTH_SHORT).show();
            et_name.requestFocus();
            return false;
        } else if (et_winning_amount.getText().toString().equalsIgnoreCase("") || Integer.parseInt(et_winning_amount.getText().toString()) < 0) {
            Toast.makeText(getActivity(), "Enter Winning Amount", Toast.LENGTH_SHORT).show();
            et_winning_amount.requestFocus();
            return false;
        } else if (Integer.parseInt(et_winning_amount.getText().toString()) <= 0) {
            Toast.makeText(getActivity(), "Winning Amount should not be 0", Toast.LENGTH_SHORT).show();
            et_winning_amount.requestFocus();
            return false;
        } else if (et_contest_size.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(getActivity(), "Enter Contest Size", Toast.LENGTH_SHORT).show();
            et_contest_size.requestFocus();
            return false;
        } else if (Integer.parseInt(et_contest_size.getText().toString()) <= 1) {
            Toast.makeText(getActivity(), "Contest Size should be minimum 2", Toast.LENGTH_SHORT).show();
            et_contest_size.requestFocus();
            return false;
        } else {
            //lv_winner.setVisibility(View.VISIBLE);
            et_name.setEnabled(false);
            et_winning_amount.setEnabled(false);
            et_contest_size.setEnabled(false);
            return true;
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
        callAPITransactionCreateContest(transactionId, amount, "paytm", "success");
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
        callAPITransactionCreateContest(transactionId, amount, "paytm", "failure");
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
            callAPITransactionCreateContest(razorpayPaymentID, amount, "razorpay", "success");
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

    private void callAPITransactionCreateContest(String paymentId, String amount, String type, String status) {
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
                        ContestInviteFragment contestInviteFragment = new ContestInviteFragment();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("contestCallBackDto", contestCallBackDto);
                        contestInviteFragment.setArguments(bundle);
                        ((BaseHeaderActivity) getActivity()).addFragment(contestInviteFragment, false, ContestInviteFragment.class.getName());
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }

            @Override
            public void onFailure(Call<TransactionWrapper> call, Throwable t) {
                Log.e("TAG", t.toString());
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }

}
