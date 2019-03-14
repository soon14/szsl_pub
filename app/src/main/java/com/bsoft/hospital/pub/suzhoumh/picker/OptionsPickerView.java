package com.bsoft.hospital.pub.suzhoumh.picker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.picker.view.BasePickerView;
import com.bsoft.hospital.pub.suzhoumh.picker.view.WheelOptions;

import java.util.ArrayList;

/**
 * 条件选择器
 * Created by Sai on 15/11/22.
 */
public class OptionsPickerView<T> extends BasePickerView implements View.OnClickListener {
    private static final String TAG_CANCEL = "cancel";
    private static final String TAG_TITLE = "title";
    private static final String TAG_CONFIRM = "confirm";
    private static final String TAG_DIVIDER = "divider";

    private TextView mCancelTv;
    private TextView mTitleTv;
    private TextView mConfirmTv;
    private View mDividerView;

    WheelOptions<T> wheelOptions;
    private OnOptionsSelectListener optionsSelectListener;

    public OptionsPickerView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.picker_option, contentContainer);
        mCancelTv = (TextView) findViewById(R.id.tv_cancel);
        mTitleTv = (TextView) findViewById(R.id.tv_title);
        mConfirmTv = (TextView) findViewById(R.id.tv_confirm);
        mDividerView = findViewById(R.id.divider);
        mCancelTv.setTag(TAG_CANCEL);
        mCancelTv.setOnClickListener(this);
        mTitleTv.setTag(TAG_TITLE);
        mConfirmTv.setTag(TAG_CONFIRM);
        mConfirmTv.setOnClickListener(this);
        mDividerView.setTag(TAG_DIVIDER);
        final View optionLayout = findViewById(R.id.layout_option);
        wheelOptions = new WheelOptions(optionLayout);
    }

    public void setPicker(ArrayList<T> optionsItems) {
        wheelOptions.setPicker(optionsItems, null, null, false);
    }

    public void setPicker(ArrayList<T> options1Items,
                          ArrayList<ArrayList<T>> options2Items, boolean linkage) {
        wheelOptions.setPicker(options1Items, options2Items, null, linkage);
    }

    public void setPicker(ArrayList<T> options1Items,
                          ArrayList<ArrayList<T>> options2Items,
                          ArrayList<ArrayList<ArrayList<T>>> options3Items,
                          boolean linkage) {
        wheelOptions.setPicker(options1Items, options2Items, options3Items,
                linkage);
    }

    /**
     * 设置选中的item位置
     *
     * @param option1 位置
     */
    public void setSelectOptions(int option1) {
        wheelOptions.setCurrentItems(option1, 0, 0);
    }

    /**
     * 设置选中的item位置
     *
     * @param option1 位置
     * @param option2 位置
     */
    public void setSelectOptions(int option1, int option2) {
        wheelOptions.setCurrentItems(option1, option2, 0);
    }

    /**
     * 设置选中的item位置
     *
     * @param option1 位置
     * @param option2 位置
     * @param option3 位置
     */
    public void setSelectOptions(int option1, int option2, int option3) {
        wheelOptions.setCurrentItems(option1, option2, option3);
    }

    /**
     * 设置选项的单位
     *
     * @param label1 单位
     */
    public void setLabels(String label1) {
        wheelOptions.setLabels(label1, null, null);
    }

    /**
     * 设置选项的单位
     *
     * @param label1 单位
     * @param label2 单位
     */
    public void setLabels(String label1, String label2) {
        wheelOptions.setLabels(label1, label2, null);
    }

    /**
     * 设置选项的单位
     *
     * @param label1 单位
     * @param label2 单位
     * @param label3 单位
     */
    public void setLabels(String label1, String label2, String label3) {
        wheelOptions.setLabels(label1, label2, label3);
    }

    /**
     * 设置是否循环滚动
     *
     * @param cyclic 是否循环
     */
    public void setCyclic(boolean cyclic) {
        wheelOptions.setCyclic(cyclic);
    }

    public void setCyclic(boolean cyclic1, boolean cyclic2, boolean cyclic3) {
        wheelOptions.setCyclic(cyclic1, cyclic2, cyclic3);
    }


    @Override
    public void onClick(View v) {
        String tag = (String) v.getTag();
        if (tag.equals(TAG_CANCEL)) {
            dismiss();
        } else if (tag.equals(TAG_CONFIRM)) {
            if (optionsSelectListener != null) {
                int[] optionsCurrentItems = wheelOptions.getCurrentItems();
                optionsSelectListener.onOptionsSelect(optionsCurrentItems[0], optionsCurrentItems[1], optionsCurrentItems[2]);
            }
            dismiss();
        } else if (!tag.equals(TAG_TITLE) && !tag.equals(TAG_DIVIDER)) {
            dismiss();
        }
    }

    public interface OnOptionsSelectListener {
        void onOptionsSelect(int options1, int option2, int options3);
    }

    public void setOnOptionsSelectListener(
            OnOptionsSelectListener optionsSelectListener) {
        this.optionsSelectListener = optionsSelectListener;
    }

    public void setTitle(String title) {
        mTitleTv.setText(title);
    }
}
