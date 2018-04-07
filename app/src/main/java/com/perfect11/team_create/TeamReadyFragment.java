package com.perfect11.team_create;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.perfect11.R;
import com.perfect11.base.ApiClient;
import com.perfect11.base.ApiClient4;
import com.perfect11.base.ApiInterface;
import com.perfect11.base.BaseFragment;
import com.perfect11.base.BaseHeaderActivity;
import com.perfect11.home.HomeFragment;
import com.perfect11.home.dto.JoinContestCallBackDto;
import com.perfect11.home.dto.TeamIDDto;
import com.perfect11.home.wrapper.CreateTeamCallBackWrapper;
import com.perfect11.login_signup.dto.UserDto;
import com.perfect11.payment.paytm.Checksum;
import com.perfect11.payment.paytm.Paytm;
import com.perfect11.payment.paytm.Transaction;
import com.perfect11.payment.wrapper.TransactionWrapper;
import com.perfect11.team_create.dto.ContestDto;
import com.perfect11.team_create.dto.PlayerDto;
import com.perfect11.team_create.dto.SelectedMatchDto;
import com.perfect11.upcoming_matches.dto.UpComingMatchesDto;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.squareup.picasso.Picasso;
import com.utility.AlertDialogCallBack;
import com.utility.Constants;
import com.utility.DialogUtility;
import com.utility.PreferenceUtility;
import com.utility.customView.CustomButton;
import com.utility.customView.CustomTextView;

import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.utility.Constants.TAG;

public class TeamReadyFragment extends BaseFragment implements PaytmPaymentTransactionCallback, PaymentResultListener {
    private ArrayList<PlayerDto> selectedTeam;
    private CustomTextView tv_team1, tv_team2, tv_team_count1, tv_team_count2, ctv_time, ctv_country1, ctv_country2;
    private ImageView cimg_country1, cimg_country2;

    private ImageView iv_wkt, iv_bat1, iv_bat2, iv_bat3, iv_bat4, iv_bat5, iv_bat6, iv_ar1, iv_ar2, iv_ar3, iv_ar4, iv_bowler1, iv_bowler2, iv_bowler3,
            iv_bowler4, iv_bowler5, iv_bowler6;


    private ImageView iv_wkt_c;
    private ImageView iv_bat1_c, iv_bat2_c, iv_bat3_c, iv_bat4_c, iv_bat5_c, iv_bat6_c;
    private ImageView iv_ar1_c, iv_ar2_c, iv_ar3_c, iv_ar4_c;
    private ImageView iv_bowler1_c, iv_bowler2_c, iv_bowler3_c, iv_bowler4_c, iv_bowler5_c, iv_bowler6_c;

    private CustomTextView tv_wkt_name, tv_bat1_name, tv_bat2_name, tv_bat3_name, tv_bat4_name,
            tv_bat5_name, tv_bat6_name, tv_ar1_name, tv_ar2_name, tv_ar3_name, tv_ar4_name, tv_bowler1_name, tv_bowler2_name, tv_bowler3_name,
            tv_bowler4_name, tv_bowler5_name, tv_bowler6_name;

    private RelativeLayout rl_bat1, rl_bat2, rl_bat3, rl_bat4, rl_bat5, rl_bat6, rl_ar1, rl_ar2, rl_ar3, rl_ar4, rl_bowler1, rl_bowler2, rl_bowler3,
            rl_bowler4, rl_bowler5, rl_bowler6;

    private String team1, team2;

    private CustomButton btn_save;

    private int bowler = 0, batsman = 0, allrounder = 0, keeper = 0;

    private SelectedMatchDto selectedMatchDto;
    private UpComingMatchesDto upComingMatchesDto;
    private ApiInterface apiInterface;

    private ArrayList<String> batsmanList = new ArrayList<>();
    private ArrayList<String> allRounderList = new ArrayList<>();
    private ArrayList<String> bowlerList = new ArrayList<>();
    private ArrayList<String> keeperList = new ArrayList<>();
    private String captain = "", vCaptain = "";
    private float player_amount_count = 0;
    private UserDto userDto;
    private ContestDto contestDto;
    private TeamIDDto teamIDDto;
    public HttpLoggingInterceptor interceptor = null;
    public OkHttpClient client = null;
    public Gson gson;
    private String paymentGateway, contestId, amount;
    private Handler mHandler = new Handler();
    private Runnable updateRemainingTimeRunnable = new Runnable() {
        @Override
        public void run() {
            long currentTime = System.currentTimeMillis();
            updateTimeRemaining(currentTime);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.ready_team, container, false);
        readFromBundle();
        startUpdateTimer();
        initView();
        return view;
    }

    private void readFromBundle() {
        userDto = (UserDto) PreferenceUtility.getObjectInAppPreference(getActivity(), PreferenceUtility.APP_PREFERENCE_NAME);
        selectedTeam = (ArrayList<PlayerDto>) getArguments().getSerializable("selectedTeam");
        selectedMatchDto = (SelectedMatchDto) getArguments().getSerializable("selectedMatchDto");
        upComingMatchesDto = (UpComingMatchesDto) getArguments().getSerializable("upComingMatchesDto");
        contestDto = (ContestDto) getArguments().getSerializable("contestDto");
//        System.out.println("upComingMatchesDto:" + upComingMatchesDto.toString());
    }

