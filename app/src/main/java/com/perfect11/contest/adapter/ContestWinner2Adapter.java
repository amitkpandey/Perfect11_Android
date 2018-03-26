package com.perfect11.contest.adapter;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.perfect11.R;
import com.perfect11.contest.dto.ContestWinnerDto;
import com.utility.customView.CustomEditText;
import com.utility.customView.CustomTextView;

import java.util.ArrayList;

public class ContestWinner2Adapter extends BaseAdapter {
    private Activity mActivity;
    private ViewHolder viewHolder;
    private ArrayList<ContestWinnerDto> contestWinnerDtoArrayList;
    private OnButtonListener onButtonListener;
private float total_amount=0;
    public ContestWinner2Adapter(Activity activity, ArrayList<ContestWinnerDto> contestWinnerDtoArrayList, float total_amount) {
        this.mActivity = activity;
        this.contestWinnerDtoArrayList = contestWinnerDtoArrayList;
        this.total_amount=total_amount;
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
    public View getView(final int position, View view,final ViewGroup viewGroup) {
        LayoutInflater layoutInflater = mActivity.getLayoutInflater();
       /* if (view == null) {*/
            view = layoutInflater.inflate(R.layout.contest_winner_row, viewGroup, false);
            viewHolder = new ViewHolder(view);
          /*  view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }*/
        ContestWinnerDto contestWinnerDto = contestWinnerDtoArrayList.get(position);
        viewHolder.et_percent.setText(""+contestWinnerDto.percentage);
        viewHolder.tv_amount.setText(""+contestWinnerDto.amount);
        viewHolder.tv_position.setText(""+contestWinnerDto.poistion);

        viewHolder.et_percent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("percentage: "+s);

                float percentage= 0;
                try {
                    percentage = Float.parseFloat(s.toString());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    percentage =0;
                }
                System.out.println("percentage: "+s);
                if(position!=0)
                {
                    if(contestWinnerDtoArrayList.get(position-1).percentage< percentage)
                    {
                        Toast.makeText(mActivity, "Wrong input", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        contestWinnerDtoArrayList.get(position).percentage=percentage;
                        contestWinnerDtoArrayList.get(position).amount=total_amount * (percentage / 100);
                    }
                }else
                {
                    contestWinnerDtoArrayList.get(position).percentage=percentage;
                    contestWinnerDtoArrayList.get(position).amount=total_amount * (percentage / 100);
                }

                viewHolder.tv_amount.setText(""+ contestWinnerDtoArrayList.get(position).amount);
                System.out.println(""+contestWinnerDtoArrayList.get(position).amount);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
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
