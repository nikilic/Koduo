package com.prvaci.koduo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class AFEditorAdapter  extends RecyclerView.Adapter<AFEditorAdapter.ViewHolder>{
    private List<AFEditorItem> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    AFEditorAdapter(Context context, List<AFEditorItem> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_afeditor, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String name = mData.get(position).getName();
        String data = mData.get(position).getData();
        holder.tvName.setText(name);
        holder.tvData.setText(data);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvName;
        TextView tvData;
        ImageButton bDown,bUp,bDelete;

        ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvData = itemView.findViewById(R.id.tvData);
            bDown = itemView.findViewById(R.id.bDown);
            bUp = itemView.findViewById(R.id.bUp);
            bDelete = itemView.findViewById(R.id.bDelete);
            itemView.setOnClickListener(this);
            bDown.setOnClickListener(this);
            bUp.setOnClickListener(this);
            bDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null)
                mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
