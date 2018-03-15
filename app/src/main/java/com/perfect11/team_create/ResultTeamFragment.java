package com.perfect11.team_create;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.perfect11.R;
import com.perfect11.base.ApiClient;
import com.perfect11.base.ApiInterface;
import com.perfect11.base.BaseFragment;
import com.perfect11.contest.dto.TeamDto;
import com.perfect11.contest.dto.TeamPlayerDto;
import com.perfect11.contest.wrapper.TeamWrapper;
import com.perfect11.login_signup.dto.UserDto;
import com.squareup.picasso.Picasso;
import com.utility.PreferenceUtility;
import com.utility.customView.CustomTextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ResultTeamFragment extends BaseFragment {
    private String matchId, team_id;
    private TextView tv_wkt_point, tv_bat1_point, tv_bat2_point, tv_bat3_point, tv_bat4_point, tv_bat5_point, tv_bat6_point, tv_ar1_point, tv_ar2_point,
            tv_ar3_point, tv_ar4_point, tv_bowler1_point, tv_bowler2_point, tv_bowler3_point, tv_bowler4_point, tv_bowler5_point, tv_bowler6_point,
            tv_wkt_name, tv_bat1_name, tv_bat2_name, tv_bat3_name, tv_bat4_name, tv_bat5_name, tv_bat6_name, tv_ar1_name, tv_ar2_name, tv_ar3_name,
            tv_ar4_name, tv_bowler1_name, tv_bowler2_name, tv_bowler3_name, tv_bowler4_name, tv_bowler5_name, tv_bowler6_name;

    private CustomTextView tv_team1, tv_team2, tv_team_count1, tv_team_count2;
    private ImageView iv_wkt, iv_bat1, iv_bat2, iv_bat3, iv_bat4, iv_bat5, iv_bat6, iv_ar1, iv_ar2, iv_ar3, iv_ar4, iv_bowler1,
            iv_bowler2, iv_bowler3, iv_bowler4, iv_bowler5, iv_bowler6;

    private CircleImageView iv_team1, iv_team2;

    private int bowler = 0, batsman = 0, allrounder = 0, keeper = 0;

    private ArrayList<String> batsmanList = new ArrayList<>();
    private ArrayList<String> allRounderList = new ArrayList<>();
    private ArrayList<String> bowlerList = new ArrayList<>();
    private ArrayList<String> keeperList = new ArrayList<>();
    private String captain = "", vCaptain = "", team1, team2, teamA, teamB, reference_id;
    private float player_amount_count = 0;
    private TeamDto teamDto;
    private UserDto userDto;
    private RelativeLayout rl_bat1, rl_bat2, rl_bat3, rl_bat4, rl_bat5, rl_bat6, rl_ar1, rl_ar2, rl_ar3, rl_ar4, rl_bowler1, rl_bowler2, rl_bowler3,
            rl_bowler4, rl_bowler5, rl_bowler6;

    public static ResultTeamFragment newInstance() {
        return new ResultTeamFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.my_team, container, false);
        userDto = (UserDto) PreferenceUtility.getObjectInAppPreference(getActivity(), PreferenceUtility.APP_PREFERENCE_NAME);
        readFromBundle();
        initView();
        if (userDto.reference_id.equalsIgnoreCase(reference_id)) {
            setInnerHeader("My Team");
        } else
            setInnerHeader(reference_id + " Team");
        callApi();
        return view;
    }

    private void callApi() {
        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<TeamWrapper> call = apiInterface.getPlayerLiveScore(team_id, matchId);
        call.enqueue(new Callback<TeamWrapper>() {
            @Override
            public void onResponse(Call<TeamWrapper> call, Response<TeamWrapper> response) {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                TeamWrapper teamWrapper = response.body();
                if (teamWrapper != null && teamWrapper.data != null && teamWrapper.data.size() > 0) {
                    teamDto = teamWrapper.data.get(0);
                    setTeam();
                }
            }

            @Override
            public void onFailure(Call<TeamWrapper> call, Throwable t) {
                Log.e("TAG", t.toString());
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }

    private void initView() {
        tv_team1 = view.findViewById(R.id.tv_team1);
        tv_team2 = view.findViewById(R.id.tv_team2);
        tv_team_count1 = view.findViewById(R.id.tv_team_count1);
        tv_team_count2 = view.findViewById(R.id.tv_team_count2);

        iv_team1 = view.findViewById(R.id.iv_team1);
        iv_team2 = view.findViewById(R.id.iv_team2);

        iv_wkt = view.findViewById(R.id.iv_wkt);
        tv_wkt_name = view.findViewById(R.id.tv_wkt_name);

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
            reference_id = getArguments().getString("reference_id");
            team_id = getArguments().getString("teamId");
            team1 = getArguments().getString("team1");
            team2 = getArguments().getString("team2");
            teamA = getArguments().getString("teamA");
            teamB = getArguments().getString("teamB");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setPlayerVisibilityGone() {
        tv_wkt_point.setVisibility(View.GONE);
        iv_wkt.setVisibility(View.GONE);

        tv_wkt_name.setVisibility(View.GONE);

        rl_bat1.setVisibility(View.GONE);
        rl_bat2.setVisibility(View.GONE);
        rl_bat3.setVisibility(View.GONE);
        rl_bat4.setVisibility(View.GONE);
        rl_bat5.setVisibility(View.GONE);
        rl_bat6.setVisibility(View.GONE);

        rl_ar1.setVisibility(View.INVISIBLE);
        rl_ar2.setVisibility(View.INVISIBLE);
        rl_ar3.setVisibility(View.INVISIBLE);
        rl_ar4.setVisibility(View.INVISIBLE);

        rl_bowler1.setVisibility(View.GONE);
        rl_bowler2.setVisibility(View.GONE);
        rl_bowler3.setVisibility(View.GONE);
        rl_bowler4.setVisibility(View.GONE);
        rl_bowler5.setVisibility(View.GONE);
        rl_bowler6.setVisibility(View.GONE);
    }

    private void setTeam() {
        int total_team1 = 0;
        int total_team2 = 0;

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
                    setImageWK(iv_wkt, tv_wkt_point, tv_wkt_name, teamPlayerDto);
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
                rl_ar1.setVisibility(View.VISIBLE);
                setImageAllRounder(iv_ar1, tv_ar1_point, tv_ar1_name, teamPlayerDto);
                break;
            case 2:
                rl_ar2.setVisibility(View.VISIBLE);
                setImageAllRounder(iv_ar2, tv_ar2_point, tv_ar2_name, teamPlayerDto);
                break;
            case 3:
                rl_ar3.setVisibility(View.VISIBLE);
                setImageAllRounder(iv_ar3, tv_ar3_point, tv_ar3_name, teamPlayerDto);
                break;
            case 4:
                rl_ar4.setVisibility(View.VISIBLE);
                setImageAllRounder(iv_ar4, tv_ar4_point, tv_ar4_name, teamPlayerDto);
                break;
        }
    }

    /**
     * Visible Batsman
     */
    private void setVisibleBatsman(int batsman, TeamPlayerDto teamPlayerDto) {
        switch (batsman) {
            case 1:
                rl_bat1.setVisibility(View.VISIBLE);
                setImageBatsman(iv_bat1, tv_bat1_point, tv_bat1_name, teamPlayerDto);
                break;
            case 2:
                rl_bat2.setVisibility(View.VISIBLE);
                setImageBatsman(iv_bat2, tv_bat2_point, tv_bat2_name, teamPlayerDto);
                break;
            case 3:
                rl_bat3.setVisibility(View.VISIBLE);
                setImageBatsman(iv_bat3, tv_bat3_point, tv_bat3_name, teamPlayerDto);
                break;
            case 4:
                rl_bat4.setVisibility(View.VISIBLE);
                setImageBatsman(iv_bat4, tv_bat4_point, tv_bat4_name, teamPlayerDto);
                break;
            case 5:
                rl_bat5.setVisibility(View.VISIBLE);
                setImageBatsman(iv_bat5, tv_bat5_point, tv_bat5_name, teamPlayerDto);
                break;
            case 6:
                rl_bat6.setVisibility(View.VISIBLE);
                setImageBatsman(iv_bat6, tv_bat6_point, tv_bat6_name, teamPlayerDto);
                break;
        }
    }

    /**
     * Visible Bowler
     */
    private void setVisibleBowler(int bowler, TeamPlayerDto teamPlayerDto) {
        switch (bowler) {
            case 1:
                rl_bowler1.setVisibility(View.VISIBLE);
                setImageBowler(iv_bowler1, tv_bowler1_point, tv_bowler1_name, teamPlayerDto);
                break;
            case 2:
                rl_bowler2.setVisibility(View.VISIBLE);
                setImageBowler(iv_bowler2, tv_bowler2_point, tv_bowler2_name, teamPlayerDto);
                break;
            case 3:
                rl_bowler3.setVisibility(View.VISIBLE);
                setImageBowler(iv_bowler3, tv_bowler3_point, tv_bowler3_name, teamPlayerDto);
                break;
            case 4:
                rl_bowler4.setVisibility(View.VISIBLE);
                setImageBowler(iv_bowler4, tv_bowler4_point, tv_bowler4_name, teamPlayerDto);
                break;
            case 5:
                rl_bowler5.setVisibility(View.VISIBLE);
                setImageBowler(iv_bowler5, tv_bowler5_point, tv_bowler5_name, teamPlayerDto);
                break;
            case 6:
                rl_bowler6.setVisibility(View.VISIBLE);
                setImageBowler(iv_bowler6, tv_bowler6_point, tv_bowler6_name, teamPlayerDto);
                break;
        }
    }


    private void setImageWK(ImageView iv_bowler1, TextView tv_wicketKeeperPoint, TextView tv_wicketKeeperName, TeamPlayerDto teamPlayerDto) {
        if (teamPlayerDto.player.equalsIgnoreCase(teamDto.captain)) {
            iv_bowler1.setImageDrawable(getResources().getDrawable(R.drawable.w1));
        } else if (teamPlayerDto.player.equalsIgnoreCase(teamDto.vice_captain)) {
            iv_bowler1.setImageDrawable(getResources().getDrawable(R.drawable.w2));
        }
        iv_bowler1.setVisibility(View.VISIBLE);
        tv_wicketKeeperPoint.setVisibility(View.VISIBLE);
        tv_wicketKeeperName.setVisibility(View.VISIBLE);
        tv_wicketKeeperPoint.setText(teamPlayerDto.points_gain);
        tv_wicketKeeperName.setText(teamPlayerDto.full_name);
    }

    private void setImageAllRounder(ImageView iv_bowler1, TextView tv_allRounderPoint, TextView tv_allRounderName, TeamPlayerDto teamPlayerDto) {
        if (teamPlayerDto.player.equalsIgnoreCase(teamDto.captain)) {
            iv_bowler1.setImageDrawable(getResources().getDrawable(R.drawable.ar1));
        } else if (teamPlayerDto.player.equalsIgnoreCase(teamDto.vice_captain)) {
            iv_bowler1.setImageDrawable(getResources().getDrawable(R.drawable.ar2));
        }
        iv_bowler1.setVisibility(View.VISIBLE);
        tv_allRounderPoint.setText(teamPlayerDto.points_gain);
        tv_allRounderName.setText(teamPlayerDto.full_name);
    }

    private void setImageBowler(ImageView iv_bowler1, TextView tv_bowlerPoint, TextView tv_bowlerName, TeamPlayerDto teamPlayerDto) {
        if (teamPlayerDto.player.equalsIgnoreCase(teamDto.captain)) {
            iv_bowler1.setImageDrawable(getResources().getDrawable(R.drawable.b1));
        } else if (teamPlayerDto.player.equalsIgnoreCase(teamDto.vice_captain)) {
            iv_bowler1.setImageDrawable(getResources().getDrawable(R.drawable.b2));
        }
        iv_bowler1.setVisibility(View.VISIBLE);
        tv_bowlerPoint.setText(teamPlayerDto.points_gain);
        tv_bowlerName.setText(teamPlayerDto.full_name);
    }

    private void setImageBatsman(ImageView iv_bowler1, TextView tv_batsmanPoint, TextView tv_batsmanName, TeamPlayerDto teamPlayerDto) {
        if (teamPlayerDto.player.equalsIgnoreCase(teamDto.captain)) {
            iv_bowler1.setImageDrawable(getResources().getDrawable(R.drawable.bat1));
        } else if (teamPlayerDto.player.equalsIgnoreCase(teamDto.vice_captain)) {
            iv_bowler1.setImageDrawable(getResources().getDrawable(R.drawable.bat2));
        }
        iv_bowler1.setVisibility(View.VISIBLE);
        tv_batsmanPoint.setText(teamPlayerDto.points_gain);
        tv_batsmanName.setText(teamPlayerDto.full_name);
    }

    private String getPictureURL(String teamA) {
        String country = teamA.trim().replace(" ", "-");
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
