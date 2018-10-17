package com.woaiqw.library.factory;

import android.app.Activity;
import android.support.annotation.StyleRes;
import android.widget.ImageView;

import com.woaiqw.library.ImageLoader;
import com.woaiqw.library.RxImagePicker;
import com.woaiqw.library.annotation.ResultType;
import com.woaiqw.library.listener.ImagePickerResultListener;
import com.woaiqw.library.view.ImageChooseUI;

import java.lang.ref.WeakReference;

/**
 * Created by haoran on 2018/10/12.
 */
public class ImagePickerFactory {

    RxImagePicker picker;

    ImageChooseUI ui;

    ImagePickerResultListener listener;

    ImageLoader<ImageView> loader;


    public ImagePickerFactory(Builder builder) {
        this.loader = builder.loader;
        this.picker = builder.picker;
        this.ui = builder.ui;
        this.listener = builder.listener;
    }

    public static ImageLoader<ImageView> getImageLoader() {
        return null;
    }


    public static class Builder {

        private static final int COLUMNS_DEFAULT = 3;
        private final RxImagePicker picker;
        private int gridColumn;
        private ImageLoader<ImageView> loader;
        private @StyleRes
        int theme;
        private @ResultType
        int resultType;
        private ImageChooseUI ui;
        private ImagePickerResultListener listener;

        private ImageChooseUI initImageChooseUI(WeakReference<? extends Activity> source, int theme, int gridColumn, @ResultType int resultType) {
            return new ImageChooseUI(source, theme, gridColumn, resultType);
        }

        public Builder(RxImagePicker picker) {
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

        public Builder setImageLoader(ImageLoader<ImageView> loader) {
            this.loader = loader;
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
            this.ui = initImageChooseUI(picker.getSource(), gridColumn, theme, resultType);
            ui.createUI();
            return new ImagePickerFactory(this);
        }

    }


}
