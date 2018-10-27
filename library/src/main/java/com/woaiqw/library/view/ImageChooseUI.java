package com.woaiqw.library.view;

import android.app.Activity;
import android.support.annotation.StyleRes;

import com.woaiqw.library.annotation.ResultType;

import java.lang.ref.WeakReference;

/**
 * Created by haoran on 2018/10/12.
 */
public class ImageChooseUI {

    private WeakReference<? extends Activity> source;

    @StyleRes
    private
    int theme;

    private int gridColumns;

    @ResultType
    private
    int resultType;

    private int pickedNum;

    public ImageChooseUI(WeakReference<? extends Activity> source, @StyleRes int theme, int gridColumns, int resultType,int pickedNum) {
        this.source = source;
        this.theme = theme;
        this.gridColumns = gridColumns;
        this.resultType = resultType;
        this.pickedNum = pickedNum;
    }

    public void createUI() {
        if (source == null || theme == 0) {
            throw new RuntimeException(" source or theme must be createFactory ");
        }
        ImageChooseGridActivity.startImageChooseGridActivityForResult(source, theme, gridColumns, resultType,pickedNum);
    }


}
