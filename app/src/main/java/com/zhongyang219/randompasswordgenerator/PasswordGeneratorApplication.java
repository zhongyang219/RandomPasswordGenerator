package com.zhongyang219.randompasswordgenerator;

import android.app.Application;

public class PasswordGeneratorApplication extends Application
{
    private static PasswordGeneratorApplication instance;

    public static PasswordGeneratorApplication getInstance()
    {
        return instance;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        instance = this;
    }
}
