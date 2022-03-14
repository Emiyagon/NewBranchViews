package com.illyasr.mydempviews.adapter;

import android.content.Context;

import com.illyasr.mydempviews.base.adapters.BaseAdapter;
import com.illyasr.mydempviews.base.adapters.BaseHolder;

import java.util.List;

public class TestAdapter extends BaseAdapter<String> {

    public TestAdapter(Context context, List<String> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    protected void onBindData(BaseHolder baseHolder, String s, int postion) {


    }
}
