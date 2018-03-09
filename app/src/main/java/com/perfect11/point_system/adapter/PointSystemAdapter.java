package com.perfect11.point_system.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.perfect11.R;
import com.utility.customView.CustomTextView;

/**
 * Created by Developer on 13-02-2018.
 */

public class PointSystemAdapter extends RecyclerView.Adapter<PointSystemAdapter.ViewHolder> {
    public class ViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout ll_root;
        private CustomTextView ctv_type,ctv_t20,ctv_odi,ctv_test;
        public ViewHolder(View itemView) {
            super(itemView);
            ll_root=itemView.findViewById(R.id.ll_root);
            ctv_type=itemView.findViewById(R.id.ctv_type);
            ctv_t20=itemView.findViewById(R.id.ctv_t20);
            ctv_odi=itemView.findViewById(R.id.ctv_odi);
            ctv_test=itemView.findViewById(R.id.ctv_test);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if(viewType==0) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_point_system_header, parent, false);
        }else
        {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_point_system, parent, false);
        }
        ViewHolder viewHolder=new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        switch (position)
        {
            case 1:
                holder.ctv_type.setText("Run");
                holder.ctv_t20.setText("1");
                holder.ctv_odi.setText("3");
                holder.ctv_test.setText("2");
                break;
            case 2:
                holder.ctv_type.setText("Boundary");
                holder.ctv_t20.setText("1");
                holder.ctv_odi.setText("2");
                holder.ctv_test.setText("3");
                break;
            case 3:

                holder.ctv_type.setText("Six");
                holder.ctv_t20.setText("2");
                holder.ctv_odi.setText("1");
                holder.ctv_test.setText("2");
                break;
            case 4:

                holder.ctv_type.setText("Half Century");
                holder.ctv_t20.setText("2");
                holder.ctv_odi.setText("2");
                holder.ctv_test.setText("1");
                break;
            case 5:

                holder.ctv_type.setText("Century");
                holder.ctv_t20.setText(".5");
                holder.ctv_odi.setText("1");
                holder.ctv_test.setText("2");
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return 6;
    }

}
