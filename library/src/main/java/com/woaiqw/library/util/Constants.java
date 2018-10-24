package com.woaiqw.library.util;

/**
 * Created by haoran on 2018/10/12.
 */
public class Constants {

    /**
     * bundle tag
     */
    public static final String THEME_RES_ID = "theme";
    public static final String GRID_COLUMN = "columns";
    public static final String RESULT_TYPE = "resultType";
    public static final String COLOR_PRIMARY = "colorPrimary";
    public static final String PREVIEW_IMAGE_LIST = "list";
    public static final String RESULT_NUM = "num";

    /**
     * request code
     */
    // 从你的 Activity 跳到 ImageChooseGridActivity
    public static final int REQUEST_SOURCE_CREATE_IMAGE_PICKER = 20;
    // 点击 ImageChooseGridActivity 的 预览按钮
    public static final int REQUEST_IMAGE_CHOOSE_ACTIVITY_PREVIEW_BUTTON = 21;
    // 点击 ImageChooseGridActivity 的 拍照按钮
    public static final int REQUEST_IMAGE_CHOOSE_ACTIVITY_TAKE_PHOTO_BUTTON = 22;

    /**
     * result code
     */
    public static final int RESULT_CODE = 30;
    public static final int RESULT_PREVIEW_CHOOSE_ACTIVITY_USE_BUTTON = 31;
    public static final int RESULT_PREVIEW_CHOOSE_ACTIVITY_BACK_BUTTON = 32;


}
