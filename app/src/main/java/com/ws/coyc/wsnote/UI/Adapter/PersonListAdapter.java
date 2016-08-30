package com.ws.coyc.wsnote.UI.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ws.coyc.wsnote.Data.DateInfo;
import com.ws.coyc.wsnote.Data.Person;
import com.ws.coyc.wsnote.R;

import java.util.ArrayList;


/**
 * Created by leipe on 2016/5/20.
 */
public class PersonListAdapter extends BaseAdapter {

    public ArrayList<Person> info = new ArrayList<>();
    private LayoutInflater inflater;
    private Context mContext;

    public PersonListAdapter(ArrayList<Person> ps , Context c)
    {
        info = ps;
        mContext = c;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return info.size();
    }

    @Override
    public Object getItem(int position) {
        return info.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if(convertView == null)
        {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_dateselect,parent,false);
            initView(convertView, holder);
            convertView.setTag(holder);
        }else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        initData(position, convertView, holder);

        return convertView;
    }

    private void initData(int position, View convertView, ViewHolder holder) {
        holder.date.setText(info.get(position).name);
        holder.date.setTextColor(Color.WHITE);

    }

    private void initView(View convertView, ViewHolder holder) {
        holder.date = (TextView) convertView.findViewById(R.id.tv_date_text);
    }


    public class ViewHolder{

        private TextView date;

    }
}
