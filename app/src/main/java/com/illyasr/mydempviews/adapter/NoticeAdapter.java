package com.illyasr.mydempviews.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.illyasr.mydempviews.R;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeViewHolder>{

    private int color = 0xffffffff;

    public NoticeAdapter(int color) {
        this.color = color;
    }

    public NoticeAdapter( ) {

    }

    List<String> mDatas = new ArrayList<>();

    public void setmDatas(List<String> mDatas) {
        this.mDatas = mDatas;
    }

    @Override
    public NoticeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NoticeViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_user_notice,parent,false));
    }

    @Override
    public void onBindViewHolder(NoticeViewHolder holder, final int position) {
        //  因为一开始就在最前面,这样感官很不好,所以第一个设置空的,然后
        if (position==0){
            holder.setTextViewValue("----------------",0x00000000);

        }else {
            final  int tempPos = position%(mDatas.size());
            holder.setTextViewValue(mDatas.get(tempPos),color);
        }


    }

    @Override
    public int getItemCount() {
        return 100001;
    }
}

class NoticeViewHolder extends RecyclerView.ViewHolder{
    private TextView textView;
    public NoticeViewHolder(View itemView) {
        super(itemView);
        textView =  itemView.findViewById(R.id.tab_title);
    }

    public void setTextViewValue(String data,int color){
        textView.setText(data);
        textView.setTextColor(color);
    }


}

