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
import com.perfect11.team_create.dto.TeamDetailForCheckingDto;
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
    private int setMaxPlayer = 11;
    private int total_cedit_point = 100;
    private TeamDetailForCheckingDto tDFC_Dto;

    /**
     * Type
     * ------
     * 0 = Keeper
     * 1 = Batman
     * 2 = All Rounder
     * 3=  Bowler
     */
    public WkAdapter(Activity activity, ArrayList<PlayerDto> playerDtoArrayList, int type, float totalPoints, int totalPlayers, String ateam, String bteam, int maxPlayer, int total_cedit_point, TeamDetailForCheckingDto tDFC_Dto) {
        this.mActivity = activity;
        this.playerDtoArrayList = playerDtoArrayList;
        this.mtype = type;
        this.totalPoints = totalPoints;
        this.mtotalPlayers = totalPlayers;
        teamName1 = ateam;
        teamName2 = bteam;
        System.out.println("teamName1 =" + ateam + "teamName2 =" + bteam);
        this.setMaxPlayer = maxPlayer;
        this.total_cedit_point = total_cedit_point;
        this.tDFC_Dto = tDFC_Dto;
    }

    public void updateTotalPoints(float totalPoints, int totalPlayers, TeamDetailForCheckingDto tDFC_Dto) {
        this.totalPoints = totalPoints;
        this.mtotalPlayers = totalPlayers;
        this.tDFC_Dto = tDFC_Dto;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rl_root;
        private ImageView cb_add;
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
        holder.tv_point.setText(playerDtoArrayList.get(position).team_code);
        if (playerDtoArrayList.get(position).isSelected) {
            holder.cb_add.setBackground(mActivity.getResources().getDrawable(R.drawable.select_player));
        } else {
            holder.cb_add.setBackground(mActivity.getResources().getDrawable(R.drawable.not_select_player));
        }

        if (teamName1.equals(playerDtoArrayList.get(position).team_code)) {
            holder.iv_rentImage2.setVisibility(View.VISIBLE);
            setPicture(holder.iv_rentImage2, playerDtoArrayList.get(position).team_code);
            holder.iv_rentImage.setVisibility(View.GONE);
        } else {
            holder.iv_rentImage2.setVisibility(View.GONE);
            holder.iv_rentImage.setVisibility(View.VISIBLE);
            setPicture(holder.iv_rentImage, playerDtoArrayList.get(position).team_code);
        }
        holder.tv_score.setText(playerDtoArrayList.get(position).credit);
        holder.rl_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (playerDtoArrayList.get(position).isSelected) {
                    playerDtoArrayList.get(position).isSelected = false;
                    totalPoints = totalPoints - Float.parseFloat(playerDtoArrayList.get(position).credit);
                    mtotalPlayers--;
                    decrement();

                    if (teamName1.equals(playerDtoArrayList.get(position).team_code)) {
                        tDFC_Dto.count_teama--;
                    } else {
                        tDFC_Dto.count_teamb--;
                    }

                    if (onButtonListener != null)
                        onButtonListener.onButtonClick(playerDtoArrayList, totalPoints, mtotalPlayers);

                    holder.cb_add.setBackground(mActivity.getResources().getDrawable(R.drawable.not_select_player));
                } else {
                    if (isValid((totalPoints + Float.parseFloat(playerDtoArrayList.get(position).credit)), playerDtoArrayList.get(position).team_code)) {
                        playerDtoArrayList.get(position).isSelected = true;
                        totalPoints = totalPoints + Float.parseFloat(playerDtoArrayList.get(position).credit);
                        mtotalPlayers++;
                        increment();
                        if (teamName1.equals(playerDtoArrayList.get(position).team_code)) {
                            tDFC_Dto.count_teama++;
                        } else {
                            tDFC_Dto.count_teamb++;
                        }

                        if (onButtonListener != null)
                            onButtonListener.onButtonClick(playerDtoArrayList, totalPoints, mtotalPlayers);

                        holder.cb_add.setBackground(mActivity.getResources().getDrawable(R.drawable.select_player));
                    }
                }
            }
        });

    }


    private void decrement() {
        switch (mtype) {
            case 0:
                tDFC_Dto.keeper_count--;
                break;
            case 1:
                tDFC_Dto.batsman_count--;
                break;
            case 2:
                tDFC_Dto.allrounder_count--;
                break;
            case 3:
                tDFC_Dto.bowler_count--;
                break;
        }
    }

    private void increment() {
        switch (mtype) {
            case 0:
                tDFC_Dto.keeper_count++;
                break;
            case 1:
                tDFC_Dto.batsman_count++;
                break;
            case 2:
                tDFC_Dto.allrounder_count++;
                break;
            case 3:
                tDFC_Dto.bowler_count++;
                break;
        }
    }

    private void setPicture(ImageView iv_rentImage2, String team_code) {
        String url = "";
        switch (team_code) {
            case "CSK":
                url = "https://perfect11.in/public/images/app/jersey/jersey-csk.png";
                break;
            case "KXIP":
                url = "https://perfect11.in/public/images/app/jersey/jersey-kxip.png";
                break;
            case "MI":
                url = "https://perfect11.in/public/images/app/jersey/jersey-mi.png";
                break;
            case "DD":
                url = "https://perfect11.in/public/images/app/jersey/jersey-dd.png";
                break;
            case "KKR":
                url = "https://perfect11.in/public/images/app/jersey/jersey-kkr.png";
                break;
            case "RCB":
                url = "https://perfect11.in/public/images/app/jersey/jersey-rcb.png";
                break;
            case "SRH":
                url = "https://perfect11.in/public/images/app/jersey/jersey-srh.png";
                break;
            case "RR":
                url = "https://perfect11.in/public/images/app/jersey/jersey-rr.png";
                break;
            default:
                url = "";
                break;
        }
        if (!url.trim().equals("")) {
            Picasso.with(mActivity).load(url).placeholder(R.drawable.progress_animation).error(R.drawable.myteam).into(iv_rentImage2);
        }
    }

    private boolean isValid(float v, String team_code) {
        int number_selected = getNoOfSelectedPlayers();

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
                    Toast.makeText(mActivity, "You have selected maximum All Rounder", Toast.LENGTH_SHORT).show();
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

        if (checkPlayerTypeSelectonCount()) {
            return false;
        }
        if (mtotalPlayers > (setMaxPlayer - 1)) {
            Toast.makeText(mActivity, "You have selected maximum player.", Toast.LENGTH_SHORT).show();
            return false;
        }

        //(tDFC_Dto.team_a_no) = Means Max limit of Player from a team
        if (teamName1.trim().equals(team_code.trim())) {
            System.out.println("tDFC_Dto.count_teama " + tDFC_Dto.count_teama);
            System.out.println("tDFC_Dto.team_a_no " + tDFC_Dto.team_a_no);
            if (tDFC_Dto.count_teama > (tDFC_Dto.team_a_no - 1)) {
                Toast.makeText(mActivity, "You have selected maximum players form one team.", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            if (tDFC_Dto.count_teamb > (tDFC_Dto.team_a_no - 1)) {
                System.out.println("tDFC_Dto.count_teamb " + tDFC_Dto.count_teamb);
                System.out.println("tDFC_Dto.team_b_no " + tDFC_Dto.team_a_no);
                Toast.makeText(mActivity, "You have selected maximum players form one team.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if (v > total_cedit_point) {
            Toast.makeText(mActivity, "'Your point limit has been exceeded", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean checkPlayerTypeSelectonCount() {
        int wk = tDFC_Dto.keeper_count - 1;
        int bm = tDFC_Dto.batsman_count - 3;
        int ar = tDFC_Dto.allrounder_count - 1;
        int bowler = tDFC_Dto.bowler_count - 3;
        int player_left = (setMaxPlayer - 1) - mtotalPlayers;

        System.out.println("wk: " + wk + " bm: " + bm + " ar: " + ar + " bowler: " + bowler + " player_left:" + player_left);

        wk = -1 * wk;
        bm = -1 * bm;
        ar = -1 * ar;
        bowler = -1 * bowler;

        switch (mtype) {
            case 0:
                wk--;
                break;
            case 1:
                bm--;
                break;
            case 2:
                ar--;
                break;
            case 3:
                bowler--;
                break;
        }
        if (wk < 0) {
            wk = 0;
        }
        if (bm < 0) {
            bm = 0;
        }
        if (ar < 0) {
            ar = 0;
        }
        if (bowler < 0) {
            bowler = 0;
        }

        System.out.println("wk: " + wk + " bm: " + bm + " ar: " + ar + " bowler: " + bowler + " player_left:" + player_left + " mtype:" + mtype);

        if (player_left < (wk + bm + ar + bowler)) {
            //ok
            if (tDFC_Dto.keeper_count < 1 && mtype != 0) {
                Toast.makeText(mActivity, "You have to select minimum 1 Keeper", Toast.LENGTH_SHORT).show();
                return true;
            } else if (tDFC_Dto.batsman_count < 3 && mtype != 1) {
                Toast.makeText(mActivity, "You have to select minimum 3 Batsman", Toast.LENGTH_SHORT).show();
                return true;
            } else if (tDFC_Dto.allrounder_count < 1 && mtype != 2) {
                Toast.makeText(mActivity, "You have to select minimum 1  All Rounder", Toast.LENGTH_SHORT).show();
                return true;
            } else if (tDFC_Dto.bowler_count < 3 && mtype != 3) {
                Toast.makeText(mActivity, "You have to select minimum 3  Bowlers", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
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
