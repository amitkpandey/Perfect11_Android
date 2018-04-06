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

public class WkAdapter extends RecyclerView.Adapter<WkAdapter.ViewHolder> {
    private OnButtonListener onButtonListener;
    private Activity mActivity;
    private ArrayList<PlayerDto> playerDtoArrayList;
    private int mtype, mtotalPlayers;
    private float totalPoints;
    private String teamName1, teamName2;
    private int setMaxPlayer=11;
    private int total_cedit_point=100;

    /**
     * Type
     * ------
     * 0 = Keeper
     * 1 = Batman
     * 2 = All Rounder
     * 3=  Bowler
     */
    public WkAdapter(Activity activity, ArrayList<PlayerDto> playerDtoArrayList, int type, float totalPoints, int totalPlayers, String ateam, String bteam, int maxPlayer, int total_cedit_point) {
        this.mActivity = activity;
        this.playerDtoArrayList = playerDtoArrayList;
        this.mtype = type;
        this.totalPoints = totalPoints;
        this.mtotalPlayers = totalPlayers;
        teamName1 = ateam;
        teamName2 = bteam;
        this.setMaxPlayer=maxPlayer;
        this.total_cedit_point=total_cedit_point;
    }

    public void updateTotalPoints(float totalPoints, int totalPlayers) {
        this.totalPoints = totalPoints;
        this.mtotalPlayers = totalPlayers;
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
        holder.tv_name.setText(playerDtoArrayList.get(position).full_name);
        holder.tv_point.setText(playerDtoArrayList.get(position).team_code + " | " + "-- Point");
        if (playerDtoArrayList.get(position).isSelected) {
            holder.cb_add.setBackground(mActivity.getResources().getDrawable(R.drawable.select_player));
        } else {
            holder.cb_add.setBackground(mActivity.getResources().getDrawable(R.drawable.not_select_player));
        }

        if (teamName1.equals(playerDtoArrayList.get(position).team_name)) {
            holder.iv_rentImage2.setVisibility(View.VISIBLE);
            setPicture(holder.iv_rentImage2, playerDtoArrayList.get(position).team_code);
            holder.iv_rentImage.setVisibility(View.GONE);
        } else {
            holder.iv_rentImage2.setVisibility(View.GONE);
            holder.iv_rentImage.setVisibility(View.VISIBLE);
            setPicture(holder.iv_rentImage, playerDtoArrayList.get(position).team_code);
        }
        holder.tv_score.setText(playerDtoArrayList.get(position).credit);
        holder.cb_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (playerDtoArrayList.get(position).isSelected) {
                    playerDtoArrayList.get(position).isSelected = false;
                    totalPoints = totalPoints - Float.parseFloat(playerDtoArrayList.get(position).credit);
                    mtotalPlayers--;
                    if (onButtonListener != null)
                        onButtonListener.onButtonClick(playerDtoArrayList, totalPoints, mtotalPlayers);

                    holder.cb_add.setBackground(mActivity.getResources().getDrawable(R.drawable.not_select_player));
                } else {
                    if (isValid(totalPoints + Float.parseFloat(playerDtoArrayList.get(position).credit))) {
                        playerDtoArrayList.get(position).isSelected = true;
                        totalPoints = totalPoints + Float.parseFloat(playerDtoArrayList.get(position).credit);
                        mtotalPlayers++;
                        if (onButtonListener != null)
                            onButtonListener.onButtonClick(playerDtoArrayList, totalPoints, mtotalPlayers);

                        holder.cb_add.setBackground(mActivity.getResources().getDrawable(R.drawable.select_player));
                    }
                }
            }
        });

    }

    private void setPicture(ImageView iv_rentImage2, String team_code) {
        String url = "";
        switch (team_code) {
            case "CSK":
                url = "http://52.15.50.179/public/images/app/jersey/jersey-csk.png";
                break;
            case "KXIP":
                url = "http://52.15.50.179/public/images/app/jersey/jersey-kxip.png";
                break;
            case "MI":
                url = "http://52.15.50.179/public/images/app/jersey/jersey-mi.png";
                break;
            case "DD":
                url = "http://52.15.50.179/public/images/app/jersey/jersey-dd.png";
                break;
            case "KKR":
                url = "http://52.15.50.179/public/images/app/jersey/jersey-kkr.png";
                break;
            case "RCB":
                url = "http://52.15.50.179/public/images/app/jersey/jersey-rcb.png";
                break;
            case "SRH":
                url = "http://52.15.50.179/public/images/app/jersey/jersey-srh.png";
                break;
            case "RR":
                url = "http://52.15.50.179/public/images/app/jersey/jersey-rr.png";
                break;
            default:
                url = "";
                break;
        }
        if (!url.trim().equals("")) {
            Picasso.with(mActivity).load(url).placeholder(R.drawable.progress_animation).error(R.drawable.myteam).into(iv_rentImage2);
        }
    }

    private boolean isValid(float v) {
        int number_selected = getNoOfSelectedPlayers();
        ;
        switch (mtype) {
            case 0:
                if (number_selected > 0) {
                    Toast.makeText(mActivity, "You have selected maximum Keeper", Toast.LENGTH_SHORT).show();
                    return false;
                }
                break;
            case 1:
                if (number_selected > 5) {
                    Toast.makeText(mActivity, "You have selected maximum  Batsman", Toast.LENGTH_SHORT).show();
                    return false;
                }
                break;
            case 2:
                if (number_selected > 3) {
                    Toast.makeText(mActivity, "You have selected maximum  Rounder", Toast.LENGTH_SHORT).show();
                    return false;
                }
                break;
            case 3:
                if (number_selected > 5) {
                    Toast.makeText(mActivity, "You have selected maximum Bowlers", Toast.LENGTH_SHORT).show();
                    return false;
                }
                break;
        }
        if (v > total_cedit_point) {
            Toast.makeText(mActivity, "Your point has been exhausted.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mtotalPlayers > (setMaxPlayer-1)) {
            Toast.makeText(mActivity, "You have selected maximum player.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private int getNoOfSelectedPlayers() {
        int i = 0;
        for (PlayerDto playerDto : playerDtoArrayList) {
            if (playerDto.isSelected) {
                i++;
            }
        }
        return i;
    }

    @Override
    public int getItemCount() {
        return playerDtoArrayList.size();
    }

    public void setOnButtonListener(OnButtonListener onButtonListener) {
        this.onButtonListener = onButtonListener;
    }

    public interface OnButtonListener {
        void onButtonClick(ArrayList<PlayerDto> playerDtoArrayList, float totalPoints, int totalPlayers);
    }
}
