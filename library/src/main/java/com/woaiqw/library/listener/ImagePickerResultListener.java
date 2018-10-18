package com.woaiqw.library.listener;

/**
 * Created by haoran on 2018/10/11.
 */
public interface ImagePickerResultListener<T> {

    void onPicked(T t);

    void onPhotoTook(T t);

}
