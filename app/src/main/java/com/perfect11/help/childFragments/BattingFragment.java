package com.perfect11.help.childFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.perfect11.R;
import com.perfect11.base.BaseFragment;
import com.perfect11.help.adapter.HelpAdapter;
import com.perfect11.upcoming_matches.adapter.UpcomingMatchesAdapter;


/**
 * Created by Developer on 30-06-2017.
 */

public class BattingFragment extends BaseFragment {
    private RecyclerView rv_list;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view=inflater.inflate(R.layout.viewpager_help,container,false);
        initView();
         return view;
    }

    private void initView() {
        rv_list = (RecyclerView) view.findViewById(R.id.rv_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_list.setLayoutManager(layoutManager);
        rv_list.setAdapter(new HelpAdapter());
    }
}
