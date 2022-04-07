package com.illyasr.mydempviews.bean;

import android.content.Context;
import android.view.View;

import com.illyasr.mydempviews.base.adapters.BaseAdapter;
import com.illyasr.mydempviews.base.adapters.BaseHolder;

import java.util.List;

/**
 * TODO
 *
 * @author qingshilin
 * @version 1.0
 * @date 2022/3/23 11:58
 */
public class TestAdapter extends BaseAdapter<TabBean> {

    public TestAdapter(Context context, List<TabBean> datas) {
        super(context, datas, 0);
    }

    @Override
    protected void onBindData(BaseHolder baseHolder, TabBean tabBean, int postion) {

    }

    class CVH extends BaseHolder {
        public CVH(View itemView) {
            super(itemView);
        }
    }
}
