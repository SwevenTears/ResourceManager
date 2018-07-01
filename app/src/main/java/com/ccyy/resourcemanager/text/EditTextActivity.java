package com.ccyy.resourcemanager.text;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;

import com.ccyy.resourcemanager.R;
import com.ccyy.resourcemanager.dialog.ChooseFolderDialog;
import com.ccyy.resourcemanager.dialog.InputNameDialog;
import com.ccyy.resourcemanager.main.FileTools;
import com.ccyy.resourcemanager.tools.T;

import java.io.File;
import java.io.FileInputStream;
import java.util.Objects;

public class EditTextActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private EditText editText;
    private PopupMenu popupMenu;
    private Menu menu;

    private final int MENU_GROUP_EDIT = 0;
    private final int MENU_GROUP_OPEN = 1;

    private final int MENU_ITEM_PASTE = 1;
    private final int MENU_ITEM_COPY = 2;
    private final int MENU_ITEM_EDIT = 3;
    private final int MENU_ITEM_BREAK = 4;

    private MenuInflater menuInflater;

    private String OLD_FILE_DATA;
    private String TXT_path;
    private String TXT_name;
    private InputNameDialog inputName;

    private String new_name;
    private ChooseFolderDialog folderChooser;

    @SuppressLint("StaticFieldLeak")
    private static T t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_text);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        popupMenu = new PopupMenu(this, fab);
        menu = popupMenu.getMenu();

        editText = findViewById(R.id.edit_text_scroll);

        setEditable_OF_EditText(false);

        Intent intent = getIntent();
        TXT_name = intent.getStringExtra("File_Name");
        TXT_path = intent.getStringExtra("File_Path");
        Objects.requireNonNull(getSupportActionBar()).setTitle(TXT_name);
        showTextData(TXT_path);

        menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_edit_text, menu);

        popupMenu.setOnMenuItemClickListener(this);

        t = new T(getBaseContext());

    }

    /**
     * 将TXT文件中的内容提取到TextView控件里
     *
     * @param path 文件地址
     */
    private void showTextData(String path) {
        String res = "";
        try {
            FileInputStream fin = new FileInputStream(path);
            int length = fin.available();
            byte[] buffer = new byte[length];
            fin.read(buffer);
            res = new String(buffer);
            fin.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        editText.setText(res);
        OLD_FILE_DATA = editText.getText().toString();
        editText.setFocusable(false);
        editText.setFocusableInTouchMode(false);
    }

    /**
     * 按钮监听，打开菜单栏
     *
     * @param v
     */
    public void openEditMenu(View v) {
        popupMenu.show();
        menuManager(editText.isFocusable());
    }

    /**
     * @param isEditable 是否进入编辑状态，还是查看状态
     */
    private void menuManager(boolean isEditable) {
        menu.removeGroup(R.id.must_menu);
        menu.removeGroup(MENU_GROUP_OPEN);
        menu.removeGroup(MENU_GROUP_EDIT);
        if (isEditable) {
            if (hasClipboard()) {
                menu.add(MENU_GROUP_EDIT, MENU_ITEM_PASTE, 0, "粘贴");
            } else {
                menu.removeItem(MENU_ITEM_PASTE);
            }
            if (isSelectOfEditText()) {
                menu.add(MENU_GROUP_EDIT, MENU_ITEM_COPY, 0, "复制");
                menu.removeItem(MENU_ITEM_PASTE);
            } else {
                menu.removeItem(MENU_ITEM_COPY);
            }

            menuInflater.inflate(R.menu.menu_edit_text, menu);
        } else {
            menu.removeGroup(MENU_GROUP_OPEN);
            menu.add(MENU_GROUP_OPEN, MENU_ITEM_EDIT, 0, "编辑");
            menu.add(MENU_GROUP_OPEN, MENU_ITEM_BREAK, 0, "返回");
        }
    }

    /**
     * @param item 菜单项
     * @return 菜单事件地设置
     */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_ITEM_PASTE: {
                addWord(getClipboard());
                break;
            }
            case MENU_ITEM_COPY: {
                addClipboard(getSelectedText());
                break;
            }
            case R.id.selectAll: {
                editText.selectAll();
                break;
            }
            case R.id.save_file: {
                saveFile();
                break;
            }
            case R.id.save_as: {
                saveAs();
                break;
            }
            case R.id.exit: {

                previous_step(
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                saveFile();
                            }
                        },
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                editText.setText(OLD_FILE_DATA);
                                setEditable_OF_EditText(false);
                                getSupportActionBar().setTitle(TXT_name);
                            }
                        });
                break;
            }
            case MENU_ITEM_EDIT: {
                setEditable_OF_EditText(true);
                getSupportActionBar().setTitle(TXT_name + "  正在编辑...");
                break;
            }
            case MENU_ITEM_BREAK: {
                finish();
                break;
            }
            default:
                break;
        }
        return false;
    }

    /**
     * 设置EditText是否可以编辑
     *
     * @param b 状态
     */
    private void setEditable_OF_EditText(boolean b) {
        editText.setFocusable(b);
        editText.setFocusableInTouchMode(b);
        editText.setEnabled(b);
        if (b)
            editText.requestFocus();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            previous_step(
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            saveFile();
                            finish();
                        }
                    },
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            t.tips("文件未保存");
                            finish();
                        }
                    });
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 返回上一步的保存提示窗口
     *
     * @param positiveListener 确定的监听器
     * @param NegativeListener 取消的监听器
     */
    private void previous_step(DialogInterface.OnClickListener positiveListener
            , DialogInterface.OnClickListener NegativeListener) {
        if (editText.getText().toString().equals(OLD_FILE_DATA)) {
            setEditable_OF_EditText(false);
            getSupportActionBar().setTitle(TXT_name);
        } else {
            dialogTip(EditTextActivity.this, positiveListener, NegativeListener);
        }
    }

    /**
     * 修改文件后保存源文件
     */
    private void saveFile() {
        setEditable_OF_EditText(false);
        getSupportActionBar().setTitle(TXT_name);
        if (OLD_FILE_DATA.equals(editText.getText().toString())) {
            t.tips("文本未修改");
        } else {
            String newText = editText.getText().toString();
            boolean state = FileTools.saveTXTFile(newText, TXT_path);
            if (state) {
                OLD_FILE_DATA = newText;
                t.tips("保存成功");
            } else {
                t.tips("保存失败");
            }
        }
    }

    /**
     * 另存文件
     */
    public void saveAs() {
        String str = TXT_name.substring(0, TXT_name.length() - 4);
        new_name = str + " 副本.txt";
        inputName = new InputNameDialog(this, saveAs_inputNameListener, new_name);
        inputName.show();
    }

    /**
     * 文件夹选择的监听
     */
    private View.OnClickListener folderChoose_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.choose_folder) {
                String parent_path = new File(TXT_path).getParent();
                String choose_path = folderChooser.present_path;
                if (parent_path.equals(choose_path)) {
                    t.tips("目录未更改");
                } else {
                    String new_text = editText.getText().toString();
                    if (FileTools.saveTXTFile(new_text, choose_path + "/" + new_name)) {
                        setEditable_OF_EditText(false);
                        TXT_name = new_name;
                        getSupportActionBar().setTitle(new_name);
                        t.tips("保存成功");
                        folderChooser.dismiss();
                    } else {
                        t.tips("保存失败");
                    }
                }
            }
        }
    };

    /**
     * 打开文件夹选择器
     */
    public void folderChooser() {
        folderChooser = new ChooseFolderDialog(EditTextActivity.this, folderChoose_listener);
        folderChooser.show();
    }

    /**
     * 另存为时输入名称的监听
     */
    private View.OnClickListener saveAs_inputNameListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.btn_input_name_sure:

                    new_name = inputName.text_name.getText().toString().trim();
                    String newText = editText.getText().toString();
                    if (FileTools.isLegal_FileName(new_name)) {
                        folderChooser();
                        inputName.dismiss();
                    } else {
                        t.tips("文件名不合法：文件名中不能包括以下字符：\n\\/:*?\"<>|,");
                    }
                    break;
                case R.id.btn_input_new_name_cancel: {
                    inputName.dismiss();
                    break;
                }
            }
        }

    };

    /**
     * @param context          context
     * @param positiveListener 确定的监听器
     * @param NegativeListener 取消的监听器
     */
    private void dialogTip(Context context
            , DialogInterface.OnClickListener positiveListener
            , DialogInterface.OnClickListener NegativeListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.drawable.ic_launcher_background);
        builder.setTitle("提示");
        builder.setMessage("文件未保存，是否保存？");
        builder.setPositiveButton("保存", positiveListener);
        builder.setNegativeButton("不保存", NegativeListener);
        builder.show();
    }

    /**
     * @return 获取剪切板上的内容
     */
    private String getClipboard() {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData data = clipboardManager.getPrimaryClip();
        ClipData.Item item;
        try {
            item = data.getItemAt(0);
        } catch (Exception e) {
            return "";
        }
        return item.getText().toString();
    }

    /**
     * @param text 需要添加到剪切板上的内容
     */
    private void addClipboard(String text) {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        assert clipboardManager != null;
        clipboardManager.setText(text);
    }

    /**
     * @return 剪切板是否有内容
     */
    private boolean hasClipboard() {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData data = null;
        try {
            data = clipboardManager.getPrimaryClip();
            return data.getItemCount() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @return 判断是否选中文本
     */
    private boolean isSelectOfEditText() {
        int sta = editText.getSelectionStart();
        int end = editText.getSelectionEnd();
        return end - sta > 0;
    }

    /**
     * @return 获取选中的文本
     */
    private String getSelectedText() {
        String all = editText.getText().toString();
        int sta = editText.getSelectionStart();
        int end = editText.getSelectionEnd();
        return all.substring(sta, end);
    }

    /**
     * @param text 需要粘贴的内容
     */
    private void addWord(String text) {
        int index = editText.getSelectionStart();//获取光标所在位置
        Editable edit = editText.getEditableText();//获取EditText的文字
        if (index < 0 || index >= edit.length()) {
            edit.append(text);
        } else {
            edit.insert(index, text);//光标所在位置插入文字
        }
    }
}
