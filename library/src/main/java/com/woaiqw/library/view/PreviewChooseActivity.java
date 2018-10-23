package com.woaiqw.library.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;

import com.woaiqw.library.R;
import com.woaiqw.library.adapter.PreviewPagerAdapter;
import com.woaiqw.library.base.ThemeActivity;
import com.woaiqw.library.model.Counter;
import com.woaiqw.library.model.Image;

import java.util.ArrayList;

import static com.woaiqw.library.util.Constants.COLOR_PRIMARY;
import static com.woaiqw.library.util.Constants.REQUEST_CODER;

/**
 * Created by haoran on 2018/10/23.
 */
public class PreviewChooseActivity extends ThemeActivity {

    private ViewPager vp;

    public static void startPreviewChooseActivityForResult(Activity source, int colorPrimary) {
        Intent intent = new Intent(source, PreviewChooseActivity.class);
        intent.putExtra(COLOR_PRIMARY, colorPrimary);
        source.startActivityForResult(intent, REQUEST_CODER);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_choose);
        vp = findViewById(R.id.vp);
        ArrayList<Image> list = Counter.getInstance().getCheckedList();
        vp.setAdapter(new PreviewPagerAdapter(this, list));

    }
}
