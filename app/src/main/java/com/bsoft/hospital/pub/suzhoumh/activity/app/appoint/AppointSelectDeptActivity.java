package com.bsoft.hospital.pub.suzhoumh.activity.app.appoint;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.util.AsyncTaskUtil;
import com.app.tanklib.view.BsoftActionBar.Action;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.appoint.SelectDoctorOrDateActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.appointment.AppointSelectActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.app.appoint.AppointDeptVo;
import com.bsoft.hospital.pub.suzhoumh.util.PinyinUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 选择科室
 *
 * @author Administrator
 */
public class AppointSelectDeptActivity extends BaseActivity {

    private ListView lv_dept;
    private ArrayList<AppointDeptVo> list;
    private DeptAdapter adapter;
    private GetDataTask task;

    private int type = 0;//2普通挂号，1专家挂号

    private int[] bg_dept_text = new int[]{R.drawable.bg_dept_text_1, R.drawable.bg_dept_text_2, R.drawable.bg_dept_text_3};

    private EditText et_search;
    private ImageView ib_search_clear;
    private ArrayList<AppointDeptVo> searchlist = new ArrayList<AppointDeptVo>();

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.ACTION_APPOINT_CLOSE)) {
                finish();
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_dept);
        findView();
        initView();
        initData();
    }

    @Override
    public void findView() {
        findActionBar();
        actionBar.setTitle("选择科室");
        actionBar.setBackAction(new Action() {

            @Override
            public int getDrawable() {
                return R.drawable.btn_back;
            }

            @Override
            public void performAction(View view) {
                finish();
            }

        });
    }

    private void initView() {
        lv_dept = (ListView) findViewById(R.id.lv_dept);
        et_search = (EditText) findViewById(R.id.et_search);
        ib_search_clear = (ImageView) findViewById(R.id.ib_search_clear);
    }

    private void initData() {
        type = getIntent().getIntExtra("type", 0);
        list = new ArrayList<AppointDeptVo>();
        adapter = new DeptAdapter(list);
        lv_dept.setAdapter(adapter);
        task = new GetDataTask();
        task.execute();

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.ACTION_APPOINT_CLOSE);
        this.registerReceiver(receiver, filter);

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = s.toString();
                if (content != null && !content.equals("")) {
                    searchlist.clear();
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).ksmc.contains(content)) {
                            searchlist.add(list.get(i));
                        }
                    }
                    adapter.refresh(searchlist);
                } else {
                    adapter.refresh(list);
                }
            }
        });

        ib_search_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_search.getText() != null && !et_search.getText().toString().equals("")) {
                    et_search.setText("");
                    adapter.refresh(list);
                }
            }
        });
    }


    @SuppressLint("StaticFieldLeak")
    @SuppressWarnings("unchecked")
    class GetDataTask extends AsyncTask<Void, Void, ResultModel<ArrayList<AppointDeptVo>>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            actionBar.startTextRefresh();
        }

        @Override
        protected ResultModel<ArrayList<AppointDeptVo>> doInBackground(Void... params) {
            HashMap<String, String> map = new HashMap<String, String>();
            if (type == 2) {
                //普通挂号
                map.put("method", "listghkspt");
            } else if (type == 1) {
                //专家
                map.put("method", "listghks");
            } else if (type == 3) {
                //专科
                map.put("method", "listghkszk");
            }
            return HttpApi.getInstance().parserArray_His(AppointDeptVo.class,
                    "hiss/ser", map,
                    new BsoftNameValuePair("id", loginUser.id),
                    new BsoftNameValuePair("sn", loginUser.sn));
        }

        @Override
        protected void onPostExecute(ResultModel<ArrayList<AppointDeptVo>> result) {
            super.onPostExecute(result);
            actionBar.endTextRefresh();
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    if (null != result.list && result.list.size() > 0) {
                        list = result.list;
                        adapter.refresh(list);
                        setHanyuPinyin(list);
                    }
                } else {
                    result.showToast(baseContext);
                }
            } else {
                Toast.makeText(baseContext, "数据为空", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private void setHanyuPinyin(ArrayList<AppointDeptVo> list) {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).pinyin = PinyinUtil.getPingYin(list.get(i).ksmc);
        }
    }

    class DeptAdapter extends BaseAdapter {
        private ArrayList<AppointDeptVo> mList;

        public DeptAdapter(ArrayList<AppointDeptVo> list) {
            mList = list;
        }

        public void refresh(ArrayList<AppointDeptVo> list) {
            mList = list;
            adapter.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
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
                convertView = LayoutInflater.from(baseContext).inflate(R.layout.select_dept_item, parent, false);
                holder.tv_dept = (TextView) convertView.findViewById(R.id.tv_dept);
                holder.tv_first_name = (TextView) convertView.findViewById(R.id.tv_first_name);
                holder.btn_see_deptinfo = (Button) convertView.findViewById(R.id.btn_see_doctinfo);
                holder.rl_item = (RelativeLayout) convertView.findViewById(R.id.rl_item);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv_dept.setText(mList.get(position).ksmc);
            holder.tv_first_name.setText(mList.get(position).ksmc.substring(0, 1));
            holder.tv_first_name.setBackgroundResource(bg_dept_text[position % 3]);
            holder.btn_see_deptinfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AppointSelectDeptActivity.this, AppointDeptInfoActivity.class);
                    intent.putExtra("ksdm", mList.get(position).ksdm);
                    intent.putExtra("type", type);
                    startActivity(intent);
                }
            });

            holder.rl_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = null;
                    if (type == 2) {
                        //普通挂号
                        intent = new Intent(AppointSelectDeptActivity.this, AppointSelectDateActivity.class);
                        intent.putExtra("dept", mList.get(position));
                        intent.putExtra("type", 2);
                        startActivity(intent);
                    } else if (type == 3) {
                        //专科挂号
                        if ("1".equals(mList.get(position).zjks)){
                            //专家
                            intent = new Intent(AppointSelectDeptActivity.this, SelectDoctorOrDateActivity.class);
                            intent.putExtra("type", 1);
                        }else if ("0".equals(mList.get(position).zjks)){
                            //普通
                            intent = new Intent(AppointSelectDeptActivity.this, AppointSelectDateActivity.class);
                            intent.putExtra("type", 3);
                        }
                        intent.putExtra("dept", mList.get(position));
                        startActivity(intent);
                    } else if (type == 1) {
                        //专家挂号
//                        Intent intent = new Intent(AppointSelectDeptActivity.this, AppointSelectDoctorActivity.class);
                        intent = new Intent(AppointSelectDeptActivity.this, SelectDoctorOrDateActivity.class);
                        intent.putExtra("dept", mList.get(position));
                        startActivity(intent);
                    }
                }
            });
            return convertView;
        }

        class ViewHolder {
            TextView tv_dept;
            TextView tv_first_name;
            TextView btn_see_deptinfo;
            RelativeLayout rl_item;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AsyncTaskUtil.cancelTask(task);
        if (receiver != null) {
            this.unregisterReceiver(receiver);
        }
    }


}
