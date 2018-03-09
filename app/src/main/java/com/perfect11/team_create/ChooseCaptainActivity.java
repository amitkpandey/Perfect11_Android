package com.perfect11.team_create;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.perfect11.R;
import com.perfect11.team_create.adapter.CaptainAdapter;
import com.perfect11.team_create.dto.PlayerDto;
import com.perfect11.team_create.dto.SelectedMatchDto;
import com.perfect11.upcoming_matches.dto.UpComingMatchesDto;
import com.utility.ActivityController;
import com.utility.customView.CustomButton;
import com.utility.customView.CustomTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class ChooseCaptainActivity extends Activity {
    private RecyclerView rv_team;
    private ArrayList<PlayerDto> selectedPlayer = null;
    private CaptainAdapter captainAdapter;
    private SelectedMatchDto selectedMatchDto;
    private UpComingMatchesDto upCommingMatchesDto;
    private CustomTextView tv_player_count, tv_header, ctv_time;
    private CustomButton btn_save;
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
        setContentView(R.layout.choose_captain);
        readFromBundle();
        initView();
        startUpdateTimer();
    }

    private void readFromBundle() {
        selectedPlayer = (ArrayList<PlayerDto>) getIntent().getExtras().getSerializable("selectedPlayer");
        selectedMatchDto = (SelectedMatchDto) getIntent().getExtras().getSerializable("selectedMatchDto");
        upCommingMatchesDto = (UpComingMatchesDto) getIntent().getExtras().getSerializable("upCommingMatchesDto");
//        System.out.println("upCommingMatchesDto:" + upCommingMatchesDto.toString());
    }

    private void initView() {
        rv_team = findViewById(R.id.rv_team);
        ctv_time = findViewById(R.id.ctv_time);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_team.setLayoutManager(layoutManager);

        captainAdapter = new CaptainAdapter(this, selectedPlayer, selectedMatchDto.teamName1, selectedMatchDto.teamName2);
        rv_team.setAdapter(captainAdapter);
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

    public void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.btn_save:
                ArrayList<PlayerDto> selectedTeam = captainAdapter.getSelectedCaptainAndVCaptainWithTeam();

                if (selectedTeam != null) {
                    for (PlayerDto playerDto : selectedTeam) {
                        System.out.println(playerDto.full_name + "  " + playerDto.isC + "  " + playerDto.isCV);
                    }
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("selectedTeam", selectedTeam);
                    bundle.putSerializable("selectedMatchDto", selectedMatchDto);
                    bundle.putSerializable("upCommingMatchesDto", upCommingMatchesDto);

                    ActivityController.startNextActivity(this, TeamReadyActivity.class, bundle, false);
                }
                break;
        }
    }
}
