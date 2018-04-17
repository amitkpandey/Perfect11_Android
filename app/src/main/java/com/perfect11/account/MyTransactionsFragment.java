package com.perfect11.account;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.perfect11.R;
import com.perfect11.account.dto.MyTransectionDto;
import com.perfect11.account.wrapper.MyTransectionWrapper;
import com.perfect11.base.ApiClient;
import com.perfect11.base.ApiInterface;
import com.perfect11.base.BaseFragment;
import com.perfect11.base.BaseHeaderActivity;
import com.perfect11.contest.ContestFragment;
import com.perfect11.contest.adapter.MyTransactionsAdapter;
import com.perfect11.login_signup.dto.UserDto;
import com.utility.DialogUtility;
import com.utility.PreferenceUtility;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyTransactionsFragment extends BaseFragment {

    public static MyTransactionsFragment newInstance() {
        return new MyTransactionsFragment();
    }

    private RecyclerView rv_transactions;
    private LinearLayoutManager layoutManager;
    private ApiInterface apiInterface;
    private UserDto userDto;
    private MyTransactionsAdapter myTransactionsAdapter;
    private boolean isScrolling = false;
    private int currentIteam, totalIteams, scrollOutIteams, page, listcount = 0;

    private ProgressBar progress;
    private ArrayList<MyTransectionDto> shopkeeperlist;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.my_transactions_layout, container, false);
        setInnerHeader("My Transactions");
        initView();
        page = 0;
        callAPI(page);
        return view;
    }


    private void initView() {
        userDto = (UserDto) PreferenceUtility.getObjectInAppPreference(getActivity(), PreferenceUtility.APP_PREFERENCE_NAME);

        progress = view.findViewById(R.id.progress);
        rv_transactions = view.findViewById(R.id.rv_transactions);
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_transactions.setLayoutManager(layoutManager);
        setScrolling();
    }


    private void setScrolling() {
        rv_transactions.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                scrollOutIteams = layoutManager.findFirstVisibleItemPosition();
                Log.e("Abcd", "Scrolling" + scrollOutIteams);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentIteam = layoutManager.getChildCount();
                totalIteams = layoutManager.getItemCount();
                scrollOutIteams = layoutManager.findFirstVisibleItemPosition();
                Log.e("Abcd", "currentIteam:" + currentIteam + " totalIteams: " + totalIteams + " listcount: " + listcount + " scrollOutIteams:" + scrollOutIteams);
                if (isScrolling && (currentIteam + scrollOutIteams == totalIteams)) {
                    page++;
                    isScrolling = false;
                    if (totalIteams < listcount) {
                        callAPI(page);
                    }

                    //Fatch Data
                }
            }
        });
    }

    private void callAPI(final int page) {
        //API
        /**
         * Collecting data*/
        Log.d("API", "Get Player");
        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        if (page == 0) {
            mProgressDialog.show();
        } else {
            progress.setVisibility(View.VISIBLE);
        }

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<MyTransectionWrapper> call = apiInterface.myTransactionList(userDto.member_id, page, 20);
        call.enqueue(new Callback<MyTransectionWrapper>() {
            @Override
            public void onResponse(Call<MyTransectionWrapper> call, Response<MyTransectionWrapper> response) {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();

                if (progress.getVisibility() == View.VISIBLE)
                    progress.setVisibility(View.GONE);

                if (page == 0) {
                    shopkeeperlist = null;
                    shopkeeperlist = response.body().data.shopkeeperlist;
                    listcount = response.body().data.listcount;
                    myTransactionsAdapter = new MyTransactionsAdapter(shopkeeperlist, getActivity());
                    rv_transactions.setAdapter(myTransactionsAdapter);
                   /* myTransactionsAdapter.setOnButtonListener(new MyTransactionsAdapter.OnButtonListener() {
                        @Override
                        public void onButtonClick(MyTransectionDto myTransectionDto) {
                            TextView tv_Details,tv_Applied_For,tv_Amount,tv_title;

                            final Dialog dialog = new Dialog(getActivity());
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setCancelable(false);
                            dialog.setContentView(R.layout.dialog_transaction_details);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            Button cbtn_ok = dialog.findViewById(R.id.btn_ok);
                            tv_Amount=dialog.findViewById(R.id.tv_Amount);
                            tv_Details=dialog.findViewById(R.id.tv_Details);
                            tv_Applied_For=dialog.findViewById(R.id.tv_Applied_For);
                            tv_title=dialog.findViewById(R.id.tv_title);

                            tv_Amount.setText(myTransectionDto.amount);
                            tv_Details.setText(myTransectionDto.description);
                            tv_Applied_For.setText(myTransectionDto.applied_for);
                            tv_title.setText(myTransectionDto.created_date);

                            cbtn_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                        }
                    });*/
                } else {
                    shopkeeperlist.addAll(response.body().data.shopkeeperlist);
                    myTransactionsAdapter.setData(shopkeeperlist);
                }
            }

            @Override
            public void onFailure(Call<MyTransectionWrapper> call, Throwable t) {

                if (progress.getVisibility() == View.VISIBLE)
                    progress.setVisibility(View.GONE);
                if (t instanceof IOException) {
                    DialogUtility.showConnectionErrorDialogWithOk(getActivity());
                    // logging probably not necessary
                } else {
                    Toast.makeText(getActivity(), "Conversion issue! big problems :(", Toast.LENGTH_SHORT).show();
                    // todo log to some central bug tracking service
                }
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });

    }

    public void onButtonClick(View view) {
        super.onButtonClick(view);
        switch (view.getId()) {
            case R.id.img_back:
                getActivity().onBackPressed();
                break;
        }
    }
}
