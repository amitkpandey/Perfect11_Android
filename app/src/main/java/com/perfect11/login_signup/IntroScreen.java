package com.perfect11.login_signup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.perfect11.R;
import com.perfect11.upcoming_matches.UpcomingMatchesActivity;
import com.utility.ActivityController;

public class IntroScreen extends AppCompatActivity {
private ImageView iv_image1,iv_image2,iv_image3,iv_image4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_screen);
    initView();
    }

    private void initView() {
        //4 Image of Intro Screen
        iv_image1=findViewById(R.id.iv_image1);
        iv_image2=findViewById(R.id.iv_image2);
        iv_image3=findViewById(R.id.iv_image3);
        iv_image4=findViewById(R.id.iv_image4);
    }

    public void onButtonClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_login:
                ActivityController.startNextActivity(IntroScreen.this,LoginActivity.class,false);
                break;
            case R.id.btn_register:
                ActivityController.startNextActivity(this,RegisterActivity.class,false);
                break;
            case R.id.ctv_skip:
                ActivityController.startNextActivity(this, UpcomingMatchesActivity.class,false);
                break;
        }
    }
}
