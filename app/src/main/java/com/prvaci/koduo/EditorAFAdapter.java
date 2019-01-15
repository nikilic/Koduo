package com.prvaci.koduo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class EditorAFAdapter extends RecyclerView.Adapter<EditorAFAdapter.ViewHolder> {

    private List<String> mData;
    private LayoutInflater mInflater;
    private EditorAFAdapter.ItemClickListener mClickListener;

    EditorAFAdapter(Context context, List<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @Override
    public EditorAFAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_editoraf, parent, false);
        return new EditorAFAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EditorAFAdapter.ViewHolder holder, int position) {
        String name = mData.get(position);
        holder.tvName.setText(name);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvName;

        ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.textView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    void setClickListener(EditorAFAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
