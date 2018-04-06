package com.perfect11.team_create;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.perfect11.R;
import com.perfect11.base.ApiClient;
import com.perfect11.base.ApiInterface;
import com.perfect11.base.BaseHeaderActivity;
import com.perfect11.contest.ChildFragment.AllContestFragment;
import com.perfect11.contest.ChildFragment.AllContestFragmentForActivity;
import com.perfect11.contest.ContestFragment;
import com.perfect11.contest.adapter.ContestListAdapter;
import com.perfect11.login_signup.RegisterActivity;
import com.perfect11.team_create.dto.ContestDto;
import com.perfect11.team_create.dto.ContestSubDto;
import com.perfect11.team_create.dto.PlayerDto;
import com.perfect11.team_create.dto.SelectedMatchDto;
import com.perfect11.team_create.wrapper.ContestWrapper;
import com.perfect11.upcoming_matches.dto.UpComingMatchesDto;
import com.utility.ActivityController;
import com.utility.DialogUtility;
import com.utility.customView.CustomTextView;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class ChooseContestActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    private ArrayList<PlayerDto> selectedTeam;
    private SelectedMatchDto selectedMatchDto;
    private UpComingMatchesDto upComingMatchesDto;
    private ApiInterface apiInterface;

    //Delete
    //private ContestListAdapter contestListAdapter;

    private CustomTextView ctv_time, ctv_country1, ctv_country2;
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
        setContentView(R.layout.choose_contest_layout);
        readFromBundle();
        initView();
        startUpdateTimer();
        callAPI();
    }

    private void readFromBundle() {
        selectedTeam = (ArrayList<PlayerDto>) getIntent().getExtras().getSerializable("selectedTeam");
        selectedMatchDto = (SelectedMatchDto) getIntent().getExtras().getSerializable("selectedMatchDto");
        upComingMatchesDto = (UpComingMatchesDto) getIntent().getExtras().getSerializable("upComingMatchesDto");
    }

    private void initView() {
        ctv_country1 = findViewById(R.id.ctv_country1);
        ctv_country2 = findViewById(R.id.ctv_country2);
        ctv_time = findViewById(R.id.ctv_time);
        String[] team = upComingMatchesDto.short_name.split(" ");
        String team1 = team[0];
        String team2 = team[2];
        ctv_country1.setText(team1);
        ctv_country2.setText(team2);

        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
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
                onBackPressed();
                break;
        }
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

    private void callAPI() {
        //API
        /*
         * Collecting data*/
        Log.d("API", "Get Player");
        final ProgressDialog mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<ArrayList<ContestDto>> call = apiInterface.getContestList(upComingMatchesDto.key_name);
        call.enqueue(new Callback<ArrayList<ContestDto>>() {
            @Override
            public void onResponse(Call<ArrayList<ContestDto>> call, Response<ArrayList<ContestDto>> response) {
                ArrayList<ContestDto> contestDtoArrayList = response.body();
                setupViewPager(viewPager,contestDtoArrayList);

                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ArrayList<ContestDto>> call, Throwable t) {
                Log.e("TAG", t.toString());
                if (t instanceof IOException) {
                    DialogUtility.showConnectionErrorDialogWithOk(ChooseContestActivity.this);
                    // logging probably not necessary
                } else {
                    Toast.makeText(ChooseContestActivity.this, "Conversion issue! big problems :(", Toast.LENGTH_SHORT).show();
                    // todo log to some central bug tracking service
                }
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }

    private void setupViewPager(ViewPager viewPager, ArrayList<ContestDto> contestDtoArrayList) {
        System.out.println("List");
        ArrayList<ContestDto> my_Contest = new ArrayList<>(),
                public_Contest = new ArrayList<>(),
                hattrick_Contest = new ArrayList<>(),
                bouncer_Contest = new ArrayList<>(),
                bowled_Contest = new ArrayList<>(),
                practice_Contest = new ArrayList<>();
        for(ContestDto contestDto:contestDtoArrayList)
        {
            switch(contestDto.tournament)
            {
                case "":
                    public_Contest.add(contestDto);
                    break;
                case "Hattrick":
                    hattrick_Contest.add(contestDto);
                    break;
                case "Bouncer":
                    bouncer_Contest.add(contestDto);
                    break;
                case "Bowled":
                    bowled_Contest.add(contestDto);
                    break;
                case "Practice":
                    practice_Contest.add(contestDto);
                    break;
            }
        }
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AllContestFragmentForActivity(this,contestDtoArrayList,upComingMatchesDto,selectedTeam), "All");
        adapter.addFragment(new AllContestFragmentForActivity(this,public_Contest,upComingMatchesDto,selectedTeam), "Public");
        adapter.addFragment(new AllContestFragmentForActivity(this,hattrick_Contest,upComingMatchesDto,selectedTeam), "Hattrick");
        adapter.addFragment(new AllContestFragmentForActivity(this,bouncer_Contest,upComingMatchesDto,selectedTeam), "Bouncer");
        adapter.addFragment(new AllContestFragmentForActivity(this,bowled_Contest,upComingMatchesDto,selectedTeam), "Bowled");
        adapter.addFragment(new AllContestFragmentForActivity(this,practice_Contest,upComingMatchesDto,selectedTeam), "Practice");
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
