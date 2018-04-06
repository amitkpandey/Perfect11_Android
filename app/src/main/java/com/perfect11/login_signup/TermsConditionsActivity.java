package com.perfect11.login_signup;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.perfect11.R;

public class TermsConditionsActivity extends AppCompatActivity {

    private WebView wv_01;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view_layout);
        // Makes Progress bar Visible
        getWindow().setFeatureInt(Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON);
        readFromBundle();
        initView();
        setValues();
    }

    private void readFromBundle() {

    }

    private void initView() {
        wv_01 = (WebView) findViewById(R.id.wv_01);
        wv_01.setBackgroundColor(Color.WHITE);
        wv_01.setBackgroundDrawable(null);
    }

    private void setValues() {
        initializeProgressBar();
        wv_01.getSettings().setJavaScriptEnabled(true); // enable javascript

        wv_01.getSettings().setLoadWithOverviewMode(true);
        wv_01.getSettings().setUseWideViewPort(true);
        wv_01.getSettings().setBuiltInZoomControls(true);

        wv_01.setWebViewClient(new WebViewClient() {
                                   public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                                       Toast.makeText(TermsConditionsActivity.this, description, Toast.LENGTH_SHORT).show();
                                   }

                                   @Override
                                   public void onPageStarted(WebView view, String url, Bitmap favicon) {
                                       progressDialog.show();
                                   }


                                   @Override
                                   public void onPageFinished(WebView view, String url) {
                                       progressDialog.dismiss();
//                                       String webUrl = wv_01.getUrl();
                                   }
                               }

        );
        wv_01.loadUrl("http://perfect11.in/page/term_of_use/mobile");
        System.out.println("loadUrl " + wv_01.getUrl());
    }

    private void initializeProgressBar() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    finish();
                }
            });
        }
    }

    public void onButtonClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
        }

    }
}
