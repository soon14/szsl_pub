package com.bsoft.hospital.pub.suzhoumh.fragment.index;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.service.quicksettings.TileService;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.util.AsyncTaskUtil;
import com.app.tanklib.view.BsoftActionBar;
import com.bsoft.hospital.pub.suzhoumh.AppApplication;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.account.MipcaActivityCapture;
import com.bsoft.hospital.pub.suzhoumh.activity.app.appoint.AppointMainActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.app.fee.FeePreListActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.app.healthtool.HealthToolActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.app.hosptial.HosptialPageActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.app.insurance.InsuranceActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.app.more.MoreActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.app.news.HospitalNewsActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.app.news.NewsListActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.app.queue.MyQueueNewActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.app.report.ReportMainActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.app.schedule.DoctorScheduleActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.app.sign.SignListActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.app.physical.PhysicalActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.app.visit.VisitListActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.charge.ParkingChargesActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.clinic.ClinicDetailInfoActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.cloud.CloudClinicActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.LoginUser;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.ScanVo;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.StringModel;
import com.bsoft.hospital.pub.suzhoumh.util.ContextUtils;
import com.bsoft.hospital.pub.suzhoumh.util.SpUtil;
import com.bsoft.hospital.pub.suzhoumh.util.pop.CampusSelection;
import com.bsoft.hospital.pub.suzhoumh.util.pop.impl.CampusSelectionPopWindowImpl;
import com.daoyixun.ipsmap.IpsMapSDK;

import java.util.HashMap;

public class HomeFragment extends BaseFragment implements OnClickListener {

