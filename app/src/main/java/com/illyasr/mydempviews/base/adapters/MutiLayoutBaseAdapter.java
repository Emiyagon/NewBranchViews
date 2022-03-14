package com.illyasr.mydempviews.base.adapters;

import android.content.Context;

import java.util.List;

/**
 * 封装一个多种布局BaseAdapter。
 */
public abstract class MutiLayoutBaseAdapter<T> extends BaseAdapter<T> {

    public MutiLayoutBaseAdapter(Context context, List<T> datas, int[] layoutIds) {
        super(context, datas, layoutIds);
    }
    @Override
    public int getItemViewType(int position) {
        return getItemType(position);
    }

    @Override
    protected void onBindData(BaseHolder baseHolder, T t, int postion) {
        onBinds(baseHolder,t,postion,getItemViewType(postion));
    }

    //子类实现得到具体的子类布局的方法
    public abstract int getItemType(int position);

    //子类实现对不同的item进行操作
    protected abstract void onBinds(BaseHolder baseHolder, T t, int postion, int itemViewType);


}
