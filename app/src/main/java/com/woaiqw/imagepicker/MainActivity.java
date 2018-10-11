package com.woaiqw.imagepicker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.woaiqw.library.ImagePicker;
import com.woaiqw.library.listener.ImagePickerResultListener;

public class MainActivity extends AppCompatActivity implements ImagePickerResultListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImagePicker.create().onResult(this);

    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onTakePhoto() {

    }
}