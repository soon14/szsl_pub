package com.bsoft.hospital.pub.suzhoumh.activity.app.monitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.util.AsyncTaskUtil;
import com.app.tanklib.view.BsoftActionBar.Action;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.moitor.MonitorBP;
import com.bsoft.hospital.pub.suzhoumh.model.moitor.MonitorBmi;
import com.bsoft.hospital.pub.suzhoumh.model.moitor.MonitorHeight;
import com.bsoft.hospital.pub.suzhoumh.model.moitor.MonitorModel;
import com.bsoft.hospital.pub.suzhoumh.model.moitor.MonitorOxygen;
import com.bsoft.hospital.pub.suzhoumh.model.moitor.MonitorRate;
import com.bsoft.hospital.pub.suzhoumh.model.moitor.MonitorSetting;
import com.bsoft.hospital.pub.suzhoumh.model.moitor.MonitorSports;
import com.bsoft.hospital.pub.suzhoumh.model.moitor.MonitorSugar;
import com.bsoft.hospital.pub.suzhoumh.model.moitor.MonitorWaist;
import com.bsoft.hospital.pub.suzhoumh.model.moitor.MonitorWeight;
import com.bsoft.hospital.pub.suzhoumh.util.DateUtil;
import com.github.mikephil.charting.charts.BarLineChartBase.BorderPosition;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.XLabels;
import com.github.mikephil.charting.utils.XLabels.XLabelPosition;

/**
 * 健康监测
 *
 * @author Administrator
 */
