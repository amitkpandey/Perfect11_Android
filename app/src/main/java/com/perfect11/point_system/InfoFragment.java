package com.perfect11.point_system;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.perfect11.R;
import com.perfect11.base.BaseFragment;
import com.perfect11.base.BaseHeaderActivity;
import com.perfect11.payment.ConfirmFragment;

public class InfoFragment extends BaseFragment {
//    private ViewPager viewPager;
//    private TabLayout tabLayout;
    private  WebView myWebView;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
        view=inflater.inflate(R.layout.fragment_info,container,false);
        setHeader("Info System");
        initView();
        return view;
    }

    public static Fragment newInstance() {
        return new InfoFragment();
    }

    private void initView() {

    }

    @Override
    public void onButtonClick(View view) {
        super.onButtonClick(view);
        switch (view.getId()){
            case R.id.tv_link1:
                ((BaseHeaderActivity) getActivity()).addFragment(AboutUsFragment.newInstance(), true,
                        AboutUsFragment.class.getName());
                break;
            case R.id.tv_link2:
                ((BaseHeaderActivity) getActivity()).addFragment(HowToPlayFragment.newInstance(), true,
                        HowToPlayFragment.class.getName());
                break;
            case R.id.tv_link3:
                ((BaseHeaderActivity) getActivity()).addFragment(PrivacyPolicyFragment.newInstance(), true,
                        PrivacyPolicyFragment.class.getName());
                break;
            case R.id.tv_link4:
                ((BaseHeaderActivity) getActivity()).addFragment(LegalityFragment.newInstance(), true,
                        LegalityFragment.class.getName());
                break;
            case R.id.tv_link5:
                ((BaseHeaderActivity) getActivity()).addFragment(RefundPolicyFragment.newInstance(), true,
                        RefundPolicyFragment.class.getName());
                break;
            case R.id.tv_link6:
                ((BaseHeaderActivity) getActivity()).addFragment(FaqFragment.newInstance(), true,
                        FaqFragment.class.getName());
                break;
        }
    }
}
