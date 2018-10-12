package com.woaiqw.library.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.woaiqw.library.R;

import java.lang.ref.WeakReference;

/**
 * Created by haoran on 2018/10/12.
 */
public class ImageChooseActivity extends AppCompatActivity {

    private WeakReference<? extends Activity> source;

    public static void startImageChooseActivity(ImageChooseUI ui) {
    /*    decorate(ui);
        ui.body.startActivity(new Intent(ui.body, ImageChooseActivity.class));*/
    }

    private  void decorate(ImageChooseUI ui) {
        this.source = ui.source;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_image);
    }
}
