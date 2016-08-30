package com.ws.coyc.wsnote.UI.DialogFragment;

import android.app.DialogFragment;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ws.coyc.wsnote.R;

/**
 * Created by coyc on 16-8-27.
 */

public class FatherDialog extends DialogFragment {

    protected TextView mTv_tille;
    protected EditText mText_name;
    protected EditText mText_goods;
    protected EditText mTv_phone;
    protected EditText mTv_address;

    protected String mTitle;
    protected String  name = "";
    protected String goods = "";
    protected String phone = "";
    protected String address = "";

    protected View v;
    protected ListView mLv_list;

    public FatherDialog()
    {

    }


    public void initData()
    {

    }

    public void initView()
    {
        mTv_tille = (TextView) v.findViewById(R.id.tv_title);
        mText_name = (EditText) v.findViewById(R.id.tv_name);
        mText_goods = (EditText) v.findViewById(R.id.tv_goods);
        mTv_phone = (EditText) v.findViewById(R.id.tv_phone);
        mTv_address = (EditText) v.findViewById(R.id.tv_address);


        mTv_tille.setText(mTitle);
        mTv_tille.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager)getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                ClipData data = cm.getPrimaryClip();
                mText_goods.append(data.getItemAt(0).getText());
            }
        });

        mText_name.setText(name);
        mText_goods.setText(goods);
        mTv_phone.setText(phone);
        mTv_address.setText(address);

        mText_name.setFocusable(true);
        mText_name.requestFocus();

    }

    public boolean check()
    {

        if(mText_name.getText().length()==0)
        {
            Toast.makeText(getActivity(),"请输入姓名",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(mText_goods.getText().length()==0)
        {
            Toast.makeText(getActivity(),"请输入商品信息",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void show()
    {

    }
    public void setTitle(String title)
    {
        this.mTitle = title;
    }


}
