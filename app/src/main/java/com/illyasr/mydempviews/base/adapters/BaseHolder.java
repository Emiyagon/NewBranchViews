package com.illyasr.mydempviews.base.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.illyasr.mydempviews.R;
import com.illyasr.mydempviews.util.GlideUtil;

public class BaseHolder extends RecyclerView.ViewHolder {
    View itemView;
    SparseArray<View> views;//存放itemview中的子view

    public BaseHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        views = new SparseArray<>();
    }

    //供adapter使用,返回holder
    public static <T extends BaseHolder> T getHolder(Context context, ViewGroup parent, int layoutId) {
        return (T) new BaseHolder(LayoutInflater.from(context).inflate(layoutId, parent, false));
    }
    //获取view
    public <T extends View> T getView(int id) {
        View view = views.get(id);
        if (view == null) {
            view = itemView.findViewById(id);
            views.put(id, view);
        }
        return (T) view;
    }

    public View getItemView() {
        return itemView;
    }
    //设置点击事件监听
    public BaseHolder setOnclickListioner(int viewId, View.OnClickListener onClickListener) {
        getView(viewId).setOnClickListener(onClickListener);
        return this;
    }

    public BaseHolder setText(int viewId, String descrp) {
        ((TextView) getView(viewId)).setText(descrp);
        return this;
    }

    public BaseHolder setText(int viewId,int resId){
        ((TextView) getView(viewId)).setText(resId);
        return this;
    }
    // 设置图片
    public BaseHolder setImageView(int imagViewId, Object url) {
//        GlideApp.with(AIApplication.mContext).load(url).error(R.drawable.ic_default).placeholder(R.drawable.ic_default)
//                .transform(new GlideRoundTransform()).into((ImageView) getView(imagViewId));
        GlideUtil.putHttpImg(url,(ImageView) getView(imagViewId));
        return this;
    }


}
