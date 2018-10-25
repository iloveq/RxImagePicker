package com.woaiqw.library.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.woaiqw.library.factory.ImagePickerFactory;
import com.woaiqw.library.model.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haoran on 2018/10/23.
 */
public class PreviewPagerAdapter extends PagerAdapter {

    private List<ImageView> views = new ArrayList<>();

    public PreviewPagerAdapter(Context context, List<Image> images) {

        for (Image image : images) {
            ImageView iv = new ImageView(context);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ImagePickerFactory.getImageLoader().displayImage(context, image.path, iv, 300, 300, 0.85f);
            views.add(iv);
        }

    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        container.addView(views.get(position));
        return views.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}

