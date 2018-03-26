package com.perfect11.contest;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.perfect11.R;
import com.perfect11.base.BaseFragment;
import com.perfect11.contest.dto.ContestCallBackDto;
import com.utility.customView.CustomTextView;


public class ContestInviteFragment extends BaseFragment {
    private ContestCallBackDto contestCallBackDto;
    private CustomTextView ctv_share_code;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view=inflater.inflate(R.layout.fragment_contest_invite,container,false);
        setInnerHeader("Invite Friends");
        readFromBundle();
        initView();
        return view;
    }

    private void initView() {
        ctv_share_code=view.findViewById(R.id.ctv_share_code);
        ctv_share_code.setText(contestCallBackDto.contest_code);
    }

    private void readFromBundle() {
        contestCallBackDto= (ContestCallBackDto) getArguments().getSerializable("contestCallBackDto");
    }

    @Override
    public void onButtonClick(View view) {
        super.onButtonClick(view);
        switch (view.getId())
        {
            case R.id.img_back:
                getActivity().onBackPressed();
                break;
            case R.id.btn_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Perfect11 Contest ID: "+contestCallBackDto.contest_code);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
        }
    }

    public static Fragment newInstance() {
        return new ContestInviteFragment();
    }
}
