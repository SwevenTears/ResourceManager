package com.ccyy.resourcemanager.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.ccyy.resourcemanager.R;

/**
 * Created by Sweven on 2018/6/25.
 * Email:sweventears@Foxmail.com
 */
public class InputNameDialog extends Dialog {


    /**
     * 上下文对象 *
     */
    Activity activity;
    private Button btn_save;
    private Button btn_cancel;
    public EditText text_name;
    private View.OnClickListener mClickListener;

    private String default_name;

    public InputNameDialog(Activity activity, View.OnClickListener clickListener, String default_name) {
        super(activity, R.style.AppTheme);
        this.activity = activity;
        this.mClickListener = clickListener;
        this.default_name = default_name;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 指定布局
        this.setContentView(R.layout.dialog_input_new_file_name);

        text_name = findViewById(R.id.input_new_name);

        text_name.setText(default_name);

        if(default_name.equals("新建文件夹")){
            text_name.setHint("文件夹名称：如，folder");
            text_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus){
                        text_name.setSelection(0,default_name.length());
                    }
                }
            });
        }
        else{
            text_name.setHint("文件名称，如：file.type");
            text_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus){
                        int point=default_name.lastIndexOf(".");
                        text_name.setSelection(0,point);
                    }
                }
            });
        }

        /*
         * 获取Activity的窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),
         * 表示获得这个Activity的Window对象,这样这可以以同样的方式改变这个Activity的属性.
         */
        Window dialogWindow = this.getWindow();

        WindowManager m = activity.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.width = (int) (d.getWidth() * 0.6);
        p.height = 240;
        dialogWindow.setAttributes(p);

        // 根据id在布局中找到控件对象
        btn_save = findViewById(R.id.btn_input_name_sure);
        btn_cancel = findViewById(R.id.btn_input_new_name_cancel);

        // 为按钮绑定点击事件监听器
        btn_save.setOnClickListener(mClickListener);
        btn_cancel.setOnClickListener(mClickListener);

        this.setCancelable(true);

    }
}

