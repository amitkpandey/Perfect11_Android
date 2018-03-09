package com.perfect11.account;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.perfect11.R;
import com.perfect11.base.BaseFragment;
import com.perfect11.base.BaseHeaderActivity;
import com.perfect11.contest.MyTransactionsFragment;
import com.perfect11.payment.PaymentFragment;


public class MyAccountFragment extends BaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_myaccount, container, false);
        setHeader("My Account");
        return view;
    }

    public static Fragment newInstance() {
        return new MyAccountFragment();
    }

    @Override
    public void onButtonClick(View view) {
        super.onButtonClick(view);
    switch(view.getId()){
        case R.id.btn_transactions:
            ((BaseHeaderActivity)getActivity()).addFragment(MyTransactionsFragment.newInstance(),false,MyTransactionsFragment.class.getName());
            break;
        case R.id.btn_add_cash:
            ((BaseHeaderActivity)getActivity()).addFragment(PaymentFragment.newInstance(),false,PaymentFragment.class.getName());
            break;

    }
    }
}
