package com.perfect11.login_signup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.perfect11.R;
import com.perfect11.base.ApiClient;
import com.perfect11.base.ApiInterface;
import com.perfect11.login_signup.dto.PictureDto;
import com.perfect11.login_signup.wrapper.PictureWrapper;
import com.perfect11.upcoming_matches.UpcomingMatchesActivity;
import com.squareup.picasso.Picasso;
import com.utility.ActivityController;
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
    private ArrayList<PictureDto> pictureDtoArrayList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_screen);
        initView();
        callAPI();
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
