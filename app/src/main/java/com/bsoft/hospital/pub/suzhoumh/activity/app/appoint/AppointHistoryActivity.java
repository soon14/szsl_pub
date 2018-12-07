package com.bsoft.hospital.pub.suzhoumh.activity.app.appoint;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.view.BsoftActionBar.Action;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.NullModel;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.app.appoint.AppointInfoVo;
import com.bsoft.hospital.pub.suzhoumh.model.my.MyFamilyVo;
import com.bsoft.hospital.pub.suzhoumh.model.my.PersonVo;
import com.bsoft.hospital.pub.suzhoumh.util.DateUtil;

/**
 * 预约记录
 *
 * @author Administrator
 */
public class AppointHistoryActivity extends BaseActivity implements OnClickListener {

    private ListView lv_history;

    private RelativeLayout rl_1;//当前预约
    private RelativeLayout rl_2;//历史预约
    private TextView tv_1;
    private TextView tv_2;
    private ImageView iv_1;
    private ImageView iv_2;

    private ArrayList<AppointInfoVo> list = new ArrayList<AppointInfoVo>();
    private ArrayList<AppointInfoVo> currentlist = new ArrayList<AppointInfoVo>();
    private AppointAdapter adapter;
    private GetDataTask getDatatask;
    private DelDataTask delDatatask;
    private int delposition = 0;

    //private int currentState = "";//
    private String currentState = "";//预约状态 未取号，已取号，已1是已预约 0是取消的预约退号

