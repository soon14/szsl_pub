package com.bsoft.hospital.pub.suzhoumh.activity.app.appoint;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.view.BsoftActionBar;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.DoctorVo;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.util.Utility;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * “医生介绍”界面（Updated by Wang Cong on 2016/04/19）
 * Created by Administrator on 2015/12/31.
 */
public class AppointDoctorInfoActivity extends BaseActivity {

    private ImageView riv_head;
    private TextView tv_name;
    private TextView tv_dept;
    private TextView tv_profession;
    private TextView tv_intro;

    private String ygdm;//员工代码
    private DoctorVo doctorVo;
    public ImageLoader imageLoader = ImageLoader.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointdoctorinfo);
        findView();
        initData();
    }

    @Override
    public void findView() {
        findActionBar();
        actionBar.setTitle("医生介绍");
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

        riv_head = (ImageView) findViewById(R.id.riv_doctor_info_head);
        tv_name = (TextView) findViewById(R.id.tv_appoint_doctor_name);
        tv_dept = (TextView) findViewById(R.id.tv_doctor_info_dept);
        tv_profession = (TextView) findViewById(R.id.tv_doctor_info_profession);
        tv_intro = (TextView) findViewById(R.id.tv_doctor_info_intro);
    }

    private void initData() {
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
        ygdm = getIntent().getStringExtra("ygdm");
        getHeader();//获取头像
        GetDataTask task = new GetDataTask();
        task.execute();
    }

    @SuppressLint("StaticFieldLeak")
    class GetDataTask extends AsyncTask<Void, Void, ResultModel<DoctorVo>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            actionBar.startTextRefresh();
        }

        @Override
        protected ResultModel<DoctorVo> doInBackground(Void... params) {
            return HttpApi.getInstance().parserData(DoctorVo.class,
                    "auth/doctor/getDoctorByOrgAndCode",
                    new BsoftNameValuePair("id", loginUser.id),
                    new BsoftNameValuePair("sn", loginUser.sn),
                    new BsoftNameValuePair("orgId", Constants.getHospitalID()),
                    new BsoftNameValuePair("code", ygdm)
            );
        }

        @Override
        protected void onPostExecute(ResultModel<DoctorVo> result) {
            super.onPostExecute(result);
            actionBar.endTextRefresh();
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    if (null != result.data) {
                        doctorVo = result.data;
                        if (doctorVo.introduce != null && !doctorVo.introduce.equals("")) {
                            setDoctorInfo();
                        } else {
                            Toast.makeText(baseContext, "当前医生无介绍", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    result.showToast(baseContext);
                }
            } else {
                Toast.makeText(baseContext, "数据为空", Toast.LENGTH_SHORT)
                        .show();
            }
        }

        private void setDoctorInfo() {
            tv_name.setText(doctorVo.name);
            tv_dept.setVisibility(View.VISIBLE);
            tv_dept.setText(doctorVo.deptname);
            tv_profession.setText(doctorVo.professionaltitle);
            tv_intro.setText(doctorVo.introduce);
        }
    }

    private void getHeader() {
        riv_head.setImageResource(R.drawable.doc_header);
        final String url =Constants.getHeadUrl() +Constants.getHospitalID() + "/" + ygdm + "_150x150" + ".png";
        // 显示图片的配置
        try {
            new Thread() {
                public void run() {
                    //这儿是耗时操作，完成之后更新UI；
                    if (Utility.checkURL(url)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //更新UI
                                DisplayImageOptions options = new DisplayImageOptions.Builder()
                                        .cacheInMemory(true).cacheOnDisk(true)
                                        .bitmapConfig(Bitmap.Config.RGB_565).build();
                                imageLoader.loadImage(url, options,
                                        new SimpleImageLoadingListener() {
                                            @Override
                                            public void onLoadingComplete(String imageUri,
                                                                          View view, Bitmap loadedImage) {
                                                super.onLoadingComplete(imageUri, view,
                                                        loadedImage);
                                                Drawable drawable = new BitmapDrawable(
                                                        loadedImage);
                                                riv_head.setImageDrawable(drawable);
                                            }

                                        });
                            }

                        });
                    }
                }
            }.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
