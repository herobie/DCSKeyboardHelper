package com.example.dcskeyboardhelper.model.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class FragmentsAdapter<T extends Fragment> extends FragmentStateAdapter {
    private List<T> fragments;

    public FragmentsAdapter(@NonNull Fragment fragment, List<T> fragments) {
        super(fragment);
        this.fragments = fragments;
    }

    public FragmentsAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, List<T> fragments) {
        super(fragmentManager, lifecycle);
        this.fragments = fragments;
    }

    public void setFragments(List<T> fragments) {
        this.fragments = fragments;
    }

    public List<T> getFragments() {
        return fragments;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments == null? 0:fragments.size();
    }

}
