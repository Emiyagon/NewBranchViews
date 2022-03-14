package com.illyasr.mydempviews.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.illyasr.mydempviews.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SpeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private int[] list;
    private int choose = 0;

    public SpeedAdapter(Context context, int[] list) {
        this.context = context;
        this.list = list;
    }

    public SpeedAdapter setChoose(int choose) {
        this.choose = choose;
        notifyDataSetChanged();
        return this;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(context).inflate(R.layout.item_text,null));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        VH holder = (VH) viewHolder;
        holder.tv.setText(list[position]+"");

        // bg_tv_un  bg_tv
        holder.tv.setBackgroundResource( position==choose? R.drawable.bg_tv:R.drawable.bg_tv_un);

        holder.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onSpeedClicklistener != null) {
                    onSpeedClicklistener.OnCLick(position,list[position]);
                    setChoose(position);
                }
            }
        });

    }

    OnSpeedClicklistener onSpeedClicklistener;

    public SpeedAdapter setOnSpeedClicklistener(OnSpeedClicklistener onSpeedClicklistener) {
        this.onSpeedClicklistener = onSpeedClicklistener;
        return this;

    }

    public interface OnSpeedClicklistener {
        void OnCLick(int pos,int speed);
    }

    @Override
    public int getItemCount() {
        return list.length;
    }
    public class VH extends RecyclerView.ViewHolder {
        TextView tv;
        public VH(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tps);
        }
    }

}
