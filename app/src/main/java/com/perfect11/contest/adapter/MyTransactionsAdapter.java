package com.perfect11.contest.adapter;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.perfect11.R;
import com.perfect11.account.dto.MyTransectionDto;
import com.utility.DateUtility;
import com.utility.customView.CustomTextView;

import java.util.ArrayList;

/**
 * Created by Developer on 13-02-2018.
 */

public class MyTransactionsAdapter extends RecyclerView.Adapter<MyTransactionsAdapter.ViewHolder> {
    private OnButtonListener onButtonListener;
    private ArrayList<MyTransectionDto> mshopkeeperlist;
    private FragmentActivity mactivity;

    public MyTransactionsAdapter(ArrayList<MyTransectionDto> shopkeeperlist, FragmentActivity activity) {
        mshopkeeperlist = shopkeeperlist;
        mactivity = activity;
    }

    public void setData(ArrayList<MyTransectionDto> shopkeeperlist) {
        mshopkeeperlist = shopkeeperlist;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rl_01, rl_id;
        private CustomTextView tv_date, tv_amount, tv_id/*, tv_applied_for*/, tv_Details;

        public ViewHolder(View itemView) {
            super(itemView);
            rl_01 = itemView.findViewById(R.id.rl_01);
            rl_id = itemView.findViewById(R.id.rl_id);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_amount = itemView.findViewById(R.id.tv_amount);
            tv_id = itemView.findViewById(R.id.tv_id);
          /*  tv_applied_for = itemView.findViewById(R.id.tv_applied_for);*/
            tv_Details = itemView.findViewById(R.id.tv_Details);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tv_amount.setText("Rs. " + mshopkeeperlist.get(position).amount + "/-");
        holder.tv_date.setText(DateUtility.getDayOfWeekFromDate(mshopkeeperlist.get(position).created_date));
        holder.tv_id.setText(mshopkeeperlist.get(position).payment_id);
     /*   holder.tv_applied_for.setText(mshopkeeperlist.get(position).applied_for);*/
        holder.tv_Details.setText(mshopkeeperlist.get(position).description);

        if (mshopkeeperlist.get(position).payment_id.trim().equals("")) {
            holder.rl_id.setVisibility(View.GONE);
        } else {
            holder.rl_id.setVisibility(View.VISIBLE);
        }

/*        holder.rl_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonListener.onButtonClick(mshopkeeperlist.get(position));
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return mshopkeeperlist.size();
    }

    public void setOnButtonListener(OnButtonListener onButtonListener) {
        this.onButtonListener = onButtonListener;
    }

    public interface OnButtonListener {
        void onButtonClick(MyTransectionDto position);
    }
}
