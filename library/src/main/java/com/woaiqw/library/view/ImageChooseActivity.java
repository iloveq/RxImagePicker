package com.woaiqw.library.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.woaiqw.library.base.ToolbarActivity;
import com.woaiqw.library.controller.ImageController;
import com.woaiqw.library.factory.ImagePickerFactory;
import com.woaiqw.library.model.Image;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static com.woaiqw.library.util.Constants.GRID_COLUMN;
import static com.woaiqw.library.util.Constants.REQUEST_CODER;
import static com.woaiqw.library.util.Constants.RESULT_TYPE;
import static com.woaiqw.library.util.Constants.THEME_RES_ID;

/**
 * Created by haoran on 2018/10/12.
 */
public class ImageChooseActivity extends ToolbarActivity {


    private GridView grid;
    private List<Image> list = new ArrayList<>();
    private GridAdapter gridAdapter;


    public static void startImageChooseActivityForResult(WeakReference<? extends Activity> source, int themeResId, int gridColumns, int resultType) {
        Intent intent = new Intent(source.get(), ImageChooseActivity.class);
        intent.putExtra(THEME_RES_ID, themeResId);
        intent.putExtra(GRID_COLUMN, gridColumns);
        intent.putExtra(RESULT_TYPE, resultType);
        source.get().startActivityForResult(intent, REQUEST_CODER);
    }

    @Override
    public View hookContentView() {
        grid = new GridView(this);
        grid.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        grid.setNumColumns(getIntent().getIntExtra(GRID_COLUMN, 3));
        return grid;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gridAdapter = new GridAdapter(this, list, ImagePickerFactory.getImageLoader());
        grid.setAdapter(gridAdapter);
        ImageController.get().getSource(this, new ImageController.ImageControllerListener() {
            @Override
            public void onSuccess(List<Image> images) {
                gridAdapter.setData(list);
            }

            @Override
            public void onError(String msg) {

            }
        });


    }
}
