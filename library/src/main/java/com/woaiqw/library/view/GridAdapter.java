package com.woaiqw.library.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.woaiqw.library.ImageLoader;
import com.woaiqw.library.R;
import com.woaiqw.library.model.Image;

import java.util.List;

/**
 * Created by haoran on 2018/10/17.
 */
public class GridAdapter extends BaseAdapter {

    private List<Image> list;
    private Context context;
    private ImageLoader<ImageView> loader;

    public GridAdapter(Context context, List<Image> list, ImageLoader<ImageView> loader) {
        this.list = list;
        this.context = context;
        this.loader = loader;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list == null ? 0 : list.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GridViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_grid, parent);
            holder = new GridViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (GridViewHolder) convertView.getTag();
        }

        loader.displayImage(context, list.get(position).path, holder.iv);

        return convertView;
    }

    public void setData(List<Image> list) {
        this.list.clear();
        if (list == null) {
            return;
        }
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    class GridViewHolder {
        private ImageView iv;

        GridViewHolder(View view) {
            iv = view.findViewById(R.id.iv);
        }
    }

}
