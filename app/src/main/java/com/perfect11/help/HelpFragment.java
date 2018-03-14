package com.perfect11.help;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.perfect11.R;
import com.perfect11.base.BaseFragment;

public class HelpFragment extends BaseFragment {
    /*private ViewPager viewPager;
    private TabLayout tabLayout;*/
    private WebView myWebView;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_help, container, false);
        setHeader("Help");
        initView();
        return view;
    }

    public static HelpFragment newInstance() {
        return new HelpFragment();
    }

    private void initView() {
        myWebView = view.findViewById(R.id.webview);
        setValues();
        /*viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        ((BaseHeaderActivity)getActivity()).slideMenu.addIgnoredView(viewPager);
        tabLayout=view.findViewById(R.id.tabs);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getBackground();*/
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
        myWebView.loadUrl("http://52.15.50.179/point-system");
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

  /*   private void setupViewPager(ViewPager viewPager) {
        System.out.println("List");
        HelpViewPagerAdapter help_adapter = new HelpViewPagerAdapter(getChildFragmentManager());
        help_adapter.addFragment(new BattingFragment(), "Batting");
        help_adapter.addFragment(new BattingFragment(), "Balling");
        help_adapter.addFragment(new BattingFragment(), "Fielding");
        help_adapter.addFragment(new BattingFragment(), "Others");
        help_adapter.addFragment(new BattingFragment(), "Economy Rate");
        help_adapter.addFragment(new BattingFragment(), "Strike Rate");
        viewPager.setAdapter(help_adapter);
    }

   class HelpViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public HelpViewPagerAdapter(FragmentManager manager) {
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
    }*/

}
