package com.app.tanklib.searchbox;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.app.tanklib.R;
import com.app.tanklib.searchbox.MaterialMenuDrawable.IconState;

import java.util.List;

public class SearchBox extends RelativeLayout {

	public static final int VOICE_RECOGNITION_CODE = 1234;

	private MaterialMenuView materialMenu;
	private EditText search;
	private Context context;
	private ListView results;
	private SearchAdapter adapter;
	private boolean searchOpen;
	private View tint;
	private boolean isMic;
	private ImageView mic;
	private SearchListener listener;
	private MenuListener menuListener;
	private FrameLayout rootLayout;
	private ProgressBar pb;

	private boolean isVoiceRecognitionIntentSupported;
	private VoiceRecognitionListener voiceRecognitionListener;
	private Activity mContainerActivity;
	private Fragment mContainerFragment;
	private android.support.v4.app.Fragment mContainerSupportFragment;

	/**
	 * Create a new searchbox
	 * 
	 * @param context
	 *            Context
	 */
	public SearchBox(Context context) {
		this(context, null);
	}

	/**
	 * Create a searchbox with params
	 * 
	 * @param context
	 *            Context
	 * @param attrs
	 *            Attributes
	 */
	public SearchBox(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	/**
	 * Create a searchbox with params and a style
	 * 
	 * @param context
	 *            Context
	 * @param attrs
	 *            Attributes
	 * @param defStyle
	 *            Style
	 */
	public SearchBox(final Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		inflate(context, R.layout.searchbox, this);
		this.searchOpen = false;
		this.isMic = true;
		this.materialMenu = (MaterialMenuView) findViewById(R.id.material_menu_button);
		this.search = (EditText) findViewById(R.id.search);
		this.results = (ListView) findViewById(R.id.results);
		this.results.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				InputMethodManager inputMethodManager = (InputMethodManager) context
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				inputMethodManager.hideSoftInputFromWindow(
						getApplicationWindowToken(), 0);
				return false;
			}
		});
		this.context = context;
		this.pb = (ProgressBar) findViewById(R.id.pb);
		this.mic = (ImageView) findViewById(R.id.mic);
		materialMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (searchOpen) {
					toggleSearch();
				} else {
					if (menuListener != null) {
						menuListener.onMenuClick();
					}
				}
			}

		});
		isVoiceRecognitionIntentSupported = isIntentAvailable(context,
				new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH));
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			RelativeLayout searchRoot = (RelativeLayout) findViewById(R.id.search_root);
			LayoutTransition lt = new LayoutTransition();
			lt.setDuration(100);
			searchRoot.setLayoutTransition(lt);
		}
		search.setOnEditorActionListener(new OnEditorActionListener() {
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					search(getSearchText());
					return true;
				}
				return false;
			}
		});
		search.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
					if (TextUtils.isEmpty(getSearchText())) {
						toggleSearch();
					} else {
						search(getSearchText());
					}
					return true;
				}
				return false;
			}
		});
		micStateChanged();
		mic.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (voiceRecognitionListener != null) {
					voiceRecognitionListener.onClick();
				} else {
					micClick();
				}
			}
		});
	}

	public boolean searchOpen() {
		return searchOpen;
	}

	public void search(String text) {
		if (listener != null) {
			listener.onSearch(text);
		}
	}

	public void setSearchAdapter(SearchAdapter adapter) {
		this.adapter = adapter;
		results.setAdapter(adapter);
	}

	private static boolean isIntentAvailable(Context context, Intent intent) {
		PackageManager mgr = context.getPackageManager();
		List<ResolveInfo> list = mgr.queryIntentActivities(intent,
				PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}

	/***
	 * Reveal the searchbox from a menu item. Specify the menu item id and pass
	 * the activity so the item can be found
	 * 
	 * @param id
	 *            View ID
	 * @param activity
	 *            Activity
	 */
	public void revealFromMenuItem(int id, Activity activity) {
		setVisibility(View.VISIBLE);
		View menuButton = activity.findViewById(id);
		if (menuButton != null) {
			FrameLayout layout = (FrameLayout) activity.getWindow()
					.getDecorView().findViewById(android.R.id.content);
			if (layout.findViewWithTag("searchBox") == null) {
				int[] location = new int[2];
				menuButton.getLocationInWindow(location);
				revealFrom((float) location[0], (float) location[1], activity,
						this);
			}
		}
	}

	/***
	 * Hide the searchbox using the circle animation. Can be called regardless
	 * of result list length
	 * 
	 * @param activity
	 *            Activity
	 */
	public void hideCircularly(Activity activity) {
		search.setText("");
		Display display = activity.getWindowManager().getDefaultDisplay();
		Point size = new Point();
		final FrameLayout layout = (FrameLayout) activity.getWindow()
				.getDecorView().findViewById(android.R.id.content);
		RelativeLayout root = (RelativeLayout) findViewById(R.id.search_root);
		display.getSize(size);
		Resources r = getResources();
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 96,
				r.getDisplayMetrics());
		int cx = layout.getLeft() + layout.getRight();
		int cy = layout.getTop();
		int finalRadius = (int) Math.max(layout.getWidth() * 1.5, px);

		SupportAnimator animator = ViewAnimationUtils.createCircularReveal(
				root, cx, cy, 0, finalRadius);
		animator.setInterpolator(new ReverseInterpolator());
		animator.setDuration(500);
		animator.start();
		animator.addListener(new SupportAnimator.AnimatorListener() {

			@Override
			public void onAnimationStart() {

			}

			@Override
			public void onAnimationEnd() {
				setVisibility(View.GONE);
			}

			@Override
			public void onAnimationCancel() {

			}

			@Override
			public void onAnimationRepeat() {

			}

		});
	}

	/***
	 * Toggle the searchbox's open/closed state manually
	 */
	public void toggleSearch() {
		if (searchOpen) {
			closeSearch();
		} else {
			openSearch(true);
		}
		searchOpen = !searchOpen;
	}

	/***
	 * Hide the search results manually
	 */
	public void hideResults() {
		this.search.setVisibility(View.GONE);
		this.results.setVisibility(View.GONE);
	}

	/***
	 * Start the voice input activity manually
	 */
	public void startVoiceRecognition() {
		if (isMicEnabled()) {
			Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
			intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
					RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
			intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now");
			if (mContainerActivity != null) {
				mContainerActivity.startActivityForResult(intent,
						VOICE_RECOGNITION_CODE);
			} else if (mContainerFragment != null) {
				mContainerFragment.startActivityForResult(intent,
						VOICE_RECOGNITION_CODE);
			} else if (mContainerSupportFragment != null) {
				mContainerSupportFragment.startActivityForResult(intent,
						VOICE_RECOGNITION_CODE);
			}
		}
	}

	/***
	 * Enable voice recognition for Activity
	 * 
	 * @param context
	 *            Context
	 */
	public void enableVoiceRecognition(Activity context) {
		mContainerActivity = context;
		micStateChanged();
	}

	/***
	 * Enable voice recognition for Fragment
	 * 
	 * @param context
	 *            Fragment
	 */
	public void enableVoiceRecognition(Fragment context) {
		mContainerFragment = context;
		micStateChanged();
	}

	/***
	 * Enable voice recognition for Support Fragment
	 * 
	 * @param context
	 *            Fragment
	 */
	public void enableVoiceRecognition(android.support.v4.app.Fragment context) {
		mContainerSupportFragment = context;
		micStateChanged();
	}

	private boolean isMicEnabled() {
		return isVoiceRecognitionIntentSupported
				&& (mContainerActivity != null
						|| mContainerSupportFragment != null || mContainerFragment != null);
	}

	private void micStateChanged() {
		mic.setVisibility((!isMic || isMicEnabled()) ? VISIBLE : INVISIBLE);
	}

	private void micStateChanged(boolean isMic) {
		this.isMic = isMic;
		micStateChanged();
	}

	/***
	 * Set whether to show the progress bar spinner
	 * 
	 * @param show
	 *            Whether to show
	 */

	public void showLoading(boolean show) {
		if (show) {
			pb.setVisibility(View.VISIBLE);
			mic.setVisibility(View.INVISIBLE);
		} else {
			pb.setVisibility(View.INVISIBLE);
			mic.setVisibility(View.VISIBLE);
		}
	}

	/***
	 * Mandatory method for the onClick event
	 */
	public void micClick() {
		if (!isMic) {
			setSearchString("");
		} else {
			startVoiceRecognition();
		}

	}

	/***
	 * Set whether the menu button should be shown. Particularly useful for apps
	 * that adapt to screen sizes
	 * 
	 * @param visibility
	 *            Whether to show
	 */

	public void setMenuVisibility(int visibility) {
		materialMenu.setVisibility(visibility);
	}

	/***
	 * Set the menu listener
	 * 
	 * @param menuListener
	 *            MenuListener
	 */
	public void setMenuListener(MenuListener menuListener) {
		this.menuListener = menuListener;
	}

	/***
	 * Set the search listener
	 * 
	 * @param listener
	 *            SearchListener
	 */
	public void setSearchListener(SearchListener listener) {
		this.listener = listener;
	}

	/***
	 * Set the maximum length of the searchbox's edittext
	 * 
	 * @param length
	 *            Length
	 */
	public void setMaxLength(int length) {
		search.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
				length) });
	}

	/***
	 * Get the searchbox's current text
	 * 
	 * @return Text
	 */
	public String getSearchText() {
		return search.getText().toString();
	}

	/***
	 * Set the searchbox's current text manually
	 * 
	 * @param text
	 *            Text
	 */
	public void setSearchString(String text) {
		search.setText(text);
	}

	private void revealFrom(float x, float y, Activity a, SearchBox s) {
		FrameLayout layout = (FrameLayout) a.getWindow().getDecorView()
				.findViewById(android.R.id.content);
		RelativeLayout root = (RelativeLayout) s.findViewById(R.id.search_root);
		Resources r = getResources();
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 96,
				r.getDisplayMetrics());
		int cx = layout.getLeft() + layout.getRight();
		int cy = layout.getTop();

		int finalRadius = (int) Math.max(layout.getWidth(), px);

		SupportAnimator animator = ViewAnimationUtils.createCircularReveal(
				root, cx, cy, 0, finalRadius);
		animator.setInterpolator(new AccelerateDecelerateInterpolator());
		animator.setDuration(500);
		animator.addListener(new SupportAnimator.AnimatorListener() {

			@Override
			public void onAnimationCancel() {

			}

			@Override
			public void onAnimationEnd() {
				toggleSearch();
			}

			@Override
			public void onAnimationRepeat() {

			}

			@Override
			public void onAnimationStart() {

			}

		});
		animator.start();
	}
	
	public void hideInput(){
		InputMethodManager inputMethodManager = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(getApplicationWindowToken(),
				0);
	}

	private void openSearch(Boolean openKeyboard) {
		this.materialMenu.animateState(IconState.ARROW);
		this.search.setVisibility(View.VISIBLE);
		search.requestFocus();
		this.results.setVisibility(View.VISIBLE);
		search.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				if (s.length() > 0) {
					micStateChanged(false);
					mic.setImageDrawable(context.getResources().getDrawable(
							R.drawable.ic_clear));
					adapter.filter(s.toString());
				} else {
					micStateChanged(true);
					mic.setImageDrawable(context.getResources().getDrawable(
							R.drawable.ic_action_mic));
					adapter.filter(null);
				}

				if (listener != null) {
					listener.onSearchTermChanged();
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

		});
		if (listener != null) {
			listener.onSearchOpened();
		}
		if (getSearchText().length() > 0) {
			micStateChanged(false);
			mic.setImageDrawable(context.getResources().getDrawable(
					R.drawable.ic_clear));
		}
		if (openKeyboard) {
			InputMethodManager inputMethodManager = (InputMethodManager) context
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputMethodManager.toggleSoftInputFromWindow(
					getApplicationWindowToken(),
					InputMethodManager.SHOW_FORCED, 0);
		}
	}

	private void closeSearch() {
		this.materialMenu.animateState(IconState.BURGER);
		this.search.setVisibility(View.GONE);
		this.results.setVisibility(View.GONE);
		if (tint != null && rootLayout != null) {
			rootLayout.removeView(tint);
		}
		if (listener != null) {
			listener.onSearchClosed();
		}
		micStateChanged(true);
		mic.setImageDrawable(context.getResources().getDrawable(
				R.drawable.ic_action_mic));
		InputMethodManager inputMethodManager = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(getApplicationWindowToken(),
				0);
	}

	// class SearchAdapter extends ArrayAdapter<SearchResult> {
	// public SearchAdapter(Context context, ArrayList<SearchResult> options) {
	// super(context, 0, options);
	// }
	//
	// int count = 0;
	//
	// @Override
	// public View getView(int position, View convertView, ViewGroup parent) {
	// SearchResult option = getItem(position);
	// if (convertView == null) {
	// convertView = LayoutInflater.from(getContext()).inflate(
	// R.layout.search_option, parent, false);
	//
	// if (animate) {
	// Animation anim = AnimationUtils.loadAnimation(context,
	// R.anim.anim_down);
	// anim.setDuration(400);
	// convertView.startAnimation(anim);
	// if (count == this.getCount()) {
	// animate = false;
	// }
	// count++;
	// }
	// }
	//
	// View border = convertView.findViewById(R.id.border);
	// if (position == 0) {
	// border.setVisibility(View.VISIBLE);
	// } else {
	// border.setVisibility(View.GONE);
	// }
	// final TextView title = (TextView) convertView
	// .findViewById(R.id.title);
	// title.setText(option.title);
	// ImageView icon = (ImageView) convertView.findViewById(R.id.icon);
	// icon.setImageDrawable(option.icon);
	// ImageView up = (ImageView) convertView.findViewById(R.id.up);
	// up.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// setSearchString(title.getText().toString());
	// search.setSelection(search.getText().length());
	// }
	//
	// });
	//
	// return convertView;
	// }
	// }

	public interface SearchListener {
		/**
		 * Called when the searchbox is opened
		 */
		public void onSearchOpened();

		/**
		 * Called when the clear button is pressed
		 */
		public void onSearchCleared();

		/**
		 * Called when the searchbox is closed
		 */
		public void onSearchClosed();

		/**
		 * Called when the searchbox's edittext changes
		 */
		public void onSearchTermChanged();

		/**
		 * Called when a search happens, with a result
		 * 
		 * @param result
		 */
		public void onSearch(String result);
	}

	public interface MenuListener {
		/**
		 * Called when the menu button is pressed
		 */
		public void onMenuClick();
	}

	public interface VoiceRecognitionListener {
		/**
		 * Called when the menu button is pressed
		 */
		public void onClick();
	}

}
