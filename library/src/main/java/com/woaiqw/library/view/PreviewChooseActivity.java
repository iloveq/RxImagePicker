package com.woaiqw.library.view;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.woaiqw.library.R;
import com.woaiqw.library.adapter.PreviewPagerAdapter;
import com.woaiqw.library.base.ThemeActivity;
import com.woaiqw.library.listener.WrapOnPageChangeListener;
import com.woaiqw.library.model.Counter;
import com.woaiqw.library.model.Image;

import java.util.List;

import static com.woaiqw.library.util.Constants.REQUEST_IMAGE_CHOOSE_ACTIVITY_PREVIEW_BUTTON;
import static com.woaiqw.library.util.Constants.RESULT_PREVIEW_CHOOSE_ACTIVITY_BACK_BUTTON;
import static com.woaiqw.library.util.Constants.RESULT_PREVIEW_CHOOSE_ACTIVITY_USE_BUTTON;
import static com.woaiqw.library.util.Constants.THEME_RES_ID;

/**
 * Created by haoran on 2018/10/23.
 */
public class PreviewChooseActivity extends ThemeActivity implements View.OnClickListener {

    private TextView bottom_left, bottom_right;
    private CheckView checkView;
    private int currentPos;
    private List<Image> list;
    private ViewPager vp;

    public static void startPreviewChooseActivityForResult(Activity source, int themeId) {
        Intent intent = new Intent(source, PreviewChooseActivity.class);
        intent.putExtra(THEME_RES_ID, themeId);
        source.startActivityForResult(intent, REQUEST_IMAGE_CHOOSE_ACTIVITY_PREVIEW_BUTTON);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_choose);

        setBottomBar();
        initData();
        setCheckView();
        initVpAndSetData();
        initListener();

    }

    private void setBottomBar() {

        bottom_left = findViewById(R.id.footer_left);
        bottom_right = findViewById(R.id.footer_right);

        int[][] states = new int[][]{
                new int[]{-android.R.attr.state_pressed}, // unpressed
                new int[]{android.R.attr.state_pressed}  // pressed
        };
        int[] colorsLeft = new int[]{
                Color.WHITE,
                getColorPrimary()
        };
        int[] colorsRight = new int[]{
                getColorPrimary(),
                Color.WHITE
        };
        ColorStateList colorStateListLeft = new ColorStateList(states, colorsLeft);
        ColorStateList colorStateListRight = new ColorStateList(states, colorsRight);

        bottom_left.setTextColor(colorStateListLeft);
        bottom_right.setTextColor(colorStateListRight);

        bottom_left.setText(R.string.back);
        String rightText = getString(R.string.use_start) +
                Counter.getInstance().getCheckedList().size() +
                getString(R.string.use_end);
        bottom_right.setText(rightText);

    }

    private void initData() {
        list = Counter.getInstance().getCheckedList();
    }

    private void setCheckView() {
        checkView = findViewById(R.id.cv);
        checkView.setBackGroundDefaultColor(getColorPrimary());
        checkView.setEnabled(true);
        checkView.setChecked(true);
    }

    private void initVpAndSetData() {
        vp = findViewById(R.id.vp);
        vp.setOffscreenPageLimit(3);
        vp.setAdapter(new PreviewPagerAdapter(this, list));
    }

    private void initListener() {
        bottom_left.setOnClickListener(this);
        bottom_right.setOnClickListener(this);
        checkView.setOnClickListener(this);
        ViewPager.OnPageChangeListener listener = new WrapOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentPos = position;
                if (list.size() > 0)
                    checkView.setChecked(list.get(position).checked);
            }
        };

        vp.addOnPageChangeListener(listener);
        listener.onPageSelected(vp.getCurrentItem());
    }


    @Override
    public void onClick(View v) {

        int id = v.getId();

        if (id == R.id.footer_left) {
            // 点击返回
            setResult(RESULT_PREVIEW_CHOOSE_ACTIVITY_BACK_BUTTON, null);
            finish();

        } else if (id == R.id.footer_right) {
            // 点击使用
            setResult(RESULT_PREVIEW_CHOOSE_ACTIVITY_USE_BUTTON, null);
            finish();

        } else if (id == R.id.cv) {
            // 点击 check view
            boolean flag = list.get(currentPos).checked;
            checkView.setChecked(!flag);
            Counter.getInstance().resetCheckedStatus(list.get(currentPos), !flag);
            list.get(currentPos).checked = !flag;
            String text = getString(R.string.use_start) +
                    Counter.getInstance().getCheckedList().size() +
                    getString(R.string.use_end);
            bottom_right.setText(text);

        }

    }
}
