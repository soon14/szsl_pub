package com.bsoft.hospital.pub.suzhoumh.activity.app.healthtool;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bsoft.hospital.pub.suzhoumh.R;

/**
 * 设置次数的对话框
 * 
 * @author Administrator
 * 
 */
public class CustomDialog extends Dialog {

public CustomDialog(Context context) {
		super(context);
	}

public CustomDialog(Context context, int theme) {
		super(context, theme);
	}

public static class Builder {
		
		private Context context;
		private String title;
		private String positiveButtonText;
		private String negativeButtonText;
		private View contentView;
		private DialogInterface.OnClickListener positiveButtonClickListener;
		private DialogInterface.OnClickListener negativeButtonClickListener;
		public int times = 0;// 次数
		private MedicineRemindModel model;
		private Calendar c = Calendar.getInstance();
		List<String> alltime = new ArrayList<String>();
		List<String> alltimes = new ArrayList<String>();

		private List<TextView> tvlist = new ArrayList<TextView>();

		TextView textview_begintime1;
		TextView textview_begintime2;
		TextView textview_begintime3;
		TextView textview_begintime4;

		LinearLayout ll_time1;
		LinearLayout ll_time2;
		LinearLayout ll_time3;
		LinearLayout ll_time4;

		public Builder(Context context) {
			this.context = context;
		}

		public void setMedicinieWarnModel(MedicineRemindModel mymodel) {
			this.model = mymodel;
		}

		/**
		 * Set the Dialog title from resource
		 * 
		 * @param title
		 * @return
		 */
		public Builder setTitle(int title) {
			this.title = (String) context.getText(title);
			return this;
		}

		/**
		 * Set the Dialog title from String
		 * 
		 * @param title
		 * @return
		 */

		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}

		public Builder setContentView(View v) {
			this.contentView = v;
			return this;
		}

		/**
		 * Set the positive button resource and it's listener
		 * 
		 * @param positiveButtonText
		 * @return
		 */
		public Builder setPositiveButton(int positiveButtonText,
				DialogInterface.OnClickListener listener) {
			this.positiveButtonText = (String) context
					.getText(positiveButtonText);
			this.positiveButtonClickListener = listener;
			return this;
		}

