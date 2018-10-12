package com.woaiqw.library.helper;

import android.app.Activity;
import android.support.annotation.StyleRes;

import com.woaiqw.library.ImagePicker;
import com.woaiqw.library.annotation.ResultType;
import com.woaiqw.library.listener.ImagePickerResultListener;
import com.woaiqw.library.view.ImageChooseUI;

import java.lang.ref.WeakReference;

/**
 * Created by haoran on 2018/10/12.
 */
public class ImagePickerFactory {

    ImagePicker picker;

    ImageChooseUI ui;

    ImagePickerResultListener listener;


    public ImagePickerFactory(Builder builder) {
        this.picker = builder.picker;
        this.ui = builder.ui;
        this.listener = builder.listener;
    }


    public static class Builder {

        private static final int COLUMNS_DEFAULT = 3;
        private final ImagePicker picker;
        private int gridColumn;
        private @StyleRes
        int theme;
        private @ResultType
        int resultType;
        private ImageChooseUI ui;
        private ImagePickerResultListener listener;

        private ImageChooseUI initImageChooseUI(WeakReference<? extends Activity> source, int theme, int gridColumn, @ResultType int resultType) {
            return new ImageChooseUI(source,theme, gridColumn, resultType);
        }

        public Builder(ImagePicker picker) {
            this.picker = picker;
            this.gridColumn = COLUMNS_DEFAULT;
        }

        public Builder setTheme(@StyleRes int theme) {
            this.theme = theme;
            return this;
        }

        public Builder setGridColumn(int gridColumn) {
            this.resultType = gridColumn;
            return this;
        }

        public Builder setResultType(@ResultType int type) {
            this.resultType = type;
            return this;
        }

        public Builder onResult(ImagePickerResultListener listener) {
            this.listener = listener;
            return this;
        }


        public ImagePickerFactory build() {
            this.ui = initImageChooseUI(picker.getSource(), gridColumn,theme, resultType);
            ui.createUI();
            return new ImagePickerFactory(this);
        }

    }


}
