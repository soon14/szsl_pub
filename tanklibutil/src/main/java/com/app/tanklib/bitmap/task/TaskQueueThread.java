package com.app.tanklib.bitmap.task;

import android.graphics.Bitmap;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 线程基类
 * @author zoo
 *
 */

public abstract class TaskQueueThread extends Thread {
	private static final long SHUTDOWN_TIMEOUT_IN_MS = 100;
	private final Queue<ImageRequest> pendingRequests;
	private boolean isRunning;

	protected abstract Bitmap processRequest(ImageRequest request);

	protected abstract void onRequestComplete(RequestResponse response);

	protected abstract void onRequestCancelled(ImageRequest request);

	public TaskQueueThread(final String taskName) {
		super(taskName);
		pendingRequests = new LinkedList<ImageRequest>();
	}

	@Override
	public void run() {
		ImageRequest request;
		isRunning = true;
		while (isRunning) {
			synchronized (pendingRequests) {
				while (pendingRequests.isEmpty() && isRunning) {
					try {
						pendingRequests.wait();
					} catch (InterruptedException e) {
						isRunning = false;
						break;
					}
				}

				try {
					request = getNextRequest(pendingRequests);
				} catch (Exception e) {
					continue;
				}
			}

			try {
				if (request != null && request.listener != null) {
					try {
						Bitmap bitmap = processRequest(request);
						synchronized (pendingRequests) {
							if (isRequestStillValid(request, pendingRequests)) {
								if (bitmap != null) {
									onRequestComplete(new RequestResponse(
											bitmap, request));
								}
							} else {
								onRequestCancelled(request);
							}
						}
					} catch (Exception e) {
						request.listener.onBitmapLoadError(e.getMessage());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void interrupt() {
		super.interrupt();
		isRunning = false;
	}

	public void addTask(ImageRequest request) {
		synchronized (pendingRequests) {
			pendingRequests.add(request);
			pendingRequests.notifyAll();
		}
	}

	private ImageRequest getNextRequest(Queue<ImageRequest> requestQueue) {
		ImageRequest request = requestQueue.poll();
		if (request.listener == null) {
			return request;
		}
		Iterator requestIterator = requestQueue.iterator();
		while (requestIterator.hasNext()) {
			ImageRequest checkRequest = (ImageRequest) requestIterator.next();
			if (request.listener.equals(checkRequest.listener)) {
				if (request.imageUrl.equals(checkRequest.imageUrl)) {
					request.listener.onBitmapLoadCancelled();
					requestIterator.remove();
				} else {
					request.listener.onBitmapLoadCancelled();
					request = checkRequest;
					requestIterator.remove();
				}
			}
		}

		return request;
	}

	private boolean isRequestStillValid(ImageRequest finishedRequest,
			Queue<ImageRequest> requestQueue) {
		for (ImageRequest checkRequest : requestQueue) {
			if (finishedRequest.listener.equals(checkRequest.listener)
					&& !finishedRequest.imageUrl.equals(checkRequest.imageUrl)) {
				return false;
			}
		}
		return true;
	}

	public void cancelAllRequests() {
		synchronized (pendingRequests) {
			for (ImageRequest request : pendingRequests) {
				request.listener.onBitmapLoadCancelled();
				request.listener = null;
			}
			pendingRequests.clear();
		}
	}

	public void shutdown() {
		try {
			interrupt();
			join(SHUTDOWN_TIMEOUT_IN_MS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
