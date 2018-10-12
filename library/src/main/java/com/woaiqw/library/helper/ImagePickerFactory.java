package com.woaiqw.library.helper;

import android.app.Activity;

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

    @ResultType
    int resultType;

    ImagePickerResultListener listener;


    public ImagePickerFactory(Builder builder) {
        this.picker = builder.picker;
        this.ui = builder.ui;
        this.resultType = builder.resultType;
        this.listener = builder.listener;
    }


    public static class Builder {

        private static final int COLUMNS_DEFAULT = 3;
        private final ImagePicker picker;
        private int gridColumns;
        private ImageChooseUI ui;
        private ImagePickerResultListener listener;
        private @ResultType
        int resultType;

        private ImageChooseUI initImageChooseUI(WeakReference<? extends Activity> source, int gridColumns) {
            return new ImageChooseUI(source, gridColumns);
        }

        public Builder(ImagePicker picker) {
            this.picker = picker;
            this.gridColumns = COLUMNS_DEFAULT;
        }

        public Builder setGridColums(int gridColumns) {
            this.resultType = gridColumns;
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
            this.ui = initImageChooseUI(picker.getSource(), gridColumns);
            ui.createUI();
            return new ImagePickerFactory(this);
        }

    }


}
