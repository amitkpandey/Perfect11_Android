package com.perfect11.home.childFragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.perfect11.contest.ContestFragment;
import com.perfect11.team_create.CreateTeamActivity;
import com.perfect11.upcoming_matches.UpcomingMatchesActivity;
import com.perfect11.upcoming_matches.adapter.UpcomingMatchesAdapter;
import com.perfect11.upcoming_matches.dto.UpComingMatchesDto;
import com.perfect11.upcoming_matches.wrapper.UpComingMatchesWrapper;
import com.utility.ActivityController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Developer on 30-06-2017.
 */

public class FixturesFragment extends BaseFragment {
    private RecyclerView rv_list;
    private ApiInterface apiInterface;
    private UpComingMatchesWrapper upComingMatchesWrapper;
    private UpcomingMatchesAdapter upcomingMatchesAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.viewpager_fixtures, container, false);
        initView();
        callAPI();
        return view;
    }

    private void initView() {
        rv_list = view.findViewById(R.id.rv_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_list.setLayoutManager(layoutManager);
 /*        UpcomingMatchesAdapter upcomingMatchesAdapter = new UpcomingMatchesAdapter();
       rv_list.setAdapter(upcomingMatchesAdapter);
        upcomingMatchesAdapter.setOnButtonListener(new UpcomingMatchesAdapter.OnButtonListener() {
            @Override
            public void onButtonClick(int position) {
                ((BaseHeaderActivity) getActivity()).addFragment(ContestFragment.newInstance(), true, ContestFragment.class.getName());
//                ((BaseHeaderActivity) getActivity()).addFragment(CreateContestFragment.newInstance(), true, CreateContestFragment.class.getName());
            }
        });*/
    }

    private void callAPI() {
        //API
        Log.d("API", "UpcomingMatches");
        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<UpComingMatchesWrapper> call = apiInterface.getUpcommingMatches();
        call.enqueue(new Callback<UpComingMatchesWrapper>() {
            @Override
            public void onResponse(Call<UpComingMatchesWrapper> call, Response<UpComingMatchesWrapper> response) {
                upComingMatchesWrapper = response.body();
//                Log.e("UpcomingMatchesAPI", upCommingMatchesWrapper.toString());
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                for (UpComingMatchesDto upComingMatchesDto : upComingMatchesWrapper.data) {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                        Date date;
                        date = sdf.parse(upComingMatchesDto.start_date);
                        long millis = date.getTime();
                        upComingMatchesDto.timeRemaining = millis - System.currentTimeMillis();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                upcomingMatchesAdapter = new UpcomingMatchesAdapter(upComingMatchesWrapper.data, getActivity());
                rv_list.setAdapter(upcomingMatchesAdapter);

                upcomingMatchesAdapter.setOnButtonListener(new UpcomingMatchesAdapter.OnButtonListener() {
                    @Override
                    public void onButtonClick(int position) {
                        Bundle bundle = new Bundle();
                        UpComingMatchesDto upComingMatchesDto = upComingMatchesWrapper.data.get(position);
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                            Date date;
                            date = sdf.parse(upComingMatchesDto.start_date);
                            long millis = date.getTime();
                            long hoursMillis = 60 * 60 * 1000;
                            long timeDiff = (millis - hoursMillis) - System.currentTimeMillis();
                            if (timeDiff > 0) {
                                bundle.putSerializable("upComingMatchesDto", upComingMatchesDto);
                                ContestFragment contestFragment = ContestFragment.newInstance();
                                contestFragment.setArguments(bundle);
                                ((BaseHeaderActivity) getActivity()).addFragment(contestFragment, true, ContestFragment.class.getName());
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                    }
                });
            }

            @Override
            public void onFailure(Call<UpComingMatchesWrapper> call, Throwable t) {
                Log.e("TAG", t.toString());
                //DialogUtility.showMessageWithOk(t.toString(),UpcomingMatchesActivity.this);
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }
}
