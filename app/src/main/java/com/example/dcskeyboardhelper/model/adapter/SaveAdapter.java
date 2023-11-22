package com.example.dcskeyboardhelper.model.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dcskeyboardhelper.R;
import com.example.dcskeyboardhelper.model.bean.Profile;
import com.example.dcskeyboardhelper.ui.dialog.ProfileDialog;
import com.example.dcskeyboardhelper.util.DateUtil;
import com.example.dcskeyboardhelper.viewModel.LoadViewModel;

import java.util.List;

public class SaveAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Profile> profiles;
    private Context context;
    private LoadViewModel viewModel;

    public SaveAdapter(Context context, LoadViewModel loadViewModel) {
        this.context = context;
        this.viewModel = loadViewModel;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setProfiles(List<Profile> profiles) {
        this.profiles = profiles;
        notifyDataSetChanged();//从数据库获取所有配置后传入并刷新rv
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0){//未创建
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile_not_created,
                    parent, false);
            return new InsertVH(view);
        }else {//已创建
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile_created,
                    parent, false);
            return new ProfileVH(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (holder.getItemViewType() == 0){
            ((InsertVH) holder).ib_new_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProfileDialog profileDialog = new ProfileDialog(context, viewModel);
                    profileDialog.show();
                }
            });
        }else {
            ((ProfileVH) holder).tv_profile_title.setText(profiles.get(position).getTitle());
            ((ProfileVH) holder).tv_profile_desc.setText(profiles.get(position).getDesc());
            ((ProfileVH) holder).tv_profile_date.setText(DateUtil.longToDate(profiles.get(position).getCreatedDate()));
            ((ProfileVH) holder).tv_profile_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteItem(position);
                }
            });
        }
    }

    //删除配置
    public void deleteItem(int position){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.warning))
                .setMessage(context.getString(R.string.confirm_operate));
        alertBuilder.setPositiveButton(context.getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                viewModel.delete(profiles.get(position).getId());
            }
        });
        alertBuilder.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertBuilder.show();
    }

    @Override
    public int getItemViewType(int position) {
        if (profiles != null && position < profiles.size()){
            return 1;
        }else {
            return 0;
        }
    }

    @Override
    public int getItemCount() {
        //如果没有任何保存的配置或数量为空或数量不足5，那么就设置数量为5，否则显示所有配置以及额外的两个空位
        return (profiles == null || profiles.isEmpty()) || profiles.size() <= 5? 5 : profiles.size() + 2;
    }

    static class InsertVH extends RecyclerView.ViewHolder{
        private final ImageButton ib_new_profile;
        public InsertVH(@NonNull View itemView) {
            super(itemView);
            ib_new_profile = itemView.findViewById(R.id.ib_new_profile);
        }
    }

    static class ProfileVH extends RecyclerView.ViewHolder{
        private final TextView tv_profile_title, tv_profile_desc, tv_profile_date;
        private final ImageButton tv_profile_delete;
        public ProfileVH(@NonNull View itemView) {
            super(itemView);
            tv_profile_title = itemView.findViewById(R.id.tv_profile_title);
            tv_profile_desc = itemView.findViewById(R.id.tv_profile_desc);
            tv_profile_date = itemView.findViewById(R.id.tv_profile_date);
            tv_profile_delete = itemView.findViewById(R.id.tv_profile_delete);
        }
    }
}
