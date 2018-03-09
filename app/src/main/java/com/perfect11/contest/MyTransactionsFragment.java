package com.perfect11.contest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.perfect11.R;
import com.perfect11.base.BaseFragment;
import com.perfect11.base.BaseHeaderActivity;
import com.perfect11.contest.adapter.MyTransactionsAdapter;

public class MyTransactionsFragment extends BaseFragment {

    public static MyTransactionsFragment newInstance() {
        return new MyTransactionsFragment();
    }

    private RecyclerView rv_transactions;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.my_transactions_layout, container, false);
        setInnerHeader("My Transactions");
        initView();
        return view;
    }

    private void initView() {
        rv_transactions = view.findViewById(R.id.rv_transactions);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_transactions.setLayoutManager(layoutManager);
        MyTransactionsAdapter myTransactionsAdapter = new MyTransactionsAdapter();
        rv_transactions.setAdapter(myTransactionsAdapter);
        myTransactionsAdapter.setOnButtonListener(new MyTransactionsAdapter.OnButtonListener() {
            @Override
            public void onButtonClick(int position) {
                ((BaseHeaderActivity) getActivity()).addFragment(ContestFragment.newInstance(), true, ContestFragment.class.getName());
//                ((BaseHeaderActivity) getActivity()).addFragment(CreateContestFragment.newInstance(), true, CreateContestFragment.class.getName());
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
