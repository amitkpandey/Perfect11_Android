package com.perfect11.team_create.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.perfect11.R;
import com.perfect11.team_create.dto.PlayerDto;
import com.perfect11.team_create.dto.TeamDetailForCheckingDto;
import com.utility.customView.CustomTextView;

import java.util.ArrayList;

/**
 * Created by Developer on 16-02-2018.
 */

public class PlayerTypeAdapter extends RecyclerView.Adapter<PlayerTypeAdapter.ViewHolder> {
    private int images[] = {R.drawable.select_palyer_wk, R.drawable.select_palyer_bat, R.drawable.select_palyer_ar, R.drawable.select_palyer_baller};
    private String title[] = {"WK", "BAT", "AR", "BOWL"};
    private String details[] = {"Choose 1 WK", "Choose 3 to 6 BAT", "Choose 1 to 4 AR", "Choose 3 to 6 BOWL"};
    private Activity activity;
    private int select_position = 0;
    private OnButtonListener onButtonListener;
    private int mtotal_player[]=new int[4];

    public PlayerTypeAdapter(Activity activity, ArrayList<PlayerDto> bowler, ArrayList<PlayerDto> batsman, ArrayList<PlayerDto> allrounder, ArrayList<PlayerDto> keeper) {
        this.activity = activity;

        //Total Selected Keeper
        for(PlayerDto playerDto:keeper)
        {if(playerDto.isSelected)
            mtotal_player[0]++;
        }

        //Total Selected batsman
        for(PlayerDto playerDto:batsman)
        {if(playerDto.isSelected)
            mtotal_player[1]++;
        }

        //Total Selected allrounder
        for(PlayerDto playerDto:allrounder)
        {if(playerDto.isSelected)
            mtotal_player[2]++;
        }

        //Total Selected bowler
        for(PlayerDto playerDto:bowler)
        {if(playerDto.isSelected)
            mtotal_player[3]++;
        }

    }

    public void updateView(int select_position, ArrayList<PlayerDto> playerDtoArrayList) {
        this.select_position = select_position;
        mtotal_player[select_position] = 0;

        for(PlayerDto playerDto:playerDtoArrayList)
        {if(playerDto.isSelected)
            mtotal_player[select_position]++;
        }

        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_player_type, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.img_player_type.setImageDrawable(activity.getResources().getDrawable(images[position]));
        holder.ctv_bat_title.setText(title[position]);
        holder.ctv_bat_total.setText(""+mtotal_player[position]);
        holder.ctv_bat_description.setText(details[position]);

        if (position == select_position) {
            holder.rl_body.setBackgroundColor(activity.getResources().getColor(R.color.red_text_color));
            holder.ctv_bat_title.setTextColor(Color.WHITE);
            holder.ctv_bat_description.setTextColor(Color.WHITE);
        } else {
            holder.rl_body.setBackgroundColor(Color.WHITE);
            holder.ctv_bat_title.setTextColor(Color.BLACK);
            holder.ctv_bat_description.setTextColor(Color.BLACK);
        }
        holder.rl_body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonListener.onButtonClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_player_type;
        private CustomTextView ctv_bat_total, ctv_bat_title,ctv_bat_description;
        private RelativeLayout rl_body;

        public ViewHolder(View itemView) {
            super(itemView);
            img_player_type = itemView.findViewById(R.id.img_player_type);
            ctv_bat_total = itemView.findViewById(R.id.ctv_bat_total);
            ctv_bat_title = itemView.findViewById(R.id.ctv_bat_title);
            rl_body = itemView.findViewById(R.id.rl_body);
            ctv_bat_description= itemView.findViewById(R.id.ctv_bat_description);
        }
    }

    public void setOnButtonListener(OnButtonListener onButtonListener) {
        this.onButtonListener = onButtonListener;
    }

    public interface OnButtonListener {
        void onButtonClick(int position);
    }
}
