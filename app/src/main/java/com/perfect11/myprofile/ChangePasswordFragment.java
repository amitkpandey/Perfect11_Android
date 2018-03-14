package com.perfect11.myprofile;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.perfect11.R;
import com.perfect11.base.BaseFragment;
import com.perfect11.login_signup.dto.UserDto;
import com.perfect11.requestHandler.ApplicationServiceRequestHandler;
import com.utility.AlertDialogCallBack;
import com.utility.CommonUtility;
import com.utility.Constants;
import com.utility.DialogUtility;
import com.utility.PreferenceUtility;

public class ChangePasswordFragment extends BaseFragment {
    private EditText confirm_password, password,old_password;
    private UserDto userDto;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view=inflater.inflate(R.layout.fragment_change_password,container,false);
        setInnerHeader("Change Password");
        initView();
        return view;
    }

    private void initView() {
        userDto = (UserDto) PreferenceUtility.getObjectInAppPreference(getActivity(), PreferenceUtility.APP_PREFERENCE_NAME);
        old_password=(EditText) view.findViewById(R.id.old_password);
        password = (EditText) view.findViewById(R.id.password);
        confirm_password = (EditText) view.findViewById(R.id.confirm_password);
        confirm_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptCall();
                    return true;
                }
                return false;
            }
        });
    }

    private void attemptCall() {
        if (isValid()) {
            callAPI();
        }
    }

    private boolean isValid() {

        if (old_password.getText().toString().length() == 0) {
            old_password.requestFocus();
            old_password.setError("Enter your old password");
            return false;
        } else if (old_password.getText().toString().length() < 6) {
            old_password.requestFocus();
            old_password.setError("Password should be at least 6 digit");
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
                attemptCall();
                break;
        }
    }

    private void callAPI() {
        //API

        new ApplicationServiceRequestHandler(getActivity(),this, new String[]{"old_password", "new_password","member_id"},
                new Object[]{old_password.getText().toString(),confirm_password.getText().toString(),userDto.member_id},
                "Loading...", ApplicationServiceRequestHandler.CHANGEPASSWORD, Constants.BASE_URL);
    }

    public void serviceCallback(String message) {
        DialogUtility.showMessageOkWithCallback(message, getActivity(), new AlertDialogCallBack() {
            @Override
            public void onSubmit() {
                getActivity().onBackPressed();
            }

            @Override
            public void onCancel() {

            }
        });
    }
}
