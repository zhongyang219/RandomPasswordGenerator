package com.zhong.randompasswordgenerator;

public class PasswordListItem
{
    private String name;
    private String password;

    public PasswordListItem(String name, String password)
    {
        this.name = name;
        this.password = password;
    }

    public String GetName()
    {
        return name;
    }

    public String GetPassword()
    {
        return password;
    }

    public void SetName(String name)
    {
        this.name = name;
    }
}
