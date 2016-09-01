package com.ws.coyc.wsnote.UI.Layout.ImageSelectPopup;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
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
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.location.Location;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.util.LruCache;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;


/**
 * ����ͼƬ������ �ܼ���ϵͳ��Ƭ
 *
 *
 * ����ͼƬ�����־����������� �������ģʽ���ڷ���ģʽ����������£��û������������Ƭ��Ƶ�������ϴ����Ŷ���ȥ���رշ���ģʽ�򲻡�����֪ͨ���û���
 * ����Ҫ������ģʽ�Ŀ��ؽӿں������� �û��ҵ����Ա��û��ܷܺ���Ŀ������߹رշ���ģʽ
 * �����û�һֱû�п�������ģʽ����ôӦ����Ҳ���и��ر�ķ���ӿڡ���������ض��ķ���ӿں���ѡ����������ͼƬ���з���
 * ��Ȼ�û���Ȩ��ɾ���Լ���������������Ƭ������Ƶ
 *
 * @author leipe
 */
public class ImageManager {
	private Context mContext;

	public ImageManager(Context context) {
		mContext = context;
	}

	/**
	 * ͼƬ����
	 */
	private LruCache<String, Bitmap> mLruCache;

	/**
	 * �̳߳�
	 */
	private ExecutorService mThreadPool;

	public static final int DEFULT_THREAD_COUNT = 1;

	public enum Type {
		FIFO, LIFO;
	}

	private Type mType = Type.LIFO;

	private LinkedList<Runnable> mTaskQueue;
	/**
	 * ��ѵ�߳�
	 */
	private Thread mPoolThread;
	/**
	 * ��ѵ�̵߳�handler
	 */
	private Handler mPoolThreadHandler;

	private Handler mUIHandler;

	private volatile Semaphore mSemaphorePoolThreadHandler = new Semaphore(0);

	private volatile Semaphore mSemaphorePoolThread;



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

