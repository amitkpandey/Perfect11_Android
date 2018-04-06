package com.perfect11.contest.ChildFragment;

import android.annotation.SuppressLint;
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
import com.perfect11.contest.wrapper.TeamWrapper;
import com.perfect11.team_create.CreateTeamFragment;
import com.perfect11.team_create.SelectPlayersFragment;
import com.perfect11.team_create.adapter.ContestAdapter;
import com.perfect11.team_create.dto.ContestDto;
import com.perfect11.team_create.dto.ContestSubDto;
import com.perfect11.upcoming_matches.dto.UpComingMatchesDto;
import com.utility.customView.CustomTextView;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class AllContestFragment extends BaseFragment {
    private RecyclerView rv_list;
    private ArrayList<ContestDto> contestDtoArrayList;
    private Activity activity;

    private ContestAdapter contestAdapter;
private UpComingMatchesDto upComingMatchesDto;
    private TeamWrapper teamWrapper;
    private CustomTextView not_found;

    @SuppressLint("ValidFragment")
    public AllContestFragment(Activity activity, ArrayList<ContestDto> contestDtoArrayList, UpComingMatchesDto upComingMatchesDto, TeamWrapper teamWrapper) {
        this.contestDtoArrayList = contestDtoArrayList;
        this.activity = activity;
       this.upComingMatchesDto=upComingMatchesDto;
       this.teamWrapper=teamWrapper;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_contest_child, container, false);
        initView();
        return view;
    }

    private void initView() {
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
                        ((BaseHeaderActivity) activity).addFragment(createTeamFragment, true, CreateTeamFragment.class.getName());
                    } else {
                        bundle.putSerializable("contestDto", contestDto);
                        bundle.putSerializable("upComingMatchesDto", upComingMatchesDto);
                        SelectPlayersFragment selectPlayersFragment = new SelectPlayersFragment();
                        selectPlayersFragment.setArguments(bundle);
                        ((BaseHeaderActivity) activity).addFragment(selectPlayersFragment, true, SelectPlayersFragment.class.getName());

                    }
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
