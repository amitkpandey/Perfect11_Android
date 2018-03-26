package com.perfect11.contest;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.perfect11.R;
import com.perfect11.base.ApiClient;
import com.perfect11.base.ApiInterface;
import com.perfect11.base.BaseFragment;
import com.perfect11.base.BaseHeaderActivity;
import com.perfect11.contest.adapter.ContestWinnerAdapter;
import com.perfect11.contest.dto.ContestCallBackDto;
import com.perfect11.contest.dto.ContestWinnerDto;
import com.perfect11.login_signup.dto.InviteDto;
import com.perfect11.login_signup.dto.UserDto;
import com.perfect11.team_create.CreateTeamFragment;
import com.perfect11.team_create.SelectPlayersActivity;
import com.perfect11.team_create.wrapper.PlayerWrapper;
import com.perfect11.upcoming_matches.dto.UpComingMatchesDto;
import com.utility.CommonUtility;
import com.utility.DialogUtility;
import com.utility.PreferenceUtility;
import com.utility.customView.CustomButton;
import com.utility.customView.CustomEditText;
import com.utility.customView.CustomTextView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CreateContestFragment extends BaseFragment {
    private CustomTextView tv_match, tv_status, tv_amount;
    private CustomEditText et_name, et_winning_amount, et_contest_size, et_no_winners;
    private CheckBox  cb_allow_multiple_team;
    private ApiInterface apiInterface;
    private UpComingMatchesDto upComingMatchesDto;
    private LinearLayout ll_winner_details;
    private ContestWinnerAdapter contestWinnerAdapter=null;
    private ListView lv_winner;
    private CustomButton btn_set;
    private ArrayList<ContestWinnerDto> contestWinnerDtoArrayList = new ArrayList<>();
    private ArrayList<Float> winnerAmount=null;
    private ArrayList<Float> winnerPercent=null;
    public static CreateContestFragment newInstance() {
        return new CreateContestFragment();
    }
    private UserDto userDto;
    private int totalAmount=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.create_contest, container, false);
        setInnerHeader("Create Contest");
        readFromBundle();
        initView();
        setValues();
        return view;
    }

    private void readFromBundle() {
        upComingMatchesDto = (UpComingMatchesDto) getArguments().getSerializable("upComingMatchesDto");
        userDto = (UserDto) PreferenceUtility.getObjectInAppPreference(getActivity(), PreferenceUtility.APP_PREFERENCE_NAME);
    }

    private void initView() {
        tv_match = view.findViewById(R.id.tv_match);
        tv_status = view.findViewById(R.id.tv_status);
        et_name = view.findViewById(R.id.et_name);
        et_winning_amount = view.findViewById(R.id.et_winning_amount);
        et_contest_size = view.findViewById(R.id.et_contest_size);
        tv_amount = view.findViewById(R.id.tv_amount);

        //cb_allow_Customize = view.findViewById(R.id.cb_allow_Customize);
        cb_allow_multiple_team = view.findViewById(R.id.cb_allow_multiple_team);

        lv_winner = view.findViewById(R.id.lv_winner);
        ll_winner_details = view.findViewById(R.id.ll_winner_details);

        et_no_winners = view.findViewById(R.id.et_no_winners);
        btn_set = view.findViewById(R.id.btn_set);
    }

    private void setAdapter(int n_winner) {
        float parcentage = (100 / n_winner);
        float amount = Float.parseFloat(et_winning_amount.getText().toString().trim()) * (parcentage / 100);

        System.out.println(et_winning_amount.getText().toString() + "amount: " + amount);
        for (int i = 0; i < n_winner; i++) {
            ContestWinnerDto contestWinnerDto = new ContestWinnerDto();
            contestWinnerDto.amount = amount;
            contestWinnerDto.percentage = parcentage;
            contestWinnerDto.poistion = (i + 1);
            contestWinnerDtoArrayList.add(contestWinnerDto);
        }

        contestWinnerAdapter = new ContestWinnerAdapter(getActivity(), contestWinnerDtoArrayList,Float.parseFloat(et_winning_amount.getText().toString().trim()));
        lv_winner.setAdapter(contestWinnerAdapter);

        CommonUtility.setListViewHeightBasedOnChildren(lv_winner);
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
                        int percentage = (int) (amount * 15f / 100f);
                        int total = (amount + percentage) / contest;
                        System.out.println("percentage " + percentage);
                        System.out.println("total " + total);
                        totalAmount=total;
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
                        totalAmount=total;
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
                if (et_name.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Enter Contest Name", Toast.LENGTH_SHORT).show();
                    et_name.requestFocus();
                } else if (et_winning_amount.getText().toString().equalsIgnoreCase("") || Integer.parseInt(et_winning_amount.getText().toString()) < 0) {
                    Toast.makeText(getActivity(), "Enter Winning Amount", Toast.LENGTH_SHORT).show();
                    et_winning_amount.requestFocus();
                } else if (et_contest_size.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Enter Contest Size", Toast.LENGTH_SHORT).show();
                    et_contest_size.requestFocus();
                } else if (Integer.parseInt(et_contest_size.getText().toString()) <= 1) {
                    Toast.makeText(getActivity(), "Contest Size should be minimum 2", Toast.LENGTH_SHORT).show();
                    et_contest_size.requestFocus();
                }else if (et_no_winners.getText().toString().trim().equals("")) {
                    Toast.makeText(getActivity(), "Enter number of winners.", Toast.LENGTH_SHORT).show();
                    et_no_winners.requestFocus();
                } else if (Integer.parseInt(et_no_winners.getText().toString().trim()) == 0) {
                    Toast.makeText(getActivity(), "Number of winners should not be 0", Toast.LENGTH_SHORT).show();
                    et_no_winners.requestFocus();
                } else if (Integer.parseInt(et_no_winners.getText().toString().trim()) >= Integer.parseInt(et_contest_size.getText().toString().trim())) {
                    Toast.makeText(getActivity(), "Number of winners should not be greater then contest size", Toast.LENGTH_SHORT).show();
                    et_no_winners.requestFocus();
                }else if(contestWinnerAdapter==null)
                {
                    Toast.makeText(getActivity(), "Set Number of winner", Toast.LENGTH_SHORT).show();
                }else if(checkArray())
                {
                    callAPI();
                }
                break;
            case R.id.btn_set:
                if(isValidData()) {
                    if (et_no_winners.getText().toString().trim().equals("")) {
                        Toast.makeText(getActivity(), "Enter number of winners.", Toast.LENGTH_SHORT).show();
                        et_no_winners.requestFocus();
                    } else if (Integer.parseInt(et_no_winners.getText().toString().trim()) == 0) {
                        Toast.makeText(getActivity(), "Number of winners should not be 0", Toast.LENGTH_SHORT).show();
                        et_no_winners.requestFocus();
                    } else if (Integer.parseInt(et_no_winners.getText().toString().trim()) >= Integer.parseInt(et_contest_size.getText().toString().trim())) {
                        Toast.makeText(getActivity(), "Number of winners should not be greater then contest size", Toast.LENGTH_SHORT).show();
                        et_no_winners.requestFocus();
                    } else {
                        lv_winner.setVisibility(View.VISIBLE);
                        setAdapter(Integer.parseInt(et_no_winners.getText().toString().trim()));
                    }
                }
                break;
        }
    }

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

        Call<ContestCallBackDto> call = apiInterface.createContest(Integer.parseInt(et_contest_size.getText().toString()),1,
                totalAmount,cb_allow_multiple_team.isChecked()?1:0,
                upComingMatchesDto.key_name,et_name.getText().toString(), winnerAmount,winnerPercent,
                Integer.parseInt(et_no_winners.getText().toString()),
                Integer.parseInt(et_winning_amount.getText().toString().trim()),
                userDto.member_id,userDto.reference_id
                );
        call.enqueue(new Callback<ContestCallBackDto>() {
            @Override
            public void onResponse(Call<ContestCallBackDto> call, Response<ContestCallBackDto> response) {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                ContestCallBackDto contestCallBackDto=response.body();

                ContestInviteFragment contestInviteFragment=new ContestInviteFragment();
                Bundle bundle=new Bundle();
                bundle.putSerializable("contestCallBackDto",contestCallBackDto);
                contestInviteFragment.setArguments(bundle);
                ((BaseHeaderActivity)getActivity()).addFragment(contestInviteFragment,false,ContestInviteFragment.class.getName());
            }

            @Override
            public void onFailure(Call<ContestCallBackDto> call, Throwable t) {
                Log.e("TAG", t.toString());
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                DialogUtility.showMessageWithOk(t.getMessage(),getActivity());
            }
        });

    }

    private boolean checkArray() {

        winnerAmount=new ArrayList<>();
        winnerPercent=new ArrayList<>();

    contestWinnerDtoArrayList=contestWinnerAdapter.getArray();
    float amount=0;
    for(int i=0;i<contestWinnerDtoArrayList.size();i++)
    {
        winnerAmount.add(contestWinnerDtoArrayList.get(i).amount);
        winnerPercent.add(contestWinnerDtoArrayList.get(i).percentage);
        amount+=contestWinnerDtoArrayList.get(i).amount;
       if(i!=0)
       {
           if(contestWinnerDtoArrayList.get(i).amount>contestWinnerDtoArrayList.get(i-1).amount)
           {Toast.makeText(getActivity(), "Winner \"+i+\" should get more then winner "+(i+1), Toast.LENGTH_SHORT).show();
               return false;
           }
       }
    }


    if(amount>Integer.parseInt(et_winning_amount.getText().toString()))
    {
        Toast.makeText(getActivity(), "You crossed winner's amount limit.\n Winner amount="+Integer.parseInt(et_winning_amount.getText().toString())+"\n Used="+amount, Toast.LENGTH_SHORT).show();
        return false;
    }

        if(amount<Integer.parseInt(et_winning_amount.getText().toString()))
        {
            Toast.makeText(getActivity(), "You did not use all amount of Winners.\n Winner amount="+Integer.parseInt(et_winning_amount.getText().toString())+"\n Used="+amount, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean isValidData() {
            if (et_name.getText().toString().equalsIgnoreCase("")) {
                Toast.makeText(getActivity(), "Enter Contest Name", Toast.LENGTH_SHORT).show();
                et_name.requestFocus();
                return false;
            } else if (et_winning_amount.getText().toString().equalsIgnoreCase("") || Integer.parseInt(et_winning_amount.getText().toString()) < 0) {
                Toast.makeText(getActivity(), "Enter Winning Amount", Toast.LENGTH_SHORT).show();
                et_winning_amount.requestFocus();
                return false;
            } else if (Integer.parseInt(et_winning_amount.getText().toString()) <= 0) {
                Toast.makeText(getActivity(), "Winning Amount should not be 0", Toast.LENGTH_SHORT).show();
                et_winning_amount.requestFocus();
                return false;
            } else if (et_contest_size.getText().toString().equalsIgnoreCase("")) {
                Toast.makeText(getActivity(), "Enter Contest Size", Toast.LENGTH_SHORT).show();
                et_contest_size.requestFocus();
                return false;
            } else if (Integer.parseInt(et_contest_size.getText().toString()) <= 1) {
                Toast.makeText(getActivity(), "Contest Size should be minimum 2", Toast.LENGTH_SHORT).show();
                et_contest_size.requestFocus();
                return false;
            } else {
                //lv_winner.setVisibility(View.VISIBLE);
                et_name.setEnabled(false);
                et_winning_amount.setEnabled(false);
                et_contest_size.setEnabled(false);
                return true;
            }
    }
}