	public Bitmap OpitionImage(ImageView view, String path) {
		ReqImageSize reqImageSize = GetReqImageSize(view);

		Options op = new Options();
		op.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, op);
		int optScale = GetOptScale(op, reqImageSize);
		op.inSampleSize = optScale;
		op.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(path, op);
	}

	private int GetOptScale(Options op, ReqImageSize reqImageSize) {
		int widthS = 0;
		int hightS = 0;
		if (op.outWidth > reqImageSize.width
				|| op.outHeight > reqImageSize.higth) {
			widthS = Math.round(op.outWidth / reqImageSize.width);
			hightS = Math.round(op.outHeight / reqImageSize.higth);
		}

		return Math.max(widthS, hightS);
	}

	@SuppressLint("NewApi")
	private ReqImageSize GetReqImageSize(ImageView view) {

		ReqImageSize result = new ReqImageSize();

		LayoutParams lp = (LayoutParams) view.getLayoutParams();

		int width = view.getWidth();// ��ȡʵ�ʿ��
		if (width <= 0) {
			width = lp.width; // ��ȡXML�е�Ԥ��ֵ
		}
		if (width <= 0) {
			width = view.getMaxWidth();
		}
		if (width <= 0) {
			width = view.getContext().getResources().getDisplayMetrics().widthPixels;
		}

		int hight = view.getHeight();// ��ȡʵ�ʿ��
		if (hight <= 0) {
			hight = lp.height; // ��ȡXML�е�Ԥ��ֵ
		}
		if (hight <= 0) {
			hight = view.getMaxHeight();
		}
		if (hight <= 0) {
			hight = view.getContext().getResources().getDisplayMetrics().heightPixels;
		}
		result.width = width;
		result.higth = hight;

		return result;
	}

	private class ReqImageSize {
		int width;
		int higth;
	}

	@SuppressLint("NewApi")
	public void init(int threadCount, Type type) {
		// ��̨��ѵ�߳�
		mPoolThread = new Thread() {

			@SuppressLint("HandlerLeak")
			@Override
			public void run() {

				Looper.prepare();
				mPoolThreadHandler = new Handler() {
					@Override
					public void handleMessage(Message msg) {

						// �̳߳���ȡ��һ���߳�ȥִ��
						mThreadPool.execute(getTask());
						try {
							mSemaphorePoolThread.acquire();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

					}

				};
				// �ͷ��ź���
				mSemaphorePoolThreadHandler.release();
				Looper.loop();

			}
		};

		mPoolThread.start();

		int maxMemory = (int) Runtime.getRuntime().maxMemory();

		int cacheMenmory = maxMemory / 8;

		mLruCache = new LruCache<String, Bitmap>(cacheMenmory) {
			protected int sizeOf(String key, Bitmap value) {

				return value.getRowBytes() * value.getHeight();
			}
		};
		// �����̳߳�
		mThreadPool = Executors.newFixedThreadPool(threadCount);
		mSemaphorePoolThread = new Semaphore(threadCount);
		mTaskQueue = new LinkedList<Runnable>();

		mType = type == null ? Type.LIFO : type;

	}

	private Runnable getTask() {

		if (mType == Type.FIFO) {
			return mTaskQueue.removeFirst();
		} else {
			return mTaskQueue.removeLast();
		}
	}

	@SuppressLint("HandlerLeak")
	public void LoadImage(final String path, final ImageView imageView) {
		System.out.println("LoadImag........");
		imageView.setTag(path);

		if (mUIHandler == null) {
			mUIHandler = new Handler() {
				@SuppressLint("HandlerLeak")
				public void handleMessage(Message msg) {
					System.out.println("handleMessage UI Handler........");
					// ��ȡ�õ���ͼƬ��ΪimageView�ص�����bitmap
					ImageBeanHolder holder = (ImageBeanHolder) msg.obj;
					Bitmap bm = holder.bitmap;
					ImageView im = holder.imageView;
					final String path = holder.path;
					if (im.getTag().toString().equals(path)) {
						System.out.println("im.setImageBitmap(bm);........");
						im.setImageBitmap(bm);
					}
				}

			};
		}
		System.out.println("getBitmapFromLruCache");
		Bitmap bm = getBitmapFromLruCache(path);

		if (bm != null) {
			System.out.println("getBitmapFromLruCache  not  null");
			RefreashBitmap(imageView, path, bm);

		} else {
			addTasks(new Runnable() {

				@Override
				public void run() {
					System.out.println("OpitionImage........");
					// ����ͼƬ
					// ͼƬ��ѹ��
					Bitmap bm = OpitionImage(imageView, path);

					// ��ͼƬ���뵽����
					addBitMapToLruCache(bm, path);

					RefreashBitmap(imageView, path, bm);

					mSemaphorePoolThread.release();
				}

			});
		}
		System.out.println("getBitmapFromLruCache  is null  ");

	}

	private synchronized void addTasks(Runnable runnable) {

		mTaskQueue.add(runnable);
		try {
			if (mPoolThreadHandler == null)
				mSemaphorePoolThreadHandler.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		mPoolThreadHandler.sendEmptyMessage(1);

	};

	private void RefreashBitmap(final ImageView imageView, final String path,
			Bitmap bm) {

		ImageBeanHolder holder = new ImageBeanHolder();
		holder.bitmap = getBitmapFromLruCache(path);
		holder.imageView = imageView;
		holder.path = path;
		Message msg = Message.obtain();
		msg.obj = holder;
		mUIHandler.sendMessage(msg);
	}

	private void addBitMapToLruCache(Bitmap bm, String path) {
		if (getBitmapFromLruCache(path) == null) {
			if (bm != null) {

				System.out.println("mLruCache.put(path, bm);");
				mLruCache.put(path, bm);
			}
		}
	}

	/**
	 * ���� path�ڻ����л�ȡbitmap����
	 *
	 * @param path
	 * @return
	 */

	private Bitmap getBitmapFromLruCache(String path) {
		return mLruCache.get(path);
	}

	private class ImageBeanHolder {
		Bitmap bitmap;
		ImageView imageView;
		String path;
	}


	public static Bitmap convertToBitmap(String path, int w, int h) {
        Options opts = new Options();
        // ����Ϊtureֻ��ȡͼƬ��С
        opts.inJustDecodeBounds = true;
        opts.inPreferredConfig = Config.ARGB_8888;
        // ����Ϊ��
        try {
        	BitmapFactory.decodeFile(path, opts);
		} catch (IllegalStateException e) {
		}catch (Exception e) {
		}

        int width = opts.outWidth;
        int height = opts.outHeight;


        float scaleWidth = 0.f, scaleHeight = 0.f;
        if (width > w || height > h) {
            // ����
            scaleWidth = ((float) width) / w;
            scaleHeight = ((float) height) / h;
        }
        opts.inJustDecodeBounds = false;
        float scale = Math.max(scaleWidth, scaleHeight);
        opts.inSampleSize = (int)scale;
        WeakReference<Bitmap> weak = null ;
        try {
        	weak = new WeakReference<Bitmap>(BitmapFactory.decodeFile(path, opts));
        	return Bitmap.createScaledBitmap(weak.get(), w, h, true);
		} catch (IllegalStateException e) {
			// TODO: handle exception
		}catch (NullPointerException e) {
			// TODO: handle exception
		}
        catch (Exception e) {
			// TODO: handle exception
		}
        Bitmap bt = null;
        try {

		} catch (NullPointerException e) {
			// TODO: handle exception
			 bt = Bitmap.createScaledBitmap(weak.get(), w, h, true);
		}

        return bt;
    }
	/**
	 *
	 * @param path
	 * @param Sacle ѹ������
	 * @return
	 */
	public static Bitmap convertToBitmapByScale(String path,int Sacle) {

        Options opts = new Options();
        opts.inJustDecodeBounds = true;
        opts.inPreferredConfig = Config.ARGB_8888;//һ������ռ��4���ֽ�
        BitmapFactory.decodeFile(path, opts);
        int width = opts.outWidth;
        int height = opts.outHeight;
        if(width*height<3*1024*1024)
        {
        	Sacle = 1;
        }
        opts.inJustDecodeBounds = false;
        opts.inSampleSize = Sacle;

        return BitmapFactory.decodeFile(path, opts);

    }





	 public static Bitmap loadBitmap(String imgpath) {
	        return BitmapFactory.decodeFile(imgpath);
	 }

	 /** �Ӹ�����·������ͼƬ����ָ���Ƿ��Զ���ת����*/
	    public static Bitmap loadBitmap(String imgpath, boolean adjustOritation) {
	        if (!adjustOritation) {
	            return loadBitmap(imgpath);
	        } else {
	            Bitmap bm = loadBitmap(imgpath);
	            int digree = 0;
	            ExifInterface exif = null;
	            try {
	                exif = new ExifInterface(imgpath);
	            } catch (IOException e) {
	                e.printStackTrace();
	                exif = null;
	            }

	            if (exif != null) {
	                // ��ȡͼƬ�����������Ϣ
	                int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
	                        ExifInterface.ORIENTATION_UNDEFINED);
	                // ������ת�Ƕ�
	                switch (ori) {
	                case ExifInterface.ORIENTATION_ROTATE_90:
	                    digree = 90;
	                    break;
	                case ExifInterface.ORIENTATION_ROTATE_180:
	                    digree = 180;
	                    break;
	                case ExifInterface.ORIENTATION_ROTATE_270:
	                    digree = 270;
	                    break;
	                default:
	                    digree = 0;
	                    break;
	                }
	            }
	            if (digree != 0) {
	                // ��תͼƬ
	                Matrix m = new Matrix();
	                m.postRotate(digree);
	                bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
	                        bm.getHeight(), m, true);
	            }
	            return bm;
	        }
	    }





	    private static String gpsInfoConvert(double gpsInfo) {
	        gpsInfo= Math.abs(gpsInfo);
	        String dms = Location.convert(gpsInfo, Location.FORMAT_SECONDS);
	        String[]splits = dms.split(":");
	        String[]secnds = (splits[2]).split("\\.");
	        String seconds;
	        if (secnds.length == 0) {
	            seconds= splits[2];
	        }else{
	            seconds= secnds[0];
	        }
	        return splits[0] + "/1," + splits[1] + "/1," + seconds + "/1";
	 }


