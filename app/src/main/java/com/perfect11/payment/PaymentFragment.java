package com.perfect11.payment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.perfect11.R;
import com.perfect11.base.BaseFragment;
import com.perfect11.base.BaseHeaderActivity;

public class PaymentFragment extends BaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_payment, container, false);
        setInnerHeader("Payment");
        return view;
    }

    @Override
    public void onButtonClick(View view) {
        super.onButtonClick(view);
        switch (view.getId()) {
            case R.id.img_back:
                getActivity().onBackPressed();
                break;
            case R.id.btn_save_card:
                ((BaseHeaderActivity) getActivity()).addFragment(ConfirmFragment.newInstance(), false, ConfirmFragment.class.getName());
                break;
            case R.id.btn_submit:
                ((BaseHeaderActivity) getActivity()).addFragment(ConfirmFragment.newInstance(), false, ConfirmFragment.class.getName());
                break;
        }
    }

    public static Fragment newInstance() {
        return new PaymentFragment();
    }
}
