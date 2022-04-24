package com.illyasr.mydempviews.ui.activity.notify;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 纪念日
 * 为了增加点趣味性,增加了拖拽删除和拖拽排序的功能
 * 具体功能参照pictureselector
 * 本来打算用数据库的,但是发现room好像问题还不少
 * 用了itemretor
 */
public class NotifyActivity extends BaseActivity<ActivityNotifyBinding, MainPresent> {

    private String test = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fitem%2F201910%2F10%2F20191010152014_dzmkn.png&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1653188184&t=50646a6b5194b2a611e8dd7ebb7f977f";
    private NotifyAdapter adapter;
//    private NotifyDao ordDao;
//    private NotifyDateBase wordDatabase;

    private ItemTouchHelper mItemTouchHelper;
    private DragListener mDragListener;

    private boolean isUpward;
    private boolean needScaleBig = true;
    private boolean needScaleSmall = true;

    @Override
    protected void initData() {

        mBindingView.title.tvRight.setText("添加");
//        mBindingView.title.imgLeft.setImageResource(R.drawable.table);
//        GlideUtil.putHttpImg(R.drawable.table,mBindingView.title.imgLeft);
//        mBindingView.title.imgLeft.setScaleType(ImageView.ScaleType.FIT_XY);
        mBindingView.title.llBack.setOnClickListener(v -> {
            mBindingView.rlPe.openDrawer(Gravity.LEFT);
        });
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
//                       ordDao.deleteWords(bean);
                       adapter.delete(pos);
                    })
                    .show();
        });
        mBindingView.rv.setAdapter(adapter);
        getCoume();

        //侧边栏
        mBindingView.frameDraw.setAdapter(new DrawableTableAdapter(this)
        .setEndInterface(() -> {
            mBindingView.rlPe.closeDrawer(Gravity.LEFT);
        })
        );


        onPotc();
    }

    private void onPotc() {


        mDragListener = new DragListener() {
            @Override
            public void deleteState(boolean isDelete) {
                if (isDelete) {
                    mBindingView.tvDeleteText.setText("松手即可删除");
                    mBindingView.tvDeleteText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.ic_let_go_delete, 0, 0);
                } else {
                    mBindingView.tvDeleteText.setText("拖动到此处删除");
                    mBindingView.tvDeleteText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.picture_icon_delete, 0, 0);
                }

            }

            @Override
            public void dragState(boolean isStart) {
                int visibility = mBindingView.tvDeleteText.getVisibility();
                if (isStart) {
                    if (visibility == View.GONE) {
                        mBindingView.tvDeleteText.animate().alpha(1).setDuration(300).setInterpolator(new AccelerateInterpolator());
                        mBindingView.tvDeleteText.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (visibility == View.VISIBLE) {
                        mBindingView. tvDeleteText.animate().alpha(0).setDuration(300).setInterpolator(new AccelerateInterpolator());
                        mBindingView.tvDeleteText.setVisibility(View.GONE);
                    }
                }
            }
        };
        mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public boolean isLongPressDragEnabled() {
                return true;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int itemViewType = 0;

                return makeMovementFlags(ItemTouchHelper.DOWN
                        |ItemTouchHelper.UP|ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT,0);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                //得到item原来的position
                try {
                    int fromPosition = viewHolder.getAdapterPosition();
                    //得到目标position
                    int toPosition = target.getAdapterPosition();
                    int itemViewType = target.getItemViewType();
//                    if (itemViewType != GridImageAdapter.TYPE_CAMERA) {
                    if (fromPosition < toPosition) {
                        for (int i = fromPosition; i < toPosition; i++) {
                            Collections.swap(adapter.getData(), i, i + 1);
                        }
                    } else {
                        for (int i = fromPosition; i > toPosition; i--) {
                            Collections.swap(adapter.getData(), i, i - 1);
                        }
                    }
                    adapter.notifyItemMoved(fromPosition, toPosition);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                int itemViewType = viewHolder.getItemViewType();
//                if (itemViewType != GridImageAdapter.TYPE_CAMERA) {
                if (itemViewType != -1) {
                    if (null == mDragListener) {
                        return;
                    }
                    if (needScaleBig) {
                        //如果需要执行放大动画
                        viewHolder.itemView.animate().scaleXBy(0.1f).scaleYBy(0.1f).setDuration(100);
                        //执行完成放大动画,标记改掉
                        needScaleBig = false;
                        //默认不需要执行缩小动画，当执行完成放大 并且松手后才允许执行
                        needScaleSmall = false;
                    }
                    int sh = recyclerView.getHeight() + mBindingView.tvDeleteText.getHeight();
                    int ry = mBindingView.tvDeleteText.getBottom() - sh;
                    if (dY >= ry) {
                        //拖到删除处
                        mDragListener.deleteState(true);
                        if (isUpward) {
                            //在删除处放手，则删除item
                            viewHolder.itemView.setVisibility(View.INVISIBLE);
                            adapter.delete(viewHolder.getAdapterPosition());
                            resetState();
                            return;
                        }
                    } else {//没有到删除处
                        if (View.INVISIBLE == viewHolder.itemView.getVisibility()) {
                            //如果viewHolder不可见，则表示用户放手，重置删除区域状态
                            mDragListener.dragState(false);
                        }
                        if (needScaleSmall) {//需要松手后才能执行
                            viewHolder.itemView.animate().scaleXBy(1f).scaleYBy(1f).setDuration(100);
                        }
                        mDragListener.deleteState(false);
                    }
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            }

            @Override
            public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
//                int itemViewType = viewHolder != null ? viewHolder.getItemViewType() : GridImageAdapter.TYPE_CAMERA;
                int itemViewType = viewHolder != null ? viewHolder.getItemViewType() : -1;
//                if (itemViewType != GridImageAdapter.TYPE_CAMERA) {
                if (itemViewType != -1) {
                    if (ItemTouchHelper.ACTION_STATE_DRAG == actionState && mDragListener != null) {
                        mDragListener.dragState(true);
                    }
                    super.onSelectedChanged(viewHolder, actionState);
                }
            }

            @Override
            public long getAnimationDuration(@NonNull RecyclerView recyclerView, int animationType, float animateDx, float animateDy) {
                needScaleSmall = true;
                isUpward = true;
                return super.getAnimationDuration(recyclerView, animationType, animateDx, animateDy);
            }

            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int itemViewType = viewHolder.getItemViewType();
//                if (itemViewType != GridImageAdapter.TYPE_CAMERA) {
                if (itemViewType != -1) {
                    viewHolder.itemView.setAlpha(1.0f);
                    super.clearView(recyclerView, viewHolder);
                    adapter.notifyDataSetChanged();
                    resetState();
                }
            }
        });
        mItemTouchHelper.attachToRecyclerView(mBindingView.rv);
    }

    private void getCoume() {
//        wordDatabase = NotifyDateBase.getInstance(this);
        // new a dao
//        ordDao = wordDatabase.getNotifyDao();
//        notifyThis();
        List<NotifyBean> list = new ArrayList<>();
        Date date = new Date();
        list.add(new NotifyBean("无标题",date.getTime(),date.getTime()+10000000));
        adapter.setList(list);
    }


    @Override
    protected void onResume() {
        super.onResume();
//        if (wordDatabase != null && ordDao != null) {
//            notifyThis();
//        }
    }

    /**
     * 重置
     */
    private void resetState() {
        if (mDragListener != null) {
            mDragListener.deleteState(false);
            mDragListener.dragState(false);
        }
        isUpward = false;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_notify;
    }
}