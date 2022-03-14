package com.illyasr.mydempviews.view.roll;

/**
 * Created by bullet on 2018/8/1.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.illyasr.mydempviews.R;

import java.util.List;

/**
 * 仿淘宝首页的 淘宝头条滚动的自定义View
 */
public class UPMarqueeView extends ViewFlipper {
    private Context mContext;
    /**是否开启动画*/
    private boolean isSetAnimDuration = true;
    /**时间间隔*/
    private int interval = 3000;
    /**动画时间 */
    private int animDuration = 500;



    public UPMarqueeView(Context context) {
        super(context);
        init(context, null, 0);
    }
    public UPMarqueeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }
    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.mContext = context;
        setFlipInterval(interval);
        if (isSetAnimDuration) {
            Animation animIn = AnimationUtils.loadAnimation(mContext, R.anim.anim_bottom_in);
            animIn.setDuration(animDuration);
            setInAnimation(animIn);
            Animation animOut = AnimationUtils.loadAnimation(mContext, R.anim.anim_top_out);
            animOut.setDuration(animDuration);
            setOutAnimation(animOut);
        }
    }
    /**
     * 设置循环滚动的View数组
     * @param
     */
    public void setViews(final List<UPMarqueeViewData> datas) {
        if (datas == null || datas.size() == 0) {
            return;
        }
        int size = datas.size();
        for (int i = 0; i < size; i += 2) {
            final int position = i;
            //根布局
            final LinearLayout item = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.item, null);
            //设置监听
            item.findViewById(R.id.rl).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, "你点击了" + datas.get(position).getValue(), Toast.LENGTH_SHORT).show();
                }
            });
            //控件赋值
            ((TextView) item.findViewById(R.id.tv1)).setText(datas.get(position).getValue());
            ((TextView) item.findViewById(R.id.title1)).setText(datas.get(position).getTitle());
            //当数据是奇数时，最后那个item仅有一项
            if (position + 1 < size) {
                item.findViewById(R.id.rl2).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Toast.makeText(mContext, "你点击了" + datas.get(position + 1).getValue(), Toast.LENGTH_SHORT).show();
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClick(position,item);
                        }
                    }
                });
                ((TextView) item.findViewById(R.id.tv2)).setText(datas.get(position + 1).getValue());
                ((TextView) item.findViewById(R.id.title2)).setText(datas.get(position + 1).getTitle());
            } else {
                item.findViewById(R.id.rl2).setVisibility(View.GONE);
            }
            addView(item);
        }
    }
    public boolean isSetAnimDuration() {
        return isSetAnimDuration;
    }
    public void setSetAnimDuration(boolean isSetAnimDuration) {
        this.isSetAnimDuration = isSetAnimDuration;
    }
    public int getInterval() {
        return interval;
    }
    public void setInterval(int interval) {
        this.interval = interval;
    }
    public int getAnimDuration() {
        return animDuration;
    }
    public void setAnimDuration(int animDuration) {
        this.animDuration = animDuration;
    }

    private OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View view);
    }

}