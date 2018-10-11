package com.woaiqw.library.helper;

import com.woaiqw.library.listener.ImagePickerResultListener;
import com.woaiqw.library.model.Result;

/**
 * Created by haoran on 2018/10/11.
 */
public class ResultBuilder {

    public void setListener(ImagePickerResultListener listener) {

    }

    public Result build() {
        return new Result(this);
    }
}
