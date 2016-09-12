package com.ws.coyc.wsnote.UI.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ws.coyc.wsnote.Data.Goods;
import com.ws.coyc.wsnote.R;
import com.ws.coyc.wsnote.UI.Activity.ImageViewActivity;
import com.ws.coyc.wsnote.UI.Layout.CircleImageView;
import com.ws.coyc.wsnote.Utils.ImageLoader;
import com.ws.coyc.wsnote.Utils.MyColor;
import com.ws.coyc.wsnote.Utils.SDFile;

import java.util.ArrayList;


/**
 * Created by leipe on 2016/5/20.
 */
public class GoodsInfoAdapter extends BaseAdapter {

    public ArrayList<Goods> info = new ArrayList<>();
    private LayoutInflater inflater;
    private Context mContext;

    public GoodsInfoAdapter(ArrayList<Goods> ps , Context c)
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
            convertView = inflater.inflate(R.layout.item_goods,parent,false);
            initView(convertView, holder,position);

            convertView.setTag(holder);
        }else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        Goods goods = info.get(position);

        holder.id.setText(position+1+"");
        holder.name.setText(goods.name);
        if(goods.count<=0)
        {
            holder.count.setText("缺货");
        }else
        {
            holder.count.setText(""+goods.count);
        }

        holder.money_in.setText("进价:"+goods.money_in_temp);
        holder.money_out.setText("售价:"+goods.money_out_temp);
        holder.info.setText(goods.info);

        if(SDFile.CheckFileExistsABS(goods.image_url_local))
        {
            holder.imageView.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().loadImage(goods.image_url_local,holder.imageView,4);

        }else
        {
            holder.imageView.setVisibility(View.GONE);
        }


//        convertView.setBackgroundColor(MyColor.coffe_5);

        return convertView;
    }

    private void initView(View convertView, ViewHolder holder,final int position) {
        holder.id = (TextView) convertView.findViewById(R.id.tv_id);

        holder.name = (TextView) convertView.findViewById(R.id.tv_name);
        holder.count = (TextView) convertView.findViewById(R.id.tv_ware_count);
        holder.money_in = (TextView) convertView.findViewById(R.id.tv_money_in);
        holder.money_out = (TextView) convertView.findViewById(R.id.tv_money_out);
        holder.info = (TextView) convertView.findViewById(R.id.tv_info);
        holder.imageView = (CircleImageView) convertView.findViewById(R.id.iv_image);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, ImageViewActivity.class);
                intent.putExtra("path",info.get(position).image_url_local);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
    }


    public class ViewHolder{
        public TextView id;
        public TextView name;
        public TextView count;
        public TextView money_in;
        public TextView money_out;
        public TextView info;

        public CircleImageView imageView;


    }
}
