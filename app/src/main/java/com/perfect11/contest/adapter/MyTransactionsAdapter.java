package com.perfect11.contest.adapter;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.perfect11.R;
import com.perfect11.account.dto.MyTransectionDto;
import com.utility.customView.CustomTextView;

import java.util.ArrayList;

/**
 * Created by Developer on 13-02-2018.
 */

public class MyTransactionsAdapter extends RecyclerView.Adapter<MyTransactionsAdapter.ViewHolder> {
    private OnButtonListener onButtonListener;
    private ArrayList<MyTransectionDto> mshopkeeperlist;
    private FragmentActivity mactivity;

    public MyTransactionsAdapter(ArrayList<MyTransectionDto> shopkeeperlist, FragmentActivity activity) {
        mshopkeeperlist = shopkeeperlist;
        mactivity = activity;
    }

    public void setData(ArrayList<MyTransectionDto> shopkeeperlist) {
        mshopkeeperlist = shopkeeperlist;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rl_01;
        private CustomTextView tv_date, tv_amount;

        public ViewHolder(View itemView) {
            super(itemView);
            rl_01 = itemView.findViewById(R.id.rl_01);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_amount = itemView.findViewById(R.id.tv_amount);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tv_amount.setText(mshopkeeperlist.get(position).amount);
        holder.tv_date.setText(mshopkeeperlist.get(position).created_date);
        holder.rl_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonListener.onButtonClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mshopkeeperlist.size();
    }

    public void setOnButtonListener(OnButtonListener onButtonListener) {
        this.onButtonListener = onButtonListener;
    }

    public interface OnButtonListener {
        void onButtonClick(int position);
    }
}
