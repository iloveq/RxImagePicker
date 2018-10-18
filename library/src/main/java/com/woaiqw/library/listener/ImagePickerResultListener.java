package com.woaiqw.library.listener;

import java.util.List;

/**
 * Created by haoran on 2018/10/11.
 */
public interface ImagePickerResultListener<T> {

    void onPicked(List<T> t);

    void onPhotoTook(T t);

}
