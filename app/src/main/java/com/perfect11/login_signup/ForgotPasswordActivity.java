package com.perfect11.login_signup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.perfect11.R;
import com.perfect11.requestHandler.ApplicationServiceRequestHandler;
import com.utility.ActivityController;
import com.utility.AlertDialogCallBack;
import com.utility.CommonUtility;
import com.utility.Constants;
import com.utility.DialogUtility;

public class ForgotPasswordActivity extends AppCompatActivity {
private AutoCompleteTextView email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        initView();
    }

    private void initView() {
        email=findViewById(R.id.email);
        email.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptResetPassword();
                    return true;
                }
                return false;
            }
        });
    }

    private void attemptResetPassword() {
        if(isValid())
        {
            callAPI();
        }
    }

    private boolean isValid() {
        System.out.println("email:"+email.length());
    if(email.getText().toString().trim().length()==0)
    {
        email.requestFocus();
        email.setError("Enter an email ID");
        return false;
    }else if(CommonUtility.validateEmail(email.getText().toString()))
    {
        email.requestFocus();
        email.setError("Invalid email ID");
        return false;
    }
        return true;
    }

    public void onButtonClick(View view) {
        switch(view.getId())
        {
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.btn_reset:
                attemptResetPassword();
                break;
        }
    }

    private void callAPI() {
        //API
        new ApplicationServiceRequestHandler(this, new String[]{"email","weblink"},
                new Object[]{email.getText().toString().trim(),"perfect11"},
                "Loading...", ApplicationServiceRequestHandler.FORGET_PASSWORD,Constants.BASE_URL);
    }

    public void serviceCallback(String message) {
        DialogUtility.showMessageOkWithCallback(message, this, new AlertDialogCallBack() {
            @Override
            public void onSubmit() {
                ActivityController.startNextActivity(ForgotPasswordActivity.this,LoginActivity.class,true);
            }

            @Override
            public void onCancel() {

            }
        });
    }
}
