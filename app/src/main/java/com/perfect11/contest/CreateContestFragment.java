package com.perfect11.contest;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.perfect11.R;
import com.perfect11.base.ApiInterface;
import com.perfect11.base.BaseFragment;
import com.perfect11.base.BaseHeaderActivity;
import com.perfect11.login_signup.dto.UserDto;
import com.perfect11.team_create.CreateTeamFragment;
import com.perfect11.upcoming_matches.dto.UpComingMatchesDto;
import com.utility.PreferenceUtility;
import com.utility.customView.CustomEditText;
import com.utility.customView.CustomTextView;


public class CreateContestFragment extends BaseFragment {
    private CustomTextView tv_match, tv_status, tv_amount;
    private CustomEditText et_name, et_winning_amount, et_contest_size;
    private CheckBox cb_allow;
    private ApiInterface apiInterface;
    private UpComingMatchesDto upComingMatchesDto;
    private UserDto userDto;

    public static CreateContestFragment newInstance() {
        return new CreateContestFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.create_contest, container, false);
        userDto = (UserDto) PreferenceUtility.getObjectInAppPreference(getActivity(), PreferenceUtility.APP_PREFERENCE_NAME);
        setInnerHeader("Create Contest");
        readFromBundle();
        initView();
        setValues();
        return view;
    }

    private void readFromBundle() {
        upComingMatchesDto = (UpComingMatchesDto) getArguments().getSerializable("upComingMatchesDto");
    }

    private void initView() {
        tv_match = view.findViewById(R.id.tv_match);
        tv_status = view.findViewById(R.id.tv_status);
        et_name = view.findViewById(R.id.et_name);
        et_winning_amount = view.findViewById(R.id.et_winning_amount);
        et_contest_size = view.findViewById(R.id.et_contest_size);
        tv_amount = view.findViewById(R.id.tv_amount);
        cb_allow = view.findViewById(R.id.cb_allow);
    }

    private void setValues() {
        tv_match.setText(upComingMatchesDto.teama + " vs " + upComingMatchesDto.teamb);
        tv_status.setText(upComingMatchesDto.matchstatus);
        et_contest_size.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (!et_contest_size.getText().toString().equalsIgnoreCase("") ||
                            !et_winning_amount.getText().toString().equalsIgnoreCase("")) {
                        int contest = Integer.parseInt(et_contest_size.getText().toString().trim());
                        int amount = Integer.parseInt(et_winning_amount.getText().toString().trim());
                        int percentage = (int) (amount * 50f / 100f);
                        int total = (amount + percentage) / contest;
                        System.out.println("percentage " + percentage);
                        System.out.println("total " + total);
                        tv_amount.setText(getActivity().getResources().getString(R.string.Rs) + " " + total);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_winning_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (!et_contest_size.getText().toString().equalsIgnoreCase("") ||
                            !et_winning_amount.getText().toString().equalsIgnoreCase("")) {
                        int contest = Integer.parseInt(et_contest_size.getText().toString().trim());
                        int amount = Integer.parseInt(et_winning_amount.getText().toString().trim());
                        int percentage = (int) (amount * 50f / 100f);
                        int total = (amount + percentage) / contest;
                        System.out.println("percentage " + percentage);
                        System.out.println("total " + total);
                        tv_amount.setText(getActivity().getResources().getString(R.string.Rs) + " " + total);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onButtonClick(View view) {
        super.onButtonClick(view);
        switch (view.getId()) {
            case R.id.img_back:
                getActivity().onBackPressed();
                break;
            case R.id.btn_create:
                if (!et_winning_amount.getText().toString().equalsIgnoreCase("0")) {

                }
                ((BaseHeaderActivity) getActivity()).addFragment(CreateTeamFragment.newInstance(), true, CreateTeamFragment.class.getName());
                break;
        }
    }
}
