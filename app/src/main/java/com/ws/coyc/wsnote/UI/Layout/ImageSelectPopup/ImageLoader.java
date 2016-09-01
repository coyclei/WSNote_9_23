package com.ws.coyc.wsnote.UI.Layout.ImageSelectPopup;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.LruCache;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

public class ImageLoader
{
	private LruCache<String, Bitmap> mLruCache;
	private ExecutorService mThreadPool;
	private int mThreadCount = 1;
	private Type mType = Type.LIFO;
	private LinkedList<Runnable> mTasks;
	private Thread mPoolThread;
	private Handler mPoolThreadHander;

	private Handler mHandler;

	private volatile Semaphore mSemaphore = new Semaphore(0);

	private volatile Semaphore mPoolSemaphore;

	private static ImageLoader mInstance;

	public enum Type
	{
		FIFO, LIFO
	}


	public static ImageLoader getInstance()
	{

		if (mInstance == null)
		{
			synchronized (ImageLoader.class)
			{
				if (mInstance == null)
				{
					mInstance = new ImageLoader(1, Type.LIFO);
				}
			}
		}
		return mInstance;
	}

	private ImageLoader(int threadCount, Type type)
	{
		init(threadCount, type);
	}

	private void init(int threadCount, Type type)
	{
		// loop thread
		mPoolThread = new Thread()
		{
			@Override
			public void run()
			{
				Looper.prepare();

				mPoolThreadHander = new Handler()
				{
					@Override
					public void handleMessage(Message msg)
					{
						mThreadPool.execute(getTask());
						try
						{
							mPoolSemaphore.acquire();
						} catch (InterruptedException e)
						{
						}
					}
				};
				mSemaphore.release();
				Looper.loop();
			}
		};
		mPoolThread.start();

		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int cacheSize = maxMemory /10;
		mLruCache = new LruCache<String, Bitmap>(cacheSize)
		{
			@Override
			protected int sizeOf(String key, Bitmap value)
			{
				return value.getRowBytes() * value.getHeight();
			}
		};

		mThreadPool = Executors.newFixedThreadPool(threadCount);
		mPoolSemaphore = new Semaphore(threadCount);
		mTasks = new LinkedList<Runnable>();
		mType = type == null ? Type.LIFO : type;

	}
	public void loadImage(final String path, final ImageView imageView)
	{
		loadImage(path,imageView,16);
	}
	
	@SuppressLint("HandlerLeak") 
	public void loadImage(final String path, final ImageView imageView,final int scale)
	{
		imageView.setTag(path);
		if (mHandler == null)
		{
			mHandler = new Handler()
			{
				@Override
				public void handleMessage(Message msg)
				{
					ImgBeanHolder holder = (ImgBeanHolder) msg.obj;
					ImageView imageView = holder.imageView;
					Bitmap bm = holder.bitmap;
					String path = holder.path;
					if (imageView.getTag().toString().equals(path))
					{
						if(bm != null)
						{
							imageView.setImageBitmap(bm);
						}
					}
				}
			};
		}

		Bitmap bm = getBitmapFromLruCache(path);
		if (bm != null)
		{
			if (imageView.getTag().toString().equals(path))
			{
				imageView.setImageBitmap(bm);
			}
		} else
		{
			addTask(new Runnable()
			{
				@Override
				public void run()
				{
					Bitmap bm = decodeSampledBitmapFromResource(path, scale);
					
					addBitmapToLruCache(path, bm);
					ImgBeanHolder holder = new ImgBeanHolder();
					holder.bitmap = getBitmapFromLruCache(path);
					holder.imageView = imageView;
					holder.path = path;
					Message message = Message.obtain();
					message.obj = holder;
					mHandler.sendMessage(message);
					mPoolSemaphore.release();
				}
			});
		}

	}


	/**
	 * 濞ｈ濮炴稉锟芥稉顏冩崲閸旓拷
	 * 
	 * @param runnable
	 */
	private synchronized void addTask(Runnable runnable)
	{
		try
		{
			if (mPoolThreadHander == null)
				mSemaphore.acquire();
		} catch (InterruptedException e)
		{
		}
		mTasks.add(runnable);
		
		mPoolThreadHander.sendEmptyMessage(0x110);
	}

	/**
	 * 閸欐牕鍤稉锟芥稉顏冩崲閸旓拷
	 * 
	 * @return
	 */
	private synchronized Runnable getTask()
	{
		if (mType == Type.FIFO)
		{
			return mTasks.removeFirst();
		} else if (mType == Type.LIFO)
		{
			return mTasks.removeLast();
		}
		return null;
	}
	
	/**
	 * 閸楁洑绶ラ懢宄扮繁鐠囥儱鐤勬笟瀣嚠鐠烇拷
	 * 
	 * @return
	 */
	public static ImageLoader getInstance(int threadCount, Type type)
	{

		if (mInstance == null)
		{
			synchronized (ImageLoader.class)
			{
				if (mInstance == null)
				{
					mInstance = new ImageLoader(threadCount, type);
				}
			}
		}
		return mInstance;
	}


