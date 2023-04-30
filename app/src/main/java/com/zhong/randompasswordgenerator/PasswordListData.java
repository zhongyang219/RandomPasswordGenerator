package com.zhong.randompasswordgenerator;

import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PasswordListData
{
    public PasswordListData(android.content.Context context)
    {

        m_listData = new ArrayList<>();
        m_passwordDataAdapter = new SimpleAdapter(context, m_listData, android.R.layout.simple_list_item_1,
                new String[]{"name", "password"}, new int[]{R.id.password_name, R.id.password_value});
        m_passwordDataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
    }

    //密码字段列表
    private List<Map<String, Object>> m_listData;
    private SimpleAdapter m_passwordDataAdapter;

    public SimpleAdapter GetAdapter()
    {
        return m_passwordDataAdapter;
    }

    public void AddData(String name, String password)
    {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("name", name);
        dataMap.put("password", password);
        m_listData.add(dataMap);
    }

    public void RemoveData(int index)
    {
        m_listData.remove(index);
    }
}
