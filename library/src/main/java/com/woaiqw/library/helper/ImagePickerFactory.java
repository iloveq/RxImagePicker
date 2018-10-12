package com.woaiqw.library.helper;

import com.woaiqw.library.ImagePicker;
import com.woaiqw.library.annotation.ResultType;
import com.woaiqw.library.listener.ImagePickerResultListener;
import com.woaiqw.library.view.ImageChooseActivity;
import com.woaiqw.library.view.ImageChooseUI;

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

        private final ImagePicker picker;
        private ImageChooseUI ui;
        private ImagePickerResultListener listener;
        private @ResultType
        int resultType;


        public Builder(ImagePicker picker) {
            this.picker = picker;
            this.ui = ImageChooseActivity.createImageChooseUI(picker.getSource());
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
            return new ImagePickerFactory(this);
        }
    }


}