	private ImageSize getImageViewWidth(ImageView imageView)
	{
		ImageSize imageSize = new ImageSize();
		final DisplayMetrics displayMetrics = imageView.getContext()
				.getResources().getDisplayMetrics();
		final LayoutParams params = imageView.getLayoutParams();

		int width = params.width == LayoutParams.WRAP_CONTENT ? 0 : imageView
				.getWidth(); // Get actual image width
		if (width <= 0)
			width = params.width; // Get layout width parameter
		if (width <= 0)
		{
			width = getImageViewFieldValue(imageView, "mMaxWidth"); // Check
		}
			
		if (width <= 0)
		{
			width = displayMetrics.widthPixels/3;
		}
			
		
		
		int height = params.height == LayoutParams.WRAP_CONTENT ? 0 : imageView
				.getHeight(); // Get actual image height
		if (height <= 0)
			height = params.height; // Get layout height parameter
		if (height <= 0)
			height = getImageViewFieldValue(imageView, "mMaxHeight"); // Check
																		// maxHeight
																		// parameter
		if (height <= 0)
			height = displayMetrics.heightPixels/5;
		imageSize.width = width;
		imageSize.height = height;
		return imageSize;

	}
	
	
	private Bitmap getBitmapFromLruCache(String key)
	{
		return mLruCache.get(key);
	}

	private void addBitmapToLruCache(String key, Bitmap bitmap)
	{
		if (getBitmapFromLruCache(key) == null)
		{
			if (bitmap != null)
				mLruCache.put(key, bitmap);
		}
	}

	private int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight)
	{
		int width = options.outWidth;
		int height = options.outHeight;
		int inSampleSize = 1;

		if (width > reqWidth && height > reqHeight)
		{
			int widthRatio = Math.round((float) width / (float) reqWidth);
			int heightRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = Math.max(widthRatio, heightRatio);
		}
		return inSampleSize;
	}

	private Bitmap decodeSampledBitmapFromResource(String pathName,
			int reqWidth, int reqHeight)
	{
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(pathName, options);
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);
		options.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeFile(pathName, options);

		return bitmap;
	}

	private Bitmap decodeSampledBitmapFromResource(String pathName,int scale)
	{
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(pathName, options);

		if(options.outWidth*options.outHeight<1*1024*1024)
		{
			scale = 1;
		}
		options.inSampleSize = scale;
		options.inJustDecodeBounds = false;
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeFile(pathName, options);
		} catch (OutOfMemoryError e) {
		}
		return bitmap;
	}

	private class ImgBeanHolder
	{
		Bitmap bitmap;
		ImageView imageView;
		String path;
	}

	private class ImageSize
	{
		int width;
		int height;
	}

	private static int getImageViewFieldValue(Object object, String fieldName)
	{
		int value = 0;
		try
		{
			Field field = ImageView.class.getDeclaredField(fieldName);
			field.setAccessible(true);
			int fieldValue = (Integer) field.get(object);
			if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE)
			{
				value = fieldValue;

				Log.e("TAG", value + "");
			}
		} catch (Exception e)
		{
		}
		return value;
	}

	public static ArrayList<ImageInfoContentResolver> GetSysImageAll(Context mContext) {

		ArrayList<ImageInfoContentResolver> result = new ArrayList<ImageInfoContentResolver>();
		ContentResolver cr = mContext.getContentResolver();

		String select = MediaStore.Images.Media.MIME_TYPE + " = ? or "
				+ MediaStore.Images.Media.MIME_TYPE + " = ? ";

		String[] selects = new String[] { "image/jpeg", "image/png" };
		Cursor cursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				null, select, selects, "");

		cursor.moveToFirst();
		while (cursor.moveToNext()) {

			ImageInfoContentResolver temp = new ImageInfoContentResolver();
			temp.path = cursor.getString(cursor
					.getColumnIndex(MediaStore.Images.Media.DATA));
			temp.date = cursor.getString(cursor
					.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN));
			result.add(temp);
		}
		cursor.close();
		ComparatorImageDateAll comparator = new ComparatorImageDateAll();
		try {
			Collections.sort(result, comparator);
		} catch (NullPointerException e) {
		}
		return result;
	}

	public static Bitmap GetRoundedCornerBitmap(Bitmap bitmap) {
	        try {
	            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
	                    bitmap.getHeight(), Bitmap.Config.ARGB_8888);
	            Canvas canvas = new Canvas(output);
	            final Paint paint = new Paint();
	            final Rect rect = new Rect(0, 0, bitmap.getWidth(),
	                    bitmap.getHeight());
	            final RectF rectF = new RectF(new Rect(0, 0, bitmap.getWidth(),
	                    bitmap.getHeight()));
	            final float roundPx = 14;
	            paint.setAntiAlias(true);
	            canvas.drawARGB(0, 0, 0, 0);
	            paint.setColor(Color.BLACK);
	            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
	            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

	            final Rect src = new Rect(0, 0, bitmap.getWidth(),
	                    bitmap.getHeight());

	            canvas.drawBitmap(bitmap, src, rect, paint);
	            return output;
	        } catch (Exception e) {
	            return bitmap;
	        }
	    }


	/**
	 * 获取网落图片资源
	 * @param url
	 * @return
	 */
	public static Bitmap getHttpBitmap(String url){
		URL myFileURL;
		Bitmap bitmap=null;
		try{
			myFileURL = new URL(url);
			//获得连接
			HttpURLConnection conn=(HttpURLConnection)myFileURL.openConnection();
			//设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
			conn.setConnectTimeout(6000);
			//连接设置获得数据流
			conn.setDoInput(true);
			//不使用缓存
			conn.setUseCaches(false);
			//这句可有可无，没有影响
			//conn.connect();
			//得到数据流
			InputStream is = conn.getInputStream();
			//解析得到图片
			bitmap = BitmapFactory.decodeStream(is);
			//关闭数据流
			is.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return bitmap;
	}
}
