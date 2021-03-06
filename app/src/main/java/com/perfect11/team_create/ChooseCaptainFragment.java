package com.perfect11.team_create;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.perfect11.R;
import com.perfect11.base.BaseFragment;
import com.perfect11.base.BaseHeaderActivity;
import com.perfect11.team_create.adapter.CaptainAdapter;
import com.perfect11.team_create.dto.ContestDto;
import com.perfect11.team_create.dto.PlayerDto;
import com.perfect11.team_create.dto.SelectedMatchDto;
import com.perfect11.upcoming_matches.dto.UpComingMatchesDto;
import com.utility.customView.CustomButton;
import com.utility.customView.CustomTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class ChooseCaptainFragment extends BaseFragment {
    private RecyclerView rv_team;
    private ArrayList<PlayerDto> selectedPlayer = null;
    private CaptainAdapter captainAdapter;
    private SelectedMatchDto selectedMatchDto;
    private UpComingMatchesDto upComingMatchesDto;
    private CustomTextView tv_player_count, tv_header, ctv_country1, ctv_country2, ctv_time;
    private CustomButton btn_save;
    private ContestDto contestDto;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.choose_captain, container, false);
        readFromBundle();
        startUpdateTimer();
        initView();
        //setTeamName();
        return view;
    }

    private void setTeamName() {
        final Dialog dialog = new Dialog(getActivity());
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
                    Toast.makeText(getActivity(), "Enter Your Team Name", Toast.LENGTH_SHORT).show();
                }else if (!team_name.getText().toString().trim().matches("[a-zA-Z0-9 ]*")) {
                    Toast.makeText(getActivity(), "Special Character are not allow", Toast.LENGTH_SHORT).show();
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
        selectedPlayer = (ArrayList<PlayerDto>) getArguments().getSerializable("selectedPlayer");
        selectedMatchDto = (SelectedMatchDto) getArguments().getSerializable("selectedMatchDto");
        upComingMatchesDto = (UpComingMatchesDto) getArguments().getSerializable("upComingMatchesDto");
        contestDto = (ContestDto) getArguments().getSerializable("contestDto");
//        System.out.println("upComingMatchesDto:" + upComingMatchesDto.toString());
    }

    private void initView() {
        btn_save= view.findViewById(R.id.btn_save);
        btn_save.setText("Set Team Name");
        rv_team = view.findViewById(R.id.rv_team);
        ctv_country1 = view.findViewById(R.id.ctv_country1);
        ctv_country2 = view.findViewById(R.id.ctv_country2);
        ctv_time = view.findViewById(R.id.ctv_time);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_team.setLayoutManager(layoutManager);

        captainAdapter = new CaptainAdapter(getActivity(), selectedPlayer, selectedMatchDto.teamName1, selectedMatchDto.teamName2);
        rv_team.setAdapter(captainAdapter);
        String[] team = upComingMatchesDto.short_name.split(" ");
        String team1 = team[0];
        String team2 = team[2];
        ctv_country1.setText(team1);
        ctv_country2.setText(team2);
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

    public void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                getActivity().onBackPressed();
                break;
            case R.id.btn_save:
                ArrayList<PlayerDto> selectedTeam = captainAdapter.getSelectedCaptainAndVCaptainWithTeam();

                if (team_Name.trim().equals("")) {
                    setTeamName();
                } else {
                    if (selectedTeam != null) {
                    /*for (PlayerDto playerDto : selectedTeam) {
                        System.out.println(playerDto.full_name + "  " + playerDto.isC + "  " + playerDto.isCV);
                    }*/
                        Bundle bundle = new Bundle();
                        upComingMatchesDto.my_team_name = team_Name;
                        bundle.putSerializable("selectedTeam", selectedTeam);
                        bundle.putSerializable("selectedMatchDto", selectedMatchDto);
                        bundle.putSerializable("upComingMatchesDto", upComingMatchesDto);
                        bundle.putSerializable("contestDto", contestDto);
                        TeamReadyFragment teamReadyFragment = new TeamReadyFragment();
                        teamReadyFragment.setArguments(bundle);
                        ((BaseHeaderActivity) getActivity()).addFragment(teamReadyFragment, true, TeamReadyFragment.class.getName());
                    }
                }
                break;
        }
    }
}
