package com.zhong.randompasswordgenerator;

import android.content.Context;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class GlobalData
{
    static private GlobalData instance;

    public static GlobalData getInstance()
    {
        if (instance == null)
        {
            instance = new GlobalData();
        }
        return instance;
    }
    private final List<PasswordListItem> passwordList = new ArrayList<>();

    List<PasswordListItem> GetPasswordList()
    {
        return passwordList;
    }

    void AddPassword(String passwordName, String passwordValue)
    {
        PasswordListItem newItem = new PasswordListItem(passwordName, passwordValue);
        //获取当前时间
        newItem.SetCreateTime(System.currentTimeMillis());
        passwordList.add(newItem);
    }

    void LoadPasswordList()
    {
        passwordList.clear();
        FileInputStream in;
        try
        {
            Context context = PasswordGeneratorApplication.getInstance().getApplicationContext();
            in = context.openFileInput("passwordList.xml");
            //解析xml
            try
            {
                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(in, null);
//                parser.nextTag();
                int eventType = parser.getEventType();
                PasswordListItem passwordItem = null;
                while (eventType != XmlPullParser.END_DOCUMENT)
                {
                    switch (eventType)
                    {
                        case XmlPullParser.START_TAG:
                            //开始标签,parser获取当前事件对应的元素名字
                            if ("password".equals(parser.getName()))
                            {
                                String passwordName = parser.getAttributeValue(null, "name");
                                String passwordValue = parser.getAttributeValue(null, "value");
                                String passwordCreateTime = parser.getAttributeValue(null, "createTime");
                                String passwordNameDecoded = Utilities.Base64Decode(passwordName);
                                String passwordValueDecode = Utilities.Base64Decode(passwordValue);
                                passwordItem = new PasswordListItem(passwordNameDecoded, passwordValueDecode);
                                try
                                {
                                    passwordItem.SetCreateTime(Long.parseLong(passwordCreateTime));
                                }
                                catch (NumberFormatException e)
                                {
                                    passwordItem.SetCreateTime(0);
                                }
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            if ("password".equals(parser.getName()))
                            {
                                if (passwordItem != null)
                                    passwordList.add(passwordItem);
                                break;
                            }
                            break;
                        default:
                            break;
                    }
                    //调用parser.next()方法解析下一个元素
                    eventType = parser.next();
                }
            }
            catch (XmlPullParserException ignored)
            {
            }
            finally
            {
                in.close();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    void SavePasswordList()
    {
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try
        {
            Context context = PasswordGeneratorApplication.getInstance().getApplicationContext();
            out = context.openFileOutput("passwordList.xml", Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            //生成字符串
            StringBuilder xmlContents = new StringBuilder();
            xmlContents.append("<passwordList>\n");
            for (PasswordListItem data : passwordList )
            {
                String passwordNameEncoded = Utilities.Base64Encode(data.GetName());
                String passwordEncoded = Utilities.Base64Encode(data.GetPassword());
                String lineStr = String.format("  <password name=\"%s\" value=\"%s\" createTime=\"%s\"/>\n",
                        passwordNameEncoded, passwordEncoded, data.GetCreateTime());
                xmlContents.append(lineStr);
            }
            xmlContents.append("</passwordList>\n");
            writer.write(xmlContents.toString());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (writer != null)
                    writer.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
