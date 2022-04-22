package com.illyasr.mydempviews.ui.activity.notify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import com.illyasr.mydempviews.MainPresent;
import com.illyasr.mydempviews.R;
import com.illyasr.mydempviews.base.BaseActivity;
import com.illyasr.mydempviews.databinding.ActivityNotifyBinding;
import com.illyasr.mydempviews.ui.activity.GetVideoActivity;
import com.illyasr.mydempviews.ui.activity.bilibili.BiliBiliActivity;
import com.illyasr.mydempviews.util.DonwloadSaveImg;
import com.illyasr.mydempviews.util.GlideUtil;
import com.illyasr.mydempviews.view.ActionSheetDialog;
import com.illyasr.mydempviews.view.MyAlertDialog;

import java.util.List;

/**
 * 纪念日
 * 为了增加点趣味性,增加了拖拽删除和拖拽排序的功能
 * 具体功能参照pictureselector
 * 用了itemretor
 */
public class NotifyActivity extends BaseActivity<ActivityNotifyBinding, MainPresent> {

    private String test = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fitem%2F201910%2F10%2F20191010152014_dzmkn.png&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1653188184&t=50646a6b5194b2a611e8dd7ebb7f977f";
    private NotifyAdapter adapter;
    private NotifyDao ordDao;
    private NotifyDateBase wordDatabase;


    @Override
    protected void initData() {

        mBindingView.title.tvRight.setText("添加");
        mBindingView.title.tvRight.setOnClickListener(v -> {

        });
        adapter = new NotifyAdapter(this)
        .setOnCardClick((pos,bean) -> {
            new ActionSheetDialog(this)
                    .builder()
                    .setTitle("请选择操作")
                    .setCancelable(true)
                    .setCanceledOnTouchOutside(true)
                    .addSheetItem("查看详情", ActionSheetDialog.SheetItemColor.Blue, which -> {

                    })
                    .addSheetItem("删除", ActionSheetDialog.SheetItemColor.Blue, which -> {
                       ordDao.deleteWords(bean);
                       adapter.deleteBean(pos);
                    })
                    .show();
        });
        mBindingView.rv.setAdapter(adapter);
        getCoume();
    }

    private void getCoume() {
        wordDatabase = Room.databaseBuilder(this,NotifyDateBase.class,"word_database") //new a database
                .allowMainThreadQueries()
                .build();
        // new a dao
        ordDao = wordDatabase.getNotifyDao();
        notifyThis();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (wordDatabase != null && ordDao != null) {
            notifyThis();
        }
    }

    private void notifyThis() {
        List<NotifyBean> list = ordDao.getAllWords();
        adapter.setList(list);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_notify;
    }
}