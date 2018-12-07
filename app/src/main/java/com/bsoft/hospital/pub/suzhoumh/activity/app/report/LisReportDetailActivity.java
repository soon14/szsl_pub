package com.bsoft.hospital.pub.suzhoumh.activity.app.report;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.view.BsoftActionBar.Action;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.app.report.LisDetailVo;
import com.bsoft.hospital.pub.suzhoumh.model.app.report.LisReportVo;

/**
 * 检验详情
 *
 * @author Administrator
 */
public class LisReportDetailActivity extends BaseActivity {

    private TextView tv_title;
    private TextView tv_date;
    private TextView tv_doc;
    private ListView lv_detail;
    private ArrayList<LisDetailVo> list = new ArrayList<LisDetailVo>();
    private LisReportVo vo;
    private DetailAdapter adapter;
    private GetDataTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.report_lis_detail);
        findView();
        initData();
    }


    @Override
    public void findView() {
        findActionBar();
        actionBar.setTitle("检验详情");
        actionBar.setBackAction(new Action() {

            @Override
            public int getDrawable() {
                return R.drawable.btn_back;
            }

            @Override
            public void performAction(View view) {
                back();
            }

        });
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_doc = (TextView) findViewById(R.id.tv_doc);
        lv_detail = (ListView) findViewById(R.id.lv_detail);
    }

    private void initData() {
        vo = (LisReportVo) getIntent().getSerializableExtra("vo");
        tv_title.setText(vo.JYMD);
        tv_date.setText(vo.OBSERVATIONDATETIME);
        tv_doc.setText(vo.OBSERVATIONOPTNAME);
        adapter = new DetailAdapter();
        lv_detail.setAdapter(adapter);
        task = new GetDataTask();
        task.execute();
    }

    private class GetDataTask extends AsyncTask<String, Void, ResultModel<ArrayList<LisDetailVo>>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            actionBar.startTextRefresh();
        }

        @Override
        protected ResultModel<ArrayList<LisDetailVo>> doInBackground(String... params) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("as_jlxh", vo.ASSAYRECORDGUID);
            map.put("method", "listptlabdetailbylrdh");
            return HttpApi.getInstance().parserArray_His(LisDetailVo.class, "hiss/ser", map,
                    new BsoftNameValuePair("id", loginUser.id),
                    new BsoftNameValuePair("sn", loginUser.sn));
        }

        @Override
        protected void onPostExecute(ResultModel<ArrayList<LisDetailVo>> result) {
            super.onPostExecute(result);
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    if (result.list != null && result.list.size() > 0) {
                        list = result.list;
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(baseContext, result.message, Toast.LENGTH_SHORT).show();
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

    class DetailAdapter extends BaseAdapter {

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
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(baseContext).inflate(R.layout.report_lis_detail_item, null);
                holder.tv_xm = (TextView) convertView.findViewById(R.id.tv_xm);
                holder.tv_jg = (TextView) convertView.findViewById(R.id.tv_jg);
                holder.tv_ckz = (TextView) convertView.findViewById(R.id.tv_ckz);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv_xm.setText(list.get(position).OBSERVATIONSUB_NAME);
            if (list.get(position).SFYC == 0) {
                holder.tv_jg.setText(list.get(position).OBSERVATIONVALUE);
                holder.tv_jg.setTextColor(getResources().getColor(R.color.black));
            } else {
                holder.tv_jg.setText(list.get(position).OBSERVATIONVALUE + list.get(position).YCBS);
                holder.tv_jg.setTextColor(getResources().getColor(R.color.red));
            }
            holder.tv_ckz.setText(getCKZ(list.get(position).BOTTOMVALUE, list.get(position).TOPVALUE, list.get(position).UNITS));
            /*if(!list.get(position).BOTTOMVALUE.equals("")&&list.get(position).TOPVALUE.equals(""))
			{
				holder.tv_ckz.setText(list.get(position).BOTTOMVALUE+"("+list.get(position).UNITS+")");
			}
			else if(list.get(position).BOTTOMVALUE.equals("")&&!list.get(position).TOPVALUE.equals(""))
			{
				holder.tv_ckz.setText(list.get(position).TOPVALUE+"("+list.get(position).UNITS+")");
			}
			else if(!list.get(position).BOTTOMVALUE.equals("")&&!list.get(position).TOPVALUE.equals(""))
			{
				holder.tv_ckz.setText(list.get(position).BOTTOMVALUE+"-"+list.get(position).TOPVALUE+"("+list.get(position).UNITS+")");
			}*/
            return convertView;
        }

        private String getCKZ(String topvalue, String bottomvalue, String unit) {
            String value = "";
            if (!unit.equals("")) {
                if (!bottomvalue.equals("") && topvalue.equals("")) {
                    value = bottomvalue + "(" + unit + ")";
                } else if (bottomvalue.equals("") && !topvalue.equals("")) {
                    value = topvalue + "(" + unit + ")";
                } else if (!bottomvalue.equals("") && !topvalue.equals("")) {
                    value = bottomvalue + "-" + topvalue + "(" + unit + ")";
                }
            } else {
                if (!bottomvalue.equals("") && topvalue.equals("")) {
                    value = bottomvalue;
                } else if (bottomvalue.equals("") && !topvalue.equals("")) {
                    value = topvalue;
                } else if (!bottomvalue.equals("") && !topvalue.equals("")) {
                    value = bottomvalue;
                }
            }
            return value;
        }

        class ViewHolder {
            TextView tv_xm;
            TextView tv_jg;
            TextView tv_ckz;
        }
    }
}
