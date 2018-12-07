package com.app.tanklib.bitmap.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.app.tanklib.R;
import com.app.tanklib.bitmap.CacheManage;
import com.app.tanklib.bitmap.task.ImageLoader;
import com.app.tanklib.bitmap.task.ImageRequest;
import com.app.tanklib.bitmap.task.RequestResponse;
import com.app.tanklib.util.MD5;

public class NetImageView extends ImageView implements ImageRequest.Listener {

	Handler uiHandler;
	private Listener listener;

	private Drawable errorImage;
	private int errorImageResId;
	private Drawable placeholderImage;
	private int placeholderImageResId;
	private String loadedImageUrl;
	private String pendingImageUrl;
	private int pendingImageType;
	BitmapFactory.Options opt;

	// 圆角
	// private boolean isRounded;

	private Animation animation;
	private boolean animated = false;
	private boolean animateOnce = true;

	private enum States {
		EMPTY, LOADING, LOADED, RELOADING, CANCELLED, ERROR,
	}

	private States currentState = States.EMPTY;

	public void resertStates() {
		currentState = States.EMPTY;
	}

	public interface Listener {
		public void onImageLoadStarted();

		public void onImageLoading(int result);

		public void onImageLoadComplete();

		public void onImageLoadError();

		public void onImageLoadCancelled();
	}

	public NetImageView(Context context) {
		super(context);
		initialize(null);
	}