//	    public Bitmap convertToBitmap(String path, int w, int h) {
//            BitmapFactory.Options opts = new BitmapFactory.Options();
//            // ����Ϊtureֻ��ȡͼƬ��С
//            opts.inJustDecodeBounds = true;
//            opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
//            // ����Ϊ��
//            BitmapFactory.decodeFile(path, opts);
//            int width = opts.outWidth;
//            int height = opts.outHeight;
//            float scaleWidth = 0.f, scaleHeight = 0.f;
//            if (width > w || height > h) {
//                // ����
//                scaleWidth = ((float) width) / w;
//                scaleHeight = ((float) height) / h;
//            }
//            opts.inJustDecodeBounds = false;
//            float scale = Math.max(scaleWidth, scaleHeight);
//            opts.inSampleSize = (int)scale;
//            WeakReference<Bitmap> weak = new WeakReference<Bitmap>(BitmapFactory.decodeFile(path, opts));
//            return Bitmap.createScaledBitmap(weak.get(), w, h, true);
//        }

	    public  static String getAbsoluteImagePath(Uri uri,Context context)
		{
		       // can post image
			ContentResolver cr = context.getContentResolver();

		       String [] proj={MediaStore.Images.Media.DATA};
		       Cursor cursor = cr.query( uri,
		                       proj,                 // Which columns to return
		                       null,       // WHERE clause; which rows to return (all rows)
		                       null,       // WHERE clause selection arguments (none)
		                       null);                 // Order-by clause (ascending by name)

		       int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		       cursor.moveToFirst();

		       return cursor.getString(column_index);
		   }
	    /**
	     * ����bitmapתԲ��bitmap
	     * @param bitmap
	     * @return
	     */
	    public static Bitmap GetRoundedCornerBitmap(Bitmap bitmap) {
	        try {
	            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
	                    bitmap.getHeight(), Config.ARGB_8888);
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
	            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

	            final Rect src = new Rect(0, 0, bitmap.getWidth(),
	                    bitmap.getHeight());

	            canvas.drawBitmap(bitmap, src, rect, paint);
	            return output;
	        } catch (Exception e) {
	            return bitmap;
	        }
	    }

	    /**
		 * draw to round
		 */
		public static Bitmap toRoundBitmap(Bitmap bitmap,int col) {
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			float roundPx;
			float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
			if (width <= height) {
				roundPx = width / 2;
				top = 0;
				bottom = width;
				left = 0;
				right = width;
				height = width;
				dst_left = 0;
				dst_top = 0;
				dst_right = width;
				dst_bottom = width;
			} else {
				roundPx = height / 2;
				float clip = (width - height) / 2;
				left = clip;
				right = width - clip;
				top = 0;
				bottom = height;
				width = height;
				dst_left = 0;
				dst_top = 0;
				dst_right = height;
				dst_bottom = height;
			}

			Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
			Canvas canvas = new Canvas(output);

			final int color = 0xff424242;
			final Paint paint = new Paint();
			final Rect src = new Rect((int) left, (int) top, (int) right,
					(int) bottom);
			final Rect dst = new Rect((int) dst_left, (int) dst_top,
					(int) dst_right, (int) dst_bottom);
			final RectF rectF = new RectF(dst);

			paint.setAntiAlias(true);

			canvas.drawARGB(0, 0, 0, 0);

			paint.setColor(color);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(bitmap, src, dst, paint);

			paint.setColor(col);
			paint.setStyle(Style.STROKE);
			paint.setStrokeWidth(6);
//			canvas.drawCircle(roundPx, roundPx, roundPx, paint);
			canvas.drawArc(rectF, 0, 360, true, paint);
			return output;
		}

		/***
         * ͼƬ�����ŷ���
         *
         * @param bgimage
         *            ��ԴͼƬ��Դ
         * @param newWidth
         *            �����ź���
         * @param newHeight
         *            �����ź�߶�
         * @return
         */
        public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
                        double newHeight) {
                // ��ȡ���ͼƬ�Ŀ�͸�
                float width = bgimage.getWidth();
                float height = bgimage.getHeight();
                // ��������ͼƬ�õ�matrix����
                Matrix matrix = new Matrix();
                // ������������
                float scaleWidth = ((float) newWidth) / width;
                float scaleHeight = ((float) newHeight) / height;
                // ����ͼƬ����
                matrix.postScale(scaleWidth, scaleHeight);
                Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                                (int) height, matrix, true);
                return bitmap;
        }
        public static void saveBitmap(String path,String filename,Bitmap destBitmap)
        {
        	 File dirFile = new File(path);
             if(!dirFile.exists()){
                 dirFile.mkdir();
             }
             File file = new File(path+filename);
//             if(!file.exists()){
//            	 file.mkdir();
//             }
//             SDFile
             try {
				file.createNewFile();
				file.setWritable(true);
			} catch (IOException e1) {

				e1.printStackTrace();
			}

             if(file.exists()){
                 file.delete();
             }
             FileOutputStream out;
             try{
                 out = new FileOutputStream(file);
                 if(destBitmap.compress(Bitmap.CompressFormat.PNG, 90, out))
                 {
                     out.flush();
                     out.close();
                 }
             }catch (FileNotFoundException e)
             {
                 e.printStackTrace();
             }catch (IOException e)
             {
                 e.printStackTrace();
             }
        }
