package com.illyasr.mydempviews.util.xpopup;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.illyasr.mydempviews.R;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.ImageViewerPopupView;
import com.lxj.xpopup.interfaces.OnSrcViewUpdateListener;

import java.util.List;

/**
 * TODO
 *
 * @author qingshilin
 * @version 1.0
 * @date 2022/3/18 11:17
 */
public class XPopupUtil {
    /**
     * 展示多图
     * @param context
     * @param rv 需要的list,必须是object(得空给他改成泛型)
     * @param img imageview,点击传进来的那个view
     * @param position 点击位置
     * @param xxImg 详细写法看注释
     */
    public void OnThumbList(Context context, List<Object> rv, ImageView img, int position,ImageView xxImg) {
        new XPopup.Builder(context).asImageViewer(img, position, rv, (popupView, i) -> {
//  popupView.updateSrcView((ImageView) (recyclerView.getLayoutManager()).findViewByPosition(R.id.Img_poster));
            popupView.updateSrcView(xxImg);

        }, new LargeImgLoader()).show();
    }


    /**
     *  单图展示,一般用不到
     * @param context
     * @param img
     * @param url
     */
    public void onThumbSingle(Context context,ImageView img ,Object url) {
        new XPopup.Builder(context)
                .asImageViewer(img, url, new LargeImgLoader())
                .show();
    }
}
