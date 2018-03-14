package com.perfect11.team_create;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.perfect11.R;
import com.perfect11.base.BaseFragment;
import com.perfect11.base.BaseHeaderActivity;
import com.perfect11.contest.dto.TeamDto;
import com.perfect11.contest.dto.TeamPlayerDto;
import com.perfect11.team_create.adapter.CreateTeamAdapter;
import com.utility.customView.CustomButton;
import com.utility.customView.CustomTextView;

import java.util.ArrayList;


public class CreateTeamFragment extends BaseFragment {
    private RecyclerView rv_team;
    private ArrayList<TeamDto> teamDtoArrayList;
    private CustomButton btn_create;
    private CustomTextView tv_match, tv_status;
    private String team1, teamA, team2, teamB, matchStatus;

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
        teamDtoArrayList = (ArrayList<TeamDto>) getArguments().getSerializable("teamDto");
        team1 = getArguments().getString("team1");
        teamA = getArguments().getString("teamA");
        team2 = getArguments().getString("team2");
        teamB = getArguments().getString("teamB");
        matchStatus = getArguments().getString("matchStatus");
    }

    private void initView() {
        rv_team = view.findViewById(R.id.rv_team);
        btn_create = view.findViewById(R.id.btn_create);
        tv_match = view.findViewById(R.id.tv_match);
        tv_status = view.findViewById(R.id.tv_status);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_team.setLayoutManager(layoutManager);
        CreateTeamAdapter createTeamAdapter = new CreateTeamAdapter(getActivity(), teamDtoArrayList);
        rv_team.setAdapter(createTeamAdapter);
        createTeamAdapter.setOnButtonListener(new CreateTeamAdapter.OnButtonListener() {

            @Override
            public void onEditClick(int position) {

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
                ((BaseHeaderActivity) getActivity()).addFragment(MyTeamFragment.newInstance(), true, MyTeamFragment.class.getName());
                break;
        }
    }

}
