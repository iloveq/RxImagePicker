package com.woaiqw.library.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.woaiqw.library.R;
import com.woaiqw.library.adapter.PreviewPagerAdapter;
import com.woaiqw.library.base.ThemeActivity;
import com.woaiqw.library.model.Counter;
import com.woaiqw.library.model.Image;

import java.util.List;

import static com.woaiqw.library.util.Constants.THEME_RES_ID;

/**
 * Created by haoran on 2018/10/23.
 */
public class PreviewChooseActivity extends ThemeActivity implements View.OnClickListener {

    private ViewPager vp;
    private TextView bottom_left, bottom_right;
    private CheckView checkView;
    private int currentPos;

    public static void startPreviewChooseActivityForResult(Activity source, int themeId) {
        Intent intent = new Intent(source, PreviewChooseActivity.class);
        intent.putExtra(THEME_RES_ID, themeId);
        source.startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_choose);
        checkView = findViewById(R.id.cv);
        bottom_left = findViewById(R.id.footer_left);
        bottom_right = findViewById(R.id.footer_right);
        bottom_left.setText("返回");
        bottom_left.setOnClickListener(this);
        bottom_right.setText("使用");
        vp = findViewById(R.id.vp);
        vp.setOffscreenPageLimit(3);
        checkView.setBackGroundDefaultColor(getColorPrimary());
        checkView.setEnabled(true);
        final List<Image> list = Counter.getInstance().getCheckedList();
        PreviewPagerAdapter adapter = new PreviewPagerAdapter(this, list);
        vp.setAdapter(adapter);
        checkView.setChecked(true);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                checkView.setChecked(list.get(position).checked);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        checkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = list.get(currentPos).checked;
                if (flag) {
                    checkView.setChecked(false);
                    Counter.getInstance().resetCheckedStatus(list.get(currentPos));
                } else {
                    checkView.setChecked(true);
                }

            }
        });

    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
