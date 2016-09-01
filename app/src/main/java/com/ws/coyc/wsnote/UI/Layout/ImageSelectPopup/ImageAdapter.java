package com.ws.coyc.wsnote.UI.Layout.ImageSelectPopup;


import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.ws.coyc.wsnote.R;


public class ImageAdapter extends BaseAdapter {

	private ArrayList<ImageInfoContentResolver> mImagePath;
	private LayoutInflater mInflater;
	private Context  mContext;
	
	public ImageAdapter(Context context,ArrayList<ImageInfoContentResolver> imagePath){
		
		mInflater = LayoutInflater.from(context);
		mContext = context;
		mImagePath = imagePath;		
	}
	public ImageAdapter(ArrayList<String> imagePath,Context context){
		
		mInflater = LayoutInflater.from(context);
		mContext = context;
		mImagePath = new ArrayList<ImageInfoContentResolver>();		
		for(int i = 0;i<imagePath.size();i++)
		{
			ImageInfoContentResolver t = new ImageInfoContentResolver();
			t.path = imagePath.get(i);
			mImagePath.add(t);
		}
	}
	
	
	@Override
	public int getCount() {
		return mImagePath.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mImagePath.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int position, View converview, ViewGroup group) {
		
		ViewHolder holder = null;
		if(converview == null)
		{
			converview = mInflater.inflate(R.layout.item_image_adapter, group, false);
			holder = new ViewHolder();
			holder.image = (ImageView) converview.findViewById(R.id.imageview_image);
			converview.setTag(holder);
			
		}else
		{
			holder = (ViewHolder) converview.getTag();
		}
//		holder.image.setImageResource(R.mipmap.time);
		
		ImageLoader.getInstance().loadImage(mImagePath.get(position).path, holder.image,4);

		return converview;
	}
	
	public class ViewHolder
	{
		public ImageView image;
	}

}
