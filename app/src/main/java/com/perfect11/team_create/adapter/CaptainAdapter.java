package com.perfect11.team_create.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.perfect11.R;
import com.perfect11.team_create.dto.PlayerDto;
import com.squareup.picasso.Picasso;
import com.utility.customView.CustomTextView;

import java.util.ArrayList;

public class CaptainAdapter extends RecyclerView.Adapter<CaptainAdapter.ViewHolder> {
    private OnButtonListener onButtonListener;
    private int selectedCapten = -1, selectedVCapten = -1;
    private Activity activity;
    ArrayList<PlayerDto> selectedPlayer;
    private String teamName1, teamName2;

    public CaptainAdapter(Activity activity, ArrayList<PlayerDto> selectedPlayer, String teamName1, String teamName2) {
        this.activity = activity;
        this.selectedPlayer = selectedPlayer;
        this.teamName1 = teamName1;
        this.teamName2 = teamName2;

        for (int i = 0; i < selectedPlayer.size(); i++) {
            if (selectedPlayer.get(i).isC) {
                selectedCapten = i;
            }
            if (selectedPlayer.get(i).isCV) {
                selectedVCapten = i;
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rl_root;
        private CustomTextView tv_type, tv_name, tv_point;
        // private RadioGroup rg_select;
        private RadioButton rb_captain, rb_vice_captain;
        private ImageView iv_rentImage2, iv_rentImage;

        public ViewHolder(View itemView) {
            super(itemView);
            rl_root = itemView.findViewById(R.id.rl_root);
            tv_type = itemView.findViewById(R.id.tv_type);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_point = itemView.findViewById(R.id.tv_point);
            iv_rentImage = itemView.findViewById(R.id.iv_rentImage);

            //   rg_select=itemView.findViewById(R.id.rg_select);
            rb_captain = itemView.findViewById(R.id.rb_captain);
            rb_vice_captain = itemView.findViewById(R.id.rb_vice_captain);

            iv_rentImage = itemView.findViewById(R.id.iv_rentImage);
            iv_rentImage2 = itemView.findViewById(R.id.iv_rentImage2);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.captain_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.rl_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        holder.tv_name.setText(selectedPlayer.get(position).full_name);
        holder.tv_point.setText(selectedPlayer.get(position).team_name + " | " + "-- Point");

        //Visible Player Type
        if (selectedPlayer.get(position).titleHeader.equals("")) {
            holder.tv_type.setVisibility(View.GONE);
        } else {
            holder.tv_type.setVisibility(View.VISIBLE);
            holder.tv_type.setText(selectedPlayer.get(position).titleHeader);
        }

//Change team Jersey
        if (teamName1.equals(selectedPlayer.get(position).team_name)) {
            holder.iv_rentImage2.setVisibility(View.VISIBLE);
            holder.iv_rentImage.setVisibility(View.GONE);
            setPicture(holder.iv_rentImage2,selectedPlayer.get(position).team_code);
        } else {
            holder.iv_rentImage2.setVisibility(View.GONE);
            holder.iv_rentImage.setVisibility(View.VISIBLE);
            setPicture(holder.iv_rentImage,selectedPlayer.get(position).team_code);
        }

//check the radio button if both position and selectedPosition matches
        holder.rb_captain.setChecked(position == selectedCapten);
        holder.rb_vice_captain.setChecked(position == selectedVCapten);

//Set the position tag to both radio button and label
        holder.rb_captain.setTag(position);
        holder.rb_vice_captain.setTag(position);

        holder.rb_captain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captainCheckChanged(v);
            }
        });

        holder.rb_vice_captain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vCaptainCheckChanged(v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return selectedPlayer.size();
    }

    //For Button Click Listener
    public void setOnButtonListener(OnButtonListener onButtonListener) {
        this.onButtonListener = onButtonListener;
    }

    public interface OnButtonListener {
        void onButtonClick(int position);
    }

    //On selecting any view set the current position to selectedPositon and notify adapter
    private void captainCheckChanged(View v) {
        selectedCapten = (Integer) v.getTag();
        if (selectedCapten == selectedVCapten) {
            selectedVCapten = -1;
        }
        notifyDataSetChanged();
    }

    private void vCaptainCheckChanged(View v) {
        selectedVCapten = (Integer) v.getTag();
        if (selectedCapten == selectedVCapten) {
            selectedCapten = -1;
        }
        notifyDataSetChanged();
    }


    //Return the selectedPosition item
    public ArrayList<PlayerDto> getSelectedCaptainAndVCaptainWithTeam() {

        for (int i = 0; i < selectedPlayer.size(); i++) {
            selectedPlayer.get(i).isC = false;
            selectedPlayer.get(i).isCV = false;
        }
        if (isValid()) {
            selectedPlayer.get(selectedCapten).isC = true;
            selectedPlayer.get(selectedVCapten).isCV = true;
            return selectedPlayer;
        }
        return null;
    }

    public boolean isValid() {
        if (selectedCapten == -1) {
            Toast.makeText(activity, "Select Captain", Toast.LENGTH_SHORT).show();
            return false;
        } else if (selectedVCapten == -1) {
            Toast.makeText(activity, "Select Vice Captain", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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
            Picasso.with(activity).load(url).placeholder(R.drawable.progress_animation).error(R.drawable.myteam).into(iv_rentImage2);
        }
    }
}
