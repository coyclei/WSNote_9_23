package com.ws.coyc.wsnote.UI.PopUp.SetTextPopup;


public interface OnShow2Interface {

	void onChange(String name, String text, String prise_in, boolean isfh, boolean isfk,String phone,String address,String image_path);
	void onSend(String name, String text, String prise_in,String phone,String address,String image_path);
	void onDelete();
}
