package com.ws.coyc.wsnote.UI.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.ws.coyc.wsnote.Data.InfoOver;
import com.ws.coyc.wsnote.R;
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
public class Info3Adapter extends BaseAdapter {

    public ArrayList<InfoOver> info = new ArrayList<InfoOver>();
    private LayoutInflater inflater;
    private Context mContext;
    private ListPosition position;

    public Info3Adapter(ArrayList<InfoOver> ps , Context c,ListPosition p)
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
            convertView = inflater.inflate(R.layout.item_3,parent,false);
            initView(convertView, holder);


            convertView.setTag(holder);
        }else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        int id = info.size()-position;
        InfoOver infoOver = info.get(position);
        
        
        holder.id.setText(id+"");
        holder.name.setText(infoOver.person.name);
        holder.goods.setText(infoOver.goods);
        String week1 = DateUtils.getWeek(infoOver.dateStart);
        String week2 = DateUtils.getWeek(infoOver.end_date);

//        holder.create_date.setText(week1+" "+DateUtils.ConverToString_MDHM(infoOver.dateStart));
        holder.create_date.setText(week1+" "+DateUtils.ConverToString_MDHM(infoOver.end_date));
//        holder.end_date.setText(week2+" "+DateUtils.ConverToString_MDHM(infoOver.end_date));
        holder.end_date.setText("");
        holder.jh_money.setText("进价:"+infoOver.all_in_money);
        holder.ch_money.setText("售价:"+infoOver.all_out_money);
        int a =infoOver.all_in_money ;
        int b =infoOver.all_out_money ;
        if(b-a<0)
        {
            holder.get_money.setTextColor(MyColor.red_5);
            holder.get_money.setText("亏损:"+(a-b));
        }else
        {
            holder.get_money.setTextColor(MyColor.white);
            holder.get_money.setText("利润:"+(b-a));
        }

        if(SDFile.CheckFileExistsABS(infoOver.image_url))
        {
            holder.id.setBackgroundColor(MyColor.red_5);
            ImageLoader.getInstance().loadImage(infoOver.image_url,holder.image,5);
        }else
        {
            holder.id.setBackgroundColor(Color.TRANSPARENT);
            holder.image.setVisibility(View.GONE);
        }
        if(this.position.position == position)
        {
            holder.address.setVisibility(View.VISIBLE);
            holder.phone.setVisibility(View.VISIBLE);

            holder.address.setText("地    址 : "+infoOver.person.address1);
            holder.phone.setText("手机号 : "+infoOver.person.phone);
            if(SDFile.CheckFileExistsABS(infoOver.image_url))
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
            holder.image.setVisibility(View.GONE);
            convertView.setBackgroundColor(MyColor.coffe_5);
        }


        if(infoOver.isChoose)
        {
            convertView.setBackgroundColor(Color.argb(255,255,0,0));
        }

        return convertView;
    }

    private void initView(View convertView, ViewHolder holder) {
        holder.id = (TextView) convertView.findViewById(R.id.tv_id);

        holder.create_date = (TextView) convertView.findViewById(R.id.tv_date_create);
        holder.name = (TextView) convertView.findViewById(R.id.tv_name);
        holder.goods = (TextView) convertView.findViewById(R.id.tv_goods);

        holder.end_date = (TextView) convertView.findViewById(R.id.tv_date_end);
        holder.jh_money = (TextView) convertView.findViewById(R.id.tv_jh_money);
        holder.ch_money = (TextView) convertView.findViewById(R.id.tv_ch_money);
        holder.get_money = (TextView) convertView.findViewById(R.id.tv_get_money);
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

        public TextView end_date;
        public TextView jh_money;
        public TextView ch_money;
        public TextView get_money;
//        public TextView bill_id;
        public TextView phone;
        public TextView address;
        public CircleImageView image;
//        public Button more;


    }
}
