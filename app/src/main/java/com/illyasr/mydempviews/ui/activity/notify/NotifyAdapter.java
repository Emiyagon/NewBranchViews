package com.illyasr.mydempviews.ui.activity.notify;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.illyasr.bilibili.biliplayer.utils.StringUtils;
import com.illyasr.mydempviews.R;
import com.illyasr.mydempviews.base.adapters.BaseAdapter;
import com.illyasr.mydempviews.base.adapters.BaseHolder;
import com.illyasr.mydempviews.databinding.ItemGridCardBinding;
import com.illyasr.mydempviews.service.MyNetPics;
import com.illyasr.mydempviews.util.StringUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * TODO
 *
 * @author qingshilin
 * @version 1.0
 * @date 2022/4/22 10:35
 */
public class NotifyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<NotifyBean> list = new ArrayList<>();

    /**
     * 添加事件并刷新
     * @param bean
     */
    public void addBeans(NotifyBean bean) {
        list.add(bean);
        notifyDataSetChanged();
    }

    /**
     *  删除
     * @param pos
     */
    public void deleteBean(int pos) {
        notifyItemRemoved(pos);
        list.remove(pos);
        notifyItemRangeChanged(pos,getItemCount()-pos);
    }

    public NotifyAdapter(Context context, List<NotifyBean> list) {
        this.context = context;
        this.list = list;
    }
    public NotifyAdapter(Context context) {
        this.context = context;
    }

    public NotifyAdapter setList(List<NotifyBean> list) {
        this.list = list;
        return this;
    }

    private OnCardClick onCardClick;

    public NotifyAdapter setOnCardClick(OnCardClick onCardClick) {
        this.onCardClick = onCardClick;
        return this;
    }

    public interface OnCardClick{
        void onclick(int pos,NotifyBean bean);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BVH(LayoutInflater.from(context).inflate(R.layout.item_grid_card,parent,false));
    }

    private static long oneDayMillion = 24*60*60*1000l;
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        BVH holder = (BVH) viewHolder;
        NotifyBean bean = list.get(position);
        boolean isEnd = bean.getEndDate()-bean.getNowDate()>0;//结束了
        holder.binding.card.setCardBackgroundColor(isEnd?context.getResources().getColor(R.color.light_blue_A200)
                :context.getResources().getColor(R.color.actionsheet_blue));
        holder.binding.stvType.setText(isEnd?"已过":"还剩");
        holder.binding.stvTitle.setText(bean.getTitle());
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(bean.getEndDate());
        holder.binding.tvDate.setText(String.format("%d/%d/%d"));
        holder.binding.tvS.setText(StringUtil.returnWeekString(c.get(Calendar.DAY_OF_WEEK)));

        long day = Math.abs(bean.getEndDate()-bean.getNowDate()) / oneDayMillion ;
        holder.binding.tvNum.setText(day+"");

        holder.binding.card.setOnClickListener(v -> {
            if (onCardClick != null) {
                onCardClick.onclick(position,bean);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class BVH extends RecyclerView.ViewHolder {
        ItemGridCardBinding binding;
        public BVH(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
