package com.perfect11.login_signup;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.perfect11.R;
import com.perfect11.base.ApiClient;
import com.perfect11.base.ApiInterface;
import com.perfect11.login_signup.dto.InviteDto;
import com.perfect11.team_create.dto.ContestDto;
import com.perfect11.team_create.dto.PlayerDto;
import com.perfect11.upcoming_matches.dto.UpComingMatchesDto;
import com.utility.ActivityController;
import com.utility.AlertDialogCallBack;
import com.utility.DialogUtility;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InviteActivity extends AppCompatActivity {
    private AutoCompleteTextView invite;
    private ContestDto contestDto = null;
    private boolean flag = false;
    private ArrayList<PlayerDto> selectedTeam;
    private UpComingMatchesDto upComingMatchesDto;
    private String member_id;
    private ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_invite_code);

        readFromBundle();
        initView();
    }

    private void readFromBundle() {
        try {
            flag = getIntent().getExtras().getBoolean("flag");
            contestDto = (ContestDto) getIntent().getExtras().getSerializable("contestDto");
            selectedTeam = (ArrayList<PlayerDto>) getIntent().getExtras().getSerializable("selectedTeam");
            member_id = getIntent().getExtras().getString("member_id");
            upComingMatchesDto = (UpComingMatchesDto) getIntent().getExtras().getSerializable("upComingMatchesDto");
            System.out.println(contestDto.toString() + " Flag:" + flag + member_id);
//            Log.e("Login:", contestDto.toString() + upComingMatchesDto.toString() + selectedTeam.size() + flag);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initView() {
        invite = findViewById(R.id.invite);
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
        if (isValid()) {
            callAPI();
        }
    }

    private boolean isValid() {
        if (invite.getText().toString().trim().length() == 0) {
            invite.requestFocus();
            invite.setError("Enter Invite Code");
            return false;
        }
        return true;
    }

    public void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.ctv_skip:
                gotoLogin();
                break;
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.btn_submit:
                attemptInvite();
                break;
        }
    }

    private void callAPI() {
        //API
        //API
        /**
         * Collecting data*/
        Log.d("API", "Get Player");
        final ProgressDialog mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<InviteDto> call = apiInterface.inviteCall(member_id, invite.getText().toString().trim());
        call.enqueue(new Callback<InviteDto>() {
            @Override
            public void onResponse(Call<InviteDto> call, Response<InviteDto> response) {
                serviceCallback(response.body());

                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<InviteDto> call, Throwable t) {
                Log.e("TAG", t.toString());
                if (t instanceof IOException) {
                    DialogUtility.showConnectionErrorDialogWithOk(InviteActivity.this);
                    // logging probably not necessary
                } else {
                    Toast.makeText(InviteActivity.this, "Conversion issue! big problems :(", Toast.LENGTH_SHORT).show();
                    // todo log to some central bug tracking service
                }
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }

    public void serviceCallback(final InviteDto body) {
        DialogUtility.showMessageOkWithCallback(body.message, this, new AlertDialogCallBack() {
            @Override
            public void onSubmit() {
                if (body.status)
                    gotoLogin();
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
        ActivityController.startNextActivity(InviteActivity.this, LoginActivity.class, bundle, true);
    }
}
