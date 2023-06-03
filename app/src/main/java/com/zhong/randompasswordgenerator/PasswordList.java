package com.zhong.randompasswordgenerator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class PasswordList extends AppCompatActivity
{
    private List<PasswordListItem> passwordList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_list);

        //设置页面标题
        this.setTitle("密码列表");

        //添加返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setHomeButtonEnabled(true);//设置左上角的图标是否可以点击
            actionBar.setDisplayHomeAsUpEnabled(true);//给左上角图标的左边加上一个返回的图标
        }

        //初始化密码列表数据
        InitPasswordData();

        //设置适配器
        PasswordListAdapter adapter = new PasswordListAdapter(this, R.layout.password_list_item, passwordList);
        ListView listView = (ListView) findViewById(R.id.passwordListView);
        listView.setAdapter(adapter);

        //设置列表响应点击事件
        listView.setOnItemClickListener((parent, view, position, id) -> {
            PasswordListItem passwordItem = passwordList.get(position);
            //复制密码到剪贴版
            CopyStringToClipBoard(passwordItem.GetPassword());
        });

//        listView.setOnItemLongClickListener((parent, view, position, id) -> {
//            PasswordListItem passwordItem = passwordList.get(position);
//            //复制密码到剪贴版
//            CopyStringToClipBoard(passwordItem.GetPassword());
//            return false;
//        });
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

    private void InitPasswordData()
    {
        passwordList.add(new PasswordListItem("测试密码", "123456"));
        passwordList.add(new PasswordListItem("测试密码2", "233333"));
    }

    private void CopyStringToClipBoard(String text)
    {
        ClipboardManager myClipboard;
        myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        ClipData myClip;
        myClip = ClipData.newPlainText("text", text);
        myClipboard.setPrimaryClip(myClip);
        Toast.makeText(this, "密码已经复制到剪贴板。", Toast.LENGTH_SHORT).show();
    }
}