package com.perfect11.contest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.perfect11.R;
import com.perfect11.base.BaseFragment;
import com.perfect11.login_signup.LoginActivity;
import com.perfect11.requestHandler.ApplicationServiceRequestHandler;
import com.utility.ActivityController;
import com.utility.AlertDialogCallBack;
import com.utility.CommonUtility;
import com.utility.DialogUtility;

public class InviteCodeFragment extends BaseFragment {
private AutoCompleteTextView invite;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
        view=inflater.inflate(R.layout.fragment_invite_code,container,false);
        initView();
        setInnerHeader("Invite Code");
        return view;
    }

    private void initView() {
        invite=view.findViewById(R.id.invite);
        invite.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptInvite();
                    return true;
                }
                return false;
            }
        });
    }

    private void attemptInvite() {
        if(isValid())
        {
            callAPI();
        }
    }

    private boolean isValid() {
        System.out.println("invite:"+invite.length());
    if(invite.getText().toString().trim().length()==0)
    {
        invite.requestFocus();
        invite.setError("Enter Invite Code");
        return false;
    }
        return true;
    }

    public void onButtonClick(View view) {
        switch(view.getId())
        {
            case R.id.img_back:
                getActivity().onBackPressed();
                break;
            case R.id.btn_reset:
                attemptInvite();
                break;
        }
    }

    private void callAPI() {

    }

    public void serviceCallback(String message) {

    }

    public static Fragment newInstance() {
    return new InviteCodeFragment();
    }
}