//        01-04 16:01:13.678: W/System.err(29963): java.io.FileNotFoundException: pathicotab_news_now_v5.png: open failed: EROFS (Read-only file system)


//        public static void saveBitmap(String path,Bitmap destBitmap){
//        	Log.i("coyc", path);
//            File f = new File(path);
//            if(f.exists())
//            {
//            	f.delete();
//            }
//            try {
//				f.createNewFile();
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//
//            FileOutputStream fos = null;
//            try {
//                    fos= new FileOutputStream(f);
//               //ÿ��ִ�е���ʱ���׳��쳣FileNotFoundException
//            } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//            }
//            destBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
//            try {
//                    fos.flush();
//                    fos.close();
//            } catch (IOException e) {
//                    e.printStackTrace();
//            }
//        }

        /**
         * ����ָ����ͼ��·���ʹ�С����ȡ����ͼ
         * �˷���������ô���
         *     1. ʹ�ý�С���ڴ�ռ䣬��һ�λ�ȡ��bitmapʵ����Ϊnull��ֻ��Ϊ�˶�ȡ��Ⱥ͸߶ȣ�
         *        �ڶ��ζ�ȡ��bitmap�Ǹ��ݱ���ѹ������ͼ�񣬵����ζ�ȡ��bitmap����Ҫ������ͼ��
         *     2. ����ͼ����ԭͼ������û�����죬����ʹ����2.2�汾���¹���ThumbnailUtils��ʹ
         *        ������������ɵ�ͼ�񲻻ᱻ���졣
         * @param imagePath ͼ���·��
         * @param width ָ�����ͼ��Ŀ��
         * @param height ָ�����ͼ��ĸ߶�
         * @return ���ɵ�����ͼ
         */
        public static Bitmap getImageThumbnail(String imagePath, int width, int height) {
            Bitmap bitmap = null;
            Options options = new Options();
            options.inJustDecodeBounds = true;
            // ��ȡ���ͼƬ�Ŀ�͸ߣ�ע��˴���bitmapΪnull
            bitmap = BitmapFactory.decodeFile(imagePath, options);
            options.inJustDecodeBounds = false; // ��Ϊ false
            // �������ű�
            int h = options.outHeight;
            int w = options.outWidth;
            int beWidth = w / width;
            int beHeight = h / height;
            int be = 1;
            if (beWidth < beHeight) {
                be = beWidth;
            } else {
                be = beHeight;
            }
            if (be <= 0) {
                be = 1;
            }
            options.inSampleSize = be;
            // ���¶���ͼƬ����ȡ���ź��bitmap��ע�����Ҫ��options.inJustDecodeBounds ��Ϊ false
            bitmap = BitmapFactory.decodeFile(imagePath, options);
            // ����ThumbnailUtils����������ͼ������Ҫָ��Ҫ�����ĸ�Bitmap����
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
            return bitmap;
        }

        /**
         * ��ȡ��Ƶ������ͼ
         * ��ͨ��ThumbnailUtils������һ����Ƶ������ͼ��Ȼ��������ThumbnailUtils������ָ����С������ͼ��
         * �����Ҫ������ͼ�Ŀ�͸߶�С��MICRO_KIND��������Ҫʹ��MICRO_KIND��Ϊkind��ֵ���������ʡ�ڴ档
         * @param videoPath ��Ƶ��·��
         * @param width ָ�������Ƶ����ͼ�Ŀ��
         * @param height ָ�������Ƶ����ͼ�ĸ߶ȶ�
         * @param kind ����MediaStore.Images.Thumbnails���еĳ���MINI_KIND��MICRO_KIND��
         *            ���У�MINI_KIND: 512 x 384��MICRO_KIND: 96 x 96
         * @return ָ����С����Ƶ����ͼ
         */
        public static Bitmap getVideoThumbnail(String videoPath, int width, int height,
                int kind) {
            Bitmap bitmap = null;
            // ��ȡ��Ƶ������ͼ
            bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
            System.out.println("w"+bitmap.getWidth());
            System.out.println("h"+bitmap.getHeight());
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
            return bitmap;
        }




}
