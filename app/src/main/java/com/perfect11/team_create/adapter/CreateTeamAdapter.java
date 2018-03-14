package com.perfect11.team_create.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.perfect11.R;
import com.perfect11.contest.dto.TeamDto;
import com.perfect11.contest.dto.TeamPlayerDto;
import com.utility.customView.CustomButton;
import com.utility.customView.CustomTextView;

import java.util.ArrayList;

/**
 * Created by Developer on 13-02-2018.
 */

public class CreateTeamAdapter extends RecyclerView.Adapter<CreateTeamAdapter.ViewHolder> {
    private OnButtonListener onButtonListener;
    private ArrayList<TeamDto> teamDtoArrayList;
    private Activity activity;

    public CreateTeamAdapter(Activity context, ArrayList<TeamDto> teamDtoArrayList) {
        this.activity = context;
        this.teamDtoArrayList = teamDtoArrayList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rl_01;
        private CustomTextView tv_team, tv_captain_name, tv_vice_captain_name, tv_wicket, tv_bat, tv_all, tv_bowl;
        private CustomButton btn_edit, btn_preview;

        public ViewHolder(View itemView) {
            super(itemView);
            rl_01 = itemView.findViewById(R.id.rl_01);
            tv_team = itemView.findViewById(R.id.tv_team);
            tv_captain_name = itemView.findViewById(R.id.tv_captain_name);
            tv_vice_captain_name = itemView.findViewById(R.id.tv_vice_captain_name);
            tv_wicket = itemView.findViewById(R.id.tv_wicket);
            tv_bat = itemView.findViewById(R.id.tv_bat);
            tv_all = itemView.findViewById(R.id.tv_all);
            tv_bowl = itemView.findViewById(R.id.tv_bowl);
            btn_edit = itemView.findViewById(R.id.btn_edit);
            btn_preview = itemView.findViewById(R.id.btn_preview);
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
        TeamDto teamDto = teamDtoArrayList.get(position);
        int allRounderSize = 0, bowlerSize = 0, batsmanSize = 0, wicketKeeperSize = 0;
        for (TeamPlayerDto teamPlayerDto : teamDto.team_player) {
            if (teamDto.captain.equalsIgnoreCase(teamPlayerDto.player)) {
                holder.tv_captain_name.setText(teamPlayerDto.full_name);
            }
            if (teamDto.vice_captain.equalsIgnoreCase(teamPlayerDto.player)) {
                holder.tv_vice_captain_name.setText(teamPlayerDto.full_name);
            }
            if (teamPlayerDto.type.equalsIgnoreCase("allrounder")) {
                allRounderSize++;
            }
            if (teamPlayerDto.type.equalsIgnoreCase("batsman")) {
                batsmanSize++;
            }
            if (teamPlayerDto.type.equalsIgnoreCase("bowler")) {
                bowlerSize++;
            }
            if (teamPlayerDto.type.equalsIgnoreCase("keeper")) {
                wicketKeeperSize++;
            }
        }
        holder.tv_team.setText("Team " + (position + 1));
        holder.tv_wicket.setText("" + wicketKeeperSize);
        holder.tv_all.setText("" + allRounderSize);
        holder.tv_bowl.setText("" + bowlerSize);
        holder.tv_bat.setText("" + batsmanSize);
        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonListener.onEditClick(position);
            }
        });
        holder.btn_preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonListener.onPreviewClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return teamDtoArrayList.size();
    }

    public void setOnButtonListener(OnButtonListener onButtonListener) {
        this.onButtonListener = onButtonListener;
    }

    public interface OnButtonListener {
        void onEditClick(int position);
        void onPreviewClick(int position);
    }
}
