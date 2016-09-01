package com.ws.coyc.wsnote.UI.Adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.ws.coyc.wsnote.Data.InfoIng;
import com.ws.coyc.wsnote.R;
import com.ws.coyc.wsnote.UI.Activity.ImageViewActivity;
import com.ws.coyc.wsnote.UI.Layout.CircleImageView;
import com.ws.coyc.wsnote.Utils.DateUtils;
import com.ws.coyc.wsnote.Utils.ImageLoader;
import com.ws.coyc.wsnote.Utils.MyColor;
import com.ws.coyc.wsnote.Utils.SDFile;

import java.net.URISyntaxException;
import java.util.ArrayList;


/**
 * Created by leipe on 2016/5/20.
 */
public class Info2Adapter extends BaseAdapter {

    public ArrayList<InfoIng> info = new ArrayList<InfoIng>();
    private LayoutInflater inflater;
    private Context mContext;
    private ListPosition position;
    public Info2Adapter(ArrayList<InfoIng> ps , Context c,ListPosition p)
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
            convertView = inflater.inflate(R.layout.item_2,parent,false);
            initView(convertView, holder);

            convertView.setTag(holder);
        }else
        {
            holder = (ViewHolder) convertView.getTag();
        }


        int id = info.size()-position;

        holder.id.setText(id+"");
        InfoIng infoIng = info.get(position);

        holder.name.setText(infoIng.person.name);
        holder.goods.setText(infoIng.goods);
        String week = DateUtils.getWeek(infoIng.dateStart);
        holder.create_date.setText(week+" "+DateUtils.ConverToString_MDHM(infoIng.dateStart));
        holder.jh_moeny.setText("进价："+infoIng.all_in_money);

        if(SDFile.CheckFileExistsABS(infoIng.image_url))
        {
            holder.id.setBackgroundColor(MyColor.red_5);
            ImageLoader.getInstance().loadImage(infoIng.image_url,holder.image,5);
        }else
        {
            holder.id.setBackgroundColor(Color.TRANSPARENT);
            holder.image.setVisibility(View.GONE);
        }
        if(this.position.position == position)
        {
            holder.address.setVisibility(View.VISIBLE);
            holder.phone.setVisibility(View.VISIBLE);

            holder.address.setText("地    址 : "+infoIng.person.address1);
            holder.phone.setText("手机号 : "+infoIng.person.phone);
            if(SDFile.CheckFileExistsABS(infoIng.image_url))
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
        
        
        if(infoIng.fh_state.equals(InfoIng.FH_STATE_TRUE))
        {
            holder.fh_state.setTextColor(Color.WHITE);
        }else
        {
            holder.fh_state.setTextColor(MyColor.red_5);
        }
        if(infoIng.fk_state.equals(InfoIng.FK_STATE_TRUE))
        {
            holder.fk_state.setTextColor(Color.WHITE);
        }else
        {
            holder.fk_state.setTextColor(MyColor.red_5);
        }

        holder.fh_state.setText(infoIng.fh_state+"");
        holder.fk_state.setText(infoIng.fk_state+"");

        if(infoIng.isChoose)
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

        holder.jh_moeny = (TextView) convertView.findViewById(R.id.tv_jh_money);
        holder.fh_state = (TextView) convertView.findViewById(R.id.tv_fh_state);
        holder.fk_state = (TextView) convertView.findViewById(R.id.tv_fk_state);
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
        holder.address.setLongClickable(true);
        holder.address.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                Intent intent = null;  //调起百度地图客户端（Android）展示上海市"28"路公交车的检索结果
                try {
                    intent = Intent.getIntent("intent://map/line?coordtype=&zoom=&region=中国&name="+info.get(position.position).person.address1+"&src=coyc|wsnote#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                mContext.startActivity(intent);   //启动调用

                return true;
            }
        });
        holder.phone.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(info.get(position.position).person.phone));
                try{
                    mContext.startActivity(intent);
                }catch (ActivityNotFoundException e)
                {
                    Toast.makeText(mContext,"Android系统版本不支持",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

                return true;
            }
        });

    }


    public class ViewHolder{
        public TextView id;

        public TextView create_date;
        public TextView name;
        public TextView goods;

        public TextView jh_moeny;
        public TextView fh_state;
        public TextView fk_state;

        public TextView phone;
        public TextView address;
        public CircleImageView image;
    }
}
