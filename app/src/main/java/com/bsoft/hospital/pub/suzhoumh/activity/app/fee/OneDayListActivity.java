package com.bsoft.hospital.pub.suzhoumh.activity.app.fee;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.view.BsoftActionBar;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.OneDayFeeVo;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.util.DateUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * 一日清单
 */
public class OneDayListActivity extends BaseActivity implements View.OnClickListener{

    private ListView lv_fee;
    private ExpandableListView ex_fee;
    private Button btn_search;
    private TextView tv_date;
    private TextView tv_money_all;
    private LinearLayout ll_money_all;
    public OneDayFeeVo oneDayFeeVo;
    public FeeAdapter adapter;
    private String date;
    private GetDataTask task;
    public Calendar cd;
    public DecimalFormat df = new DecimalFormat("0.00");

    public int m_year;
    public int m_month;
    public int m_day;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oneday_list);
        findView();
        initData();
    }

    @Override
    public void findView() {
        findActionBar();
        actionBar.setTitle("一日清单");
        actionBar.setBackAction(new BsoftActionBar.Action(){

            @Override
            public int getDrawable() {
                return R.drawable.btn_back;
            }

            @Override
            public void performAction(View view) {
                finish();
            }
        });
        btn_search = (Button)findViewById(R.id.btn_search);
        lv_fee = (ListView)findViewById(R.id.lv_fee);
        ex_fee = (ExpandableListView)findViewById(R.id.ex_fee);
        ll_money_all = (LinearLayout)findViewById(R.id.ll_money_all);
        tv_money_all = (TextView)findViewById(R.id.tv_money_all);
        tv_date = (TextView)findViewById(R.id.tv_date);
    }
    private void initData()
    {
        tv_date.setOnClickListener(this);
        btn_search.setOnClickListener(this);
        ll_money_all.setVisibility(View.GONE);

        ex_fee.setGroupIndicator(null);
        ex_fee.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
        cd = Calendar.getInstance();
        tv_date.setText(DateUtil.getBeforeDay(cd));//设置前一天的日期
        date = tv_date.getText().toString();
        m_year = cd.get(Calendar.YEAR);
        m_month = cd.get(Calendar.MONTH);
        m_day = cd.get(Calendar.DATE);

        task = new GetDataTask();
        task.execute();
    }
    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.btn_search:
                task = new GetDataTask();
                task.execute();
                break;
            case R.id.tv_date:
                new DatePickerDialog(OneDayListActivity.this, new DatePickerDialog.OnDateSetListener(){
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        String mm;
                        String dd;
                        if(monthOfYear<=8)
                        {
                            monthOfYear = monthOfYear+1;
                            mm="0"+monthOfYear;
                        }
                        else{
                            monthOfYear = monthOfYear+1;
                            mm=String.valueOf(monthOfYear);
                        }
                        if(dayOfMonth<=9)
                        {
                            dd="0"+dayOfMonth;
                        }
                        else{
                            dd=String.valueOf(dayOfMonth);
                        }
                        tv_date.setText(year+"-"+mm+"-"+dd);
                        date = tv_date.getText().toString();
                        m_year = year;
                        m_month = monthOfYear-1;
                        m_day = dayOfMonth;
                    }
                },
                        m_year,
                        m_month,
                        m_day).show();
                break;
        }
    }

    /**
     * 获取数据
     * @author Administrator
     *
     */
    private class GetDataTask extends
            AsyncTask<Void, Void, ResultModel<ArrayList<OneDayFeeVo>>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            actionBar.startTextRefresh();
        }

        @Override
        protected ResultModel<ArrayList<OneDayFeeVo>> doInBackground(
                Void... params) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("method", "getinventorylist");
            map.put("as_sfzh", loginUser.idcard);
//            map.put("as_sfzh", "320502193707081026");
            map.put("as_rq", date);

            return HttpApi.getInstance().parserArray_His(OneDayFeeVo.class,
                    "hiss/ser", map,
                    new BsoftNameValuePair("id", loginUser.id),
                    new BsoftNameValuePair("sn", loginUser.sn));

        }

        @Override
        protected void onPostExecute(ResultModel<ArrayList<OneDayFeeVo>> result) {
            super.onPostExecute(result);
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    if(result.list != null && result.list.size() > 0){
                        oneDayFeeVo = result.list.get(0);
                        if (oneDayFeeVo.mainCostList != null) {
                            ll_money_all.setVisibility(View.VISIBLE);
                            String totalCost = df.format(Double.parseDouble(oneDayFeeVo.totalCost));
                            tv_money_all.setText("¥" + totalCost);
                            adapter = new FeeAdapter(oneDayFeeVo.mainCostList);
                            ex_fee.setAdapter(adapter);
                            for (int i = 0; i < oneDayFeeVo.mainCostList.size(); i++) {
                                ex_fee.expandGroup(i);
                            }
                        }
                    }
                } else {
                    result.showToast(baseContext);
                }
            } else {
                Toast.makeText(baseContext, "加载失败", Toast.LENGTH_SHORT).show();
            }
            actionBar.endTextRefresh();
        }
    }
