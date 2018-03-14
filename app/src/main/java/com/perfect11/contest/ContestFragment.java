package com.perfect11.contest;

import android.app.ProgressDialog;
import android.os.Bundle;
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
import com.perfect11.contest.adapter.ContestListAdapter;
import com.perfect11.team_create.SelectPlayersFragment;
import com.perfect11.team_create.dto.ContestDto;
import com.perfect11.team_create.wrapper.ContestWrapper;
import com.perfect11.upcoming_matches.dto.UpComingMatchesDto;
import com.utility.DialogUtility;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class ContestFragment extends BaseFragment {

    private StickyListHeadersListView lv_contests;
    private ApiInterface apiInterface;
    private UpComingMatchesDto upComingMatchesDto;
    private ContestListAdapter contestListAdapter;

    public static ContestFragment newInstance() {
        return new ContestFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.contest_layout, container, false);
        setInnerHeader("Contest");
        initView();
        readFromBundle();
        return view;
    }

    private void initView() {
        lv_contests = view.findViewById(R.id.lv_contests);
    }

    private void readFromBundle() {
        upComingMatchesDto = (UpComingMatchesDto) getArguments().getSerializable("upComingMatchesDto");
        System.out.println("upComingMatchesDto:" + upComingMatchesDto.toString());
        callAPI();
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

        Call<ContestWrapper> call = apiInterface.getContestList(upComingMatchesDto.key_name, "all", "all", "all");
        call.enqueue(new Callback<ContestWrapper>() {
            @Override
            public void onResponse(Call<ContestWrapper> call, Response<ContestWrapper> response) {
                ContestWrapper contestWrapper = response.body();

                Log.e("UpcomingMatchesAPI", contestWrapper.toString());
                if (contestWrapper.status) {
                    System.out.println(contestWrapper.data.size());
                    setAdapter(contestWrapper.data);
                } else {
                    DialogUtility.showMessageWithOk(contestWrapper.message, getActivity());
                }
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ContestWrapper> call, Throwable t) {
                Log.e("TAG", t.toString());
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });

    }

    private void setAdapter(ArrayList<ContestDto> data) {

        contestListAdapter = new ContestListAdapter(getActivity(), data);
        lv_contests.setAdapter(contestListAdapter);
        contestListAdapter.setOnItemClickListener(new ContestListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onjoinClick(ContestDto contestDto) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("contestDto", contestDto);
                bundle.putSerializable("upComingMatchesDto", upComingMatchesDto);

                SelectPlayersFragment selectPlayersFragment = new SelectPlayersFragment();
                selectPlayersFragment.setArguments(bundle);
                ((BaseHeaderActivity) getActivity()).addFragment(selectPlayersFragment, true, SelectPlayersFragment.class.getName());
                Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onButtonClick(View view) {
        super.onButtonClick(view);
        switch (view.getId()) {
            case R.id.img_back:
                getActivity().onBackPressed();
                break;
            case R.id.btn_create_contest:
                ((BaseHeaderActivity) getActivity()).addFragment(CreateContestFragment.newInstance(), true, CreateContestFragment.class.getName());
                break;
        }
    }
}
