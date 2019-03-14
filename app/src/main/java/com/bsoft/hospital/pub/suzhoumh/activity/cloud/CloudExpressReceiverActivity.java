package com.bsoft.hospital.pub.suzhoumh.activity.cloud;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.view.BsoftActionBar;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.adapter.cloud.CloudExpressReceiverAdapter;
import com.bsoft.hospital.pub.suzhoumh.activity.adapter.cloud.listener.CloudExpressReceiverListener;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.cloud.dialog.CustomDialog;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.CloudExpressReceiverModel;
import com.bsoft.hospital.pub.suzhoumh.model.NullModel;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.util.ToastUtils;
import com.bsoft.hospital.pub.suzhoumh.util.pop.CampusSelection;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * @author :lizhengcao
 * @date :2019/3/13
 * E-mail:lizc@bsoft.com.cn
 * @类说明 收货人地址
 */
public class CloudExpressReceiverActivity extends BaseActivity implements CloudExpressReceiverListener {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_edit)
    TextView tvEdit;
    @BindView(R.id.btn_shipping_address)
    Button btnShippingAddress;
    private CloudExpressReceiverAdapter adapter;
    private List<CloudExpressReceiverModel> cloudExpressReceiverList;
    private boolean isEdit = false;
    private List<String> isDeleteList;
    private List<Integer> deletePosList;
    private String ids = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver_express_cloud);
        mUnbinder = ButterKnife.bind(this);
        findView();
        initData();
    }

    private void initData() {

        tvEdit.setVisibility(View.GONE);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = new CloudExpressReceiverAdapter(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);

        new GetDataTask().execute();
    }


    @OnClick({R.id.btn_shipping_address, R.id.tv_edit})
    public void doClick(View v) {
        switch (v.getId()) {
            case R.id.btn_shipping_address:
                shippingAddressOrDeleteAddress();
                break;
            case R.id.tv_edit:
                editAddress();
                break;
        }
    }

    /**
     * 对收货地址的编辑操作
     */
    private void editAddress() {
        if (!isEdit) {
            tvEdit.setText("取消编辑");
            btnShippingAddress.setText("删除");
            isEdit = true;
        } else {
            tvEdit.setText("编辑地址");
            btnShippingAddress.setText("新增收货地址");
            isEdit = false;
        }
        for (CloudExpressReceiverModel data : cloudExpressReceiverList) {
            data.isEdit = isEdit;
        }
        adapter.setNewData(cloudExpressReceiverList);
    }

    /**
     * 新增收货地址或者是删除地址
     */
    private void shippingAddressOrDeleteAddress() {
        if (isEdit) {
            //按钮文字为删除
            isDeleteList = new ArrayList<>();
            deletePosList = new ArrayList<>();
            cloudExpressReceiverList = adapter.getData();
            for (int i = 0; i < cloudExpressReceiverList.size(); i++) {
                CloudExpressReceiverModel d = cloudExpressReceiverList.get(i);
                if (d.select) {
                    isDeleteList.add(d.addrId);
                    deletePosList.add(i);
                }
            }
            if (isDeleteList.size() == 0 || deletePosList.size() == 0)
                ToastUtils.showToastShort("请选择要删除的地址");
            else {
                for (int j = 0; j < isDeleteList.size(); j++) {
                    if (j == isDeleteList.size() - 1)
                        ids = String.format("%s%s", ids, isDeleteList.get(j));
                    else
                        ids = String.format("%s%s", ids, isDeleteList.get(j) + ",");
                }
                new CustomDialog.Builder(this)
                        .setCancelable(true)
                        .setContent("确认删除吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                new GetDeleteMoreDataTask().execute();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.dismiss();
                            }
                        }).create().show();

            }
        } else
            //按钮文字为新增收货地址
            startActivity(new Intent(baseContext, NewShippingAddressActivity.class));
    }

    /**
     * 编辑操作
     *
     * @param data
     */
    @Override
    public void onEdit(CloudExpressReceiverModel data) {
        Intent intent = new Intent(this, NewShippingAddressActivity.class);
        intent.putExtra("cloudExpressReceiverModel", data);
        intent.putExtra("isEdit", true);
        startActivity(intent);
    }

    /**
     * 默认地址的修改
     *
     * @param data
     */
    private String addrId;

    @Override
    public void onDefaultAddress(final CloudExpressReceiverModel data) {
        this.addrId = data.addrId;
        new CustomDialog.Builder(this)
                .setCancelable(true)
                .setContent("是否设置为默认收货地址？")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        new GetDefaultAddressDataTask().execute();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                }).create().show();

    }


    /**
     * 设置默认地址
     */
    @SuppressLint("StaticFieldLeak")
    private class GetDefaultAddressDataTask extends
            AsyncTask<Void, Void, ResultModel<List<NullModel>>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            actionBar.startTextRefresh();
        }

        @SuppressWarnings("unchecked")
        @Override
        protected ResultModel<List<NullModel>> doInBackground(
                Void... params) {
            return HttpApi.getInstance().parserArray(NullModel.class, "auth/cloudClinic/updateAddrIdForDefault",
                    new BsoftNameValuePair("addrId", addrId),
                    new BsoftNameValuePair("id", loginUser.id),
                    new BsoftNameValuePair("sn", loginUser.sn));

        }

        @Override
        protected void onPostExecute(ResultModel<List<NullModel>> result) {
            super.onPostExecute(result);
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    cloudExpressReceiverList = adapter.getData();
                    for (CloudExpressReceiverModel receiver : cloudExpressReceiverList) {
                        if (receiver.addrId.equals(addrId))
                            receiver.tolerate = "1";
                        else
                            receiver.tolerate = "0";
                    }
                    adapter.setNewData(cloudExpressReceiverList);
                    ToastUtils.showToastShort("默认地址设置成功");
                } else {
                    result.showToast(baseContext);
                }
            } else {
                ToastUtils.showToastShort("请求失败");
            }
            actionBar.endTextRefresh();
        }
    }


    /**
     * 批量删除常用地址
     */
    @SuppressLint("StaticFieldLeak")
    private class GetDeleteMoreDataTask extends
            AsyncTask<Void, Void, ResultModel<List<NullModel>>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            actionBar.startTextRefresh();
        }

        @SuppressWarnings("unchecked")
        @Override
        protected ResultModel<List<NullModel>> doInBackground(
                Void... params) {
            return HttpApi.getInstance().parserArray(NullModel.class, "auth/cloudClinic/deletePostAddrByAddrIds",
                    new BsoftNameValuePair("addrIds", ids),
                    new BsoftNameValuePair("id", loginUser.id),
                    new BsoftNameValuePair("sn", loginUser.sn));

        }

        @Override
        protected void onPostExecute(ResultModel<List<NullModel>> result) {
            super.onPostExecute(result);
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    List<CloudExpressReceiverModel> newData = new ArrayList<>();
                    for (int i = 0; i < cloudExpressReceiverList.size(); i++) {
                        if (!deletePosList.contains(i)) {
                            //非删除项重新添加
                            newData.add(cloudExpressReceiverList.get(i));
                        }
                    }
                    cloudExpressReceiverList = newData;
                    adapter.setNewData(cloudExpressReceiverList);
                    ToastUtils.showToastShort("删除收货地址成功");
                } else {
                    result.showToast(baseContext);
                }
            } else {
                ToastUtils.showToastShort("请求失败");
            }
            actionBar.endTextRefresh();
        }
    }

    /**
     * 查询常用地址
     */
    @SuppressLint("StaticFieldLeak")
    private class GetDataTask extends
            AsyncTask<Void, Void, ResultModel<List<CloudExpressReceiverModel>>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            actionBar.startTextRefresh();
        }

        @SuppressWarnings("unchecked")
        @Override
        protected ResultModel<List<CloudExpressReceiverModel>> doInBackground(
                Void... params) {
            return HttpApi.getInstance().parserArray(CloudExpressReceiverModel.class, "auth/cloudClinic/queryPostAddrList",
                    new BsoftNameValuePair("id", loginUser.id),
                    new BsoftNameValuePair("sn", loginUser.sn));

        }

        @Override
        protected void onPostExecute(ResultModel<List<CloudExpressReceiverModel>> result) {
            super.onPostExecute(result);
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    if (null != result.list && result.list.size() > 0) {
                        tvEdit.setVisibility(View.VISIBLE);
                        cloudExpressReceiverList = result.list;
                        adapter.setNewData(cloudExpressReceiverList);
                    } else
                        tvEdit.setVisibility(View.GONE);
                } else {
                    result.showToast(baseContext);
                }
            } else {
                ToastUtils.showToastShort("请求失败");
            }
            actionBar.endTextRefresh();
        }
    }


    @Override
    public void findView() {
        findActionBar();
        actionBar.setTitle("收件人地址");
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
    }
}
