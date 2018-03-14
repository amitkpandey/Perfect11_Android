package com.perfect11.home;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.perfect11.R;
import com.perfect11.base.ApiClient;
import com.perfect11.base.ApiInterface;
import com.perfect11.base.BaseFragment;
import com.perfect11.base.BaseHeaderActivity;
import com.perfect11.home.childFragments.FixturesFragment;
import com.perfect11.home.childFragments.LiveFragment;
import com.perfect11.home.childFragments.ResultsFragment;
import com.perfect11.home.dto.JoinContestCallBackDto;
import com.perfect11.home.wrapper.CreateTeamCallBackWrapper;
import com.perfect11.login_signup.dto.UserDto;
import com.perfect11.team_create.dto.ContestDto;
import com.perfect11.team_create.dto.PlayerDto;
import com.perfect11.upcoming_matches.dto.UpComingMatchesDto;
import com.utility.DialogUtility;
import com.utility.PreferenceUtility;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends BaseFragment {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private boolean flag;
    private ContestDto contestDto;
    private ArrayList<PlayerDto> selectedTeam;
    private UpComingMatchesDto upComingMatchesDto;
    private ApiInterface apiInterface;

    private int bowler_size, batsman_size, allrounder_size, keeper_size;
    private String[] bowler;
    private String[] batsman;
    private String[] allrounder;
    private String[] keeper;

//    private String bowler = "";
//    private String batsman = "";
//    private String allrounder = "";
//    private String keeper = "";

    private String captain;
    private String vcaptain;
    private UserDto userDto;


    private float player_amount_count = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_home, container, false);
        setHeader("All Matches");
        initView();
        readFromBundle();
        return view;
    }

    public static Fragment newInstance() {
        return new HomeFragment();
    }

    private void readFromBundle() {
        userDto = (UserDto) PreferenceUtility.getObjectInAppPreference(getActivity(), PreferenceUtility.APP_PREFERENCE_NAME);
        try {
            flag = getArguments().getBoolean("flag");
            contestDto = (ContestDto) getArguments().getSerializable("contestDto");
            selectedTeam = (ArrayList<PlayerDto>) getArguments().getSerializable("selectedTeam");
            upComingMatchesDto = (UpComingMatchesDto) getArguments().getSerializable("upComingMatchesDto");
            System.out.println(contestDto.toString());
//            Log.e("BaseHeaderActivity:", contestDto.toString() + upComingMatchesDto.toString() + selectedTeam.size() + "" + flag);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (flag) {
            final Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.custom_dialog_conformation);
            dialog.show();
            TextView header = dialog.findViewById(R.id.ctv_title);
            TextView content = dialog.findViewById(R.id.ctv_body);
            Button cbtn_join = dialog.findViewById(R.id.cbtn_join);
            Button cbtn_cancel = dialog.findViewById(R.id.cbtn_cancel);
            cbtn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            cbtn_join.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                    callCreateTeamAPI();
                    dialog.dismiss();
                }
            });

        }
    }

    private void callCreateTeamAPI() {
        //API

        Log.d("API", "Create Team");
        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();

        getSizeOfArray(selectedTeam);
        bowler = new String[bowler_size];
        batsman = new String[batsman_size];
        allrounder = new String[allrounder_size];
        keeper = new String[keeper_size];
        selectPlayerList(selectedTeam);

//        System.out.println("allRounder " + Arrays.toString(allrounder));
//        System.out.println("batsman " + Arrays.toString(batsman));
//        System.out.println("bowler " + Arrays.toString(bowler));
//        System.out.println("keeper " + Arrays.toString(keeper));
        ArrayList<String> batsmanList = new ArrayList<>();
        ArrayList<String> allRounderList = new ArrayList<>();
        ArrayList<String> bowlerList = new ArrayList<>();
        ArrayList<String> keeperList = new ArrayList<>();
        for (String batsman : batsman) {
            batsmanList.add(batsman);
        }
        for (String allRounder : allrounder) {
            allRounderList.add(allRounder);
        }
        for (String bowler : bowler) {
            bowlerList.add(bowler);
        }
        for (String keeper : keeper) {
            keeperList.add(keeper);
        }
        Call<CreateTeamCallBackWrapper> call = apiInterface.createTeamAPI(batsmanList, allRounderList, bowlerList, keeperList, captain,
                player_amount_count, upComingMatchesDto.key_name, vcaptain, userDto.member_id);
        call.enqueue(new Callback<CreateTeamCallBackWrapper>() {
            @Override
            public void onResponse(Call<CreateTeamCallBackWrapper> call, Response<CreateTeamCallBackWrapper> response) {
                CreateTeamCallBackWrapper callBackDto = response.body();

                Log.e("CreateTeamCallBack", callBackDto.toString());
                if (callBackDto.status) {
                    Log.e("CreateTeamCallBack", callBackDto.message);
                    callAPIJoinContest(callBackDto.data.team_id);
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

    private void callAPIJoinContest(int team_id) {
        //API
        Log.d("API", "Join Contest");
        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();

        Call<JoinContestCallBackDto> call = apiInterface.joinContest(contestDto.id, userDto.reference_id, userDto.member_id, upComingMatchesDto.key_name);
        call.enqueue(new Callback<JoinContestCallBackDto>() {
            @Override
            public void onResponse(Call<JoinContestCallBackDto> call, Response<JoinContestCallBackDto> response) {
                JoinContestCallBackDto callBackDto = response.body();

                Log.e("UpcomingMatchesAPI", callBackDto.toString());
                if (callBackDto.status) {
                    Log.e("UpcomingMatchesAPI", callBackDto.message);
                    DialogUtility.showMessageWithOk(callBackDto.message, getActivity());
                } else {
                    DialogUtility.showMessageWithOk(callBackDto.message, getActivity());
                }
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<JoinContestCallBackDto> call, Throwable t) {
                Log.e("TAG", t.toString());
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }

    private void selectPlayerList(ArrayList<PlayerDto> data) {
        int bowler_i, batsman_i, allrounder_i, keeper_i;
        bowler_i = 0;
        batsman_i = 0;
        allrounder_i = 0;
        keeper_i = 0;

        for (PlayerDto playerDto : data) {
            player_amount_count = player_amount_count + Float.parseFloat(playerDto.credit);
            /** Divided  players*/
            switch (playerDto.seasonal_role) {
                case "bowler":
                    bowler[bowler_i] = playerDto.short_name;
                    bowler_i++;
                    break;
                case "batsman":
                    batsman[batsman_i] = playerDto.short_name;
                    batsman_i++;
                    break;
                case "allrounder":

                    allrounder[allrounder_i] = playerDto.short_name;
                    allrounder_i++;
                    break;
                case "keeper":
                    keeper[keeper_i] = playerDto.short_name;
                    keeper_i++;
                    break;
            }
            if (playerDto.isC) {
                captain = playerDto.short_name;
            }

            if (playerDto.isCV) {
                vcaptain = playerDto.short_name;
            }
        }
    }

    private void getSizeOfArray(ArrayList<PlayerDto> data) {

        bowler_size = 0;
        batsman_size = 0;
        allrounder_size = 0;
        keeper_size = 0;
        for (PlayerDto playerDto : data) {

            /* Divided  players*/
            switch (playerDto.seasonal_role) {
                case "bowler":
                    bowler_size++;
                    break;
                case "batsman":
                    batsman_size++;
                    break;
                case "allrounder":
                    allrounder_size++;
                    break;
                case "keeper":
                    keeper_size++;
                    break;
            }
            if (playerDto.isC) {
                captain = playerDto.short_name;
            }

            if (playerDto.isCV) {
                vcaptain = playerDto.short_name;
            }
        }
    }

    private void initView() {
        viewPager = view.findViewById(R.id.view_pager);
        ((BaseHeaderActivity) getActivity()).slideMenu.addIgnoredView(viewPager);
        tabLayout = view.findViewById(R.id.tabs);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        System.out.println("List");
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new FixturesFragment(), "Fixtures");
        adapter.addFragment(new LiveFragment(), "Live");
        adapter.addFragment(new ResultsFragment(), "Results");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
