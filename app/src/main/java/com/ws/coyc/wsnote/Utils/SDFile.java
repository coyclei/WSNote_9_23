package com.ws.coyc.wsnote.Utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;


public class SDFile {

	/**
	 * Android SD锟斤拷锟斤拷锟斤拷 锟斤拷锟斤拷Android锟斤拷锟斤拷锟斤拷使锟矫碉拷时锟津开革拷锟竭筹拷锟斤拷锟斤拷锟斤拷锟侥硷拷锟斤拷写锟斤拷
	 * 
	 * @param file
	 * @param filename
	 *            eg:travelplan.json
	 * @param dir
	 *            GoTalk/TravelPlan/
	 */
	public static synchronized void Save(String file, String filename,
			String dir) {
		String sd_dir = Environment.getExternalStorageDirectory() + "/";
		File dstDir = new File(sd_dir + dir);
		if (!dstDir.exists()) {
			dstDir.mkdirs();
		}
		if (dstDir.exists() && dstDir.canWrite()) {
			File dstFile = new File(sd_dir + dir + filename);
			try {
				dstFile.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			FileOutputStream fos = null;
			if (dstFile.exists() && dstFile.canWrite()) {
				try {

					fos = new FileOutputStream(dstFile);
					fos.write(file.getBytes());
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (fos != null) {
						try {
							fos.flush();
							fos.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	public static synchronized void Save(String file, String dir_filename) {
		String sd_dir = Environment.getExternalStorageDirectory() + "/";
		File dstDir = new File(sd_dir + dir_filename);
		if (!dstDir.exists()) {
			dstDir.mkdirs();
		}
		if (dstDir.exists() && dstDir.canWrite()) {
			File dstFile = new File(sd_dir + dir_filename);
			try {
				dstFile.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			FileOutputStream fos = null;
			if (dstFile.exists() && dstFile.canWrite()) {
				try {

					fos = new FileOutputStream(dstFile);
					fos.write(file.getBytes());
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (fos != null) {
						try {
							fos.flush();
							fos.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	/**
	 * 删锟斤拷某锟斤拷锟截讹拷锟斤拷锟侥硷拷
	 */
	public static synchronized void DeleteFile(String dir_filename) {
		String sd_dir = Environment.getExternalStorageDirectory() + "/";
		File dstDir = new File(sd_dir + dir_filename);
		if (dstDir.exists()) {
			System.out.println("deleteFile");
			dstDir.delete();
		}
	}
	
	/**
     * 锟捷癸拷删锟斤拷目录锟铰碉拷锟斤拷锟斤拷锟侥硷拷锟斤拷锟斤拷目录锟斤拷锟斤拷锟斤拷锟侥硷拷
     * @param dir 锟斤拷要删锟斤拷锟斤拷锟侥硷拷目录
     * @return boolean Returns "true" if all deletions were successful.
     *                 If a deletion fails, the method stops attempting to
     *                 delete and returns "false".
     */
    private static boolean DeleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            
            for (int i=0; i<children.length; i++) {
                boolean success = DeleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录锟斤拷时为锟秸ｏ拷锟斤拷锟斤拷删锟斤拷
//        if (dir != null) {
//            String tmpPath = dir.getParent() + File.separator + System.currentTimeMillis();
//            File tmp = new File(tmpPath);
//            dir.renameTo(tmp);
//            return tmp.delete();
//        }
        return dir.delete();
    }
    /**
     * 删锟斤拷某锟斤拷锟侥硷拷锟斤拷锟皆硷拷锟斤拷锟斤拷
     * @param dir
     */
    public static void DeleteDir(String dir)
    {
    	System.out.println("DeleteDir"+dir);
    	String sd_dir = Environment.getExternalStorageDirectory() + "/";
    	File flie = new File(sd_dir+dir);
    	String tmpPath = flie.getParent() + File.separator + System.currentTimeMillis();
    	File tmp = new File(tmpPath);
    	flie.renameTo(tmp);
    	DeleteDir(tmp);
    }

	public static synchronized void SaveByBuffer(byte[] buffer,
			String filename, String dir) {
		String sd_dir = Environment.getExternalStorageDirectory() + "/";
		File dstDir = new File(sd_dir + dir);
		if (!dstDir.exists()) {
			dstDir.mkdirs();
		}
		if (dstDir.exists() && dstDir.canWrite()) {
			File dstFile = new File(sd_dir + dir + filename);
			try {
				dstFile.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			FileOutputStream fos = null;
			if (dstFile.exists() && dstFile.canWrite()) {
				try {

					fos = new FileOutputStream(dstFile);
					fos.write(buffer);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (fos != null) {
						try {
							fos.flush();
							fos.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	public static synchronized String Read(String dir, String filename) {
		String result = "";
		String sd_dir = Environment.getExternalStorageDirectory() + "/";
		File dstDir = new File(sd_dir + dir + filename);
		if (!dstDir.exists()) {
			return "";
		}
		try {
			@SuppressWarnings("resource")
			FileInputStream fi = new FileInputStream(dstDir);

			if (fi.available() == 0) {
				return "";
			}
			byte[] readBytes = new byte[fi.available()];
			while (fi.read(readBytes) != -1) {
			}
			result = new String(readBytes);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static synchronized boolean CheckFileExistsABS(String strFile) {

		String dir = strFile;
		try {
			File f = new File(dir);
			if (!f.exists()) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 锟斤拷锟侥筹拷锟斤拷募锟斤拷欠锟斤拷锟斤拷
	 * 
	 * @param strFile
	 * @return
	 */
	public static synchronized boolean CheckFileExists(String strFile) {

		String sd_dir = Environment.getExternalStorageDirectory() + "/";
		String dir = sd_dir + strFile;
		try {
			File f = new File(dir);
			if (!f.exists()) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 锟斤拷锟侥筹拷锟斤拷募锟斤拷欠锟斤拷锟斤拷
	 * 
	 * @param dir
	 * @param filename
	 * @return
	 */
	public static synchronized boolean CheckFileExists(String dir,
			String filename) {

		String sd_dir = Environment.getExternalStorageDirectory() + "/";
		String strFile = sd_dir + dir + filename;
		try {
			File f = new File(strFile);
			if (!f.exists()) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 锟斤拷锟斤拷一锟斤拷目录
	 */
	public static synchronized void CreateDir(String dir) {
		String sd_dir = Environment.getExternalStorageDirectory() + "/";

		String dir_create = sd_dir + dir;
		try {
			File f = new File(dir_create);
			if (!f.exists()) {
				f.mkdirs();
			}
		} catch (Exception e) {
		}
	}
	public static synchronized void CreateDir2(String dir) {

		String dir_create = dir;
		try {
			File f = new File(dir_create);
			if (!f.exists()) {
				f.mkdirs();
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 锟斤拷取某锟斤拷锟侥硷拷锟斤拷锟铰碉拷锟斤拷锟斤拷锟斤拷锟侥硷拷
	 * 
	 * @param dir
	 *            锟斤拷愿锟侥柯硷拷锟铰凤拷锟�
	 */
	@SuppressLint("ShowToast")
	public static synchronized File[] GetAllFileInFile(String dir) {
		if (CheckFileExists(dir) == false) {
			System.out.println("CheckFileExists(dir)==false");
			CreateDir(dir);
		}

		String sd_dir = Environment.getExternalStorageDirectory() + "/";
		String strFile = sd_dir + dir;
		File catalogFile = new File(strFile);// 目录锟侥硷拷锟斤拷

		File[] files = catalogFile.listFiles();

//		 files[0].getName();

		return files;
	}
	

	/**
	 * 锟斤拷取某锟斤拷锟侥硷拷锟斤拷锟铰碉拷锟斤拷锟斤拷锟斤拷锟侥硷拷锟侥革拷锟斤拷
	 * 
	 * @param dir
	 *            锟斤拷愿锟侥柯硷拷锟铰凤拷锟�
	 */
	public static synchronized int GetAllFileInFileCount(String dir) {
		File[] files = GetAllFileInFile(dir);
		if(files == null)
		{
			return 0;
		}
		return files.length;
	}

	public static synchronized void nioTransferCopy(File source, File target) {
		FileChannel in = null;
		FileChannel out = null;
		FileInputStream inStream = null;
		FileOutputStream outStream = null;
		try {
			inStream = new FileInputStream(source);
			outStream = new FileOutputStream(target);
			in = inStream.getChannel();
			out = outStream.getChannel();
			in.transferTo(0, in.size(), out);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				inStream.close();
				in.close();
				out.close();
				outStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
	
	public static boolean CheckIsSDCanUse()
	{
		if (Environment.getExternalStorageState().equals(  
                Environment.MEDIA_MOUNTED)) {  
			System.out.println("锟斤拷前锟借备SD锟斤拷锟斤拷锟斤拷");
                  return true;
            }else {  
            	System.out.println("锟斤拷前锟借备SD锟斤拷锟斤拷锟斤拷锟斤拷");
            	return false;
            }  
	}
	
	public static String getFileName(String path)
	{
		String re = "";
		File f = new File(path);
		if(f!=null)
		{
			re = f.getName();	
		}
		return re;
	}
	
	
	/**  
     * 锟斤拷锟狡碉拷锟斤拷锟侥硷拷  
     * @param oldPath String 原锟侥硷拷路锟斤拷 锟界：c:/fqf.txt  
     * @param newPath String 锟斤拷锟狡猴拷路锟斤拷 锟界：f:/fqf.txt  
     * @return boolean  
     */   
   public static void  copyFile(String oldPath, String newPath) {   
       try {   
           int bytesum = 0;   
           int byteread = 0;   
           File oldfile = new File(oldPath);   
           if (!oldfile.exists()) { //锟侥硷拷锟斤拷锟斤拷锟斤拷时   
               InputStream inStream = new FileInputStream(oldPath); //锟斤拷锟斤拷原锟侥硷拷   
               FileOutputStream fs = new FileOutputStream(newPath);   
               byte[] buffer = new byte[1024];   
               int length;   
               while ( (byteread = inStream.read(buffer)) != -1) {   
                   bytesum += byteread; //锟街斤拷锟斤拷 锟侥硷拷锟斤拷小   
                   System.out.println(bytesum);   
                   fs.write(buffer, 0, byteread);   
               }   
               inStream.close();   
           }   
       }   
       catch (Exception e) {   
           System.out.println("锟斤拷锟狡碉拷锟斤拷锟侥硷拷锟斤拷锟斤拷锟斤拷锟斤拷");   
           e.printStackTrace();   
       }   
   }   
  
   /**  
     * 锟斤拷锟斤拷锟斤拷锟斤拷锟侥硷拷锟斤拷锟斤拷锟斤拷  
     * @param oldPath String 原锟侥硷拷路锟斤拷 锟界：c:/fqf  
     * @param newPath String 锟斤拷锟狡猴拷路锟斤拷 锟界：f:/fqf/ff  
     * @return boolean  
     */   
   public static void copyFolder(String oldPath, String newPath) {   
  
       try {   
           (new File(newPath)).mkdirs();    
           File a=new File(oldPath);   
           String[] file=a.list();   
           File temp=null;   
           for (int i = 0; i < file.length; i++) {   
               if(oldPath.endsWith(File.separator)){   
                   temp=new File(oldPath+file[i]);   
               }   
               else{   
                   temp=new File(oldPath+File.separator+file[i]);   
               }   
  
               if(temp.isFile()){   
                   FileInputStream input = new FileInputStream(temp);   
                   FileOutputStream output = new FileOutputStream(newPath + "/" +   
                           (temp.getName()).toString());   
                   byte[] b = new byte[1024 * 5];   
                   int len;   
                   while ( (len = input.read(b)) != -1) {   
                       output.write(b, 0, len);   
                   }   
                   output.flush();   
                   output.close();   
                   input.close();   
               }   
               if(temp.isDirectory()){
                   copyFolder(oldPath+"/"+file[i],newPath+"/"+file[i]);   
               }   
           }   
       }   
       catch (Exception e) {   
           System.out.println("拷贝异常");   
           e.printStackTrace();   
  
       }   
  
   }

	public static void saveBitmap(String path,String filename,Bitmap destBitmap)
	{
		File dirFile = new File(path);
		if(!dirFile.exists()){
			dirFile.mkdirs();
		}
		if(!dirFile.exists())
		{

			dirFile.mkdirs();
		}
		File file = new File(path+filename);
		try {
			file.createNewFile();
			file.setWritable(true);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
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
			Log.i("coyc", "catch FileNotFoundException e");
			e.printStackTrace();
		}catch (IOException e)
		{
			Log.i("coyc", "catch IOException e");
			e.printStackTrace();
		}
	}
}
