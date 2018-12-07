package com.bsoft.hospital.pub.suzhoumh.util;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.AppApplication;
import com.bsoft.hospital.pub.suzhoumh.activity.my.family.DialogListAdapter;

public class DialogUtil {

    public static Dialog builder;

    public static void showSelectDialog(Context context, String title, DialogListAdapter dialogAdapter, OnItemClickListener listener) {
        builder = new Dialog(context, R.style.alertDialogTheme);
        View viewDialog = LayoutInflater.from(context).inflate(R.layout.dialog_list,
                null);
        ListView listview = (ListView) viewDialog.findViewById(R.id.lv_content);
        TextView tv_title = (TextView) viewDialog.findViewById(R.id.tv_title);
        tv_title.setText(title);
        listview.setAdapter(dialogAdapter);
        listview.setOnItemClickListener(listener);
        // 设置对话框的宽高
        LayoutParams layoutParams = new LayoutParams(AppApplication
                .getWidthPixels() * 85 / 100, LayoutParams.WRAP_CONTENT);
        builder.setContentView(viewDialog, layoutParams);
        builder.show();
    }

    public static void close() {
        if (builder != null) {
            builder.dismiss();
        }
    }
}
