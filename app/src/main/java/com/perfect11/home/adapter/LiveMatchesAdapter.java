package com.perfect11.home.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.perfect11.R;
import com.perfect11.upcoming_matches.adapter.UpcomingMatchesAdapter;
import com.perfect11.upcoming_matches.dto.UpComingMatchesDto;
import com.squareup.picasso.Picasso;
import com.utility.customView.CustomTextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Developer on 13-02-2018.
 */

public class LiveMatchesAdapter extends RecyclerView.Adapter<LiveMatchesAdapter.ViewHolder> {
    private OnButtonListener onButtonListener;
    private ArrayList<UpComingMatchesDto> mdata;
    private Activity mactivity;

    public LiveMatchesAdapter(ArrayList<UpComingMatchesDto> data, Activity activity) {
        this.mdata = data;
        this.mactivity = activity;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout ll_root;
        private CustomTextView ctv_title, ctv_country1, ctv_country2;
        private CircleImageView cimg_country1,cimg_country2;
        public ViewHolder(View itemView) {
            super(itemView);
            ll_root = itemView.findViewById(R.id.ll_root);
            ctv_title = itemView.findViewById(R.id.ctv_title);
            ctv_country1 = itemView.findViewById(R.id.ctv_country1);
            ctv_country2 = itemView.findViewById(R.id.ctv_country2);
            cimg_country1=itemView.findViewById(R.id.cimg_country1);
            cimg_country2=itemView.findViewById(R.id.cimg_country2);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_live_matches, parent, false);
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

        holder.ctv_title.setText(mdata.get(position).season + " " + mdata.get(position).format);
        holder.ctv_country1.setText(mdata.get(position).teama);
        holder.ctv_country2.setText(mdata.get(position).teamb);
        Picasso.with(mactivity).load(getPictureURL(mdata.get(position).teama)).placeholder(R.drawable.progress_animation).error(R.drawable.no_team).into(holder.cimg_country1);
        Picasso.with(mactivity).load(getPictureURL(mdata.get(position).teamb)).placeholder(R.drawable.progress_animation).error(R.drawable.no_team).into(holder.cimg_country2);
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    private String getPictureURL(String teama) {
        String country = teama.trim().replace(" ", "-");
        String url = "http://52.15.50.179/public/images/team/flag-of-" + country + ".png";
        return url;
    }

    public void setOnButtonListener(OnButtonListener onButtonListener) {
        this.onButtonListener = onButtonListener;
    }

    public interface OnButtonListener {
        void onButtonClick(int position);
    }
}
