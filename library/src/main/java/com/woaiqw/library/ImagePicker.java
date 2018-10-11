package com.woaiqw.library;

import com.woaiqw.library.helper.RequestBuilder;
import com.woaiqw.library.helper.ResultBuilder;
import com.woaiqw.library.listener.ImagePickerResultListener;

/**
 * Created by haoran on 2018/10/11.
 */
public class ImagePicker {

    private ResultBuilder result;
    private RequestBuilder request;


    private ImagePicker() {
        request = new RequestBuilder();
        result = new ResultBuilder();
    }

    private static class Holder {
        private static final ImagePicker IN = new ImagePicker();
    }


    public static ImagePicker create() {
        return Holder.IN;
    }


    public void onResult(ImagePickerResultListener listener) {

        result.setListener(listener);

    }

}
