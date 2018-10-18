package com.woaiqw.library.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

/**
 * Created by haoran on 2018/10/18.
 */
public class UIUtils {
    public UIUtils() {
    }


    private static Resources getResources(Context context) {
        return context.getResources();
    }


    public static int dp2px(Context context, float dp) {
        return (int) (getResources(context).getDisplayMetrics().density * dp + 0.5F);
    }

    public static int px2dp(Context context, float px) {
        return (int) (px / getResources(context).getDisplayMetrics().density + 0.5F);
    }

    public static int sp2px(Context context, float sp) {
        return (int) TypedValue.applyDimension(2, sp, getResources(context).getDisplayMetrics());
    }

    public static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        assert windowManager != null;
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    public static int getScreenWidth(Context context) {
        return getDisplayMetrics(context).widthPixels;
    }

    public static int getScreenHegith(Context context) {
        return getDisplayMetrics(context).heightPixels;
    }
}
