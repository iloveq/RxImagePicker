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

    private static final String THEME_RES_ID = "themeResId";
    private static final String GRID_COLUMN = "gridColumns";

    public static void startImageChooseActivity(WeakReference<? extends Activity> source, int themeResId,int gridColumns) {
        Intent intent = new Intent(source.get(), ImageChooseActivity.class);
        intent.putExtra(THEME_RES_ID, themeResId);
        intent.putExtra(GRID_COLUMN,gridColumns);
        source.get().startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(getIntent().getIntExtra(THEME_RES_ID, R.style.Default));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_image);
    }
}
