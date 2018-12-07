package com.app.tanklib.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.app.tanklib.bitmap.task.ImageRequest;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 内存储器图片操作类
 * 
 * @author zkl
 * 
 */
public class DiskCache implements ICache {
	private DiskLruCache mDiskCache;
	private static final int DISK_CACHE_SIZE = 1024 * 1024 * 30;
	private static final int APP_VERSION = 1;
	private static final int VALUE_COUNT = 1;

	public DiskCache(Context context) {
		try {
			mDiskCache = DiskLruCache.open(getDiskCacheDir(context),
					APP_VERSION, VALUE_COUNT, DISK_CACHE_SIZE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean writeBitmapToFile(Bitmap bitmap, DiskLruCache.Editor editor)
			throws IOException, FileNotFoundException {
		OutputStream out = null;
		try {
			out = new BufferedOutputStream(editor.newOutputStream(0), 1024);
			return bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	// 内存储（可变外存储）
	private File getDiskCacheDir(Context context) {
		// final String cachePath = Environment.getExternalStorageState() ==
		// Environment.MEDIA_MOUNTED
		// || !Utils.isExternalStorageRemovable() ? Utils
		// .getExternalCacheDir(context).getPath() : context.getCacheDir()
		// .getPath();
		//
		// return new File(cachePath + File.separator + uniqueName);
		return new File(context.getCacheDir(), "temp");
	}

	public void put(String key, Bitmap data) {
		DiskLruCache.Editor editor = null;
		try {
			editor = mDiskCache.edit(key);
			if (editor == null) {
				return;
			}

			if (writeBitmapToFile(data, editor)) {
				mDiskCache.flush();
				editor.commit();
			} else {
				editor.abort();
			}
		} catch (IOException e) {
			try {
				if (editor != null) {
					editor.abort();
				}
			} catch (IOException ignored) {
				e.printStackTrace();
			}
		}
	}

	public void deskflush() {
		try {
			mDiskCache.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public DiskLruCache.Editor getEditor(String key) {
		try {
			return mDiskCache.edit(key);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Bitmap getBitmap(String key, ImageRequest request) {
		Bitmap bitmap = null;
		DiskLruCache.Snapshot snapshot = null;
		try {
			snapshot = mDiskCache.get(key);
			if (snapshot == null) {
				return null;
			}
			final InputStream in = snapshot.getInputStream(0);
			if (in != null) {
				final BufferedInputStream buffIn = new BufferedInputStream(in,
						1024);
				// if (request.imageType == CacheManage.IMAGE_TYPE_BIG
				// || request.imageType == CacheManage.IMAGE_TYPE_SMALL) {
				// Matrix m = new Matrix();
				// Bitmap resizedBitmap = BitmapFactory.decodeStream(buffIn);
				// int newWidth = ShowApplication
				// .getSwitchKeyWidth(request.imageType,
				// ShowApplication.getWidthPixels());
				// int width = resizedBitmap.getWidth();
				// int height = resizedBitmap.getHeight();
				// float temp = ((float) resizedBitmap.getWidth())
				// / ((float) resizedBitmap.getHeight());
				// int newHeight = (int) ((newWidth) * temp);
				// float scaleWidth = ((float) newWidth) / width;
				// float scaleHeight = ((float) newHeight) / height;
				// Matrix matrix = new Matrix();
				// matrix.postScale(scaleWidth, scaleHeight);
				// bitmap = Bitmap.createBitmap(resizedBitmap, 0, 0,
				// resizedBitmap.getWidth(),
				// resizedBitmap.getHeight(), m, true);
				// } else {
				// bitmap = BitmapFactory.decodeStream(buffIn);
				// }
				bitmap = BitmapFactory.decodeStream(buffIn);
			}
		} catch (OutOfMemoryError e) {
			System.gc();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (snapshot != null) {
				snapshot.close();
			}
		}
		return bitmap;
	}

	// 备份
	// public Bitmap getBitmap(String key, BitmapFactory.Options loadOptions) {
	// Bitmap bitmap = null;
	// DiskLruCache.Snapshot snapshot = null;
	// try {
	// snapshot = mDiskCache.get(key);
	// if (snapshot == null) {
	// return null;
	// }
	// final InputStream in = snapshot.getInputStream(0);
	// if (in != null) {
	// final BufferedInputStream buffIn = new BufferedInputStream(in,
	// 1024);
	// if (null != loadOptions) {
	// // bitmap = BitmapFactory.decodeStream(buffIn,loadOptions);
	// bitmap = BitmapFactory.decodeStream(buffIn, null,
	// loadOptions);
	// } else {
	// bitmap = BitmapFactory.decodeStream(buffIn);
	// }
	// }
	// } catch (OutOfMemoryError e) {
	// System.gc();
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// if (snapshot != null) {
	// snapshot.close();
	// }
	// }
	// return bitmap;
	// }

	public Bitmap getBitmap(String key) {
		Bitmap bitmap = null;
		DiskLruCache.Snapshot snapshot = null;
		try {
			snapshot = mDiskCache.get(key);
			if (snapshot == null) {
				return null;
			}
			final InputStream in = snapshot.getInputStream(0);
			if (in != null) {
				final BufferedInputStream buffIn = new BufferedInputStream(in,
						1024);
				bitmap = BitmapFactory.decodeStream(buffIn);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (snapshot != null) {
				snapshot.close();
			}
		}
		return bitmap;
	}

	public boolean containsKey(String key) {
		boolean contained = false;
		DiskLruCache.Snapshot snapshot = null;
		try {
			snapshot = mDiskCache.get(key);
			contained = snapshot != null;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (snapshot != null) {
				snapshot.close();
			}
		}
		return contained;

	}

	public void clearCache() {
		try {
			mDiskCache.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public File getCacheFolder() {
		return mDiskCache.getDirectory();
	}

}
