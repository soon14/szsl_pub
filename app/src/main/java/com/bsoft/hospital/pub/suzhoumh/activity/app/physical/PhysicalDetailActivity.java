package com.bsoft.hospital.pub.suzhoumh.activity.app.physical;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.util.AsyncTaskUtil;
import com.app.tanklib.util.StringUtil;
import com.app.tanklib.view.BsoftActionBar;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.adapter.physical.PhysicalDetailAdapter;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.app.physical.PhysicalDetailVo;
import com.bsoft.hospital.pub.suzhoumh.model.app.physical.PhysicalResultVo;

import java.util.ArrayList;
import java.util.HashMap;

public class PhysicalDetailActivity extends BaseActivity {

    //“体检详情”
    private RelativeLayout mDetailLayout;
    private TextView mDetailTv;
    private ImageView mDetailIv;
    private ExpandableListView mExpandableListView;

    //“体检综述”
    private RelativeLayout mReviewLayout;
    private TextView mReviewTv;
    private ImageView mReviewIv;
    private ScrollView mScrollView;
    private TextView mNameTv;
    private ImageView mSexIv;
    private TextView mAgeTv;
    private TextView mNumTv;
    private TextView mDeptTv;
    private TextView mDateTv;
    private TextView mSummarizeTv;
    private TextView mSuggestTv;

    private String tjdh;

    private PhysicalResultVo mResultVo;
    private ArrayList<PhysicalDetailVo> mDetailVos;

    private PhysicalDetailAdapter mAdapter;

    private GetDataTask mDataTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physical_detail);

        tjdh = getIntent().getStringExtra("tjdh");

        findView();
        initView();
        setClick();
        initData();
    }

    private void initData() {
        mDataTask = new GetDataTask();
        mDataTask.execute();
    }

    private void setClick() {
        mDetailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDetailTv.setTextColor(getResources().getColor(R.color.blue));
                mDetailIv.setVisibility(View.VISIBLE);
                mExpandableListView.setVisibility(View.VISIBLE);
                mReviewTv.setTextColor(getResources().getColor(R.color.black));
                mReviewIv.setVisibility(View.INVISIBLE);
                mScrollView.setVisibility(View.GONE);
            }
        });
        mReviewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mReviewTv.setTextColor(getResources().getColor(R.color.blue));
                mReviewIv.setVisibility(View.VISIBLE);
                mScrollView.setVisibility(View.VISIBLE);
                mDetailTv.setTextColor(getResources().getColor(R.color.black));
                mDetailIv.setVisibility(View.INVISIBLE);
                mExpandableListView.setVisibility(View.GONE);
            }
        });
    }

    private void initView() {
        mDetailVos = new ArrayList<>();
        mAdapter = new PhysicalDetailAdapter(baseContext, mDetailVos);
        mExpandableListView.setAdapter(mAdapter);
        mExpandableListView.setGroupIndicator(null);
        mExpandableListView.setChildDivider(getResources().getDrawable(R.color.white));
        mExpandableListView.setDivider(getResources().getDrawable(R.color.divider2white));
        mExpandableListView.setDividerHeight(1);
        mExpandableListView.setClickable(false);
    }

    @Override
    public void findView() {
        findActionBar();
        actionBar.setTitle("体检详情");
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

        mDetailLayout = (RelativeLayout) findViewById(R.id.rl_detail);
        mDetailTv = (TextView) findViewById(R.id.tv_detail);
        mDetailIv = (ImageView) findViewById(R.id.iv_detail);
        mExpandableListView = (ExpandableListView) findViewById(R.id.expandableListView);

        mReviewLayout = (RelativeLayout) findViewById(R.id.rl_review);
        mReviewTv = (TextView) findViewById(R.id.tv_review);
        mReviewIv = (ImageView) findViewById(R.id.iv_review);
        mScrollView = (ScrollView) findViewById(R.id.scrollView);
        mNameTv = (TextView) findViewById(R.id.tv_name);
        mSexIv = (ImageView) findViewById(R.id.iv_sex);
        mAgeTv = (TextView) findViewById(R.id.tv_age);
        mNumTv = (TextView) findViewById(R.id.tv_num);
        mDeptTv = (TextView) findViewById(R.id.tv_dept);
        mDateTv = (TextView) findViewById(R.id.tv_date);
        mSummarizeTv = (TextView) findViewById(R.id.tv_summarize);
        mSuggestTv = (TextView) findViewById(R.id.tv_suggest);
    }

    private class GetDataTask extends AsyncTask<Void, Void, ResultModel<ArrayList<PhysicalResultVo>>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            actionBar.startImageRefresh();
        }

        @SuppressWarnings("unchecked")
        @Override
        protected ResultModel<ArrayList<PhysicalResultVo>> doInBackground(Void... params) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("method", "gettjxq");
            map.put("as_tjbh", tjdh);
            return HttpApi.getInstance().parserArray_His(PhysicalResultVo.class, "hiss/ser", map,
                    new BsoftNameValuePair("id", loginUser.id),
                    new BsoftNameValuePair("sn", loginUser.sn));
        }

        @Override
        protected void onPostExecute(ResultModel<ArrayList<PhysicalResultVo>> result) {
            super.onPostExecute(result);
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    if (null != result.list) {
                        mResultVo = result.list.get(0);
                        mDetailVos = mResultVo.tjxq;
                        mAdapter.addData(mDetailVos);
                        initReview();
                    }
                } else {
                    result.showToast(baseContext);
                }
            } else {
                Toast.makeText(baseContext, "加载失败", Toast.LENGTH_SHORT).show();
            }
            actionBar.endImageRefresh();
        }
    }

    private void initReview() {
        mNameTv.setText(mResultVo.xm);
        switch (mResultVo.xb) {
            case "男":
                mSexIv.setVisibility(View.VISIBLE);
                mSexIv.setImageResource(R.drawable.man);
                break;
            case "女":
                mSexIv.setVisibility(View.VISIBLE);
                mSexIv.setImageResource(R.drawable.woman);
                break;
            default:
                mSexIv.setVisibility(View.INVISIBLE);
        }
        if (null == mResultVo.nl) {
            mAgeTv.setVisibility(View.INVISIBLE);
        } else {
            mAgeTv.setVisibility(View.VISIBLE);
            mAgeTv.setText(mResultVo.nl + "岁");
        }
        mNumTv.setText(tjdh);
        if (!StringUtil.isEmpty(mResultVo.dwmc)) {
            mDeptTv.setText(mResultVo.dwmc);
        }
        if (!StringUtil.isEmpty(mResultVo.tjrq)) {
            mDateTv.setText(mResultVo.tjrq);
        }
        if (!StringUtil.isEmpty(mResultVo.zs)) {
            mSummarizeTv.setText(Html.fromHtml(mResultVo.zs));
        }
        if (!StringUtil.isEmpty(mResultVo.jy)) {
            mSuggestTv.setText(Html.fromHtml(mResultVo.jy));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AsyncTaskUtil.cancelTask(mDataTask);
    }
}
