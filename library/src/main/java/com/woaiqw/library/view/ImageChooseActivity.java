package com.woaiqw.library.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.woaiqw.library.adapter.GridRVAdapter;
import com.woaiqw.library.base.ToolbarActivity;
import com.woaiqw.library.controller.ImageController;
import com.woaiqw.library.factory.ImagePickerFactory;
import com.woaiqw.library.model.Image;

import java.lang.ref.WeakReference;
import java.util.List;

import static com.woaiqw.library.util.Constants.GRID_COLUMN;
import static com.woaiqw.library.util.Constants.REQUEST_CODER;
import static com.woaiqw.library.util.Constants.RESULT_TYPE;
import static com.woaiqw.library.util.Constants.THEME_RES_ID;

/**
 * Created by haoran on 2018/10/12.
 */
public class ImageChooseActivity extends ToolbarActivity {


    private RecyclerView rv;
    private GridRVAdapter adapter;
    private int i;


    public static void startImageChooseActivityForResult(WeakReference<? extends Activity> source, int themeResId, int gridColumns, int resultType) {
        Intent intent = new Intent(source.get(), ImageChooseActivity.class);
        intent.putExtra(THEME_RES_ID, themeResId);
        intent.putExtra(GRID_COLUMN, gridColumns);
        intent.putExtra(RESULT_TYPE, resultType);
        source.get().startActivityForResult(intent, REQUEST_CODER);
    }

    @Override
    public View hookContentView() {
        rv = new RecyclerView(this);
        rv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        i = getIntent().getIntExtra(GRID_COLUMN, 3);
        rv.setLayoutManager(new GridLayoutManager(this, i));
        return rv;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new GridRVAdapter(this, ImagePickerFactory.getImageLoader(), i);
        rv.setAdapter(adapter);
        ImageController.get().getSource(this, new ImageController.ImageControllerListener() {
            @Override
            public void onSuccess(List<Image> images) {
                if (images == null || images.size() == 0) {
                    Toast.makeText(ImageChooseActivity.this, "相册还没有照片哦！", Toast.LENGTH_SHORT).show();
                    return;
                }
                adapter.setData(images);
            }

            @Override
            public void onError(String msg) {
                Toast.makeText(ImageChooseActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImageController.get().release();
    }
}
