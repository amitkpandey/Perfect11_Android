package com.perfect11.contest.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.perfect11.R;
import com.perfect11.contest.dto.JoinedContestDto;
import com.utility.customView.CustomTextView;

import java.util.ArrayList;

public class MyContestAdapter extends RecyclerView.Adapter<MyContestAdapter.ViewHolder> {
    private OnButtonListener onButtonListener;
    private Activity activity;
    private ArrayList<JoinedContestDto> mdata;

    public MyContestAdapter(Activity context, ArrayList<JoinedContestDto> data) {
        this.activity = context;
        this.mdata = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout ll_contest;
        private CustomTextView ctv_title,tv_practice, tv_price, tv_entry_fee, tv_joined_count, tv_points_count, tv_rank_count;

        public ViewHolder(View itemView) {
            super(itemView);
            ll_contest = itemView.findViewById(R.id.ll_contest);
            tv_practice = itemView.findViewById(R.id.tv_practice);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_entry_fee = itemView.findViewById(R.id.tv_entry_fee);
            tv_joined_count = itemView.findViewById(R.id.tv_joined_count);
            tv_points_count = itemView.findViewById(R.id.tv_points_count);
            tv_rank_count = itemView.findViewById(R.id.tv_rank_count);
            ctv_title=itemView.findViewById(R.id.ctv_title);
            ctv_title.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.join_contest_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (mdata.get(position).amount.equalsIgnoreCase("0.00")) {
            holder.tv_practice.setText("Practice Contest");
            holder.tv_price.setText("Winner takes all the glory");
        } else {
            holder.tv_practice.setText("Paid Contest");
            holder.tv_price.setText("Winning Amount Rs. " + mdata.get(position).winningAmount + "/-");
        }
        holder.tv_entry_fee.setText("Rs. " + mdata.get(position).amount + "/-");
        holder.tv_joined_count.setText("Team 1");
        holder.tv_points_count.setText(mdata.get(position).winingrank);
        holder.tv_rank_count.setText("#1");

        if(mdata.get(position).tournament==null||mdata.get(position).tournament.trim().equals(""))
            holder.ctv_title.setText(mdata.get(position).room_name);
        else
            holder.ctv_title.setText(mdata.get(position).tournament);

        holder.ll_contest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonListener.onButtonClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setOnButtonListener(OnButtonListener onButtonListener) {
        this.onButtonListener = onButtonListener;
    }

    public interface OnButtonListener {
        void onButtonClick(int position);
    }
}
