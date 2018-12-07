package com.bsoft.hospital.pub.suzhoumh.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bsoft.hospital.pub.suzhoumh.R;

import java.util.ArrayList;

/**
 * 包含列表的自定义Dialog
 *
 * Author: 嘿嘿抛物线
 * Email: 497635745@qq.com
 * Date: 2016-8-19 10:34
 */
public class SingleChoiceListDialog extends Dialog {

    private Context context;

    private TextView titleView;
    private ListView contentView;
    private TextView cancelView;

    private ArrayList<String> dataList;
    private ListDialogAdapter adapter;

    private String title;

    public SingleChoiceListDialog(Context context, String title, ArrayList<String> dataList) {
        this(context, R.style.alertDialogTheme);
        this.context = context;
        this.title = title;
        this.dataList = dataList;
    }

    private SingleChoiceListDialog(Context context) {
        super(context);
    }

    private SingleChoiceListDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    private SingleChoiceListDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_single_choice_list);
        setCancelable(false);

        findView();
        initView();
    }

    private void initView() {
        adapter = new ListDialogAdapter();
        contentView.setAdapter(adapter);
        contentView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onListItemClickListener.onItemClick(parent, view, position, id);
                dismiss();
            }
        });

        setTitleText(title);

        cancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void findView() {
        titleView = (TextView) findViewById(R.id.tv_title);
        contentView = (ListView) findViewById(R.id.lv_content);
        cancelView = (TextView) findViewById(R.id.tv_cancel);
    }

    private class ListDialogAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public String getItem(int position) {
            return dataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (null == convertView) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_single_choice_list_dialog, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.textView = (TextView) convertView.findViewById(R.id.tv);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.textView.setText(getItem(position));
            return convertView;
        }
    }

    class ViewHolder {
        TextView textView;
    }

    public interface OnListItemClickListener {
        void onItemClick(AdapterView<?> parent, View view, int position, long id);
    }

    private OnListItemClickListener onListItemClickListener;

    public void setOnListItemClickListener(OnListItemClickListener onListItemClickListener) {
        this.onListItemClickListener = onListItemClickListener;
    }

    private void setTitleText(String titleText) {
        if (titleText != null) {
            this.titleView.setText(titleText);
        }
    }

    private void setCancelText(String cancelText) {
        if (cancelText != null) {
            this.cancelView.setText(cancelText);
        }
    }
}
