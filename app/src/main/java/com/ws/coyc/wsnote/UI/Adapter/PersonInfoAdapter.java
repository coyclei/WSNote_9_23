package com.ws.coyc.wsnote.UI.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ws.coyc.wsnote.Data.Person;
import com.ws.coyc.wsnote.R;
import com.ws.coyc.wsnote.Utils.MyColor;

import java.util.ArrayList;


/**
 * Created by leipe on 2016/5/20.
 */
public class PersonInfoAdapter extends BaseAdapter {

    public ArrayList<Person> info = new ArrayList<Person>();
    private LayoutInflater inflater;
    private Context mContext;

    public PersonInfoAdapter(ArrayList<Person> ps , Context c)
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
            convertView = inflater.inflate(R.layout.item_person,parent,false);
            initView(convertView, holder);


            convertView.setTag(holder);
        }else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        Person person = info.get(position);
        
        
        holder.id.setText(position+1+"");
        holder.name.setText(person.name);

        holder.count.setText("单数:"+person.count);
        holder.all_prise.setText("成交额:"+person.all_prise_out);
        int b =person.all_prise_out-person.all_prise_in ;
        if(b<0)
        {
            holder.get_money.setTextColor(MyColor.red_5);
            holder.get_money.setText("总亏损:"+(b));
        }else
        {
            holder.get_money.setTextColor(MyColor.white);
            holder.get_money.setText("总利润:"+(b));
        }


            holder.address.setVisibility(View.VISIBLE);
            holder.phone.setVisibility(View.VISIBLE);

            holder.address.setText("地    址 : "+person.address1);
            holder.phone.setText("手机号 : "+person.phone);

            convertView.setBackgroundColor(MyColor.coffe_6);


        return convertView;
    }

    private void initView(View convertView, ViewHolder holder) {
        holder.id = (TextView) convertView.findViewById(R.id.tv_id);

        holder.name = (TextView) convertView.findViewById(R.id.tv_name);
        holder.count = (TextView) convertView.findViewById(R.id.tv_jh_money);
        holder.all_prise = (TextView) convertView.findViewById(R.id.tv_ch_money);
        holder.get_money = (TextView) convertView.findViewById(R.id.tv_get_money);
        holder.phone = (TextView) convertView.findViewById(R.id.tv_phone);
        holder.address = (TextView) convertView.findViewById(R.id.tv_address);

    }


    public class ViewHolder{
        public TextView id;
        public TextView name;
        public TextView count;
        public TextView all_prise;
        public TextView get_money;
        public TextView phone;
        public TextView address;


    }
}
