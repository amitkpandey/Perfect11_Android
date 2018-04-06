package com.perfect11.contest;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.perfect11.R;
import com.perfect11.base.ApiClient;
import com.perfect11.base.ApiInterface;
import com.perfect11.base.BaseFragment;
import com.perfect11.base.BaseHeaderActivity;
import com.perfect11.contest.ChildFragment.AllContestFragment;
import com.perfect11.contest.adapter.ContestListAdapter;
import com.perfect11.contest.wrapper.JoinedContestWrapper;
import com.perfect11.contest.wrapper.TeamWrapper;
import com.perfect11.home.HomeFragment;
import com.perfect11.home.childFragments.FixturesFragment;
import com.perfect11.home.childFragments.LiveFragment;
import com.perfect11.home.childFragments.ResultsFragment;
import com.perfect11.login_signup.dto.UserDto;
import com.perfect11.team_create.CreateTeamFragment;
import com.perfect11.team_create.SelectPlayersFragment;
import com.perfect11.team_create.adapter.ContestAdapter;
import com.perfect11.team_create.dto.ContestDto;
import com.perfect11.team_create.dto.ContestSubDto;
import com.perfect11.team_create.wrapper.ContestWrapper;
import com.perfect11.upcoming_matches.dto.UpComingMatchesDto;
import com.utility.DialogUtility;
import com.utility.PreferenceUtility;
import com.utility.customView.CustomButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class ContestFragment extends BaseFragment {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    private ApiInterface apiInterface;
    private UpComingMatchesDto upComingMatchesDto;


    private UserDto userDto;

    private ArrayList<ContestDto> contestDtoArrayList;
    private JoinedContestWrapper joinedContestWrapper;
    private TeamWrapper teamWrapper;

    private CustomButton btn_my_team, btn_join_contest;
    private int team_size = 0;

    private ProgressDialog mProgressDialog;

    public static ContestFragment newInstance() {
        return new ContestFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.contest_layout, container, false);
        userDto = (UserDto) PreferenceUtility.getObjectInAppPreference(getActivity(), PreferenceUtility.APP_PREFERENCE_NAME);
        setInnerHeader("Contest");
        initView();
        readFromBundle();
        return view;
    }

    private void initView() {
        btn_join_contest = view.findViewById(R.id.btn_join_contest);
        btn_my_team = view.findViewById(R.id.btn_my_team);

        viewPager = view.findViewById(R.id.view_pager);
        ((BaseHeaderActivity) getActivity()).slideMenu.addIgnoredView(viewPager);
       tabLayout = view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void readFromBundle() {
        upComingMatchesDto = (UpComingMatchesDto) getArguments().getSerializable("upComingMatchesDto");

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        callAPI();
    }

    private void callAPI() {
        mProgressDialog.show();

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ArrayList<ContestDto>> call = apiInterface.getContestList(upComingMatchesDto.key_name);

        call.enqueue(new Callback<ArrayList<ContestDto>>() {
            @Override
            public void onResponse(Call<ArrayList<ContestDto>> call, Response<ArrayList<ContestDto>> response) {
                contestDtoArrayList = response.body();
                Log.e("UpcomingMatchesAPI", contestDtoArrayList.toString());
                    callMyJoinedContestApi();
            }

            @Override
            public void onFailure(Call<ArrayList<ContestDto>> call, Throwable t) {
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

    private void callMyJoinedContestApi() {
        Log.d("API", "MyJoinedContestApi");

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<JoinedContestWrapper> call = apiInterface.getUserContest(upComingMatchesDto.key_name, userDto.member_id);
        call.enqueue(new Callback<JoinedContestWrapper>() {
            @Override
            public void onResponse(Call<JoinedContestWrapper> call, Response<JoinedContestWrapper> response) {
                joinedContestWrapper = response.body();
                callMyJoinedTeamApi();
            }

            @Override
            public void onFailure(Call<JoinedContestWrapper> call, Throwable t) {
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

    private void callMyJoinedTeamApi() {
        Log.d("API", "Get MyJoinedTeamApi");

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<TeamWrapper> teamWrapperCall = apiInterface.getTeamList(upComingMatchesDto.key_name, userDto.member_id);

        teamWrapperCall.enqueue(new Callback<TeamWrapper>() {
            @Override
            public void onResponse(Call<TeamWrapper> call, Response<TeamWrapper> response) {
                teamWrapper = response.body();
                team_size = teamWrapper.data.size();


               // setAdapter(contestDtoArrayList);
                setupViewPager(viewPager,contestDtoArrayList);
                if (joinedContestWrapper.data != null && joinedContestWrapper.data.size() > 0)
                    btn_join_contest.setText("Joined Contests (" + joinedContestWrapper.data.size() + ")");
                else
                    btn_join_contest.setText("Joined Contests");

                if (teamWrapper.data != null && teamWrapper.data.size() > 0)
                    btn_my_team.setText("My Team (" + teamWrapper.data.size() + ")");
                else
                    btn_my_team.setText("My Team");
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<TeamWrapper> call, Throwable t) {
                Log.e("TAG", t.toString());
                if (t instanceof IOException) {
                    DialogUtility.showConnectionErrorDialogWithOk(getActivity());
                }
                else {
                    Toast.makeText(getActivity(), "Conversion issue! big problems :(", Toast.LENGTH_SHORT).show();
                }
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }

    public void onButtonClick(View view) {
        super.onButtonClick(view);
        Bundle bundle;
        String[] team = upComingMatchesDto.short_name.split(" ");
        String team1 = team[0].trim();
        String team2 = team[2].trim();

        switch (view.getId()) {
            case R.id.img_back:
                getActivity().onBackPressed();
                break;
            case R.id.btn_create_contest:
                if (team_size == 0) {
                    DialogUtility.showMessageWithOk("To Create a contest, you have to create a team", getActivity());
                } else {
                    bundle = new Bundle();
                    bundle.putSerializable("upComingMatchesDto", upComingMatchesDto);
                    bundle.putSerializable("joinedContestDto", joinedContestWrapper.data);
                    CreateContestFragment createContestFragment = CreateContestFragment.newInstance();
                    createContestFragment.setArguments(bundle);
                    ((BaseHeaderActivity) getActivity()).addFragment(createContestFragment, true, CreateContestFragment.class.getName());
                }
                break;
            case R.id.btn_join_contest:
                if (joinedContestWrapper.data != null && joinedContestWrapper.data.size() > 0) {
                    bundle = new Bundle();
                    bundle.putBoolean("isFixture", true);
                    bundle.putString("team1", team1);
                    bundle.putString("team2", team2);
                    bundle.putString("teamA", upComingMatchesDto.teama);
                    bundle.putString("teamB", upComingMatchesDto.teamb);
                    bundle.putString("matchStatus", upComingMatchesDto.matchstatus);
                    bundle.putSerializable("joinedContestDto", joinedContestWrapper.data);
                    JoinContestFragment joinContestFragment = JoinContestFragment.newInstance();
                    joinContestFragment.setArguments(bundle);
                    ((BaseHeaderActivity) getActivity()).addFragment(joinContestFragment, true, JoinContestFragment.class.getName());
                }
                break;
            case R.id.btn_my_team:
                if (teamWrapper.data != null && teamWrapper.data.size() > 0) {
                    bundle = new Bundle();
                    bundle.putString("team1", team1);
                    bundle.putString("team2", team2);
                    bundle.putSerializable("upComingMatchesDto", upComingMatchesDto);
                    bundle.putSerializable("teamDto", teamWrapper.data);
                    CreateTeamFragment createTeamFragment = CreateTeamFragment.newInstance();
                    createTeamFragment.setArguments(bundle);
                    ((BaseHeaderActivity) getActivity()).addFragment(createTeamFragment, true, CreateTeamFragment.class.getName());
                } else {
                    SelectPlayersFragment selectPlayersFragment = new SelectPlayersFragment();
                    Bundle bundle1 = new Bundle();
                    bundle1.putSerializable("upComingMatchesDto", upComingMatchesDto);
                    selectPlayersFragment.setArguments(bundle1);
                    ((BaseHeaderActivity) getActivity()).addFragment(selectPlayersFragment, true, SelectPlayersFragment.class.getName());

                }
                break;
        }
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
            if(contestDto.created_by.trim().equals(userDto.member_id.trim()))
            {
                my_Contest.add(contestDto);
            }
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
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new AllContestFragment(getActivity(),contestDtoArrayList,upComingMatchesDto,teamWrapper), "All");
        adapter.addFragment(new AllContestFragment(getActivity(),my_Contest,upComingMatchesDto,teamWrapper), "My Contest");
        adapter.addFragment(new AllContestFragment(getActivity(),public_Contest,upComingMatchesDto,teamWrapper), "Public");
        adapter.addFragment(new AllContestFragment(getActivity(),hattrick_Contest,upComingMatchesDto,teamWrapper), "Hattrick");
        adapter.addFragment(new AllContestFragment(getActivity(),bouncer_Contest,upComingMatchesDto,teamWrapper), "Bouncer");
        adapter.addFragment(new AllContestFragment(getActivity(),bowled_Contest,upComingMatchesDto,teamWrapper), "Bowled");
        adapter.addFragment(new AllContestFragment(getActivity(),practice_Contest,upComingMatchesDto,teamWrapper), "Practice");
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
