package com.zhongyang219.randompasswordgenerator;

import android.util.Base64;
import java.nio.charset.StandardCharsets;

public class Utilities
{
    public static String Base64Encode(String content)
    {
        byte[] contentByte = content.getBytes(StandardCharsets.UTF_8);
        return Base64.encodeToString(contentByte, Base64.DEFAULT);
    }

    public static String Base64Decode(String content)
    {
        byte[] contentByte;
        try
        {
            contentByte = Base64.decode(content, Base64.DEFAULT);
        }
        catch (IllegalArgumentException e)
        {
            return content;
        }
        return new String(contentByte, StandardCharsets.UTF_8);
    }
}
