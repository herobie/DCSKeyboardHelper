package com.example.dcskeyboardhelper.ui.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dcskeyboardhelper.R;
import com.example.dcskeyboardhelper.base.BaseDialog;
import com.example.dcskeyboardhelper.databinding.DialogSortPageBinding;
import com.example.dcskeyboardhelper.model.adapter.SortPagesAdapter;
import com.example.dcskeyboardhelper.model.bean.OperatePage;

import java.util.Collections;
import java.util.List;

//页面更新及排序dialog
public class SortPagesDialog extends BaseDialog<DialogSortPageBinding> {
    private final List<OperatePage> pages;
    private boolean isConfirm = false;//是否按下确认键
    public SortPagesDialog(@NonNull Context context, List<OperatePage> pages) {
        super(context);
        this.pages = pages;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.rvSortPages.setLayoutManager(layoutManager);
        SortPagesAdapter adapter = new SortPagesAdapter(pages);
        binding.rvSortPages.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int swipeFlags =ItemTouchHelper.ACTION_STATE_IDLE;
                int dragFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN |  ItemTouchHelper.UP;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int targetPosition = target.getAdapterPosition();
                Collections.swap(pages, fromPosition, targetPosition);
                adapter.notifyItemMoved(fromPosition, targetPosition);
                return true;
            }

            @SuppressLint("NewApi")
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }

            @Override
            public float getMoveThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
                return 0.8f;
            }
        });
        itemTouchHelper.attachToRecyclerView(binding.rvSortPages);

        binding.btnSortPagesConfirm.setOnClickListener(v -> {
            isConfirm = true;
            dismiss();
        });
    }

    public List<OperatePage> getPages() {
        return isConfirm? pages : null;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.dialog_sort_page;
    }
}
