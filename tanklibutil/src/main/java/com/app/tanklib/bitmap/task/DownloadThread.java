package com.app.tanklib.bitmap.task;

import android.graphics.Bitmap;

import com.app.tanklib.bitmap.CacheManage;
import com.app.tanklib.bitmap.DiskLruCache;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * 图片下载线程,实现线程同步功能
 * 
 * @author zkl
 * 
 */
public class DownloadThread extends TaskQueueThread {

	private int totalSize = 0;
	private int size = 0;
	private int max = 100;

	public DownloadThread() {
		super("Download");
		setPriority(Thread.MIN_PRIORITY);
	}

	@Override
	protected Bitmap processRequest(final ImageRequest request) {
		if (CacheManage.getInstance().containsKey(request.imageUrl,
				request.imageType)) {
			FileLoaderThread.getInstance().addTask(request);
		} else {
			Future f = (Future) DownloadThreadPool.getInstance().get(
					request.imageUrl);
			if (null == f) {
				Callable eval = new Callable() {
					@Override
					public Object call() throws Exception {
						return loadHttpImageToFile(request);
					}
				};

				FutureTask ft = new FutureTask(eval);
				f = DownloadThreadPool.getInstance().put(request.imageUrl, ft);
				if (null == f) {
					f = ft;
					ft.run();
				}
			}
			try {
				Object name = f.get();
				if (null != name && Boolean.valueOf(name.toString())) {
					FileLoaderThread.getInstance().addTask(request);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	protected void onRequestComplete(RequestResponse response) {
		// Never reached
	}

	@Override
	protected void onRequestCancelled(ImageRequest request) {
		// Nothing to do
	}

	/**
	 * 下载图片
	 * 
	 * @param request
	 * @return
	 */
	public boolean loadHttpImageToFile(ImageRequest request) {
		URL myFileUrl = null;
		try {
			myFileUrl = new URL(request.imageUrl);
			// 改变获取规则
			// myFileUrl = new
			// URL(AppApplication.getSizeImage(request.imageUrl));
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return false;
		}
		HttpURLConnection conn = null;
		InputStream is = null;
		BufferedOutputStream bos = null;
		try {
			conn = (HttpURLConnection) myFileUrl.openConnection();
			conn.setDoInput(true);
			conn.connect();
			is = conn.getInputStream();
			// 需要考虑 Transfer-Encoding: chunked 方式
			if (200 == conn.getResponseCode()) {
				int length = (int) conn.getContentLength();
				// String host = conn.getRequestProperty("Host");
				// 解决CMCC或者ChinaNet 连接上，但是返回不了数据的问题
				if (length != -1) {
					totalSize = length;
				} else {
					totalSize = is.available();
				}
				if (totalSize <= 10) {
					totalSize = 50000;
				}
				DiskLruCache.Editor editor = null;
				editor = CacheManage.getInstance().getEditor(request.imageUrl);
				if (editor == null) {
					return false;
				}
				try {
					bos = new BufferedOutputStream(editor.newOutputStream(0),
							1024);
				} catch (java.io.FileNotFoundException e) {
				/*	((BaseLBSApplication) ((Activity) request.context)
							.getApplication()).resetCacheManage();
					editor = CacheManage.getInstance().getEditor(
							MD5.getMD5(request.imageUrl));*/
					if (editor == null) {
						return false;
					}
				}
				byte[] buffer = new byte[1024];
				int readLen = 0;
				int destPos = 0;
				try {
					while ((readLen = is.read(buffer)) > 0) {
						bos.write(buffer, 0, readLen);
						destPos += readLen;
						size = destPos;
						if (request.imageType == CacheManage.IMAGE_TYPE_PROGRESS) {
							/*Intent intent = new Intent(
									MD5.getMD5(request.imageUrl));
							intent.putExtra("result", getProgressInt(max));
							BaseLBSApplication.sendProcessBroadcast(intent);*/
						}
					}
					if (request.imageType == CacheManage.IMAGE_TYPE_PROGRESS) {
						/*Intent intents = new Intent(
								MD5.getMD5(request.imageUrl));
						intents.putExtra("result", getProgressInt(max));
						BaseLBSApplication.sendProcessBroadcast(intents);*/
					}
				} catch (IOException e) {
					try {
						if (editor != null) {
							editor.abort();
						}
					} catch (IOException ignored) {
						e.printStackTrace();
					}
				} finally {
					CacheManage.getInstance().deskflush();
					editor.commit();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != bos) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != conn) {
				conn.disconnect();
			}
		}
		return true;
	}

	// public boolean loadImageToFile(ImageRequest request) {
	// URL myFileUrl = null;
	// try {
	// // myFileUrl = new
	// // URL(AppApplication.getSizeImage(request.imageUrl));
	// myFileUrl = new URL(request.imageUrl);
	// } catch (MalformedURLException e) {
	// e.printStackTrace();
	// return false;
	// }
	// HttpURLConnection conn = null;
	// InputStream is = null;
	// BufferedOutputStream bos = null;
	// try {
	// conn = (HttpURLConnection) myFileUrl.openConnection();
	// conn.setDoInput(true);
	// conn.connect();
	// is = conn.getInputStream();
	// int length = (int) conn.getContentLength();
	// // String host = conn.getRequestProperty("Host");
	// // 解决CMCC或者ChinaNet 连接上，但是返回不了数据的问题
	// if (length != -1) {
	// totalSize = length;
	// } else {
	// totalSize = is.available();
	// }
	// DiskLruCache.Editor editor = null;
	// editor = CacheManage.getInstance().getEditor(request.imageUrl);
	// if (editor == null) {
	// return false;
	// }
	// bos = new BufferedOutputStream(editor.newOutputStream(0), 1024);
	// byte[] buffer = new byte[1024];
	// int readLen = 0;
	// int destPos = 0;
	// // 需要考虑 Transfer-Encoding: chunked 方式
	// try {
	// while ((readLen = is.read(buffer)) > 0) {
	// bos.write(buffer, 0, readLen);
	// destPos += readLen;
	// size = destPos;
	// if (request.imageType == CacheManage.IMAGE_TYPE_BIG) {
	// Intent intent = new Intent(
	// MD5Util.getMD5(request.imageUrl));
	// intent.putExtra("result", getProgressInt(max));
	// AppApplication.sendProcessBroadcast(intent);
	// }
	// }
	// if (request.imageType == CacheManage.IMAGE_TYPE_BIG) {
	// Intent intents = new Intent(
	// MD5Util.getMD5(request.imageUrl));
	// intents.putExtra("result", getProgressInt(max));
	// AppApplication.sendProcessBroadcast(intents);
	// }
	// } catch (IOException e) {
	// try {
	// if (editor != null) {
	// editor.abort();
	// }
	// } catch (IOException ignored) {
	// e.printStackTrace();
	// }
	// } finally {
	// CacheManage.getInstance().deskflush();
	// editor.commit();
	// }
	// } catch (IOException e) {
	// e.printStackTrace();
	// } finally {
	// if (null != bos) {
	// try {
	// bos.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// if (null != is) {
	// try {
	// is.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// if (null != conn) {
	// conn.disconnect();
	// }
	// }
	// return true;
	// }
	//
	// public boolean loadImage(ImageRequest request) {
	// URL myFileUrl = null;
	// Bitmap bitmap = null;
	// try {
	// myFileUrl = new URL(request.imageUrl);
	// } catch (MalformedURLException e) {
	// e.printStackTrace();
	// return false;
	// }
	// HttpURLConnection conn = null;
	// InputStream is = null;
	// try {
	// conn = (HttpURLConnection) myFileUrl.openConnection();
	// conn.setDoInput(true);
	// conn.connect();
	// is = conn.getInputStream();
	// int length = (int) conn.getContentLength();
	// totalSize = length;
	// if (length != -1) {
	// byte[] imgData = new byte[length];
	// byte[] buffer = new byte[1024];
	// int readLen = 0;
	// int destPos = 0;
	// while ((readLen = is.read(buffer)) > 0) {
	// System.arraycopy(buffer, 0, imgData, destPos, readLen);
	// destPos += readLen;
	// size = destPos;
	// // 发送进度条广播
	// if (request.imageType == CacheManage.IMAGE_TYPE_BIG) {
	// Intent intent = new Intent(
	// MD5Util.getMD5(request.imageUrl));
	// intent.putExtra("result", getProgressInt(max));
	// AppApplication.sendProcessBroadcast(intent);
	// }
	// }
	// // 处理成相应规格的图片
	// if (null != request.loadOptions) {
	// bitmap = BitmapFactory.decodeByteArray(imgData, 0,
	// imgData.length, request.loadOptions);
	// } else {
	// bitmap = BitmapFactory.decodeByteArray(imgData, 0,
	// imgData.length);
	// }
	// // 发送进度条广播
	// if (request.imageType == CacheManage.IMAGE_TYPE_BIG) {
	// Intent intents = new Intent(
	// MD5Util.getMD5(request.imageUrl));
	// intents.putExtra("result", getProgressInt(max));
	// AppApplication.sendProcessBroadcast(intents);
	// }
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// if (null != is) {
	// try {
	// is.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// if (null != conn) {
	// conn.disconnect();
	// }
	// }
	// CacheManage.getInstance().put(request.imageUrl, bitmap,
	// request.imageType);
	// return true;
	// }

	// 备份代码
	// /**
	// * 下载图片
	// *
	// * @param request
	// * @return
	// */
	// public boolean loadHttpImageToFile(ImageRequest request) {
	// URL myFileUrl = null;
	// try {
	// // myFileUrl = new URL(request.imageUrl);
	// myFileUrl = new URL(ShowApplication.getSizeImage(request.imageUrl));
	// } catch (MalformedURLException e) {
	// e.printStackTrace();
	// return false;
	// }
	// System.out.println("loadImageToFile  :  "
	// + ShowApplication.getSizeImage(request.imageUrl));
	// HttpURLConnection conn = null;
	// InputStream is = null;
	// BufferedOutputStream bos = null;
	// try {
	// conn = (HttpURLConnection) myFileUrl.openConnection();
	// conn.setDoInput(true);
	// conn.connect();
	// is = conn.getInputStream();
	// // 需要考虑 Transfer-Encoding: chunked 方式
	// if (200 == conn.getResponseCode()) {
	// int length = (int) conn.getContentLength();
	// //String host = conn.getRequestProperty("Host");
	// // 解决CMCC或者ChinaNet 连接上，但是返回不了数据的问题
	// // if ("imgcache.mysodao.com".equals(host)
	// // || "css.sodao.com".equals(host)) {
	// if (length != -1) {
	// totalSize = length;
	// } else {
	// totalSize = is.available();
	// }
	// if (totalSize <= 10) {
	// totalSize = 50000;
	// }
	// DiskLruCache.Editor editor = null;
	// editor = CacheManage.getInstance().getEditor(
	// MD5.getMD5(request.imageUrl));
	// if (editor == null) {
	// return false;
	// }
	// bos = new BufferedOutputStream(editor.newOutputStream(0), 1024);
	// byte[] buffer = new byte[1024];
	// int readLen = 0;
	// int destPos = 0;
	// try {
	// while ((readLen = is.read(buffer)) > 0) {
	// bos.write(buffer, 0, readLen);
	// destPos += readLen;
	// size = destPos;
	// if (request.imageType == CacheManage.IMAGE_TYPE_BIG) {
	// Intent intent = new Intent(MD5
	// .getMD5(request.imageUrl));
	// intent.putExtra("result", getProgressInt(max));
	// ShowApplication.sendProcessBroadcast(intent);
	// }
	// }
	// if (request.imageType == CacheManage.IMAGE_TYPE_BIG) {
	// Intent intents = new Intent(MD5
	// .getMD5(request.imageUrl));
	// intents.putExtra("result", getProgressInt(max));
	// ShowApplication.sendProcessBroadcast(intents);
	// }
	// } catch (IOException e) {
	// try {
	// if (editor != null) {
	// editor.abort();
	// }
	// } catch (IOException ignored) {
	// e.printStackTrace();
	// }
	// } finally {
	// CacheManage.getInstance().deskflush();
	// editor.commit();
	// }
	// // }
	// }
	// } catch (IOException e) {
	// e.printStackTrace();
	// } finally {
	// if (null != bos) {
	// try {
	// bos.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// if (null != is) {
	// try {
	// is.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// if (null != conn) {
	// conn.disconnect();
	// }
	// }
	// return true;
	// }

	// 图片下载百分比
	private int getProgressInt(int max) {
		int result = (totalSize > 0) ? (int) (size * max * 1.0 / totalSize) : 0;
		return result;
	}
}
