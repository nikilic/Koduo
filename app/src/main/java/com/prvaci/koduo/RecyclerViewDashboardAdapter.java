package com.prvaci.koduo;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewDashboardAdapter  extends RecyclerView.Adapter<RecyclerViewDashboardAdapter.ViewHolder>{
    private List<AppDashboard> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    RecyclerViewDashboardAdapter(Context context, List<AppDashboard> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_dashboard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String name = mData.get(position).getName();
        String runs = mData.get(position).getRuns();
        String shortname = mData.get(position).getShortname();
        holder.tvName.setText(name);
        holder.tvRuns.setText(runs);
        holder.tvShortname.setText(shortname);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvName;
        TextView tvRuns;
        TextView tvShortname;

        ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvRuns = itemView.findViewById(R.id.tvRuns);
            tvShortname = itemView.findViewById(R.id.tvShortname);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
