package com.woaiqw.library;

import android.content.Context;
import android.view.View;

/**
 * Created by haoran on 2018/10/17.
 */
public interface ImageLoader<T extends View> {

    void displayImage(Context context, Object path, T imageView);

}
