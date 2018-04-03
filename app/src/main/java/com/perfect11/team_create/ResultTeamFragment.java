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
import android.widget.Toast;

import com.perfect11.R;
import com.perfect11.base.ApiClient;
import com.perfect11.base.ApiInterface;
import com.perfect11.base.BaseFragment;
import com.perfect11.contest.dto.TeamDto;
import com.perfect11.contest.dto.TeamPlayerDto;
import com.perfect11.contest.wrapper.TeamWrapper;
import com.perfect11.login_signup.dto.UserDto;
import com.squareup.picasso.Picasso;
import com.utility.DialogUtility;
import com.utility.PreferenceUtility;
import com.utility.customView.CustomTextView;

import java.io.IOException;

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

    private ImageView iv_wkt_c;
    private ImageView iv_bat1_c, iv_bat2_c, iv_bat3_c, iv_bat4_c, iv_bat5_c, iv_bat6_c;
    private ImageView iv_ar1_c, iv_ar2_c, iv_ar3_c, iv_ar4_c;
    private ImageView iv_bowler1_c, iv_bowler2_c, iv_bowler3_c, iv_bowler4_c, iv_bowler5_c, iv_bowler6_c;

    private CircleImageView iv_team1, iv_team2;

    private int bowler = 0, batsman = 0, allrounder = 0, keeper = 0;

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
                    break;
                case "batsman":
                    batsman++;
                    setVisibleBatsman(batsman, teamPlayerDto);
                    break;
                case "allrounder":
                    allrounder++;
                    setVisibleAllRounder(allrounder, teamPlayerDto);
                    break;
                case "keeper":
