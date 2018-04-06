package com.perfect11.contest.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.perfect11.R;
import com.perfect11.team_create.dto.ContestSubDto;
import com.utility.customView.CustomEditText;
import com.utility.customView.CustomTextView;

import java.util.ArrayList;

public class RankAdapter extends BaseAdapter {
    private ViewHolder viewHolder;
    private Activity mActivity;
private ArrayList<ContestSubDto> sub_data;

    public RankAdapter(Activity mActivity, ArrayList<ContestSubDto> sub_data) {
   this.sub_data=sub_data;
   this.mActivity=mActivity;
    }

    @Override
    public int getCount() {
        return sub_data.size();
    }

    @Override
    public Object getItem(int i) {
        return sub_data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = mActivity.getLayoutInflater();
        view = layoutInflater.inflate(R.layout.winner_row, viewGroup, false);
        viewHolder = new ViewHolder(view);
        viewHolder.tv_rank.setText("Rank "+sub_data.get(i).rank);
        viewHolder.tv_amount.setText("Rs. "+sub_data.get(i).winning_amount+"/-");
        return view;
    }

    private class ViewHolder {
        private TextView tv_rank, tv_amount;

        public ViewHolder(View v) {
            tv_rank = v.findViewById(R.id.tv_rank);
            tv_amount = v.findViewById(R.id.tv_amount);
        }
    }
}
