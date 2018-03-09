package com.perfect11.team_create;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.perfect11.R;
import com.perfect11.base.ApiClient;
import com.perfect11.base.ApiInterface;
import com.perfect11.contest.adapter.ContestListAdapter;
import com.perfect11.login_signup.RegisterActivity;
import com.perfect11.team_create.dto.ContestDto;
import com.perfect11.team_create.dto.PlayerDto;
import com.perfect11.team_create.dto.SelectedMatchDto;
import com.perfect11.team_create.wrapper.ContestWrapper;
import com.perfect11.upcoming_matches.dto.UpComingMatchesDto;
import com.utility.ActivityController;
import com.utility.DialogUtility;
import com.utility.customView.CustomTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class ChooseContestActivity extends Activity implements AdapterView.OnItemClickListener, StickyListHeadersListView.OnHeaderClickListener,
        StickyListHeadersListView.OnStickyHeaderOffsetChangedListener, StickyListHeadersListView.OnStickyHeaderChangedListener {

    private StickyListHeadersListView lv_contests;
    private ArrayList<PlayerDto> selectedTeam;
    private SelectedMatchDto selectedMatchDto;
    private UpComingMatchesDto upCommingMatchesDto;

    private ApiInterface apiInterface;
    private ContestListAdapter contestListAdapter;
    private CustomTextView ctv_time;
    private Handler mHandler = new Handler();
    private Runnable updateRemainingTimeRunnable = new Runnable() {
        @Override
        public void run() {
            long currentTime = System.currentTimeMillis();
            updateTimeRemaining(currentTime);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_contest_layout);
        readFromBundle();
        initView();
        startUpdateTimer();
        callAPI();
    }

    private void readFromBundle() {
        selectedTeam = (ArrayList<PlayerDto>) getIntent().getExtras().getSerializable("selectedTeam");
        selectedMatchDto = (SelectedMatchDto) getIntent().getExtras().getSerializable("selectedMatchDto");
        upCommingMatchesDto = (UpComingMatchesDto) getIntent().getExtras().getSerializable("upCommingMatchesDto");
//        System.out.println("upCommingMatchesDto:" + upCommingMatchesDto.toString() + selectedTeam.size());
    }

    private void initView() {
        lv_contests = findViewById(R.id.lv_contests);
        ctv_time = findViewById(R.id.ctv_time);
    }

    private void startUpdateTimer() {
        Timer tmr = new Timer();
        tmr.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.post(updateRemainingTimeRunnable);
            }
        }, 1000, 1000);
    }

    public void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
        }
    }

    private void updateTimeRemaining(long currentTime) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            Date date;
            date = sdf.parse(upCommingMatchesDto.start_date);
            long millis = date.getTime();
            long timeDiff = millis - currentTime;
            if (timeDiff > 0) {
                int seconds = (int) (timeDiff / 1000) % 60;
                int minutes = (int) ((timeDiff / (1000 * 60)) % 60);
                int hours = (int) ((timeDiff / (1000 * 60 * 60)) % 24);
                ctv_time.setText(hours + " hrs " + minutes + " mins " + seconds + " sec");
            } else {
                ctv_time.setText("Expired!!");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void callAPI() {
        //API
        /**
         * Collecting data*/
        Log.d("API", "Get Player");
        final ProgressDialog mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<ContestWrapper> call = apiInterface.getContestList(upCommingMatchesDto.key_name, "all", "all", "all");
        call.enqueue(new Callback<ContestWrapper>() {
            @Override
            public void onResponse(Call<ContestWrapper> call, Response<ContestWrapper> response) {
                ContestWrapper contestWrapper = response.body();

                Log.e("UpcomingMatchesAPI", contestWrapper.toString());
                if (contestWrapper.status) {
                    System.out.println(contestWrapper.data.size());
                    setAdapter(contestWrapper.data);
                } else {
                    DialogUtility.showMessageWithOk(contestWrapper.message, ChooseContestActivity.this);
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
//        if (data != null && data.size() > 0) {
//            ContestListAdapter contestListAdapter = new ContestListAdapter(this, data);
//            lv_contests.setVisibility(View.VISIBLE);
//            lv_contests.setAdapter(contestListAdapter);
//            lv_contests.setOnItemClickListener(this);
//            lv_contests.setOnHeaderClickListener(this);
//            lv_contests.setOnStickyHeaderChangedListener(this);
//            lv_contests.setOnStickyHeaderOffsetChangedListener(this);
//            lv_contests.setDrawingListUnderStickyHeader(true);
//            lv_contests.setFastScrollEnabled(true);
//            lv_contests.setAreHeadersSticky(true);
//            lv_contests.setStickyHeaderTopOffset(-20);
//        } else {
//            lv_contests.setVisibility(View.GONE);
//        }
        contestListAdapter = new ContestListAdapter(this, data);
        lv_contests.setAdapter(contestListAdapter);
        contestListAdapter.setOnItemClickListener(new ContestListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onjoinClick(ContestDto contestDto) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("contestDto", contestDto);
                bundle.putSerializable("upCommingMatchesDto", upCommingMatchesDto);
                bundle.putSerializable("selectedTeam", selectedTeam);
                bundle.putBoolean("flag", true);
                ActivityController.startNextActivity(ChooseContestActivity.this,
                        RegisterActivity.class, bundle, false);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onHeaderClick(StickyListHeadersListView l, View header, int itemPosition, long headerId, boolean currentlySticky) {

    }

    @Override
    public void onStickyHeaderOffsetChanged(StickyListHeadersListView l, View header, int offset) {

    }

    @Override
    public void onStickyHeaderChanged(StickyListHeadersListView l, View header, int itemPosition, long headerId) {

    }
}
