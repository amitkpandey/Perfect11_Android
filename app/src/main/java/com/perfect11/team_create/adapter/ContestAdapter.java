package com.perfect11.team_create.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.perfect11.R;
import com.perfect11.contest.adapter.ContestListAdapter;
import com.perfect11.contest.adapter.RankAdapter;
import com.perfect11.team_create.dialog.FilterDialog;
import com.perfect11.team_create.dto.ContestDto;
import com.perfect11.team_create.dto.ContestSubDto;
import com.perfect11.team_create.dto.PlayerDto;
import com.squareup.picasso.Picasso;
import com.utility.customView.CustomButton;
import com.utility.customView.CustomTextView;

import java.util.ArrayList;

/**
 * Created by Developer on 13-02-2018.
 */

public class ContestAdapter extends RecyclerView.Adapter<ContestAdapter.ViewHolder> {
    private OnItemClickListener onItemClickListener;
    private Activity mActivity;
    private ArrayList<ContestDto> mdata;

    public ContestAdapter(Activity activity, ArrayList<ContestDto> data) {
        this.mActivity = activity;
        this.mdata = data;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rl_row, rl_header;
        private DonutProgress donut_progress;
        private CustomButton btn_filter;
        private Button btn_join;
        private CustomTextView tv_price, tv_total_win, tv_entry_fee, tv_spot_left;

        public ViewHolder(View itemView) {
            super(itemView);
            rl_row = itemView.findViewById(R.id.rl_row);
            donut_progress = itemView.findViewById(R.id.donut_progress);
            btn_join = itemView.findViewById(R.id.btn_join);

            tv_price = itemView.findViewById(R.id.tv_price);
            tv_total_win = itemView.findViewById(R.id.tv_total_win);
            tv_entry_fee = itemView.findViewById(R.id.tv_entry_fee);
            tv_spot_left = itemView.findViewById(R.id.tv_spot_left);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.contest_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        viewHolder.tv_entry_fee.setText(mActivity.getResources().getString(R.string.Rs) + " " + mdata.get(position).entryfee + "/-");
        viewHolder.tv_spot_left.setText("Only " + (Integer.parseInt(mdata.get(position).contestSize) - Integer.parseInt(mdata.get(position).join_size)) + " spots left");
        viewHolder.tv_price.setText(mActivity.getResources().getString(R.string.Rs) + " " + mdata.get(position).winningAmount + "/-");
        viewHolder.tv_total_win.setText("" + mdata.get(position).sub_data.size());

        viewHolder.donut_progress.setMax(Integer.parseInt(mdata.get(position).contestSize));
        viewHolder.donut_progress.setProgress(Integer.parseInt((mdata.get(position).join_size)));
        viewHolder.donut_progress.setText("" + mdata.get(position).contestSize + "\nTeams");
        viewHolder.rl_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomMessageOk(mActivity,mdata.get(position).sub_data);
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(mdata.get(position).sub_data);
                }
            }
        });

        viewHolder.btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onJoinClick(mdata.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {

        void onJoinClick(ContestDto contestDto);

        void onItemClick(ArrayList<ContestSubDto> sub_data);
    }

    public static void showCustomMessageOk(final Activity mActivity, ArrayList<ContestSubDto> sub_data)
    {
        final Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_winner_list);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        Button btn_ok = dialog.findViewById(R.id.btn_ok);
        ListView lv_winner=dialog.findViewById(R.id.lv_winner);
        RankAdapter rankAdapter=new RankAdapter(mActivity,sub_data);
        lv_winner.setAdapter(rankAdapter);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
//        dialog.show();
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.show();
            }
        });

    }
}
