package com.perfect11.upcoming_matches.adapter;

import android.app.Activity;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.perfect11.R;
import com.perfect11.upcoming_matches.dto.UpComingMatchesDto;
import com.squareup.picasso.Picasso;
import com.utility.customView.CustomTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Developer on 13-02-2018.
 */

public class UpcomingMatchesAdapter extends RecyclerView.Adapter<UpcomingMatchesAdapter.ViewHolder> {
    private OnButtonListener onButtonListener;
    private ArrayList<UpComingMatchesDto> mdata;
    private Activity mactivity;
    private final List<ViewHolder> lstHolders;
    private Handler mHandler = new Handler();
    private Runnable updateRemainingTimeRunnable = new Runnable() {
        @Override
        public void run() {
            synchronized (lstHolders) {
                long currentTime = System.currentTimeMillis();
                for (ViewHolder holder : lstHolders) {
                    holder.updateTimeRemaining(currentTime);
                }
            }
        }
    };

    public UpcomingMatchesAdapter(ArrayList<UpComingMatchesDto> data, Activity activity) {
        this.mdata = data;
        this.mactivity = activity;
        lstHolders = new ArrayList<>();
        startUpdateTimer();
    }

    private void startUpdateTimer() {
        Timer tmr = new Timer();
        tmr.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.post(updateRemainingTimeRunnable);
            }
        }, 1000, 1000);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout ll_root;
        private CustomTextView ctv_title, ctv_country1, ctv_country2, ctv_timer;
        private CircleImageView cimg_country1, cimg_country2;
        private UpComingMatchesDto upComingMatchesDto;


        public ViewHolder(View itemView) {
            super(itemView);
            ll_root = itemView.findViewById(R.id.ll_root);
            ctv_title = itemView.findViewById(R.id.ctv_title);
            ctv_country1 = itemView.findViewById(R.id.ctv_country1);
            ctv_country2 = itemView.findViewById(R.id.ctv_country2);
            cimg_country1 = itemView.findViewById(R.id.cimg_country1);
            cimg_country2 = itemView.findViewById(R.id.cimg_country2);
            ctv_timer = itemView.findViewById(R.id.ctv_timer);
        }

        private void setData(UpComingMatchesDto item, final int position) {
            upComingMatchesDto = item;
            ctv_title.setText(item.season + " " + item.format);
            ctv_country1.setText(item.teama);
            ctv_country2.setText(item.teamb);
//        holder.ctv_timer.setText(millToMins(mdata.get(position).timeRemaining) + " mins remaining");
            Picasso.with(mactivity).load(getPictureURL(item.teama)).placeholder(R.drawable.progress_animation).error(R.drawable.no_team).into(cimg_country1);
            Picasso.with(mactivity).load(getPictureURL(item.teamb)).placeholder(R.drawable.progress_animation).error(R.drawable.no_team).into(cimg_country2);
            ll_root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onButtonListener.onButtonClick(position);
                }
            });
            updateTimeRemaining(System.currentTimeMillis());
        }

        private void updateTimeRemaining(long currentTime) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                Date date;
                date = sdf.parse(upComingMatchesDto.start_date);
                long millis = date.getTime();
                long timeDiff = millis - currentTime;
                if (timeDiff > 0) {
                    int seconds = (int) (timeDiff / 1000) % 60;
                    int minutes = (int) ((timeDiff / (1000 * 60)) % 60);
                    int hours = (int) ((timeDiff / (1000 * 60 * 60)) % 24);
                    ctv_timer.setText(hours + " hrs " + minutes + " mins " + seconds + " sec");
                } else {
                    ctv_timer.setText("Expired!!");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_upcoming_matches, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        synchronized (lstHolders) {
            lstHolders.add(viewHolder);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.setData(mdata.get(position), position);

//        holder.ctv_title.setText(mdata.get(position).season + " " + mdata.get(position).format);
//        holder.ctv_country1.setText(mdata.get(position).teama);
//        holder.ctv_country2.setText(mdata.get(position).teamb);
////        holder.ctv_timer.setText(millToMins(mdata.get(position).timeRemaining) + " mins remaining");
//        Picasso.with(mactivity).load(getPictureURL(mdata.get(position).teama)).placeholder(R.drawable.progress_animation).error(R.drawable.no_team).into(holder.cimg_country1);
//        Picasso.with(mactivity).load(getPictureURL(mdata.get(position).teamb)).placeholder(R.drawable.progress_animation).error(R.drawable.no_team).into(holder.cimg_country2);
//        holder.ll_root.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onButtonListener.onButtonClick(position);
//            }
//        });
//        updateTimeRemaining(mdata.get(position), holder.ctv_timer, System.currentTimeMillis());

//        try {
//            // String myDate = "2018/02/26 18:19:45";
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
//            Date date;
//            date = sdf.parse(mdata.get(position).start_date);
//            long millis = date.getTime();
//            long countdown = millis - System.currentTimeMillis();
//            long hours = TimeUnit.MILLISECONDS.toHours(countdown);
//            long min = TimeUnit.MILLISECONDS.toMinutes((countdown - TimeUnit.HOURS.toMillis(hours)));
//            long sec = TimeUnit.MILLISECONDS.toSeconds((countdown - (TimeUnit.HOURS.toMillis(hours) + TimeUnit.MINUTES.toMillis(min))));
//
////            System.out.println(" Abcd " + (millis - System.currentTimeMillis()) + " h:" + hours + " m:" + min + " s:" + sec);
//            holder.ctv_timer.setText(hours + " hr " + min + " min " + sec + " sec");
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
    }


    private String getPictureURL(String teama) {
        String country = teama.trim().replace(" ", "-");
        return "http://52.15.50.179/public/images/team/flag-of-" + country + ".png";
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public void setOnButtonListener(OnButtonListener onButtonListener) {
        this.onButtonListener = onButtonListener;
    }

    public interface OnButtonListener {
        void onButtonClick(int position);
    }


}
