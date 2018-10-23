package com.woaiqw.library.listener;

/**
 * Created by haoran on 2018/10/11.
 */
public interface ImagePickerResultListener {

    void onPicked(Object t);

    void onPhotoTook(Object t);

    void onException(String msg);
}