public class MonitorActivity extends BaseActivity {
    private LinearLayout mainView;
    private LayoutInflater mLayoutInflater;
    private ArrayList<MonitorSetting> datas;
    private ArrayList<MonitorSetting> flagDatas;
    public ArrayList<LineChart> chartList;
    public HashMap<Integer, ArrayList<MonitorModel>> map = new HashMap<Integer, ArrayList<MonitorModel>>();
    private GetDataTask task;
    private int colors[] = new int[]{R.color.monitor_2, R.color.monitor_3, R.color.monitor_4, R.color.monitor_5,
            R.color.monitor_6, R.color.monitor_7, R.color.monitor_8, R.color.monitor_9};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.monitor);
        this.mLayoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.MonitorChange_ACTION);
        this.registerReceiver(this.broadcastReceiver, filter);
        findView();
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constants.MonitorChange_ACTION.equals(intent.getAction())) {
                view();
                if (flagDatas.size() > 0) {
                    task = new GetDataTask();
                    task.execute();
                }
            }
        }
    };

    @Override
    public void findView() {
        findActionBar();
        actionBar.setTitle("健康监测");
        actionBar.setBackAction(new Action() {
            @Override
            public void performAction(View view) {
                back();
            }

            @Override
            public int getDrawable() {
                return R.drawable.btn_back;
            }
        });
        actionBar.setRefreshTextView("编辑", new Action() {

            @Override
            public void performAction(View view) {
                Intent intent = new Intent(baseContext,
                        MonitorSettingActivity.class);
                startActivityForResult(intent, 110);
            }

            @Override
            public int getDrawable() {
                return 0;
            }
        });

        mainView = (LinearLayout) findViewById(R.id.mainView);
        view();
        if (flagDatas.size() > 0) {
            task = new GetDataTask();
            task.execute();
        }
    }

    public void view() {
        datas = application.getMonitorSetting();
        flagDatas = new ArrayList<MonitorSetting>();
        chartList = new ArrayList<LineChart>();
        mainView.removeAllViews();
        for (MonitorSetting vo : datas) {
            if (vo.flag == 1) {
                flagDatas.add(vo);
                mainView.addView(createView(vo));
            }
        }
    }

    private View createView(final MonitorSetting model) {
        View view = mLayoutInflater.inflate(R.layout.monitor_item, null);
        ((ImageView) view.findViewById(R.id.icon)).setImageResource(MonitorUtils.getImageId(model.id));
        ((TextView) view.findViewById(R.id.name)).setText(model.name);
        ((TextView) view.findViewById(R.id.unit)).setText(model.unit);
        view.findViewById(R.id.monitorLay).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        Toast.makeText(baseContext, "111111", Toast.LENGTH_LONG)
                                .show();
                    }
                });
        LineChart mChart = (LineChart) view.findViewById(R.id.chart);
        // 设置单位
        //mChart.setUnit(model.unit);
        mChart.setDrawUnitsInChart(true);
        mChart.setStartAtZero(false);
        // 设置是否显示y轴的值的数据
        mChart.setDrawYValues(false);
        mChart.setDrawBorder(true);
        mChart.setBorderPositions(new BorderPosition[]{BorderPosition.LEFT,
                BorderPosition.BOTTOM});

        XLabels xl = mChart.getXLabels();
        //xl.setCenterXLabelText(true);
        xl.setPosition(XLabelPosition.BOTTOM);

        // 设置表格的描述
        mChart.setDescription("");
        // 如果没有数据的时候，会显示这个，类似listview的emtpyview
        //mChart.setNoDataTextDescription("加载中...");
        mChart.setNoDataTextDescription("没有数据");
        mChart.setHighlightEnabled(true);
        mChart.setTouchEnabled(true);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setPinchZoom(true);
        // MyMarkerView mv = new MyMarkerView(this,
        // R.layout.custom_marker_view);
        // mChart.setMarkerView(mv);
        mChart.setHighlightIndicatorEnabled(false);
        // 设置标示，就是那个一组y的value的
        // Legend l = mChart.getLegend();
        // l.setForm(LegendForm.LINE);
        // 动画 , 如果调用动画方法后，就没有必要调用invalidate（）方法，来刷新界面了
        mChart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(baseContext,
                        MonitorInfoActivity.class);
                intent.putExtra("model", model);
                Bundle bundle = new Bundle();
                bundle.putSerializable("list", map.get(model.id));
                intent.putExtra("bundle", bundle);
                startActivity(intent);
            }
        });
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(baseContext,
                        MonitorInfoActivity.class);
                intent.putExtra("model", model);
                Bundle bundle = new Bundle();
                bundle.putSerializable("list", map.get(model.id));
                intent.putExtra("bundle", bundle);
                startActivity(intent);
            }
        });
        chartList.add(mChart);
        return view;
    }


    // 得到X轴数据
    public ArrayList<String> getXVals(ArrayList<MonitorModel> list) {
        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++) {
//			xVals.add(DateUtil.getDateTime("MM-dd HH:mm",
//					list.get(i).createdate));
            xVals.add(DateUtil.getDateTime("MM-dd",
                    list.get(i).createdate));
        }
        return xVals;
    }

    // 得到Y轴数据
    public ArrayList<LineDataSet> getYVals(int type,
                                           ArrayList<MonitorModel> list) {
        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        if (type == 1) {
            // 血压- 2跳线
            ArrayList<Entry> yVals1 = new ArrayList<Entry>();
            ArrayList<Entry> yVals2 = new ArrayList<Entry>();
            for (int i = 0; i < list.size(); i++) {
                MonitorBP bp = JSON.parseObject(list.get(i).monitorinfo, MonitorBP.class);
                yVals1.add(new Entry(bp.sbp, i));
                yVals2.add(new Entry(bp.dbp, i));
            }
            LineDataSet set1 = new LineDataSet(yVals1, "收缩压");

            set1.enableDashedLine(10f, 5f, 0f);
            set1.setColor(getResources().getColor(R.color.monitor_1_1));
            set1.setCircleColor(getResources().getColor(R.color.monitor_1_1));
            set1.setLineWidth(1f);
            set1.setCircleSize(4f);
            set1.setFillAlpha(65);
            set1.setFillColor(Color.BLACK);

            LineDataSet set2 = new LineDataSet(yVals2, "舒张压");
            set2.enableDashedLine(10f, 5f, 0f);
            set2.setColor(getResources().getColor(R.color.monitor_1_2));
            set2.setCircleColor(getResources().getColor(R.color.monitor_1_2));
            set2.setLineWidth(1f);
            set2.setCircleSize(4f);
            set2.setFillAlpha(65);
            set2.setFillColor(Color.BLACK);

            dataSets.add(set1);
            dataSets.add(set2);
        } else {
            ArrayList<Entry> yVals = new ArrayList<Entry>();
            for (int i = 0; i < list.size(); i++) {
                yVals.add(new Entry(getValue(type, list.get(i)), i));
            }
            LineDataSet set1 = new LineDataSet(yVals, getName(type));

            set1.enableDashedLine(10f, 5f, 0f);
            set1.setColor(getResources().getColor(colors[type - 2]));
            set1.setCircleColor(getResources().getColor(colors[type - 2]));
            set1.setLineWidth(1f);
            set1.setCircleSize(4f);
            set1.setFillAlpha(65);
            set1.setFillColor(Color.BLACK);

            dataSets.add(set1);
        }

        return dataSets;
    }

    public float getValue(int type, MonitorModel vo) {
        switch (type) {
            case 2:
                MonitorSugar sugar = JSON.parseObject(vo.monitorinfo, MonitorSugar.class);
                return sugar.sugar;
            case 3:
                MonitorWeight weight = JSON.parseObject(vo.monitorinfo, MonitorWeight.class);
                return weight.weight;
            case 4:
                MonitorHeight height = JSON.parseObject(vo.monitorinfo, MonitorHeight.class);
                return height.height;
            case 5:
                MonitorBmi bmi = JSON.parseObject(vo.monitorinfo, MonitorBmi.class);
                return bmi.bmi;
            case 6:
                MonitorRate rate = JSON.parseObject(vo.monitorinfo, MonitorRate.class);
                return rate.rate;
            case 7:
                MonitorWaist waist = JSON.parseObject(vo.monitorinfo, MonitorWaist.class);
                return waist.waist;
            case 8:
                MonitorSports sports = JSON.parseObject(vo.monitorinfo, MonitorSports.class);
                return sports.sports;
            case 9:
                MonitorOxygen oxygen = JSON.parseObject(vo.monitorinfo, MonitorOxygen.class);
                return oxygen.oxygen;
            default:
                return 0;
        }
    }


    /**
     * 1血压:整数，mmhg 2血糖:支持1位小数，mmol/L 3体重：支持2位小数，Kg 4身高：支持1位小数，m
     * 5BMI：支持2位小数，Kg/m^2 6心率：整数，单位：bpm 7腰围：支持1位小数，单位cm 8运动指数：整数，单位：步
     * 9血氧：整数，单位：%
     */
    public String getName(int type) {
        switch (type) {
            case 1:
                return "血压";
            case 2:
                return "血糖";
            case 3:
                return "体重";
            case 4:
                return "身高";
            case 5:
                return "BMI";
            case 6:
                return "心率";
            case 7:
                return "腰围";
            case 8:
                return "运动指数";
            case 9:
                return "血氧";
            default:
                return "";
        }
    }

    // 得到图标数据
    public LineData getDate(int type, ArrayList<MonitorModel> list, String name) {
        Collections.sort(list);
        return new LineData(getXVals(list), getYVals(type, list));
    }

    private class GetDataTask extends
            AsyncTask<Void, Void, ResultModel<ArrayList<MonitorModel>>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            actionBar.startTextRefresh();
        }

        @Override
        protected ResultModel<ArrayList<MonitorModel>> doInBackground(
                Void... params) {
            String strId="";
            for (MonitorSetting vo : flagDatas) {
				if (vo.flag == 1) {
                    strId+=vo.id+",";
				}
			}
            return HttpApi.getInstance().parserArray(MonitorModel.class,
					"auth/health/monitor/list",
                    new BsoftNameValuePair("mids",strId),
                    new BsoftNameValuePair("id", loginUser.id),
                    new BsoftNameValuePair("sn", loginUser.sn)
                   );
        }

        @Override
        protected void onPostExecute(ResultModel<ArrayList<MonitorModel>> result) {
            super.onPostExecute(result);
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    if (null != result.list && result.list.size() > 0) {
                        map.clear();
                        for (MonitorModel m : result.list) {
                            if (map.containsKey(m.monitortype)) {
                                map.get(m.monitortype).add(m);
                            } else {
                                ArrayList<MonitorModel> l = new ArrayList<MonitorModel>();
                                l.add(m);
                                map.put(m.monitortype, l);
                            }
                        }
                        for (int i = 0; i < flagDatas.size(); i++) {
                            if (map.get(flagDatas.get(i).id) != null) {
                                chartList.get(i).setData(
                                        getDate(flagDatas.get(i).id,
                                                map.get(flagDatas.get(i).id),
                                                flagDatas.get(i).name));
                                chartList.get(i).animateX(2000);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        AsyncTaskUtil.cancelTask(task);
        if (null != this.broadcastReceiver) {
            this.unregisterReceiver(this.broadcastReceiver);
            broadcastReceiver = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 110) {
                view();
            }
        }
    }
}
