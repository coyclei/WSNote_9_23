package com.ws.coyc.wsnote.UI.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.polites.android.GestureImageView;
import com.ws.coyc.wsnote.R;
import com.ws.coyc.wsnote.Utils.ImageLoader;

public class ImageViewActivity extends Activity{

	
	private GestureImageView mImageview;
	private String mImagePath = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_imageview);
		
		initData();
		initView();
		
	}

	private void initData() {
		Intent intent = getIntent();
		mImagePath = intent.getStringExtra("path");
	}

	private void initView() {
		mImageview = (GestureImageView) findViewById(R.id.giv_view);
		ImageLoader.getInstance().loadImage(mImagePath, mImageview,1);
	}
	
}
