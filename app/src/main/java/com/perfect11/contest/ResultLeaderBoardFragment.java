package com.perfect11.contest;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.perfect11.R;
import com.perfect11.base.ApiClient;
import com.perfect11.base.ApiClient2;
import com.perfect11.base.ApiInterface;
import com.perfect11.base.BaseFragment;
import com.perfect11.base.BaseHeaderActivity;
import com.perfect11.contest.adapter.PracticeContestAdapter;
import com.perfect11.contest.dto.JoinedContestDto;
import com.perfect11.contest.dto.LiveLeaderboardDto;
import com.perfect11.contest.dto.TeamDto;
import com.perfect11.contest.wrapper.TeamWrapper;
import com.perfect11.login_signup.dto.UserDto;
import com.perfect11.team_create.ResultTeamFragment;
import com.utility.DialogUtility;
import com.utility.PreferenceUtility;
import com.utility.customView.CustomButton;
import com.utility.customView.CustomTextView;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ResultLeaderBoardFragment extends BaseFragment {
    private RecyclerView rv_contests;
    private CustomTextView tv_total_win, tv_entry_fee, tv_match, tv_status;
    private CustomButton btn_save;

    private JoinedContestDto joinedContestDto;
    private UserDto userDto;

    private PracticeContestAdapter practiceContestAdapter;

    private ApiInterface apiInterface;
    private String team1, team2, teamA, teamB, matchStatus;
    private RelativeLayout rl_footer;
    private String userTeamId;

    public static ResultLeaderBoardFragment newInstance() {
        return new ResultLeaderBoardFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.my_contest_layout, container, false);
        readFromBundle();
        setInnerHeader(joinedContestDto.room_name);
        initView();
        callTeamApi();
        return view;
    }

    private void readFromBundle() {
        userDto = (UserDto) PreferenceUtility.getObjectInAppPreference(getActivity(), PreferenceUtility.APP_PREFERENCE_NAME);
        joinedContestDto = (JoinedContestDto) getArguments().getSerializable("joinedContestDto");
//        team1 = getArguments().getString("team1");
//        team2 = getArguments().getString("team2");
//        teamA = getArguments().getString("teamA");
//        teamB = getArguments().getString("teamB");
        matchStatus = getArguments().getString("matchStatus");
    }

    private void initView() {
        tv_match = view.findViewById(R.id.tv_match);
        tv_status = view.findViewById(R.id.tv_status);
        tv_total_win = view.findViewById(R.id.tv_total_win);
        tv_entry_fee = view.findViewById(R.id.tv_entry_fee);
        rv_contests = view.findViewById(R.id.rv_contests);
        rl_footer = view.findViewById(R.id.rl_footer);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_contests.setLayoutManager(layoutManager);
        btn_save = view.findViewById(R.id.btn_save);
    }

    private void setValues() {
        tv_match.setText(team1 + " vs " + team2);
        tv_status.setText(matchStatus);
        tv_total_win.setText("Rs. " + joinedContestDto.winingamount + "/-");
        tv_entry_fee.setText("Rs. " + joinedContestDto.amount + "/-");
        btn_save.setText("My Team");
    }

    @Override
    public void onButtonClick(View view) {
        super.onButtonClick(view);
        switch (view.getId()) {
            case R.id.img_back:
                getActivity().onBackPressed();
                break;
            case R.id.btn_save:
                Bundle bundle = new Bundle();
                bundle.putString("matchId", joinedContestDto.matchID);
                bundle.putString("reference_id", userDto.reference_id);
                bundle.putString("teamId", userTeamId);
                bundle.putString("team1", team1);
                bundle.putString("team2", team2);
                bundle.putString("teamA", teamA);
                bundle.putString("teamB", teamB);
                ResultTeamFragment resultTeamFragment = ResultTeamFragment.newInstance();
                resultTeamFragment.setArguments(bundle);
                ((BaseHeaderActivity) getActivity()).addFragment(resultTeamFragment, true, ResultTeamFragment.class.getName());
                break;
        }
    }

    private void callTeamApi() {
        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<TeamWrapper> call = apiInterface.getPlayerLiveScore(joinedContestDto.team_id, joinedContestDto.matchID);
        call.enqueue(new Callback<TeamWrapper>() {
            @Override
            public void onResponse(Call<TeamWrapper> call, Response<TeamWrapper> response) {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                TeamWrapper teamWrapper = response.body();
                if (teamWrapper != null && teamWrapper.data != null && teamWrapper.data.size() > 0) {
//                    team1 = teamWrapper.d
                    TeamDto teamDto = teamWrapper.data.get(0);
                    String[] team = teamDto.short_name.split(" ");
                    team1 = team[0];
                    team2 = team[2];
                    teamA = teamDto.teama;
                    teamB = teamDto.teamb;
                    setValues();
                    callAPI();
                }
            }

            @Override
            public void onFailure(Call<TeamWrapper> call, Throwable t) {
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

    private void callAPI() {
        //API
        /*
         * Collecting data*/
        Log.d("API", "Get Player");
        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();
        apiInterface = ApiClient2.getApiClient().create(ApiInterface.class);

        Call<ArrayList<LiveLeaderboardDto>> call = apiInterface.getLeaderBoardList(joinedContestDto.matchID, joinedContestDto.contestId);
        call.enqueue(new Callback<ArrayList<LiveLeaderboardDto>>() {
            @Override
            public void onResponse(Call<ArrayList<LiveLeaderboardDto>> call, Response<ArrayList<LiveLeaderboardDto>> response) {
                for (LiveLeaderboardDto liveLeaderboardDto : response.body()) {
//                    System.out.println("userDto.reference_id " + userDto.reference_id);
                    if (userDto.reference_id.trim().toLowerCase().equalsIgnoreCase(liveLeaderboardDto.reference_id.trim().toLowerCase())) {
                        System.out.println("userDto.reference_id " + userDto.reference_id);
                        rl_footer.setVisibility(View.VISIBLE);
                        userTeamId = "" + liveLeaderboardDto.team_id;
                        break;
                    } else {
                        rl_footer.setVisibility(View.GONE);
                    }
                }
                setAdapter(response.body());
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ArrayList<LiveLeaderboardDto>> call, Throwable t) {
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

    private void setAdapter(final ArrayList<LiveLeaderboardDto> data) {
        practiceContestAdapter = new PracticeContestAdapter(getActivity(), data);
        rv_contests.setAdapter(practiceContestAdapter);
        practiceContestAdapter.setOnButtonListener(new PracticeContestAdapter.OnButtonListener() {
            @Override
            public void onButtonClick(int position) {
                Bundle bundle = new Bundle();
                bundle.putString("matchId", joinedContestDto.matchID);
                bundle.putString("reference_id", data.get(position).reference_id);
                bundle.putString("teamId", "" + data.get(position).team_id);
                bundle.putString("team1", team1);
                bundle.putString("team2", team2);
                bundle.putString("teamA", teamA);
                bundle.putString("teamB", teamB);
                ResultTeamFragment resultTeamFragment = ResultTeamFragment.newInstance();
                resultTeamFragment.setArguments(bundle);
                ((BaseHeaderActivity) getActivity()).addFragment(resultTeamFragment, true, ResultTeamFragment.class.getName());
            }
        });
    }
}
