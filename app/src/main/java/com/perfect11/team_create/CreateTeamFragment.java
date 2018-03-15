package com.perfect11.team_create;

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
import com.perfect11.contest.dto.TeamDto;
import com.perfect11.contest.dto.TeamPlayerDto;
import com.perfect11.home.HomeFragment;
import com.perfect11.home.dto.JoinContestCallBackDto;
import com.perfect11.login_signup.dto.UserDto;
import com.perfect11.team_create.adapter.CreateTeamAdapter;
import com.perfect11.team_create.dto.ContestDto;
import com.perfect11.upcoming_matches.dto.UpComingMatchesDto;
import com.utility.AlertDialogCallBack;
import com.utility.DialogUtility;
import com.utility.PreferenceUtility;
import com.utility.customView.CustomButton;
import com.utility.customView.CustomTextView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CreateTeamFragment extends BaseFragment {
    private RecyclerView rv_team;
    private ArrayList<TeamDto> teamDtoArrayList;
    private CustomButton btn_create;
    private CustomTextView tv_match, tv_status;
    private String team1, teamA, team2, teamB, matchStatus;
    private UpComingMatchesDto upComingMatchesDto;
    private ContestDto contestDto = null;
    private boolean isJoiningContest = false;
    private ApiInterface apiInterface;
    private UserDto userDto;

    public static CreateTeamFragment newInstance() {
        return new CreateTeamFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.create_team, container, false);
        readFromBundle();
        setInnerHeader("My Team List");
        initView();
        return view;
    }

    private void readFromBundle() {
        userDto = (UserDto) PreferenceUtility.getObjectInAppPreference(getActivity(), PreferenceUtility.APP_PREFERENCE_NAME);

        teamDtoArrayList = (ArrayList<TeamDto>) getArguments().getSerializable("teamDto");
        team1 = getArguments().getString("team1");
        team2 = getArguments().getString("team2");
        upComingMatchesDto = (UpComingMatchesDto) getArguments().getSerializable("upComingMatchesDto");
        teamA = upComingMatchesDto.teama;
        teamB = upComingMatchesDto.teamb;
        matchStatus = upComingMatchesDto.matchstatus;

        try {
            contestDto = (ContestDto) getArguments().getSerializable("contestDto");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (contestDto != null) {
            isJoiningContest = true;
        }
    }

    private void initView() {
        rv_team = view.findViewById(R.id.rv_team);
        btn_create = view.findViewById(R.id.btn_create);
        tv_match = view.findViewById(R.id.tv_match);
        tv_status = view.findViewById(R.id.tv_status);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_team.setLayoutManager(layoutManager);
        CreateTeamAdapter createTeamAdapter = new CreateTeamAdapter(getActivity(), teamDtoArrayList, isJoiningContest);
        rv_team.setAdapter(createTeamAdapter);
        createTeamAdapter.setOnButtonListener(new CreateTeamAdapter.OnButtonListener() {

            @Override
            public void onEditClick(int position) {
                SelectPlayersFragment selectPlayersFragment = new SelectPlayersFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("upComingMatchesDto", upComingMatchesDto);
                if (contestDto != null) {
                    bundle.putSerializable("contestDto", contestDto);
                }
                TeamDto teamDto = teamDtoArrayList.get(position);
                bundle.putSerializable("teamDto", teamDto);
                selectPlayersFragment.setArguments(bundle);
                ((BaseHeaderActivity) getActivity()).addFragment(selectPlayersFragment, true, SelectPlayersFragment.class.getName());
            }

            @Override
            public void onPreviewClick(int position) {
                TeamDto teamDto = teamDtoArrayList.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("team1", team1);
                bundle.putString("team2", team2);
                bundle.putString("teamA", teamA);
                bundle.putString("teamB", teamB);
                bundle.putSerializable("teamDto", teamDto);
                PreviewTeamFragment previewTeamFragment = PreviewTeamFragment.newInstance();
                previewTeamFragment.setArguments(bundle);
                ((BaseHeaderActivity) getActivity()).addFragment(previewTeamFragment, true, PreviewTeamFragment.class.getName());
            }

            @Override
            public void onJoinClick(int position) {
                TeamDto teamDto = teamDtoArrayList.get(position);
                callAPIJoinContest(Integer.parseInt(teamDto.team_id));
            }
        });
        btn_create.setText("Create Team " + (teamDtoArrayList.size() + 1));
        tv_match.setText(team1 + " vs " + team2);
        tv_status.setText(matchStatus);
    }

    @Override
    public void onButtonClick(View view) {
        super.onButtonClick(view);
        switch (view.getId()) {
            case R.id.img_back:
                getActivity().onBackPressed();
                break;
            case R.id.btn_create:
                SelectPlayersFragment selectPlayersFragment = new SelectPlayersFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("upComingMatchesDto", upComingMatchesDto);
                if (contestDto != null) {
                    bundle.putSerializable("contestDto", contestDto);
                }
                selectPlayersFragment.setArguments(bundle);
                ((BaseHeaderActivity) getActivity()).addFragment(selectPlayersFragment, true, SelectPlayersFragment.class.getName());
                break;
        }
    }

    private void callAPIJoinContest(int team_id) {
        //API
        Log.d("API", "Join Contest");
        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<JoinContestCallBackDto> call = apiInterface.joinContest(contestDto.id, userDto.reference_id, userDto.member_id, upComingMatchesDto.key_name);
        call.enqueue(new Callback<JoinContestCallBackDto>() {
            @Override
            public void onResponse(Call<JoinContestCallBackDto> call, Response<JoinContestCallBackDto> response) {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();

                JoinContestCallBackDto callBackDto = response.body();
                Log.e("UpcomingMatchesAPI", callBackDto.toString());
               DialogUtility.showMessageOkWithCallback(callBackDto.message, getActivity(), new AlertDialogCallBack() {
                   @Override
                   public void onSubmit() {
                       ((BaseHeaderActivity) getActivity()).removeAllFragment();
                       ((BaseHeaderActivity) getActivity()).replaceFragment(HomeFragment.newInstance(), false, HomeFragment.class.getName());
                   }

                   @Override
                   public void onCancel() {

                   }
               });
            }

            @Override
            public void onFailure(Call<JoinContestCallBackDto> call, Throwable t) {
                Log.e("TAG", t.toString());
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }
}
