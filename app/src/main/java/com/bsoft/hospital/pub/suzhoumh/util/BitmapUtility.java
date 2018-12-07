package com.bsoft.hospital.pub.suzhoumh.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;

public class BitmapUtility {

	/**
	 * 得到压缩的图片
	 * 
	 * @param filePath
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap getSmallBitmap(String filePath,int width,int height) {

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, width, height);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		Bitmap bm = BitmapFactory.decodeFile(filePath, options);
		if (bm == null) {
			return null;
		}
		int degree = readPictureDegree(filePath);
		bm = rotateBitmap(bm, degree);
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			bm.compress(Bitmap.CompressFormat.JPEG, 30, baos);

		} finally {
			try {
				if (baos != null)
					baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bm;

	}

	private static Bitmap rotateBitmap(Bitmap bitmap, int rotate) {
		if (bitmap == null)
			return null;

		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		// Setting post rotate to 90
		Matrix mtx = new Matrix();
		mtx.postRotate(rotate);
		return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
	}

	private static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	private static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
		}

		return inSampleSize;
	}

	// 存储进SD卡

	public static void saveFile(Bitmap bm, String fileName)  {

		BufferedOutputStream bos = null;
		try
		{
			File dirFile = new File(fileName);
			// 检测图片是否存在
			if (dirFile.exists()) {

				dirFile.delete(); // 删除原图片

			}
			File myCaptureFile = new File(fileName);

			bos = new BufferedOutputStream(
					new FileOutputStream(myCaptureFile));

			// 100表示不进行压缩，70表示压缩率为30%

			bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);

			bos.flush();
			bos.close();
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}

	}
	
	   /**
     * 获得缩略图
     * 
     * @param originalBitmap
     * @return
     */
    public static Bitmap thumbnailWithImageWithoutScale(Bitmap originalBitmap,int width,int height) {
    	//获取分辨率
        //Display display = getWindowManager().getDefaultDisplay();
        //int height = display.getHeight();
        //int SCALE = 500;// 缩略图大小
//    	DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        int width = dm.widthPixels;//宽度
//        int height = dm.heightPixels ;//高度
        
        //int SCALE_WIDTH = width;
        //int SCALE_HEIGHT = originalBitmap.getWidth()*width/originalBitmap.getHeight();
    	
    	int SCALE_WIDTH = width;
    	int SCALE_HEIGHT = height;
        
        // 得到缩略图
        Bitmap bitmap = ThumbnailUtils.extractThumbnail(originalBitmap, SCALE_WIDTH, SCALE_HEIGHT, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
     
        return bitmap;
    }
}
