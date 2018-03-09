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
import com.perfect11.team_create.adapter.CreateTeamAdapter;


public class CreateTeamFragment extends BaseFragment {
    private RecyclerView rv_team;

    public static CreateTeamFragment newInstance() {
        return new CreateTeamFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.create_team, container, false);
        setInnerHeader("Create Contest");
        initView();
        return view;
    }

    private void initView() {
        rv_team = view.findViewById(R.id.rv_team);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_team.setLayoutManager(layoutManager);
        CreateTeamAdapter createTeamAdapter = new CreateTeamAdapter();
        rv_team.setAdapter(createTeamAdapter);
        createTeamAdapter.setOnButtonListener(new CreateTeamAdapter.OnButtonListener() {
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
