package com.perfect11.contest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.perfect11.R;
import com.perfect11.base.BaseFragment;
import com.perfect11.base.BaseHeaderActivity;
import com.perfect11.team_create.CreateTeamFragment;


public class CreateContestFragment extends BaseFragment {

    public static CreateContestFragment newInstance() {
        return new CreateContestFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.create_contest, container, false);
        setInnerHeader("Create Contest");
        return view;
    }

    @Override
    public void onButtonClick(View view) {
        super.onButtonClick(view);
        switch (view.getId()) {
            case R.id.img_back:
                getActivity().onBackPressed();
                break;
            case R.id.btn_create:
                ((BaseHeaderActivity) getActivity()).addFragment(CreateTeamFragment.newInstance(), true, CreateTeamFragment.class.getName());
                break;
        }
    }
}
