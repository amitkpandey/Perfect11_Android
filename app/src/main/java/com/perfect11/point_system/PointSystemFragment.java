package com.perfect11.point_system;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.perfect11.R;
import com.perfect11.base.BaseFragment;
import com.perfect11.base.BaseHeaderActivity;
import com.perfect11.home.childFragments.FixturesFragment;
import com.perfect11.home.childFragments.LiveFragment;
import com.perfect11.home.childFragments.ResultsFragment;
import com.perfect11.point_system.childFragments.BattingPointSystemFragment;
import com.perfect11.team_create.CreateTeamActivity;

import java.util.ArrayList;
import java.util.List;

public class PointSystemFragment extends BaseFragment {
//    private ViewPager viewPager;
//    private TabLayout tabLayout;
    private  WebView myWebView;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
        view=inflater.inflate(R.layout.fragment_point_system,container,false);
        setHeader("Point System");
        initView();
        return view;
    }

    public static Fragment newInstance() {
        return new PointSystemFragment();
    }

    private void initView() {
         myWebView = (WebView)view.findViewById(R.id.webview);
      setValues();
      /*  viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        ((BaseHeaderActivity)getActivity()).slideMenu.addIgnoredView(viewPager);
        tabLayout=view.findViewById(R.id.tabs);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);*/
    }

    private void setValues() {
        initializeProgressBar();
        myWebView.getSettings().setJavaScriptEnabled(true); // enable javascript
        myWebView.getSettings().setLoadWithOverviewMode(true);
        myWebView.getSettings().setUseWideViewPort(true);

        myWebView.setWebViewClient(new WebViewClient() {
                                       public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                                           Toast.makeText(getActivity(), description, Toast.LENGTH_SHORT).show();
                                       }

                                       @Override
                                       public void onPageStarted(WebView view, String url, Bitmap favicon) {
                                           progressDialog.show();
                                       }


                                       @Override
                                       public void onPageFinished(WebView view, String url) {
                                           progressDialog.dismiss();
                                       }
                                   }

        );
        myWebView.loadUrl("http://www.perfect11.in/page/point_system_for_app");
    }

    private void initializeProgressBar() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    onDestroy();
                }
            });
        }
    }

   /* private void setupViewPager(ViewPager viewPager) {
        System.out.println("List");
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
      *//*  adapter.addFragment(new BattingPointSystemFragment(), "Fixtures");
        adapter.addFragment(new BattingPointSystemFragment(), "Live");
        adapter.addFragment(new BattingPointSystemFragment(), "Results");
*//*
        adapter.addFragment(new BattingPointSystemFragment(), "Batting");
        adapter.addFragment(new BattingPointSystemFragment(), "Balling");
        adapter.addFragment(new BattingPointSystemFragment(), "Fielding");
        adapter.addFragment(new BattingPointSystemFragment(), "Others");
        adapter.addFragment(new BattingPointSystemFragment(), "Economy Rate");
        adapter.addFragment(new BattingPointSystemFragment(), "Strike Rate");
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
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }*/
}
