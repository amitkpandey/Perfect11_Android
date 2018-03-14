package com.perfect11.team_create;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.perfect11.R;
import com.perfect11.base.BaseFragment;
import com.perfect11.contest.dto.TeamDto;
import com.perfect11.contest.dto.TeamPlayerDto;
import com.perfect11.home.service.BackgroundScoreUpdateService;
import com.perfect11.login_signup.dto.UserDto;
import com.squareup.picasso.Picasso;
import com.utility.PreferenceUtility;
import com.utility.customView.CustomTextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class MyTeamFragment extends BaseFragment {
    private String matchId, team_id;
    private Intent intent;
    private TextView tv_wkt_point, tv_bat1_point, tv_bat2_point, tv_bat3_point, tv_bat4_point, tv_bat5_point, tv_bat6_point, tv_ar1_point, tv_ar2_point,
            tv_ar3_point, tv_ar4_point, tv_bowler1_point, tv_bowler2_point, tv_bowler3_point, tv_bowler4_point, tv_bowler5_point, tv_bowler6_point;

    private CustomTextView tv_team1, tv_team2, tv_team_count1, tv_team_count2;
    private ImageView iv_wkt, iv_bat1, iv_bat2, iv_bat3, iv_bat4, iv_bat5, iv_bat6, iv_ar1, iv_ar2, iv_ar3, iv_ar4, iv_bowler1,
            iv_bowler2, iv_bowler3, iv_bowler4, iv_bowler5, iv_bowler6;

    private CircleImageView iv_team1, iv_team2;

    private int bowler, batsman, allrounder, keeper;

    private ArrayList<String> batsmanList;
    private ArrayList<String> allRounderList;
    private ArrayList<String> bowlerList;
    private ArrayList<String> keeperList;
    private String captain = "", vCaptain = "", team1, team2, teamA, teamB;
    private float player_amount_count = 0;
    private TeamDto teamDto;

    public static MyTeamFragment newInstance() {
        return new MyTeamFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.my_team, container, false);
        readFromBundle();
        initView();
        setInnerHeader("My Team");
        return view;
    }

    private void startBackgroundService() {
        intent = new Intent();
        intent.putExtra("matchId", matchId);
        intent.putExtra("teamId", team_id);
        intent.setClass(getActivity(), BackgroundScoreUpdateService.class);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateUI(intent);
        }
    };

    private void updateUI(Intent intent) {
        teamDto = (TeamDto) intent.getExtras().getSerializable("teamDto");
        System.out.println("intent Background Service " + teamDto.toString());
        bowler = 0;
        batsman = 0;
        allrounder = 0;
        keeper = 0;
        batsmanList = new ArrayList<>();
        allRounderList = new ArrayList<>();
        bowlerList = new ArrayList<>();
        keeperList = new ArrayList<>();
        setTeam();
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().startService(intent);
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(BackgroundScoreUpdateService.BROADCAST_ACTION));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(broadcastReceiver);
        getActivity().stopService(intent);
    }

    private void initView() {
        startBackgroundService();
        tv_team1 = view.findViewById(R.id.tv_team1);
        tv_team2 = view.findViewById(R.id.tv_team2);
        tv_team_count1 = view.findViewById(R.id.tv_team_count1);
        tv_team_count2 = view.findViewById(R.id.tv_team_count2);

        iv_team1 = view.findViewById(R.id.iv_team1);
        iv_team2 = view.findViewById(R.id.iv_team2);

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

        tv_wkt_point = view.findViewById(R.id.tv_wkt_point);

        tv_bat1_point = view.findViewById(R.id.tv_bat1_point);
        tv_bat2_point = view.findViewById(R.id.tv_bat2_point);
        tv_bat3_point = view.findViewById(R.id.tv_bat3_point);
        tv_bat4_point = view.findViewById(R.id.tv_bat4_point);
        tv_bat5_point = view.findViewById(R.id.tv_bat5_point);
        tv_bat6_point = view.findViewById(R.id.tv_bat6_point);

        tv_ar1_point = view.findViewById(R.id.tv_ar1_point);
        tv_ar2_point = view.findViewById(R.id.tv_ar2_point);
        tv_ar3_point = view.findViewById(R.id.tv_ar3_point);
        tv_ar4_point = view.findViewById(R.id.tv_ar4_point);

        tv_bowler1_point = view.findViewById(R.id.tv_bowler1_point);
        tv_bowler2_point = view.findViewById(R.id.tv_bowler2_point);
        tv_bowler3_point = view.findViewById(R.id.tv_bowler3_point);
        tv_bowler4_point = view.findViewById(R.id.tv_bowler4_point);
        tv_bowler5_point = view.findViewById(R.id.tv_bowler5_point);
        tv_bowler6_point = view.findViewById(R.id.tv_bowler6_point);
        setPlayerVisibilityGone();
    }

    private void readFromBundle() {
        try {
            matchId = getArguments().getString("matchId");
            team_id = getArguments().getString("teamId");
            team1 = getArguments().getString("team1");
            team2 = getArguments().getString("team2");
            teamA = getArguments().getString("teamA");
            teamB = getArguments().getString("teamB");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().stopService(intent);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().stopService(intent);
    }

    private void setPlayerVisibilityGone() {
        tv_wkt_point.setVisibility(View.GONE);
        iv_wkt.setVisibility(View.GONE);

        iv_bat1.setVisibility(View.GONE);
        iv_bat2.setVisibility(View.GONE);
        iv_bat3.setVisibility(View.GONE);
        iv_bat4.setVisibility(View.GONE);
        iv_bat5.setVisibility(View.GONE);
        iv_bat6.setVisibility(View.GONE);

        iv_ar1.setVisibility(View.GONE);
        iv_ar2.setVisibility(View.GONE);
        iv_ar3.setVisibility(View.GONE);
        iv_ar4.setVisibility(View.GONE);

        iv_bowler1.setVisibility(View.GONE);
        iv_bowler2.setVisibility(View.GONE);
        iv_bowler3.setVisibility(View.GONE);
        iv_bowler4.setVisibility(View.GONE);
        iv_bowler5.setVisibility(View.GONE);
        iv_bowler6.setVisibility(View.GONE);

        tv_bat1_point.setVisibility(View.GONE);
        tv_bat2_point.setVisibility(View.GONE);
        tv_bat3_point.setVisibility(View.GONE);
        tv_bat4_point.setVisibility(View.GONE);
        tv_bat5_point.setVisibility(View.GONE);
        tv_bat6_point.setVisibility(View.GONE);

        tv_ar1_point.setVisibility(View.GONE);
        tv_ar2_point.setVisibility(View.GONE);
        tv_ar3_point.setVisibility(View.GONE);
        tv_ar4_point.setVisibility(View.GONE);

        tv_bowler1_point.setVisibility(View.GONE);
        tv_bowler2_point.setVisibility(View.GONE);
        tv_bowler3_point.setVisibility(View.GONE);
        tv_bowler4_point.setVisibility(View.GONE);
        tv_bowler5_point.setVisibility(View.GONE);
        tv_bowler6_point.setVisibility(View.GONE);
    }

    private void setTeam() {
        int total_team1 = 0;
        int total_team2 = 0;

        System.out.println("team1" + team1);
        System.out.println("team2" + team2);
        for (TeamPlayerDto teamPlayerDto : teamDto.team_player) {
            if (teamPlayerDto.team_code.trim().equals(team1)) {
                total_team1++;
            } else {
                total_team2++;
            }
        }
        tv_team1.setText(team1);
        tv_team2.setText(team2);
        Picasso.with(getActivity()).load(getPictureURL(teamA)).placeholder(R.drawable.progress_animation).error(R.drawable.no_team).
                into(iv_team1);
        Picasso.with(getActivity()).load(getPictureURL(teamB)).placeholder(R.drawable.progress_animation).error(R.drawable.no_team).
                into(iv_team2);
        tv_team_count1.setText("" + total_team1 + "/7");
        tv_team_count2.setText("" + total_team2 + "/7");
        arrangePlayerOnField();
    }

    private void arrangePlayerOnField() {
        for (TeamPlayerDto teamPlayerDto : teamDto.team_player) {
            /* Divided  players*/
            switch (teamPlayerDto.type) {
                case "bowler":
                    bowler++;
                    setVisibleBowler(bowler, teamPlayerDto);
                    bowlerList.add(teamPlayerDto.full_name);
                    break;
                case "batsman":
                    batsman++;
                    setVisibleBatsman(batsman, teamPlayerDto);
                    batsmanList.add(teamPlayerDto.full_name);
                    break;
                case "allrounder":
                    allrounder++;
                    setVisibleAllRounder(allrounder, teamPlayerDto);
                    allRounderList.add(teamPlayerDto.full_name);
                    break;
                case "keeper":
//                    iv_wkt.setVisibility(View.VISIBLE);
                    setImageWK(iv_wkt, tv_wkt_point, teamPlayerDto);
                    keeperList.add(teamPlayerDto.full_name);
                    break;
            }

//            player_amount_count = player_amount_count + Float.parseFloat(playerDto.credit);
//
//            if (playerDto.isC) {
//                captain = playerDto.short_name;
//            }
//            if (playerDto.isCV) {
//                vCaptain = playerDto.short_name;
//            }
        }
    }

    private void setVisibleAllRounder(int allRounder, TeamPlayerDto teamPlayerDto) {

        switch (allRounder) {
            case 1:
                setImageAllRounder(iv_ar1, tv_ar1_point, teamPlayerDto);
                break;
            case 2:
                setImageAllRounder(iv_ar2, tv_ar2_point, teamPlayerDto);
                break;
            case 3:
                setImageAllRounder(iv_ar3, tv_ar3_point, teamPlayerDto);
                break;
            case 4:
                setImageAllRounder(iv_ar4, tv_ar4_point, teamPlayerDto);
                break;
        }
    }

    /**
     * Visible Batsman
     */
    private void setVisibleBatsman(int batsman, TeamPlayerDto teamPlayerDto) {
        switch (batsman) {
            case 1:
                setImageBatsman(iv_bat1, tv_bat1_point, teamPlayerDto);
                break;
            case 2:
                setImageBatsman(iv_bat2, tv_bat2_point, teamPlayerDto);
                break;
            case 3:
                setImageBatsman(iv_bat3, tv_bat3_point, teamPlayerDto);
                break;
            case 4:
                setImageBatsman(iv_bat4, tv_bat4_point, teamPlayerDto);
                break;
            case 5:
                setImageBatsman(iv_bat5, tv_bat5_point, teamPlayerDto);
                break;
            case 6:
                setImageBatsman(iv_bat6, tv_bat6_point, teamPlayerDto);
                break;
        }
    }

    /**
     * Visible Bowler
     */
    private void setVisibleBowler(int bowler, TeamPlayerDto teamPlayerDto) {
        switch (bowler) {
            case 1:
                setImageBowler(iv_bowler1, tv_bowler1_point, teamPlayerDto);
                break;
            case 2:
                setImageBowler(iv_bowler2, tv_bowler2_point, teamPlayerDto);
                break;
            case 3:
                setImageBowler(iv_bowler3, tv_bowler3_point, teamPlayerDto);
                break;
            case 4:
                setImageBowler(iv_bowler4, tv_bowler4_point, teamPlayerDto);
                break;
            case 5:
                setImageBowler(iv_bowler5, tv_bowler5_point, teamPlayerDto);
                break;
            case 6:
                setImageBowler(iv_bowler6, tv_bowler6_point, teamPlayerDto);
                break;
        }
    }


    private void setImageWK(ImageView iv_bowler1, TextView tv_wicketKeeper, TeamPlayerDto teamPlayerDto) {
        if (teamPlayerDto.player.equalsIgnoreCase(teamDto.captain)) {
            iv_bowler1.setImageDrawable(getResources().getDrawable(R.drawable.w1));
        } else if (teamPlayerDto.player.equalsIgnoreCase(teamDto.vice_captain)) {
            iv_bowler1.setImageDrawable(getResources().getDrawable(R.drawable.w2));
        }
        iv_bowler1.setVisibility(View.VISIBLE);
        tv_wicketKeeper.setVisibility(View.VISIBLE);
        tv_wicketKeeper.setText(teamPlayerDto.points_gain);
    }

    private void setImageAllRounder(ImageView iv_bowler1, TextView tv_allRounder, TeamPlayerDto teamPlayerDto) {
        if (teamPlayerDto.player.equalsIgnoreCase(teamDto.captain)) {
            iv_bowler1.setImageDrawable(getResources().getDrawable(R.drawable.ar1));
        } else if (teamPlayerDto.player.equalsIgnoreCase(teamDto.vice_captain)) {
            iv_bowler1.setImageDrawable(getResources().getDrawable(R.drawable.ar2));
        }
        iv_bowler1.setVisibility(View.VISIBLE);
        tv_allRounder.setVisibility(View.VISIBLE);
        tv_allRounder.setText(teamPlayerDto.points_gain);
    }

    private void setImageBowler(ImageView iv_bowler1, TextView tv_bowler, TeamPlayerDto teamPlayerDto) {
        if (teamPlayerDto.player.equalsIgnoreCase(teamDto.captain)) {
            iv_bowler1.setImageDrawable(getResources().getDrawable(R.drawable.b1));
        } else if (teamPlayerDto.player.equalsIgnoreCase(teamDto.vice_captain)) {
            iv_bowler1.setImageDrawable(getResources().getDrawable(R.drawable.b2));
        }
        iv_bowler1.setVisibility(View.VISIBLE);
        tv_bowler.setVisibility(View.VISIBLE);
        tv_bowler.setText(teamPlayerDto.points_gain);
    }

    private void setImageBatsman(ImageView iv_bowler1, TextView tv_batsman, TeamPlayerDto teamPlayerDto) {
        if (teamPlayerDto.player.equalsIgnoreCase(teamDto.captain)) {
            iv_bowler1.setImageDrawable(getResources().getDrawable(R.drawable.bat1));
        } else if (teamPlayerDto.player.equalsIgnoreCase(teamDto.vice_captain)) {
            iv_bowler1.setImageDrawable(getResources().getDrawable(R.drawable.bat2));
        }
        iv_bowler1.setVisibility(View.VISIBLE);
        tv_batsman.setVisibility(View.VISIBLE);
        tv_batsman.setText(teamPlayerDto.points_gain);
    }

    private String getPictureURL(String teama) {
        String country = teama.trim().replace(" ", "-");
        return "http://52.15.50.179/public/images/team/flag-of-" + country + ".png";
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
