package com.ws.coyc.wsnote.UI.PopUp.SetTextPopup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

import com.ws.coyc.wsnote.R;


public class BgPopWind {

	private PopupWindow mPopup;
	private Context mContext;
	private View v;
	
	
	
	public BgPopWind(Context c)
	{
		mContext = c;
		initPopupWind();
	}
	public BgPopWind(Context c,boolean show)
	{
		mContext = c;
		if(show)
		{
			initPopupWind();
		}else
		{
			initPopupWindNotShow();
		}
	}
	

	/**
	 * ��ʼ��popupwind
	 */
	@SuppressLint("InflateParams") 
	private void initPopupWind()
	{
		initPopupWindNotShow();
		show();
	}
	private void initPopupWindNotShow()
	{
		View view = LayoutInflater.from(mContext).inflate(R.layout.popupbg, null);
		v = view;
		mPopup = new PopupWindow(view, 3000,5000, true);
		mPopup.setTouchable(true);
		mPopup.setBackgroundDrawable(view.getBackground());
		mPopup.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss() {
			}
		});
	}
	
	public void show()
	{
		mPopup.showAtLocation(v, Gravity.FILL ,0, 0);
	}
	
	public void dismiss()
	{
		mPopup.dismiss();
	}
	
}








