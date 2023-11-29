package com.example.dcskeyboardhelper.model.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dcskeyboardhelper.R;
import com.example.dcskeyboardhelper.base.SuperBaseAdapter;
import com.example.dcskeyboardhelper.databinding.ItemKeyBinding;
import com.example.dcskeyboardhelper.model.bean.Key;
import com.example.dcskeyboardhelper.model.socket.KeyCodes;

import java.util.List;

//选择要模拟的按键的Adapter
public class KeysAdapter extends SuperBaseAdapter<ItemKeyBinding> {
    private final List<Key> keys;

    public KeysAdapter(KeySelectedListener keySelectedListener){
        this.keys = KeyCodes.getKeys();
        this.keySelectedListener = keySelectedListener;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        binding.tvKeyName.setText(keys.get(position).getName());
        binding.cvKeyBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keySelectedListener.onKeySelected(keys.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    protected int getItemLayoutRes() {
        return R.layout.item_key;
    }

    @Override
    public int getItemCount() {
        return keys == null? 0 : keys.size();
    }

    private KeySelectedListener keySelectedListener;

    public interface KeySelectedListener{
        void onKeySelected(Key key);
    }

    public void setKeySelectedListener(KeySelectedListener keySelectedListener) {
        this.keySelectedListener = keySelectedListener;
    }
}
