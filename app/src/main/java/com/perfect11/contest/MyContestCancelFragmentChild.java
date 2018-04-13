package com.perfect11.contest;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.perfect11.R;
import com.perfect11.base.ApiClient;
import com.perfect11.base.ApiInterface;
import com.perfect11.base.BaseFragment;
import com.perfect11.base.BaseHeaderActivity;
import com.perfect11.contest.adapter.MyContestAdapter;
import com.perfect11.contest.dto.JoinedContestDto;
import com.perfect11.contest.wrapper.MyContestWrapper;
import com.perfect11.login_signup.dto.UserDto;
import com.utility.DialogUtility;
import com.utility.PreferenceUtility;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyContestCancelFragmentChild extends BaseFragment {
    private RecyclerView rv_contests;
    private ArrayList<JoinedContestDto> joinedContestDto;

    private UserDto userDto;
    private ApiInterface apiInterface;
    private MyContestAdapter myContestAdapter;

    public static MyContestCancelFragmentChild newInstance() {
        return new MyContestCancelFragmentChild();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_child_my_contest, container, false);
        //setHeader("My Contests");
        initView();
        readFromBundle();
        callAPI();
        return view;
    }

    private void callAPI() {
        //API
        /*
         * Collecting data*/
        Log.d("API", "Get Player");
        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<MyContestWrapper> call = apiInterface.getMyCanceledContests(userDto.member_id);
        call.enqueue(new Callback<MyContestWrapper>() {
            @Override
            public void onResponse(Call<MyContestWrapper> call, Response<MyContestWrapper> response) {
                MyContestWrapper myContestWrapper = response.body();

                if (myContestWrapper.status) {
                    System.out.println(myContestWrapper.data.size());
                    setAdapter(myContestWrapper.data);
                } else {
                    DialogUtility.showMessageWithOk(myContestWrapper.message, getActivity());
                }
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<MyContestWrapper> call, Throwable t) {
                Log.e("TAG", t.toString());
                if (t instanceof IOException) {
                    DialogUtility.showConnectionErrorDialogWithOk(getActivity());
                    // logging probably not necessary
                }
                else {
                    Toast.makeText(getActivity(), "Conversion issue! big problems :(", Toast.LENGTH_SHORT).show();
                    // todo log to some central bug tracking service
                }
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });

    }

    private void setAdapter(final ArrayList<JoinedContestDto> data) {
        myContestAdapter = new MyContestAdapter(getActivity(), data);
        rv_contests.setAdapter(myContestAdapter);
        myContestAdapter.setOnButtonListener(new MyContestAdapter.OnButtonListener() {
            @Override
            public void onButtonClick(int position) {
               /* Bundle bundle = new Bundle();
                bundle.putSerializable("joinedContestDto", data.get(position));
                try {
                    String str[] = data.get(position).room_name.split(" ");
                    bundle.putString("team1", str[0]);
                    bundle.putString("team2", str[2]);
                    bundle.putString("teamA", str[0]);
                    bundle.putString("teamB", str[2]);
                } catch (Exception e) {
                    bundle.putString("team1", "");
                    bundle.putString("team2", "");
                    bundle.putString("teamA", "");
                    bundle.putString("teamB", "");
                    e.printStackTrace();
                }
                bundle.putString("matchStatus", data.get(position).matchid);
                ResultLeaderBoardFragment resultLeaderBoardFragment = ResultLeaderBoardFragment.newInstance();
                resultLeaderBoardFragment.setArguments(bundle);
                ((BaseHeaderActivity) getActivity()).addFragment(resultLeaderBoardFragment, true, ResultLeaderBoardFragment.class.getName());
*/
            }
        });

    }

    private void initView() {
        rv_contests = view.findViewById(R.id.rv_contests);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_contests.setLayoutManager(layoutManager);
    }

    private void readFromBundle() {
        userDto = (UserDto) PreferenceUtility.getObjectInAppPreference(getActivity(), PreferenceUtility.APP_PREFERENCE_NAME);
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
