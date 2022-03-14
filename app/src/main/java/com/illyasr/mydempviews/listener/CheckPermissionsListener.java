package com.illyasr.mydempviews.listener;

import java.util.List;

/**
 * Created by bullet on 2018/8/11.
 */
public interface CheckPermissionsListener {
    void onGranted();
    void onDenied(List<String> permissions);
}
