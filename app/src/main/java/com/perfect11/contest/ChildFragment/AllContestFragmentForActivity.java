package com.perfect11.contest.ChildFragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.perfect11.R;
import com.perfect11.base.BaseFragment;
import com.perfect11.base.BaseHeaderActivity;
import com.perfect11.contest.wrapper.TeamWrapper;
import com.perfect11.login_signup.RegisterActivity;
import com.perfect11.team_create.ChooseContestActivity;
import com.perfect11.team_create.CreateTeamFragment;
import com.perfect11.team_create.SelectPlayersFragment;
import com.perfect11.team_create.adapter.ContestAdapter;
import com.perfect11.team_create.dto.ContestDto;
import com.perfect11.team_create.dto.ContestSubDto;
import com.perfect11.team_create.dto.PlayerDto;
import com.perfect11.upcoming_matches.dto.UpComingMatchesDto;
import com.utility.ActivityController;
import com.utility.customView.CustomTextView;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class AllContestFragmentForActivity extends Fragment {
    private RecyclerView rv_list;
    private ArrayList<ContestDto> contestDtoArrayList;
    private Activity activity;
    private ArrayList<PlayerDto> selectedTeam;
    private ContestAdapter contestAdapter;
private UpComingMatchesDto upComingMatchesDto;

    private CustomTextView not_found;

    @SuppressLint("ValidFragment")
    public AllContestFragmentForActivity(Activity activity, ArrayList<ContestDto> contestDtoArrayList,UpComingMatchesDto upComingMatchesDto, ArrayList<PlayerDto> selectedTeam) {
        this.contestDtoArrayList = contestDtoArrayList;
        this.activity = activity;
       this.upComingMatchesDto=upComingMatchesDto;
       this.selectedTeam=selectedTeam;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_contest_child, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        not_found=view.findViewById(R.id.not_found);
        rv_list = view.findViewById(R.id.rv_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_list.setLayoutManager(layoutManager);

        if (contestDtoArrayList.size() != 0) {
            not_found.setVisibility(View.GONE);
            contestAdapter = new ContestAdapter(activity, contestDtoArrayList);
            rv_list.setAdapter(contestAdapter);

            contestAdapter.setOnItemClickListener(new ContestAdapter.OnItemClickListener() {
                @Override
                public void onJoinClick(ContestDto contestDto) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("contestDto", contestDto);
                    bundle.putSerializable("upComingMatchesDto", upComingMatchesDto);
                    bundle.putSerializable("selectedTeam", selectedTeam);
                    bundle.putBoolean("flag", true);
                    ActivityController.startNextActivity(activity, RegisterActivity.class, bundle, false);
                }

                @Override
                public void onItemClick(ArrayList<ContestSubDto> sub_data) {



                }
            });
        }else {
            not_found.setVisibility(View.VISIBLE);
        }


    }
}