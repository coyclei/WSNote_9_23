package com.ws.coyc.wsnote.UI.PopUp.SetTextPopup;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

import com.ws.coyc.wsnote.R;
import com.ws.coyc.wsnote.Utils.Calc;


public class TextEditPopWind3 extends PopUpWindFather{

	private Button mDisBt;
	private Button mOkBt;


	private EditText mText_in;
	private EditText mText_out;

	private OnDescribeInterface3 describeInterface;
	private String in_prise = "";

	/**
	 *
	 * @param c
	 * @param goods
	 * @param tag
     * @param h
     */
	public TextEditPopWind3(Activity c, String name, String goods, String in_prise , String tag, int h, boolean show, OnDescribeInterface3 s,String phone,String address,String path)
	{
		mContext = c;
		this.mTitle = tag;
		this.goods = goods;
		this.name = name;
		this.in_prise = in_prise;
		this.phone = phone;
		this.address = address;
		this.image_path = path;
		mWindhight = h;
		describeInterface = s ;

		v = LayoutInflater.from(mContext).inflate(R.layout.popupdescriberecord3, null);

		initData();
		initView();
		initPopupWind(show);
	}


	public void initData() {
		super.initData();
	}

	public void initView()
	{
		super.initView();
		mText_in = (EditText) v.findViewById(R.id.tv_text_prise);
		mText_out = (EditText) v.findViewById(R.id.tv_text_prise_out);

		mText_in.setText(in_prise);

		mDisBt = (Button) v.findViewById(R.id.bt_dis);
		mDisBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mPopup.dismiss();
			}
		});
		mOkBt = (Button) v.findViewById(R.id.bt_ok);
		mOkBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if(!TextEditPopWind3.super.check())
				{
					return;
				}

				if(mText_in.getText().length()==0)
				{
					Toast.makeText(mContext,"请输入进货价",Toast.LENGTH_SHORT).show();
					return;
				}
				if(mText_out.getText().length()==0)
				{
					Toast.makeText(mContext,"请输入出货价",Toast.LENGTH_SHORT).show();
					return;
				}

				describeInterface.onDescribed(mText_name.getText().toString()
						, mText_goods.getText().toString()
						, Calc.calc(mText_in.getText().toString()).toString()
						,Calc.calc(mText_out.getText().toString()).toString()
						,mTv_phone.getText().toString()
						,mTv_address.getText().toString()
						,image_path);

				mPopup.dismiss();
			}
		});
	}

	@SuppressLint("InflateParams")
	public void initPopupWind(boolean show)
	{
		super.initPopupWind(show);
	}
	

	public void setOnDescribeListener(OnDescribeInterface3 s)
	{
		describeInterface = s;
	}


	
}







