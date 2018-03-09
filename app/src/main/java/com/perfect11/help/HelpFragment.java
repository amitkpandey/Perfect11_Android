package com.perfect11.help;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.perfect11.R;
import com.perfect11.base.BaseFragment;
import com.perfect11.base.BaseHeaderActivity;
import com.perfect11.help.childFragments.BattingFragment;
import com.perfect11.home.childFragments.FixturesFragment;
import com.perfect11.home.childFragments.LiveFragment;
import com.perfect11.home.childFragments.ResultsFragment;

import java.util.ArrayList;
import java.util.List;

public class HelpFragment extends BaseFragment {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
        view=inflater.inflate(R.layout.fragment_help,container,false);
        setHeader("Help");
        initView();
        return view;
    }

    public static HelpFragment newInstance() {
        return new HelpFragment();
    }

    private void initView() {
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        ((BaseHeaderActivity)getActivity()).slideMenu.addIgnoredView(viewPager);
        tabLayout=view.findViewById(R.id.tabs);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getBackground();
    }

    private void setupViewPager(ViewPager viewPager) {
        System.out.println("List");
        HelpViewPagerAdapter help_adapter = new HelpViewPagerAdapter(getChildFragmentManager());
        help_adapter.addFragment(new BattingFragment(), "Batting");
        help_adapter.addFragment(new BattingFragment(), "Balling");
        help_adapter.addFragment(new BattingFragment(), "Fielding");
        help_adapter.addFragment(new BattingFragment(), "Others");
        help_adapter.addFragment(new BattingFragment(), "Economy Rate");
        help_adapter.addFragment(new BattingFragment(), "Strike Rate");
        viewPager.setAdapter(help_adapter);
    }

    class HelpViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public HelpViewPagerAdapter(FragmentManager manager) {
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
