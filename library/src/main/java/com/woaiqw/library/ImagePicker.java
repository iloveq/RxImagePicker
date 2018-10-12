package com.woaiqw.library;

import android.app.Activity;

import com.woaiqw.library.annotation.ResultType;
import com.woaiqw.library.helper.ImagePickerFactory;
import com.woaiqw.library.listener.ImagePickerResultListener;

import java.lang.ref.WeakReference;

/**
 * Created by haoran on 2018/10/11.
 */
public class ImagePicker {

    private static volatile ImagePicker IN = null;
    private WeakReference<? extends Activity> source;
    private ImagePickerFactory.Builder builder;


    private ImagePicker(Activity activity) {
        source = new WeakReference<>(activity);
        builder = new ImagePickerFactory.Builder(this);
    }


    public static ImagePicker in(Activity activity) {
        if (null == IN) {
            synchronized (ImagePicker.class) {
                if (null == IN) {
                    IN = new ImagePicker(activity);
                }
            }
        }
        return IN;
    }


    public ImagePickerFactory.Builder createChooseUI() {
        return builder;
    }


    public ImagePickerFactory.Builder onResult(ImagePickerResultListener listener) {

        builder.onResult(listener);
        builder.build();
        return builder;

    }

    public ImagePickerFactory.Builder setResultType(@ResultType int type) {

        builder.setResultType(type);

        return builder;
    }

    public Activity getSource() {
        return source.get();
    }
}