		public Builder setPositiveButton(String positiveButtonText,
				DialogInterface.OnClickListener listener) {
			this.positiveButtonText = positiveButtonText;
			this.positiveButtonClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(int negativeButtonText,
				DialogInterface.OnClickListener listener) {
			this.negativeButtonText = (String) context
					.getText(negativeButtonText);
			this.negativeButtonClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(String negativeButtonText,
				DialogInterface.OnClickListener listener) {
			this.negativeButtonText = negativeButtonText;
			this.negativeButtonClickListener = listener;
			return this;
		}

		public CustomDialog create() {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// instantiate the dialog with the custom Theme
			final CustomDialog dialog = new CustomDialog(context,
					R.style.Dialog);
			final View layout = inflater.inflate(R.layout.dialog_normal_layout,
					null);
			dialog.addContentView(layout, new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			final TextView tv_title = (TextView) layout
					.findViewById(R.id.title);
			tv_title.setText(title);
			if (model.times != null && !model.times.equals("")) {
				times = Integer.parseInt(model.times);
			}
			if (model.timesde != null && !model.timesde.equals("")) {
				String timesde[] = model.timesde.split(",");
				for (int i = 0; i < timesde.length; i++) {
					alltimes.add(timesde[i]);
				}
			}

			textview_begintime1 = ((TextView) layout
					.findViewById(R.id.textview_begintime1));
			textview_begintime2 = ((TextView) layout
					.findViewById(R.id.textview_begintime2));
			textview_begintime3 = ((TextView) layout
					.findViewById(R.id.textview_begintime3));
			textview_begintime4 = ((TextView) layout
					.findViewById(R.id.textview_begintime4));

			tvlist.add(textview_begintime1);
			tvlist.add(textview_begintime2);
			tvlist.add(textview_begintime3);
			tvlist.add(textview_begintime4);

			ll_time1 = (LinearLayout) layout.findViewById(R.id.ll_time1);
			ll_time2 = (LinearLayout) layout.findViewById(R.id.ll_time2);
			ll_time3 = (LinearLayout) layout.findViewById(R.id.ll_time3);
			ll_time4 = (LinearLayout) layout.findViewById(R.id.ll_time4);

			ll_time1.setOnClickListener(new OnClickLitener());
			ll_time2.setOnClickListener(new OnClickLitener());
			ll_time3.setOnClickListener(new OnClickLitener());
			ll_time4.setOnClickListener(new OnClickLitener());

			showView(times, alltimes);

			// set the confirm button
			if (positiveButtonText != null) {
				((Button) layout.findViewById(R.id.positiveButton))
						.setText(positiveButtonText);
				if (positiveButtonClickListener != null) {
					((Button) layout.findViewById(R.id.positiveButton))
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									if (times == 1) {
										alltime.add(textview_begintime1
												.getText().toString());
									} else if (times == 2) {
										alltime.add(textview_begintime1
												.getText().toString());
										alltime.add(textview_begintime3
												.getText().toString());
									} else if (times == 3) {
										alltime.add(textview_begintime1
												.getText().toString());
										alltime.add(textview_begintime2
												.getText().toString());
										alltime.add(textview_begintime3
												.getText().toString());
									} else if (times == 4) {
										alltime.add(textview_begintime1
												.getText().toString());
										alltime.add(textview_begintime2
												.getText().toString());
										alltime.add(textview_begintime3
												.getText().toString());
										alltime.add(textview_begintime4
												.getText().toString());
									}

									// 设置选择的时间
									StringBuffer timede = new StringBuffer();
									for (int i = 0; i < alltime.size(); i++) {
										if (i < alltime.size() - 1) {
											timede.append(alltime.get(i) + ",");
										} else {
											timede.append(alltime.get(i));
										}
									}

									model.timesde = timede.toString();
									model.times = String.valueOf(times);

									positiveButtonClickListener.onClick(dialog,
											DialogInterface.BUTTON_POSITIVE);
								}
							});
				}
			} else {
				// if no confirm button just set the visibility to GONE
				layout.findViewById(R.id.positiveButton).setVisibility(
						View.GONE);
			}
			// set the cancel button
			if (negativeButtonText != null) {
				((Button) layout.findViewById(R.id.negativeButton))
						.setText(negativeButtonText);
				if (negativeButtonClickListener != null) {
					((Button) layout.findViewById(R.id.negativeButton))
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									negativeButtonClickListener.onClick(dialog,
											DialogInterface.BUTTON_NEGATIVE);
								}
							});
				}
			} else {
				// if no confirm button just set the visibility to GONE
				layout.findViewById(R.id.negativeButton).setVisibility(
						View.GONE);
			}
			dialog.setContentView(layout);
			return dialog;
		}

		private void showView(int time, List<String> arrtimes) {
			hideView();
			switch (time) {
			case 1:
				ll_time1.setVisibility(View.VISIBLE);
				break;
			case 2:
				ll_time1.setVisibility(View.VISIBLE);
				ll_time2.setVisibility(View.VISIBLE);
				break;
			case 3:
				ll_time1.setVisibility(View.VISIBLE);
				ll_time2.setVisibility(View.VISIBLE);
				ll_time3.setVisibility(View.VISIBLE);
				break;
			case 4:
				ll_time1.setVisibility(View.VISIBLE);
				ll_time2.setVisibility(View.VISIBLE);
				ll_time3.setVisibility(View.VISIBLE);
				ll_time4.setVisibility(View.VISIBLE);
				break;
			}
			if (arrtimes != null && arrtimes.size() > 0) {
				for (int i = 0; i < arrtimes.size(); i++) {
					tvlist.get(i).setText(arrtimes.get(i));
				}
			}
		}

		private void hideView() {
			ll_time1.setVisibility(View.GONE);
			ll_time2.setVisibility(View.GONE);
			ll_time3.setVisibility(View.GONE);
			ll_time4.setVisibility(View.GONE);
		}

		class OnClickLitener implements View.OnClickListener {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.ll_time1:
					editTime(textview_begintime1);
					break;
				case R.id.ll_time2:
					editTime(textview_begintime2);
					break;
				case R.id.ll_time3:
					editTime(textview_begintime3);
					break;
				case R.id.ll_time4:
					editTime(textview_begintime4);
					break;
				}
			}
		}

		/**
		 * 修改时间
		 * 
		 * @param tv
		 */
		private void editTime(final TextView tv) {
			c.setTimeInMillis(System.currentTimeMillis());
			int mHour = c.get(Calendar.HOUR_OF_DAY);
			int mMinute = c.get(Calendar.MINUTE);
			new TimePickerDialog(context,
					new TimePickerDialog.OnTimeSetListener() {
						@Override
						public void onTimeSet(TimePicker view, int hourOfDay,
								int minute) {
							c.setTimeInMillis(System.currentTimeMillis());
							c.set(Calendar.HOUR_OF_DAY, hourOfDay);
							c.set(Calendar.MINUTE, minute);
							c.set(Calendar.SECOND, 0); // 设为 0
							c.set(Calendar.MILLISECOND, 0); // 设为 0

							tv.setText((hourOfDay < 10 ? ("0" + hourOfDay)
									: hourOfDay)
									+ ":"
									+ (minute < 10 ? ("0" + minute) : minute));
						}
					}, mHour, mMinute, true).show();
		}

	}

}
