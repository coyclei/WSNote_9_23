package com.ws.coyc.wsnote.UI.Layout.ImageSelectPopup;


import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

import com.ws.coyc.wsnote.R;


public class ImageSelectPopWind {

	private PopupWindow mPopup;
	
	private Context mContext;
	
	private GridView mGridView;
	private ImageAdapter mImageAdapter;
	
	private Button mDisBt;

	private ArrayList<ImageInfoContentResolver> mImages = new ArrayList<ImageInfoContentResolver>();

	private String mSelectImagePath;
	private OnImageSelectedInterface imageSelectedInterface;
	
	private View v;
	public ImageSelectPopWind(Context c,OnImageSelectedInterface s)
	{
		mContext = c;
		imageSelectedInterface = s;
		initData();
		initPopupWind();
	}
	
	private void initData() {
		mImages = ImageLoader.GetSysImageAll(mContext);
	}

	/**
	 * ��ʼ��popupwind
	 */
	@SuppressLint("InflateParams") 
	private void initPopupWind()
	{

		View view = LayoutInflater.from(mContext).inflate(R.layout.popupimageselect, null);
		v = view;
		mGridView = (GridView) view.findViewById(R.id.gv_images);
		initGridView();
		
		mDisBt = (Button) view.findViewById(R.id.bt_dis);
		mDisBt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				mPopup.dismiss();
			}
		});

		mPopup = new PopupWindow(view, LayoutParams.MATCH_PARENT, 1000, true);
		mPopup.setTouchable(true);
		mPopup.setBackgroundDrawable(view.getBackground());
		mPopup.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss() {
			Log.i("coyc","popupwind ondismiss");
			}
		});
	}
	
	public void show()
	{
		mPopup.showAtLocation(v, Gravity.BOTTOM, 0, 0);
	}

	private void initGridView() {
		
		mImageAdapter = new ImageAdapter(mContext, mImages);
		mGridView.setAdapter(mImageAdapter);
		
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
					imageSelectedInterface.onImageSelected(mImages.get(arg2).path);
					mPopup.dismiss();
				}
		});
	}
	
	public void initImageSelectListener(OnImageSelectedInterface s)
	{
		imageSelectedInterface = s;
	}
	
}








