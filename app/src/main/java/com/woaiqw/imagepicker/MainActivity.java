package com.woaiqw.imagepicker;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.woaiqw.base.common.PermissionActivity;
import com.woaiqw.base.utils.GlideApp;
import com.woaiqw.base.utils.PermissionListener;
import com.woaiqw.library.ImageLoaderInterface;
import com.woaiqw.library.RxImagePicker;
import com.woaiqw.library.annotation.ResultType;
import com.woaiqw.library.listener.ImagePickerResultListener;

import java.util.List;

public class MainActivity extends PermissionActivity implements View.OnClickListener {

    private String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.tv).setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        requestPermissions(permissions, new PermissionListener() {
            @Override
            public void onGranted() {
                RxImagePicker
                        .source(MainActivity.this, new ImageLoaderInterface<ImageView>() {
                            @Override
                            public void displayImage(Context context, String path, ImageView imageView) {
                                GlideApp.with(context).load(path).diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(imageView);
                            }
                        })
                        .createFactory()
                        .setGridColumn(3)
                        .setTheme(R.style.AppTheme)
                        .setPickedNum(5)
                        .setResultType(ResultType.URI)
                        .onPicked(new ImagePickerResultListener() {
                            @Override
                            public void onPicked(List o) {

                            }

                            @Override
                            public void onPhotoTook(Object o) {

                            }
                        })
                        .build();
            }

            @Override
            public void onDenied(List<String> list) {

            }
        });

    }
}