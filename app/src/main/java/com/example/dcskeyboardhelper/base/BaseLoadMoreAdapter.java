package com.example.dcskeyboardhelper.base;

import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

@RequiresApi(api = Build.VERSION_CODES.M)
public abstract class BaseLoadMoreAdapter<T extends ViewDataBinding, VM extends ViewModel>
        extends BaseAdapter<T, VM> {
    protected final int TYPE_ITEM = 1001;
    protected final int TYPE_FOOTER = 1002;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM){
            binding = DataBindingUtil
                    .inflate(LayoutInflater.from(parent.getContext()) , getItemLayoutRes() , parent , false);
            return new BaseViewHolder(binding.getRoot());
        }else if (viewType == TYPE_FOOTER && getFooterRes() != 0){
            View view = LayoutInflater.from(parent.getContext()).inflate(getFooterRes(), parent, false);
            return new FootViewHolder(view);
        }
        return new BaseViewHolder(binding.getRoot());//这里可能有问题
    }

    @Override
    public int getItemViewType(int position) {
        //给最后一个item设置foot_view
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        Log.d("MainActivity", "onAttachedToRecyclerView: ");

        //gridLayout下给footer页面加权使其单独占用一列
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager){
            ((GridLayoutManager) manager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return position + 1 == getItemCount()? 2 : 1;
                }
            });
        }

        //滑动到底部时候的监听回调
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private boolean isSlidingUpward = false;
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    assert manager != null;
                    int lastItemPosition = manager.findLastCompletelyVisibleItemPosition();
                    int itemCount = manager.getItemCount();
                    if(lastItemPosition == (itemCount - 1) && isSlidingUpward){
                        loadMoreAction();//滑动到底部且继续在上滑操作时执行加载
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                isSlidingUpward = dy > 0;
            }
        });
    }

    abstract protected void loadMoreAction();

    abstract protected int getFooterRes();

    public static class FootViewHolder extends RecyclerView.ViewHolder{

        public FootViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
