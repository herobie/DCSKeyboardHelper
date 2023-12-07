package com.example.dcskeyboardhelper.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dcskeyboardhelper.R;
import com.example.dcskeyboardhelper.model.Constant;
import com.example.dcskeyboardhelper.model.adapter.KeysAdapter;
import com.example.dcskeyboardhelper.model.bean.Action;
import com.example.dcskeyboardhelper.model.bean.Key;
import com.example.dcskeyboardhelper.model.socket.KeyCodes;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//点击获取键盘输入及输入步骤名的Dialog
public class SetKeyboardActionDialog extends Dialog implements KeysAdapter.KeySelectedListener {
    private TextInputLayout ed_step_desc;
    private Button btn_step_backspace, btn_set_confirm;
    private GridLayout grid_actions_container;
    private Action action;
    private boolean isConfirm = false;//用于标记是否是按确认键退出Dialog的
    private final String dialog_status;
    private List<Integer> codes;
    public SetKeyboardActionDialog(@NonNull Context context) {
        super(context, R.style.DialogBaseStyle);
        dialog_status = Constant.CREATE;
        codes = new ArrayList<>();
    }

    public SetKeyboardActionDialog(@NonNull Context context, Action action) {
        super(context);
        this.action = action;
        this.codes = action.getCodes();
        dialog_status = Constant.UPDATE;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_insert_action);

        ed_step_desc = findViewById(R.id.ed_step_desc);
        btn_step_backspace = findViewById(R.id.btn_step_backspace);
        btn_set_confirm = findViewById(R.id.btn_set_confirm);
        RecyclerView rv_keyboard = findViewById(R.id.rv_keyboard);
        grid_actions_container = findViewById(R.id.grid_actions_container);

        if (action != null){//如果是更新模式下，点进来会显示已经设置好的步骤名及模拟操作
            Objects.requireNonNull(ed_step_desc.getEditText()).setText(action.getName());
            if (codes != null){
                for (int code : codes){
                    //显示该步骤所要执行的模拟按键
                    grid_actions_container.addView(createKeyActionIndicator(KeyCodes.getKeyNameByValue(code)));
                }
            }
        }

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 4);
        rv_keyboard.setLayoutManager(layoutManager);
        KeysAdapter adapter = new KeysAdapter(this);
        rv_keyboard.setAdapter(adapter);

        btn_set_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //每个步骤至少选择一个模拟按键
                if (grid_actions_container.getChildCount() == 0){
                    Toast.makeText(getContext(), getContext().getString(R.string.actions_non_null), Toast.LENGTH_SHORT).show();
                    return;
                }
                isConfirm = true;
                dismiss();
            }
        });

        btn_step_backspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //退格，移除队列最后一个模拟按键，没有按键时会抛出异常，这里try-catch直接捕捉处理
                    grid_actions_container.removeViewAt(grid_actions_container.getChildCount() - 1);
                }catch (NullPointerException ignored){
                }
            }
        });
    }

    @Override
    public void onKeySelected(Key key) {
        int code = key.getCode();
        String name = key.getName();
        codes.add(code);
        grid_actions_container.addView(createKeyActionIndicator(name));
    }

    @SuppressLint("ResourceAsColor")
    private Button createKeyActionIndicator(String name){
        Button button = new Button(getContext());
        button.setBackgroundColor(android.R.color.transparent);
        button.setTextColor(R.color.black);
        button.setText(name);
        return button;
    }

    public Action getActions() {
        if (isConfirm){
            String name = Objects.requireNonNull(ed_step_desc.getEditText()).getText().toString();
            if (action == null){//如果action没有创建（一般是在创建模式下）则创建一个
                action = new Action(name, codes);
                return action;
            }
            action.setCodes(codes);
            action.setName(name);
            return action;
        }
        return null;//只有按确认键才能获取key
    }

    public String getDialog_status() {
        return dialog_status;
    }
}
