package com.woaiqw.library.view;

import android.app.Activity;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;

import java.lang.ref.WeakReference;

/**
 * Created by haoran on 2018/10/12.
 */
public class ImageChooseUI {

    WeakReference<? extends Activity> source;

    int themeResId;

    int gridColumns;

    public ImageChooseUI(WeakReference<? extends Activity> source, int gridColumns) {
        this.source = source;
        this.gridColumns = gridColumns;
        themeResId = getColorBackground(source.get());


    }

    private @ColorInt
    int getColorBackground(Activity activity) {
        int[] attrs = new int[]{android.R.attr.colorBackground};
        TypedArray typedArray = activity.obtainStyledAttributes(attrs);
        int color = typedArray.getColor(0, 0xfffafafa);
        typedArray.recycle();
        return color;
    }

    public void createUI() {
        if (source == null || themeResId == 0) {
            throw new RuntimeException(" source or themeResId must be createFactory ");
        }
        ImageChooseActivity.startImageChooseActivity(source, themeResId, gridColumns);
    }


}
