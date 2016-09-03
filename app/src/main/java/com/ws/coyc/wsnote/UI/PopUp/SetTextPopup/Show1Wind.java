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
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

import com.ws.coyc.wsnote.R;


public class Show1Wind extends  PopUpWindFather{

	private Button mBt_ok_change;
	private Button mBt_ok_buy;
	private Button mBt_ok_send;
	private ImageButton mBt_delete;
	private OnShow1Interface anInterface;

	/**
	 *
	 * @param c
	 * @param goods
	 * @param tag
     * @param h
     */
	public Show1Wind(Activity c, String name, String goods, String phone
			, String address, String tag, int h, boolean show, OnShow1Interface s
			,String path)
	{
		mContext = c;
		this.mTitle = tag;
		this.name = name;
		this.goods = goods;
		this.phone = phone;
		this.address = address;
		this.phone = phone;
		this.address = address;
		this.image_path = path;
		mWindhight = h;
		anInterface = s ;

		v = LayoutInflater.from(mContext).inflate(R.layout.popupshow1, null);

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
		mBt_ok_change = (Button) v.findViewById(R.id.bt_ok_change);
		mBt_ok_buy = (Button) v.findViewById(R.id.bt_ok_buy);
		mBt_ok_send = (Button) v.findViewById(R.id.bt_ok_send);
		mBt_delete = (ImageButton) v.findViewById(R.id.bt_delete);

		mBt_ok_change.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(!check())
				{
					return;
				}
				anInterface.onChange(mText_name.getText().toString(), mText_goods.getText().toString()
						,mTv_phone.getText().toString(),mTv_address.getText().toString(),image_path);
				mPopup.dismiss();
			}
		});

		mBt_ok_buy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(!check())
				{
					return;
				}
				anInterface.onBuy(mText_name.getText().toString(), mText_goods.getText().toString()
						,mTv_phone.getText().toString(),mTv_address.getText().toString(),image_path);

				mPopup.dismiss();
			}
		});

		mBt_ok_send.setOnClickListener(new OnClickListener() {


			@Override
			public void onClick(View arg0) {
				if(!check())
				{
					return;
				}
				anInterface.onSend(mText_name.getText().toString(), mText_goods.getText().toString()
						,mTv_phone.getText().toString(),mTv_address.getText().toString(),image_path);
				mPopup.dismiss();
			}
		});

		mBt_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Toast.makeText(mContext,"长按删除",Toast.LENGTH_SHORT).show();
			}
		});
		mBt_delete.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View view) {

				anInterface.onDelete();
				mPopup.dismiss();
				return true;
			}
		});

	}

	@SuppressLint("InflateParams")
	public void initPopupWind(boolean show)
	{
		super.initPopupWind(show);

	}
	

	public void setOnDescribeListener(OnShow1Interface s)
	{
		anInterface = s;
	}


	public boolean check()
	{
		return super.check();
	}
	
}








