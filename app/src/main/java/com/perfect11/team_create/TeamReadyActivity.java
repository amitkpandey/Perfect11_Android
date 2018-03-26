package com.perfect11.team_create;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.perfect11.R;
import com.perfect11.team_create.dto.PlayerDto;
import com.perfect11.team_create.dto.SelectedMatchDto;
import com.perfect11.upcoming_matches.dto.UpComingMatchesDto;
import com.squareup.picasso.Picasso;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class TeamReadyActivity extends Activity {
    private ArrayList<PlayerDto> selectedTeam;
    private CustomTextView tv_team1, tv_team2, tv_team_count1, tv_team_count2, ctv_time, ctv_country1, ctv_country2;
    private CircleImageView cimg_country1, cimg_country2;

    private ImageView iv_wkt;
    private ImageView iv_bat1, iv_bat2, iv_bat3, iv_bat4, iv_bat5, iv_bat6;
    private ImageView iv_ar1, iv_ar2, iv_ar3, iv_ar4;
    private ImageView iv_bowler1, iv_bowler2, iv_bowler3, iv_bowler4, iv_bowler5, iv_bowler6;

    private CustomTextView tv_wkt_name, tv_bat1_name, tv_bat2_name, tv_bat3_name, tv_bat4_name,
            tv_bat5_name, tv_bat6_name, tv_ar1_name, tv_ar2_name, tv_ar3_name, tv_ar4_name, tv_bowler1_name, tv_bowler2_name, tv_bowler3_name,
            tv_bowler4_name, tv_bowler5_name, tv_bowler6_name;

    private RelativeLayout rl_bat1, rl_bat2, rl_bat3, rl_bat4, rl_bat5, rl_bat6, rl_ar1, rl_ar2, rl_ar3, rl_ar4, rl_bowler1, rl_bowler2, rl_bowler3,
            rl_bowler4, rl_bowler5, rl_bowler6;

    private CustomButton btn_save;

    private int bowler = 0, batsman = 0, allrounder = 0, keeper = 0;

    private SelectedMatchDto selectedMatchDto;
    private UpComingMatchesDto upComingMatchesDto;
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
        setContentView(R.layout.ready_team);
        readFromBundle();
        initView();
        startUpdateTimer();
    }

    private void readFromBundle() {
        selectedTeam = (ArrayList<PlayerDto>) getIntent().getExtras().getSerializable("selectedTeam");
        selectedMatchDto = (SelectedMatchDto) getIntent().getExtras().getSerializable("selectedMatchDto");
        upComingMatchesDto = (UpComingMatchesDto) getIntent().getExtras().getSerializable("upComingMatchesDto");
    }

    private void initView() {
        ctv_country1 = findViewById(R.id.ctv_country1);
        ctv_country2 = findViewById(R.id.ctv_country2);
        tv_team2 = findViewById(R.id.tv_team2);
        tv_team1 = findViewById(R.id.tv_team1);
        tv_team2 = findViewById(R.id.tv_team2);
        tv_team_count1 = findViewById(R.id.tv_team_count1);
        tv_team_count2 = findViewById(R.id.tv_team_count2);
        ctv_time = findViewById(R.id.ctv_time);

        cimg_country1 = findViewById(R.id.cimg_country1);
        cimg_country2 = findViewById(R.id.cimg_country2);

        iv_wkt = findViewById(R.id.iv_wkt);

        iv_bat1 = findViewById(R.id.iv_bat1);
        iv_bat2 = findViewById(R.id.iv_bat2);
        iv_bat3 = findViewById(R.id.iv_bat3);
        iv_bat4 = findViewById(R.id.iv_bat4);
        iv_bat5 = findViewById(R.id.iv_bat5);
        iv_bat6 = findViewById(R.id.iv_bat6);

        iv_ar1 = findViewById(R.id.iv_ar1);
        iv_ar2 = findViewById(R.id.iv_ar2);
        iv_ar3 = findViewById(R.id.iv_ar3);
        iv_ar4 = findViewById(R.id.iv_ar4);

        iv_bowler1 = findViewById(R.id.iv_bowler1);
        iv_bowler2 = findViewById(R.id.iv_bowler2);
        iv_bowler3 = findViewById(R.id.iv_bowler3);
        iv_bowler4 = findViewById(R.id.iv_bowler4);
        iv_bowler5 = findViewById(R.id.iv_bowler5);
        iv_bowler6 = findViewById(R.id.iv_bowler6);

        btn_save = findViewById(R.id.btn_save);
        btn_save.setText("Join Contest");

        tv_wkt_name = findViewById(R.id.tv_wkt_name);

        tv_bat1_name = findViewById(R.id.tv_bat1_name);
        tv_bat2_name = findViewById(R.id.tv_bat2_name);
        tv_bat3_name = findViewById(R.id.tv_bat3_name);
        tv_bat4_name = findViewById(R.id.tv_bat4_name);
        tv_bat5_name = findViewById(R.id.tv_bat5_name);
        tv_bat6_name = findViewById(R.id.tv_bat6_name);

        tv_ar1_name = findViewById(R.id.tv_ar1_name);
        tv_ar2_name = findViewById(R.id.tv_ar2_name);
        tv_ar3_name = findViewById(R.id.tv_ar3_name);
        tv_ar4_name = findViewById(R.id.tv_ar4_name);

        tv_bowler1_name = findViewById(R.id.tv_bowler1_name);
        tv_bowler2_name = findViewById(R.id.tv_bowler2_name);
        tv_bowler3_name = findViewById(R.id.tv_bowler3_name);
        tv_bowler4_name = findViewById(R.id.tv_bowler4_name);
        tv_bowler5_name = findViewById(R.id.tv_bowler5_name);
        tv_bowler6_name = findViewById(R.id.tv_bowler6_name);

        rl_bat1 = findViewById(R.id.rl_bat1);
        rl_bat2 = findViewById(R.id.rl_bat2);
        rl_bat3 = findViewById(R.id.rl_bat3);
        rl_bat4 = findViewById(R.id.rl_bat4);
        rl_bat5 = findViewById(R.id.rl_bat5);
        rl_bat6 = findViewById(R.id.rl_bat6);

        rl_ar1 = findViewById(R.id.rl_ar1);
        rl_ar2 = findViewById(R.id.rl_ar2);
        rl_ar3 = findViewById(R.id.rl_ar3);
        rl_ar4 = findViewById(R.id.rl_ar4);

        rl_bowler1 = findViewById(R.id.rl_bowler1);
        rl_bowler2 = findViewById(R.id.rl_bowler2);
        rl_bowler3 = findViewById(R.id.rl_bowler3);
        rl_bowler4 = findViewById(R.id.rl_bowler4);
        rl_bowler5 = findViewById(R.id.rl_bowler5);
        rl_bowler6 = findViewById(R.id.rl_bowler6);
        updateTimeRemaining(System.currentTimeMillis());
        setPlayerVisibilityGone();
        setTeam();
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

    private void startUpdateTimer() {
        Timer tmr = new Timer();
        tmr.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.post(updateRemainingTimeRunnable);
            }
        }, 1000, 1000);
    }

    private void setPlayerVisibilityGone() {
        iv_wkt.setVisibility(View.INVISIBLE);
        tv_wkt_name.setVisibility(View.INVISIBLE);

        rl_bat1.setVisibility(View.INVISIBLE);
        rl_bat2.setVisibility(View.INVISIBLE);
        rl_bat3.setVisibility(View.GONE);
        rl_bat4.setVisibility(View.GONE);
        rl_bat5.setVisibility(View.GONE);
        rl_bat6.setVisibility(View.GONE);

        rl_ar1.setVisibility(View.INVISIBLE);
        rl_ar2.setVisibility(View.INVISIBLE);
        rl_ar3.setVisibility(View.INVISIBLE);
        rl_ar4.setVisibility(View.INVISIBLE);

        rl_bowler1.setVisibility(View.INVISIBLE);
        rl_bowler2.setVisibility(View.INVISIBLE);
        rl_bowler3.setVisibility(View.GONE);
        rl_bowler4.setVisibility(View.GONE);
        rl_bowler5.setVisibility(View.GONE);
        rl_bowler6.setVisibility(View.GONE);
    }

    private void setTeam() {
        int total_team1 = 0;
        int total_team2 = 0;

        for (PlayerDto playerDto : selectedTeam) {
            if (playerDto.team_name.trim().equals(selectedMatchDto.teamName1)) {
                total_team1++;
            } else {
                total_team2++;
            }

        }
        String[] team = upComingMatchesDto.short_name.split(" ");
        String team1 = team[0];
        String team2 = team[2];
        ctv_country1.setText(team1);
        ctv_country2.setText(team2);
        tv_team1.setText(selectedMatchDto.teamName1);
        tv_team2.setText(selectedMatchDto.teamName2);
        Picasso.with(this).load(getPictureURL(selectedMatchDto.teamName1)).placeholder(R.drawable.progress_animation).error(R.drawable.no_team).into(cimg_country1);
        Picasso.with(this).load(getPictureURL(selectedMatchDto.teamName2)).placeholder(R.drawable.progress_animation).error(R.drawable.no_team).into(cimg_country2);
        tv_team_count1.setText("" + total_team1 + "/7");
        tv_team_count2.setText("" + total_team2 + "/7");
        arrangePlayerOnField();
    }

    private void arrangePlayerOnField() {

        for (PlayerDto playerDto : selectedTeam) {
            /* Divided  players*/
            switch (playerDto.seasonal_role) {
                case "bowler":
                    bowler++;
                    setVisibleBowler(bowler,playerDto.full_name, playerDto.isC, playerDto.isCV);
                    break;
                case "batsman":
                    batsman++;
                    setVisibleBatsman(batsman,playerDto.full_name, playerDto.isC, playerDto.isCV);
                    break;
                case "allrounder":
                    allrounder++;
                    setVisibleAllRounder(allrounder,playerDto.full_name, playerDto.isC, playerDto.isCV);
                    break;
                case "keeper":
                    iv_wkt.setVisibility(View.VISIBLE);
                    tv_wkt_name.setVisibility(View.VISIBLE);
                    tv_wkt_name.setText(playerDto.full_name);
                    setImageWK(iv_wkt, playerDto.isC, playerDto.isCV);
                    break;
            }
        }
    }

    private void setVisibleAllRounder(int allRounder, String full_name, boolean isC, boolean isVC) {

        switch (allRounder) {
            case 1:
                rl_ar1.setVisibility(View.VISIBLE);
                tv_ar1_name.setText(full_name);
                setImageAllrounder(iv_ar1, isC, isVC);
                break;
            case 2:
                rl_ar2.setVisibility(View.VISIBLE);
                tv_ar2_name.setText(full_name);
                setImageAllrounder(iv_ar2, isC, isVC);
                break;
            case 3:
                rl_ar3.setVisibility(View.VISIBLE);
                tv_ar3_name.setText(full_name);
                setImageAllrounder(iv_ar3, isC, isVC);
                break;
            case 4:
                rl_ar4.setVisibility(View.VISIBLE);
                tv_ar4_name.setText(full_name);
                setImageAllrounder(iv_ar4, isC, isVC);
                break;
        }
    }

    /**
     * Visible Batsman
     */
    private void setVisibleBatsman(int batsman, String full_name, boolean isC, boolean isVC) {
        switch (batsman) {
            case 1:
                rl_bat1.setVisibility(View.VISIBLE);
                tv_bat1_name.setText(full_name);
                setImageBatsman(iv_bat1, isC, isVC);
                break;
            case 2:
                rl_bat2.setVisibility(View.VISIBLE);
                tv_bat2_name.setText(full_name);
                setImageBatsman(iv_bat2, isC, isVC);
                break;
            case 3:
                rl_bat3.setVisibility(View.VISIBLE);
                tv_bat3_name.setText(full_name);
                setImageBatsman(iv_bat3, isC, isVC);
                break;
            case 4:
                rl_bat4.setVisibility(View.VISIBLE);
                tv_bat4_name.setText(full_name);
                setImageBatsman(iv_bat4, isC, isVC);
                break;
            case 5:
                rl_bat5.setVisibility(View.VISIBLE);
                tv_bat5_name.setText(full_name);
                setImageBatsman(iv_bat5, isC, isVC);
                break;
            case 6:
                rl_bat6.setVisibility(View.VISIBLE);
                tv_bat6_name.setText(full_name);
                setImageBatsman(iv_bat6, isC, isVC);
                break;
        }
    }

    /**
     * Visible Bowler
     */
    private void setVisibleBowler(int bowler, String full_name, boolean isC, boolean isVC) {
        switch (bowler) {
            case 1:
                rl_bowler1.setVisibility(View.VISIBLE);
                tv_bowler1_name.setText(full_name);
                setImageBowler(iv_bowler1, isC, isVC);
                break;
            case 2:
                rl_bowler2.setVisibility(View.VISIBLE);
                tv_bowler2_name.setText(full_name);
                setImageBowler(iv_bowler2, isC, isVC);
                break;
            case 3:
                rl_bowler3.setVisibility(View.VISIBLE);
                tv_bowler3_name.setText(full_name);
                setImageBowler(iv_bowler3, isC, isVC);
                break;
            case 4:
                rl_bowler4.setVisibility(View.VISIBLE);
                tv_bowler4_name.setText(full_name);
                setImageBowler(iv_bowler4, isC, isVC);
                break;
            case 5:
                rl_bowler5.setVisibility(View.VISIBLE);
                tv_bowler5_name.setText(full_name);
                setImageBowler(iv_bowler5, isC, isVC);
                break;
            case 6:
                rl_bowler6.setVisibility(View.VISIBLE);
                tv_bowler6_name.setText(full_name);
                setImageBowler(iv_bowler6, isC, isVC);
                break;
        }
    }


    private void setImageWK(ImageView iv_bowler1, boolean isC, boolean isVC) {
        if (isC) {
            iv_bowler1.setImageDrawable(getResources().getDrawable(R.drawable.w1));
        } else if (isVC) {
            iv_bowler1.setImageDrawable(getResources().getDrawable(R.drawable.w2));
        }
    }

    private void setImageAllrounder(ImageView iv_bowler1, boolean isC, boolean isVC) {
        if (isC) {
            iv_bowler1.setImageDrawable(getResources().getDrawable(R.drawable.ar1));
        } else if (isVC) {
            iv_bowler1.setImageDrawable(getResources().getDrawable(R.drawable.ar2));
        }
    }

    private void setImageBowler(ImageView iv_bowler1, boolean isC, boolean isVC) {
        if (isC) {
            iv_bowler1.setImageDrawable(getResources().getDrawable(R.drawable.b1));
        } else if (isVC) {
            iv_bowler1.setImageDrawable(getResources().getDrawable(R.drawable.b2));
        }
    }

    private void setImageBatsman(ImageView iv_bowler1, boolean isC, boolean isVC) {
        if (isC) {
            iv_bowler1.setImageDrawable(getResources().getDrawable(R.drawable.bat1));
        } else if (isVC) {
            iv_bowler1.setImageDrawable(getResources().getDrawable(R.drawable.bat2));
        }
    }

    public void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.btn_save:
                Bundle bundle = new Bundle();
                bundle.putSerializable("selectedTeam", selectedTeam);
                bundle.putSerializable("selectedMatchDto", selectedMatchDto);
                bundle.putSerializable("upComingMatchesDto", upComingMatchesDto);
                ActivityController.startNextActivity(this, ChooseContestActivity.class, bundle, false);
                break;
        }
    }

    private String getPictureURL(String teama) {
        String country = teama.trim().replace(" ", "-");
        return "http://52.15.50.179/public/images/team/flag-of-" + country + ".png";
    }
}
