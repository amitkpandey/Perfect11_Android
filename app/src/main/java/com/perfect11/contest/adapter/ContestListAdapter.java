package com.perfect11.contest.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.perfect11.R;
import com.perfect11.team_create.dialog.FilterDialog;
import com.perfect11.team_create.dto.ContestDto;
import com.perfect11.team_create.dto.ContestSubDto;
import com.utility.customView.CustomButton;
import com.utility.customView.CustomTextView;

import java.util.ArrayList;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class ContestListAdapter extends ArrayAdapter<ContestDto> implements StickyListHeadersAdapter, SectionIndexer {

    private List<String> stringList;
    private Activity activity;
    private LayoutInflater layoutInflater;

    private int[] mSectionIndices;
    private Character[] mSectionLetters;
    private LayoutInflater mInflater;
    private OnItemClickListener onItemClickListener;
    private String[] header;

    private ArrayList<ContestDto> mdata;

    public ContestListAdapter(Activity context, ArrayList<ContestDto> data) {
        super(context, 0, data);
        this.activity = context;
        this.mdata = data;
        header = new String[data.size()];

        for (int i = 0; i < data.size(); i++) {
            header[i] = data.get(i).winningAmount;
        }

        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return mdata.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final ViewHolder viewHolder;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.contest_row, parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.tv_entry_fee.setText(activity.getResources().getString(R.string.Rs) + " " + mdata.get(position).entryfee + "/-");
        viewHolder.tv_spot_left.setText("Only " + (Integer.parseInt(mdata.get(position).contestSize) - Integer.parseInt(mdata.get(position).join_size)) + " spots left");
        viewHolder.tv_price.setText(activity.getResources().getString(R.string.Rs) + " " + mdata.get(position).winningAmount + "/-");
        viewHolder.tv_total_win.setText("" + mdata.get(position).sub_data.size());

        viewHolder.donut_progress.setMax(Integer.parseInt(mdata.get(position).contestSize));
        viewHolder.donut_progress.setProgress(Integer.parseInt((mdata.get(position).join_size)));
        viewHolder.donut_progress.setText("" + mdata.get(position).contestSize + "\nTeams");
        viewHolder.rl_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomMessageOk(activity,mdata.get(position).sub_data);
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(mdata.get(position).sub_data);
                }
            }
        });

        viewHolder.btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onJoinClick(mdata.get(position));
                }
            }
        });

        viewHolder.btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, FilterDialog.class);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
//                ActivityController.startNextActivity(activity, FilterDialog.class, false);
            }
        });


        return view;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        HeaderViewHolder holder;
        if (view == null) {
            holder = new HeaderViewHolder();
            view = mInflater.inflate(R.layout.contest_header, parent, false);
            holder.text = view.findViewById(R.id.text1);
            view.setTag(holder);
        } else {
            holder = (HeaderViewHolder) view.getTag();
        }

//        holder.text.setText(header[position]);
        if (header[position].equalsIgnoreCase("0.00")) {
            holder.text.setText("Practice Contest");
        } else {
            holder.text.setText("Cash Contest");
        }
        Log.e("Header", "Testing");
        return view;
    }

    /**
     * Remember that these have to be static, position=1 should always return
     * the same Id that is.
     */
    @Override
    public long getHeaderId(int position) {
        // return the first character of the country as ID because this is what headers are based upon

        if (header[position].equals("0.00")) {
            String title = "Practice Contest";
            return title.charAt(0);
        } else {
            String title = "Cash Contest";
            return title.charAt(0);
        }

    }

    @Override
    public int getPositionForSection(int section) {
        if (mSectionIndices.length == 0) {
            return 0;
        }

        if (section >= mSectionIndices.length) {
            section = mSectionIndices.length - 1;
        } else if (section < 0) {
            section = 0;
        }
        return mSectionIndices[section];
    }

    @Override
    public int getSectionForPosition(int position) {
        for (int i = 0; i < mSectionIndices.length; i++) {
            if (position < mSectionIndices[i]) {
                return i - 1;
            }
        }
        return mSectionIndices.length - 1;
    }

    @Override
    public Object[] getSections() {
        return mSectionLetters;
    }

    private class HeaderViewHolder {
        private TextView text;
    }

    private class ViewHolder {
        private RelativeLayout rl_row, rl_header;
        private DonutProgress donut_progress;
        private CustomButton btn_filter;
        private Button btn_join;
        private CustomTextView tv_price, tv_total_win, tv_entry_fee, tv_spot_left;

        public ViewHolder(View view) {
            rl_row = view.findViewById(R.id.rl_row);
            rl_header = view.findViewById(R.id.rl_header);
            donut_progress = view.findViewById(R.id.donut_progress);
            btn_filter = view.findViewById(R.id.btn_filter);
            btn_join = view.findViewById(R.id.btn_join);

            tv_price = view.findViewById(R.id.tv_price);
            tv_total_win = view.findViewById(R.id.tv_total_win);
            tv_entry_fee = view.findViewById(R.id.tv_entry_fee);
            tv_spot_left = view.findViewById(R.id.tv_spot_left);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {

        void onJoinClick(ContestDto contestDto);

        void onItemClick(ArrayList<ContestSubDto> sub_data);
    }

    public static void showCustomMessageOk(final Activity mActivity, ArrayList<ContestSubDto> sub_data)
    {
        final Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_winner_list);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        Button btn_ok = dialog.findViewById(R.id.btn_ok);
        ListView lv_winner=dialog.findViewById(R.id.lv_winner);
        RankAdapter rankAdapter=new RankAdapter(mActivity,sub_data);
        lv_winner.setAdapter(rankAdapter);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
//        dialog.show();
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.show();
            }
        });

    }
}
