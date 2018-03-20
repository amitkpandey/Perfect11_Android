package com.perfect11.contest;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.perfect11.R;
import com.perfect11.base.ApiClient3;
import com.perfect11.base.ApiInterface;
import com.perfect11.base.BaseFragment;
import com.perfect11.login_signup.LoginActivity;
import com.perfect11.login_signup.dto.InviteDto;
import com.perfect11.login_signup.dto.UserDto;
import com.perfect11.requestHandler.ApplicationServiceRequestHandler;
import com.perfect11.team_create.dto.PlayerDto;
import com.perfect11.team_create.wrapper.PlayerWrapper;
import com.utility.ActivityController;
import com.utility.AlertDialogCallBack;
import com.utility.CommonUtility;
import com.utility.DialogUtility;
import com.utility.PreferenceUtility;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InviteCodeFragment extends BaseFragment {
private AutoCompleteTextView invite;
private ApiInterface apiInterface;
private UserDto userDto;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
        view=inflater.inflate(R.layout.fragment_invite_code,container,false);
        initView();
        setHeader("Join Contest");
        return view;
    }

    private void initView() {
        userDto = (UserDto) PreferenceUtility.getObjectInAppPreference(getActivity(), PreferenceUtility.APP_PREFERENCE_NAME);
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
        invite.setError("Enter Contest Invite Code");
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
        Log.d("API","Contest Invite");

        final ProgressDialog progressDialog=new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        apiInterface= ApiClient3.getApiClient().create(ApiInterface.class);


        Call<InviteDto> call = apiInterface.joinContestByContestCode(userDto.member_id,invite.getText().toString().trim());
        call.enqueue(new Callback<InviteDto>() {
            @Override
            public void onResponse(Call<InviteDto> call, Response<InviteDto> response) {

                    DialogUtility.showMessageWithOk(response.body().message, getActivity());

                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<InviteDto> call, Throwable t) {
                DialogUtility.showMessageWithOk(t.getMessage(), getActivity());
                Log.e("TAG", t.toString());
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        });
    }

    public static Fragment newInstance() {
    return new InviteCodeFragment();
    }
}
