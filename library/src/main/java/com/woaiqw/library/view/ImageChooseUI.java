package com.woaiqw.library.view;

import android.app.Activity;
import android.support.annotation.StyleRes;

import com.woaiqw.library.annotation.ResultType;

import java.lang.ref.WeakReference;

/**
 * Created by haoran on 2018/10/12.
 */
public class ImageChooseUI {

    WeakReference<? extends Activity> source;

    @StyleRes
    int theme;

    int gridColumns;

    @ResultType
    int resultType;

    public ImageChooseUI(WeakReference<? extends Activity> source, @StyleRes int theme, int gridColumns, int resultType) {
        this.source = source;
        this.theme = theme;
        this.gridColumns = gridColumns;
        this.resultType = resultType;
    }

    public void createUI() {
        if (source == null || theme == 0) {
            throw new RuntimeException(" source or theme must be createFactory ");
        }
        ImageChooseActivity.startImageChooseActivityForResult(source, theme, gridColumns, resultType);
    }


}
