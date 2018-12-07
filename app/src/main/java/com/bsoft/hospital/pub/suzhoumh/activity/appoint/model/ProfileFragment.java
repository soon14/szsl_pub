package com.bsoft.hospital.pub.suzhoumh.activity.appoint.model;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.util.AsyncTaskUtil;
import com.app.tanklib.view.BsoftActionBar;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.fragment.index.BaseFragment;
import com.bsoft.hospital.pub.suzhoumh.model.DoctorVo;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.util.Utility;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * @author :lizhengcao
 * @date :2017/3/9
 * E-mail:lizc@bsoft.com.cn
 * @类说明
 */

public class ProfileFragment extends BaseFragment {

    private ImageView riv_head;
    private TextView tv_name;
    private TextView tv_dept;
    private TextView tv_profession;
    private TextView tv_intro;

    private String ygdm;//员工代码
    private DoctorVo doctorVo;
    public ImageLoader imageLoader = ImageLoader.getInstance();
    private BsoftActionBar actionBar;
    private GetDataTask task;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initView(view);
        initData();
    }

    private void initData() {
        ygdm = getActivity().getIntent().getStringExtra("ygdm");
        getHeader();//获取头像
        task = new GetDataTask();
        task.execute();
    }

    private void initView(View view) {
        actionBar = (BsoftActionBar) getActivity().findViewById(R.id.actionbar);
        riv_head = (ImageView) view.findViewById(R.id.riv_doctor_info_head);
        tv_name = (TextView) view.findViewById(R.id.tv_appoint_doctor_name);
        tv_dept = (TextView) view.findViewById(R.id.tv_doctor_info_dept);
        tv_profession = (TextView) view.findViewById(R.id.tv_doctor_info_profession);
        tv_intro = (TextView) view.findViewById(R.id.tv_doctor_info_intro);
    }

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
                            Toast.makeText(getActivity(), "当前医生无介绍", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    result.showToast(getActivity());
                }
            } else {
                Toast.makeText(getActivity(), "数据为空", Toast.LENGTH_SHORT)
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
        final String url = Constants.getHeadUrl() + Constants.getHospitalID() + "/" + ygdm + "_150x150" + ".png";
        // 显示图片的配置
        try {
            new Thread() {
                public void run() {
                    //这儿是耗时操作，完成之后更新UI；
                    if (Utility.checkURL(url)) {
                        getActivity().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                //更新UI
                                DisplayImageOptions options = new DisplayImageOptions.Builder()
                                        .cacheInMemory(true).cacheOnDisk(true)
                                        // .displayer(new RoundedBitmapDisplayer(20))
                                        .bitmapConfig(Bitmap.Config.RGB_565).build();
                                imageLoader.loadImage(url, options,
                                        new SimpleImageLoadingListener() {

                                            @Override
                                            public void onLoadingComplete(String imageUri,
                                                                          View view, Bitmap loadedImage) {
                                                // TODO Auto-generated method stub
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

    @Override
    public void startHint() {

    }

    @Override
    public void endHint() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AsyncTaskUtil.cancelTask(task);
    }
}
