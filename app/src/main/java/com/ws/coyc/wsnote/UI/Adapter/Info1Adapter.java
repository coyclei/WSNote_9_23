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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.ws.coyc.wsnote.Data.InfoWait;
import com.ws.coyc.wsnote.R;
import com.ws.coyc.wsnote.SQLiteHelper.Utils.l;
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

            holder.address.setText(infoWait.person.address1);
//            holder.address.setText("地    址 : "+infoWait.person.address1);
            holder.phone.setText(infoWait.person.phone);
//            holder.phone.setText("手机号 : "+infoWait.person.phone);
            holder.image.setVisibility(View.VISIBLE);
            holder.image.setVisibility(View.VISIBLE);
            holder.ib_phone.setVisibility(View.VISIBLE);
            holder.ib_address.setVisibility(View.VISIBLE);

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
            holder.image.setVisibility(View.GONE);
            holder.ib_phone.setVisibility(View.GONE);
            holder.ib_address.setVisibility(View.GONE);
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
        holder.ib_phone = (ImageButton) convertView.findViewById(R.id.ib_phone);
        holder.ib_address = (ImageButton) convertView.findViewById(R.id.ib_address);



        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Intent intent = new Intent(mContext, ImageViewActivity.class);
                intent.putExtra("path",info.get(position.position).image_url);
                mContext.startActivity(intent);
            }
        });

        holder.ib_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;  //调起百度地图客户端（Android）
                try {
                    intent = Intent.getIntent("intent://map/line?coordtype=&zoom=&region=中国&name="+info.get(position.position).person.address1+"&src=coyc|wsnote#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

                mContext.startActivity(intent);   //启动调用
            }
        });
        holder.ib_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("18274833970"));
                try{
                    mContext.startActivity(intent);
                }catch (ActivityNotFoundException e)
                {
                    Toast.makeText(mContext,"Android系统版本不支持",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
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
        public ImageButton ib_phone;
        public ImageButton ib_address;
//        public Button more;
        public ViewHolder()
        {

        }
    }
}
