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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;
import com.ws.coyc.wsnote.R;
import com.ws.coyc.wsnote.Utils.Calc;


public class Show2Wind extends PopUpWindFather{


	private Button mBt_onChange;
	private Button mOkBt;
	private Button mBt_delete;
	private EditText mText_prise;
	private CheckBox isFH;
	private CheckBox isFK;

	private OnShow2Interface describeInterface;
	private String prise_in = "";
	private boolean mIsfh = false;
	private boolean mIsfk = false;


	/**
	 *
	 * @param c
	 * @param goods
	 * @param tag
     * @param h
     */
	public Show2Wind(Activity c, String name, String goods,
					 String prise_in, boolean isFk, boolean isFh,
					 String tag, int h, boolean show, OnShow2Interface s
			,String phone,String address,String path)
	{
		mContext = c;
		this.mTitle = tag;
		this.goods = goods;
		this.name = name;
		this.mIsfh = isFh;
		this.mIsfk = isFk;
		this.prise_in = prise_in;
		this.phone = phone;
		this.address = address;
		this.image_path = path;
		mWindhight = h;
		describeInterface = s ;

		v = LayoutInflater.from(mContext).inflate(R.layout.popupshow2, null);

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
		mText_prise = (EditText) v.findViewById(R.id.tv_text_prise);
		mText_prise.setText(prise_in);

		isFH = (CheckBox) v.findViewById(R.id.cb_fh);
		isFK = (CheckBox) v.findViewById(R.id.cb_fk);

		if(mIsfk)
		{
			isFK.setChecked(true);
		}else
		{
			isFK.setChecked(false);
		}

		if(mIsfh)
		{
			isFH.setChecked(true);
		}else
		{
			isFH.setChecked(false);
		}



		mBt_onChange = (Button) v.findViewById(R.id.bt_dis);
		mBt_onChange.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(!check())
				{
					return;
				}
				describeInterface.onChange(mText_name.getText().toString()
						, mText_goods.getText().toString()
						, Calc.calc(mText_prise.getText().toString()).toString()
						,isFH.isChecked(),isFK.isChecked()
						,mTv_phone.getText().toString()
						,mTv_address.getText().toString()
						,image_path);
				mPopup.dismiss();
			}
		});
		mOkBt = (Button) v.findViewById(R.id.bt_ok);
		mOkBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(!check())
				{
					return;
				}
				describeInterface.onSend(mText_name.getText().toString()
						,mText_goods.getText().toString()
						,Calc.calc(mText_prise.getText().toString()).toString()
						,mTv_phone.getText().toString()
						,mTv_address.getText().toString()
						,image_path
				);

				mPopup.dismiss();
			}
		});

		mBt_delete = (Button) v.findViewById(R.id.bt_delete);
		mBt_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				describeInterface.onDelete();
				mPopup.dismiss();
			}
		});
	}

	@SuppressLint("InflateParams")
	public void initPopupWind(boolean show)
	{
		super.initPopupWind(show);
	}
	

	public void setOnDescribeListener(OnShow2Interface s)
	{
		describeInterface = s;
	}

	public boolean check()
	{
		if(!super.check())
		{
			return false;
		}
		if(mText_prise.getText().length()==0)
		{
			Toast.makeText(mContext,"请输入进货价",Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	
}