//                    iv_wkt.setVisibility(View.VISIBLE);
                    setImageWK(iv_wkt,iv_wkt_c, tv_wkt_point, tv_wkt_name, teamPlayerDto);

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

    private void setVisibleAllRounder(int allRounder, TeamPlayerDto teamPlayerDto) {

        switch (allRounder) {
            case 1:
                rl_ar1.setVisibility(View.VISIBLE);
                setImageAllRounder(iv_ar1,iv_ar1_c, tv_ar1_point, tv_ar1_name, teamPlayerDto);
                break;
            case 2:
                rl_ar2.setVisibility(View.VISIBLE);
                setImageAllRounder(iv_ar2, iv_ar2_c, tv_ar2_point, tv_ar2_name, teamPlayerDto);
                break;
            case 3:
                rl_ar3.setVisibility(View.VISIBLE);
                setImageAllRounder(iv_ar3, iv_ar3_c, tv_ar3_point, tv_ar3_name, teamPlayerDto);
                break;
            case 4:
                rl_ar4.setVisibility(View.VISIBLE);
                setImageAllRounder(iv_ar4, iv_ar4_c, tv_ar4_point, tv_ar4_name, teamPlayerDto);
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
                setImageBatsman(iv_bat1,iv_bat1_c, tv_bat1_point, tv_bat1_name, teamPlayerDto);
                break;
            case 2:
                rl_bat2.setVisibility(View.VISIBLE);
                setImageBatsman(iv_bat2, iv_bat2_c, tv_bat2_point, tv_bat2_name, teamPlayerDto);
                break;
            case 3:
                rl_bat3.setVisibility(View.VISIBLE);
                setImageBatsman(iv_bat3, iv_bat3_c, tv_bat3_point, tv_bat3_name, teamPlayerDto);
                break;
            case 4:
                rl_bat4.setVisibility(View.VISIBLE);
                setImageBatsman(iv_bat4, iv_bat4_c, tv_bat4_point, tv_bat4_name, teamPlayerDto);
                break;
            case 5:
                rl_bat5.setVisibility(View.VISIBLE);
                setImageBatsman(iv_bat5, iv_bat5_c, tv_bat5_point, tv_bat5_name, teamPlayerDto);
                break;
            case 6:
                rl_bat6.setVisibility(View.VISIBLE);
                setImageBatsman(iv_bat6, iv_bat6_c, tv_bat6_point, tv_bat6_name, teamPlayerDto);
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
                setImageBowler(iv_bowler1,iv_bowler1_c, tv_bowler1_point, tv_bowler1_name, teamPlayerDto);
                break;
            case 2:
                rl_bowler2.setVisibility(View.VISIBLE);
                setImageBowler(iv_bowler2, iv_bowler2_c, tv_bowler2_point, tv_bowler2_name, teamPlayerDto);
                break;
            case 3:
                rl_bowler3.setVisibility(View.VISIBLE);
                setImageBowler(iv_bowler3, iv_bowler3_c, tv_bowler3_point, tv_bowler3_name, teamPlayerDto);
                break;
            case 4:
                rl_bowler4.setVisibility(View.VISIBLE);
                setImageBowler(iv_bowler4, iv_bowler4_c, tv_bowler4_point, tv_bowler4_name, teamPlayerDto);
                break;
            case 5:
                rl_bowler5.setVisibility(View.VISIBLE);
                setImageBowler(iv_bowler5, iv_bowler5_c, tv_bowler5_point, tv_bowler5_name, teamPlayerDto);
                break;
            case 6:
                rl_bowler6.setVisibility(View.VISIBLE);
                setImageBowler(iv_bowler6, iv_bowler6_c, tv_bowler6_point, tv_bowler6_name, teamPlayerDto);
                break;
        }
    }


    private void setImageWK(ImageView iv_bowler1, ImageView iv_wkt_c, TextView tv_wicketKeeperPoint, TextView tv_wicketKeeperName, TeamPlayerDto teamPlayerDto) {
        if (teamPlayerDto.player.equalsIgnoreCase(teamDto.captain)) {
            iv_wkt_c.setVisibility(View.VISIBLE);
            iv_wkt_c.setImageDrawable(getResources().getDrawable(R.drawable.c));
        } else if (teamPlayerDto.player.equalsIgnoreCase(teamDto.vice_captain)) {
            iv_wkt_c.setVisibility(View.VISIBLE);
            iv_wkt_c.setImageDrawable(getResources().getDrawable(R.drawable.vc));
        }
        iv_bowler1.setVisibility(View.VISIBLE);
        tv_wicketKeeperPoint.setVisibility(View.VISIBLE);
        tv_wicketKeeperName.setVisibility(View.VISIBLE);
        tv_wicketKeeperPoint.setText(teamPlayerDto.points_gain);
        tv_wicketKeeperName.setText(teamPlayerDto.full_name);
        setKeeperImage(iv_bowler1,teamPlayerDto.team_code);
    }

    private void setImageAllRounder(ImageView iv_bowler1, ImageView iv_ar1_c, TextView tv_allRounderPoint, TextView tv_allRounderName, TeamPlayerDto teamPlayerDto) {
        if (teamPlayerDto.player.equalsIgnoreCase(teamDto.captain)) {
            iv_ar1_c.setVisibility(View.VISIBLE);
            iv_ar1_c.setImageDrawable(getResources().getDrawable(R.drawable.c));
        } else if (teamPlayerDto.player.equalsIgnoreCase(teamDto.vice_captain)) {
            iv_ar1_c.setVisibility(View.VISIBLE);
            iv_ar1_c.setImageDrawable(getResources().getDrawable(R.drawable.vc));
        }
        iv_bowler1.setVisibility(View.VISIBLE);
        tv_allRounderPoint.setText(teamPlayerDto.points_gain);
        tv_allRounderName.setText(teamPlayerDto.full_name);
        setAllrounderImage(iv_bowler1,teamPlayerDto.team_code);
    }

    private void setImageBowler(ImageView iv_bowler1, ImageView iv_bowler1_c, TextView tv_bowlerPoint, TextView tv_bowlerName, TeamPlayerDto teamPlayerDto) {
        if (teamPlayerDto.player.equalsIgnoreCase(teamDto.captain)) {
            iv_bowler1_c.setVisibility(View.VISIBLE);
            iv_bowler1_c.setImageDrawable(getResources().getDrawable(R.drawable.c));
        } else if (teamPlayerDto.player.equalsIgnoreCase(teamDto.vice_captain)) {
            iv_bowler1_c.setVisibility(View.VISIBLE);
            iv_bowler1_c.setImageDrawable(getResources().getDrawable(R.drawable.vc));
        }
        iv_bowler1.setVisibility(View.VISIBLE);
        tv_bowlerPoint.setText(teamPlayerDto.points_gain);
        tv_bowlerName.setText(teamPlayerDto.full_name);
        setBowlerImage(iv_bowler1,teamPlayerDto.team_code);
    }

    private void setImageBatsman(ImageView iv_bowler1, ImageView iv_bat1_c, TextView tv_batsmanPoint, TextView tv_batsmanName, TeamPlayerDto teamPlayerDto) {
        if (teamPlayerDto.player.equalsIgnoreCase(teamDto.captain)) {
            iv_bat1_c.setVisibility(View.VISIBLE);
            iv_bat1_c.setImageDrawable(getResources().getDrawable(R.drawable.c));
        } else if (teamPlayerDto.player.equalsIgnoreCase(teamDto.vice_captain)) {
            iv_bat1_c.setVisibility(View.VISIBLE);
            iv_bat1_c.setImageDrawable(getResources().getDrawable(R.drawable.vc));
        }
        iv_bowler1.setVisibility(View.VISIBLE);
        tv_batsmanPoint.setText(teamPlayerDto.points_gain);
        tv_batsmanName.setText(teamPlayerDto.full_name);
        setBatsmanImage(iv_bowler1,teamPlayerDto.team_code);
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
