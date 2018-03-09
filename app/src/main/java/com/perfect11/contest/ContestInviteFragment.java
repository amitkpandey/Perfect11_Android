package com.perfect11.contest;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.perfect11.R;
import com.perfect11.base.BaseFragment;


public class ContestInviteFragment extends BaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view=inflater.inflate(R.layout.fragment_contest_invite,container,false);
        setHeader("Invite Friends");
        return view;
    }

    @Override
    public void onButtonClick(View view) {
        super.onButtonClick(view);
        switch (view.getId())
        {
            case R.id.img_back:
                getActivity().onBackPressed();
                break;
        }
    }

    public static Fragment newInstance() {
        return new ContestInviteFragment();
    }
}
