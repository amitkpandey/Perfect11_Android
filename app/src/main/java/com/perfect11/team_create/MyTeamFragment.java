package com.perfect11.team_create;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.perfect11.R;
import com.perfect11.base.BaseFragment;
import com.perfect11.contest.dto.JoinedContestDto;
import com.perfect11.home.service.BackgroundReceiver;
import com.perfect11.home.service.BackgroundScoreUpdateService;
import com.perfect11.login_signup.dto.UserDto;
import com.utility.PreferenceUtility;


public class MyTeamFragment extends BaseFragment {
    private UserDto userDto;
    private JoinedContestDto joinedContestDto;
    private BackgroundReceiver backgroundReceiver;

    public static MyTeamFragment newInstance() {
        return new MyTeamFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.my_team, container, false);
        userDto = (UserDto) PreferenceUtility.getObjectInAppPreference(getActivity(), PreferenceUtility.APP_PREFERENCE_NAME);
        initView();
        readFromBundle();
        setInnerHeader("My Team");
        startBackgroundService();
        return view;
    }

    private void initView() {
        backgroundReceiver = new BackgroundReceiver(new BackgroundReceiver.onMessageUpDateListener() {

            @Override
            public void onMessageUpdate() {

            }
        });

        getActivity().registerReceiver(backgroundReceiver, new IntentFilter("com.perfect11.home.service.BackgroundReceiver"));
    }

    private void readFromBundle() {
        joinedContestDto = (JoinedContestDto) getArguments().getSerializable("joinedContestDto");
    }

    private void startBackgroundService() {
        Intent intent = new Intent();
        intent.putExtra("matchId", joinedContestDto.matchID);
        intent.putExtra("contestId", joinedContestDto.contestId);
        intent.setClass(getActivity(), BackgroundScoreUpdateService.class);
        //mActivity.sendBroadcast(intent);
        getActivity().startService(intent);
    }

    public void onButtonClick(View view) {
        super.onButtonClick(view);
        switch (view.getId()) {
            case R.id.img_back:
                getActivity().onBackPressed();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(backgroundReceiver);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(backgroundReceiver);
    }
}
