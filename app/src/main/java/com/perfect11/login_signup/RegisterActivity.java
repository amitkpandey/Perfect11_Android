package com.perfect11.login_signup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.perfect11.R;
import com.perfect11.requestHandler.ApplicationServiceRequestHandler;
import com.perfect11.team_create.dto.ContestDto;
import com.perfect11.team_create.dto.PlayerDto;
import com.perfect11.upcoming_matches.dto.UpComingMatchesDto;
import com.utility.ActivityController;
import com.utility.AlertDialogCallBack;
import com.utility.CommonUtility;
import com.utility.Constants;
import com.utility.DatePickerUtility;
import com.utility.DateUtility;
import com.utility.DialogUtility;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {
    private EditText confirm_password, password;
    private AutoCompleteTextView name, email, phone, zipcode;
    private ContestDto contestDto = null;
    private TextView dob;
    private boolean flag = false;
    private ArrayList<PlayerDto> selectedTeam;
    private UpComingMatchesDto upComingMatchesDto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        readFromBundle();
    }

    private void readFromBundle() {
        try {
            flag = getIntent().getExtras().getBoolean("flag");
            contestDto = (ContestDto) getIntent().getExtras().getSerializable("contestDto");
            selectedTeam = (ArrayList<PlayerDto>) getIntent().getExtras().getSerializable("selectedTeam");
            upComingMatchesDto = (UpComingMatchesDto) getIntent().getExtras().getSerializable("upComingMatchesDto");

//            System.out.println(contestDto.toString() + " Flag:" + flag);
//            Log.e("Login:", contestDto.toString() + upComingMatchesDto.toString() + selectedTeam.size() + flag);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initView() {
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        dob = findViewById(R.id.dob);
        zipcode = findViewById(R.id.zipcode);
        password = findViewById(R.id.password);
        confirm_password = findViewById(R.id.confirm_password);
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
        } else if (phone.getText().toString().trim().length() != 10) {
            phone.requestFocus();
            phone.setError("Enter a valid Phone Number");
            return false;
        } else if (dob.getText().toString().trim().length() == 0) {
            dob.requestFocus();
            dob.setError("Enter a Date of Birth");
            return false;
        } else if (zipcode.getText().toString().trim().length() != 6) {
            zipcode.requestFocus();
            zipcode.setError("Enter a valid Zip Code");
            return false;
        } else if (password.getText().toString().length() == 0) {
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
        } else if (!confirm_password.getText().toString().trim().equals(password.getText().toString().trim())) {
            confirm_password.requestFocus();
            confirm_password.setError("Mismatch password");
            return false;
        }
        return true;
    }

    public void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.ll_login:
                gotoLogin();
                break;
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.email_register_button:
                attemptRegister();
                break;
            case R.id.img_google:
                Toast.makeText(this, "Google", Toast.LENGTH_SHORT).show();
                break;
            case R.id.img_fb:
                Toast.makeText(this, "Facebook", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rl_dob:
            case R.id.dob:
                System.out.println("dob");
                DatePickerUtility.createDatePickerDialog(RegisterActivity.this, dob, false).show();
                break;
            case R.id.tv_terms:
                ActivityController.startNextActivity(this, TermsConditionsActivity.class, false);
                break;
        }
    }

    private void callRegisterAPI() {
        //API
        String str[] = name.getText().toString().trim().split(" ");
        new ApplicationServiceRequestHandler(this, new String[]{"phone", "email", "password", "first_name", "last_name", "weblink", "device_type", "phone", "dob", "zipcode"},
                new Object[]{"", email.getText().toString().trim(), password.getText().toString().trim(), str[0], (str.length >= 2) ? str[1] : "", "perfect11", "android", phone.getText().toString().trim(), DateUtility.convertDataToGivitDateFormatForMyAccount(dob.getText().toString().trim()), zipcode.getText().toString().trim()},
                "Loading...", ApplicationServiceRequestHandler.SIGN_UP, Constants.BASE_URL);
    }

    public void serviceCallbackSignUP(String message, final String member_id) {
        DialogUtility.showMessageOkWithCallback("Registration Successful", this, new AlertDialogCallBack() {
            @Override
            public void onSubmit() {
                gotoInviteCode(member_id);
            }

            @Override
            public void onCancel() {

            }
        });
    }

    private void gotoLogin() {
        Bundle bundle = new Bundle();
        Log.e("flag", "" + flag);
        if (flag) {
            bundle.putSerializable("upComingMatchesDto", upComingMatchesDto);
            bundle.putSerializable("selectedTeam", selectedTeam);
            bundle.putSerializable("contestDto", contestDto);
        }
        bundle.putBoolean("flag", flag);
        ActivityController.startNextActivity(RegisterActivity.this, LoginActivity.class, bundle, true);
    }

    private void gotoInviteCode(String member_id) {
        Bundle bundle = new Bundle();
        Log.e("flag", "" + flag);
        if (flag) {
            bundle.putSerializable("upComingMatchesDto", upComingMatchesDto);
            bundle.putSerializable("selectedTeam", selectedTeam);
            bundle.putSerializable("contestDto", contestDto);
        }
        bundle.putBoolean("flag", flag);
        bundle.putString("member_id", member_id);
        ActivityController.startNextActivity(RegisterActivity.this, InviteActivity.class, bundle, true);
    }
}
