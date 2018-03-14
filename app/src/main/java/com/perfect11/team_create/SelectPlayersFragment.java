package com.perfect11.team_create;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.perfect11.R;
import com.perfect11.base.ApiClient;
import com.perfect11.base.ApiInterface;
import com.perfect11.base.BaseFragment;
import com.perfect11.base.BaseHeaderActivity;
import com.perfect11.team_create.adapter.PlayerTypeAdapter;
import com.perfect11.team_create.adapter.WkAdapter;
import com.perfect11.team_create.dto.PlayerDto;
import com.perfect11.team_create.dto.SelectedMatchDto;
import com.perfect11.team_create.wrapper.PlayerWrapper;
import com.perfect11.upcoming_matches.dto.UpComingMatchesDto;
import com.squareup.picasso.Picasso;
import com.utility.DialogUtility;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectPlayersFragment extends BaseFragment {
    private RecyclerView rv_section, rv_list;
    private UpComingMatchesDto upComingMatchesDto;
    private ApiInterface apiInterface;
    private PlayerWrapper playerWrapper;

    private ArrayList<PlayerDto> bowler = new ArrayList<>();
    private ArrayList<PlayerDto> batsman = new ArrayList<>();
    private ArrayList<PlayerDto> allrounder = new ArrayList<>();
    private ArrayList<PlayerDto> keeper = new ArrayList<>();
    private ArrayList<PlayerDto> selectedPlayer = null;

    private PlayerTypeAdapter playerTypeAdapter;

    private WkAdapter wkAdapter;
    private WkAdapter batAdapter;
    private WkAdapter arAdapter;
    private WkAdapter bowlAdapter;

    private int selectedplayerType = 0;
    private float totalPoints = 0;
    private int totalPlayers = 0;

    private SelectedMatchDto selectedMatchDto;

    private LinearLayout ll_preview, ll_select_payer;

    private CustomTextView tv_player_count, tv_header;
    private CustomButton btn_save;

    /**
     * Preview Section Start
     */
    /**
     * Preview Section Start
     * Ground View Start
     */
    private CustomTextView tv_team1, tv_team2, tv_team_count1, tv_team_count2, ctv_time;
    private CircleImageView cimg_country1, cimg_country2;

    private ImageView iv_wkt;
    private ImageView iv_bat1, iv_bat2, iv_bat3, iv_bat4, iv_bat5, iv_bat6;
    private ImageView iv_ar1, iv_ar2, iv_ar3, iv_ar4;
    private ImageView iv_bowler1, iv_bowler2, iv_bowler3, iv_bowler4, iv_bowler5, iv_bowler6;
    /**
     * Ground View End
     */

    private Handler mHandler = new Handler();
    private Runnable updateRemainingTimeRunnable = new Runnable() {
        @Override
        public void run() {
            long currentTime = System.currentTimeMillis();
            updateTimeRemaining(currentTime);
        }
    };

    /**
     * Preview Section End
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.activity_select_players, container, false);
        initView();
        readFromBundle();
        startUpdateTimer();
        callAPI();
        return view;
    }

    public static Fragment newInstance() {
        return new SelectPlayersFragment();
    }

    private void readFromBundle() {
        upComingMatchesDto = (UpComingMatchesDto) getArguments().getSerializable("upComingMatchesDto");
    }

    private void initView() {
        initViewPreview();
        /**
         * Preview Section
         * */

        tv_team1 = view.findViewById(R.id.tv_team1);
        tv_team2 = view.findViewById(R.id.tv_team2);

        tv_team_count1 = view.findViewById(R.id.tv_team_count1);
        tv_team_count2 = view.findViewById(R.id.tv_team_count2);
        ctv_time = view.findViewById(R.id.ctv_time);
/**
 * End Preview Section
 * */
        tv_player_count = view.findViewById(R.id.tv_player_count);
        tv_player_count.setVisibility(View.VISIBLE);

        tv_header = view.findViewById(R.id.tv_header);
        tv_header.setVisibility(View.VISIBLE);

        btn_save = view.findViewById(R.id.btn_save);
        btn_save.setText("Next");

        ll_preview = view.findViewById(R.id.ll_preview);
        ll_select_payer = view.findViewById(R.id.ll_select_payer);

        rv_section = view.findViewById(R.id.rv_section);
        rv_list = view.findViewById(R.id.rv_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_section.setLayoutManager(layoutManager);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity());
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        rv_list.setLayoutManager(layoutManager1);

        selectedMatchDto = new SelectedMatchDto();
    }

    private void initViewPreview() {
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
    }

    private void setTeam() {
        setPlayerVisiblityGone();
        tv_team1.setText(upComingMatchesDto.teama);
        tv_team2.setText(upComingMatchesDto.teamb);
        System.out.println("getPictureURL(upCommingMatchesDto.teama) "+ getPictureURL(upComingMatchesDto.teama));
        System.out.println("getPictureURL(upCommingMatchesDto.teamb) "+ getPictureURL(upComingMatchesDto.teamb));
        Picasso.with(getActivity()).load(getPictureURL(upComingMatchesDto.teama)).placeholder(R.drawable.progress_animation).error(R.drawable.no_team).into(cimg_country1);
        Picasso.with(getActivity()).load(getPictureURL(upComingMatchesDto.teamb)).placeholder(R.drawable.progress_animation).error(R.drawable.no_team).into(cimg_country2);
        arrangePlayerOnField();
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

    private void arrangePlayerOnField() {
        int total_team1 = 0;
        int total_team2 = 0;
        int i = 1,j = 1,k = 1;
        /**bowler*/
        for (PlayerDto playerDto : bowler) {
            if (playerDto.isSelected) {
                System.out.println("Count:"+i);
                setVisibleBowler(i);
                i++;

                if (playerDto.team_name.trim().equals(upComingMatchesDto.teama)) {
                    total_team1++;
                } else {
                    total_team2++;
                }
            }
        }
        /**batsman*/
        for (PlayerDto playerDto : batsman) {
            if (playerDto.isSelected) {
                System.out.println("Count:"+j);
                setVisibleBatsman(j);
                j++;

                if (playerDto.team_name.trim().equals(upComingMatchesDto.teama)) {
                    total_team1++;
                } else {
                    total_team2++;
                }
            }
        }

/**allrounder*/

        for (PlayerDto playerDto : allrounder) {
            if (playerDto.isSelected) {
                System.out.println("Count:"+k);
                setVisibleAllrounder(k);
                k++;

                if (playerDto.team_name.trim().equals(upComingMatchesDto.teama)) {
                    total_team1++;
                } else {
                    total_team2++;
                }
            }
        }

/**keeper*/
        for (PlayerDto playerDto : keeper) {
            if (playerDto.isSelected) {
                iv_wkt.setVisibility(View.VISIBLE);

                if (playerDto.team_name.trim().equals(upComingMatchesDto.teama)) {
                    total_team1++;
                } else {
                    total_team2++;
                }
            }
        }


        /** Set Player Number*/
        tv_team_count1.setText("" + total_team1 + "/7");
        tv_team_count2.setText("" + total_team2 + "/7");
    }

    private void setVisibleAllrounder(int allrounder) {

        switch (allrounder) {
            case 1:
                iv_ar1.setVisibility(View.VISIBLE);
                break;
            case 2:
                iv_ar2.setVisibility(View.VISIBLE);
                break;
            case 3:
                iv_ar3.setVisibility(View.VISIBLE);
                break;
            case 4:
                iv_ar4.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * Visible Batsman
     */
    private void setVisibleBatsman(int batsman) {
        switch (batsman) {
            case 1:
                iv_bat1.setVisibility(View.VISIBLE);
                break;
            case 2:
                iv_bat2.setVisibility(View.VISIBLE);
                break;
            case 3:
                iv_bat3.setVisibility(View.VISIBLE);
                break;
            case 4:
                iv_bat4.setVisibility(View.VISIBLE);
                break;
            case 5:
                iv_bat5.setVisibility(View.VISIBLE);
                break;
            case 6:
                iv_bat6.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * Visible Bowler
     */
    private void setVisibleBowler(int bowler) {
        switch (bowler) {
            case 1:
                iv_bowler1.setVisibility(View.VISIBLE);
                break;
            case 2:
                iv_bowler2.setVisibility(View.VISIBLE);
                break;
            case 3:
                iv_bowler3.setVisibility(View.VISIBLE);
                break;
            case 4:
                iv_bowler4.setVisibility(View.VISIBLE);
                break;
            case 5:
                iv_bowler5.setVisibility(View.VISIBLE);
                break;
            case 6:
                iv_bowler6.setVisibility(View.VISIBLE);
                break;
        }
    }

    private String getPictureURL(String teama) {
        String country = teama.trim().replace(" ", "-");
        String url = "http://52.15.50.179/public/images/team/flag-of-" + country + ".png";
        return url;
    }


    private void updateTimeRemaining(long currentTime) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            Date date;
            date = sdf.parse(upComingMatchesDto.start_date);
            long millis = date.getTime();
            long timeDiff = millis - currentTime;
            if (timeDiff > 0) {
                int seconds = (int) (timeDiff / 1000) % 60;
                int minutes = (int) ((timeDiff / (1000 * 60)) % 60);
                int hours = (int) ((timeDiff / (1000 * 60 * 60)) % 24);
                ctv_time.setText(hours + " hrs " + minutes + " mins " + seconds + " sec");
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
                if (isMinimumPlayerSelected()) {
                    Bundle bundle = new Bundle();
                    selectedPlayer = getSelectedPlayers();
                    bundle.putSerializable("selectedPlayer", selectedPlayer);
                    selectedMatchDto.teamName1 = upComingMatchesDto.teama;
                    selectedMatchDto.teamName2 = upComingMatchesDto.teamb;
                    selectedMatchDto.numberOfPlayer = totalPlayers;
                    selectedMatchDto.credit_used = totalPoints;
                    bundle.putSerializable("selectedMatchDto", selectedMatchDto);
                    bundle.putSerializable("upComingMatchesDto", upComingMatchesDto);
                    System.out.println("teamName1:" + upComingMatchesDto.teama + "   teamName2:" + upComingMatchesDto.teamb);
                    ChooseCaptainFragment chooseCaptainFragment = new ChooseCaptainFragment();
                    chooseCaptainFragment.setArguments(bundle);
                    ((BaseHeaderActivity) getActivity()).addFragment(chooseCaptainFragment, true, ChooseCaptainFragment.class.getName());
                 }
                break;
            case R.id.ll_up:
                setTeam();
                ll_select_payer.setVisibility(View.GONE);
                ll_preview.setVisibility(View.VISIBLE);
              /*  ll_select_payer.animate()
                        .translationY(view.getHeight())
                        .alpha(0.0f)
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                ll_select_payer.setVisibility(View.GONE);
                            }
                        });*/
               /* ll_preview.animate()
                        .translationY(view.getHeight())
                        .alpha(0.0f)
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                ll_select_payer.setVisibility(View.VISIBLE);
                            }
                        });*/

                break;
            case R.id.tv_name:
            case R.id.btn_arrow:
                ll_select_payer.setVisibility(View.VISIBLE);
                ll_preview.setVisibility(View.GONE);

               /* ll_select_payer.animate()
                        .translationY(view.getHeight())
                        .alpha(0.0f)
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                ll_select_payer.setVisibility(View.VISIBLE);
                            }
                        });
                ll_preview.animate()
                        .translationY(view.getHeight())
                        .alpha(0.0f)
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                ll_select_payer.setVisibility(View.GONE);
                            }
                        });*/
                break;
        }
    }


    /**
     * Collecting Data
     * Divided Player as per type
     */
    private void callAPI() {
        //API
        /**
         * Collecting data*/
        Log.d("API", "Get Player");
        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<PlayerWrapper> call = apiInterface.getPlayer(upComingMatchesDto.key_name);
        call.enqueue(new Callback<PlayerWrapper>() {
            @Override
            public void onResponse(Call<PlayerWrapper> call, Response<PlayerWrapper> response) {
                playerWrapper = response.body();

                Log.e("UpcomingMatchesAPI", playerWrapper.toString());
                if (playerWrapper.data.size() != 0) {
                    selectPlayerList(playerWrapper.data);
                } else {
                    DialogUtility.showMessageWithOk("Have no player", getActivity());
                }
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<PlayerWrapper> call, Throwable t) {
                Log.e("TAG", t.toString());
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });

    }

    /**
     * Divided Player as per type
     */
    private void selectPlayerList(ArrayList<PlayerDto> data) {
        //Name of Team a

        for (PlayerDto playerDto : data) {

            /** Divided  players*/
            switch (playerDto.seasonal_role) {
                case "bowler":
                    bowler.add(playerDto);
                    break;
                case "batsman":
                    batsman.add(playerDto);
                    break;
                case "allrounder":
                    allrounder.add(playerDto);
                    break;
                case "keeper":
                    keeper.add(playerDto);
                    break;
            }
        }
        System.out.println(" bowler: " + bowler.size() + " batsman: " + batsman.size() + " allrounder: " + allrounder.size() + " keeper: " + keeper.size());
        setAdapter();
    }

    /**
     * Set Adapter Type
     * Set Adapter Players
     */
    private void setAdapter() {

        /** Set Adapter Players*/

        /**Wicket Keeper*/
        wkAdapter = new WkAdapter(getActivity(), keeper, 0, totalPoints, totalPlayers, upComingMatchesDto.teama, upComingMatchesDto.teamb);
        wkAdapter.setOnButtonListener(new WkAdapter.OnButtonListener() {

            @Override
            public void onButtonClick(ArrayList<PlayerDto> playerDtoArrayList, float totalPlayerpoint, int totalPlayer) {
                keeper = playerDtoArrayList;
                totalPoints = totalPlayerpoint;
                totalPlayers = totalPlayer;
                tv_header.setText("" + totalPlayerpoint + "/1000\nCredit Left");
                tv_player_count.setText("" + totalPlayers + "/11\nPlayers");
                playerTypeAdapter.updateView(selectedplayerType, keeper);
            }
        });

        /**BatsMan*/
        batAdapter = new WkAdapter(getActivity(), batsman, 1, totalPoints, totalPlayers, upComingMatchesDto.teama, upComingMatchesDto.teamb);
        batAdapter.setOnButtonListener(new WkAdapter.OnButtonListener() {

            @Override
            public void onButtonClick(ArrayList<PlayerDto> playerDtoArrayList, float totalPlayerpoint, int totalPlayer) {
                batsman = playerDtoArrayList;
                totalPoints = totalPlayerpoint;
                totalPlayers = totalPlayer;
                tv_header.setText("" + totalPlayerpoint + "/1000\nCredit Left");
                tv_player_count.setText("" + totalPlayers + "/11\nPlayers");
                playerTypeAdapter.updateView(selectedplayerType, batsman);
            }
        });

        /**AllRounder*/
        arAdapter = new WkAdapter(getActivity(), allrounder, 2, totalPoints, totalPlayers, upComingMatchesDto.teama, upComingMatchesDto.teamb);
        arAdapter.setOnButtonListener(new WkAdapter.OnButtonListener() {

            @Override
            public void onButtonClick(ArrayList<PlayerDto> playerDtoArrayList, float totalPlayerpoint, int totalPlayer) {
                allrounder = playerDtoArrayList;
                totalPoints = totalPlayerpoint;
                totalPlayers = totalPlayer;
                tv_header.setText("" + totalPlayerpoint + "/1000\nCredit Left");
                tv_player_count.setText("" + totalPlayers + "/11\nPlayers");
                playerTypeAdapter.updateView(selectedplayerType, allrounder);
            }
        });
        /**AllRounder*/
        bowlAdapter = new WkAdapter(getActivity(), bowler, 3, totalPoints, totalPlayers, upComingMatchesDto.teama, upComingMatchesDto.teamb);
        bowlAdapter.setOnButtonListener(new WkAdapter.OnButtonListener() {

            @Override
            public void onButtonClick(ArrayList<PlayerDto> playerDtoArrayList, float totalPlayerpoint, int totalPlayer) {
                bowler = playerDtoArrayList;
                totalPoints = totalPlayerpoint;
                totalPlayers = totalPlayer;
                tv_header.setText("" + totalPlayerpoint + "/1000\nCredit Left");
                tv_player_count.setText("" + totalPlayers + "/11\nPlayers");
                playerTypeAdapter.updateView(selectedplayerType, bowler);
            }
        });

        /**Set Adapter Keeper*/

        rv_list.setAdapter(wkAdapter);


/** Set Adapter Type */
        playerTypeAdapter = new PlayerTypeAdapter(getActivity(), bowler, batsman, allrounder, keeper);
        rv_section.setAdapter(playerTypeAdapter);

        playerTypeAdapter.setOnButtonListener(new PlayerTypeAdapter.OnButtonListener() {
            @Override
            public void onButtonClick(int position) {
                selectedplayerType = position;
                switch (position) {
                    case 0:
                        rv_list.setAdapter(wkAdapter);
                        wkAdapter.updateTotalPoints(totalPoints, totalPlayers);
                        playerTypeAdapter.updateView(position, keeper);
                        break;
                    case 1:
                        rv_list.setAdapter(batAdapter);
                        batAdapter.updateTotalPoints(totalPoints, totalPlayers);
                        playerTypeAdapter.updateView(position, batsman);

                        break;
                    case 2:
                        rv_list.setAdapter(arAdapter);
                        arAdapter.updateTotalPoints(totalPoints, totalPlayers);
                        playerTypeAdapter.updateView(position, allrounder);
                        break;
                    case 3:
                        rv_list.setAdapter(bowlAdapter);
                        bowlAdapter.updateTotalPoints(totalPoints, totalPlayers);
                        playerTypeAdapter.updateView(position, bowler);
                        break;
                }
            }
        });

    }


    /**
     * Validation Testing
     */
    private boolean isMinimumPlayerSelected() {
        int total_keeper = getNoOfSelectedPlayers(keeper);
        int total_batsman = getNoOfSelectedPlayers(batsman);
        int total_allrounder = getNoOfSelectedPlayers(allrounder);
        int total_bowler = getNoOfSelectedPlayers(bowler);

        int tottal_player = total_keeper + total_batsman + total_allrounder + total_bowler;

        if (total_keeper < 1) {
            Toast.makeText(getActivity(), "You have to select minimum 1 Keeper", Toast.LENGTH_SHORT).show();
            return false;
        } else if (total_batsman < 3) {
            Toast.makeText(getActivity(), "You have to select minimum 3 Batsman", Toast.LENGTH_SHORT).show();
            return false;
        } else if (total_allrounder < 1) {
            Toast.makeText(getActivity(), "You have to select minimum 1  All Rounder", Toast.LENGTH_SHORT).show();
            return false;
        } else if (total_bowler < 2) {
            Toast.makeText(getActivity(), "You have to select minimum 2  Bowlers", Toast.LENGTH_SHORT).show();
            return false;
        } else if (tottal_player < 11) {
            Toast.makeText(getActivity(), "You have to select 11 Players", Toast.LENGTH_SHORT).show();
            return false;
        } else if (isMoreThen()) {
            Toast.makeText(getActivity(), "You can't select more then 7 player from a team.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean isMoreThen() {
        int no_ateam = 0, no_bteam = 0;
        selectedPlayer = getSelectedPlayers();
        for (PlayerDto playerDto : selectedPlayer) {
            if (playerDto.team_name.equals(upComingMatchesDto.teama)) {
                no_ateam++;
            } else {
                no_bteam++;
            }
        }
        Log.e("Team A:", "" + no_ateam);
        Log.e("Team B:", "" + no_bteam);
        if (no_ateam > 7 || no_bteam > 7) {
            System.out.println("return true");
            return true;
        }
        System.out.println("return false");
        return false;
    }

    private ArrayList<PlayerDto> getSelectedPlayers() {
        ArrayList<PlayerDto> selectedPalyers = new ArrayList<>();

//ADD Keeper
        boolean isFirstKeeper = true;
        for (PlayerDto playerDto : keeper) {
            if (playerDto.isSelected) {
                if (isFirstKeeper) {
                    playerDto.titleHeader = "WICKET-KEEPER";
                    isFirstKeeper = false;
                } else {
                    playerDto.titleHeader = "";
                }
                selectedPalyers.add(playerDto);
            }
        }

//ADD Batsman
        boolean isFirstBatsman = true;
        for (PlayerDto playerDto : batsman) {
            if (playerDto.isSelected) {
                if (isFirstBatsman) {
                    playerDto.titleHeader = "BATSMAN";
                    isFirstBatsman = false;
                } else {
                    playerDto.titleHeader = "";
                }
                selectedPalyers.add(playerDto);
            }
        }

//ADD All-Rounder
        boolean isFirstAllrounder = true;
        for (PlayerDto playerDto : allrounder) {
            if (playerDto.isSelected) {
                if (isFirstAllrounder) {
                    playerDto.titleHeader = "ALL-ROUNDER";
                    isFirstAllrounder = false;
                } else {
                    playerDto.titleHeader = "";
                }
                selectedPalyers.add(playerDto);
            }
        }

//ADD Bowler
        boolean isFirstBowler = true;
        for (PlayerDto playerDto : bowler) {
            if (playerDto.isSelected) {
                if (isFirstBowler) {
                    playerDto.titleHeader = "BOWLER";
                    isFirstBowler = false;
                } else {
                    playerDto.titleHeader = "";
                }
                selectedPalyers.add(playerDto);
            }
        }

        return selectedPalyers;
    }

    private int getNoOfSelectedPlayers(ArrayList<PlayerDto> playerDtoArrayList) {
        int i = 0;
        for (PlayerDto playerDto : playerDtoArrayList) {
            if (playerDto.isSelected) {
                i++;
            }
        }
        return i;
    }


}
