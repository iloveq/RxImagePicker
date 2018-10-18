package com.woaiqw.library;

import android.app.Activity;
import android.widget.ImageView;

import com.woaiqw.library.factory.ImagePickerFactory;

import java.lang.ref.WeakReference;

/**
 * Created by haoran on 2018/10/11.
 */
public class RxImagePicker {

    private static volatile RxImagePicker IN = null;
    private WeakReference<? extends Activity> source;
    private ImageLoaderInterface<ImageView> loader;


    private RxImagePicker(Activity activity, ImageLoaderInterface<ImageView> loaderInterface) {
        source = new WeakReference<>(activity);
        loader = loaderInterface;
    }


    public static RxImagePicker in(Activity activity, ImageLoaderInterface<ImageView> loaderInterface) {
        if (null == IN) {
            synchronized (RxImagePicker.class) {
                if (null == IN) {
                    IN = new RxImagePicker(activity, loaderInterface);
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

    public ImageLoaderInterface<ImageView> getLoader() {
        if (loader == null){
            throw new RuntimeException(" must init ImageLoader ");
        }
        return loader;
    }

}
