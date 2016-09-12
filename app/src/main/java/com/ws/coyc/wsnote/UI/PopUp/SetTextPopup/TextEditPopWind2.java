package com.ws.coyc.wsnote.UI.PopUp.SetTextPopup;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.ws.coyc.wsnote.R;
import com.ws.coyc.wsnote.Utils.Calc;

public class TextEditPopWind2 extends PopUpWindFather{

	private Button mDisBt;
	private Button mOkBt;

	private EditText mText_prise;
	private CheckBox isFH;
	private CheckBox isFK;

	private OnDescribeInterface2 describeInterface;

	/**
	 *
	 * @param c
	 * @param goods
	 * @param tag
     * @param h
     */
	public TextEditPopWind2(Activity c, String name, String goods, String tag, int h
			, boolean show, OnDescribeInterface2 s,String phone,String address,String path)
	{
		mContext = c;
		this.mTitle = tag;
		this.name = name;
		this.goods = goods;
		mWindhight = h;
		this.phone = phone;
		this.address = address;
		this.image_path = path;
		describeInterface = s ;

		v = LayoutInflater.from(mContext).inflate(R.layout.popupdescriberecord2, null);

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
		mText_prise = (EditText) v.findViewById(R.id.tv_text_money_in);

		isFH = (CheckBox) v.findViewById(R.id.cb_fh);
		isFK = (CheckBox) v.findViewById(R.id.cb_fk);


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

				if (!TextEditPopWind2.super.check())
				{
					return;
				}
				if(mText_prise.getText().length()==0)
				{
					Toast.makeText(mContext,"请输入进货价",Toast.LENGTH_SHORT).show();
					return;
				}

				describeInterface.onDescribed(mText_name.getText().toString()
						, mText_goods.getText().toString(), Calc.calc(mText_prise.getText().toString()).toString()
						,isFH.isChecked(),isFK.isChecked(),mTv_phone.getText().toString()
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
	

	public void setOnDescribeListener(OnDescribeInterface2 s)
	{
		describeInterface = s;
	}


	
}