	public NetImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(attrs);
	}

	public NetImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialize(attrs);
	}

	void initialize(AttributeSet attrSet) {
		uiHandler = new Handler();

		if (null != attrSet) {
			TypedArray a = getContext().obtainStyledAttributes(attrSet,
					R.styleable.NetImage);
			// isRounded = a.getBoolean(R.styleable.SodaoImage_roundable,
			// false);
			animateOnce = a.getBoolean(R.styleable.NetImage_animateOnce, true);

			int animId = a.getResourceId(R.styleable.NetImage_animation, 0);
			if (animId != 0) {
				animation = AnimationUtils.loadAnimation(getContext(), animId);
			}
			a.recycle();
		} else {
			// isRounded = false;
		}
	}

	/**
	 * Set a listener to be informed about events from this view. If this is not
	 * set, then no events are sent.
	 * 
	 * @param listener
	 *            Listener
	 */
	public void setListener(Listener listener) {
		this.listener = listener;
	}

	/**
	 * Load an image asynchronously from the web
	 * 
	 * @param imageUrl
	 *            Image URL to download image from
	 */
	// public void setImageUrl(String imageUrl, int imageType) {
	// if (imageType == CacheManage.IMAGE_TYPE_BIG
	// && imageType == CacheManage.IMAGE_TYPE_SMALL) {
	// setImageUrl(imageUrl, getOptions(imageUrl),
	// R.color.image_backgroud, R.color.image_backgroud, imageType);
	// } else {
	// setImageUrl(imageUrl, null, R.color.image_backgroud,
	// R.color.image_backgroud, imageType);
	// }
	// }

	public void setImageUrl(final String imageUrl, int imageType) {
		if (CacheManage.getInstance().getBitCounter()
				.containsKey(MD5.getMD5(imageUrl))) {
			setImageBitmap(CacheManage.getInstance().getBitCounter()
					.getBitmap(MD5.getMD5(imageUrl)));
			if (listener != null) {
				listener.onImageLoadComplete();
			}
		} else {
			setImageUrl(imageUrl, null, R.color.image_backgroud,
					R.color.image_backgroud, imageType);
		}
	}


	public void setImageUrl(String imageUrl, BitmapFactory.Options options,
			int imageType) {
		setImageUrl(imageUrl, options, R.color.image_backgroud,
				R.color.image_backgroud, imageType);
	}

	/**
	 * Load an image asynchronously from the web
	 * 
	 * @param imageUrl
	 *            Image URL to download image from
	 * @param options
	 *            Options to use when loading the image. See the documentation
	 *            for {@link BitmapFactory.Options} for more details. Can be
	 *            null.
	 * @param errorImageResId
	 *            Resource ID to be displayed in case the image could not be
	 *            loaded. If 0, no new image will be displayed on error.
	 * @param placeholderImageResId
	 *            Resource ID to set for placeholder image while image is
	 *            loading.
	 */
	public void setImageUrl(String imageUrl, BitmapFactory.Options options,
			int errorImageResId, int placeholderImageResId, int imageType) {
		if (imageUrl == null) {
			return;
		} else if (currentState == States.LOADED
				&& imageUrl.equals(loadedImageUrl)) {
			return;
		} else if (currentState == States.LOADING
				&& imageUrl.equals(pendingImageUrl)) {
			return;
		}

		currentState = States.LOADING;
		this.errorImageResId = errorImageResId;
		if (this.placeholderImageResId > 0) {
			setImageResource(this.placeholderImageResId);
		} else if (this.placeholderImage != null) {
			setImageDrawable(this.placeholderImage);
		} else if (placeholderImageResId > 0) {
			setImageResource(placeholderImageResId);
		}
		if (this.listener != null) {
			listener.onImageLoadStarted();
		}
		pendingImageUrl = imageUrl;
		pendingImageType = imageType;
		ImageLoader.load(getContext(), imageUrl, this, options, imageType);
	}

	/**
	 * This method is called when the drawable has been downloaded (or retreived
	 * from cache) and is ready to be displayed. If you override this class,
	 * then you should not call this method via super.onBitmapLoaded(). Instead,
	 * handle the drawable as necessary (ie, resizing or other transformations),
	 * and then call postToGuiThread() to display the image from the correct
	 * thread.
	 * 
	 * If you only need a callback to be notified about the drawable being
	 * loaded to update other GUI elements and whatnot, then you should override
	 * onImageLoaded() instead.
	 * 
	 * @param response
	 *            Request response
	 */
	public void onBitmapLoaded(final RequestResponse response) {
		if (response.originalRequest.imageUrl.equals(pendingImageUrl)) {
			postToGuiThread(new Runnable() {
				public void run() {
					Bitmap bitmap = response.bitmapReference.get();
					if (bitmap != null) {
						System.out.println("onBitmapLoaded  "
								+ response.originalRequest.imageUrl);
						setImageBitmap(bitmap);
						// 加入到缓存管理
						CacheManage.getInstance().put(
								response.originalRequest.imageUrl, bitmap,
								response.originalRequest.imageType);
						if (null != animation && !(animateOnce && animated)) {
							startAnimation(animation);
							animated = true;
						}
						currentState = States.LOADED;
						loadedImageUrl = response.originalRequest.imageUrl;
						pendingImageUrl = null;
						pendingImageType = 0;
						if (listener != null) {
							listener.onImageLoadComplete();
						}
					} else {
						// The garbage collecter hass cleaned up this bitmap by
						// now (yes, that does happen), so re-issue the request
						ImageLoader.load(getContext(),
								response.originalRequest.imageUrl,
								response.originalRequest.listener,
								response.originalRequest.loadOptions,
								response.originalRequest.imageType);
						currentState = States.RELOADING;
					}
					// 等待下次GC时候回收
					bitmap = null;
				}
			});
		} else {
			if (listener != null) {
				listener.onImageLoadCancelled();
			}
			currentState = States.CANCELLED;
		}
	}

	/**
	 * This method is called if the drawable could not be loaded for any reason.
	 * If you need a callback to react to these events, you should override
	 * onImageError() instead.
	 * 
	 * @param message
	 *            Error message (non-localized)
	 */
	public void onBitmapLoadError(String message) {
		currentState = States.ERROR;
		postToGuiThread(new Runnable() {
			public void run() {
				// In case of error, lazily load the drawable here
				if (errorImageResId > 0) {
					errorImage = getResources().getDrawable(errorImageResId);
				}
				if (errorImage != null) {
					setImageDrawable(errorImage);
				}
			}
		});
		if (listener != null) {
			listener.onImageLoadError();
		}
	}

	@Override
	protected void onWindowVisibilityChanged(int visibility) {
		super.onWindowVisibilityChanged(visibility);
		if (visibility == VISIBLE && currentState == States.LOADING) {
			setImageUrl(pendingImageUrl, pendingImageType);
		}
	}

	/**
	 * Called when the URL which the caller asked to load was cancelled. This
	 * can happen for a number of reasons, including the activity being closed
	 * or scrolling rapidly in a ListView. For this reason it is recommended not
	 * to do so much work in this method.
	 */
	public void onBitmapLoadCancelled() {
		currentState = States.CANCELLED;
		if (listener != null) {
			listener.onImageLoadCancelled();
		}
	}

	/**
	 * Post a message to the GUI thread. This should be used for updating the
	 * component from background tasks.
	 * 
	 * @param runnable
	 *            Runnable task
	 */
	public final void postToGuiThread(Runnable runnable) {
		uiHandler.post(runnable);
	}

	public void setErrorImageResId(int errorImageResId) {
		this.errorImageResId = errorImageResId;
	}

	public void setErrorImage(Drawable errorImage) {
		this.errorImage = errorImage;
	}

	public void setPlaceholderImage(Drawable placeholderImage) {
		this.placeholderImage = placeholderImage;
	}

	public void setPlaceholderImageResId(int placeholderImageResId) {
		this.placeholderImageResId = placeholderImageResId;
	}

	public BitmapFactory.Options getOptions(String url) {
		if (null == opt) {
			opt = new BitmapFactory.Options();
			opt.inPreferredConfig = Bitmap.Config.RGB_565;
			opt.inPurgeable = true;
			opt.inInputShareable = true;

		}
		return opt;
	}

	// @Override
	// protected void onDraw(Canvas canvas) {
	// if (isRounded) {
	// Path clipPath = new Path();
	// int w = this.getWidth();
	// int h = this.getHeight();
	// clipPath.addRoundRect(new RectF(0, 0, w, h), 18.0f, 18.0f,
	// Path.Direction.CCW);
	// canvas.clipPath(clipPath);
	// }
	// super.onDraw(canvas);
	// }

}
