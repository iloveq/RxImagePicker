package com.woaiqw.library;

import android.app.Activity;

import com.woaiqw.library.helper.ImagePickerFactory;

import java.lang.ref.WeakReference;

/**
 * Created by haoran on 2018/10/11.
 */
public class RxImagePicker {

    private static volatile RxImagePicker IN = null;
    private WeakReference<? extends Activity> source;


    private RxImagePicker(Activity activity) {
        source = new WeakReference<>(activity);

    }


    public static RxImagePicker in(Activity activity) {
        if (null == IN) {
            synchronized (RxImagePicker.class) {
                if (null == IN) {
                    IN = new RxImagePicker(activity);
                }
            }
        }
        return IN;
    }


    public ImagePickerFactory.Builder createFactory() {
        return new ImagePickerFactory.Builder(this);
    }

    public WeakReference<? extends Activity> getSource() {
        if (source == null) {
            throw new RuntimeException(" must init RxImagePicker ");
        }
        return source;
    }

}
