package com.perfect11.team_create.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.perfect11.R;
import com.perfect11.team_create.dto.PlayerDto;
import com.squareup.picasso.Picasso;
import com.utility.customView.CustomTextView;

import java.util.ArrayList;

/**
 * Created by Developer on 13-02-2018.
 */

public class ContestAdapter extends RecyclerView.Adapter<ContestAdapter.ViewHolder> {
    private OnButtonListener onButtonListener;
    private Activity mActivity;

    /**
     * Type
     * ------
     * 0 = Keeper
     * 1 = Batman
     * 2 = All Rounder
     * 3=  Bowler
     */
    public ContestAdapter(Activity activity) {
        this.mActivity = activity;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rl_root;
        private Button cb_add;
        private CustomTextView tv_name, tv_point, tv_score;
        private ImageView iv_rentImage2, iv_rentImage;

        public ViewHolder(View itemView) {
            super(itemView);
            rl_root = itemView.findViewById(R.id.rl_root);
            cb_add = itemView.findViewById(R.id.cb_add);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_point = itemView.findViewById(R.id.tv_point);
            tv_score = itemView.findViewById(R.id.tv_score);

            iv_rentImage = itemView.findViewById(R.id.iv_rentImage);
            iv_rentImage2 = itemView.findViewById(R.id.iv_rentImage2);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_player, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public void setOnButtonListener(OnButtonListener onButtonListener) {
        this.onButtonListener = onButtonListener;
    }

    public interface OnButtonListener {
        void onButtonClick(int position);
    }
}
