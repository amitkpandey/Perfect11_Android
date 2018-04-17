package com.perfect11.contest.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.perfect11.R;
import com.perfect11.contest.dto.JoinedContestDto;
import com.perfect11.contest.dto.LiveLeaderboardDto;
import com.perfect11.login_signup.dto.UserDto;
import com.utility.PreferenceUtility;
import com.utility.customView.CustomTextView;

import java.util.ArrayList;

public class PracticeContestAdapter extends RecyclerView.Adapter<PracticeContestAdapter.ViewHolder> {
    private OnButtonListener onButtonListener;
    private Activity activity;
    private ArrayList<LiveLeaderboardDto> mdata;
    private UserDto userDto;

    public PracticeContestAdapter(Activity context, ArrayList<LiveLeaderboardDto> data) {
        this.activity = context;
        this.mdata = data;
        userDto = (UserDto) PreferenceUtility.getObjectInAppPreference(activity, PreferenceUtility.APP_PREFERENCE_NAME);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rl_01;
        private CustomTextView tv_name, tv_point, tv_rank, tv_team_name, tv_amount;
        private RelativeLayout ll_winner;

        public ViewHolder(View itemView) {
            super(itemView);
            rl_01 = itemView.findViewById(R.id.rl_01);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_point = itemView.findViewById(R.id.tv_point);
            tv_rank = itemView.findViewById(R.id.tv_rank);
            tv_team_name = itemView.findViewById(R.id.tv_team_name);
            ll_winner = itemView.findViewById(R.id.ll_winner);
            tv_amount = itemView.findViewById(R.id.tv_amount);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.practice_contest_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tv_name.setText(mdata.get(position).reference_id);
        holder.tv_point.setText("" + mdata.get(position).points);

        holder.tv_rank.setText("# " + mdata.get(position).rank);


        if (position % 2 == 0) {
            holder.rl_01.setBackgroundColor(activity.getResources().getColor(R.color.tab_color1));
        } else {
            holder.rl_01.setBackgroundColor(activity.getResources().getColor(R.color.tab_color2));
        }

        if (userDto.reference_id.trim().equals(mdata.get(position).reference_id)) {
            holder.rl_01.setBackgroundColor(activity.getResources().getColor(R.color.tab_color3));
        }

        if (mdata.get(position).winingamount == 0) {
            holder.tv_amount.setVisibility(View.GONE);
            holder.ll_winner.setVisibility(View.GONE);
        } else {
            holder.ll_winner.setVisibility(View.VISIBLE);
            holder.tv_amount.setVisibility(View.VISIBLE);
            holder.tv_amount.setText("Rs." + mdata.get(position).winingamount+"/-");
        }

        holder.tv_team_name.setText("(" + mdata.get(position).team_name + ")");
        holder.rl_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonListener.onButtonClick(position);
            }
        });

        if (userDto.reference_id.trim().equals(mdata.get(position).reference_id.trim())) {
            holder.tv_rank.setTextColor(activity.getResources().getColor(R.color.red_text_color));
        } else {
            holder.tv_rank.setTextColor(activity.getResources().getColor(R.color.colorPrimaryDark));
        }
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
