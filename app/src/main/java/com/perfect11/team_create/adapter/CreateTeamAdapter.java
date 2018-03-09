package com.perfect11.team_create.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.perfect11.R;

/**
 * Created by Developer on 13-02-2018.
 */

public class CreateTeamAdapter extends RecyclerView.Adapter<CreateTeamAdapter.ViewHolder> {
    private OnButtonListener onButtonListener;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rl_01;

        public ViewHolder(View itemView) {
            super(itemView);
            rl_01 = itemView.findViewById(R.id.rl_01);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.team_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.rl_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonListener.onButtonClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public void setOnButtonListener(OnButtonListener onButtonListener) {
        this.onButtonListener = onButtonListener;
    }

    public interface OnButtonListener {
        void onButtonClick(int position);
    }
}
