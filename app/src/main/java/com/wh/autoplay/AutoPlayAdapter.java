package com.wh.autoplay;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.wh.R;

import java.util.List;

public class AutoPlayAdapter extends RecyclerView.Adapter<AutoPlayAdapter.BaseViewHolder> {
    private final List<String> mData;

    private OnAutoPlayClickListener mOnAutoPlayClickListener;

    public AutoPlayAdapter(List<String> list, OnAutoPlayClickListener listener) {
        this.mData = list;
        this.mOnAutoPlayClickListener = listener;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_auto_play, parent, false);
        BaseViewHolder holder = new BaseViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, final int position) {
        final int currentPosition = position % mData.size();
        final String data = mData.get(currentPosition);
        holder.tv.setText(data);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnAutoPlayClickListener != null) {
                    mOnAutoPlayClickListener.onClick(v, currentPosition, data);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    class BaseViewHolder extends RecyclerView.ViewHolder {
        TextView tv;

        public BaseViewHolder(View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.text);
        }
    }

    public interface OnAutoPlayClickListener {
        void onClick(View view, int position, String data);
    }
}