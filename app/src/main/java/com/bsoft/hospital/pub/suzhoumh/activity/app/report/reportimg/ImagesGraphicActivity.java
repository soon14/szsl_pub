package com.bsoft.hospital.pub.suzhoumh.activity.app.report.reportimg;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.photoview.PhotoView;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bumptech.glide.Glide;

import java.util.HashMap;

/**
 * @author :lizhengcao
 * @date :2017/3/16
 * E-mail:lizc@bsoft.com.cn
 * @类说明 影像图片主页面
 */

public class ImagesGraphicActivity extends BaseActivity {
    private String mSqdh;
    private String mTpdz;
    private GetDataTask task;
    private ImageButton ibBack;
    private TextView tvTitle;
    private ProgressBar pBar;
    private int mCurPosition;
    private PhotoView mPV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphic_image);
        initView();
        initData();
        setListener();
    }

    private void setListener() {

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        tvTitle.setText("影像图片（" + (mCurPosition + 1) + "）");
        task = new GetDataTask();
        task.execute();
    }

    private void initView() {
        mSqdh = getIntent().getStringExtra("sqdh");
        mTpdz = getIntent().getStringExtra("tpdz");
        mCurPosition = getIntent().getIntExtra("position", -1);

        ibBack = (ImageButton) findViewById(R.id.ib_back);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        pBar = (ProgressBar) findViewById(R.id.pbar);
        mPV = (PhotoView) findViewById(R.id.photo_view);
    }

    class GetDataTask extends AsyncTask<String, Void, ResultModel> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ResultModel doInBackground(String... params) {
            HashMap<String, String> map = new HashMap<>();
            map.put("method", "yxcx");
            map.put("as_spdh", mSqdh);
            map.put("as_tpdz", mTpdz);
            return HttpApi.getInstance().parserData_His(ResultModel.class, "hiss/ser", map,
                    new BsoftNameValuePair("id", loginUser.id),
                    new BsoftNameValuePair("sn", loginUser.sn));
        }

        @Override
        protected void onPostExecute(ResultModel result) {
            super.onPostExecute(result);
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
//                    mPV.setImageBitmap(BitmapUtils.stringtoBitmap(result.data.toString()));
                    Glide.with(baseContext)
                            .load(result.data.toString())
                            .into(mPV);
                } else {
                    result.showToast(baseContext);
                }
            } else {
                Toast.makeText(baseContext, "加载失败", Toast.LENGTH_SHORT).show();
            }
            pBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void findView() {

    }

}
