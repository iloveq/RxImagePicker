package com.woaiqw.library;

import android.app.Activity;

import com.woaiqw.library.helper.ImagePickerFactory;

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


    public ImagePickerFactory.Builder createFactory() {
        return builder = new ImagePickerFactory.Builder(this);
    }

    public WeakReference<? extends Activity> getSource() {
        return source;
    }

}
