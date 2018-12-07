package com.bsoft.hospital.pub.suzhoumh.view.list;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.bsoft.hospital.pub.suzhoumh.R;

/**
 * 上拉加载更多
 * @author Administrator
 *
 */
public class ListViewExt extends ListView implements OnScrollListener {

	private final static String TAG = "ListViewExt";
	
	private HeaderView mHeaderView = null;
	private RelativeLayout mHeaderContent = null;
	private int iHeaderHeight = 0;
	
	private FooterView mFooterView = null;
	private RelativeLayout mFooterContent = null;
	private int iFooterHeight = 0;
	
	private final static int SCROLL_HEADER = 0;
	private final static int SCROLL_FOOTER = 1;
	private int iScrollWhich = SCROLL_HEADER;
	
	private Scroller mScroller = null;
	private final static float OFFSET_Y = 0.7f;
	private float iLastY = 0;
	private int mTotalNumber = 0;
	
	private Context context;
	
	private BaseAdapter adapter;
	
	public ListViewExt(Context context) {
		this(context, null, 0);
	}
	public ListViewExt(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	public ListViewExt(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private void initView(Context context){
		this.context = context;
		/*
		 * mScroller用来回弹下拉刷新/上拉更多
		 * 配合computerScroll来使用
		 */
		mScroller = new Scroller(context, new DecelerateInterpolator());
		super.setOnScrollListener(this);
		
		initHeaderView(context);
		initFooterView(context);
	}
	
	@Override
	public void setAdapter(ListAdapter adapter) {
		this.adapter = (BaseAdapter) adapter;
		if(getFooterViewsCount() == 0){
			addFooterView(mFooterView);
		}
		super.setAdapter(adapter);
	}
	
	public void addFooter(){
		if(getFooterViewsCount() == 0){
			addFooterView(mFooterView);
		}
	}
	
	/*@Override
	public boolean onTouchEvent(MotionEvent ev) {
		
		switch(ev.getAction()){
		case MotionEvent.ACTION_DOWN:
			iLastY = ev.getY();
			break;
			
		case MotionEvent.ACTION_MOVE:
			float deltaY = ev.getY() - iLastY;
			iLastY = ev.getY();
			if(canHeaderPull() && getFirstVisiblePosition() == 0 &&
			   (deltaY > 0 || mHeaderView.getHeaderHeight() > 0)){
				updateHeaderState(deltaY * OFFSET_Y);
			}else if(canFooterPull() && getLastVisiblePosition() == mTotalNumber - 1
					&& (deltaY < 0 || mFooterView.getFooterHeight() > 0)){
				updateFooterState(-deltaY * OFFSET_Y);
			}
			break;
			
		case MotionEvent.ACTION_UP:
			if(getFirstVisiblePosition() == 0){
				if(mHeaderView.getHeaderHeight() > iHeaderHeight){
				   mHeaderView.setHeaderState(HeaderView.STATE_REFRESHING);
				}
				resetHeader();
			}else if(getLastVisiblePosition() == mTotalNumber - 1){
				if(mFooterView.getFooterHeight() > iFooterHeight){
					mFooterView.setFooterState(FooterView.STATE_LOADING);
				}
				resetFooter();
				loadMoreDate();
			}
			break;
		
		default:
			break;
		}
		return super.onTouchEvent(ev);
	}*/
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		return super.onTouchEvent(ev);
	}
	
	public void doActionDown(float mLasty){
		iLastY = mLasty;
	}
	
	public void doActionMove(float mLasty,boolean hasFooter){
		float deltaY = mLasty - iLastY;
		iLastY = mLasty;
		if(canHeaderPull() && getFirstVisiblePosition() == 0 &&
		   (deltaY > 0 || mHeaderView.getHeaderHeight() > 0)){
			updateHeaderState(deltaY * OFFSET_Y);
		}else if(hasFooter && canFooterPull() && getLastVisiblePosition() == mTotalNumber - 1
				&& (deltaY < 0 || mFooterView.getFooterHeight() > 0)){
			updateFooterState(-deltaY * OFFSET_Y);
		}
	}
	
	public int doActionUp(boolean hasFooter){
		if(getFirstVisiblePosition() == 0){
			if(mHeaderView.getHeaderHeight() > iHeaderHeight){
			   mHeaderView.setHeaderState(HeaderView.STATE_REFRESHING);
			}
			resetHeader();
			return 1;
		}else if(hasFooter && getLastVisiblePosition() == mTotalNumber - 1){
			if(mFooterView.getFooterHeight() > iFooterHeight){
				mFooterView.setFooterState(FooterView.STATE_LOADING);
			}
			resetFooter();
			return 2;
		}
		return 0;
	}

	@Override
	public void computeScroll() {
		if(mScroller.computeScrollOffset()){
			if(iScrollWhich == SCROLL_HEADER){
				mHeaderView.setHeaderHeight(mScroller.getCurrY());
			}else if(iScrollWhich == SCROLL_FOOTER){
				mFooterView.setFooterHeight(mScroller.getCurrY());
			}
		}
		super.computeScroll();
	}
	
	/*
	 * 获取ListView有多少个item:
	 * 1. 在init中，需要设置super.setOnScrollListener;
	 * 2. 重载以下两个函数;
	 * 3. 在onScroll中取得totalItemCount即可;
	 */
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		mTotalNumber = totalItemCount;
	}
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}
	/////////////////////////////////////////////////////////////////////////////
	private boolean canHeaderPull(){
		if(mFooterView.getCurrentState() == FooterView.STATE_NORMAL){
			return true;
		}
		return false;
	}
	
	private boolean canFooterPull(){
		if(mHeaderView.getCurrentState() == HeaderView.STATE_NORMAL){
			return true;
		}
		return false;
	}
	///////////////////////////////////// Header ////////////////////////////////
	public void stopRefresh(){
		if(mHeaderView.getCurrentState() == HeaderView.STATE_REFRESHING){
			mHeaderView.setHeaderState(HeaderView.STATE_NORMAL);
			hideHeader();
		}
	}
	
	private void initHeaderView(Context context){
		mHeaderView = new HeaderView(context);
		mHeaderContent = (RelativeLayout) mHeaderView.findViewById(R.id.header_content);
		addHeaderView(mHeaderView);
		mHeaderView.getViewTreeObserver()
				   .addOnGlobalLayoutListener(new OnGlobalLayoutListener(){
			@Override
			public void onGlobalLayout() {
				iHeaderHeight = mHeaderContent.getHeight();
				getViewTreeObserver().removeGlobalOnLayoutListener(this);
			}
		});
	}
	
	private void updateHeaderState(float delta){
		mHeaderView.setHeaderHeight((int)(delta + mHeaderView.getHeaderHeight()));
		if(mHeaderView.getCurrentState() != HeaderView.STATE_REFRESHING){
			if(mHeaderView.getHeaderHeight() > iHeaderHeight){
				mHeaderView.setHeaderState(HeaderView.STATE_WILL_RELEASE);
			}else{
				mHeaderView.setHeaderState(HeaderView.STATE_NORMAL);
			}
		}
		setSelection(0);
	}
	
	private void resetHeader(){
		int height = mHeaderView.getHeaderHeight();
		if(height == 0){
			return;
		}

		int finalHeight = 0;
		if(height > iHeaderHeight){
			/*
			 * 如果超过HeaderView高度，则回滚到HeaderView高度即可
			 */
			finalHeight = iHeaderHeight;
		}else if(mHeaderView.getCurrentState() == HeaderView.STATE_REFRESHING){
			/*
			 * 如果HeaderView未完全显示
			 * 1. 处于正在刷新中，则不管;
			 * 2. 回滚HeaderView当前可视高度
			 */
			return;
		}
		
		iScrollWhich = SCROLL_HEADER;
		mScroller.startScroll(0, height, 0, finalHeight - height, 250);
		invalidate();
	}
	
	private void hideHeader(){
		iScrollWhich = SCROLL_HEADER;
		mScroller.startScroll(0, iHeaderHeight, 0, -iHeaderHeight, 250);
		invalidate();
	}
	/////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////// Footer ////////////////////////////////
	public void stopLoad(){
		if(mFooterView.getCurrentState() == FooterView.STATE_LOADING){
			mFooterView.setFooterState(FooterView.STATE_NORMAL);
			hideFooter();
		}
	}
	
	private void initFooterView(Context context){
		mFooterView = new FooterView(context);
		mFooterContent = (RelativeLayout) mFooterView.findViewById(R.id.footer_content);
		mFooterView.getViewTreeObserver()
				   .addOnGlobalLayoutListener(new OnGlobalLayoutListener(){
			@Override
			public void onGlobalLayout() {
				iFooterHeight = mFooterContent.getHeight();
				getViewTreeObserver().removeGlobalOnLayoutListener(this);
			}
		});
	}
	
	private void updateFooterState(float delta){
		
		mFooterView.setFooterHeight((int)(delta + mFooterView.getFooterHeight()));
		if(mFooterView.getCurrentState() != FooterView.STATE_LOADING){
			if(mFooterView.getFooterHeight() > iFooterHeight){
				mFooterView.setFooterState(FooterView.STATE_WILL_RELEASE);
			}else{
				mFooterView.setFooterState(FooterView.STATE_NORMAL);
			}
		}
	}
	
	private void resetFooter(){
		int height = mFooterView.getFooterHeight();
		if(height == 0){
			return;
		}
		
		int finalHeight = 0;
		if(height > iFooterHeight){
			finalHeight = iFooterHeight;
		}else if(mFooterView.getCurrentState() == FooterView.STATE_LOADING){
			return;
		}

		iScrollWhich = SCROLL_FOOTER;
		mScroller.startScroll(0, height, 0, finalHeight - height, 250);
		invalidate();
	}
	
	private void hideFooter(){
		iScrollWhich = SCROLL_HEADER;
		Log.d(TAG, "iFooterHeight:"+iFooterHeight);
		mScroller.startScroll(0, iFooterHeight, 0, -iFooterHeight, 250);
		invalidate();
		resetFooter();
	}
	
	public void removeFooter(){
		removeFooterView(mFooterView);
	}
	
	public void removeHeaderView()
	{
		removeHeaderView(mHeaderView);
	}
	/////////////////////////////////////////////////////////////////////////////
}
