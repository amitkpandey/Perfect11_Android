package com.perfect11.team_create;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.perfect11.R;
import com.perfect11.base.BaseFragment;
import com.perfect11.base.BaseHeaderActivity;
import com.perfect11.team_create.adapter.CaptainAdapter;
import com.perfect11.team_create.dto.PlayerDto;
import com.perfect11.team_create.dto.SelectedMatchDto;
import com.perfect11.upcoming_matches.dto.UpComingMatchesDto;
import com.utility.ActivityController;
import com.utility.customView.CustomButton;
import com.utility.customView.CustomTextView;

import java.util.ArrayList;

public class ChooseCaptainFragment extends BaseFragment {
    private RecyclerView rv_team;
    private ArrayList<PlayerDto> selectedPlayer =null;
    private CaptainAdapter captainAdapter;
    private SelectedMatchDto selectedMatchDto;
    private UpComingMatchesDto upCommingMatchesDto;
    private CustomTextView tv_player_count, tv_header;
    private CustomButton btn_save;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
        view=inflater.inflate(R.layout.choose_captain,container,false);
        readFromBundle();
        initView();
         return view;
    }

    private void readFromBundle() {
        selectedPlayer= (ArrayList<PlayerDto>) getArguments().getSerializable("selectedPlayer");
        selectedMatchDto= (SelectedMatchDto) getArguments().getSerializable("selectedMatchDto");
        upCommingMatchesDto = (UpComingMatchesDto) getArguments().getSerializable("upCommingMatchesDto");
        System.out.println("upCommingMatchesDto:"+upCommingMatchesDto.toString());
    }

    private void initView() {
        rv_team = view.findViewById(R.id.rv_team);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_team.setLayoutManager(layoutManager);

        captainAdapter = new CaptainAdapter(getActivity(),selectedPlayer,selectedMatchDto.teamName1,selectedMatchDto.teamName2);
        rv_team.setAdapter(captainAdapter);
    }

    public void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                getActivity().onBackPressed();
                break;
            case R.id.btn_save:
                ArrayList<PlayerDto> selectedTeam=captainAdapter.getSelectedCaptainAndVCaptainWithTeam();

                if(selectedTeam!=null) {
                    for (PlayerDto playerDto : selectedTeam) {
                        System.out.println(playerDto.full_name + "  " + playerDto.isC + "  " + playerDto.isCV);
                    }
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("selectedTeam",selectedTeam);
                    bundle.putSerializable("selectedMatchDto", selectedMatchDto);
                    bundle.putSerializable("upCommingMatchesDto", upCommingMatchesDto);
                    TeamReadyFragment teamReadyFragment=new TeamReadyFragment();
                    teamReadyFragment.setArguments(bundle);
                    ((BaseHeaderActivity) getActivity()).addFragment(teamReadyFragment, true, TeamReadyFragment.class.getName());
                }
                    break;
        }
    }
}
