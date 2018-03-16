package com.perfect11.team_create;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.perfect11.R;
import com.perfect11.base.BaseFragment;
import com.perfect11.contest.dto.TeamDto;
import com.perfect11.contest.dto.TeamPlayerDto;
import com.squareup.picasso.Picasso;
import com.utility.customView.CustomButton;
import com.utility.customView.CustomTextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class PreviewTeamFragment extends BaseFragment {
    private CustomTextView tv_team1, tv_team2, tv_team_count1, tv_team_count2, tv_wkt_name, tv_bat1_name, tv_bat2_name, tv_bat3_name, tv_bat4_name,
            tv_bat5_name, tv_bat6_name, tv_ar1_name, tv_ar2_name, tv_ar3_name, tv_ar4_name, tv_bowler1_name, tv_bowler2_name, tv_bowler3_name,
            tv_bowler4_name, tv_bowler5_name, tv_bowler6_name;

    private ImageView iv_wkt, iv_bat1, iv_bat2, iv_bat3, iv_bat4, iv_bat5, iv_bat6, iv_ar1, iv_ar2, iv_ar3, iv_ar4, iv_bowler1,
            iv_bowler2, iv_bowler3, iv_bowler4, iv_bowler5, iv_bowler6;

    private CircleImageView iv_team1, iv_team2;

    private int bowler = 0, batsman = 0, allrounder = 0, keeper = 0;

    private ArrayList<String> batsmanList = new ArrayList<>();
    private ArrayList<String> allRounderList = new ArrayList<>();
    private ArrayList<String> bowlerList = new ArrayList<>();
    private ArrayList<String> keeperList = new ArrayList<>();
    private String captain = "", vCaptain = "", team1, team2, teamA, teamB;
    private float player_amount_count = 0;
    private TeamDto teamDto;
    private RelativeLayout rl_bat1, rl_bat2, rl_bat3, rl_bat4, rl_bat5, rl_bat6, rl_ar1, rl_ar2, rl_ar3, rl_ar4, rl_bowler1, rl_bowler2, rl_bowler3,
            rl_bowler4, rl_bowler5, rl_bowler6;

    public static PreviewTeamFragment newInstance() {
        return new PreviewTeamFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.preview_team, container, false);
        setInnerHeader("Preview Team");
        readFromBundle();
        initView();
        return view;
    }

    private void readFromBundle() {
        team1 = getArguments().getString("team1");
        team2 = getArguments().getString("team2");
        teamA = getArguments().getString("teamA");
        teamB = getArguments().getString("teamB");
        teamDto = (TeamDto) getArguments().getSerializable("teamDto");
        System.out.println("teamDto " + teamDto.team_player.size());
    }

    private void initView() {
        tv_team1 = view.findViewById(R.id.tv_team1);
        tv_team2 = view.findViewById(R.id.tv_team2);
        iv_team1 = view.findViewById(R.id.iv_team1);
        iv_team2 = view.findViewById(R.id.iv_team2);
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

        setPlayerVisibilityGone();
        setTeam();
    }

    private void setPlayerVisibilityGone() {
        iv_wkt.setVisibility(View.INVISIBLE);

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
        Picasso.with(getActivity()).load(getPictureURL(teamA)).placeholder(R.drawable.progress_animation).error(R.drawable.no_team).into(iv_team1);
        Picasso.with(getActivity()).load(getPictureURL(teamB)).placeholder(R.drawable.progress_animation).error(R.drawable.no_team).into(iv_team2);
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
                    iv_wkt.setVisibility(View.VISIBLE);
                    setImageWK(iv_wkt, tv_wkt_name, teamPlayerDto);
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
                setImageAllRounder(iv_ar1, tv_ar1_name, teamPlayerDto);
                break;
            case 2:
                rl_ar2.setVisibility(View.VISIBLE);
                setImageAllRounder(iv_ar2, tv_ar2_name, teamPlayerDto);
                break;
            case 3:
                rl_ar3.setVisibility(View.VISIBLE);
                setImageAllRounder(iv_ar3, tv_ar3_name, teamPlayerDto);
                break;
            case 4:
                rl_ar4.setVisibility(View.VISIBLE);
                setImageAllRounder(iv_ar4, tv_ar4_name, teamPlayerDto);
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
                setImageBatsman(iv_bat1, tv_bat1_name, teamPlayerDto);
                break;
            case 2:
                rl_bat2.setVisibility(View.VISIBLE);
                setImageBatsman(iv_bat2, tv_bat2_name, teamPlayerDto);
                break;
            case 3:
                rl_bat3.setVisibility(View.VISIBLE);
                setImageBatsman(iv_bat3, tv_bat3_name, teamPlayerDto);
                break;
            case 4:
                rl_bat4.setVisibility(View.VISIBLE);
                setImageBatsman(iv_bat4, tv_bat4_name, teamPlayerDto);
                break;
            case 5:
                rl_bat5.setVisibility(View.VISIBLE);
                setImageBatsman(iv_bat5, tv_bat5_name, teamPlayerDto);
                break;
            case 6:
                rl_bat6.setVisibility(View.VISIBLE);
                setImageBatsman(iv_bat6, tv_bat6_name, teamPlayerDto);
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
                setImageBowler(iv_bowler1, tv_bowler1_name, teamPlayerDto);
                break;
            case 2:
                rl_bowler2.setVisibility(View.VISIBLE);
                setImageBowler(iv_bowler2, tv_bowler2_name, teamPlayerDto);
                break;
            case 3:
                rl_bowler3.setVisibility(View.VISIBLE);
                setImageBowler(iv_bowler3, tv_bowler3_name, teamPlayerDto);
                break;
            case 4:
                rl_bowler4.setVisibility(View.VISIBLE);
                setImageBowler(iv_bowler4, tv_bowler4_name, teamPlayerDto);
                break;
            case 5:
                rl_bowler5.setVisibility(View.VISIBLE);
                setImageBowler(iv_bowler5, tv_bowler5_name, teamPlayerDto);
                break;
            case 6:
                rl_bowler6.setVisibility(View.VISIBLE);
                setImageBowler(iv_bowler6, tv_bowler6_name, teamPlayerDto);
                break;
        }
    }


    private void setImageWK(ImageView iv_bowler1, CustomTextView tv_wicketKeeper, TeamPlayerDto teamPlayerDto) {
        if (teamPlayerDto.player.equalsIgnoreCase(teamDto.captain)) {
            iv_bowler1.setImageDrawable(getResources().getDrawable(R.drawable.w1));
        } else if (teamPlayerDto.player.equalsIgnoreCase(teamDto.vice_captain)) {
            iv_bowler1.setImageDrawable(getResources().getDrawable(R.drawable.w2));
        }
        tv_wicketKeeper.setText(teamPlayerDto.full_name);
    }

    private void setImageAllRounder(ImageView iv_bowler1, CustomTextView tv_allRounder, TeamPlayerDto teamPlayerDto) {
        if (teamPlayerDto.player.equalsIgnoreCase(teamDto.captain)) {
            iv_bowler1.setImageDrawable(getResources().getDrawable(R.drawable.ar1));
        } else if (teamPlayerDto.player.equalsIgnoreCase(teamDto.vice_captain)) {
            iv_bowler1.setImageDrawable(getResources().getDrawable(R.drawable.ar2));
        }
        tv_allRounder.setText(teamPlayerDto.full_name);
    }

    private void setImageBowler(ImageView iv_bowler1, CustomTextView tv_bowler, TeamPlayerDto teamPlayerDto) {
        if (teamPlayerDto.player.equalsIgnoreCase(teamDto.captain)) {
            iv_bowler1.setImageDrawable(getResources().getDrawable(R.drawable.b1));
        } else if (teamPlayerDto.player.equalsIgnoreCase(teamDto.vice_captain)) {
            iv_bowler1.setImageDrawable(getResources().getDrawable(R.drawable.b2));
        }
        tv_bowler.setText(teamPlayerDto.full_name);
    }

    private void setImageBatsman(ImageView iv_bowler1, CustomTextView tv_batsman, TeamPlayerDto teamPlayerDto) {
        if (teamPlayerDto.player.equalsIgnoreCase(teamDto.captain)) {
            iv_bowler1.setImageDrawable(getResources().getDrawable(R.drawable.bat1));
        } else if (teamPlayerDto.player.equalsIgnoreCase(teamDto.vice_captain)) {
            iv_bowler1.setImageDrawable(getResources().getDrawable(R.drawable.bat2));
        }
        tv_batsman.setText(teamPlayerDto.full_name);
    }

    private String getPictureURL(String teama) {
        String country = teama.trim().replace(" ", "-");
        return "http://52.15.50.179/public/images/team/flag-of-" + country + ".png";
    }

    @Override
    public void onButtonClick(View view) {
        super.onButtonClick(view);
        switch (view.getId()){
            case R.id.img_back:
                getActivity().onBackPressed();
                break;
        }
    }
}
