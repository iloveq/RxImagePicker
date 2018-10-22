package com.woaiqw.library.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.woaiqw.library.ImageLoaderInterface;
import com.woaiqw.library.R;
import com.woaiqw.library.listener.OnItemClickListener;
import com.woaiqw.library.model.Image;
import com.woaiqw.library.util.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haoran on 2018/10/17.
 */
public class GridRVAdapter extends RecyclerView.Adapter<GridRVAdapter.GridViewHolder> {

    private static final int VIEW_TYPE_FIRST = -1;
    private List<Image> list;
    private Context context;
    private ImageLoaderInterface<ImageView> loader;
    private OnItemClickListener listener;
    private int margin;
    private int L;

    public GridRVAdapter(Context context, ImageLoaderInterface<ImageView> loader, int i) {
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
    public int getItemViewType(int position) {
        return position == 0 ? VIEW_TYPE_FIRST : position;
    }

    @Override
    public GridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GridViewHolder(viewType != VIEW_TYPE_FIRST ? View.inflate(context, R.layout.item_grid, null) : View.inflate(context, R.layout.item_grid_first, null), viewType);
    }

    @Override
    public void onBindViewHolder(GridViewHolder holder, int position) {
        final int currentPos = holder.getAdapterPosition();
        if (position != 0) {
            holder.iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClickItem(v, currentPos - 1);
                }
            });
            loader.displayImage(context, list.get(position - 1).path, holder.iv);
        } else {
            holder.tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClickTakePhoto(v);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size() + 1;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    class GridViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv;
        private TextView tv;

        GridViewHolder(View itemView, int viewType) {
            super(itemView);
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.width = L;
            params.height = L;
            int i = margin / 2;
            params.setMargins(i, i, i, i);
            if (viewType != VIEW_TYPE_FIRST) {
                iv = itemView.findViewById(R.id.iv);
                iv.setLayoutParams(params);
            } else {
                tv = itemView.findViewById(R.id.camera);
                tv.setLayoutParams(params);
            }


        }
    }

}
