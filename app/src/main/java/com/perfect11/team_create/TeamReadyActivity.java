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

    private ImageView iv_wkt_c;
    private ImageView iv_bat1_c, iv_bat2_c, iv_bat3_c, iv_bat4_c, iv_bat5_c, iv_bat6_c;
    private ImageView iv_ar1_c, iv_ar2_c, iv_ar3_c, iv_ar4_c;
    private ImageView iv_bowler1_c, iv_bowler2_c, iv_bowler3_c, iv_bowler4_c, iv_bowler5_c, iv_bowler6_c;

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

        iv_wkt_c = findViewById(R.id.iv_wkt_c);

        iv_bat1_c = findViewById(R.id.iv_bat1_c);
        iv_bat2_c = findViewById(R.id.iv_bat2_c);
        iv_bat3_c = findViewById(R.id.iv_bat3_c);
        iv_bat4_c = findViewById(R.id.iv_bat4_c);
        iv_bat5_c = findViewById(R.id.iv_bat5_c);
        iv_bat6_c = findViewById(R.id.iv_bat6_c);

        iv_ar1_c = findViewById(R.id.iv_ar1_c);
        iv_ar2_c = findViewById(R.id.iv_ar2_c);
        iv_ar3_c = findViewById(R.id.iv_ar3_c);
        iv_ar4_c = findViewById(R.id.iv_ar4_c);

        iv_bowler1_c = findViewById(R.id.iv_bowler1_c);
        iv_bowler2_c = findViewById(R.id.iv_bowler2_c);
        iv_bowler3_c = findViewById(R.id.iv_bowler3_c);
        iv_bowler4_c = findViewById(R.id.iv_bowler4_c);
        iv_bowler5_c = findViewById(R.id.iv_bowler5_c);
        iv_bowler6_c = findViewById(R.id.iv_bowler6_c);

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
        tv_team1.setText(team1);
        tv_team2.setText(team2);

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
                    setVisibleBowler(bowler,playerDto.full_name,playerDto.team_code, playerDto.isC, playerDto.isCV);
                    break;
                case "batsman":
                    batsman++;
                    setVisibleBatsman(batsman,playerDto.full_name,playerDto.team_code, playerDto.isC, playerDto.isCV);
                    break;
                case "allrounder":
                    allrounder++;
                    setVisibleAllRounder(allrounder,playerDto.full_name,playerDto.team_code, playerDto.isC, playerDto.isCV);
                    break;
                case "keeper":
                    iv_wkt.setVisibility(View.VISIBLE);
                    tv_wkt_name.setVisibility(View.VISIBLE);
                    tv_wkt_name.setText(playerDto.full_name);
                    setCImageWK(iv_wkt_c, playerDto.isC, playerDto.isCV);
                    setKeeperImage(iv_wkt,playerDto.team_code);
                    break;
            }
        }
    }

    private void setKeeperImage(ImageView iv_wkt, String team_code) {
        String url = "";
        switch (team_code) {
            case "CSK":
                url = "http://52.15.50.179/public/images/app/players/wicket-keeper-csk.png";
                break;
            case "KXIP":
                url = "http://52.15.50.179/public/images/app/players/wicket-keeper-kxip.png";
                break;
            case "MI":
                url = "http://52.15.50.179/public/images/app/players/wicket-keeper-mi.png";
                break;
            case "DD":
                url = "http://52.15.50.179/public/images/app/players/wicket-keeper-dd.png";
                break;
            case "KKR":
                url = "http://52.15.50.179/public/images/app/players/wicket-keeper-kkr.png";
                break;
            case "RCB":
                url = "http://52.15.50.179/public/images/app/players/wicket-keeper-rcb.png";
                break;
            case "SRH":
                url = "http://52.15.50.179/public/images/app/players/wicket-keeper-srh.png";
                break;
            case "RR":
                url = "http://52.15.50.179/public/images/app/players/wicket-keeper-rr.png";
                break;
            default:
                url = "";
                break;
        }
        if (!url.trim().equals("")) {
            Picasso.with(this).load(url).placeholder(R.drawable.progress_animation).error(R.drawable.myteam).into(iv_wkt);
        }
    }
    private void setAllrounderImage(ImageView iv_wkt, String team_code) {
        String url = "";
        switch (team_code) {
            case "CSK":
                url = "http://52.15.50.179/public/images/app/players/fielder-cks.png";
                break;
            case "KXIP":
                url = "http://52.15.50.179/public/images/app/players/fielder-kxip.png";
                break;
            case "MI":
                url = "http://52.15.50.179/public/images/app/players/fielder-mi.png";
                break;
            case "DD":
                url = "http://52.15.50.179/public/images/app/players/fielder-dd.png";
                break;
            case "KKR":
                url = "http://52.15.50.179/public/images/app/players/fielder-kkr.png";
                break;
            case "RCB":
                url = "http://52.15.50.179/public/images/app/players/fielder-rcb.png";
                break;
            case "SRH":
                url = "http://52.15.50.179/public/images/app/players/fielder-srh.png";
                break;
            case "RR":
                url = "http://52.15.50.179/public/images/app/players/fielder-rr.png";
                break;
            default:
                url = "";
                break;
        }
        if (!url.trim().equals("")) {
            Picasso.with(this).load(url).placeholder(R.drawable.progress_animation).error(R.drawable.myteam).into(iv_wkt);
        }
    }
    private void setBatsmanImage(ImageView iv_wkt, String team_code) {
        String url = "";
        switch (team_code) {
            case "CSK":
                url = "http://52.15.50.179/public/images/app/players/batsman-csk.png";
                break;
            case "KXIP":
                url = "http://52.15.50.179/public/images/app/players/batsman-kxip.png";
                break;
            case "MI":
                url = "http://52.15.50.179/public/images/app/players/batsman-mi.png";
                break;
            case "DD":
                url = "http://52.15.50.179/public/images/app/players/batsman-dd.png";
                break;
            case "KKR":
                url = "http://52.15.50.179/public/images/app/players/batsman-kkr.png";
                break;
            case "RCB":
                url = "http://52.15.50.179/public/images/app/players/batsman-rcb.png";
                break;
            case "SRH":
                url = "http://52.15.50.179/public/images/app/players/batsman-srh.png";
                break;
            case "RR":
                url = "http://52.15.50.179/public/images/app/players/batsman-rr.png";
                break;
            default:
                url = "";
                break;
        }
        if (!url.trim().equals("")) {
            Picasso.with(this).load(url).placeholder(R.drawable.progress_animation).error(R.drawable.myteam).into(iv_wkt);
        }
    }
    private void setBowlerImage(ImageView iv_wkt, String team_code) {
        String url = "";
        switch (team_code) {
            case "CSK":
                url = "http://52.15.50.179/public/images/app/players/bowler-csk.png";
                break;
            case "KXIP":
                url = "http://52.15.50.179/public/images/app/players/bowler-kxip.png";
                break;
            case "MI":
                url = "http://52.15.50.179/public/images/app/players/bowler-mi.png";
                break;
            case "DD":
                url = "http://52.15.50.179/public/images/app/players/bowler-dd.png";
                break;
            case "KKR":
                url = "http://52.15.50.179/public/images/app/players/bowler-kkr.png";
                break;
            case "RCB":
                url = "http://52.15.50.179/public/images/app/players/bowler-rcb.png";
                break;
            case "SRH":
                url = "http://52.15.50.179/public/images/app/players/bowler-srh.png";
                break;
            case "RR":
                url = "http://52.15.50.179/public/images/app/players/bowler-rr.png";
                break;
            default:
                url = "";
                break;
        }
        if (!url.trim().equals("")) {
            Picasso.with(this).load(url).placeholder(R.drawable.progress_animation).error(R.drawable.myteam).into(iv_wkt);
        }
    }

    private void setVisibleAllRounder(int allRounder, String full_name, String team_code, boolean isC, boolean isVC) {

        switch (allRounder) {
            case 1:
                rl_ar1.setVisibility(View.VISIBLE);
                tv_ar1_name.setText(full_name);
                setCImageAllrounder(iv_ar1_c, isC, isVC);
                setAllrounderImage(iv_ar1,team_code);
                break;
            case 2:
                rl_ar2.setVisibility(View.VISIBLE);
                tv_ar2_name.setText(full_name);
                setCImageAllrounder(iv_ar2_c, isC, isVC);
                setAllrounderImage(iv_ar2,team_code);
                break;
            case 3:
                rl_ar3.setVisibility(View.VISIBLE);
                tv_ar3_name.setText(full_name);
                setCImageAllrounder(iv_ar3_c, isC, isVC);
                setAllrounderImage(iv_ar3,team_code);
                break;
            case 4:
                rl_ar4.setVisibility(View.VISIBLE);
                tv_ar4_name.setText(full_name);
                setCImageAllrounder(iv_ar4_c, isC, isVC);
                setAllrounderImage(iv_ar4,team_code);
                break;
        }
    }

    /**
     * Visible Batsman
     */
    private void setVisibleBatsman(int batsman, String full_name, String team_code, boolean isC, boolean isVC) {
        switch (batsman) {
            case 1:
                rl_bat1.setVisibility(View.VISIBLE);
                tv_bat1_name.setText(full_name);
                setImageBatsman(iv_bat1_c, isC, isVC);
                setBatsmanImage(iv_bat1,team_code);
                break;
            case 2:
                rl_bat2.setVisibility(View.VISIBLE);
                tv_bat2_name.setText(full_name);
                setImageBatsman(iv_bat2_c, isC, isVC);
                setBatsmanImage(iv_bat2,team_code);
                break;
            case 3:
                rl_bat3.setVisibility(View.VISIBLE);
                tv_bat3_name.setText(full_name);
                setImageBatsman(iv_bat3_c, isC, isVC);
                setBatsmanImage(iv_bat3,team_code);
                break;
            case 4:
                rl_bat4.setVisibility(View.VISIBLE);
                tv_bat4_name.setText(full_name);
                setImageBatsman(iv_bat4_c, isC, isVC);
                setBatsmanImage(iv_bat4,team_code);
                break;
            case 5:
                rl_bat5.setVisibility(View.VISIBLE);
                tv_bat5_name.setText(full_name);
                setImageBatsman(iv_bat5_c, isC, isVC);
                setBatsmanImage(iv_bat5,team_code);
                break;
            case 6:
                rl_bat6.setVisibility(View.VISIBLE);
                tv_bat6_name.setText(full_name);
                setImageBatsman(iv_bat6_c, isC, isVC);
                setBatsmanImage(iv_bat6,team_code);
                break;
        }
    }

    /**
     * Visible Bowler
     */
    private void setVisibleBowler(int bowler, String full_name, String team_code, boolean isC, boolean isVC) {
        switch (bowler) {
            case 1:
                rl_bowler1.setVisibility(View.VISIBLE);
                tv_bowler1_name.setText(full_name);
                setCImageBowler(iv_bowler1_c, isC, isVC);
                setBowlerImage(iv_bowler1,team_code);
                break;
            case 2:
                rl_bowler2.setVisibility(View.VISIBLE);
                tv_bowler2_name.setText(full_name);
                setCImageBowler(iv_bowler2_c, isC, isVC);
                setBowlerImage(iv_bowler2,team_code);
                break;
            case 3:
                rl_bowler3.setVisibility(View.VISIBLE);
                tv_bowler3_name.setText(full_name);
                setCImageBowler(iv_bowler3_c, isC, isVC);
                setBowlerImage(iv_bowler3,team_code);
                break;
            case 4:
                rl_bowler4.setVisibility(View.VISIBLE);
                tv_bowler4_name.setText(full_name);
                setCImageBowler(iv_bowler4_c, isC, isVC);
                setBowlerImage(iv_bowler4,team_code);
                break;
            case 5:
                rl_bowler5.setVisibility(View.VISIBLE);
                tv_bowler5_name.setText(full_name);
                setCImageBowler(iv_bowler5_c, isC, isVC);
                setBowlerImage(iv_bowler5,team_code);
                break;
            case 6:
                rl_bowler6.setVisibility(View.VISIBLE);
                tv_bowler6_name.setText(full_name);
                setCImageBowler(iv_bowler6_c, isC, isVC);
                setBowlerImage(iv_bowler6,team_code);
                break;
        }
    }


    private void setCImageWK(ImageView iv_bowler1, boolean isC, boolean isVC) {
        if (isC) {
            iv_bowler1.setVisibility(View.VISIBLE);
            iv_bowler1.setImageDrawable(getResources().getDrawable(R.drawable.c));
        } else if (isVC) {
            iv_bowler1.setVisibility(View.VISIBLE);
            iv_bowler1.setImageDrawable(getResources().getDrawable(R.drawable.vc));
        }
    }

    private void setCImageAllrounder(ImageView iv_bowler1, boolean isC, boolean isVC) {
        if (isC) {
            iv_bowler1.setVisibility(View.VISIBLE);
            iv_bowler1.setImageDrawable(getResources().getDrawable(R.drawable.c));
        } else if (isVC) {
            iv_bowler1.setVisibility(View.VISIBLE);
            iv_bowler1.setImageDrawable(getResources().getDrawable(R.drawable.vc));
        }
    }

    private void setCImageBowler(ImageView iv_bowler1, boolean isC, boolean isVC) {
        if (isC) {
            iv_bowler1.setVisibility(View.VISIBLE);
            iv_bowler1.setImageDrawable(getResources().getDrawable(R.drawable.c));
        } else if (isVC) {
            iv_bowler1.setVisibility(View.VISIBLE);
            iv_bowler1.setImageDrawable(getResources().getDrawable(R.drawable.vc));
        }
    }

    private void setImageBatsman(ImageView iv_bowler1, boolean isC, boolean isVC) {
        if (isC) {
            iv_bowler1.setVisibility(View.VISIBLE);
            iv_bowler1.setImageDrawable(getResources().getDrawable(R.drawable.c));
        } else if (isVC) {
            iv_bowler1.setVisibility(View.VISIBLE);
            iv_bowler1.setImageDrawable(getResources().getDrawable(R.drawable.vc));
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
