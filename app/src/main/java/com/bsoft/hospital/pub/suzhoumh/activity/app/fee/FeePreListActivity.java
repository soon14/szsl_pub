package com.bsoft.hospital.pub.suzhoumh.activity.app.fee;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.tanklib.view.BsoftActionBar;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;

import java.util.ArrayList;

/**
 * 诊间支付
 * 待支付项目
 * 已支付项目
 * Created by Administrator on 2016/5/10.
 */
public class FeePreListActivity extends BaseActivity {
    private ListView listView;
    private MyAdapter adapter;
    private ProgressBar emptyProgress;
    private LayoutInflater mLayoutInflater;

    private ArrayList<String> list = new ArrayList<String>();
    public AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeprelist);

        this.mLayoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        findView();
        initData();
        setClick();

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.PAY_SUCCESS_ACTION);
        registerReceiver(receiver, filter);
    }

    private void setClick() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0: {
                        Intent intent = new Intent(baseContext, FeeListActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case 1: {
                        Intent intent = new Intent(baseContext, FeePayedActivity2.class);
                        startActivity(intent);
                        break;
                    }
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void findView() {
        findActionBar();
        actionBar.setTitle("掌上支付");
        actionBar.setBackAction(new BsoftActionBar.Action() {

            @Override
            public void performAction(View view) {
                back();
            }

            @Override
            public int getDrawable() {
                return R.drawable.btn_back;
            }
        });
        emptyProgress = (ProgressBar) findViewById(R.id.emptyProgress);
        listView = (ListView) findViewById(R.id.listView);
    }

    private void initData() {
        list = new ArrayList<String>();
        list.add("待支付项目");
        list.add("已支付项目");
        adapter = new MyAdapter();
        listView.setAdapter(adapter);
    }

    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(baseContext).inflate(R.layout.item_fee_pre, null);
                holder.tv_info = (TextView) convertView.findViewById(R.id.tv_info);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tv_info.setText(list.get(position));
            return convertView;
        }

        class ViewHolder {
            TextView tv_info;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.PAY_SUCCESS_ACTION)) {
                startActivity(new Intent(baseContext, FeePayedActivity2.class));
            }
        }
    };
}
