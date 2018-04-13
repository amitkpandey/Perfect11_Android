package com.perfect11.account;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.perfect11.R;
import com.perfect11.account.dto.WithdrawlStatusDto;
import com.perfect11.account.wrapper.MyAccountWrapper;
import com.perfect11.base.ApiClient;
import com.perfect11.base.ApiClient3;
import com.perfect11.base.ApiClient4;
import com.perfect11.base.ApiInterface;
import com.perfect11.base.AppConstant;
import com.perfect11.base.BaseFragment;
import com.perfect11.base.BaseHeaderActivity;
import com.perfect11.login_signup.dto.InviteDto;
import com.perfect11.login_signup.dto.UserDto;
import com.perfect11.payment.paytm.Checksum;
import com.perfect11.payment.paytm.Paytm;
import com.perfect11.payment.paytm.Transaction;
import com.perfect11.payment.wrapper.WalletWrapper;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.utility.AlertDialogCallBack;
import com.utility.CommonUtility;
import com.utility.Constants;
import com.utility.DialogUtility;
import com.utility.PreferenceUtility;
import com.utility.customView.CustomEditText;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.utility.Constants.TAG;


public class MyAccountFragment extends BaseFragment implements PaytmPaymentTransactionCallback {
    private TextView tv_total_balance, tv_unutilized, tv_winnings, tv_withdrawable;
    private MyAccountWrapper myAccountWrapper;
    private ApiInterface apiInterface;
    private UserDto userDto;
    private String paymentGateway, amount;
    private Gson gson;
    private boolean flag = false;
    private float withdrawl_amount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_myaccount, container, false);
        setHeader("My Wallet");
        userDto = (UserDto) PreferenceUtility.getObjectInAppPreference(getActivity(), PreferenceUtility.APP_PREFERENCE_NAME);
        initView();
        readFromBundle();
        callAPI();
        return view;
    }

    private void readFromBundle() {
        try {
            flag = getArguments().getBoolean("flag");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (flag) {
            setInnerHeader("My Wallet");
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
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<MyAccountWrapper> call = apiInterface.getMyAccountDetails(userDto.member_id);
        call.enqueue(new Callback<MyAccountWrapper>() {
            @Override
            public void onResponse(Call<MyAccountWrapper> call, Response<MyAccountWrapper> response) {
                myAccountWrapper = response.body();

                Log.e("UpcomingMatchesAPI", myAccountWrapper.toString());
                tv_total_balance.setText("Rs." + myAccountWrapper.data.total_balance + "/-");
                userDto.total_balance = myAccountWrapper.data.total_balance;
                PreferenceUtility.saveObjectInAppPreference(getActivity(), userDto, PreferenceUtility.APP_PREFERENCE_NAME);
                float unutilized=(Float.parseFloat(myAccountWrapper.data.total_balance) - Float.parseFloat(myAccountWrapper.data.winnings));

                if(unutilized<0)
                {
                    unutilized=0;
                }
                tv_unutilized.setText("Rs." + unutilized + "/-");
                tv_winnings.setText("Rs." + myAccountWrapper.data.winnings + "/-");
                withdrawl_amount = Float.parseFloat(myAccountWrapper.data.withdraw_amount);
                tv_withdrawable.setText("Rs." + myAccountWrapper.data.withdraw_amount + "/-");
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<MyAccountWrapper> call, Throwable t) {
                Log.e("TAG", t.toString());

                if (t instanceof IOException) {
                    DialogUtility.showConnectionErrorDialogWithOk(getActivity());
                    // logging probably not necessary
                } else {
                    Toast.makeText(getActivity(), "Conversion issue! big problems :(", Toast.LENGTH_SHORT).show();
                    // todo log to some central bug tracking service
                }

                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }

    private void initView() {
        tv_total_balance = view.findViewById(R.id.tv_total_balance);
        tv_unutilized = view.findViewById(R.id.tv_unutilized);
        tv_winnings = view.findViewById(R.id.tv_winnings);
        tv_withdrawable = view.findViewById(R.id.tv_withdrawable);
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
            case R.id.img_back:
                getActivity().onBackPressed();
                break;
            case R.id.btn_withdraw:
                System.out.println("withdrawl_amount:" + withdrawl_amount);
                if (withdrawl_amount != 0.0) {
                    DialogUtility.showCustomConformationYesNO(getActivity(), "Are you sure that you want to withdraw money?", new AlertDialogCallBack() {
                        @Override
                        public void onSubmit() {
                            call_Withdrawl_API();
                        }

                        @Override
                        public void onCancel() {

                        }
                    });

                } else {
                    Toast.makeText(getActivity(), "Your account has no money to withdraw.", Toast.LENGTH_SHORT).show();
                }
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

    private void call_Withdrawl_API() {
        /*
         * Collecting data*/
        Log.d("API", "Get Player");
        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<WithdrawlStatusDto> call = apiInterface.withdrawMoneyRequest(userDto.member_id, withdrawl_amount);
        call.enqueue(new Callback<WithdrawlStatusDto>() {
            @Override
            public void onResponse(Call<WithdrawlStatusDto> call, Response<WithdrawlStatusDto> response) {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();

                if (response.body().error == 1)
                    DialogUtility.showCustomMessageOk(getActivity(), "Dear " + userDto.first_name + " " + userDto.last_name + " please verify your account info and KYC from website perfect11.in first,then you will be eligible to withdraw wining amount from app");
                else
                    DialogUtility.showCustomMessageOk(getActivity(), response.body().message);

            }

            @Override
            public void onFailure(Call<WithdrawlStatusDto> call, Throwable t) {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();

                if (t instanceof IOException) {
                    DialogUtility.showConnectionErrorDialogWithOk(getActivity());
                    // logging probably not necessary
                } else {
                    Toast.makeText(getActivity(), "Conversion issue! big problems :(", Toast.LENGTH_SHORT).show();
                    // todo log to some central bug tracking service
                }
            }
        });
    }

    private void callTicketApi(String ticket) {
        /*
         * Collecting data*/
        Log.d("API", "Get Player");
        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

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
                if (t instanceof IOException) {
                    DialogUtility.showConnectionErrorDialogWithOk(getActivity());
                    // logging probably not necessary
                } else {
                    Toast.makeText(getActivity(), "Conversion issue! big problems :(", Toast.LENGTH_SHORT).show();
                    // todo log to some central bug tracking service
                }
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
            options.put("description", "Add Wallet");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://rzp-mobile.s3.amazonaws.com/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", Float.valueOf(amount) * 100);
            options.put("theme", new JSONObject("{color: '#E93D29'}"));
           /* JSONObject preFill = new JSONObject();
            preFill.put("email", "test@razorpay.com");
            preFill.put("contact", "9876543210");

            options.put("prefill", preFill);*/

            co.open(getActivity(), options);
        } catch (Exception e) {
            System.out.println("e.getMessage() " + e.getMessage());
            Toast.makeText(getActivity(), "Error in payment: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }


    private void generateCheckSum(String amount) {
        //getting the tax amount first.

        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
        apiInterface = ApiClient4.getApiClient().create(ApiInterface.class);

        //creating paytm object
        //containing all the values required
        final Paytm paytm = new Paytm(Constants.M_ID, Constants.CHANNEL_ID, amount, Constants.WEBSITE, Constants.CALLBACK_URL, Constants.INDUSTRY_TYPE_ID);

        //creating a call object from the apiService
        Call<Checksum> call = apiInterface.getChecksum(paytm.getmId(), paytm.getOrderId(), paytm.getCustId(), paytm.getChannelId(), paytm.getTxnAmount(),
                paytm.getWebsite(), paytm.getCallBackUrl(), paytm.getIndustryTypeId());

        //making the call to generate checksum
        call.enqueue(new Callback<Checksum>() {
            @Override
            public void onResponse(Call<Checksum> call, Response<Checksum> response) {

                //once we get the checksum we will initiailize the payment.
                //the method is taking the checksum we got and the paytm object as the parameter
                initializePaytmPayment(response.body().getChecksumHash(), paytm);
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Checksum> call, Throwable t) {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();

            }
        });
    }

    private void initializePaytmPayment(String checksumHash, Paytm paytm) {

        //getting paytm service
//        PaytmPGService Service = PaytmPGService.getStagingService();

        //use this when using for production
        PaytmPGService Service = PaytmPGService.getProductionService();

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
//        paramMap.put("MOBILE_NO", "9830063231");
//        paramMap.put("EMAIL_ID", userDto.email);


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
        String orderId = bundle.getString("ORDERID");
        callTransactionAPI(orderId);
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
//    @SuppressWarnings("unused")
//    @Override
//    public void onPaymentSuccess(String razorpayPaymentID) {
//        try {
//            callAddWalletAPI(razorpayPaymentID, amount, "razorpay");
//            Toast.makeText(getActivity(), "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
//        } catch (Exception e) {
//            Log.e(TAG, "Exception in onPaymentSuccess", e);
//        }
//    }
//
//    /**
//     * The name of the function has to be onPaymentError
//     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
//     */
//    @SuppressWarnings("unused")
//    @Override
//    public void onPaymentError(int code, String response) {
//        try {
//            System.out.println("code " + code + " response " + response);
//            Toast.makeText(getActivity(), "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
//        } catch (Exception e) {
//            Log.e(TAG, "Exception in onPaymentError", e);
//        }
//    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                //Toast.makeText(this,"TEST"+resultCode,Toast.LENGTH_SHORT).show();
                if (data != null) {
                    String razorpayPaymentID = data.getExtras().getString("razorpayPaymentID");
                    callAddWalletAPI(razorpayPaymentID, amount, "razorpay");

                }
            } else {
                Toast.makeText(getActivity(), "Payment failed", Toast.LENGTH_SHORT).show();
            }
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
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<WalletWrapper> call = apiInterface.addwallet(transactionId, amount, type, userDto.member_id);
        call.enqueue(new Callback<WalletWrapper>() {
            @Override
            public void onResponse(Call<WalletWrapper> call, Response<WalletWrapper> response) {
                DialogUtility.showMessageWithOk(response.body().message, getActivity());
                tv_total_balance.setText(response.body().data.balanceavailable);

                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<WalletWrapper> call, Throwable t) {
                Log.e("TAG", t.toString());


                if (t instanceof IOException) {
                    DialogUtility.showConnectionErrorDialogWithOk(getActivity());
                    // logging probably not necessary
                } else {
                    Toast.makeText(getActivity(), "Conversion issue! big problems :(", Toast.LENGTH_SHORT).show();
                    // todo log to some central bug tracking service
                }
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }

    private void callTransactionAPI(String orderId) {
        Log.d("API", "Get Player");
        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
        apiInterface = ApiClient4.getApiClient().create(ApiInterface.class);

        Call<Transaction> call = apiInterface.getStatus(orderId);
        call.enqueue(new Callback<Transaction>() {
            @Override
            public void onResponse(Call<Transaction> call, final Response<Transaction> response) {
                if (response.body().sTATUS.equalsIgnoreCase("TXN_SUCCESS") ||
                        response.body().sTATUS.equalsIgnoreCase("PENDING")) {
                    DialogUtility.showMessageOkWithCallback("Payment Successful", getActivity(), new AlertDialogCallBack() {
                        @Override
                        public void onSubmit() {
                            callAddWalletAPI(response.body().tXNID, response.body().tXNAMOUNT, "paytm");
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                } else {
                    DialogUtility.showMessageWithOk("Payment Failure", getActivity());
                }

                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Transaction> call, Throwable t) {
                Log.e("TAG", t.toString());
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }

}
