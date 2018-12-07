package com.app.tanklib.bitmap.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.app.tanklib.R;
import com.app.tanklib.bitmap.CacheManage;
import com.app.tanklib.bitmap.task.ImageLoader;
import com.app.tanklib.bitmap.task.ImageRequest;
import com.app.tanklib.bitmap.task.RequestResponse;
import com.app.tanklib.util.MD5;
import com.app.tanklib.util.SystemUtils;

/**
 * 圆角ImageView
 * 
 * 
 */
public class RoundImageView extends ImageView implements ImageRequest.Listener {

	private static final ScaleType SCALE_TYPE = ScaleType.CENTER_CROP;

	private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
	private static final int COLORDRAWABLE_DIMENSION = 2;

	private static final int DEFAULT_BORDER_WIDTH = 0;
	private static final int DEFAULT_BORDER_COLOR = Color.BLACK;

	private final RectF mDrawableRect = new RectF();
	private final RectF mBorderRect = new RectF();

	private final Matrix mShaderMatrix = new Matrix();
	private final Paint mBitmapPaint = new Paint();
	private final Paint mBorderPaint = new Paint();

	private int mBorderColor = DEFAULT_BORDER_COLOR;
	private int mBorderWidth = DEFAULT_BORDER_WIDTH;

	private Bitmap mBitmap;
	private BitmapShader mBitmapShader;
	private int mBitmapWidth;
	private int mBitmapHeight;

	private float mDrawableRadius;
	private float mBorderRadius;

	private ColorFilter mColorFilter;

	private boolean mReady;
	private boolean mSetupPending;

	// /////////////////////////////////////
	Handler uiHandler;
	private Drawable errorImage;
	private int errorImageResId;
	private Drawable placeholderImage;
	private int placeholderImageResId;
	private String loadedImageUrl;
	private String pendingImageUrl;
	private int pendingImageType;
	BitmapFactory.Options opt;
	private Listener listener;

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

	public void setListener(Listener listener) {
		this.listener = listener;
	}

	public RoundImageView(Context context) {
		super(context);

		init();
	}

