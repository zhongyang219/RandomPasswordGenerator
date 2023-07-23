package com.zhong.randompasswordgenerator;

import android.util.Base64;
import java.io.UnsupportedEncodingException;

public class Utilities
{
    public static String Base64Encode(String content)
    {
        try
        {
            byte[] contentByte = content.getBytes("UTF-8");
            return Base64.encodeToString(contentByte, Base64.DEFAULT);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return "";
    }

    public static String Base64Decode(String content)
    {
        byte[] contentByte = Base64.decode(content, Base64.DEFAULT);
        try
        {
            return new String(contentByte, "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return "";
    }
}
