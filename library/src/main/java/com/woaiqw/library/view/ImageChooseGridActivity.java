package com.woaiqw.library.view;

import android.app.Activity;
import android.content.Intent;
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
import static com.woaiqw.library.util.Constants.REQUEST_CODER;
import static com.woaiqw.library.util.Constants.RESULT_NUM;
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


    public static void startImageChooseGridActivityForResult(WeakReference<? extends Activity> source, int themeResId, int gridColumns, int resultType, int pickedNum) {
        Intent intent = new Intent(source.get(), ImageChooseGridActivity.class);
        intent.putExtra(THEME_RES_ID, themeResId);
        intent.putExtra(GRID_COLUMN, gridColumns);
        intent.putExtra(RESULT_TYPE, resultType);
        intent.putExtra(RESULT_NUM, pickedNum);
        source.get().startActivityForResult(intent, REQUEST_CODER);
    }

    @Override
    public void onBodyCreated(View body, @Nullable Bundle savedInstanceState) {
        rv = body.findViewById(R.id.rv);
        bottom_left = body.findViewById(R.id.footer_left);
        bottom_right = body.findViewById(R.id.footer_right);
        bottom_left.setText("预览");
        bottom_left.setOnClickListener(this);
        bottom_right.setText("使用");
        column = getIntent().getIntExtra(GRID_COLUMN, 3);
        rv.setLayoutManager(new GridLayoutManager(this, column));
        adapter = new GridRVAdapter(this, ImagePickerFactory.getImageLoader(), column, getIntent().getIntExtra(RESULT_NUM, 1), getColorPrimary());
        rv.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
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
        Toast.makeText(this, "item：" + position, Toast.LENGTH_SHORT).show();
        bottom_right.setText("使用(" + Counter.getInstance().getCount() + ")");
    }

    @Override
    public void onClickTakePhoto(View v) {
        Toast.makeText(this, "take photo", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClickItemOutOfRange(int range) {
        Toast.makeText(this, "超出了可选范围：" + range, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        // 预览
        PreviewChooseActivity.startPreviewChooseActivityForResult(this, getThemeResId());
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (adapter!=null)
            adapter.refresh();
    }
}
