package com.ccyy.resourcemanager.text;

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
import com.ccyy.resourcemanager.tools.T;

import java.io.FileInputStream;
import java.io.OutputStream;
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
    private String path;
    private String name;

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

        Intent intent = getIntent();
        name = intent.getStringExtra("File_Name");
        path = intent.getStringExtra("File_Path");
        Objects.requireNonNull(getSupportActionBar()).setTitle(name);
        showTextData(path);

        menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_edit_text, menu);

        popupMenu.setOnMenuItemClickListener(this);

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
                T.error(EditTextActivity.this, "该功能暂未完善");
                break;
            }
            case R.id.save_as: {
                T.error(EditTextActivity.this, "该功能暂未完善");
                break;
            }
            case R.id.exit: {

                closeFile(
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //TODO 更新文件 未实现保存功能
                                OLD_FILE_DATA=editText.getText().toString();
                                editText.setFocusable(false);
                                editText.setFocusableInTouchMode(false);
                                getSupportActionBar().setTitle(name);
                                T.error(EditTextActivity.this,"暂时无法保存");
                            }
                        },
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                editText.setText(OLD_FILE_DATA);
                                editText.setFocusable(false);
                                editText.setFocusableInTouchMode(false);
                                getSupportActionBar().setTitle(name);
                            }
                        });
                break;
            }
            case MENU_ITEM_EDIT: {
                editText.setFocusableInTouchMode(true);
                editText.setFocusable(true);
                editText.requestFocus();
                getSupportActionBar().setTitle(name+"  正在编辑...");
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            closeFile(
                    new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialog, int which) {
                              // todo 更新文件
                              finish();
                          }
                      },
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
        }
        return super.onKeyDown(keyCode, event);
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
        ClipData data = clipboardManager.getPrimaryClip();
        ClipData.Item item;
        try {
            item = data.getItemAt(0);
        } catch (Exception e) {
            return false;
        }
        return true;
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

    /**
     * @param positiveListener 确定的监听器
     * @param NegativeListener 取消的监听器
     */
    private void closeFile(DialogInterface.OnClickListener positiveListener
            , DialogInterface.OnClickListener NegativeListener) {
        if (editText.getText().toString().equals(OLD_FILE_DATA)) {
            editText.setFocusable(false);
            editText.setFocusableInTouchMode(false);
            getSupportActionBar().setTitle(name);
        } else {
            dialogTip(EditTextActivity.this,positiveListener,NegativeListener);
        }
    }

    /**
     * @param context context
     * @param positiveListener 确定的监听器
     * @param NegativeListener 取消的监听器
     */
    private void dialogTip(Context context
            ,DialogInterface.OnClickListener positiveListener
            , DialogInterface.OnClickListener NegativeListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.drawable.ic_launcher_background);
        builder.setTitle("提示");
        builder.setMessage("文件未保存，是否保存？");
        builder.setPositiveButton("保存", positiveListener);
        builder.setNegativeButton("不保存", NegativeListener);
        builder.show();
    }

    private void write(String str) {
        try {
            OutputStream a = openFileOutput("file.txt", EditTextActivity.MODE_PRIVATE);

            a.write(str.getBytes());
            a.close();
        }
        catch (Exception e) {

        }
    }
}
