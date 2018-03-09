package com.perfect11.home.childFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.perfect11.R;
import com.perfect11.base.BaseFragment;
import com.perfect11.base.BaseHeaderActivity;
import com.perfect11.contest.JoinContestFragment;
import com.perfect11.contest.MyContestFragment;
import com.perfect11.home.adapter.ResultMatchesAdapter;
import com.perfect11.upcoming_matches.adapter.UpcomingMatchesAdapter;


/**
 * Created by Developer on 30-06-2017.
 */

public class ResultsFragment extends BaseFragment {
    private RecyclerView rv_list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.viewpager_fixtures, container, false);
        initView();
        return view;
    }

    private void initView() {
        rv_list = view.findViewById(R.id.rv_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_list.setLayoutManager(layoutManager);
        ResultMatchesAdapter resultMatchesAdapter = new ResultMatchesAdapter();
        rv_list.setAdapter(resultMatchesAdapter);
        resultMatchesAdapter.setOnButtonListener(new ResultMatchesAdapter.OnButtonListener() {
            @Override
            public void onButtonClick(int position) {
                ((BaseHeaderActivity) getActivity()).addFragment(MyContestFragment.newInstance(), true, MyContestFragment.class.getName());
            }
        });
    }
}
