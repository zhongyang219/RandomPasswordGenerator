package com.zhong.randompasswordgenerator;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;

//用于表示字符类型的枚举
enum CharType
{
    CK_NUMBERS,         //数字
    CK_CAPITAL,         //大写字母
    CK_LOWERCASE,       //小写字母
    CK_SPECIAL_CHAR       //特殊字符
}


public class MainActivity extends AppCompatActivity
{
    //私有的字段
    protected ArrayList<CharType> m_option_check = new ArrayList<>();
    final String m_specialChar = "~!@#$%^&*()_-+={}[]|\\<>/?";   //特殊字符
    Random m_random = new Random();
    final int MAX_LENGTH = 128;

    //控件变量
    private CheckBox m_numbers_chk;
    private CheckBox m_capital_chk;
    private CheckBox m_lowercase_chk;
    private CheckBox m_special_chars_chk;
    private EditText m_length_edit;
    private EditText m_result_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //将控件变量与控件ID关联
        m_numbers_chk = findViewById(R.id.check_number);
        m_capital_chk = findViewById(R.id.check_captal);
        m_lowercase_chk = findViewById(R.id.check_lowercase);
        m_special_chars_chk = findViewById(R.id.check_specal_char);
        m_length_edit = findViewById(R.id.edit_length);
        m_result_edit = findViewById(R.id.editResult);

        LoadConfig();
        GlobalData.getInstance().LoadPasswordList(this);
    }

    @SuppressLint("DefaultLocale")
    public void onClick(View view)
    {
        int id = view.getId();
        if(id==R.id.button_generate)
        {
            //获取复选框的选中状态
            m_option_check.clear();
            if(m_numbers_chk.isChecked())
                m_option_check.add(CharType.CK_NUMBERS);
            if(m_capital_chk.isChecked())
                m_option_check.add(CharType.CK_CAPITAL);
            if(m_lowercase_chk.isChecked())
                m_option_check.add(CharType.CK_LOWERCASE);
            if(m_special_chars_chk.isChecked())
                m_option_check.add(CharType.CK_SPECIAL_CHAR);

            if(m_length_edit.getText().length()==0)
            {
                Toast.makeText(this, getString(R.string.input_password_length_warning), Toast.LENGTH_SHORT).show();
                return;
            }
            if(m_option_check.isEmpty())
            {
                Toast.makeText(this, getString(R.string.select_character_type_warning), Toast.LENGTH_SHORT).show();
                return;
            }
            //获取密码的长度
            int length;
            try
            {
                length = Integer.parseInt(m_length_edit.getText().toString());
            }
            catch (NumberFormatException e)
            {
                Toast.makeText(this, getString(R.string.numbers_only_warning), Toast.LENGTH_SHORT).show();
                return;
            }
            if(length == 0)
            {
                Toast.makeText(this, getString(R.string.password_length_zero_warning), Toast.LENGTH_SHORT).show();
                return;
            }
            if(length>MAX_LENGTH)
            {
                Toast.makeText(this, getString(R.string.password_length_exceed_warning, MAX_LENGTH), Toast.LENGTH_SHORT).show();
                return;
            }
            StringBuilder password = new StringBuilder();

            for (int i = 0; i< length; i++)
            {
                int index = m_random.nextInt(m_option_check.size());  //随机确定生成哪一种字符
                CharType charType = m_option_check.get(index);
                char currentChar = '\0';
                switch(charType)
                {
                    case CK_NUMBERS:
                        currentChar = (char)(m_random.nextInt(10)+'0');    //随机生成一个0~9的数字
                        break;
                    case CK_CAPITAL:
                        currentChar = (char)(m_random.nextInt(26)+'A');    //随机生成一个大写字母
                        break;
                    case CK_LOWERCASE:
                        currentChar = (char)(m_random.nextInt(26)+'a');    //随机生成一个小写字母
                        break;
                    case CK_SPECIAL_CHAR:
                        int random = m_random.nextInt(m_specialChar.length());
                        currentChar = m_specialChar.charAt(random);     //随机生成一个特殊字符
                        break;
                }
                password.append(currentChar);
            }
            m_result_edit.setText(password.toString());
            SaveConfig();
        }
        //点击了复制
        else if(id == R.id.copy)
        {
            ClipboardManager myClipboard;
            myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
            ClipData myClip;
            String text = m_result_edit.getText().toString();
            if (text.isEmpty())
            {
                Toast.makeText(this, getString(R.string.password_not_generated_warning), Toast.LENGTH_SHORT).show();
            }
            else
            {
                myClip = ClipData.newPlainText("text", text);
                myClipboard.setPrimaryClip(myClip);
                Toast.makeText(this, getString(R.string.password_copied_info), Toast.LENGTH_SHORT).show();
            }
        }

        //点击了“密码列表”
        else if (id == R.id.passwordListBtn)
        {
            Intent intent = new Intent(this, PasswordList.class);
            startActivity(intent);
        }

        //点击了“添加到密码列表”
        else if (id == R.id.addToListBtn)
        {
            String passwordValue = m_result_edit.getText().toString();
            if (passwordValue.isEmpty())
            {
                Toast.makeText(this, getString(R.string.password_not_generated_warning), Toast.LENGTH_SHORT).show();
            }
            else
            {
                View dialog_view = getLayoutInflater().inflate(R.layout.input_dialog_view, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.input_password_name_tip))
                        .setView(dialog_view)
                        .setPositiveButton(getString(R.string.ok), (dialog, which) -> {
                            EditText editText = dialog_view.findViewById(R.id.input_dialog_text_edit);
                            String passwordName = editText.getText().toString();
                            if (passwordName.isEmpty())
                            {
                                Toast.makeText(this, getString(R.string.input_password_name_warning), Toast.LENGTH_SHORT).show();
                            }
                            else
                            {                                //添加到密码列表
                                GlobalData.getInstance().AddPassword(passwordName, passwordValue);
                                String tip = getString(R.string.password_added_tip, passwordValue);
                                Toast.makeText(this, tip, Toast.LENGTH_SHORT).show();
                                GlobalData.getInstance().SavePasswordList(this);}
                        })
                        .setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.dismiss())
                        .create()
                        .show()
                ;
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void LoadConfig()
    {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        m_numbers_chk.setChecked(preferences.getBoolean("numbers_chk", true));
        m_capital_chk.setChecked(preferences.getBoolean("capital_chk", false));
        m_lowercase_chk.setChecked(preferences.getBoolean("lowercase_chk", true));
        m_special_chars_chk.setChecked(preferences.getBoolean("special_chars_chk", false));
        m_length_edit.setText(Integer.toString(preferences.getInt("length", 8)));
    }

    private void SaveConfig()
    {
        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        editor.putBoolean("numbers_chk", m_numbers_chk.isChecked());
        editor.putBoolean("capital_chk", m_capital_chk.isChecked());
        editor.putBoolean("lowercase_chk", m_lowercase_chk.isChecked());
        editor.putBoolean("special_chars_chk", m_special_chars_chk.isChecked());
        editor.putInt("length", Integer.parseInt(m_length_edit.getText().toString()));
        editor.apply();
    }
}
