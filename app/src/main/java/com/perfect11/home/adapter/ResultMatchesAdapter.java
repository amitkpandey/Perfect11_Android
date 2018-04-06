package com.perfect11.home.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.perfect11.R;
import com.perfect11.upcoming_matches.dto.UpComingMatchesDto;
import com.squareup.picasso.Picasso;
import com.utility.customView.CustomTextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Developer on 13-02-2018.
 */

public class ResultMatchesAdapter extends RecyclerView.Adapter<ResultMatchesAdapter.ViewHolder> {
    private OnButtonListener onButtonListener;
    private ArrayList<UpComingMatchesDto> upComingMatchesDtoArrayList;
    private Activity mactivity;

    public ResultMatchesAdapter(ArrayList<UpComingMatchesDto> upComingMatchesDtoArrayList, Activity activity) {
        this.upComingMatchesDtoArrayList = upComingMatchesDtoArrayList;
        this.mactivity = activity;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout ll_root;
        private CustomTextView ctv_title, ctv_country1, ctv_country2;
        private ImageView cimg_country1, cimg_country2;

        public ViewHolder(View itemView) {
            super(itemView);
            ll_root = itemView.findViewById(R.id.ll_root);
            ctv_title = itemView.findViewById(R.id.ctv_title);
            ctv_country1 = itemView.findViewById(R.id.ctv_country1);
            ctv_country2 = itemView.findViewById(R.id.ctv_country2);
            cimg_country1 = itemView.findViewById(R.id.cimg_country1);
            cimg_country2 = itemView.findViewById(R.id.cimg_country2);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_complete_matches, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.ll_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonListener.onButtonClick(position);
            }
        });
        holder.ll_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonListener.onButtonClick(position);
            }
        });

        holder.ctv_title.setText(upComingMatchesDtoArrayList.get(position).season + " " + upComingMatchesDtoArrayList.get(position).format);
        holder.ctv_country1.setText(upComingMatchesDtoArrayList.get(position).teama);
        holder.ctv_country2.setText(upComingMatchesDtoArrayList.get(position).teamb);
        Picasso.with(mactivity).load(getPictureURL(upComingMatchesDtoArrayList.get(position).teama)).placeholder(R.drawable.progress_animation).
                error(R.drawable.team_face1).into(holder.cimg_country1);
        Picasso.with(mactivity).load(getPictureURL(upComingMatchesDtoArrayList.get(position).teamb)).placeholder(R.drawable.progress_animation).
                error(R.drawable.team_face2).into(holder.cimg_country2);
    }

    @Override
    public int getItemCount() {
        return upComingMatchesDtoArrayList.size();
    }

    public void setOnButtonListener(OnButtonListener onButtonListener) {
        this.onButtonListener = onButtonListener;
    }

    private String getPictureURL(String teamA) {
        String country = teamA.trim().replace(" ", "-");
        return "https://perfect11.in/public/images/app/country/" + country + ".png";
    }

    public interface OnButtonListener {
        void onButtonClick(int position);
    }
}
