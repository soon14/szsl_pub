package com.app.tanklib.util;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ImageUtil {

	// 计算图片缩放（优化图片加载,减少内存开销）
	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;
		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));
		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}
		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	public static void resampleImageAndSaveToNewLocation(String pathInput,
			String pathOutput) throws Exception {
		Bitmap bmp = resampleImage(pathInput, 640, false);

		OutputStream out = new FileOutputStream(pathOutput);
		bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
	}

	public static Bitmap resampleImage(String path, int maxDim, boolean isOnly)
			throws Exception {

		BitmapFactory.Options bfo = new BitmapFactory.Options();
		bfo.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, bfo);

		BitmapFactory.Options optsDownSample = new BitmapFactory.Options();
		if (isOnly) {
			optsDownSample.inSampleSize = getClosestResampleSize(bfo.outWidth,
					bfo.outWidth, maxDim);
		} else {
			optsDownSample.inSampleSize = getClosestResampleSize(bfo.outWidth,
					bfo.outHeight, maxDim);
		}

		Bitmap bmpt = BitmapFactory.decodeFile(path, optsDownSample);

		Matrix m = new Matrix();

		if (bmpt.getWidth() > maxDim || bmpt.getHeight() > maxDim) {
			BitmapFactory.Options optsScale = getResampling(bmpt.getWidth(),
					bmpt.getHeight(), maxDim);
			m.postScale((float) optsScale.outWidth / (float) bmpt.getWidth(),
					(float) optsScale.outHeight / (float) bmpt.getHeight());
		}

		// int sdk = new Integer(Build.VERSION.SDK).intValue();
		// if (sdk > 4) {
		// int rotation = getExifRotation(path);
		// if (rotation != 0) {
		// m.postRotate(rotation);
		// }
		// }

		return Bitmap.createBitmap(bmpt, 0, 0, bmpt.getWidth(),
				bmpt.getHeight(), m, true);
	}

	// isOnly 是否只是显示，true 在图片处理时只显示 false 上传的时候
	public static Bitmap resampleImage(ContentResolver resolver, Uri uripath,
			int maxDim, boolean isOnly) throws Exception {

		BitmapFactory.Options bfo = new BitmapFactory.Options();
		bfo.inJustDecodeBounds = true;
		BitmapFactory
				.decodeStream(resolver.openInputStream(uripath), null, bfo);

		BitmapFactory.Options optsDownSample = new BitmapFactory.Options();
		if (isOnly) {
			optsDownSample.inSampleSize = getClosestResampleSize(bfo.outWidth,
					bfo.outWidth, maxDim);
		} else {
			optsDownSample.inSampleSize = getClosestResampleSize(bfo.outWidth,
					bfo.outHeight, maxDim);
		}

		Bitmap bmpt = BitmapFactory.decodeStream(
				resolver.openInputStream(uripath), null, optsDownSample);

		Matrix m = new Matrix();

		if (bmpt.getWidth() > maxDim || bmpt.getHeight() > maxDim) {
			BitmapFactory.Options optsScale = getResampling(bmpt.getWidth(),
					bmpt.getHeight(), maxDim);
			m.postScale((float) optsScale.outWidth / (float) bmpt.getWidth(),
					(float) optsScale.outHeight / (float) bmpt.getHeight());
		}

		int sdk = new Integer(Build.VERSION.SDK).intValue();
		if (sdk > 4) {
			int rotation = getExifRotation(uripath.toString());
			if (rotation != 0) {
				m.postRotate(rotation);
			}
		}

		return Bitmap.createBitmap(bmpt, 0, 0, bmpt.getWidth(),
				bmpt.getHeight(), m, true);
	}

	// 得到具体的高宽
	public static BitmapFactory.Options getResampling(int cx, int cy, int max) {
		float scaleVal = 1.0f;
		BitmapFactory.Options bfo = new BitmapFactory.Options();
		if (cx > cy) {
			scaleVal = (float) max / (float) cx;
		} else if (cy > cx) {
			scaleVal = (float) max / (float) cy;
		} else {
			scaleVal = (float) max / (float) cx;
		}
		bfo.outWidth = (int) (cx * scaleVal + 0.5f);
		bfo.outHeight = (int) (cy * scaleVal + 0.5f);
		return bfo;
	}

	// 得到最佳的inSampleSize
	public static int getClosestResampleSize(int cx, int cy, int maxDim) {
		int max = Math.max(cx, cy);

		int resample = 1;
		for (resample = 1; resample < Integer.MAX_VALUE; resample++) {
			if (resample * maxDim > max) {
				resample--;
				break;
			}
		}
		if (resample > 0) {
			return resample;
		}
		return 1;
	}

	public static BitmapFactory.Options getBitmapDims(String path)
			throws Exception {
		BitmapFactory.Options bfo = new BitmapFactory.Options();
		bfo.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, bfo);
		return bfo;
	}

	public static int getExifRotation(String imgPath) {
		try {
			ExifInterface exif = new ExifInterface(imgPath);
			String rotationAmount = exif
					.getAttribute(ExifInterface.TAG_ORIENTATION);
			if (!TextUtils.isEmpty(rotationAmount)) {
				int rotationParam = Integer.parseInt(rotationAmount);
				switch (rotationParam) {
				case ExifInterface.ORIENTATION_NORMAL:
					return 0;
				case ExifInterface.ORIENTATION_ROTATE_90:
					return 90;
				case ExifInterface.ORIENTATION_ROTATE_180:
					return 180;
				case ExifInterface.ORIENTATION_ROTATE_270:
					return 270;
				default:
					return 0;
				}
			} else {
				return 0;
			}
		} catch (Exception ex) {
			return 0;
		}
	}

	public static void saveBitmap(Bitmap bitmap, String picPath) {
		File file = new File(picPath);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}

		FileOutputStream out;
		try {
			out = new FileOutputStream(file);
			if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
				out.flush();
				out.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

//	public static String encodeBase64File(String path) throws Exception {
//		File file = new File(path);
//		FileInputStream inputFile = new FileInputStream(file);
//		byte[] buffer = new byte[(int) file.length()];
//		inputFile.read(buffer);
//		inputFile.close();
//		return new BASE64Encoder().encode(buffer);
//	}
//
//	/**
//	 * <p>
//	 * 将base64字符解码保存文件
//	 * </p>
//	 * 
//	 * @param base64Code
//	 * @param targetPath
//	 * @throws Exception
//	 */
//	public static void decoderBase64File(String base64Code, String targetPath)
//			throws Exception {
//		byte[] buffer = new BASE64Decoder().decodeBuffer(base64Code);
//		FileOutputStream out = new FileOutputStream(targetPath);
//		out.write(buffer);
//		out.close();
//	}

}