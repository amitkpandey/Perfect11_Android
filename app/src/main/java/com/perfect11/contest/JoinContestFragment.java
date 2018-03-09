package com.perfect11.contest;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.perfect11.R;
import com.perfect11.base.ApiClient;
import com.perfect11.base.ApiInterface;
import com.perfect11.base.BaseFragment;
import com.perfect11.base.BaseHeaderActivity;
import com.perfect11.contest.adapter.ContestListAdapter;
import com.perfect11.contest.adapter.JoinContestAdapter;
import com.perfect11.contest.dto.JoinedContestDto;
import com.perfect11.contest.wrapper.JoinedContestWrapper;
import com.perfect11.login_signup.RegisterActivity;
import com.perfect11.login_signup.dto.UserDto;
import com.perfect11.team_create.ChooseContestActivity;
import com.perfect11.team_create.dto.ContestDto;
import com.perfect11.team_create.dto.PlayerDto;
import com.perfect11.team_create.dto.SelectedMatchDto;
import com.perfect11.team_create.wrapper.ContestWrapper;
import com.perfect11.upcoming_matches.dto.UpComingMatchesDto;
import com.utility.ActivityController;
import com.utility.DialogUtility;
import com.utility.PreferenceUtility;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JoinContestFragment extends BaseFragment {
    private RecyclerView rv_contests;
    private ApiInterface apiInterface;
    private JoinedContestDto joinedContestDto;
    private JoinContestAdapter joinContestAdapter;
    private UserDto userDto;

    public static JoinContestFragment newInstance() {
        return new JoinContestFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.join_contest_layout, container, false);
        setInnerHeader("Joined Contests");
        readFromBundle();
        initView();
        callAPI();
        return view;
    }

    private void readFromBundle() {
        userDto = (UserDto) PreferenceUtility.getObjectInAppPreference(getActivity(), PreferenceUtility.APP_PREFERENCE_NAME);
//        joinedContestDto = (JoinedContestDto) getArguments().getSerializable("joinedContestDto");
    }

    private void initView() {
        rv_contests = view.findViewById(R.id.rv_contests);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_contests.setLayoutManager(layoutManager);
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

    private void callAPI() {
        //API
        /**
         * Collecting data*/
        Log.d("API", "Get Player");
        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<JoinedContestWrapper> call = apiInterface.getJoinedContestList("nzeng_2018_one-day_03", "1");
        call.enqueue(new Callback<JoinedContestWrapper>() {
            @Override
            public void onResponse(Call<JoinedContestWrapper> call, Response<JoinedContestWrapper> response) {
                JoinedContestWrapper joinedContestWrapper = response.body();

                Log.e("JoinedContestWrapperAPI", joinedContestWrapper.toString());
                if (joinedContestWrapper.status) {
                    System.out.println(joinedContestWrapper.data.size());
                    setAdapter(joinedContestWrapper.data);
                } else {
                    DialogUtility.showMessageWithOk(joinedContestWrapper.message, getActivity());
                }
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<JoinedContestWrapper> call, Throwable t) {
                Log.e("TAG", t.toString());
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });

    }

    private void setAdapter(final ArrayList<JoinedContestDto> data) {
        joinContestAdapter = new JoinContestAdapter(getActivity(), data);
        rv_contests.setAdapter(joinContestAdapter);
        joinContestAdapter.setOnButtonListener(new JoinContestAdapter.OnButtonListener() {
            @Override
            public void onButtonClick(int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("joinedContestDto", data.get(position));
                LiveLeaderBoardFragment liveLeaderBoardFragment = LiveLeaderBoardFragment.newInstance();
                liveLeaderBoardFragment.setArguments(bundle);
                ((BaseHeaderActivity) getActivity()).addFragment(liveLeaderBoardFragment, true, LiveLeaderBoardFragment.class.getName());
            }
        });

    }
}
