package com.ws.coyc.wsnote.UI.PopUp.SetTextPopup;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ws.coyc.wsnote.R;
import com.ws.coyc.wsnote.Utils.Calc;


public class Show3Wind extends PopUpWindFather{

	private Button mOkBt;
	private ImageButton mBt_delete;


	private EditText mText_in;
	private EditText mText_out;

	private OnShow3Interface describeInterface;
	private String out_prise = "";
	private String in_prise = "";

	/**
	 *
	 * @param c
	 * @param goods
	 * @param tag
     * @param h
     */
	public Show3Wind(Activity c, String name, String goods, String in_prise
			, String out_prise , String tag, int h, boolean show, OnShow3Interface s
			,String phone,String address,String path)
	{
		mContext = c;
		this.mTitle = tag;
		this.goods = goods;
		this.name = name;
		this.in_prise = in_prise;
		this.out_prise = out_prise;
		this.phone = phone;
		this.address = address;
		this.image_path = path;
		mWindhight = h;
		describeInterface = s ;

		v = LayoutInflater.from(mContext).inflate(R.layout.popupshow3, null);

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
		mText_in = (EditText) v.findViewById(R.id.tv_text_money_in);
		mText_out = (EditText) v.findViewById(R.id.tv_text_money_out);

		mText_in.setText(in_prise);
		mText_out.setText(out_prise);

		mBt_delete = (ImageButton) v.findViewById(R.id.bt_delete);
		mBt_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Toast.makeText(mContext,"长按删除",Toast.LENGTH_SHORT).show();
			}
		});

		mBt_delete.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View view) {

				describeInterface.onDelete();
				mPopup.dismiss();
				return true;
			}
		});


		mOkBt = (Button) v.findViewById(R.id.bt_ok);
		mOkBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if(!Show3Wind.super.check())
				{
					return;
				}
				if(mText_in.getText().length()==0)
				{
					Toast.makeText(mContext,"请输入进货价",Toast.LENGTH_SHORT).show();
					return ;
				}
				if(mText_out.getText().length()==0)
				{
					Toast.makeText(mContext,"请输入出货价",Toast.LENGTH_SHORT).show();
					return ;
				}

				describeInterface.onChange(mText_name.getText().toString()
						, mText_goods.getText().toString(),Calc.calc(mText_in.getText().toString()).toString()
						, Calc.calc(mText_out.getText().toString()).toString()
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
	

	public void setOnDescribeListener(OnShow3Interface s)
	{
		describeInterface = s;
	}


	
}








