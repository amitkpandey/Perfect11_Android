package com.perfect11.help.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.perfect11.R;

/**
 * Created by Developer on 13-02-2018.
 */

public class HelpAdapter extends RecyclerView.Adapter<HelpAdapter.ViewHolder> {
    private OnButtonListener onButtonListener;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout ll_root;

        public ViewHolder(View itemView) {
            super(itemView);
            ll_root = itemView.findViewById(R.id.ll_root);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_help, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.ll_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  onButtonListener.onButtonClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public void setOnButtonListener(OnButtonListener onButtonListener) {
        this.onButtonListener = onButtonListener;
    }

    public interface OnButtonListener {
        void onButtonClick(int position);
    }
}
