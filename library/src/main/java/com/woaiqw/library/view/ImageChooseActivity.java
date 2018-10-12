package com.woaiqw.library.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridView;

import com.woaiqw.library.R;

import java.lang.ref.WeakReference;

/**
 * Created by haoran on 2018/10/12.
 */
public class ImageChooseActivity extends AppCompatActivity {

    private static final String THEME_RES_ID = "theme";
    private static final String GRID_COLUMN = "gridColumns";
    private static final String RESULT_TYPE = "resultType";

    private Toolbar toolbar;
    private GridView grid;

    public static void startImageChooseActivity(WeakReference<? extends Activity> source, int themeResId, int gridColumns, int resultType) {
        Intent intent = new Intent(source.get(), ImageChooseActivity.class);
        intent.putExtra(THEME_RES_ID, themeResId);
        intent.putExtra(GRID_COLUMN, gridColumns);
        intent.putExtra(RESULT_TYPE, resultType);
        source.get().startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(getIntent().getIntExtra(THEME_RES_ID, R.style.Default));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_image);
        toolbar = findViewById(R.id.toolbar);
        grid = findViewById(R.id.gridView);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
