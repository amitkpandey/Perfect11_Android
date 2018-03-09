package com.perfect11.base;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.perfect11.R;
import com.perfect11.login_signup.IntroScreen;
import com.perfect11.login_signup.dto.UserDto;
import com.perfect11.upcoming_matches.UpcomingMatchesActivity;
import com.utility.ActivityController;
import com.utility.PreferenceUtility;

/**
 * Created by Developer on 09-02-2018.
 */

public class SplashScreen extends AppCompatActivity {
    private int PLASH_TIME_OUT = 2000;
    private SharedPreferences sharedPreferences;
    private UserDto userDto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //For Making Full Screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        // Move on another activity after a time span
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startNextActivity();
            }
        }, PLASH_TIME_OUT);

    }

    private void startNextActivity() {
        // Check App is Running First Time or not
        //Start Check
        /** sharedPreferences=getSharedPreferences("first_time_or_not",SplashScreen.this.MODE_PRIVATE);
         SharedPreferences.Editor editor=sharedPreferences.edit();
         boolean firstTime=sharedPreferences.getBoolean("is_first_time",true);
         //End Check
         if(firstTime)
         {
         editor.putBoolean("is_first_time",false);
         editor.commit();

         }else{}*/
        userDto = (UserDto) PreferenceUtility.getObjectInAppPreference(this, PreferenceUtility.APP_PREFERENCE_NAME);

        if (userDto == null) {
            ActivityController.startNextActivity(SplashScreen.this, IntroScreen.class, true);
        } else {
            ActivityController.startNextActivity(SplashScreen.this, BaseHeaderActivity.class, true);
       }
    }
}
