package com.zhong.randompasswordgenerator;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class PasswordList extends AppCompatActivity
{
    private PasswordListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_list);

        //设置页面标题
        this.setTitle(getString(R.string.password_list));

        //添加返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setHomeButtonEnabled(true);//设置左上角的图标是否可以点击
            actionBar.setDisplayHomeAsUpEnabled(true);//给左上角图标的左边加上一个返回的图标
        }

        //设置适配器
        adapter = new PasswordListAdapter(this, R.layout.password_list_item, GlobalData.getInstance().GetPasswordList());
        ListView listView = (ListView) findViewById(R.id.passwordListView);
        listView.setAdapter(adapter);

//        //设置列表响应点击事件
//        listView.setOnItemClickListener((parent, view, position, id) -> {
//            PasswordListItem passwordItem = GlobalData.getInstance().GetPasswordList().get(position);
//            //复制密码到剪贴版
//            CopyStringToClipBoard(passwordItem.GetPassword());
//        });

        //设置列表响应长按事件
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            ShowPopupMenu(view, position);
            return false;
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        int id = item.getItemId();
        if (id == android.R.id.home)
        {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    private void CopyStringToClipBoard(String text)
    {
        ClipboardManager myClipboard;
        myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        ClipData myClip;
        myClip = ClipData.newPlainText("text", text);
        myClipboard.setPrimaryClip(myClip);
    }

    private void ShowPopupMenu(View view, int position)
    {
        //定义PopupMenu对象
        PopupMenu popupMenu = new PopupMenu(this, view);
        //设置PopupMenu对象的布局
        popupMenu.getMenuInflater().inflate(R.menu.password_list_menu, popupMenu.getMenu());
        //设置PopupMenu的点击事件
        popupMenu.setOnMenuItemClickListener(item -> {
            PasswordListItem passwordItem = GlobalData.getInstance().GetPasswordList().get(position);
            //点击了删除密码
            if (item.getItemId() == R.id.deletePassword)
            {
                GlobalData.getInstance().GetPasswordList().remove(position);
                adapter.notifyDataSetChanged();
            }
            //点击了复制到剪贴板
            else if (item.getItemId() == R.id.copyPassword)
            {
                CopyStringToClipBoard(passwordItem.GetPassword());
                Toast.makeText(this, getString(R.string.password_copied_info), Toast.LENGTH_SHORT).show();
            }
            //点击了编辑名称
            else if (item.getItemId() == R.id.editPasswordName)
            {
                View dialog_view = getLayoutInflater().inflate(R.layout.input_dialog_view, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.input_password_name_tip))
                        .setView(dialog_view);
                //获取对话框中的文本输入框
                EditText editText = dialog_view.findViewById(R.id.input_dialog_text_edit);
                //向文本输入框中填入原来的名称
                editText.setText(passwordItem.GetName());
                builder.setPositiveButton(getString(R.string.ok), (dialog, which) -> {
                        String passwordName = editText.getText().toString();
                        if (passwordName.isEmpty())
                        {
                            Toast.makeText(this, getString(R.string.input_password_name_warning), Toast.LENGTH_SHORT).show();
                        }
                        else if (!passwordName.equals(passwordItem.GetName()))
                        {
                            passwordItem.SetName(passwordName);
                            adapter.notifyDataSetChanged();
                        }
                    })
                    .setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.dismiss())
                    .create()
                    .show()
                ;
            }
            return true;
        });
        //显示菜单
        popupMenu.show();
    }
}