package com.bsoft.hospital.pub.suzhoumh.activity.app.fee;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tanklib.floatinggroup.FloatingGroupExpandableListView;
import com.app.tanklib.floatinggroup.WrapperExpandableListAdapter;
import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.nineoldandroids.view.ViewHelper;
import com.app.tanklib.util.AsyncTaskUtil;
import com.app.tanklib.view.BsoftActionBar;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.PayedGroupVo;
import com.bsoft.hospital.pub.suzhoumh.model.PayedVo;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.util.ToastUtils;
import com.bsoft.hospital.pub.suzhoumh.util.zxing.BarcodeCreater;
import com.daoyixun.ipsmap.IpsMapSDK;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 已支付项目
 * Created by Administrator on 2016/5/26.
 */
public class FeePayedActivity2 extends BaseActivity {

    public FloatingGroupExpandableListView listView;
    public GetDataTask task;

    private List<PayedGroupVo> dataList = new ArrayList<>();
    private FeeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paded2);
        findView();
        getData();
    }

    private void getData() {
        task = new GetDataTask();
        task.execute();
    }

    @Override
    public void findView() {
        findActionBar();
        actionBar.setTitle("已支付项目");
        actionBar.setBackAction(new BsoftActionBar.Action() {

            @Override
            public int getDrawable() {

                return R.drawable.btn_back;
            }

            @Override
            public void performAction(View view) {

                finish();
            }

        });

        actionBar.setRefreshTextView("刷新", new BsoftActionBar.Action() {
            @Override
            public int getDrawable() {
                return 0;
            }

            @Override
            public void performAction(View view) {
                getData();
            }
        });


        listView = (FloatingGroupExpandableListView) findViewById(R.id.listView);
        listView.setGroupIndicator(null);

        adapter = new FeeAdapter();
        WrapperExpandableListAdapter wAdapter = new WrapperExpandableListAdapter(adapter);
        listView.setAdapter(wAdapter);

    }


    //
    private class GetDataTask extends
            AsyncTask<Void, Void, ResultModel<ArrayList<PayedGroupVo>>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            actionBar.startTextRefresh();
        }


        @SuppressWarnings("unchecked")
        @Override
        protected ResultModel<ArrayList<PayedGroupVo>> doInBackground(
                Void... params) {

            HashMap<String, String> map = new HashMap<String, String>();
            map.put("method", "getclinicpayedlist");
//            map.put("as_sfzh", "362324198202110919");
            map.put("as_sfzh", loginUser.idcard);
            return HttpApi.getInstance().parserArray_His(PayedGroupVo.class, "hiss/ser", map,
                    new BsoftNameValuePair("id", loginUser.id),
                    new BsoftNameValuePair("sn", loginUser.sn));
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void onPostExecute(ResultModel<ArrayList<PayedGroupVo>> result) {
            super.onPostExecute(result);
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    if (null != result.list && result.list.size() > 0) {
                        dataList = result.list;
                        adapter.notifyDataSetChanged();
                        listView.expandGroup(0);
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

    class FeeAdapter extends BaseExpandableListAdapter {

        //得到子item需要关联的数据
        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return dataList.get(groupPosition).jfxq.get(childPosition);
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
                convertView = LayoutInflater.from(baseContext).inflate(R.layout.item_payed, null);
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tv_fphm = (TextView) convertView.findViewById(R.id.tv_fphm);
                holder.tv_sfxm = (TextView) convertView.findViewById(R.id.tv_sfxm);
                holder.tv_xmje = (TextView) convertView.findViewById(R.id.tv_xmje);
                holder.tv_rq = (TextView) convertView.findViewById(R.id.tv_rq);
                holder.tv_czgh = (TextView) convertView.findViewById(R.id.tv_czgh);
                holder.tv_bz = (TextView) convertView.findViewById(R.id.tv_bz);
                holder.iv_bar = (ImageView) convertView.findViewById(R.id.iv_bar);
                holder.iv_state = (ImageView) convertView.findViewById(R.id.iv_state);
                holder.mYndh = (TextView) convertView.findViewById(R.id.tv_yndh);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolderChild) convertView.getTag();
            }
            final PayedVo vo = dataList.get(groupPosition).jfxq.get(childPosition);
            holder.iv_bar.setImageBitmap(BarcodeCreater.creatBarcode(baseContext, vo.fphm, 300, 130, true));
            holder.tv_fphm.setText("发票号:" + vo.fphm);
            holder.tv_name.setText("姓名:" + vo.xm);
            holder.tv_sfxm.setText("收费项目:" + vo.sfxm);
            holder.tv_xmje.setText("合计金额:" + vo.xmje);
            holder.tv_rq.setText("日期:" + vo.rq);
            holder.tv_czgh.setText("收款员:" + vo.czgh);
            holder.tv_bz.setText("备注:" + vo.bz);
            if (TextUtils.equals("1", vo.zxpb)) {
                holder.iv_state.setVisibility(View.VISIBLE);
                holder.iv_state.setImageResource(R.drawable.ic_yqy);
            } else if (TextUtils.equals("2", vo.zxpb)) {
                holder.iv_state.setVisibility(View.VISIBLE);
                holder.iv_state.setImageResource(R.drawable.ic_yzx);
            } else {
                holder.iv_state.setVisibility(View.GONE);
            }


            if (Constants.getHttpUrl().equals(Constants.HttpUrlEasternDistrict))
                holder.mYndh.setVisibility(View.VISIBLE);
            else
                holder.mYndh.setVisibility(View.GONE);

            holder.mYndh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TextUtils.isEmpty(vo.ksid)) {
                        ToastUtils.showToastShort("目标地址为空，不能进行院导航");
                        return;
                    }

                    IpsMapSDK.openIpsMapActivity(baseContext, Constants.IPSMAP_MAP_ID, vo.ksid);
                }
            });

            return convertView;
        }

        class ViewHolderChild {
            TextView tv_name, tv_fphm, tv_lb, tv_grbh, tv_sfxm, tv_xmje, tv_rq, tv_czgh, tv_bz;
            ImageView iv_bar, iv_state;
            TextView mYndh;
        }

        //获取当前父item下的子item的个数
        @Override
        public int getChildrenCount(int groupPosition) {
            if (dataList.get(groupPosition).jfxq != null)
                return dataList.get(groupPosition).jfxq.size();
            return 0;
        }

        //获取当前父item的数据
        @Override
        public Object getGroup(int groupPosition) {
            return dataList.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return dataList.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        //设置父item组件
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(baseContext).inflate(R.layout.item_payed_child, null);
                holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                holder.iv_state = (ImageView) convertView.findViewById(R.id.iv_state);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv_title.setText(dataList.get(groupPosition).jfrq);
//            holder.iv_state.setImageResource( isExpanded ? R.drawable.arrow_down : R.drawable.arrow_right);
            holder.iv_state.setImageResource(R.drawable.arrow_right);
            if (isExpanded) {
                ViewHelper.setRotation(holder.iv_state, 90f);
            } else {
                ViewHelper.setRotation(holder.iv_state, -90f);
            }

            return convertView;
        }

        class ViewHolder {
            TextView tv_title;
            ImageView iv_state;
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
    public void onDestroy() {
        super.onDestroy();
        AsyncTaskUtil.cancelTask(task);

    }
}