    private void initView() {
        tv_team1 = view.findViewById(R.id.tv_team1);
        tv_team2 = view.findViewById(R.id.tv_team2);
        ctv_country1 = view.findViewById(R.id.ctv_country1);
        ctv_country2 = view.findViewById(R.id.ctv_country2);
        tv_team_count1 = view.findViewById(R.id.tv_team_count1);
        tv_team_count2 = view.findViewById(R.id.tv_team_count2);
        ctv_time = view.findViewById(R.id.ctv_time);

        cimg_country1 = view.findViewById(R.id.cimg_country1);
        cimg_country2 = view.findViewById(R.id.cimg_country2);

        iv_wkt = view.findViewById(R.id.iv_wkt);

        iv_bat1 = view.findViewById(R.id.iv_bat1);
        iv_bat2 = view.findViewById(R.id.iv_bat2);
        iv_bat3 = view.findViewById(R.id.iv_bat3);
        iv_bat4 = view.findViewById(R.id.iv_bat4);
        iv_bat5 = view.findViewById(R.id.iv_bat5);
        iv_bat6 = view.findViewById(R.id.iv_bat6);

        iv_ar1 = view.findViewById(R.id.iv_ar1);
        iv_ar2 = view.findViewById(R.id.iv_ar2);
        iv_ar3 = view.findViewById(R.id.iv_ar3);
        iv_ar4 = view.findViewById(R.id.iv_ar4);

        iv_bowler1 = view.findViewById(R.id.iv_bowler1);
        iv_bowler2 = view.findViewById(R.id.iv_bowler2);
        iv_bowler3 = view.findViewById(R.id.iv_bowler3);
        iv_bowler4 = view.findViewById(R.id.iv_bowler4);
        iv_bowler5 = view.findViewById(R.id.iv_bowler5);
        iv_bowler6 = view.findViewById(R.id.iv_bowler6);

        iv_wkt_c = view.findViewById(R.id.iv_wkt_c);

        iv_bat1_c = view.findViewById(R.id.iv_bat1_c);
        iv_bat2_c = view.findViewById(R.id.iv_bat2_c);
        iv_bat3_c = view.findViewById(R.id.iv_bat3_c);
        iv_bat4_c = view.findViewById(R.id.iv_bat4_c);
        iv_bat5_c = view.findViewById(R.id.iv_bat5_c);
        iv_bat6_c = view.findViewById(R.id.iv_bat6_c);

        iv_ar1_c = view.findViewById(R.id.iv_ar1_c);
        iv_ar2_c = view.findViewById(R.id.iv_ar2_c);
        iv_ar3_c = view.findViewById(R.id.iv_ar3_c);
        iv_ar4_c = view.findViewById(R.id.iv_ar4_c);

        iv_bowler1_c = view.findViewById(R.id.iv_bowler1_c);
        iv_bowler2_c = view.findViewById(R.id.iv_bowler2_c);
        iv_bowler3_c = view.findViewById(R.id.iv_bowler3_c);
        iv_bowler4_c = view.findViewById(R.id.iv_bowler4_c);
        iv_bowler5_c = view.findViewById(R.id.iv_bowler5_c);
        iv_bowler6_c = view.findViewById(R.id.iv_bowler6_c);

        btn_save = view.findViewById(R.id.btn_save);
        if (selectedMatchDto.isEditing)
            btn_save.setText("Update Team");
        else
            btn_save.setText("Save Team");


        tv_wkt_name = view.findViewById(R.id.tv_wkt_name);

        tv_bat1_name = view.findViewById(R.id.tv_bat1_name);
        tv_bat2_name = view.findViewById(R.id.tv_bat2_name);
        tv_bat3_name = view.findViewById(R.id.tv_bat3_name);
        tv_bat4_name = view.findViewById(R.id.tv_bat4_name);
        tv_bat5_name = view.findViewById(R.id.tv_bat5_name);
        tv_bat6_name = view.findViewById(R.id.tv_bat6_name);

        tv_ar1_name = view.findViewById(R.id.tv_ar1_name);
        tv_ar2_name = view.findViewById(R.id.tv_ar2_name);
        tv_ar3_name = view.findViewById(R.id.tv_ar3_name);
        tv_ar4_name = view.findViewById(R.id.tv_ar4_name);

        tv_bowler1_name = view.findViewById(R.id.tv_bowler1_name);
        tv_bowler2_name = view.findViewById(R.id.tv_bowler2_name);
        tv_bowler3_name = view.findViewById(R.id.tv_bowler3_name);
        tv_bowler4_name = view.findViewById(R.id.tv_bowler4_name);
        tv_bowler5_name = view.findViewById(R.id.tv_bowler5_name);
        tv_bowler6_name = view.findViewById(R.id.tv_bowler6_name);

        rl_bat1 = view.findViewById(R.id.rl_bat1);
        rl_bat2 = view.findViewById(R.id.rl_bat2);
        rl_bat3 = view.findViewById(R.id.rl_bat3);
        rl_bat4 = view.findViewById(R.id.rl_bat4);
        rl_bat5 = view.findViewById(R.id.rl_bat5);
        rl_bat6 = view.findViewById(R.id.rl_bat6);

        rl_ar1 = view.findViewById(R.id.rl_ar1);
        rl_ar2 = view.findViewById(R.id.rl_ar2);
        rl_ar3 = view.findViewById(R.id.rl_ar3);
        rl_ar4 = view.findViewById(R.id.rl_ar4);

        rl_bowler1 = view.findViewById(R.id.rl_bowler1);
        rl_bowler2 = view.findViewById(R.id.rl_bowler2);
        rl_bowler3 = view.findViewById(R.id.rl_bowler3);
        rl_bowler4 = view.findViewById(R.id.rl_bowler4);
        rl_bowler5 = view.findViewById(R.id.rl_bowler5);
        rl_bowler6 = view.findViewById(R.id.rl_bowler6);

        String[] team = upComingMatchesDto.short_name.split(" ");
        team1 = team[0].trim();
        team2 = team[2].trim();
        ctv_country1.setText(team1);
        ctv_country2.setText(team2);
        tv_team1.setText(team1);
        tv_team2.setText(team2);

        setPlayerVisibilityGone();
        setTeam();
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

        Picasso.with(getActivity()).load(getPictureURL(selectedMatchDto.teamName1)).placeholder(R.drawable.progress_animation).error(R.drawable.team_face1).
                into(cimg_country1);
        Picasso.with(getActivity()).load(getPictureURL(selectedMatchDto.teamName2)).placeholder(R.drawable.progress_animation).error(R.drawable.team_face2).
                into(cimg_country2);
        tv_team_count1.setText("" + total_team1 + "/7");
        tv_team_count2.setText("" + total_team2 + "/7");
        arrangePlayerOnField();
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

    private void arrangePlayerOnField() {

        for (PlayerDto playerDto : selectedTeam) {
            /* Divided  players*/
            switch (playerDto.seasonal_role) {
                case "bowler":
                    bowler++;
                    setVisibleBowler(bowler, playerDto.full_name, playerDto.team_code, playerDto.isC, playerDto.isCV);
                    bowlerList.add(playerDto.short_name);
                    break;
                case "batsman":
                    batsman++;
                    setVisibleBatsman(batsman, playerDto.full_name, playerDto.team_code, playerDto.isC, playerDto.isCV);
                    batsmanList.add(playerDto.short_name);
                    break;
                case "allrounder":
                    allrounder++;
                    setVisibleAllRounder(allrounder, playerDto.full_name, playerDto.team_code, playerDto.isC, playerDto.isCV);
                    allRounderList.add(playerDto.short_name);
                    break;
                case "keeper":
                    iv_wkt.setVisibility(View.VISIBLE);
                    tv_wkt_name.setVisibility(View.VISIBLE);
                    tv_wkt_name.setText(playerDto.full_name);
                    setCImageWK(iv_wkt_c, playerDto.isC, playerDto.isCV);
                    setKeeperImage(iv_wkt, playerDto.team_code);
                    keeperList.add(playerDto.short_name);
                    break;
            }
            player_amount_count = player_amount_count + Float.parseFloat(playerDto.credit);

            if (playerDto.isC) {
                captain = playerDto.short_name;
            }
            if (playerDto.isCV) {
                vCaptain = playerDto.short_name;
            }
        }
    }

    private void setKeeperImage(ImageView iv_wkt, String team_code) {
        String url = "";
        switch (team_code) {
            case "CSK":
                url = "https://perfect11.in/public/images/app/players/wicket-keeper-csk.png";
                break;
            case "KXIP":
                url = "https://perfect11.in/public/images/app/players/wicket-keeper-kxip.png";
                break;
            case "MI":
                url = "https://perfect11.in/public/images/app/players/wicket-keeper-mi.png";
                break;
            case "DD":
                url = "https://perfect11.in/public/images/app/players/wicket-keeper-dd.png";
                break;
            case "KKR":
                url = "https://perfect11.in/public/images/app/players/wicket-keeper-kkr.png";
                break;
            case "RCB":
                url = "https://perfect11.in/public/images/app/players/wicket-keeper-rcb.png";
                break;
            case "SRH":
                url = "https://perfect11.in/public/images/app/players/wicket-keeper-srh.png";
                break;
            case "RR":
                url = "https://perfect11.in/public/images/app/players/wicket-keeper-rr.png";
                break;
            default:
                url = "";
                if (team_code.trim().equals(team1)) {
                    iv_wkt.setImageDrawable(getResources().getDrawable(R.drawable.man33));
                } else {
                    iv_wkt.setImageDrawable(getResources().getDrawable(R.drawable.man3));
                }
                break;
        }
        if (!url.trim().equals("")) {
            Picasso.with(getActivity()).load(url).placeholder(R.drawable.progress_animation).error(R.drawable.myteam).into(iv_wkt);
        }
    }

    private void setAllrounderImage(ImageView iv_wkt, String team_code) {
        String url = "";
        switch (team_code) {
            case "CSK":
                url = "https://perfect11.in/public/images/app/players/fielder-cks.png";
                break;
            case "KXIP":
                url = "https://perfect11.in/public/images/app/players/fielder-kxip.png";
                break;
            case "MI":
                url = "https://perfect11.in/public/images/app/players/fielder-mi.png";
                break;
            case "DD":
                url = "https://perfect11.in/public/images/app/players/fielder-dd.png";
                break;
            case "KKR":
                url = "https://perfect11.in/public/images/app/players/fielder-kkr.png";
                break;
            case "RCB":
                url = "https://perfect11.in/public/images/app/players/fielder-rcb.png";
                break;
            case "SRH":
                url = "https://perfect11.in/public/images/app/players/fielder-srh.png";
                break;
            case "RR":
                url = "https://perfect11.in/public/images/app/players/fielder-rr.png";
                break;
            default:
                url = "";
                if (team_code.trim().equals(team1)) {
                    iv_wkt.setImageDrawable(getResources().getDrawable(R.drawable.man22));
                } else {
                    iv_wkt.setImageDrawable(getResources().getDrawable(R.drawable.man2));
                }
                break;
        }
        if (!url.trim().equals("")) {
            Picasso.with(getActivity()).load(url).placeholder(R.drawable.progress_animation).error(R.drawable.myteam).into(iv_wkt);
        }
    }

    private void setBatsmanImage(ImageView iv_wkt, String team_code) {
        String url = "";
        switch (team_code) {
            case "CSK":
                url = "https://perfect11.in/public/images/app/players/batsman-csk.png";
                break;
            case "KXIP":
                url = "https://perfect11.in/public/images/app/players/batsman-kxip.png";
                break;
            case "MI":
                url = "https://perfect11.in/public/images/app/players/batsman-mi.png";
                break;
            case "DD":
                url = "https://perfect11.in/public/images/app/players/batsman-dd.png";
                break;
            case "KKR":
                url = "https://perfect11.in/public/images/app/players/batsman-kkr.png";
                break;
            case "RCB":
                url = "https://perfect11.in/public/images/app/players/batsman-rcb.png";
                break;
            case "SRH":
                url = "https://perfect11.in/public/images/app/players/batsman-srh.png";
                break;
            case "RR":
                url = "https://perfect11.in/public/images/app/players/batsman-rr.png";
                break;
            default:
                url = "";
                if (team_code.trim().equals(team1)) {
                    iv_wkt.setImageDrawable(getResources().getDrawable(R.drawable.man44));
                } else {
                    iv_wkt.setImageDrawable(getResources().getDrawable(R.drawable.man4));
                }
                break;
        }
        if (!url.trim().equals("")) {
            Picasso.with(getActivity()).load(url).placeholder(R.drawable.progress_animation).error(R.drawable.myteam).into(iv_wkt);
        }
    }

    private void setBowlerImage(ImageView iv_wkt, String team_code) {
        String url = "";
        switch (team_code) {
            case "CSK":
                url = "https://perfect11.in/public/images/app/players/bowler-csk.png";
                break;
            case "KXIP":
                url = "https://perfect11.in/public/images/app/players/bowler-kxip.png";
                break;
            case "MI":
                url = "https://perfect11.in/public/images/app/players/bowler-mi.png";
                break;
            case "DD":
                url = "https://perfect11.in/public/images/app/players/bowler-dd.png";
                break;
            case "KKR":
                url = "https://perfect11.in/public/images/app/players/bowler-kkr.png";
                break;
            case "RCB":
                url = "https://perfect11.in/public/images/app/players/bowler-rcb.png";
                break;
            case "SRH":
                url = "https://perfect11.in/public/images/app/players/bowler-srh.png";
                break;
            case "RR":
                url = "https://perfect11.in/public/images/app/players/bowler-rr.png";
                break;
            default:
                url = "";
                if (team_code.trim().equals(team1)) {
                    iv_wkt.setImageDrawable(getResources().getDrawable(R.drawable.man11));
                } else {
                    iv_wkt.setImageDrawable(getResources().getDrawable(R.drawable.man1));
                }
                break;
        }
        if (!url.trim().equals("")) {
            Picasso.with(getActivity()).load(url).placeholder(R.drawable.progress_animation).error(R.drawable.myteam).into(iv_wkt);
        }
    }

    private void setVisibleAllRounder(int allRounder, String full_name, String team_code, boolean isC, boolean isVC) {

        switch (allRounder) {
            case 1:
                rl_ar1.setVisibility(View.VISIBLE);
                tv_ar1_name.setText(full_name);
                setCImageAllrounder(iv_ar1_c, isC, isVC);
                setAllrounderImage(iv_ar1, team_code);
                break;
            case 2:
                rl_ar2.setVisibility(View.VISIBLE);
                tv_ar2_name.setText(full_name);
                setCImageAllrounder(iv_ar2_c, isC, isVC);
                setAllrounderImage(iv_ar2, team_code);
                break;
            case 3:
                rl_ar3.setVisibility(View.VISIBLE);
                tv_ar3_name.setText(full_name);
                setCImageAllrounder(iv_ar3_c, isC, isVC);
                setAllrounderImage(iv_ar3, team_code);
                break;
            case 4:
                rl_ar4.setVisibility(View.VISIBLE);
                tv_ar4_name.setText(full_name);
                setCImageAllrounder(iv_ar4_c, isC, isVC);
                setAllrounderImage(iv_ar4, team_code);
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
                setCImageBatsman(iv_bat1_c, isC, isVC);
                setBatsmanImage(iv_bat1, team_code);
                break;
            case 2:
                rl_bat2.setVisibility(View.VISIBLE);
                tv_bat2_name.setText(full_name);
                setCImageBatsman(iv_bat2_c, isC, isVC);
                setBatsmanImage(iv_bat2, team_code);
                break;
            case 3:
                rl_bat3.setVisibility(View.VISIBLE);
                tv_bat3_name.setText(full_name);
                setCImageBatsman(iv_bat3_c, isC, isVC);
                setBatsmanImage(iv_bat3, team_code);
                break;
            case 4:
                rl_bat4.setVisibility(View.VISIBLE);
                tv_bat4_name.setText(full_name);
                setCImageBatsman(iv_bat4_c, isC, isVC);
                setBatsmanImage(iv_bat4, team_code);
                break;
            case 5:
                rl_bat5.setVisibility(View.VISIBLE);
                tv_bat5_name.setText(full_name);
                setCImageBatsman(iv_bat5_c, isC, isVC);
                setBatsmanImage(iv_bat5, team_code);
                break;
            case 6:
                rl_bat6.setVisibility(View.VISIBLE);
                tv_bat6_name.setText(full_name);
                setCImageBatsman(iv_bat6_c, isC, isVC);
                setBatsmanImage(iv_bat6, team_code);
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
                setBowlerImage(iv_bowler1, team_code);
                break;
            case 2:
                rl_bowler2.setVisibility(View.VISIBLE);
                tv_bowler2_name.setText(full_name);
                setCImageBowler(iv_bowler2_c, isC, isVC);
                setBowlerImage(iv_bowler2, team_code);
                break;
            case 3:
                rl_bowler3.setVisibility(View.VISIBLE);
                tv_bowler3_name.setText(full_name);
                setCImageBowler(iv_bowler3_c, isC, isVC);
                setBowlerImage(iv_bowler3, team_code);
                break;
            case 4:
                rl_bowler4.setVisibility(View.VISIBLE);
                tv_bowler4_name.setText(full_name);
                setCImageBowler(iv_bowler4_c, isC, isVC);
                setBowlerImage(iv_bowler4, team_code);
                break;
            case 5:
                rl_bowler5.setVisibility(View.VISIBLE);
                tv_bowler5_name.setText(full_name);
                setCImageBowler(iv_bowler5_c, isC, isVC);
                setBowlerImage(iv_bowler5, team_code);
                break;
            case 6:
                rl_bowler6.setVisibility(View.VISIBLE);
                tv_bowler6_name.setText(full_name);
                setCImageBowler(iv_bowler6_c, isC, isVC);
                setBowlerImage(iv_bowler6, team_code);
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

    private void setCImageBatsman(ImageView iv_bowler1, boolean isC, boolean isVC) {
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
                getActivity().onBackPressed();
                break;
            case R.id.btn_save:
                /*Bundle bundle=new Bundle();
                bundle.putSerializable("selectedTeam",selectedTeam);
                bundle.putSerializable("selectedMatchDto", selectedMatchDto);
                bundle.putSerializable("upComingMatchesDto", upComingMatchesDto);
                ActivityController.startNextActivity(getActivity(), ChooseContestActivity.class,bundle, false);*/
                if (selectedMatchDto.isEditing)
                    callUpdateTeamAPI();
                else
                    callCreateTeamAPI();
                break;
        }
    }


    private String getPictureURL(String teama) {
        String country = teama.trim().replace(" ", "-");
        return "https://perfect11.in/public/images/app/country/" + country + ".png";
    }


    private void callCreateTeamAPI() {
        //API
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Log.d("API", "Create Team");
        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();


        Call<CreateTeamCallBackWrapper> call = apiInterface.createTeamAPI(batsmanList, allRounderList, bowlerList, keeperList, captain,
                player_amount_count, upComingMatchesDto.key_name, vCaptain, userDto.member_id, upComingMatchesDto.my_team_name);
        call.enqueue(new Callback<CreateTeamCallBackWrapper>() {
            @Override
            public void onResponse(Call<CreateTeamCallBackWrapper> call, Response<CreateTeamCallBackWrapper> response) {
                CreateTeamCallBackWrapper callBackDto = response.body();
                teamIDDto = callBackDto.data;

                Log.e("CreateTeamCallBack", callBackDto.toString());
                if (callBackDto.status) {
                    Log.e("CreateTeamCallBack", callBackDto.message);

                    /* Joining Contest or not*/
                    if (selectedMatchDto.contest_id.trim().equals("")) {
                        gotoHome(callBackDto.message);
                    } else {
//                        if (!userDto.total_balance.equalsIgnoreCase("0.00")) {
//                            callAPIJoinContest(teamIDDto.team_id);
//                        } else if (userDto.total_balance.equalsIgnoreCase("0.00")) {
//                            final Dialog dialog = new Dialog(getActivity());
//                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                            dialog.setCancelable(false);
//                            dialog.setContentView(R.layout.custom_dialog_payment);
//                            dialog.show();
//                            final RadioGroup rg_01 = dialog.findViewById(R.id.rg_01);
//                            rg_01.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//                                @Override
//                                public void onCheckedChanged(RadioGroup group, int checkedId) {
//                                    switch (checkedId) {
//                                        case R.id.rb_paytm:
//                                            paymentGateway = "Paytm";
//                                            break;
//                                        case R.id.rb_razorpay:
//                                            paymentGateway = "Razorpay";
//                                            break;
//                                    }
//                                }
//                            });
//                            Button btn_ok = dialog.findViewById(R.id.btn_ok);
//                            btn_ok.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    if (paymentGateway.equalsIgnoreCase("Paytm")) {
//                                        generateCheckSum(contestDto.entryfee);
//                                    } else {
//                                        startPayment(contestDto.entryfee);
////                                ActivityController.startNextActivity(getActivity(), PaymentRazorPayActivity.class, true);
//                                    }
//                                    dialog.dismiss();
//                                }
//                            });
//                        } else if (Integer.parseInt(userDto.total_balance) < Integer.parseInt(contestDto.entryfee)) {
//                            final String amount = String.valueOf(Integer.parseInt(contestDto.entryfee) - Integer.parseInt(userDto.total_balance));
//                            final Dialog dialog = new Dialog(getActivity());
//                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                            dialog.setCancelable(false);
//                            dialog.setContentView(R.layout.custom_dialog_payment);
//                            dialog.show();
//                            final RadioGroup rg_01 = dialog.findViewById(R.id.rg_01);
//                            rg_01.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//                                @Override
//                                public void onCheckedChanged(RadioGroup group, int checkedId) {
//                                    switch (checkedId) {
//                                        case R.id.rb_paytm:
//                                            paymentGateway = "Paytm";
//                                            break;
//                                        case R.id.rb_razorpay:
//                                            paymentGateway = "Razorpay";
//                                            break;
//                                    }
//                                }
//                            });
//                            Button btn_ok = dialog.findViewById(R.id.btn_ok);
//                            btn_ok.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    if (paymentGateway.equalsIgnoreCase("Paytm")) {
//                                        generateCheckSum(amount);
//                                    } else {
//                                        startPayment(amount);
////                                ActivityController.startNextActivity(getActivity(), PaymentRazorPayActivity.class, true);
//                                    }
//                                    dialog.dismiss();
//                                }
//                            });
//                        }
                        callAPIJoinContest(teamIDDto.team_id);
                    }

                } else {
                    gotoHome(callBackDto.message);
                }
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<CreateTeamCallBackWrapper> call, Throwable t) {
                Log.e("TAG", t.toString());
                if (t instanceof IOException) {
                    DialogUtility.showConnectionErrorDialogWithOk(getActivity());
                    // logging probably not necessary
                } else {
                    Toast.makeText(getActivity(), "Conversion issue! big problems :(", Toast.LENGTH_SHORT).show();
                    // todo log to some central bug tracking service
                }
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }

    /*
     * Join Contest*/
    private void callAPIJoinContest(int team_id) {
        //API
        Log.d("API", "Join Contest");
        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();

        Call<JoinContestCallBackDto> call = apiInterface.joinContest(selectedMatchDto.contest_id, userDto.reference_id, userDto.member_id,
                upComingMatchesDto.key_name, String.valueOf(team_id));
        call.enqueue(new Callback<JoinContestCallBackDto>() {
            @Override
            public void onResponse(Call<JoinContestCallBackDto> call, Response<JoinContestCallBackDto> response) {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();

                final JoinContestCallBackDto callBackDto = response.body();
                Log.e("UpcomingMatchesAPI", callBackDto.toString());
                contestId = callBackDto.contest_id;
                amount = String.valueOf(callBackDto.amount_to_paid);
                if (callBackDto.amount_to_paid == 0) {
                    DialogUtility.showMessageOkWithCallback(callBackDto.msg, getActivity(), new AlertDialogCallBack() {
                        @Override
                        public void onSubmit() {
                            gotoHome(callBackDto.message);
//                            DialogUtility.showMessageWithOk(callBackDto.message, getActivity());
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                } else {
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(true);
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.setContentView(R.layout.custom_dialog_payment);
                    dialog.show();
                    final RadioGroup rg_01 = dialog.findViewById(R.id.rg_01);
                    rg_01.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            switch (checkedId) {
                                case R.id.rb_paytm:
                                    paymentGateway = "Paytm";
                                    break;
                                case R.id.rb_razorpay:
                                    paymentGateway = "Razorpay";
                                    break;
                            }
                        }
                    });
                    Button btn_ok = dialog.findViewById(R.id.btn_ok);
                    btn_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (rg_01.getCheckedRadioButtonId() == -1) {
                                DialogUtility.showMessageWithOk("Please select any one payment gateway", getActivity());
                            } else {
                                if (paymentGateway.equalsIgnoreCase("Paytm")) {
                                    generateCheckSum(String.valueOf(callBackDto.amount_to_paid));
                                } else {
                                    startPayment(String.valueOf(callBackDto.amount_to_paid));
//                                ActivityController.startNextActivity(getActivity(), PaymentRazorPayActivity.class, true);
                                }
                                dialog.dismiss();
                            }
                        }
                    });
                }


            }

            @Override
            public void onFailure(Call<JoinContestCallBackDto> call, Throwable t) {
                Log.e("TAG", t.toString());
                if (t instanceof IOException) {
                    DialogUtility.showConnectionErrorDialogWithOk(getActivity());
                    // logging probably not necessary
                } else {
                    Toast.makeText(getActivity(), "Conversion issue! big problems :(", Toast.LENGTH_SHORT).show();
                    // todo log to some central bug tracking service
                }
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }

    /*
     * Update Team*/
    private void callUpdateTeamAPI() {
        //API
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Log.d("API", "Update Team");
        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();

        Call<CreateTeamCallBackWrapper> call = apiInterface.updateTeamAPI(selectedMatchDto.team_id, batsmanList, allRounderList, bowlerList, keeperList, captain,
                player_amount_count, upComingMatchesDto.key_name, vCaptain, userDto.member_id, upComingMatchesDto.my_team_name);
        call.enqueue(new Callback<CreateTeamCallBackWrapper>() {
            @Override
            public void onResponse(Call<CreateTeamCallBackWrapper> call, Response<CreateTeamCallBackWrapper> response) {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();

                CreateTeamCallBackWrapper callBackDto = response.body();
                teamIDDto = callBackDto.data;
                Log.e("CreateTeamCallBack", callBackDto.toString());

                if (callBackDto.status) {

                    Log.e("CreateTeamCallBack", callBackDto.message);
                    if (selectedMatchDto.contest_id.trim().equals("")) {
                        gotoHome("Successfully Updated.");
                    } else {
//                        if (!userDto.total_balance.equalsIgnoreCase("0.00")) {
//                            callAPIJoinContest(teamIDDto.team_id);
//                        } else if (userDto.total_balance.equalsIgnoreCase("0.00")) {
//                            final Dialog dialog = new Dialog(getActivity());
//                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                            dialog.setCancelable(false);
//                            dialog.setContentView(R.layout.custom_dialog_payment);
//                            dialog.show();
//                            final RadioGroup rg_01 = dialog.findViewById(R.id.rg_01);
//                            rg_01.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//                                @Override
//                                public void onCheckedChanged(RadioGroup group, int checkedId) {
//                                    switch (checkedId) {
//                                        case R.id.rb_paytm:
//                                            paymentGateway = "Paytm";
//                                            break;
//                                        case R.id.rb_razorpay:
//                                            paymentGateway = "Razorpay";
//                                            break;
//                                    }
//                                }
//                            });
//                            Button btn_ok = dialog.findViewById(R.id.btn_ok);
//                            btn_ok.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    if (paymentGateway.equalsIgnoreCase("Paytm")) {
//                                        generateCheckSum(contestDto.entryfee);
//                                    } else {
//                                        startPayment(contestDto.entryfee);
////                                ActivityController.startNextActivity(getActivity(), PaymentRazorPayActivity.class, true);
//                                    }
//                                    dialog.dismiss();
//                                }
//                            });
//                        } else if (Integer.parseInt(userDto.total_balance) < Integer.parseInt(contestDto.entryfee)) {
//                            final String amount = String.valueOf(Integer.parseInt(contestDto.entryfee) - Integer.parseInt(userDto.total_balance));
//                            final Dialog dialog = new Dialog(getActivity());
//                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                            dialog.setCancelable(false);
//                            dialog.setContentView(R.layout.custom_dialog_payment);
//                            dialog.show();
//                            final RadioGroup rg_01 = dialog.findViewById(R.id.rg_01);
//                            rg_01.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//                                @Override
//                                public void onCheckedChanged(RadioGroup group, int checkedId) {
//                                    switch (checkedId) {
//                                        case R.id.rb_paytm:
//                                            paymentGateway = "Paytm";
//                                            break;
//                                        case R.id.rb_razorpay:
//                                            paymentGateway = "Razorpay";
//                                            break;
//                                    }
//                                }
//                            });
//                            Button btn_ok = dialog.findViewById(R.id.btn_ok);
//                            btn_ok.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    if (paymentGateway.equalsIgnoreCase("Paytm")) {
//                                        generateCheckSum(amount);
//                                    } else {
//                                        startPayment(amount);
////                                ActivityController.startNextActivity(getActivity(), PaymentRazorPayActivity.class, true);
//                                    }
//                                    dialog.dismiss();
//                                }
//                            });
//                        }
                        callAPIJoinContest(teamIDDto.team_id);
                    }

                } else {
                    DialogUtility.showMessageWithOk(callBackDto.message, getActivity());
                }
            }

            @Override
            public void onFailure(Call<CreateTeamCallBackWrapper> call, Throwable t) {
                Log.e("TAG", t.toString());
                if (t instanceof IOException) {
                    DialogUtility.showConnectionErrorDialogWithOk(getActivity());
                    // logging probably not necessary
                } else {
                    Toast.makeText(getActivity(), "Conversion issue! big problems :(", Toast.LENGTH_SHORT).show();
                    // todo log to some central bug tracking service
                }
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }

    private void gotoHome(String message) {
        DialogUtility.showMessageOkWithCallback(message, getActivity(), new AlertDialogCallBack() {
            @Override
            public void onSubmit() {
                ((BaseHeaderActivity) getActivity()).removeAllFragment();
                ((BaseHeaderActivity) getActivity()).replaceFragment(HomeFragment.newInstance(), false, HomeFragment.class.getName());
            }

            @Override
            public void onCancel() {

            }
        });
    }

    private void startPayment(String amount) {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Stake For Win");
            options.put("description", "Create Contest");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://rzp-mobile.s3.amazonaws.com/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", Float.valueOf(amount) * 100);
            options.put("theme", new JSONObject("{color: '#E93D29'}"));

           /* JSONObject preFill = new JSONObject();
            preFill.put("email", "test@razorpay.com");
            preFill.put("contact", "9876543210");

            options.put("prefill", preFill);*/

            co.open(getActivity(), options);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    private void generateCheckSum(String amount) {
        //getting the tax amount first.
        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        //creating the retrofit api service
        apiInterface = ApiClient4.getApiClient().create(ApiInterface.class);

        //creating paytm object
        //containing all the values required
        final Paytm paytm = new Paytm(Constants.M_ID, Constants.CHANNEL_ID, amount, Constants.WEBSITE, Constants.CALLBACK_URL, Constants.INDUSTRY_TYPE_ID);

        //creating a call object from the apiService
        Call<Checksum> call = apiInterface.getChecksum(paytm.getmId(), paytm.getOrderId(), paytm.getCustId(), paytm.getChannelId(), paytm.getTxnAmount(),
                paytm.getWebsite(), paytm.getCallBackUrl(), paytm.getIndustryTypeId());

        //making the call to generate checksum
        call.enqueue(new Callback<Checksum>() {
            @Override
            public void onResponse(Call<Checksum> call, Response<Checksum> response) {

                //once we get the checksum we will initiailize the payment.
                //the method is taking the checksum we got and the paytm object as the parameter
                initializePaytmPayment(response.body().getChecksumHash(), paytm);
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Checksum> call, Throwable t) {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();

            }
        });
    }

    private void initializePaytmPayment(String checksumHash, Paytm paytm) {

        //getting paytm service
        //PaytmPGService Service = PaytmPGService.getStagingService();

        //use this when using for production
        PaytmPGService Service = PaytmPGService.getProductionService();

        //creating a hashmap and adding all the values required
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("MID", Constants.M_ID);
        paramMap.put("ORDER_ID", paytm.getOrderId());
        paramMap.put("CUST_ID", paytm.getCustId());
        paramMap.put("CHANNEL_ID", paytm.getChannelId());
        paramMap.put("TXN_AMOUNT", paytm.getTxnAmount());
        paramMap.put("WEBSITE", paytm.getWebsite());
        paramMap.put("CALLBACK_URL", paytm.getCallBackUrl());
        paramMap.put("CHECKSUMHASH", checksumHash);
        paramMap.put("INDUSTRY_TYPE_ID", paytm.getIndustryTypeId());


        //creating a paytm order object using the hashmap
        PaytmOrder order = new PaytmOrder(paramMap);

        //intializing the paytm service
        Service.initialize(order, null);

        //finally starting the payment transaction
        Service.startPaymentTransaction(getActivity(), true, true, this);

    }

    //all these overriden method is to detect the payment result accordingly
    @Override
    public void onTransactionResponse(Bundle bundle) {
        String orderId = bundle.getString("ORDERID");
        callTransactionAPI(orderId);
//        System.out.println("transactionId " + transactionId + " bankTransactionId " + bankTransactionId);
//        Toast.makeText(getActivity(), bundle.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void networkNotAvailable() {
        Toast.makeText(getActivity(), "Network error", Toast.LENGTH_LONG).show();
    }

    @Override
    public void clientAuthenticationFailed(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void someUIErrorOccurred(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onErrorLoadingWebPage(int i, String s, String s1) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressedCancelTransaction() {
        Toast.makeText(getActivity(), "Back Pressed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTransactionCancel(String s, Bundle bundle) {
        String transactionId = bundle.getString("TXNID");
        callAPITransactionJoinContest(transactionId, amount, "paytm", "failure");
        Toast.makeText(getActivity(), s + bundle.toString(), Toast.LENGTH_LONG).show();
    }

    /**
     * The name of the function has to be onPaymentSuccess
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            callAPITransactionJoinContest(razorpayPaymentID, amount, "razorpay", "success");
            Toast.makeText(getActivity(), "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }

    /**
     * The name of the function has to be onPaymentError
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(getActivity(), "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
        }
    }

    private void callAPITransactionJoinContest(String paymentId, String amount, String type, String status) {
        //API
        Log.d("API", "Join Contest");
        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<TransactionWrapper> call = apiInterface.paymentForJoinContest(contestId, userDto.member_id, paymentId, type, status, amount);
        call.enqueue(new Callback<TransactionWrapper>() {
            @Override
            public void onResponse(Call<TransactionWrapper> call, Response<TransactionWrapper> response) {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();

                final TransactionWrapper callBackDto = response.body();
                Log.e("UpcomingMatchesAPI", callBackDto.toString());
                DialogUtility.showMessageOkWithCallback(callBackDto.message, getActivity(), new AlertDialogCallBack() {
                    @Override
                    public void onSubmit() {
                        DialogUtility.showMessageWithOk(callBackDto.message, getActivity());
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }

            @Override
            public void onFailure(Call<TransactionWrapper> call, Throwable t) {
                Log.e("TAG", t.toString());
                if (t instanceof IOException) {
                    DialogUtility.showConnectionErrorDialogWithOk(getActivity());
                    // logging probably not necessary
                } else {
                    Toast.makeText(getActivity(), "Conversion issue! big problems :(", Toast.LENGTH_SHORT).show();
                    // todo log to some central bug tracking service
                }
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }

    private void callTransactionAPI(String orderId) {
        Log.d("API", "Get Player");
        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
        apiInterface = ApiClient4.getApiClient().create(ApiInterface.class);

        Call<Transaction> call = apiInterface.getStatus(orderId);
        call.enqueue(new Callback<Transaction>() {
            @Override
            public void onResponse(Call<Transaction> call, final Response<Transaction> response) {
                if (response.body().sTATUS.equalsIgnoreCase("TXN_SUCCESS")) {
                    DialogUtility.showMessageOkWithCallback("Payment Successful", getActivity(), new AlertDialogCallBack() {
                        @Override
                        public void onSubmit() {
                            callAPITransactionJoinContest(response.body().tXNID, response.body().tXNAMOUNT, "paytm", "success");
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                } else {
                    DialogUtility.showMessageWithOk("Payment Failure", getActivity());
                }

                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Transaction> call, Throwable t) {
                Log.e("TAG", t.toString());
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }
}
