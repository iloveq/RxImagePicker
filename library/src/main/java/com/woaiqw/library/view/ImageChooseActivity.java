package com.woaiqw.library.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.woaiqw.library.R;

/**
 * Created by haoran on 2018/10/12.
 */
public class ImageChooseActivity extends AppCompatActivity {

    public static ImageChooseUI createImageChooseUI(Activity source) {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_image);
    }
}
