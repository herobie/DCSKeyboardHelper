package com.example.dcskeyboardhelper.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dcskeyboardhelper.R;
import com.example.dcskeyboardhelper.model.Constant;
import com.example.dcskeyboardhelper.model.adapter.KeysAdapter;
import com.example.dcskeyboardhelper.model.bean.Key;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

//点击获取键盘输入及输入步骤名的Dialog
public class SetKeyboardActionDialog extends Dialog implements KeysAdapter.KeySelectedListener {
    private TextInputLayout ed_step_desc;
    private Button btn_set_action, btn_set_confirm;
    private Key key;
    private String desc;
    private boolean isConfirm = false;//用于标记是否是按确认键退出Dialog的
    private final String dialog_status;
    public SetKeyboardActionDialog(@NonNull Context context) {
        super(context, R.style.DialogBaseStyle);
        dialog_status = Constant.CREATE;
    }

    public SetKeyboardActionDialog(@NonNull Context context, Key key, String desc) {
        super(context);
        this.key = key;
        this.desc = desc;
        dialog_status = Constant.UPDATE;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_insert_action);

        ed_step_desc = findViewById(R.id.ed_step_desc);
        btn_set_action = findViewById(R.id.btn_set_action);
        btn_set_confirm = findViewById(R.id.btn_set_confirm);
        RecyclerView rv_keyboard = findViewById(R.id.rv_keyboard);

        if (key != null){//设置好了再点进来会显示之前设置好的
            Objects.requireNonNull(ed_step_desc.getEditText()).setText(desc);
            btn_set_action.setText(key.getName());
        }

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 4);
        rv_keyboard.setLayoutManager(layoutManager);
        KeysAdapter adapter = new KeysAdapter(this);
        rv_keyboard.setAdapter(adapter);

        btn_set_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isConfirm = true;
                dismiss();
            }
        });
    }

    public String getStepDesc(){
        return Objects.requireNonNull(ed_step_desc.getEditText()).getText().toString();
    }

    @Override
    public void onKeySelected(Key key) {
        this.key = key;
        btn_set_action.setText(key.getName());
    }

    public Key getKey() {
        return isConfirm? key : null;//只有按确认键才能获取key
    }

    public String getDialog_status() {
        return dialog_status;
    }
}
