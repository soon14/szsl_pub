package com.bsoft.hospital.pub.suzhoumh.activity.clinic;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.view.BsoftActionBar;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.fragment.index.BaseFragment;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.visit.VisitDetailVo;
import com.bsoft.hospital.pub.suzhoumh.model.visit.VisitVo;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author :lizhengcao
 * @date :2017/3/10
 * E-mail:lizc@bsoft.com.cn
 * @类说明 就诊明细, new
 */

public class ClinicDetailFragment extends BaseFragment {


    public List<VisitDetailVo> visitDetailVoList = new ArrayList<VisitDetailVo>();

    public VisitVo visitVo;

    private GetDataTask getDataTask;

    private ExpandableListView expandableListView;

    private MyAdapter myAdapter;

    public DecimalFormat format = new DecimalFormat("0.00");
    private BsoftActionBar actionBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail_clinic, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initView(view);
        initData();
    }

    private void initData() {
        visitVo = (VisitVo) getActivity().getIntent().getSerializableExtra("visitVo");
        getDataTask = new GetDataTask();
        getDataTask.execute();
    }

    private void initView(View view) {
        actionBar = (BsoftActionBar) getActivity().findViewById(R.id.actionbar);
        expandableListView = (ExpandableListView) view.findViewById(R.id.ex_detail);
        expandableListView.setGroupIndicator(null);
    }


    private class GetDataTask extends
            AsyncTask<Void, Void, ResultModel<ArrayList<VisitDetailVo>>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            actionBar.startTextRefresh();
        }

        @Override
        protected ResultModel<ArrayList<VisitDetailVo>> doInBackground(Void... params) {

            HashMap<String, String> map = new HashMap<String, String>();
            map.put("al_jzxh", visitVo.jzxh);
            map.put("as_ygdm", visitVo.ygdm);
            map.put("adt_jzsj", visitVo.jzsj);
            map.put("as_brid", visitVo.brid);
            map.put("method", "getjzxq");
            return HttpApi.getInstance().parserArray_His(VisitDetailVo.class, "hiss/ser", map,
                    new BsoftNameValuePair("id", loginUser.id),
                    new BsoftNameValuePair("sn", loginUser.sn));
        }

        @Override
        protected void onPostExecute(ResultModel<ArrayList<VisitDetailVo>> result) {
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    if (null != result.list && result.list.size() > 0) {
                        visitDetailVoList = result.list;
                        //visitDetailAdapter.notifyDataSetChanged();
                        myAdapter = new MyAdapter();
                        expandableListView.setAdapter(myAdapter);
                        showAllData();
                    } else {
                        Toast.makeText(getActivity(), "数据为空", Toast.LENGTH_SHORT)
                                .show();
                    }
                } else {
                    result.showToast(getActivity());
                }
            } else {
                Toast.makeText(getActivity(), "加载失败", Toast.LENGTH_SHORT).show();
            }
            actionBar.endTextRefresh();
        }
    }


    private void showAllData() {
        for (int i = 0; i < visitDetailVoList.size(); i++) {
            expandableListView.expandGroup(i);
        }
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
    }

    class MyAdapter extends BaseExpandableListAdapter {

        //得到子item需要关联的数据
        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return visitDetailVoList.get(groupPosition).jzxq.get(childPosition);
        }

        //得到子item的ID
        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        //设置子item的组件
        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            ViewHolderChild holder;
            if (convertView == null) {
                holder = new ViewHolderChild();
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_visit_detail_child, null);
                holder.tv_xm = (TextView) convertView.findViewById(R.id.tv_xm);
                holder.tv_sl = (TextView) convertView.findViewById(R.id.tv_sl);
                holder.tv_dj = (TextView) convertView.findViewById(R.id.tv_dj);
                holder.tv_je = (TextView) convertView.findViewById(R.id.tv_je);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolderChild) convertView.getTag();
            }
            VisitDetailVo.JZXQ jzxq = visitDetailVoList.get(groupPosition).jzxq.get(childPosition);
            if (!jzxq.gg.equals("") && !jzxq.gg.equals("\t")) {
                holder.tv_xm.setText(jzxq.fymc + "/" + "(" + jzxq.gg + ")");
            } else {
                holder.tv_xm.setText(jzxq.fymc);
            }

            String sl = jzxq.sl.replaceAll(".00", "");
            holder.tv_sl.setText(sl + "(" + jzxq.dw + ")");
            holder.tv_dj.setText(format.format(new BigDecimal(jzxq.dj)));
            holder.tv_je.setText(jzxq.je);
            return convertView;
        }

        class ViewHolderChild {
            TextView tv_xm;
            TextView tv_sl;
            TextView tv_dj;
            TextView tv_je;
        }

        //获取当前父item下的子item的个数
        @Override
        public int getChildrenCount(int groupPosition) {
            return visitDetailVoList.get(groupPosition).jzxq.size();
        }

        //获取当前父item的数据
        @Override
        public Object getGroup(int groupPosition) {
            return visitDetailVoList.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return visitDetailVoList.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        //设置父item组件
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_visit_detail, null);
                holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                holder.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
                holder.tv_sl = (TextView) convertView.findViewById(R.id.tv_sl);
                holder.tv_ts = (TextView) convertView.findViewById(R.id.tv_ts);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv_title.setText(visitDetailVoList.get(groupPosition).xmmc + ":");
            holder.tv_money.setText(visitDetailVoList.get(groupPosition).hjje + "元");
            if (visitDetailVoList.get(groupPosition).xmmc.contains("草药"))//草药
            {
                holder.tv_ts.setVisibility(View.VISIBLE);
                holder.tv_ts.setText("帖数：" + visitDetailVoList.get(groupPosition).jzxq.get(0).cfts + "帖");
            } else {
                holder.tv_ts.setVisibility(View.GONE);
            }

            return convertView;
        }

        class ViewHolder {
            TextView tv_title;
            TextView tv_money;
            TextView tv_sl;
            TextView tv_ts;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

    }

    @Override
    public void startHint() {

    }

    @Override
    public void endHint() {

    }
}