    //private String sfzh = "";//身份证号
    private MyFamilyVo familyvo;//家庭成员
    private String sjd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.appoint_history);
        findView();
        initData();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.rl_1://已预约记录1
                tv_1.setTextColor(getResources().getColor(R.color.blue));
                iv_1.setVisibility(View.VISIBLE);
                tv_2.setTextColor(getResources().getColor(R.color.black));
                iv_2.setVisibility(View.INVISIBLE);
                currentState = "未取号";
                handleData();
                break;
            case R.id.rl_2://已取消预约记录0
                tv_2.setTextColor(getResources().getColor(R.color.blue));
                iv_2.setVisibility(View.VISIBLE);
                tv_1.setTextColor(getResources().getColor(R.color.black));
                iv_1.setVisibility(View.INVISIBLE);
                currentState = "已退号";
                handleData();
                break;
        }
    }

    /**
     * 处理区分预约成功的和取消的预约
     *
     * @param
     * @param
     */
    private void handleData() {
        currentlist.clear();
        for (int i = 0; i < list.size(); i++) {
            if (currentState.equals("已退号")) {
                if (list.get(i).yyzt.equals(String.valueOf(currentState)) || list.get(i).yyzt.equals("已取号")) {
                    currentlist.add(list.get(i));
                }
            } else {
                if (list.get(i).yyzt.equals(String.valueOf(currentState))) {
                    currentlist.add(list.get(i));
                }
            }
        }

        adapter.notifyDataSetChanged();
    }

    private void setDelPosition() {
        for (int i = 0; i < list.size(); i++) {
            if (currentlist.get(delposition).id.equals(list.get(i).id)) {
                list.get(i).yyzt = "已退号";//预约状态
                break;
            }
        }
    }

    @Override
    public void findView() {
        // TODO Auto-generated method stub
        findActionBar();
        actionBar.setTitle("预约记录");
        actionBar.setBackAction(new Action() {

            @Override
            public int getDrawable() {
                // TODO Auto-generated method stub
                return R.drawable.btn_back;
            }

            @Override
            public void performAction(View view) {
                // TODO Auto-generated method stub
                back();
            }

        });
        rl_1 = (RelativeLayout) findViewById(R.id.rl_1);
        rl_2 = (RelativeLayout) findViewById(R.id.rl_2);
        tv_1 = (TextView) findViewById(R.id.tv_1);
        tv_2 = (TextView) findViewById(R.id.tv_2);
        iv_1 = (ImageView) findViewById(R.id.iv_1);
        iv_2 = (ImageView) findViewById(R.id.iv_2);

        lv_history = (ListView) findViewById(R.id.lv_history);

        rl_1.setOnClickListener(this);
        rl_2.setOnClickListener(this);
    }

    private void initData() {
        adapter = new AppointAdapter();
        lv_history.setAdapter(adapter);
        familyvo = (MyFamilyVo) getIntent().getSerializableExtra("familyVo");
        PersonVo m_personVo = (PersonVo) getIntent().getSerializableExtra("personVo");//从预约成功传过来
        if (familyvo != null)//从家庭成员界面过来的
        {
            personVo.idcard = familyvo.idcard;
            personVo.realname = familyvo.realname;
        } else if (m_personVo != null)//预约成功之后传过来
        {
            personVo.idcard = m_personVo.idcard;
            personVo.realname = m_personVo.realname;
        } else {
            personVo.idcard = loginUser.idcard;
            personVo.realname = loginUser.realname;
        }
        getDatatask = new GetDataTask();
        getDatatask.execute();
    }

    /**
     * 获取预约记录
     *
     * @author Administrator
     */
    class GetDataTask extends AsyncTask<Void, Void, ResultModel<ArrayList<AppointInfoVo>>> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            actionBar.startTextRefresh();
        }

        @Override
        protected void onPostExecute(ResultModel<ArrayList<AppointInfoVo>> result) {
            // TODO Auto-generated method stub
            actionBar.endTextRefresh();
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    if (null != result.list && result.list.size() > 0) {
                        list = result.list;
                        currentState = "未取号";
                        handleData();
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(baseContext, "当前没有预约记录", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(baseContext, result.message, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(baseContext, "当前没有预约记录", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected ResultModel<ArrayList<AppointInfoVo>> doInBackground(Void... params) {
            // TODO Auto-generated method stub
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("method", "listwdyy");
            //map.put("as_sfzh", "320586198610012411");//做测试
            //map.put("as_sfzh", "320504198605273014");
            map.put("as_sfzh", personVo.idcard);
            map.put("as_lx", "0");
            return HttpApi.getInstance().parserArray_His(AppointInfoVo.class, "hiss/ser", map, new BsoftNameValuePair("id", loginUser.id), new BsoftNameValuePair("sn", loginUser.sn));
        }
    }

    /**
     * 取消预约记录
     *
     * @author Administrator
     */
    class DelDataTask extends AsyncTask<Void, Void, ResultModel<NullModel>> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            actionBar.startTextRefresh();
        }

        @Override
        protected void onPostExecute(ResultModel<NullModel> result) {
            // TODO Auto-generated method stub
            actionBar.endTextRefresh();
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    Toast.makeText(baseContext, "取消预约成功", Toast.LENGTH_SHORT).show();
                    //list.get(delposition).yyzt = "已退号";//预约状态
                    setDelPosition();
                    handleData();
                } else {
                    result.showToast(baseContext);
                }
            } else {
                Toast.makeText(baseContext, "取消预约失败", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected ResultModel<NullModel> doInBackground(Void... params) {
            // TODO Auto-generated method stub
            /*HashMap<String,String> map = new HashMap<String,String>();
            map.put("ksdm", currentlist.get(delposition).ksid);//科室代码
			map.put("ygdm", currentlist.get(delposition).ysid);//医生代码
			map.put("IsExpert", currentlist.get(delposition).yylx);//是否专家  0 否，1 是
			map.put("jzxh", currentlist.get(delposition).jzxh);//就诊序号
			map.put("zblb",currentlist.get(delposition).zblb);
			map.put("Id", currentlist.get(delposition).id);
			map.put("SeeDoctorDate", currentlist.get(delposition).yyrq);
			if(currentlist.get(delposition).ysid!=null)
			{
				map.put("ygdm", currentlist.get(delposition).ysid);
			}
			else
			{
				map.put("ygdm", "");
			}*/
            JSONObject json = new JSONObject();
            json.put("ksdm", currentlist.get(delposition).ksid);//科室代码
            json.put("IsExpert", currentlist.get(delposition).yylx);//是否专家  0 否，1 是
            json.put("jzxh", currentlist.get(delposition).jzxh);//就诊序号
            json.put("zblb", currentlist.get(delposition).zblb);
            json.put("Id", currentlist.get(delposition).id);
            json.put("SeeDoctorDate", currentlist.get(delposition).yyrq);
            if (currentlist.get(delposition).ysid != null) {
                json.put("ysdm", currentlist.get(delposition).ysid);
            } else {
                json.put("ysdm", "");
            }
            HashMap<String, String> map1 = new HashMap<String, String>();
            map1.put("method", "qxyy");
            String as_xml = json.toString();
            map1.put("as_xml", as_xml);
            return HttpApi.getInstance().parserData_His(NullModel.class, "hiss/ser", map1, new BsoftNameValuePair("id", loginUser.id), new BsoftNameValuePair("sn", loginUser.sn));
        }
    }

    class AppointAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return currentlist.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return currentlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(baseContext).inflate(R.layout.appoint_history_list_item, null);
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tv_dept = (TextView) convertView.findViewById(R.id.tv_dept);
                holder.tv_yyrq = (TextView) convertView.findViewById(R.id.tv_yyrq);
                holder.tv_djsj = (TextView) convertView.findViewById(R.id.tv_djrq);
                holder.tv_state = (TextView) convertView.findViewById(R.id.tv_state);
                holder.tv_del = (TextView) convertView.findViewById(R.id.tv_del);
                holder.ll_doctor = (LinearLayout) convertView.findViewById(R.id.ll_doctor);
                holder.tv_doctor = (TextView) convertView.findViewById(R.id.tv_doctor);
                holder.tv_sjd = (TextView) convertView.findViewById(R.id.tv_sjd);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tv_dept.setText(currentlist.get(position).ksmc);
            holder.tv_djsj.setText(DateUtil.dateFormate(currentlist.get(position).djsj, "yyyy-MM-dd"));
            holder.tv_yyrq.setText(currentlist.get(position).yyrq);
            if (currentlist.get(position).yyzt.equals("未取号"))//预约成功
            {
                holder.tv_state.setText("预约成功");
                if (DateUtil.isToday(currentlist.get(position).yyrq)) {
                    holder.tv_del.setVisibility(View.GONE);
                } else {
                    holder.tv_del.setVisibility(View.VISIBLE);
                }
            }
            if (currentlist.get(position).yyzt.equals("已退号")) {
                holder.tv_state.setText("已取消预约");
                holder.tv_del.setVisibility(View.GONE);
            }
            if (currentlist.get(position).yyzt.equals("已取号")) {
                holder.tv_state.setText("已取号");
                holder.tv_del.setVisibility(View.GONE);
            }
            holder.tv_name.setText(personVo.realname);
            if (currentlist.get(position).yylx.equals("0"))//0普通
            {
                holder.ll_doctor.setVisibility(View.GONE);
//                if (currentlist.get(position).zblb.equals("1"))//上午
//                {
//                    holder.tv_sjd.setText("8:00-11:30");
//                } else if (currentlist.get(position).zblb.equals("2"))//下午
//                {
//                    holder.tv_sjd.setText("12:00-17:00");
//                }
            } else {
                holder.ll_doctor.setVisibility(View.VISIBLE);
                holder.tv_doctor.setText(currentlist.get(position).ysxm);
            }

            holder.tv_del.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    new AlertDialog.Builder(AppointHistoryActivity.this).setMessage("确定取消该条预约记录？").setTitle("提示").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            delposition = position;
                            delDatatask = new DelDataTask();
                            delDatatask.execute();
                        }
                    }).setNegativeButton("取消", null).create().show();
                }

            });

            return convertView;
        }

        class ViewHolder {
            TextView tv_name;
            TextView tv_dept;
            TextView tv_yyrq;//预约日期
            TextView tv_sjd;//预约时间段
            TextView tv_djsj;//登记时间
            TextView tv_state;//预约状态
            TextView tv_del;//取消预约

            LinearLayout ll_doctor;
            TextView tv_doctor;//预约专家
        }
    }
}
