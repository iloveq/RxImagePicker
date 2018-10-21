package com.woaiqw.library.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import com.woaiqw.library.R;

import static com.woaiqw.library.util.Constants.THEME_RES_ID;

/**
 * Created by haoran on 2018/10/12.
 */
public abstract class ToolbarActivity extends BaseActivity {

    private Toolbar toolbar;
    private LinearLayout VIEW;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VIEW = new LinearLayout(this);
        VIEW.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        VIEW.setLayoutParams(params);
        toolbar = new Toolbar(this);
        int theme = getIntent().getIntExtra(THEME_RES_ID, R.style.Default);
        setTheme(theme);
        toolbar.setBackgroundColor(getColorPrimary());
        VIEW.addView(toolbar, 0);
        VIEW.addView(hookContentView(), 1);
        setContentView(VIEW);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
        }
        this.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * a containerView source contentView
     *
     * @return View
     */
    public abstract View hookContentView();

    public int getColorPrimary(){
        TypedValue typedValue = new  TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }


}
