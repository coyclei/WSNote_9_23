package com.ws.coyc.wsnote.UI.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.ws.coyc.wsnote.Data.InfoWait;
import com.ws.coyc.wsnote.R;
import com.ws.coyc.wsnote.SQLiteHelper.Utils.l;
import com.ws.coyc.wsnote.UI.Activity.ImageViewActivity;
import com.ws.coyc.wsnote.UI.Layout.CircleImageView;
import com.ws.coyc.wsnote.Utils.DateUtils;
import com.ws.coyc.wsnote.Utils.ImageLoader;
import com.ws.coyc.wsnote.Utils.MyColor;
import com.ws.coyc.wsnote.Utils.SDFile;

import java.util.ArrayList;


/**
 * Created by leipe on 2016/5/20.
 */
public class Info1Adapter extends BaseAdapter {

    public ArrayList<InfoWait> info = new ArrayList<InfoWait>();
    private LayoutInflater inflater;
    private Context mContext;
    private ListPosition position;


    public Info1Adapter(ArrayList<InfoWait> ps , Context c,ListPosition p)
    {
        info = ps;
        mContext = c;
        this.position = p;
        if(mContext == null)
        {
            l.l("context is null");
        }
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
            convertView = inflater.inflate(R.layout.item_1,parent,false);
            initView(convertView, holder);
            convertView.setTag(holder);
        }else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        int id = info.size()-position;
        InfoWait infoWait = info.get(position);

        holder.id.setText(id+"");
        holder.name.setText(infoWait.person.name);
        holder.goods.setText(infoWait.goods);
        String week = DateUtils.getWeek(infoWait.dateStart);
        holder.create_date.setText(week+" "+DateUtils.ConverToString_MDHM(infoWait.dateStart));

        if(SDFile.CheckFileExistsABS(infoWait.image_url))
        {
            holder.id.setBackgroundColor(MyColor.red_5);
            ImageLoader.getInstance().loadImage(infoWait.image_url,holder.image,5);
        }else
        {
            holder.id.setBackgroundColor(Color.TRANSPARENT);
            holder.image.setVisibility(View.GONE);
        }

        if(this.position.position == position)
        {
            holder.address.setVisibility(View.VISIBLE);
            holder.phone.setVisibility(View.VISIBLE);

            holder.address.setText("地    址 : "+infoWait.person.address1);
            holder.phone.setText("手机号 : "+infoWait.person.phone);
            holder.image.setVisibility(View.VISIBLE);
            holder.image.setVisibility(View.VISIBLE);

            if(SDFile.CheckFileExistsABS(infoWait.image_url))
            {
                holder.image.setVisibility(View.VISIBLE);
            }else
            {
                holder.image.setVisibility(View.GONE);
            }

            convertView.setBackgroundColor(MyColor.coffe_6);
        }else
        {
            holder.address.setVisibility(View.GONE);
            holder.phone.setVisibility(View.GONE);
//            holder.more.setVisibility(View.GONE);
            holder.image.setVisibility(View.GONE);
            convertView.setBackgroundColor(MyColor.coffe_5);
        }

        if(info.get(position).isChoose)
        {
            convertView.setBackgroundColor(Color.argb(255,255,150,150));
        }
        return convertView;
    }

    private void initView(View convertView, ViewHolder holder) {
        holder.id = (TextView) convertView.findViewById(R.id.tv_id);
        holder.create_date = (TextView) convertView.findViewById(R.id.tv_date_create);
        holder.name = (TextView) convertView.findViewById(R.id.tv_name);
        holder.goods = (TextView) convertView.findViewById(R.id.tv_goods);
//        holder.bill_id = (TextView) convertView.findViewById(R.id.tv_bill_id);
        holder.phone = (TextView) convertView.findViewById(R.id.tv_phone);
        holder.address = (TextView) convertView.findViewById(R.id.tv_address);
        holder.image = (CircleImageView) convertView.findViewById(R.id.iv_image);


        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ImageViewActivity.class);
                intent.putExtra("path",info.get(position.position).image_url);
                mContext.startActivity(intent);
            }
        });
    }


    public class ViewHolder{

        public TextView id;
        public TextView create_date;
        public TextView name;
        public TextView goods;
//        public TextView bill_id;
        public TextView phone;
        public TextView address;
        public CircleImageView image;
//        public Button more;
        public ViewHolder()
        {

        }


    }
}
