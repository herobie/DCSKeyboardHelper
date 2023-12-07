package com.example.dcskeyboardhelper.model.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dcskeyboardhelper.R;
import com.example.dcskeyboardhelper.base.SuperBaseAdapter;
import com.example.dcskeyboardhelper.databinding.ItemPageBinding;
import com.example.dcskeyboardhelper.model.bean.OperatePage;

import java.util.List;

//页面排序及更新adapter
public class SortPagesAdapter extends SuperBaseAdapter<ItemPageBinding> {
    private List<OperatePage> pages;

    public SortPagesAdapter(List<OperatePage> pages) {
        this.pages = pages;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        binding.btnPageItem.setText(pages.get(position).getPageName());
    }

    @Override
    protected int getItemLayoutRes() {
        return R.layout.item_page;
    }

    @Override
    public int getItemCount() {
        return pages == null? 0 : pages.size();
    }
}
