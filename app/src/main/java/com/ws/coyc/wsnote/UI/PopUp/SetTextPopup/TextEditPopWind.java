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

public class TextEditPopWind extends PopUpWindFather{

	private Button mDisBt;
	private Button mOkBt;

	private OnDescribeInterface describeInterface;


	/**
	 *
	 * @param c
	 * @param text
	 * @param tag
     * @param h
     */
	public TextEditPopWind(Activity c, String text, String tag, int h
			, boolean show, OnDescribeInterface s,String phone,String address,String path)
	{
		mContext = c;
		this.mTitle = tag;
		mWindhight = h;
		describeInterface = s ;
		this.phone = phone;
		this.address = address;
		this.image_path = path;
		v = LayoutInflater.from(mContext).inflate(R.layout.popupdescriberecord, null);
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

				if(!TextEditPopWind.super.check())
				{
					return;
				}

				describeInterface.onDescribed(mText_name.getText().toString(), mText_goods.getText().toString()
						,mTv_phone.getText().toString(),mTv_address.getText().toString(),image_path);

				mPopup.dismiss();
			}
		});
	}

	@SuppressLint("InflateParams")
	public void initPopupWind(boolean show)
	{
		super.initPopupWind(show);
	}





	public void setOnDescribeListener(OnDescribeInterface s)
	{
		describeInterface = s;
	}


	
}








