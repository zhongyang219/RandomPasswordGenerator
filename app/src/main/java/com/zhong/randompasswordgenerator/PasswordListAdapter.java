package com.zhong.randompasswordgenerator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.List;

public class PasswordListAdapter extends ArrayAdapter<PasswordListItem>
{
    private int resourceId;
    public PasswordListAdapter(@NonNull Context context, int resource, @NonNull List<PasswordListItem> objects)
    {
        super(context, resource, objects);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        PasswordListItem listData = getItem(position);      //获取当前项的PasswordListItem实例
        View view;
        ViewHolder viewHolder;
        if (convertView == null)
        {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.passwordNameView = (TextView) view.findViewById(R.id.password_name);
            viewHolder.passwordValueView = (TextView) view.findViewById(R.id.password_value);
            view.setTag(viewHolder);
        }
        else
        {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.passwordNameView.setText(listData.GetName());
        viewHolder.passwordValueView.setText(listData.GetPassword());
        return view;
    }

    class ViewHolder
    {
        TextView passwordNameView;
        TextView passwordValueView;
    }
}
