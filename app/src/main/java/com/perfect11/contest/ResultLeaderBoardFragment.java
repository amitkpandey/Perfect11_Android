package com.perfect11.contest;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.perfect11.R;
import com.perfect11.base.BaseFragment;
import com.perfect11.base.BaseHeaderActivity;
import com.perfect11.contest.adapter.PracticeContestAdapter;
import com.perfect11.contest.dto.LiveLeaderboardDto;
import com.perfect11.team_create.MyTeamFragment;

import java.util.ArrayList;


public class ResultLeaderBoardFragment extends BaseFragment {
    private RecyclerView rv_contests;
    private ArrayList<LiveLeaderboardDto> liveLeaderboardDto;

    public static ResultLeaderBoardFragment newInstance() {
        return new ResultLeaderBoardFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.my_contest_layout, container, false);
        setInnerHeader("Practice Contests");
        initView();
        return view;
    }

    private void initView() {
        rv_contests = view.findViewById(R.id.rv_contests);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_contests.setLayoutManager(layoutManager);
        PracticeContestAdapter practiceContestAdapter = new PracticeContestAdapter(getActivity(), liveLeaderboardDto);
        rv_contests.setAdapter(practiceContestAdapter);
        practiceContestAdapter.setOnButtonListener(new PracticeContestAdapter.OnButtonListener() {
            @Override
            public void onButtonClick(int position) {
//                ((BaseHeaderActivity) getActivity()).addFragment(ContestFragment.newInstance(), true, ContestFragment.class.getName());
            }
        });
    }

    @Override
    public void onButtonClick(View view) {
        super.onButtonClick(view);
        switch (view.getId()) {
            case R.id.img_back:
                getActivity().onBackPressed();
                break;
            case R.id.btn_save:
                ((BaseHeaderActivity) getActivity()).addFragment(MyTeamFragment.newInstance(), true, MyTeamFragment.class.getName());
                break;
        }
    }
}
