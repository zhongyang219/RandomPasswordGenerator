package com.zhong.randompasswordgenerator;

public class PasswordListItem
{
    private String name;
    private String password;
    private long createTime;    //创建时间，通过System.currentTimeMillis()得到

    public PasswordListItem(String name, String password)
    {
        this.name = name;
        this.password = password;
        this.createTime = 0;
    }

    public String GetName()
    {
        return name;
    }

    public String GetPassword()
    {
        return password;
    }

    public long GetCreateTime()
    {
        return createTime;
    }

    public void SetName(String name)
    {
        this.name = name;
    }

    public void SetCreateTime(long createTime)
    {
        this.createTime = createTime;
    }
}
