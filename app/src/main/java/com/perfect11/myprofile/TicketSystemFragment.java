package com.perfect11.myprofile;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.perfect11.R;
import com.perfect11.base.ApiClient;
import com.perfect11.base.ApiInterface;
import com.perfect11.base.BaseFragment;
import com.perfect11.login_signup.dto.InviteDto;
import com.perfect11.login_signup.dto.UserDto;
import com.utility.DialogUtility;
import com.utility.PreferenceUtility;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TicketSystemFragment extends BaseFragment {
    private AutoCompleteTextView ticket;
    private UserDto userDto;
    private ApiInterface apiInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_ticket_system, container, false);

        initView();
        setHeader("Ticket System");
        return view;
    }

    private void initView() {
        userDto = (UserDto) PreferenceUtility.getObjectInAppPreference(getActivity(), PreferenceUtility.APP_PREFERENCE_NAME);
        ticket = view.findViewById(R.id.ticket);
        ticket.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attempt();
                    return true;
                }
                return false;
            }
        });
    }

    private void attempt() {
        if (isValid()) {
            callAPI();
        }
    }

    private boolean isValid() {
//        System.out.println("invite:" + ticket.length());
        if (ticket.getText().toString().trim().length() == 0) {
            ticket.requestFocus();
            ticket.setError("Enter Your Ticket");
            return false;
        }
        return true;
    }

    public void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                getActivity().onBackPressed();
                break;
            case R.id.btn_submit:
                attempt();
                break;
        }
    }

    private void callAPI() {
        //API
        //API
        /**
         * Collecting data*/
        Log.d("API", "Get Player");
        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<InviteDto> call = apiInterface.ticketSaveCall(userDto.member_id, ticket.getText().toString().trim());
        call.enqueue(new Callback<InviteDto>() {
            @Override
            public void onResponse(Call<InviteDto> call, Response<InviteDto> response) {
                DialogUtility.showMessageWithOk(response.body().message, getActivity());

                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<InviteDto> call, Throwable t) {
                Log.e("TAG", t.toString());
                if (t instanceof IOException) {
                    DialogUtility.showConnectionErrorDialogWithOk(getActivity());
                    // logging probably not necessary
                } else {
                    Toast.makeText(getActivity(), "Conversion issue! big problems :(", Toast.LENGTH_SHORT).show();
                    // todo log to some central bug tracking service
                }
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }


    public static Fragment newInstance() {
        return new TicketSystemFragment();
    }
}
