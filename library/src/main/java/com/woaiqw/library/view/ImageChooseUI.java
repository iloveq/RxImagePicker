package com.woaiqw.library.view;

import android.app.Activity;

import java.lang.ref.WeakReference;

/**
 * Created by haoran on 2018/10/12.
 */
public class ImageChooseUI {

    WeakReference<? extends Activity> source;

    Theme theme;

    public ImageChooseUI(WeakReference<? extends Activity> source) {
        this.source = source;
    }


    private class Theme {


    }


}
