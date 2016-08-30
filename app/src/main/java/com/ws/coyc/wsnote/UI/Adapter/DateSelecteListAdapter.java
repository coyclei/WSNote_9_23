package com.ws.coyc.wsnote.UI.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ws.coyc.wsnote.Data.DateInfo;
import com.ws.coyc.wsnote.Data.InfoWait;
import com.ws.coyc.wsnote.R;
import com.ws.coyc.wsnote.Utils.DateUtils;

import java.io.File;
import java.util.ArrayList;


/**
 * Created by leipe on 2016/5/20.
 */
public class DateSelecteListAdapter extends BaseAdapter {

    public ArrayList<DateInfo> info = new ArrayList<>();
    private LayoutInflater inflater;
    private Context mContext;
    private ListPosition position;

    public DateSelecteListAdapter(ArrayList<DateInfo> ps , Context c, ListPosition p)
    {
        info = ps;
        mContext = c;
        this.position = p;
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
        holder.date.setText(info.get(position).date+"");
        if(this.position.position == position)
        {
            holder.date.setTextColor(Color.WHITE);
            convertView.setBackgroundColor(Color.argb(127,255,30,30));
        }else
        {
            holder.date.setTextColor(Color.BLACK);
            convertView.setBackgroundColor(Color.WHITE);
        }
    }

    private void initView(View convertView, ViewHolder holder) {
        holder.date = (TextView) convertView.findViewById(R.id.tv_date_text);
    }


    public class ViewHolder{

        private TextView date;

    }
}
