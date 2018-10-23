package com.woaiqw.library.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.woaiqw.library.R;
import com.woaiqw.library.adapter.GridRVAdapter;
import com.woaiqw.library.base.ToolbarListActivity;
import com.woaiqw.library.controller.ImageController;
import com.woaiqw.library.factory.ImagePickerFactory;
import com.woaiqw.library.listener.OnItemClickListener;
import com.woaiqw.library.model.Counter;
import com.woaiqw.library.model.Image;

import java.lang.ref.WeakReference;
import java.util.List;

import static com.woaiqw.library.util.Constants.GRID_COLUMN;
import static com.woaiqw.library.util.Constants.REQUEST_SOURCE_CREATE_IMAGE_PICKER;
import static com.woaiqw.library.util.Constants.RESULT_NUM;
import static com.woaiqw.library.util.Constants.RESULT_PREVIEW_CHOOSE_ACTIVITY_BACK_BUTTON;
import static com.woaiqw.library.util.Constants.RESULT_PREVIEW_CHOOSE_ACTIVITY_USE_BUTTON;
import static com.woaiqw.library.util.Constants.RESULT_TYPE;
import static com.woaiqw.library.util.Constants.THEME_RES_ID;

/**
 * Created by haoran on 2018/10/12.
 */
public class ImageChooseGridActivity extends ToolbarListActivity implements OnItemClickListener, View.OnClickListener {


    private RecyclerView rv;
    private GridRVAdapter adapter;
    private TextView bottom_left, bottom_right;
    private int column;
    private int resultType;


    public static void startImageChooseGridActivityForResult(WeakReference<? extends Activity> source, int themeResId, int gridColumns, int resultType, int pickedNum) {
        Intent intent = new Intent(source.get(), ImageChooseGridActivity.class);
        intent.putExtra(THEME_RES_ID, themeResId);
        intent.putExtra(GRID_COLUMN, gridColumns);
        intent.putExtra(RESULT_TYPE, resultType);
        intent.putExtra(RESULT_NUM, pickedNum);
        source.get().startActivityForResult(intent, REQUEST_SOURCE_CREATE_IMAGE_PICKER);
    }

    @Override
    public void onBodyCreated(View body, @Nullable Bundle savedInstanceState) {
        rv = body.findViewById(R.id.rv);
        bottom_left = body.findViewById(R.id.footer_left);
        bottom_right = body.findViewById(R.id.footer_right);
        bottom_left.setTextColor(Color.WHITE);
        bottom_left.setText("预览");
        bottom_left.setOnClickListener(this);
        bottom_right.setOnClickListener(this);
        bottom_right.setTextColor(getColorPrimary());
        bottom_right.setText("使用");
        column = getIntent().getIntExtra(GRID_COLUMN, 3);
        rv.setLayoutManager(new GridLayoutManager(this, column));
        adapter = new GridRVAdapter(this, ImagePickerFactory.getImageLoader(), column, getIntent().getIntExtra(RESULT_NUM, 1), getColorPrimary());
        rv.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        resultType = getIntent().getIntExtra(RESULT_TYPE, 2);
        ImageController.get().getSource(this, new ImageController.ImageControllerListener() {
            @Override
            public void onSuccess(List<Image> images) {
                if (images == null || images.size() == 0) {
                    Toast.makeText(ImageChooseGridActivity.this, "相册还没有照片哦！", Toast.LENGTH_SHORT).show();
                    return;
                }
                adapter.setData(images);
            }

            @Override
            public void onError(String msg) {
                Toast.makeText(ImageChooseGridActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImageController.get().release();
        Counter.getInstance().reset();
    }


    @Override
    public void onClickItem(View view, int position) {
        bottom_right.setText("使用(" + Counter.getInstance().getCount() + ")");
    }

    @Override
    public void onClickTakePhoto(View v) {
        Toast.makeText(this, "take photo", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClickItemOutOfRange(int range) {
        Toast.makeText(this, "最多选择：" + range + "张", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.footer_left) {
            // 预览
            PreviewChooseActivity.startPreviewChooseActivityForResult(this, getThemeResId());
        } else if (id == R.id.footer_right) {
            result(resultType);
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        bottom_right.setText("使用(" + Counter.getInstance().getCount() + ")");
        if (adapter != null)
            adapter.refresh();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_PREVIEW_CHOOSE_ACTIVITY_USE_BUTTON:
                result(resultType);
                finish();
                break;

            case RESULT_PREVIEW_CHOOSE_ACTIVITY_BACK_BUTTON:
                break;


        }
    }


    private void result(int resultType) {
        ImageController.get().convertResult(resultType);
    }

}
