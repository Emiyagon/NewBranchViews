package com.illyasr.mydempviews.phone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.illyasr.mydempviews.R;
import com.illyasr.mydempviews.databinding.DialogShowItemBinding;

import java.util.ArrayList;
import java.util.List;

public class PhoneAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<PhoneDto> list = new ArrayList<>();

    public PhoneAdapter(Context context, List<PhoneDto> list) {
        this.context = context;
        this.list = list;
    }

    private OnTxlInterface listener;
    public interface OnTxlInterface{
        void onClider(String name, String tele);
    }

    public PhoneAdapter setListener(OnTxlInterface listener) {
        this.listener = listener;
        return this;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PE(LayoutInflater.from(context).inflate(R.layout.dialog_show_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        PE holder = (PE) viewHolder;
        holder.binding.llClick.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClider(list.get(position).getName(),list.get(position).getTelPhone());
            }
        });
        holder.binding.name.setText(list.get(position).getName());
        holder.binding.tele.setText(list.get(position).getTelPhone());
//        holder.binding.tele.setTextColor(context.getResources().getColor(R.color.transparent));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class PE extends RecyclerView.ViewHolder {
        DialogShowItemBinding binding;
        public PE(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
