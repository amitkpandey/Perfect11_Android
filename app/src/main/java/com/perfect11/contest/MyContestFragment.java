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
import com.perfect11.contest.adapter.JoinContestAdapter;
import com.perfect11.contest.dto.JoinedContestDto;

import java.util.ArrayList;

public class MyContestFragment extends BaseFragment {
    private RecyclerView rv_contests;
    private ArrayList<JoinedContestDto> joinedContestDto;

    public static MyContestFragment newInstance() {
        return new MyContestFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.join_contest_layout, container, false);
        setInnerHeader("My Contests");
        initView();
        return view;
    }

    private void initView() {
        rv_contests = view.findViewById(R.id.rv_contests);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_contests.setLayoutManager(layoutManager);
        JoinContestAdapter joinContestAdapter = new JoinContestAdapter(getActivity(), joinedContestDto);
        rv_contests.setAdapter(joinContestAdapter);
        joinContestAdapter.setOnButtonListener(new JoinContestAdapter.OnButtonListener() {
            @Override
            public void onButtonClick(int position) {
                ((BaseHeaderActivity) getActivity()).addFragment(ResultLeaderBoardFragment.newInstance(), true, ResultLeaderBoardFragment.class.getName());
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
        }
    }
}
