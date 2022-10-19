package com.illyasr.mydempviews.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.coorchice.library.SuperTextView;
import com.illyasr.mydempviews.R;
import com.illyasr.mydempviews.bean.TabBean;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<TabBean> list;

    public MainAdapter(Context context, List<TabBean> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TabVH(LayoutInflater.from(context).inflate(R.layout.item_rem, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, @SuppressLint("RecyclerView") int position) {
        TabVH holder = (TabVH) viewHolder;
        holder.stvSp.setText(list.get(position).getName());
        holder.stvSp.setTextColor(list.get(position).getTextColor()> 0 ? list.get(position).getTextColor():0xff333333 );

        holder.stvSp.setOnClickListener(view -> {
            if (onRem != null) {
                onRem.OnTo(view,position,list.get(position).getClickId());
            }
        });

    }

    OnRem onRem;

    public MainAdapter setOnRem(OnRem onRem) {
        this.onRem = onRem;
        return this;
    }

    public interface OnRem {
        void OnTo(View view,int clickPos,int type);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class TabVH extends RecyclerView.ViewHolder {
//        @BindView(R.id.stv_sp)
        SuperTextView stvSp;

        public TabVH(@NonNull View itemView) {
            super(itemView);
//            ButterKnife.bind(this, itemView);
            stvSp = itemView.findViewById(R.id.stv_sp);
        }
    }



}
