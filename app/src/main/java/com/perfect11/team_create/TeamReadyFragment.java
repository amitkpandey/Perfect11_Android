package com.perfect11.team_create;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.perfect11.R;
import com.perfect11.base.ApiClient;
import com.perfect11.base.ApiInterface;
import com.perfect11.base.BaseFragment;
import com.perfect11.home.wrapper.CreateTeamCallBackWrapper;
import com.perfect11.login_signup.dto.UserDto;
import com.perfect11.team_create.dto.PlayerDto;
import com.perfect11.team_create.dto.SelectedMatchDto;
import com.perfect11.upcoming_matches.dto.UpComingMatchesDto;
import com.squareup.picasso.Picasso;
import com.utility.DialogUtility;
import com.utility.PreferenceUtility;
import com.utility.customView.CustomButton;
import com.utility.customView.CustomTextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeamReadyFragment extends BaseFragment {
    private ArrayList<PlayerDto> selectedTeam;
    private CustomTextView tv_team1, tv_team2, tv_team_count1, tv_team_count2;
    private CircleImageView cimg_country1, cimg_country2;

    private ImageView iv_wkt;
    private ImageView iv_bat1, iv_bat2, iv_bat3, iv_bat4, iv_bat5, iv_bat6;
    private ImageView iv_ar1, iv_ar2, iv_ar3, iv_ar4;
    private ImageView iv_bowler1, iv_bowler2, iv_bowler3, iv_bowler4, iv_bowler5, iv_bowler6;

    private CustomButton btn_save;

    int bowler = 0, batsman = 0, allrounder = 0, keeper = 0;

    private SelectedMatchDto selectedMatchDto;
    private UpComingMatchesDto upCommingMatchesDto;
    private ApiInterface apiInterface;

    private ArrayList<String> batsmanList = new ArrayList<>();
    private ArrayList<String> allRounderList = new ArrayList<>();
    private ArrayList<String> bowlerList = new ArrayList<>();
    private ArrayList<String> keeperList = new ArrayList<>();
    private String captain = "", vcaptain = "";
    private float player_amount_count = 0;
    private UserDto userDto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.ready_team, container, false);
        readFromBundle();
        initView();
        return view;
    }

    private void readFromBundle() {
        userDto = (UserDto) PreferenceUtility.getObjectInAppPreference(getActivity(), PreferenceUtility.APP_PREFERENCE_NAME);
        selectedTeam = (ArrayList<PlayerDto>) getArguments().getSerializable("selectedTeam");
        selectedMatchDto = (SelectedMatchDto) getArguments().getSerializable("selectedMatchDto");
        upCommingMatchesDto = (UpComingMatchesDto) getArguments().getSerializable("upCommingMatchesDto");
        System.out.println("upCommingMatchesDto:" + upCommingMatchesDto.toString());
    }

    private void initView() {
        tv_team1 = view.findViewById(R.id.tv_team1);
        tv_team2 = view.findViewById(R.id.tv_team2);
        tv_team_count1 = view.findViewById(R.id.tv_team_count1);
        tv_team_count2 = view.findViewById(R.id.tv_team_count2);

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

        btn_save = view.findViewById(R.id.btn_save);
        btn_save.setText("Save Team");

        setPlayerVisiblityGone();
        setTeam();
    }

    private void setPlayerVisiblityGone() {
        iv_wkt.setVisibility(View.GONE);

        iv_bat1.setVisibility(View.GONE);
        iv_bat2.setVisibility(View.GONE);
        iv_bat3.setVisibility(View.GONE);
        iv_bat4.setVisibility(View.GONE);
        iv_bat5.setVisibility(View.GONE);
        iv_bat6.setVisibility(View.GONE);

        iv_ar1.setVisibility(View.INVISIBLE);
        iv_ar2.setVisibility(View.INVISIBLE);
        iv_ar3.setVisibility(View.INVISIBLE);
        iv_ar4.setVisibility(View.INVISIBLE);

        iv_bowler1.setVisibility(View.GONE);
        iv_bowler2.setVisibility(View.GONE);
        iv_bowler3.setVisibility(View.GONE);
        iv_bowler4.setVisibility(View.GONE);
        iv_bowler5.setVisibility(View.GONE);
        iv_bowler6.setVisibility(View.GONE);
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
        tv_team1.setText(selectedMatchDto.teamName1);
        tv_team2.setText(selectedMatchDto.teamName2);
        Picasso.with(getActivity()).load(getPictureURL(selectedMatchDto.teamName1)).placeholder(R.drawable.progress_animation).error(R.drawable.no_team).into(cimg_country1);
        Picasso.with(getActivity()).load(getPictureURL(selectedMatchDto.teamName2)).placeholder(R.drawable.progress_animation).error(R.drawable.no_team).into(cimg_country2);
        tv_team_count1.setText("" + total_team1 + "/7");
        tv_team_count2.setText("" + total_team2 + "/7");
        arrangePlayerOnField();
    }

    private void arrangePlayerOnField() {

        for (PlayerDto playerDto : selectedTeam) {
            /** Divided  players*/
            switch (playerDto.seasonal_role) {
                case "bowler":
                    bowler++;
                    setVisibleBowler(bowler, playerDto.isC, playerDto.isCV);
                    bowlerList.add(playerDto.short_name);
                    break;
                case "batsman":
                    batsman++;
                    setVisibleBatsman(batsman, playerDto.isC, playerDto.isCV);
                    batsmanList.add(playerDto.short_name);
                    break;
                case "allrounder":
                    allrounder++;
                    setVisibleAllrounder(allrounder, playerDto.isC, playerDto.isCV);
                    allRounderList.add(playerDto.short_name);
                    break;
                case "keeper":
                    iv_wkt.setVisibility(View.VISIBLE);
                    setImageWK(iv_wkt, playerDto.isC, playerDto.isCV);
                    keeperList.add(playerDto.short_name);
                    break;
            }

            player_amount_count = player_amount_count + Float.parseFloat(playerDto.credit);

            if (playerDto.isC) {
                captain = playerDto.short_name;
            }
            if (playerDto.isCV) {
                vcaptain = playerDto.short_name;
            }
        }
    }

    private void setVisibleAllrounder(int allrounder, boolean isC, boolean isVC) {

        switch (allrounder) {
            case 1:
                iv_ar1.setVisibility(View.VISIBLE);
                setImageAllrounder(iv_ar1, isC, isVC);
                break;
            case 2:
                iv_ar2.setVisibility(View.VISIBLE);
                setImageAllrounder(iv_ar2, isC, isVC);
                break;
            case 3:
                iv_ar3.setVisibility(View.VISIBLE);
                setImageAllrounder(iv_ar3, isC, isVC);
                break;
            case 4:
                iv_ar4.setVisibility(View.VISIBLE);
                setImageAllrounder(iv_ar4, isC, isVC);
                break;
        }
    }

    /**
     * Visible Batsman
     */
    private void setVisibleBatsman(int batsman, boolean isC, boolean isVC) {
        switch (batsman) {
            case 1:
                iv_bat1.setVisibility(View.VISIBLE);
                setImageBatsman(iv_bat1, isC, isVC);
                break;
            case 2:
                iv_bat2.setVisibility(View.VISIBLE);
                setImageBatsman(iv_bat2, isC, isVC);
                break;
            case 3:
                iv_bat3.setVisibility(View.VISIBLE);
                setImageBatsman(iv_bat3, isC, isVC);
                break;
            case 4:
                iv_bat4.setVisibility(View.VISIBLE);
                setImageBatsman(iv_bat4, isC, isVC);
                break;
            case 5:
                iv_bat5.setVisibility(View.VISIBLE);
                setImageBatsman(iv_bat5, isC, isVC);
                break;
            case 6:
                iv_bat6.setVisibility(View.VISIBLE);
                setImageBatsman(iv_bat6, isC, isVC);
                break;
        }
    }

    /**
     * Visible Bowler
     */
    private void setVisibleBowler(int bowler, boolean isC, boolean isVC) {
        switch (bowler) {
            case 1:
                iv_bowler1.setVisibility(View.VISIBLE);
                setImageBowler(iv_bowler1, isC, isVC);
                break;
            case 2:
                iv_bowler2.setVisibility(View.VISIBLE);
                setImageBowler(iv_bowler2, isC, isVC);
                break;
            case 3:
                iv_bowler3.setVisibility(View.VISIBLE);
                setImageBowler(iv_bowler3, isC, isVC);
                break;
            case 4:
                iv_bowler4.setVisibility(View.VISIBLE);
                setImageBowler(iv_bowler4, isC, isVC);
                break;
            case 5:
                iv_bowler5.setVisibility(View.VISIBLE);
                setImageBowler(iv_bowler5, isC, isVC);
                break;
            case 6:
                iv_bowler6.setVisibility(View.VISIBLE);
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
                /*Bundle bundle=new Bundle();
                bundle.putSerializable("selectedTeam",selectedTeam);
                bundle.putSerializable("selectedMatchDto", selectedMatchDto);
                bundle.putSerializable("upCommingMatchesDto", upCommingMatchesDto);
                ActivityController.startNextActivity(getActivity(), ChooseContestActivity.class,bundle, false);*/
                callCreateTeamAPI();
                break;
        }
    }

    private String getPictureURL(String teama) {
        String country = teama.trim().replace(" ", "-");
        String url = "http://52.15.50.179/public/images/team/flag-of-" + country + ".png";
        return url;
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
                player_amount_count, upCommingMatchesDto.key_name, vcaptain, userDto.member_id);
        call.enqueue(new Callback<CreateTeamCallBackWrapper>() {
            @Override
            public void onResponse(Call<CreateTeamCallBackWrapper> call, Response<CreateTeamCallBackWrapper> response) {
                CreateTeamCallBackWrapper callBackDto = response.body();

                Log.e("CreateTeamCallBack", callBackDto.toString());
                if (callBackDto.status) {
                    Log.e("CreateTeamCallBack", callBackDto.message);
                    // callAPIJoinContest(callBackDto.data.team_id);
                } else {
                    DialogUtility.showMessageWithOk(callBackDto.message, getActivity());
                }
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<CreateTeamCallBackWrapper> call, Throwable t) {
                Log.e("TAG", t.toString());
                DialogUtility.showMessageWithOk(t.toString(), getActivity());
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }
}
