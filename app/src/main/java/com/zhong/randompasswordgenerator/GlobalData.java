package com.zhong.randompasswordgenerator;

import android.content.Context;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
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
        passwordList.add(new PasswordListItem(passwordName, passwordValue));
    }

    void LoadPasswordList(Context context)
    {
        FileInputStream in = null;
        BufferedReader reader = null;
        try
        {
            in = context.openFileInput("passwordList.xml");
            //解析xml
            try
            {
                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(in, null);
                parser.nextTag();

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
        finally
        {
            try
            {
                if (reader != null)
                    reader.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    void SavePasswordList(Context context)
    {
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try
        {
            out = context.openFileOutput("passwordList.xml", Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            //生成字符串
            StringBuilder xmlContents = new StringBuilder();
            xmlContents.append("<passwordList>\n");
            for (PasswordListItem data : passwordList )
            {
                String lineStr = String.format("  <password name=\"%s\" value=\"%s\"/>\n", data.GetName(), data.GetPassword());
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
