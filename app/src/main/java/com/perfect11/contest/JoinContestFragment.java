package com.perfect11.contest;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.perfect11.R;
import com.perfect11.base.ApiClient;
import com.perfect11.base.ApiInterface;
import com.perfect11.base.BaseFragment;
import com.perfect11.base.BaseHeaderActivity;
import com.perfect11.contest.adapter.JoinContestAdapter;
import com.perfect11.contest.dto.JoinedContestDto;
import com.perfect11.contest.wrapper.JoinedContestWrapper;
import com.perfect11.login_signup.dto.UserDto;
import com.perfect11.upcoming_matches.dto.UpComingMatchesDto;
import com.utility.DialogUtility;
import com.utility.PreferenceUtility;
import com.utility.customView.CustomTextView;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JoinContestFragment extends BaseFragment {
    private RecyclerView rv_contests;
    private CustomTextView tv_match, tv_status;
    private ApiInterface apiInterface;
    private UpComingMatchesDto upComingMatchesDto;
    private ArrayList<JoinedContestDto> joinedContestDtoArrayList;
    private JoinContestAdapter joinContestAdapter;
    private UserDto userDto;
    private String team1, teamA, team2, teamB, matchStatus;
    private boolean isFixture=false;

    public static JoinContestFragment newInstance() {
        return new JoinContestFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.join_contest_layout, container, false);
        setInnerHeader("Joined Contests");
        initView();
        readFromBundle();
        setValues();
        return view;
    }

    private void readFromBundle() {
        userDto = (UserDto) PreferenceUtility.getObjectInAppPreference(getActivity(), PreferenceUtility.APP_PREFERENCE_NAME);
        try {
            upComingMatchesDto = (UpComingMatchesDto) getArguments().getSerializable("upComingMatchesDto");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            joinedContestDtoArrayList = (ArrayList<JoinedContestDto>) getArguments().getSerializable("joinedContestDto");
            team1 = getArguments().getString("team1");
            team2 = getArguments().getString("team2");
            teamA = getArguments().getString("teamA");
            teamB = getArguments().getString("teamB");
            isFixture=getArguments().getBoolean("isFixture");
            matchStatus = getArguments().getString("matchStatus");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        rv_contests = view.findViewById(R.id.rv_contests);
        tv_match = view.findViewById(R.id.tv_match);
        tv_status = view.findViewById(R.id.tv_status);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_contests.setLayoutManager(layoutManager);
    }

    private void setValues() {
        if (upComingMatchesDto != null) {
            tv_match.setText(upComingMatchesDto.teama + " vs " + upComingMatchesDto.teamb);
            tv_status.setText(upComingMatchesDto.matchstatus);
            String[] team = upComingMatchesDto.short_name.split(" ");
            team1 = team[0];
            team2 = team[2];
            teamA = upComingMatchesDto.teama;
            teamB = upComingMatchesDto.teamb;
            callAPI();
        } else {
            tv_match.setText(team1 + " vs " + team2);
            tv_status.setText(matchStatus);
            setAdapter(joinedContestDtoArrayList);
        }
    }

    @Override
    public void onButtonClick(View view) {
        super.onButtonClick(view);
        switch (view.getId()) {
            case R.id.img_back:
                getActivity().onBackPressed();
                break;
        }
    }

    private void callAPI() {
        //API
        /*
         * Collecting data*/
        Log.d("API", "Get Player");
        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<JoinedContestWrapper> call = apiInterface.getJoinedContestList(upComingMatchesDto.key_name,
                userDto.member_id);
        call.enqueue(new Callback<JoinedContestWrapper>() {
            @Override
            public void onResponse(Call<JoinedContestWrapper> call, Response<JoinedContestWrapper> response) {
                JoinedContestWrapper joinedContestWrapper = response.body();

//                Log.e("JoinedContestWrapperAPI", joinedContestWrapper.toString());
                if (joinedContestWrapper.status) {
                    System.out.println(joinedContestWrapper.data.size());
                    setAdapter(joinedContestWrapper.data);
                } else {
                    DialogUtility.showMessageWithOk(joinedContestWrapper.message, getActivity());
                }
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<JoinedContestWrapper> call, Throwable t) {
                Log.e("TAG", t.toString());
                if (t instanceof IOException) {
                    DialogUtility.showConnectionErrorDialogWithOk(getActivity());
                    // logging probably not necessary
                }
                else {
                    Toast.makeText(getActivity(), "Conversion issue! big problems :(", Toast.LENGTH_SHORT).show();
                    // todo log to some central bug tracking service
                }
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });

    }

    private void setAdapter(final ArrayList<JoinedContestDto> data) {
        joinContestAdapter = new JoinContestAdapter(getActivity(), data);
        rv_contests.setAdapter(joinContestAdapter);
        joinContestAdapter.setOnButtonListener(new JoinContestAdapter.OnButtonListener() {
            @Override
            public void onButtonClick(int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("joinedContestDto", data.get(position));
                bundle.putString("team1", team1);
                bundle.putString("team2", team2);
                bundle.putString("teamA", teamA);
                bundle.putString("teamB", teamB);
                bundle.putBoolean("isFixture",isFixture);
                if (upComingMatchesDto != null && upComingMatchesDto.matchstatus.equalsIgnoreCase("completed")) {
                    ResultLeaderBoardFragment resultLeaderBoardFragment = ResultLeaderBoardFragment.newInstance();
                    resultLeaderBoardFragment.setArguments(bundle);
                    ((BaseHeaderActivity) getActivity()).addFragment(resultLeaderBoardFragment, true, ResultLeaderBoardFragment.class.getName());
                } else {
                    LiveLeaderBoardFragment liveLeaderBoardFragment = LiveLeaderBoardFragment.newInstance();
                    liveLeaderBoardFragment.setArguments(bundle);
                    ((BaseHeaderActivity) getActivity()).addFragment(liveLeaderBoardFragment, true, LiveLeaderBoardFragment.class.getName());
                }
            }
        });

    }
}