	public RoundImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		init();
	}

	public RoundImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.CircleImageView, defStyle, 0);

		mBorderWidth = a.getDimensionPixelSize(
				R.styleable.CircleImageView_border_width, DEFAULT_BORDER_WIDTH);
		mBorderColor = a.getColor(R.styleable.CircleImageView_border_color,
				DEFAULT_BORDER_COLOR);

		a.recycle();

		init();
	}

	private void init() {
		super.setScaleType(SCALE_TYPE);
		uiHandler = new Handler();
		mReady = true;

		if (mSetupPending) {
			setup();
			mSetupPending = false;
		}
	}

	@Override
	public ScaleType getScaleType() {
		return SCALE_TYPE;
	}

	@Override
	public void setScaleType(ScaleType scaleType) {
		if (scaleType != SCALE_TYPE) {
			throw new IllegalArgumentException(String.format(
					"ScaleType %s not supported.", scaleType));
		}
	}

	@Override
	public void setAdjustViewBounds(boolean adjustViewBounds) {
		if (adjustViewBounds) {
			throw new IllegalArgumentException(
					"adjustViewBounds not supported.");
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (getDrawable() == null) {
			return;
		}

		canvas.drawCircle(getWidth() / 2, getHeight() / 2, mDrawableRadius,
				mBitmapPaint);
		if (mBorderWidth != 0) {
			canvas.drawCircle(getWidth() / 2, getHeight() / 2, mBorderRadius,
					mBorderPaint);
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		setup();
	}

	public int getBorderColor() {
		return mBorderColor;
	}

	public void setBorderColor(int borderColor) {
		if (borderColor == mBorderColor) {
			return;
		}

		mBorderColor = borderColor;
		mBorderPaint.setColor(mBorderColor);
		invalidate();
	}

	public int getBorderWidth() {
		return mBorderWidth;
	}

	public void setBorderWidth(int borderWidth) {
		if (borderWidth == mBorderWidth) {
			return;
		}

		mBorderWidth = borderWidth;
		setup();
	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		super.setImageBitmap(bm);
		mBitmap = bm;
		setup();
	}

	@Override
	public void setImageDrawable(Drawable drawable) {
		super.setImageDrawable(drawable);
		mBitmap = getBitmapFromDrawable(drawable);
		setup();
	}

	@Override
	public void setImageResource(int resId) {
		super.setImageResource(resId);
		mBitmap = getBitmapFromDrawable(getDrawable());
		setup();
	}

	@Override
	public void setImageURI(Uri uri) {
		super.setImageURI(uri);
		mBitmap = getBitmapFromDrawable(getDrawable());
		setup();
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		if (cf == mColorFilter) {
			return;
		}

		mColorFilter = cf;
		mBitmapPaint.setColorFilter(mColorFilter);
		invalidate();
	}

	private Bitmap getBitmapFromDrawable(Drawable drawable) {
		if (drawable == null) {
			return null;
		}

		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		}

		try {
			Bitmap bitmap;

			if (drawable instanceof ColorDrawable) {
				bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION,
						COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
			} else {
				bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(), BITMAP_CONFIG);
			}

			Canvas canvas = new Canvas(bitmap);
			drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
			drawable.draw(canvas);
			return bitmap;
		} catch (OutOfMemoryError e) {
			return null;
		}
	}

	private void setup() {
		if (!mReady) {
			mSetupPending = true;
			return;
		}

		if (mBitmap == null) {
			return;
		}

		mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP,
				Shader.TileMode.CLAMP);

		mBitmapPaint.setAntiAlias(true);
		mBitmapPaint.setShader(mBitmapShader);

		mBorderPaint.setStyle(Paint.Style.STROKE);
		mBorderPaint.setAntiAlias(true);
		mBorderPaint.setColor(mBorderColor);
		mBorderPaint.setStrokeWidth(mBorderWidth);

		mBitmapHeight = mBitmap.getHeight();
		mBitmapWidth = mBitmap.getWidth();

		mBorderRect.set(0, 0, getWidth(), getHeight());
		mBorderRadius = Math.min((mBorderRect.height() - mBorderWidth) / 2,
				(mBorderRect.width() - mBorderWidth) / 2);

		mDrawableRect.set(mBorderWidth, mBorderWidth, mBorderRect.width()
				- mBorderWidth, mBorderRect.height() - mBorderWidth);
		mDrawableRadius = Math.min(mDrawableRect.height() / 2,
				mDrawableRect.width() / 2);

		updateShaderMatrix();
		invalidate();
	}

	private void updateShaderMatrix() {
		float scale;
		float dx = 0;
		float dy = 0;

		mShaderMatrix.set(null);

		if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width()
				* mBitmapHeight) {
			scale = mDrawableRect.height() / (float) mBitmapHeight;
			dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f;
		} else {
			scale = mDrawableRect.width() / (float) mBitmapWidth;
			dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f;
		}

		mShaderMatrix.setScale(scale, scale);
		mShaderMatrix.postTranslate((int) (dx + 0.5f) + mBorderWidth,
				(int) (dy + 0.5f) + mBorderWidth);

		mBitmapShader.setLocalMatrix(mShaderMatrix);
	}

	public void setImageUrl(String imageUrl, int imageType) {
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
	

	public void setImageUrl(String imageUrl, int imageType,
			int placeholderImageResId) {
		if (CacheManage.getInstance().getBitCounter()
				.containsKey(MD5.getMD5(imageUrl))) {
			setImageBitmap(CacheManage.getInstance().getBitCounter()
					.getBitmap(MD5.getMD5(imageUrl)));
			if (listener != null) {
				listener.onImageLoadComplete();
			}
		} else {
			setImageUrl(imageUrl, null, placeholderImageResId,
					placeholderImageResId, imageType);
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
		if (SystemUtils.getSystemVersion() >= 14) {
			setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		}
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
	@Override
	public void onBitmapLoaded(final RequestResponse response) {
		if (response.originalRequest.imageUrl.equals(pendingImageUrl)) {
			postToGuiThread(new Runnable() {
				public void run() {
					Bitmap bitmap = response.bitmapReference.get();
					if (bitmap != null && currentState != States.LOADED) {
						System.out.println("round  onBitmapLoaded  "
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
						// Animation anim = new AlphaAnimation(0.5f, 1.0f);
						// anim.setDuration(250);
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
	@Override
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

	public void onBitmapLoadCancelled() {
		currentState = States.CANCELLED;
		if (listener != null) {
			listener.onImageLoadCancelled();
		}
	}

	public final void postToGuiThread(Runnable runnable) {
		uiHandler.post(runnable);
	}

}