    private static final int REQUEST_SCAN = 1;
    LayoutInflater mInflaters;
    LinearLayout today_news;
    PrintTask printTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.activity_application_home1, container, false);
        initView();
        return mainView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    private void initView() {
        findActionBar();
        actionBar.setTitle(Constants.getHospitalName());
        actionBar.setRefreshImageView(R.drawable.btn_scan_n, new BsoftActionBar.Action() {
            @Override
            public int getDrawable() {
                return R.drawable.btn_scan_n;
            }

            @Override
            public void performAction(View view) {
                if (loginUser != null && loginUser.idcard != null && !loginUser.idcard.equals("")) {
                    Intent intent = new Intent(baseContext, MipcaActivityCapture.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityForResult(intent, REQUEST_SCAN);
                } else {
                    Toast.makeText(baseContext, "请先完善个人信息", Toast.LENGTH_SHORT).show();
                }
            }
        });


        mInflaters = LayoutInflater.from(baseContext);

        today_news = (LinearLayout) mainView.findViewById(R.id.today_news);
        today_news.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(baseContext, HospitalNewsActivity.class);
                startActivity(intent);
            }

        });


        mainView.findViewById(R.id.ll_yygh).setOnClickListener(this);
        mainView.findViewById(R.id.ll_qdqh).setOnClickListener(this);
        mainView.findViewById(R.id.ll_jzjh).setOnClickListener(this);
        mainView.findViewById(R.id.ll_zszf).setOnClickListener(this);
        mainView.findViewById(R.id.ll_bgcx).setOnClickListener(this);
        mainView.findViewById(R.id.ll_zjpb).setOnClickListener(this);
        mainView.findViewById(R.id.ll_jzls).setOnClickListener(this);
        mainView.findViewById(R.id.ll_zyyw).setOnClickListener(this);
        //停车缴费
        mainView.findViewById(R.id.ll_tcsf).setOnClickListener(this);
        mainView.findViewById(R.id.ll_yndh).setOnClickListener(this);

        mainView.findViewById(R.id.rl_home_yyjs).setOnClickListener(this);
        mainView.findViewById(R.id.rl_home_jkgj).setOnClickListener(this);
        mainView.findViewById(R.id.rl_home_jktj).setOnClickListener(this);
        mainView.findViewById(R.id.rl_home_sbbx).setOnClickListener(this);
        mainView.findViewById(R.id.btn_hospital_select).setOnClickListener(this);
        mainView.findViewById(R.id.tv_cloud_clinic).setOnClickListener(this);


    }

    @Override
    public void startHint() {

    }

    @Override
    public void endHint() {

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.tv_cloud_clinic:
                //云诊室
                intent = new Intent(baseContext, CloudClinicActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_hospital_select:
                //院区选择按钮 用来切换不同的院区  =========要来选择不同院区的
                new CampusSelectionPopWindowImpl(getContext(), application, false, new CampusSelection() {
                    @Override
                    public void setCampusSelectionDetail(String hospitalName) {
                        actionBar.setTitle(hospitalName);
                    }
                }).setPopWindow();
                break;
            case R.id.ll_tcsf:
                if (Constants.getHttpUrl().equals(Constants.HttpUrlEasternDistrict)) {
                    //停车收费========仅是东区的功能，本部和北区不允许使用
                    intent = new Intent(baseContext, ParkingChargesActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(baseContext, "停车缴费功能仅市立医院东区开放", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.ll_yndh:

                if (Constants.getHttpUrl().equals(Constants.HttpUrlEasternDistrict)) {
                    //院内导航========仅是东区的功能，本部和北区不允许使用
                    IpsMapSDK.openIpsMapActivity(baseContext, Constants.IPSMAP_MAP_ID);
                } else {
                    Toast.makeText(baseContext, "院内导航功能仅市立医院东区开放", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ll_yygh:
                // 预约挂号
                if (loginUser != null && loginUser.idcard != null && !loginUser.idcard.equals("")) {
                    intent = new Intent(baseContext, AppointMainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(baseContext, "请先完善个人信息", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ll_bgcx:
                //报告查询
                if (loginUser != null && loginUser.idcard != null && !loginUser.idcard.equals("")) {
                    intent = new Intent(baseContext, ReportMainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(baseContext, "请先完善个人信息", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ll_zjpb:
                //专家排班
                intent = new Intent(baseContext, DoctorScheduleActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_jzjh://排队叫号
                if (loginUser != null && loginUser.idcard != null && !loginUser.idcard.equals("")) {
                    intent = new Intent(baseContext, MyQueueNewActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(baseContext, "请先完善个人信息", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ll_zszf://诊间支付 掌上支付
                intent = new Intent(baseContext, FeePreListActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_jzls://就诊历史
                if (loginUser.natureJudje() && TextUtils.equals("0", loginUser.isvalidate)) {
                    Toast.makeText(baseContext, "该功能需要签约苏州银行线上支付并认证通过后使用", Toast.LENGTH_LONG).show();
                } else {
                    intent = new Intent(baseContext, VisitListActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ll_zyyw://更多
                intent = new Intent(baseContext, MoreActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_qdqh://便捷寻医  签到取号
                intent = new Intent(baseContext, SignListActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_home_jkgj:
                // 健康工具
                intent = new Intent(baseContext, HealthToolActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_home_jktj:
                // 健康监测  健康体检
                intent = new Intent(baseContext, PhysicalActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_home_yyjs:
                // 医院介绍
                intent = new Intent(baseContext, HosptialPageActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_home_sbbx:
                //商业保险
                if (loginUser != null && loginUser.idcard != null && !loginUser.idcard.equals("")) {
                    intent = new Intent(baseContext, InsuranceActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(baseContext, "请先完善个人信息", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AsyncTaskUtil.cancelTask(printTask);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK)
            return;
        if (requestCode == REQUEST_SCAN) {
            String result = data.getStringExtra("result");
            try {
                ScanVo vo = JSON.parseObject(result, ScanVo.class);
                if (vo != null) {
                    printTask = new PrintTask();
                    printTask.execute(vo.YWLX, vo.JQBH, loginUser.idcard);
                } else {
                    Toast.makeText(baseContext, "请选择苏州市立医院二维码图片", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(baseContext, "请选择苏州市立医院二维码图片", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //打印发票
    private class PrintTask extends AsyncTask<String, Void, ResultModel<StringModel>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            actionBar.startImageRefresh();
        }

        @Override
        protected ResultModel<StringModel> doInBackground(String... params) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("method", "uf_fpbd");
            map.put("as_lx", params[0]);
            map.put("as_sbbh", params[1]);
            map.put("as_sfzh", params[2]);

            return HttpApi.getInstance().parserData_His(StringModel.class, "hiss/ser", map, new BsoftNameValuePair("id", loginUser.id), new BsoftNameValuePair("sn", loginUser.sn));
        }

        @Override
        protected void onPostExecute(ResultModel<StringModel> result) {
            super.onPostExecute(result);
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    if (result.data != null && !TextUtils.isEmpty(result.data.message)) {
                        Toast.makeText(baseContext, String.valueOf(result.data.message), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(baseContext, "操作成功", Toast.LENGTH_SHORT).show();
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
}
