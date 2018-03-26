package com.perfect11.account;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.perfect11.R;
import com.perfect11.account.wrapper.MyAccountWrapper;
import com.perfect11.base.ApiClient;
import com.perfect11.base.ApiInterface;
import com.perfect11.base.BaseFragment;
import com.perfect11.base.BaseHeaderActivity;
import com.perfect11.login_signup.dto.UserDto;
import com.perfect11.payment.PaymentFragment;
import com.utility.PreferenceUtility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyAccountFragment extends BaseFragment {
    private TextView tv_total_balance,tv_unutilized,tv_winnings;
    private MyAccountWrapper myAccountWrapper;
    private ApiInterface apiInterface;
    private UserDto userDto;
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
        /**
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
        tv_total_balance=view.findViewById(R.id.tv_total_balance);
        tv_unutilized=view.findViewById(R.id.tv_unutilized);
        tv_winnings=view.findViewById(R.id.tv_winnings);
    }

    public static Fragment newInstance() {
        return new MyAccountFragment();
    }

    @Override
    public void onButtonClick(View view) {
        super.onButtonClick(view);
    switch(view.getId()){
        case R.id.btn_transactions:
            ((BaseHeaderActivity)getActivity()).addFragment(MyTransactionsFragment.newInstance(),true,MyTransactionsFragment.class.getName());
            break;
        case R.id.btn_add_cash:
            ((BaseHeaderActivity)getActivity()).addFragment(PaymentFragment.newInstance(),true,PaymentFragment.class.getName());
            break;
    }
    }
}
