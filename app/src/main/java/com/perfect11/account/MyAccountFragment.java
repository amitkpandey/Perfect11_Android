package com.perfect11.account;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.perfect11.R;
import com.perfect11.account.wrapper.MyAccountWrapper;
import com.perfect11.base.ApiClient;
import com.perfect11.base.ApiClient3;
import com.perfect11.base.ApiInterface;
import com.perfect11.base.BaseFragment;
import com.perfect11.base.BaseHeaderActivity;
import com.perfect11.login_signup.dto.InviteDto;
import com.perfect11.login_signup.dto.UserDto;
import com.perfect11.payment.paytm.Api;
import com.perfect11.payment.paytm.Checksum;
import com.perfect11.payment.paytm.Constants;
import com.perfect11.payment.paytm.Paytm;
import com.perfect11.payment.wrapper.WalletWrapper;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.utility.DialogUtility;
import com.utility.PreferenceUtility;
import com.utility.customView.CustomEditText;

import org.json.JSONObject;

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


public class MyAccountFragment extends BaseFragment implements PaytmPaymentTransactionCallback, PaymentResultListener {
    private TextView tv_total_balance, tv_unutilized, tv_winnings;
    private MyAccountWrapper myAccountWrapper;
    private ApiInterface apiInterface;
    private UserDto userDto;
    private String paymentGateway, amount;
    private HttpLoggingInterceptor interceptor = null;
    private OkHttpClient client = null;
    private Gson gson;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_myaccount, container, false);
        setHeader("My Wallet");
        userDto = (UserDto) PreferenceUtility.getObjectInAppPreference(getActivity(), PreferenceUtility.APP_PREFERENCE_NAME);
        initView();
        callAPI();
        return view;
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

        Call<MyAccountWrapper> call = apiInterface.getMyAccountDetails(userDto.member_id);
        call.enqueue(new Callback<MyAccountWrapper>() {
            @Override
            public void onResponse(Call<MyAccountWrapper> call, Response<MyAccountWrapper> response) {
                myAccountWrapper = response.body();

                Log.e("UpcomingMatchesAPI", myAccountWrapper.toString());
                tv_total_balance.setText(myAccountWrapper.data.total_balance);
                userDto.total_balance = myAccountWrapper.data.total_balance;
                PreferenceUtility.saveObjectInAppPreference(getActivity(), userDto, PreferenceUtility.APP_PREFERENCE_NAME);
                tv_unutilized.setText(myAccountWrapper.data.cash_bonus);
                tv_winnings.setText(myAccountWrapper.data.winnings);
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<MyAccountWrapper> call, Throwable t) {
                Log.e("TAG", t.toString());
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }

    private void initView() {
        tv_total_balance = view.findViewById(R.id.tv_total_balance);
        tv_unutilized = view.findViewById(R.id.tv_unutilized);
        tv_winnings = view.findViewById(R.id.tv_winnings);
    }

    public static Fragment newInstance() {
        return new MyAccountFragment();
    }

    @Override
    public void onButtonClick(View view) {
        super.onButtonClick(view);
        switch (view.getId()) {
            case R.id.btn_transactions:
                ((BaseHeaderActivity) getActivity()).addFragment(MyTransactionsFragment.newInstance(), true, MyTransactionsFragment.class.getName());
                break;
            case R.id.btn_add_cash:
                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        callAPI();
                    }
                });
                dialog.setContentView(R.layout.custom_dialog_payment);
                dialog.show();
                final RadioGroup rg_01 = dialog.findViewById(R.id.rg_01);
                RelativeLayout rl_ticket = dialog.findViewById(R.id.rl_ticket);
                final CustomEditText et_amount = dialog.findViewById(R.id.et_amount);
                final CustomEditText et_ticket = dialog.findViewById(R.id.et_ticket);
                et_amount.setVisibility(View.GONE);
                rl_ticket.setVisibility(View.VISIBLE);
                rg_01.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.rb_paytm:
                                paymentGateway = "Paytm";
                                et_amount.setVisibility(View.VISIBLE);
                                break;
                            case R.id.rb_razorpay:
                                paymentGateway = "Razorpay";
                                et_amount.setVisibility(View.VISIBLE);
                                break;
                        }
                    }
                });
                Button btn_ok = dialog.findViewById(R.id.btn_ok);
                Button btn_apply = dialog.findViewById(R.id.btn_apply);
                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (rg_01.getCheckedRadioButtonId() == -1) {
                            DialogUtility.showMessageWithOk("Please select any one payment gateway", getActivity());
                        } else {
                            if (!et_amount.getText().toString().equalsIgnoreCase("") || et_amount.getText().toString().equalsIgnoreCase("0")) {
                                if (paymentGateway.equalsIgnoreCase("Paytm")) {
                                    amount = et_amount.getText().toString().trim();
                                    generateCheckSum(et_amount.getText().toString().trim());
                                } else {
                                    amount = et_amount.getText().toString().trim();
                                    startPayment(et_amount.getText().toString().trim());
//                                ActivityController.startNextActivity(getActivity(), PaymentRazorPayActivity.class, true);
                                }
                                dialog.dismiss();
                            } else {
                                DialogUtility.showMessageWithOk("Please enter an amount", getActivity());
                            }
                        }
                    }
                });
                btn_apply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!et_ticket.getText().toString().equalsIgnoreCase("")) {
                            callTicketApi(et_ticket.getText().toString().trim());
                        } else {
                            DialogUtility.showMessageWithOk("Please enter a ticket number", getActivity());
                        }
                    }
                });
//                ((BaseHeaderActivity) getActivity()).addFragment(PaymentFragment.newInstance(), true, PaymentFragment.class.getName());
                break;
        }
    }

    private void callTicketApi(String ticket) {
        /*
         * Collecting data*/
        Log.d("API", "Get Player");
        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();
        apiInterface = ApiClient3.getApiClient().create(ApiInterface.class);

        Call<InviteDto> call = apiInterface.ticketSaveCall(userDto.member_id, ticket);
        call.enqueue(new Callback<InviteDto>() {
            @Override
            public void onResponse(Call<InviteDto> call, Response<InviteDto> response) {
                DialogUtility.showMessageWithOk(response.body().message, getActivity());

                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<InviteDto> call, Throwable t) {
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
        System.out.println("transactionId " + transactionId + " bankTransactionId " + bankTransactionId);
        callAddWalletAPI(transactionId, amount, "paytm");
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
            callAddWalletAPI(razorpayPaymentID, amount, "razorpay");
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

    private void callAddWalletAPI(String transactionId, String amount, String type) {
        //API
        /*
         * Collecting data*/
        Log.d("API", "Get Player");
        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();
        apiInterface = ApiClient3.getApiClient().create(ApiInterface.class);

        Call<WalletWrapper> call = apiInterface.addwallet(transactionId, amount, type, userDto.member_id);
        call.enqueue(new Callback<WalletWrapper>() {
            @Override
            public void onResponse(Call<WalletWrapper> call, Response<WalletWrapper> response) {
                DialogUtility.showMessageWithOk(response.body().message, getActivity());

                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<WalletWrapper> call, Throwable t) {
                Log.e("TAG", t.toString());
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }

}
