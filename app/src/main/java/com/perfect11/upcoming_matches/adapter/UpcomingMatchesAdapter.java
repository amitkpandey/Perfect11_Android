package com.perfect11.upcoming_matches.adapter;

import android.app.Activity;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.perfect11.R;
import com.perfect11.upcoming_matches.dto.UpComingMatchesDto;
import com.squareup.picasso.Picasso;
import com.utility.CommonUtility;
import com.utility.DialogUtility;
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
        private ImageView cimg_country1, cimg_country2;
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

            Picasso.with(mactivity).load(getPictureURL(item.teama)).placeholder(R.drawable.progress_animation).error(R.drawable.team_face1).into(cimg_country1);
            Picasso.with(mactivity).load(getPictureURL(item.teamb)).placeholder(R.drawable.progress_animation).error(R.drawable.team_face2).into(cimg_country2);
            ll_root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(CommonUtility.checkConnectivity(mactivity))
                    {
                        onButtonListener.onButtonClick(position);
                    }else
                    {
                        DialogUtility.showConnectionErrorDialogWithOk(mactivity);
                    }

                }
            });
            }

        private void updateTimeRemaining(long currentTime) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                Date date;
                date = sdf.parse(upComingMatchesDto.start_date);
                long millis = date.getTime();
                long hoursMillis = 60 * 60 * 1000;
//                System.out.println("millis " + millis + " start " + upComingMatchesDto.start_date + " current " + System.currentTimeMillis());
                long timeDiff = (millis - hoursMillis) - currentTime;
                if (timeDiff > 0) {
                    int seconds = (int) (timeDiff / 1000) % 60;
                    int minutes = (int) ((timeDiff / (1000 * 60)) % 60);
                    int hours = (int) ((timeDiff / (1000 * 60 * 60)) % 24);
                    int diffDays = (int) timeDiff / (24 * 60 * 60 * 1000);
                    ctv_timer.setText((diffDays == 0 ? "" : diffDays + " days ") + hours + " hrs " + minutes + " mins " + seconds + " sec");
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

    }


    private String getPictureURL(String teama) {
        String country = teama.trim().replace(" ", "-");
        return "https://perfect11.in/public/images/app/country/" + country + ".png";
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
