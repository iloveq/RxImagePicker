package com.woaiqw.imagepicker;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.woaiqw.base.common.PermissionActivity;
import com.woaiqw.base.utils.ImageLoader;
import com.woaiqw.base.utils.PermissionListener;
import com.woaiqw.library.ImageLoaderInterface;
import com.woaiqw.library.RxImagePicker;
import com.woaiqw.library.annotation.ResultType;
import com.woaiqw.library.listener.ImagePickerResultListener;

import java.util.List;

public class MainActivity extends PermissionActivity implements View.OnClickListener {

    private String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private ImageView iv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv = findViewById(R.id.iv_main);
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
                            public void displayImage(Context context, String path, ImageView imageView, int width, int height, float scale) {
                                ImageLoader.loadImageWithSize(context, imageView, path, width, height, scale);
                            }
                        })
                        .createFactory()
                        .setGridColumn(3)
                        .setTheme(R.style.AppTheme)
                        .setPickedNum(9)
                        .setResultType(ResultType.PATH)
                        .onPicked(new ImagePickerResultListener() {
                            @Override
                            public void onPicked(Object o) {
                                List<String> list = (List<String>) o;
                                String path = list.get(0);
                                ImageLoader.loadImageWithPath(MainActivity.this, iv, path);
                            }

                            @Override
                            public void onPhotoTook(Object o) {
                                Log.d("111", o.toString());
                                String path = (String) o;
                                ImageLoader.loadImageWithPath(MainActivity.this, iv, path);
                            }

                            @Override
                            public void onException(String msg) {
                                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
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