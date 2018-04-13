package com.perfect11.team_create;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.perfect11.R;
import com.perfect11.base.ApiClient;
import com.perfect11.base.ApiInterface;
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
    private UpComingMatchesDto upComingMatchesDto;
    private CustomTextView tv_player_count, tv_header, ctv_country1, ctv_country2, ctv_time;
    private CustomButton btn_save;
    private String team_Name = "";
    private Handler mHandler = new Handler();
    private String blockCharacterSet = "~#^|$%&*!";
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
        //setTeamName();
    }

    private void setTeamName() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_set_teamname);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        final EditText team_name = dialog.findViewById(R.id.team_name);

        Button btn_set = dialog.findViewById(R.id.btn_set);
        ImageView iv_cross = dialog.findViewById(R.id.iv_cross);
        iv_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (team_name.getText().toString().trim().equals("")) {
                    Toast.makeText(ChooseCaptainActivity.this, "Enter Your Team Name", Toast.LENGTH_SHORT).show();
                }else if (!team_name.getText().toString().trim().matches("[a-zA-Z0-9 ]*")) {
                    Toast.makeText(ChooseCaptainActivity.this, "Special Character are not allow", Toast.LENGTH_SHORT).show();
                }else {
                    team_Name = team_name.getText().toString().trim();
                    System.out.println(" team_Name:"+team_Name);
                    btn_save.setText("Save Team");
                    dialog.dismiss();
                }
            }
        });
    }

    private void readFromBundle() {
        selectedPlayer = (ArrayList<PlayerDto>) getIntent().getExtras().getSerializable("selectedPlayer");
        selectedMatchDto = (SelectedMatchDto) getIntent().getExtras().getSerializable("selectedMatchDto");
        upComingMatchesDto = (UpComingMatchesDto) getIntent().getExtras().getSerializable("upComingMatchesDto");
//        System.out.println("upComingMatchesDto:" + upComingMatchesDto.toString());
    }

    private void initView() {
        btn_save = findViewById(R.id.btn_save);
        btn_save.setText("Set Team Name");

        rv_team = findViewById(R.id.rv_team);
        ctv_country1 = findViewById(R.id.ctv_country1);
        ctv_country2 = findViewById(R.id.ctv_country2);
        ctv_time = findViewById(R.id.ctv_time);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_team.setLayoutManager(layoutManager);

        captainAdapter = new CaptainAdapter(this, selectedPlayer, selectedMatchDto.teamName1, selectedMatchDto.teamName2);
        rv_team.setAdapter(captainAdapter);
        String[] team = upComingMatchesDto.short_name.split(" ");
        String team1 = team[0];
        String team2 = team[2];
        ctv_country1.setText(team1);
        ctv_country2.setText(team2);
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
            date = sdf.parse(upComingMatchesDto.start_date);
            long millis = date.getTime();
            long hoursMillis = 60 * 60 * 1000;
            long timeDiff = (millis - hoursMillis) - currentTime;
            if (timeDiff > 0) {
                int seconds = (int) (timeDiff / 1000) % 60;
                int minutes = (int) ((timeDiff / (1000 * 60)) % 60);
                int hours = (int) ((timeDiff / (1000 * 60 * 60)) % 24);
                int diffDays = (int) timeDiff / (24 * 60 * 60 * 1000);
                ctv_time.setText((diffDays == 0 ? "" : diffDays + " days ") + hours + " hrs " + minutes + " mins " + seconds + " sec");
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

                if (team_Name.trim().equals("")) {
                    setTeamName();
                } else {
                    if (selectedTeam != null) {
                        upComingMatchesDto.my_team_name = team_Name;
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("selectedTeam", selectedTeam);
                        bundle.putSerializable("selectedMatchDto", selectedMatchDto);
                        bundle.putSerializable("upComingMatchesDto", upComingMatchesDto);

                        ActivityController.startNextActivity(this, TeamReadyActivity.class, bundle, false);
                    }
                }
                break;
        }
    }
}
