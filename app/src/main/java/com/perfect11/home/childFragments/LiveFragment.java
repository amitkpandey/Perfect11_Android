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
import android.widget.Toast;

import com.perfect11.R;
import com.perfect11.base.ApiClient;
import com.perfect11.base.ApiInterface;
import com.perfect11.base.BaseFragment;
import com.perfect11.base.BaseHeaderActivity;
import com.perfect11.contest.ContestFragment;
import com.perfect11.contest.JoinContestFragment;
import com.perfect11.home.adapter.LiveMatchesAdapter;
import com.perfect11.upcoming_matches.dto.UpComingMatchesDto;
import com.perfect11.upcoming_matches.wrapper.UpComingMatchesWrapper;
import com.utility.DialogUtility;
import com.utility.customView.CustomTextView;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Developer on 30-06-2017.
 */

public class LiveFragment extends BaseFragment {
    private RecyclerView rv_list;
    private ApiInterface apiInterface;
    private UpComingMatchesWrapper liveMatchesWrapper;
    private LiveMatchesAdapter liveMatchesAdapter;
private CustomTextView ctv_display;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.viewpager_fixtures, container, false);
        initView();
      //  callAPI();
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && getView() != null)
            callAPI();
    }

    private void initView() {
        rv_list = view.findViewById(R.id.rv_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_list.setLayoutManager(layoutManager);
        ctv_display=view.findViewById(R.id.ctv_display);
        /*liveMatchesAdapter.setOnButtonListener(new LiveMatchesAdapter.OnButtonListener() {
            @Override
            public void onButtonClick(int position) {
                ((BaseHeaderActivity) getActivity()).addFragment(JoinContestFragment.newInstance(), true, JoinContestFragment.class.getName());
            }
        });*/
    }

    private void callAPI() {
        //API
        Log.d("API", "UpcomingMatches");
        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<UpComingMatchesWrapper> call = apiInterface.getLiveMatches();
        call.enqueue(new Callback<UpComingMatchesWrapper>() {
            @Override
            public void onResponse(Call<UpComingMatchesWrapper> call, Response<UpComingMatchesWrapper> response) {
                liveMatchesWrapper = response.body();
                Log.e("UpcomingMatchesAPI", liveMatchesWrapper.toString());
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();

                if(liveMatchesWrapper.data.size()==0)
                {
                    rv_list.setVisibility(View.GONE);
                    ctv_display.setVisibility(View.VISIBLE);
                }else {
                    rv_list.setVisibility(View.VISIBLE);
                    ctv_display.setVisibility(View.GONE);
                    liveMatchesAdapter = new LiveMatchesAdapter(liveMatchesWrapper.data, getActivity());
                    rv_list.setAdapter(liveMatchesAdapter);

                    liveMatchesAdapter.setOnButtonListener(new LiveMatchesAdapter.OnButtonListener() {
                        @Override
                        public void onButtonClick(int position) {
                            Bundle bundle = new Bundle();
                            UpComingMatchesDto upComingMatchesDto = liveMatchesWrapper.data.get(position);
                            bundle.putSerializable("upComingMatchesDto", upComingMatchesDto);
                            JoinContestFragment joinContestFragment = JoinContestFragment.newInstance();
                            joinContestFragment.setArguments(bundle);
                            ((BaseHeaderActivity) getActivity()).addFragment(joinContestFragment, true, JoinContestFragment.class.getName());
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<UpComingMatchesWrapper> call, Throwable t) {
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
}
