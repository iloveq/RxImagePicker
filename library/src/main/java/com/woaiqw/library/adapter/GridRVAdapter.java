package com.woaiqw.library.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.woaiqw.library.ImageLoaderInterface;
import com.woaiqw.library.R;
import com.woaiqw.library.listener.OnItemClickListener;
import com.woaiqw.library.model.Counter;
import com.woaiqw.library.model.Image;
import com.woaiqw.library.util.Utils;
import com.woaiqw.library.view.CheckView;

import java.util.List;

/**
 * Created by haoran on 2018/10/17.
 */
public class GridRVAdapter extends RecyclerView.Adapter<GridRVAdapter.GridViewHolder> {

    private static final int VIEW_TYPE_FIRST = -1;

    private Context context;
    private ImageLoaderInterface<ImageView> loader;
    private OnItemClickListener listener;
    private Counter counter;
    private int margin;
    private int L;
    private int pickedNum;
    private int checkViewBgColor;

    public GridRVAdapter(Context context, ImageLoaderInterface<ImageView> loader, int i, int num, int colorPrimary) {
        this.context = context;
        this.loader = loader;
        this.pickedNum = num;
        this.checkViewBgColor = colorPrimary;
        counter = Counter.getInstance();
        margin = Utils.dp2px(context, 3);
        L = (Utils.getScreenWidth(context) / i) - margin;
    }

    public void refresh() {
        notifyDataSetChanged();
    }


    public void setData(List<Image> images) {
        if (images == null) {
            return;
        }
        counter.setList(images);
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
    public void onBindViewHolder(final GridViewHolder holder, int position) {
        final int currentPos = holder.getAdapterPosition();
        final List<Image> list = counter.getList();
        if (position != 0) {
            holder.iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean flag = list.get(currentPos - 1).checked;
                    if (counter.getCheckedList().size() >= pickedNum & !flag) {
                        listener.onClickItemOutOfRange(pickedNum);
                    } else {
                        holder.cv.setChecked(!flag);
                        list.get(currentPos - 1).checked = !flag;
                        listener.onClickItem(v, currentPos - 1);
                    }

                }
            });
            loader.displayImage(context, list.get(position - 1).path, holder.iv);
            holder.cv.setChecked(list.get(currentPos - 1).checked);
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
        return counter.getList().size() + 1;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    class GridViewHolder extends RecyclerView.ViewHolder {
        private FrameLayout ff_container;
        private LinearLayout ll_container;
        private ImageView iv;
        private TextView tv;
        private CheckView cv;

        GridViewHolder(View itemView, int viewType) {
            super(itemView);
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.width = L;
            params.height = L;
            int i = margin / 2;
            params.setMargins(i, i, i, i);
            if (viewType != VIEW_TYPE_FIRST) {
                ff_container = itemView.findViewById(R.id.ff_container);
                iv = itemView.findViewById(R.id.iv);
                cv = itemView.findViewById(R.id.check_view);
                ff_container.setLayoutParams(params);
                cv.setEnabled(true);
                cv.setBackGroundDefaultColor(checkViewBgColor);
            } else {
                ll_container = itemView.findViewById(R.id.ll_container);
                tv = itemView.findViewById(R.id.camera);
                ll_container.setLayoutParams(params);
            }

        }
    }


}
