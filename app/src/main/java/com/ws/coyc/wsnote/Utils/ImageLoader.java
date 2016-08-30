package com.ws.coyc.wsnote.Utils;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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
		int cacheSize = maxMemory /3;
		mLruCache = new LruCache<String, Bitmap>(cacheSize)
		{
			@Override
			protected int sizeOf(String key, Bitmap value)
			{
				return value.getRowBytes() * value.getHeight();
			};
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
//			ImgBeanHolder holder = new ImgBeanHolder();
//			holder.bitmap = bm;
//			holder.imageView = imageView;
//			holder.path = path;
//			Message message = Message.obtain();
//			message.obj = holder;
//			mHandler.sendMessage(message);
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
//					ImageSize imageSize = getImageViewWidth(imageView);
//					int reqWidth = imageSize.width;
//					int reqHeight = imageSize.height;
//					Bitmap bm = decodeSampledBitmapFromResource(path, reqWidth,reqHeight);
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
			System.out.println("鍙嶅皠鏈哄埗");
			width = getImageViewFieldValue(imageView, "mMaxWidth"); // Check
			// maxWidth
			// parameter
		}
			
		if (width <= 0)
		{
			System.out.println("寮哄埗鑾峰彇  灞忓箷瀹藉害銆傘�傘�傘�傘�傘�傘��");
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
//		options.inTempStorage = new byte[100 * 1024];
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(pathName, options);
		
		if(options.outWidth*options.outHeight<1*1024*1024)
		{
			scale = 1;
//			ooo.ooo("1M以下图片 不进行压缩");
		}else
		{
//			ooo.ooo("大于1M图片 进行压缩");
		}
		options.inSampleSize = scale;
		options.inJustDecodeBounds = false;
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeFile(pathName, options);
		} catch (OutOfMemoryError e) {
			
//			bitmap = BitmapFactory.decodeResource(App.getInstance().getResources(), R.drawable.fix);
			
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

}
