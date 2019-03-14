package com.bsoft.hospital.pub.suzhoumh.picker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.picker.view.BasePickerView;
import com.bsoft.hospital.pub.suzhoumh.picker.view.TimeWheelView;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间选择器
 * Created by Sai on 15/11/22.
 */
public class TimePickerView extends BasePickerView implements View.OnClickListener {
    public enum Type {
        ALL, YEAR_MONTH_DAY, HOURS_MINS, MONTH_DAY_HOUR_MIN, YEAR_MONTH
    }// 四种选择模式，年月日时分，年月日，时分，月日时分

    private static final String TAG_CANCEL = "cancel";
    private static final String TAG_TITLE = "title";
    private static final String TAG_CONFIRM = "confirm";
    private static final String TAG_DIVIDER = "divider";

    private TextView mCancelTv;
    private TextView mTitleTv;
    private TextView mConfirmTv;
    private View mDividerView;
    private LinearLayout mTimeLayout;
    private TimeWheelView mTimeWheelView;

    private OnTimeSelectListener timeSelectListener;

    public TimePickerView(Context context, Type type) {
        super(context);

        LayoutInflater.from(context).inflate(R.layout.picker_time, contentContainer);
        mCancelTv = (TextView) findViewById(R.id.tv_cancel);
        mTitleTv = (TextView) findViewById(R.id.tv_title);
        mConfirmTv = (TextView) findViewById(R.id.tv_confirm);
        mDividerView = findViewById(R.id.divider);
        mTimeLayout = (LinearLayout) findViewById(R.id.layout_time);
        mCancelTv.setTag(TAG_CANCEL);
        mCancelTv.setOnClickListener(this);
        mTitleTv.setTag(TAG_TITLE);
        mConfirmTv.setTag(TAG_CONFIRM);
        mConfirmTv.setOnClickListener(this);
        mDividerView.setTag(TAG_DIVIDER);
        mTimeWheelView = new TimeWheelView(mTimeLayout, type);

        //默认选中当前时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        mTimeWheelView.setPicker(year, month, day, hours, minute);
    }

    /**
     * 设置可以选择的时间范围
     * 要在setTime之前调用才有效果
     *
     * @param startYear 开始年份
     * @param endYear   结束年份
     */
    public void setRange(int startYear, int endYear) {
        mTimeWheelView.setStartYear(startYear);
        mTimeWheelView.setEndYear(endYear);
    }

    /**
     * 设置选中时间
     *
     * @param date 时间
     */
    public void setTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date == null)
            calendar.setTimeInMillis(System.currentTimeMillis());
        else
            calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        mTimeWheelView.setPicker(year, month, day, hours, minute);
    }

//    /**
//     * 指定选中的时间，显示选择器
//     *
//     * @param date
//     */
//    public void show(Date date) {
//        Calendar calendar = Calendar.getInstance();
//        if (date == null)
//            calendar.setTimeInMillis(System.currentTimeMillis());
//        else
//            calendar.setTime(date);
//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH);
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
//        int hours = calendar.get(Calendar.HOUR_OF_DAY);
//        int minute = calendar.get(Calendar.MINUTE);
//        wheelTime.setPicker(year, month, day, hours, minute);
//        show();
//    }

    /**
     * 设置是否循环滚动
     *
     * @param cyclic 是否循环
     */
    public void setCyclic(boolean cyclic) {
        mTimeWheelView.setCyclic(cyclic);
    }

    @Override
    public void onClick(View v) {
        String tag = (String) v.getTag();
        if (tag.equals(TAG_CANCEL)) {
            dismiss();
        } else if (tag.equals(TAG_CONFIRM)) {
            if (timeSelectListener != null) {
                try {
                    Date date = TimeWheelView.dateFormat.parse(mTimeWheelView.getTime());
                    timeSelectListener.onTimeSelect(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            dismiss();
        } else if (!tag.equals(TAG_TITLE) && !tag.equals(TAG_DIVIDER)) {
            dismiss();
        }
    }

    public interface OnTimeSelectListener {
        void onTimeSelect(Date date);
    }

    public void setOnTimeSelectListener(OnTimeSelectListener timeSelectListener) {
        this.timeSelectListener = timeSelectListener;
    }

    public void setTitle(String title) {
        mTitleTv.setText(title);
    }
}
