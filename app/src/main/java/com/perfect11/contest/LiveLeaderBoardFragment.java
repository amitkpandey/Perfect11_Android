package com.perfect11.contest;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.perfect11.R;
import com.perfect11.base.ApiClient2;
import com.perfect11.base.ApiInterface;
import com.perfect11.base.BaseFragment;
import com.perfect11.base.BaseHeaderActivity;
import com.perfect11.contest.adapter.PracticeContestAdapter;
import com.perfect11.contest.dto.JoinedContestDto;
import com.perfect11.contest.dto.LiveLeaderboardDto;
import com.perfect11.login_signup.dto.UserDto;
import com.perfect11.team_create.MyTeamFragment;
import com.utility.PreferenceUtility;
import com.utility.customView.CustomButton;
import com.utility.customView.CustomTextView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LiveLeaderBoardFragment extends BaseFragment {
    private RecyclerView rv_contests;
    private CustomTextView tv_total_win, tv_entry_fee;
    private CustomButton btn_save;

    private JoinedContestDto joinedContestDto;
    private UserDto userDto;

    private PracticeContestAdapter practiceContestAdapter;

    private ApiInterface apiInterface;

    public static LiveLeaderBoardFragment newInstance() {
        return new LiveLeaderBoardFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.practice_contest_layout, container, false);
        setInnerHeader("Practice Contests");
        readFromBundle();
        initView();
        setValues();
        callAPI();
        return view;
    }

    private void readFromBundle() {
        userDto = (UserDto) PreferenceUtility.getObjectInAppPreference(getActivity(), PreferenceUtility.APP_PREFERENCE_NAME);
        joinedContestDto = (JoinedContestDto) getArguments().getSerializable("joinedContestDto");
    }

    private void initView() {
        tv_total_win = view.findViewById(R.id.tv_total_win);
        tv_entry_fee = view.findViewById(R.id.tv_entry_fee);
        rv_contests = view.findViewById(R.id.rv_contests);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_contests.setLayoutManager(layoutManager);
        btn_save = view.findViewById(R.id.btn_save);
    }

    private void setValues() {
        tv_total_win.setText("Rs. " + joinedContestDto.winingamount + "/-");
        tv_entry_fee.setText("Rs. " + joinedContestDto.amount + "/-");
        btn_save.setText("Team Preview");
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

    private void callAPI() {
        //API
        /**
         * Collecting data*/
        Log.d("API", "Get Player");
        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();
        apiInterface = ApiClient2.getApiClient().create(ApiInterface.class);

        Call<ArrayList<LiveLeaderboardDto>> call = apiInterface.getLeaderboardList("nzeng_2018_one-day_03", "133");
        call.enqueue(new Callback<ArrayList<LiveLeaderboardDto>>() {
            @Override
            public void onResponse(Call<ArrayList<LiveLeaderboardDto>> call, Response<ArrayList<LiveLeaderboardDto>> response) {
                setAdapter(response.body());
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ArrayList<LiveLeaderboardDto>> call, Throwable t) {
                Log.e("TAG", t.toString());
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });

    }

    private void setAdapter(final ArrayList<LiveLeaderboardDto> data) {
        practiceContestAdapter = new PracticeContestAdapter(getActivity(), data);
        rv_contests.setAdapter(practiceContestAdapter);
        practiceContestAdapter.setOnButtonListener(new PracticeContestAdapter.OnButtonListener() {
            @Override
            public void onButtonClick(int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("joinedContestDto", data.get(position));
                MyTeamFragment myTeamFragment = MyTeamFragment.newInstance();
                myTeamFragment.setArguments(bundle);
                ((BaseHeaderActivity) getActivity()).addFragment(myTeamFragment, true, MyTeamFragment.class.getName());
            }
        });

    }
}
