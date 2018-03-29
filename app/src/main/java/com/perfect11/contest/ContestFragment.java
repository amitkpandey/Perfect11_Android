package com.perfect11.contest;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.perfect11.R;
import com.perfect11.base.ApiClient;
import com.perfect11.base.ApiInterface;
import com.perfect11.base.BaseFragment;
import com.perfect11.base.BaseHeaderActivity;
import com.perfect11.contest.adapter.ContestListAdapter;
import com.perfect11.contest.wrapper.JoinedContestWrapper;
import com.perfect11.contest.wrapper.TeamWrapper;
import com.perfect11.login_signup.dto.UserDto;
import com.perfect11.team_create.CreateTeamFragment;
import com.perfect11.team_create.SelectPlayersFragment;
import com.perfect11.team_create.dto.ContestDto;
import com.perfect11.team_create.wrapper.ContestWrapper;
import com.perfect11.upcoming_matches.dto.UpComingMatchesDto;
import com.utility.DialogUtility;
import com.utility.PreferenceUtility;
import com.utility.customView.CustomButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class ContestFragment extends BaseFragment {

    private StickyListHeadersListView lv_contests;
    private ApiInterface apiInterface;
    private UpComingMatchesDto upComingMatchesDto;
    private ContestListAdapter contestListAdapter;
    private UserDto userDto;
    private ContestWrapper contestWrapper;
    private JoinedContestWrapper joinedContestWrapper;
    private TeamWrapper teamWrapper;
    private CustomButton btn_my_team, btn_join_contest;
    private int team_size = 0;

    public static ContestFragment newInstance() {
        return new ContestFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.contest_layout, container, false);
        userDto = (UserDto) PreferenceUtility.getObjectInAppPreference(getActivity(), PreferenceUtility.APP_PREFERENCE_NAME);
        setInnerHeader("Contest");
        initView();
        readFromBundle();
        return view;
    }

    private void initView() {
        lv_contests = view.findViewById(R.id.lv_contests);
        btn_join_contest = view.findViewById(R.id.btn_join_contest);
        btn_my_team = view.findViewById(R.id.btn_my_team);
    }

    private void readFromBundle() {
        upComingMatchesDto = (UpComingMatchesDto) getArguments().getSerializable("upComingMatchesDto");
//        System.out.println("upComingMatchesDto:" + upComingMatchesDto.toString());
        callAPI();
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

        Call<ContestWrapper> call = apiInterface.getContestList(upComingMatchesDto.key_name, "all", "all", "all");
        call.enqueue(new Callback<ContestWrapper>() {
            @Override
            public void onResponse(Call<ContestWrapper> call, Response<ContestWrapper> response) {
                contestWrapper = response.body();

                Log.e("UpcomingMatchesAPI", contestWrapper.toString());
                if (contestWrapper.status) {
                    callMyJoinedContestApi();
//                    System.out.println(contestWrapper.data.size());
//                    setAdapter(contestWrapper.data);
                } else {
                    DialogUtility.showMessageWithOk(contestWrapper.message, getActivity());
                }
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ContestWrapper> call, Throwable t) {
                Log.e("TAG", t.toString());
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });

    }

    private void callMyJoinedContestApi() {
        Log.d("API", "Get Player");
        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<JoinedContestWrapper> call = apiInterface.getUserContest(upComingMatchesDto.key_name, userDto.member_id);
        call.enqueue(new Callback<JoinedContestWrapper>() {
            @Override
            public void onResponse(Call<JoinedContestWrapper> call, Response<JoinedContestWrapper> response) {
                joinedContestWrapper = response.body();
                callMyJoinedTeamApi();
//                Log.e("UpcomingMatchesAPI", joinedContestWrapper.toString());

                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<JoinedContestWrapper> call, Throwable t) {
                Log.e("TAG", t.toString());
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }

    private void callMyJoinedTeamApi() {
        Log.d("API", "Get Player");
        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<TeamWrapper> teamWrapperCall = apiInterface.getTeamList(upComingMatchesDto.key_name, userDto.member_id);
        teamWrapperCall.enqueue(new Callback<TeamWrapper>() {
            @Override
            public void onResponse(Call<TeamWrapper> call, Response<TeamWrapper> response) {
                teamWrapper = response.body();
                team_size = teamWrapper.data.size();

//                Log.e("UpcomingMatchesAPI", joinedContestWrapper.toString());
                setAdapter(contestWrapper.data);
                if (joinedContestWrapper.data != null && joinedContestWrapper.data.size() > 0)
                    btn_join_contest.setText("Joined Contests (" + joinedContestWrapper.data.size() + ")");
                else
                    btn_join_contest.setText("Joined Contests");

                if (teamWrapper.data != null && teamWrapper.data.size() > 0)
                    btn_my_team.setText("My Team (" + teamWrapper.data.size() + ")");
                else
                    btn_my_team.setText("My Team");
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<TeamWrapper> call, Throwable t) {
                Log.e("TAG", t.toString());
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }

    private void setAdapter(ArrayList<ContestDto> data) {
        contestListAdapter = new ContestListAdapter(getActivity(), data);
        lv_contests.setAdapter(contestListAdapter);
        contestListAdapter.setOnItemClickListener(new ContestListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onJoinClick(ContestDto contestDto) {
                String[] team = upComingMatchesDto.short_name.split(" ");
                String team1 = team[0];
                String team2 = team[2];
                Bundle bundle = new Bundle();
                if (teamWrapper.data != null && teamWrapper.data.size() > 0) {
                    bundle.putString("team1", team1);
                    bundle.putString("team2", team2);
                    bundle.putSerializable("contestDto", contestDto);
                    bundle.putSerializable("upComingMatchesDto", upComingMatchesDto);
                    bundle.putSerializable("teamDto", teamWrapper.data);
                    CreateTeamFragment createTeamFragment = CreateTeamFragment.newInstance();
                    createTeamFragment.setArguments(bundle);
                    ((BaseHeaderActivity) getActivity()).addFragment(createTeamFragment, true, CreateTeamFragment.class.getName());
                } else {
                    bundle.putSerializable("contestDto", contestDto);
                    bundle.putSerializable("upComingMatchesDto", upComingMatchesDto);
                    SelectPlayersFragment selectPlayersFragment = new SelectPlayersFragment();
                    selectPlayersFragment.setArguments(bundle);
                    ((BaseHeaderActivity) getActivity()).addFragment(selectPlayersFragment, true, SelectPlayersFragment.class.getName());

                }
            }
        });
    }

    public void onButtonClick(View view) {
        super.onButtonClick(view);
        Bundle bundle;
        String[] team = upComingMatchesDto.short_name.split(" ");
        String team1 = team[0];
        String team2 = team[2];

        switch (view.getId()) {
            case R.id.img_back:
                getActivity().onBackPressed();
                break;
            case R.id.btn_create_contest:
//                if(team_size==0){
//                    DialogUtility.showMessageWithOk("To Create a contest, you have to create a team",getActivity());
//                }else {
                bundle = new Bundle();
                bundle.putSerializable("upComingMatchesDto", upComingMatchesDto);
                bundle.putSerializable("joinedContestDto", joinedContestWrapper.data);
                CreateContestFragment createContestFragment = CreateContestFragment.newInstance();
                createContestFragment.setArguments(bundle);
                ((BaseHeaderActivity) getActivity()).addFragment(createContestFragment, true, CreateContestFragment.class.getName());
//                }
                break;
            case R.id.btn_join_contest:
                if (joinedContestWrapper.data != null && joinedContestWrapper.data.size() > 0) {
                    bundle = new Bundle();
                    bundle.putString("team1", team1);
                    bundle.putString("team2", team2);
                    bundle.putString("teamA", upComingMatchesDto.teama);
                    bundle.putString("teamB", upComingMatchesDto.teamb);
                    bundle.putString("matchStatus", upComingMatchesDto.matchstatus);
                    bundle.putSerializable("joinedContestDto", joinedContestWrapper.data);
                    JoinContestFragment joinContestFragment = JoinContestFragment.newInstance();
                    joinContestFragment.setArguments(bundle);
                    ((BaseHeaderActivity) getActivity()).addFragment(joinContestFragment, true, JoinContestFragment.class.getName());
                }
                break;
            case R.id.btn_my_team:
                if (teamWrapper.data != null && teamWrapper.data.size() > 0) {
                    bundle = new Bundle();
                    bundle.putString("team1", team1);
                    bundle.putString("team2", team2);
                    bundle.putSerializable("upComingMatchesDto", upComingMatchesDto);
                    bundle.putSerializable("teamDto", teamWrapper.data);
                    CreateTeamFragment createTeamFragment = CreateTeamFragment.newInstance();
                    createTeamFragment.setArguments(bundle);
                    ((BaseHeaderActivity) getActivity()).addFragment(createTeamFragment, true, CreateTeamFragment.class.getName());
                } else {
                    SelectPlayersFragment selectPlayersFragment = new SelectPlayersFragment();
                    Bundle bundle1 = new Bundle();
                    bundle1.putSerializable("upComingMatchesDto", upComingMatchesDto);
                    selectPlayersFragment.setArguments(bundle1);
                    ((BaseHeaderActivity) getActivity()).addFragment(selectPlayersFragment, true, SelectPlayersFragment.class.getName());

                }
                break;
        }
    }
}
