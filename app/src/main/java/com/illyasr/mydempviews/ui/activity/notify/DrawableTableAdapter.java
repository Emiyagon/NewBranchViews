package com.illyasr.mydempviews.ui.activity.notify;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.illyasr.mydempviews.R;
import com.illyasr.mydempviews.databinding.ItemRemBinding;
import com.illyasr.mydempviews.ui.activity.canender.CalenderActivity;

import java.util.Arrays;
import java.util.List;

/**
 * TODO
 *
 * @author qingshilin
 * @version 1.0
 * @date 2022/4/23 19:08
 */
public class DrawableTableAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<String> list = Arrays.asList("日期计算器","日历");

    public DrawableTableAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TOP_IV) {
            return  new TopVH(LayoutInflater.from(context).inflate(R.layout.item_top, parent, false));
        }
        return new PVh(LayoutInflater.from(context).inflate(R.layout.item_rem,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PVh) {
            PVh h = (PVh) holder;
            h.binding.stvSp.setText(list.get(position-1));
            h.binding.stvSp.setOnClickListener(v -> {
                Intent intent = new Intent();
                intent.setClass(context, (position ) == 1 ? JSActivity.class : CalenderActivity.class);
                context.startActivity(intent);
                if (endInterface != null) {
                    endInterface.end();
                }
            });
        }
    }

    public DrawableTableAdapter setEndInterface(EndInterface endInterface) {
        this.endInterface = endInterface;
        return this;
    }

    private EndInterface endInterface;
    public interface EndInterface {
        void end();
    }


    @Override
    public int getItemCount() {
        return list.size()+1;
    }

    private int TOP_IV = 0xaf;
    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0xaf;
        }
        return super.getItemViewType(position);
    }

    class TopVH extends RecyclerView.ViewHolder {
        public TopVH(@NonNull View itemView) {
            super(itemView);
        }
    }
    class PVh extends RecyclerView.ViewHolder {
        ItemRemBinding binding;
        public PVh(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
