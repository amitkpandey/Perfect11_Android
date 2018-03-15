package com.perfect11.contest.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.perfect11.R;
import com.perfect11.contest.dto.ContestWinnerDto;
import com.utility.customView.CustomEditText;
import com.utility.customView.CustomTextView;

import java.util.ArrayList;

public class ContestWinnerAdapter extends BaseAdapter {
    private Activity mActivity;
    private ViewHolder viewHolder;
    private ArrayList<ContestWinnerDto> contestWinnerDtoArrayList;
    private OnButtonListener onButtonListener;

    public ContestWinnerAdapter(Activity activity, ArrayList<ContestWinnerDto> contestWinnerDtoArrayList) {
        this.mActivity = activity;
        this.contestWinnerDtoArrayList = contestWinnerDtoArrayList;
    }

    @Override
    public int getCount() {
        return contestWinnerDtoArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = mActivity.getLayoutInflater();
        if (view == null) {
            view = layoutInflater.inflate(R.layout.contest_winner_row, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        ContestWinnerDto contestWinnerDto = contestWinnerDtoArrayList.get(position);
        viewHolder.et_percent.setText(contestWinnerDto.percentage);
        viewHolder.tv_amount.setText(contestWinnerDto.amount);
        viewHolder.tv_position.setText(contestWinnerDto.poistion);
        return view;
    }

    private class ViewHolder {
        private CustomEditText et_percent;
        private CustomTextView tv_position, tv_amount;

        public ViewHolder(View v) {
            et_percent = v.findViewById(R.id.et_percent);
            tv_position = v.findViewById(R.id.tv_position);
            tv_amount = v.findViewById(R.id.tv_amount);
        }
    }

    public void setOnButtonListener(OnButtonListener onButtonListener) {
        this.onButtonListener = onButtonListener;
    }

    public interface OnButtonListener {
        void onButtonClick(int position);
    }
}