//    private class GetDataTask extends
//            AsyncTask<Void, Void, String> {
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            actionBar.startTextRefresh();
//        }
//
//        @Override
//        protected String doInBackground(
//                Void... params) {
//
//            String result = "";
//            try
//            {
//                String url = HttpNewApi.newUrl+"inventory/getInventoryList"+"?zyh="+
//                        loginUser.getCardByType(Constants.CARD_JZH_NUM).cardNum+"&"+"rq="+date;
//                result= HttpApi.getInstance().get(url);
//            }catch (Exception e)
//            {
//                e.printStackTrace();
//            }
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            if (null != result) {
//               /* if (result.statue == Statue.SUCCESS) {
//                    if (null != result.data) {
//                        oneDayFeeVo = result.data;
//                        ll_money_all.setVisibility(View.VISIBLE);
//                        tv_money_all.setText(oneDayFeeVo.totalCost);
//                        adapter = new ParentAdapter(oneDayFeeVo.mainCostList);
//                        lv_fee.setAdapter(adapter);
//                    }
//                } else {
//                    result.showToast(baseContext);
//                }*/
//                //有二级数组列表要特殊处理
//                try
//                {
//                    JSONObject object = new JSONObject(result);
//                    if(!object.isNull("data"))
//                    {
//                        String data = object.getString("data");
//                        oneDayFeeVo = new OneDayFeeVo();
//                        oneDayFeeVo.buideJson(data);
//                        if(oneDayFeeVo.mainCostList!=null)
//                        {
//                            ll_money_all.setVisibility(View.VISIBLE);
//                            String totalCost = df.format(Double.parseDouble(oneDayFeeVo.totalCost));
//                            tv_money_all.setText("¥"+totalCost);
//                            adapter = new FeeAdapter(oneDayFeeVo.mainCostList);
//                            ex_fee.setAdapter(adapter);
//                            for(int i=0;i<oneDayFeeVo.mainCostList.size();i++)
//                            {
//                                ex_fee.expandGroup(i);
//                            }
//                        }
//                    }
//                    else
//                    {
//                        Toast.makeText(baseContext, object.getString("message"), Toast.LENGTH_SHORT).show();
//                        adapter.clearData();
//                        ll_money_all.setVisibility(View.GONE);
//                    }
//
//                }catch (Exception e)
//                {
//                    e.printStackTrace();
//                }
//            } else {
//                Toast.makeText(baseContext, "加载失败", Toast.LENGTH_SHORT).show();
//            }
//            actionBar.endTextRefresh();
//        }
//    }

    class FeeAdapter extends BaseExpandableListAdapter
    {
        private List<OneDayFeeVo.MainCost> list = new ArrayList<OneDayFeeVo.MainCost>();

        public FeeAdapter(List<OneDayFeeVo.MainCost> mainCostList)
        {
            this.list = mainCostList;
        }

        @Override
        public int getGroupCount() {
            return list.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return list.get(groupPosition).costList.size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return list.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return list.get(groupPosition).costList.get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            GroupViewHolder holder;
            if(convertView == null)
            {
                holder = new GroupViewHolder();
                convertView = LayoutInflater.from(baseContext).inflate(R.layout.oneday_list_item_parent, null);
                holder.tv_fee_name = (TextView)convertView.findViewById(R.id.tv_fee_name);
                holder.tv_money = (TextView)convertView.findViewById(R.id.tv_money);
                convertView.setTag(holder);
            }
            else
            {
                holder = (GroupViewHolder)convertView.getTag();
            }

            holder.tv_fee_name.setText(list.get(groupPosition).sfmc);
            String zjje = df.format(Double.parseDouble(list.get(groupPosition).zjje));
            holder.tv_money.setText("¥"+zjje);
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ChildViewHolder holder;
            if(convertView == null)
            {
                convertView = LayoutInflater.from(baseContext).inflate(R.layout.oneday_list_item_child, null);
                holder = new ChildViewHolder();
                holder.tv_fee_name_detail = (TextView)convertView.findViewById(R.id.tv_fee_name_detail);
                holder.tv_count = (TextView)convertView.findViewById(R.id.tv_count);
                holder.tv_money_all = (TextView)convertView.findViewById(R.id.tv_money_all);
                convertView.setTag(holder);
            }
            else
            {
                holder = (ChildViewHolder)convertView.getTag();
            }
            String fydj = df.format(Double.parseDouble(list.get(groupPosition).costList.get(childPosition).fydj));
            String zjje = df.format(Double.parseDouble(list.get(groupPosition).costList.get(childPosition).zjje));
            holder.tv_fee_name_detail.setText(list.get(groupPosition).costList.get(childPosition).fymc);
            holder.tv_count.setText("单价:¥"+fydj+"  数量:"+list.get(groupPosition).costList.get(childPosition).fysl);
            holder.tv_money_all.setText(" ¥ "+zjje);
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }

        class GroupViewHolder
        {
            TextView tv_fee_name;
            TextView tv_money;
        }

        class ChildViewHolder
        {
            TextView tv_fee_name_detail;
            TextView tv_count;
            TextView tv_money_all;
        }

        public void clearData()
        {
            this.list.clear();
            adapter.notifyDataSetChanged();
        }
    }

}
