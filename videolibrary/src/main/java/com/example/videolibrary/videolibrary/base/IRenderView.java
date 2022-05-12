package com.example.videolibrary.videolibrary.base;

import android.view.View;

public interface IRenderView {

    View getRenderView();

    void setPlayer(IMediaPlayer player);

    IMediaPlayer getPlayer();

    void setVideoSize(int width, int height);
}
