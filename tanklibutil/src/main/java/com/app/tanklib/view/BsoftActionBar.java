package com.app.tanklib.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.tanklib.R;

public class BsoftActionBar extends RelativeLayout implements OnClickListener {

	private LayoutInflater mInflater;
	private RelativeLayout mBarView;
	private TextView titleTextView;
	private TextView refreshTextView;
	private ImageView backImageView;
	private ImageView refreshImageView;
	private ProgressBar titleRefresh;
	private ProgressBar progressBar;
	private LinearLayout actionsViews;
	private LinearLayout titleLayout;
	private ImageView arrow;
	private LinearLayout titleDoubleLayout;
	private TextView titleTopTextView, titleBottomTextView;

	public BsoftActionBar(Context context) {
		super(context);
		init(context);
	}

	public BsoftActionBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public BsoftActionBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	/**
	 * 初始化界面
	 * 
	 * @param context
	 */
	void init(Context context) {
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mBarView = (RelativeLayout) mInflater.inflate(R.layout.actionbar, null);
		addView(mBarView);
		titleTextView = (TextView) mBarView.findViewById(R.id.titleTextView);
		backImageView = (ImageView) mBarView.findViewById(R.id.backImageView);
		refreshImageView = (ImageView) mBarView
				.findViewById(R.id.refreshImageView);
		progressBar = (ProgressBar) mBarView.findViewById(R.id.progressBar);
		actionsViews = (LinearLayout) findViewById(R.id.actionbar_actions);
		titleRefresh = (ProgressBar) mBarView.findViewById(R.id.titleRefresh);
		refreshTextView = (TextView) mBarView
				.findViewById(R.id.refreshTextView);
		titleLayout = (LinearLayout) mBarView.findViewById(R.id.titleLayout);
		arrow = (ImageView) mBarView.findViewById(R.id.arrow);
		titleDoubleLayout = (LinearLayout) mBarView
				.findViewById(R.id.titleDoubleLayout);
		titleTopTextView = (TextView) mBarView
				.findViewById(R.id.titleTopTextView);
		titleBottomTextView = (TextView) mBarView
				.findViewById(R.id.titleBottomTextView);
	}

	public void setBackGround(int color) {
		mBarView.setBackgroundColor(color);
	}

	// 设置返回按钮事件-Action
	public void setBackAction(Action action) {
		backImageView.setOnClickListener(this);
		backImageView.setTag(action);
		backImageView.setImageResource(action.getDrawable());
		backImageView.setVisibility(View.VISIBLE);
	}

	public void setTitleText(int resId) {
		titleTextView.setText(resId);
		titleTextView.setVisibility(View.VISIBLE);
	}

	public void setTitleClick(CharSequence title,
			OnClickListener onClickListener) {
		titleTextView.setText(title);
		titleTextView.setVisibility(View.VISIBLE);
		arrow.setVisibility(View.VISIBLE);
		titleLayout.setOnClickListener(onClickListener);
	}

	public void setTitle(String topStr, String bottomStr) {
		titleTopTextView.setText(topStr);
		titleTopTextView.setVisibility(View.VISIBLE);
		titleBottomTextView.setText(bottomStr);
		titleBottomTextView.setVisibility(View.VISIBLE);
		titleDoubleLayout.setVisibility(View.VISIBLE);
	}

	public void changTitleArrow(int resId) {
		arrow.setImageResource(resId);
	}

	public void setTitle(CharSequence title) {
		titleTextView.setText(title);
		titleTextView.setVisibility(View.VISIBLE);
	}

	public void setTitle(CharSequence title, Action action) {
		titleTextView.setOnClickListener(this);
		titleTextView.setTag(action);
		titleTextView.setText(title);
		titleTextView.setVisibility(View.VISIBLE);
	}

	// 设置刷新按钮-刷新按钮图片有2种
	public void setRefreshImageView(int resId, Action action) {
		refreshImageView.setOnClickListener(this);
		refreshImageView.setTag(action);
		refreshImageView.setImageResource(resId);
		refreshImageView.setVisibility(View.VISIBLE);
	}

	public void setRefreshTextView(String text, Action action) {
		refreshTextView.setOnClickListener(this);
		refreshTextView.setTag(action);
		refreshTextView.setText(text);
		refreshTextView.setVisibility(View.VISIBLE);
	}

	// actionBar开始刷新
	public void startTitleRefresh() {
		titleRefresh.setVisibility(View.VISIBLE);
	}

	// actionBar停止刷新
	public void endTitleRefresh() {
		titleRefresh.setVisibility(View.GONE);
	}

	// actionBar开始刷新
	public void startImageRefresh() {
		refreshImageView.setVisibility(View.GONE);
		progressBar.setVisibility(View.VISIBLE);
	}

	// actionBar停止刷新
	public void endImageRefresh() {
		refreshImageView.setVisibility(View.VISIBLE);
		progressBar.setVisibility(View.GONE);
	}

	// actionBar开始刷新
	public void startTextRefresh() {
		refreshTextView.setVisibility(View.GONE);
		progressBar.setVisibility(View.VISIBLE);
	}

	// actionBar停止刷新
	public void endTextRefresh() {
		refreshTextView.setVisibility(View.VISIBLE);
		progressBar.setVisibility(View.GONE);
	}

	public void onlyStatrtRefresh() {
		progressBar.setVisibility(View.VISIBLE);
	}

	// actionBar停止刷新
	public void onlyEndRefresh() {
		progressBar.setVisibility(View.GONE);
	}

	// 添加Action
	public void addAction(Action action) {
		int index = actionsViews.getChildCount();
		actionsViews.addView(inflateAction(action), index);
	}

	public void startSearch() {

	}

	public void endSearch() {

	}

	public void actionShow(boolean flage) {
		if (flage) {
			actionsViews.setVisibility(View.VISIBLE);
		} else {
			actionsViews.setVisibility(View.INVISIBLE);
		}
	}

	// 改变状态
	public void changAction(int flg) {
		actionsViews.setVisibility(flg);
	}

	private View inflateAction(Action action) {
		View view = mInflater.inflate(R.layout.actionbar_item, actionsViews,
				false);

		ImageView labelView = (ImageView) view
				.findViewById(R.id.actionbar_item);
		labelView.setImageResource(action.getDrawable());

		view.setTag(action);
		view.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {
		doAction(v);
		// switch (v.getId()) {
		// case R.id.refreshImageView:
		// statrtRefresh();
		// break;
		// default:
		// doAction(v);
		// break;
		// }
	}

	public void doAction(View v) {
		Object tag = v.getTag();
		if (tag instanceof Action) {
			Action action = (Action) tag;
			action.performAction(v);
		}
	}

	public interface Action {
		public int getDrawable();

		public void performAction(View view);
	}

	// 扩充Action可继承AbstractAction，实现performAction
	public static abstract class AbstractAction implements Action {
		final private int mDrawable;

		public AbstractAction(int drawable) {
			mDrawable = drawable;
		}

		public int getDrawable() {
			return mDrawable;
		}
	}

	// 按钮点击之后的Intent跳转
	public static class IntentAction extends AbstractAction {
		private Context mContext;
		private Intent mIntent;

		public IntentAction(Context context, Intent intent, int drawable) {
			super(drawable);
			mContext = context;
			mIntent = intent;
		}

		@Override
		public void performAction(View view) {
			try {
				mContext.startActivity(mIntent);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
