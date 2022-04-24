package com.illyasr.mydempviews.ui.activity.notify;

public interface DragListener {
    /**
     * 是否将item拖动到删除处,根据状态改变颜色
     * @param isDel
     */
    void deleteState(boolean isDel);

    /**
     * 是否处于拖拽状态
     * @param isStart
     */
    void dragState(boolean isStart);
}
