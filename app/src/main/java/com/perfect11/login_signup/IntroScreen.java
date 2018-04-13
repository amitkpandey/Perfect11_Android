package com.perfect11.login_signup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.perfect11.BuildConfig;
import com.perfect11.R;
import com.perfect11.base.ApiClient;
import com.perfect11.base.ApiInterface;
import com.perfect11.login_signup.dto.PictureDto;
import com.perfect11.login_signup.wrapper.PictureWrapper;
import com.perfect11.upcoming_matches.UpcomingMatchesActivity;
import com.squareup.picasso.Picasso;
import com.utility.ActivityController;
import com.utility.AlertDialogCallBack;
import com.utility.CommonUtility;
import com.utility.DialogUtility;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IntroScreen extends AppCompatActivity {
    private ImageView iv_image1, iv_image2, iv_image3, iv_image4;
    private ApiInterface apiInterface;
    private int versionCode;
    private ArrayList<PictureDto> pictureDtoArrayList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_screen);
        initView();
        versionCode = BuildConfig.VERSION_CODE;
        callVersionCheckAPI();
    }

    private void callVersionCheckAPI() {
        //API
        final ProgressDialog mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<JsonObject> call = apiInterface.checkVersion("Android", String.valueOf(versionCode));

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject jsonObject = response.body();
                boolean status = jsonObject.get("status").getAsBoolean();
                if (!status) {
                    DialogUtility.showMessageWithOkWithCallback("New version available",
                            "You are using old version. Please update to get more features.",
                            IntroScreen.this, new AlertDialogCallBack() {
                                @Override
                                public void onSubmit() {
                                    String url = "https://play.google.com/store/apps/details?id=com.perfect11";
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse(url));
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void onCancel() {

                                }
                            });
                } else {
                    callAPI();
                }
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                if (t instanceof IOException) {
                    DialogUtility.showConnectionErrorDialogWithOk(IntroScreen.this);
                    // logging probably not necessary
                } else {
                    Toast.makeText(IntroScreen.this, "Conversion issue! big problems :(", Toast.LENGTH_SHORT).show();
                    // todo log to some central bug tracking service
                }
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }

    private void callAPI() {
        //API
        /**
         * Collecting data*/
        Log.d("API", "Get Picture");
       /* final ProgressDialog mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();*/

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<PictureWrapper> call = apiInterface.getPictureList();

        call.enqueue(new Callback<PictureWrapper>() {
            @Override
            public void onResponse(Call<PictureWrapper> call, Response<PictureWrapper> response) {
                pictureDtoArrayList = response.body().data;
                if (pictureDtoArrayList != null) {
                    int i = 0;
                    for (PictureDto pictureDto : pictureDtoArrayList) {
                        switch (i) {
                            case 0:
                                if (!pictureDto.picture.equals("")) {
                                    Picasso.with(IntroScreen.this).load(pictureDto.picture)
                                            .placeholder(R.drawable.progress_animation)
                                            .error(R.drawable.squrecorner_image_bg)
                                            .into(iv_image1);
                                }
                                break;
                            case 1:
                                if (!pictureDto.picture.equals("")) {
                                    Picasso.with(IntroScreen.this).load(pictureDto.picture)
                                            .placeholder(R.drawable.progress_animation).
                                            error(R.drawable.squrecorner_image_bg)
                                            .into(iv_image2);
                                }
                                break;
                            case 2:
                                if (!pictureDto.picture.equals("")) {
                                    Picasso.with(IntroScreen.this).load(pictureDto.picture)
                                            .placeholder(R.drawable.progress_animation).
                                            error(R.drawable.squrecorner_image_bg)
                                            .into(iv_image3);
                                }
                                break;
                            case 3:
                                if (!pictureDto.picture.equals("")) {
                                    Picasso.with(IntroScreen.this).load(pictureDto.picture)
                                            .placeholder(R.drawable.progress_animation).
                                            error(R.drawable.squrecorner_image_bg)
                                            .into(iv_image4);
                                }
                                break;
                        }
                        i++;
                    }
                }
            }

            @Override
            public void onFailure(Call<PictureWrapper> call, Throwable t) {
                if (t instanceof IOException) {
                    DialogUtility.showConnectionErrorDialogWithOk(IntroScreen.this);
                    // logging probably not necessary
                } else {
                    Toast.makeText(IntroScreen.this, "Conversion issue! big problems :(", Toast.LENGTH_SHORT).show();
                    // todo log to some central bug tracking service
                }
            }
        });
    }

    private void initView() {
        //4 Image of Intro Screen
        iv_image1 = findViewById(R.id.iv_image1);
        iv_image2 = findViewById(R.id.iv_image2);
        iv_image3 = findViewById(R.id.iv_image3);
        iv_image4 = findViewById(R.id.iv_image4);
    }

    public void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                ActivityController.startNextActivity(IntroScreen.this, LoginActivity.class, false);
                break;
            case R.id.btn_register:
                ActivityController.startNextActivity(this, RegisterActivity.class, false);
                break;
            case R.id.ctv_skip:
                if (CommonUtility.checkConnectivity(this)) {
                    ActivityController.startNextActivity(this, UpcomingMatchesActivity.class, false);
                } else {
                    DialogUtility.showConnectionErrorDialogWithOk(this);
                }
                break;
        }
    }
}
