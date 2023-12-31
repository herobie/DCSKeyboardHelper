package com.example.dcskeyboardhelper.model.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dcskeyboardhelper.R;
import com.example.dcskeyboardhelper.model.Constant;
import com.example.dcskeyboardhelper.model.bean.Profile;
import com.example.dcskeyboardhelper.ui.debug.ModuleDebugActivity;
import com.example.dcskeyboardhelper.ui.dialog.ProfileDialog;
import com.example.dcskeyboardhelper.ui.simulate.SimulateActivity;
import com.example.dcskeyboardhelper.util.DateUtil;
import com.example.dcskeyboardhelper.viewModel.LoadViewModel;

import java.util.List;

public class SaveAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Profile> profiles;
    private final Context context;
    private final LoadViewModel viewModel;
    private final String parentPage;//通过这个变量来获取是这是在调试模式还是开始模拟按钮中打开的

    public SaveAdapter(Context context, LoadViewModel loadViewModel, String parentPage) {
        this.context = context;
        this.viewModel = loadViewModel;
        this.parentPage = parentPage;
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
            ((InsertVH) holder).ib_new_profile.setOnClickListener(v -> {
                ProfileDialog profileDialog = new ProfileDialog(context, viewModel);
                profileDialog.setOnDismissListener(dialog -> {
                    //如果添加成功，那么则自动跳转至新添加的配置的操作页面
                    if (profileDialog.getNewProfileId() != -1){//返回-1代表添加失败或点击了取消添加按钮
                        long profileId = profileDialog.getNewProfileId();//获取配置id并传给下一个页面
                        if (Constant.SIMULATION_MODE.equals(parentPage)){
                            Intent intent = new Intent(context, SimulateActivity.class);
                            intent.putExtra("profiledId", profileId);
                            context.startActivity(intent);
                            Constant.CURRENT_PROFILE_ID = profileId;
                        }else if (Constant.DEBUG_MODE.equals(parentPage)){
                            Intent intent = new Intent(context, ModuleDebugActivity.class);
                            intent.putExtra("profileId", profileId);
                            context.startActivity(intent);
                            Constant.CURRENT_PROFILE_ID = profileId;
                        }
                    }
                });
                profileDialog.show();
            });
        }else {
            ((ProfileVH) holder).tv_profile_title.setText(profiles.get(position).getTitle());
            ((ProfileVH) holder).tv_profile_desc.setText(profiles.get(position).getDesc());
            ((ProfileVH) holder).tv_profile_date.setText(DateUtil.longToDate(profiles.get(position).getCreatedDate()));
            ((ProfileVH) holder).tv_profile_delete.setOnClickListener(v -> deleteItem(position));
            ((ProfileVH) holder).cv_profile_background.setOnClickListener(v -> {
                if (Constant.SIMULATION_MODE.equals(parentPage)){
                    long profileId = viewModel.getProfileById(profiles.get(position).getId());
                    Intent intent = new Intent(context, SimulateActivity.class);
                    intent.putExtra("profileId", profileId);
                    context.startActivity(intent);
                    Constant.CURRENT_PROFILE_ID = profileId;
                }else if (Constant.DEBUG_MODE.equals(parentPage)){
                    long profileId = viewModel.getProfileById(profiles.get(position).getId());
                    Intent intent = new Intent(context, ModuleDebugActivity.class);
                    intent.putExtra("profileId", profileId);
                    context.startActivity(intent);
                    Constant.CURRENT_PROFILE_ID = profileId;
                }
            });
        }
    }

    //删除配置
    public void deleteItem(int position){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.warning))
                .setMessage(context.getString(R.string.confirm_operate));
        alertBuilder.setPositiveButton(context.getString(R.string.confirm), (dialog, which) ->
                viewModel.delete(profiles.get(position).getId()));
        alertBuilder.setNegativeButton(context.getString(R.string.cancel), (dialog, which) -> dialog.dismiss());
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
        private final CardView cv_profile_background;
        public ProfileVH(@NonNull View itemView) {
            super(itemView);
            cv_profile_background = itemView.findViewById(R.id.cv_profile_background);
            tv_profile_title = itemView.findViewById(R.id.tv_profile_title);
            tv_profile_desc = itemView.findViewById(R.id.tv_profile_desc);
            tv_profile_date = itemView.findViewById(R.id.tv_profile_date);
            tv_profile_delete = itemView.findViewById(R.id.tv_profile_delete);
        }
    }
}
