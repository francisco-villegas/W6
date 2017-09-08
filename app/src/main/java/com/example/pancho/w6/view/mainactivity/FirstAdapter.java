package com.example.pancho.w6.view.mainactivity;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.beardedhen.androidbootstrap.BootstrapWell;
import com.example.pancho.w6.R;
import com.example.pancho.w6.model.Result;
import com.example.pancho.w6.util.CONSTANTS;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by FRANCISCO on 10/08/2017.
 */

public class FirstAdapter extends RecyclerView.Adapter<FirstAdapter.ViewHolder> {

    private static final String TAG = "ResultListAdapter";
    List<Result> ResultList;
    Context context;

    private boolean isLoadingAdded = false;
    private static final int ITEM = 0;
    private static final int LOADING = 1;

    public FirstAdapter(List<Result> ResultList) {
        this.ResultList = ResultList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Log.d(TAG, "onCreateViewHolder: ");
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ViewHolder(view);
    }

    private int lastPosition = -1;
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        if(position > lastPosition){
            //Animation animation = AnimationUtils
        }

        //Log.d(TAG, "onBindViewHolder: ");
        final Result results = ResultList.get(position);
        Picasso.with(context).load(CONSTANTS.PATH_MOVIES_IMG + results.getPosterPath()).into(holder.img);
        if(!results.getTitle().trim().equals(""))
            holder.tvTitle.setText(results.getTitle());
        else
            holder.tvNameParent.setVisibility(holder.tvNameParent.getRootView().GONE);

        holder.tvRegion.setText(String.valueOf(results.getPopularity()));

        if(!results.getReleaseDate().trim().equals(""))
            holder.tvType.setText(results.getReleaseDate());
        else
            holder.tvType.setVisibility(holder.tvType.getRootView().GONE);

        if(!results.getOverview().trim().equals(""))
            holder.tvOverview.setText(results.getOverview());
        else
            holder.tvOverview.setVisibility(holder.tvOverview.getRootView().GONE);

        holder.scroll.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                if(holder.scroll.getChildAt(0).getHeight() > holder.scroll_parent.getMeasuredHeight()) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
                return true;
            }
        });
        
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((EventListener) context).ItemClick(results);
            }
        });

    }

    @Override
    public int getItemCount() {
        //Log.d(TAG, "getResultCount: "+ResultList.size());
        return ResultList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == ResultList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public void add(Result r) {
        ResultList.add(r);
        notifyItemInserted(ResultList.size() - 1);
    }

    public void addAll(List<Result> moveResults) {
        for (Result result : moveResults) {
            add(result);
        }
    }

    public void remove(Result r) {
        int position = ResultList.indexOf(r);
        if (position > -1) {
            ResultList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Result());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = ResultList.size() - 1;
        Result result = getItem(position);

        if (result != null) {
            ResultList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Result getItem(int position) {
        return ResultList.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Nullable
        @BindView(R.id.img)
        BootstrapCircleThumbnail img;

        @Nullable
        @BindView(R.id.tvTitle)
        TextView tvTitle;

        @Nullable
        @BindView(R.id.tvRegion)
        TextView tvRegion;

        @Nullable
        @BindView(R.id.tvRelease)
        TextView tvType;

        @Nullable
        @BindView(R.id.tvOverview)
        TextView tvOverview;

        @Nullable
        @BindView(R.id.scroll)
        ScrollView scroll;

        @Nullable
        @BindView(R.id.scroll_parent)
        FrameLayout scroll_parent;

        @Nullable
        @BindView(R.id.tvNameParent)
        BootstrapWell tvNameParent;

        public ViewHolder(View ResultView) {
            super(ResultView);
            ButterKnife.bind(this, ResultView);
        }
    }

    public interface EventListener {
        void ItemClick(Result item);
    }
}
