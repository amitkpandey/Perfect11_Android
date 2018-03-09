package com.perfect11.myprofile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.perfect11.R;
import com.perfect11.base.BaseFragment;
import com.perfect11.base.BaseHeaderActivity;
import com.perfect11.login_signup.LoginActivity;
import com.perfect11.requestHandler.ApplicationServiceRequestHandler;
import com.utility.ActivityController;
import com.utility.AlertDialogCallBack;
import com.utility.CommonUtility;
import com.utility.Constants;
import com.utility.DialogUtility;

import java.sql.SQLOutput;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChangeProfileFragment extends BaseFragment {
    private EditText confirm_password, password;
    private AutoCompleteTextView name, email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view=inflater.inflate(R.layout.fragment_change_profile,container,false);
        setInnerHeader("Update Profile");
        initView();
        return view;
    }

    private void initView() {

        name = (AutoCompleteTextView) view.findViewById(R.id.name);
        email = (AutoCompleteTextView) view.findViewById(R.id.email);
        password = (EditText) view.findViewById(R.id.password);
        confirm_password = (EditText) view.findViewById(R.id.confirm_password);
        confirm_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptRegister();
                    return true;
                }
                return false;
            }
        });
    }

    private void attemptRegister() {
        if (isValid()) {
            callRegisterAPI();
        }
    }

    private boolean isValid() {
        if (name.getText().toString().trim().length() == 0) {
            name.requestFocus();
            name.setError("Enter your name");
            return false;
        } else if (email.getText().toString().trim().length() == 0) {
            email.requestFocus();
            email.setError("Enter your email");
            return false;
        } else if (CommonUtility.validateEmail(email.getText().toString().trim())) {
            email.requestFocus();
            email.setError("Enter a valid email");
            return false;
        }else if (password.getText().toString().length() == 0) {
            password.requestFocus();
            password.setError("Enter a password");
            return false;
        } else if (password.getText().toString().length() < 6) {
            password.requestFocus();
            password.setError("Password should be at least 6 digit");
            return false;
        } else if (confirm_password.getText().toString().length() == 0) {
            confirm_password.requestFocus();
            confirm_password.setError("Enter a password");
            return false;
        } else if (confirm_password.getText().toString().length() < 6) {
            confirm_password.requestFocus();
            confirm_password.setError("Password should be at least 6 digit");
            return false;
        }else if (!confirm_password.getText().toString().trim().equals(password.getText().toString().trim())) {
            confirm_password.requestFocus();
            confirm_password.setError("Mismatch password");
            return false;
        }
        return true;
    }

    public void onButtonClick(View view) {
        switch (view.getId()) {

            case R.id.img_back:
                getActivity().onBackPressed();
                break;
            case R.id.change_profile_button:
                attemptRegister();
                break;
        }
    }

    private void callRegisterAPI() {
        //API
        String str[]=name.getText().toString().trim().split(" ");
        new ApplicationServiceRequestHandler(getActivity(),this, new String[]{"phone", "email","password","first_name","last_name","weblink","device_type"},
                new Object[]{"",email.getText().toString().trim(), password.getText().toString().trim(),str[0],(str.length>=2)?str[1]:"","perfect11","android"},
                "Loading...", ApplicationServiceRequestHandler.SIGN_UP, Constants.BASE_URL);
    }

    public void serviceCallbackSignUP(String message) {
        DialogUtility.showMessageOkWithCallback(message, getActivity(), new AlertDialogCallBack() {
            @Override
            public void onSubmit() {

            }

            @Override
            public void onCancel() {

            }
        });
    }
}
