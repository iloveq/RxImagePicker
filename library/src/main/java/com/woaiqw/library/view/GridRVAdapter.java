package com.woaiqw.library.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.woaiqw.library.ImageLoaderInterface;
import com.woaiqw.library.R;
import com.woaiqw.library.model.Image;
import com.woaiqw.library.util.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haoran on 2018/10/17.
 */
public class GridRVAdapter extends RecyclerView.Adapter<GridRVAdapter.GridViewHolder> {

    private List<Image> list;
    private Context context;
    private ImageLoaderInterface<ImageView> loader;
    private int margin;
    private int L;

    GridRVAdapter(Context context, ImageLoaderInterface<ImageView> loader, int i) {
        this.list = new ArrayList<>();
        this.context = context;
        this.loader = loader;
        margin = UIUtils.dp2px(context, 3);
        L = (UIUtils.getScreenWidth(context) / i) - margin;
    }


    public void setData(List<Image> images) {
        this.list.clear();
        if (images == null) {
            return;
        }
        this.list.addAll(images);
        notifyDataSetChanged();
    }

    @Override
    public GridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GridViewHolder(View.inflate(context, R.layout.item_grid, null));
    }

    @Override
    public void onBindViewHolder(GridViewHolder holder, int position) {
        loader.displayImage(context, list.get(position).path, holder.iv);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class GridViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv;

        GridViewHolder(View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.iv);
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.width = L;
            params.height = L;
            int i = margin / 2;
            params.setMargins(i, i, i, i);
            iv.setLayoutParams(params);

        }
    }

}
