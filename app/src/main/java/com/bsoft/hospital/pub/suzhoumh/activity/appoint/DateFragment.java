package com.bsoft.hospital.pub.suzhoumh.activity.appoint;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.util.AsyncTaskUtil;
import com.app.tanklib.view.BsoftActionBar;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.app.appoint.AppointSelectDateActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.appoint.model.AppointExpertSelectDate;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.calender.CalendarAdapter;
import com.bsoft.hospital.pub.suzhoumh.fragment.index.BaseFragment;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.app.appoint.AppointDateVo;
import com.bsoft.hospital.pub.suzhoumh.model.app.appoint.AppointDeptVo;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * @author :lizhengcao
 * @date :2017/3/7
 * E-mail:lizc@bsoft.com.cn
 * @类说明 选日期
 */

public class DateFragment extends BaseFragment implements View.OnClickListener {

    private AppointDeptVo dept;

    private GestureDetector gestureDetector = null;
    private CalendarAdapter calV = null;
    private ViewFlipper flipper = null;
    private GridView gridView = null;
    private static int jumpMonth = 0; // 每次滑动，增加或减去一个月,默认为0（即显示当前月）
    private static int jumpYear = 0; // 滑动跨越一年，则增加或者减去一年,默认为0(即当前年)
    private int year_c = 0;
    private int month_c = 0;
    private int day_c = 0;
    private String currentDate = "";
    /**
     * 挂号日期元素一次排序的下标
     */

    private int mPositionIndex = 0;

    /**
     * 每次添加gridview到viewflipper中时给的标记
     */
    private int gvFlag = 0;
    /**
     * 当前的年月，现在日历顶端
     */
    private TextView currentMonth;
    /**
     * 上个月
     */
    private ImageView prevMonth;
    /**
     * 下个月
     */
    private ImageView nextMonth;


    //医生就诊时间
    private List<String> dataList = new ArrayList<>();
    private List<String> indexDateList = new ArrayList<>();
    private GetDataTask task;
    private List<AppointExpertSelectDate> mList;
    private String zblb;
    private String yysj;
    private BsoftActionBar actionBar;

    public DateFragment() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
        currentDate = sdf.format(date); // 当期日期
        year_c = Integer.parseInt(currentDate.split("-")[0]);
        month_c = Integer.parseInt(currentDate.split("-")[1]);
        day_c = Integer.parseInt(currentDate.split("-")[2]);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_date, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initView(view);
        initData();
        setListener();
    }

    private void setListener() {
        prevMonth.setOnClickListener(this);
        nextMonth.setOnClickListener(this);

        gestureDetector = new GestureDetector(getActivity(), new MyGestureListener());
        flipper.removeAllViews();
        calV = new CalendarAdapter(getActivity(), getResources(), jumpMonth, jumpYear, year_c, month_c, day_c, dataList);
        addGridView();
        gridView.setAdapter(calV);
        flipper.addView(gridView, 0);
        addTextToTopTextView(currentMonth);
    }

    private void initData() {
        mList = new ArrayList<>();
        dept = (AppointDeptVo) getActivity().getIntent().getSerializableExtra("dept");
        task = new GetDataTask();
        task.execute();
    }

    private void initView(View view) {
        actionBar= (BsoftActionBar) getActivity().findViewById(R.id.actionbar);
        currentMonth = (TextView) view.findViewById(R.id.currentMonth);
        prevMonth = (ImageView) view.findViewById(R.id.prevMonth);
        nextMonth = (ImageView) view.findViewById(R.id.nextMonth);
        flipper = (ViewFlipper) view.findViewById(R.id.flipper);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nextMonth: // 下一个月
                enterNextMonth(gvFlag);
                break;
            case R.id.prevMonth: // 上一个月
                enterPrevMonth(gvFlag);
                break;
            default:
                break;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        AsyncTaskUtil.cancelTask(task);
    }

    /**
     * 获取日期
     *
     * @author Administrator
     */
    class GetDataTask extends AsyncTask<Void, Void, ResultModel<List<AppointExpertSelectDate>>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            actionBar.startTextRefresh();
        }

        @Override
        protected void onPostExecute(ResultModel<List<AppointExpertSelectDate>> result) {
            actionBar.endTextRefresh();
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    if (null != result.list && result.list.size() > 0) {
                        mList.addAll(result.list);
                        for (int i = 0; i < mList.size(); i++) {
                            String str = mList.get(i).gzrq;
                            String year = str.split("-")[0];
                            String month = str.split("-")[1];
                            String day = str.split("-")[2];
                            dataList.add(Integer.parseInt(year) + "-"
                                    + Integer.parseInt(month) + "-"
                                    + Integer.parseInt(day));
                        }
                        zblb = mList.get(0).zblb;
                        calV.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getActivity(), "当前医生无排班", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    result.showToast(getActivity());
                }
            }
        }

        @Override
        protected ResultModel<List<AppointExpertSelectDate>> doInBackground(
                Void... params) {
            HashMap<String, String> map = new HashMap<>();
            map.put("method", "listyysjxz");
            map.put("as_ksdm", dept.ksdm);
            return HttpApi.getInstance().parserArray_His(AppointExpertSelectDate.class,
                    "hiss/ser", map,
                    new BsoftNameValuePair("id", loginUser.id),
                    new BsoftNameValuePair("sn", loginUser.sn));
        }
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            int gvFlag = 0; // 每次添加gridview到viewflipper中时给的标记
            if (e1.getX() - e2.getX() > 120) {
                // 像左滑动
                enterNextMonth(gvFlag);
                return true;
            } else if (e1.getX() - e2.getX() < -120) {
                // 向右滑动
                enterPrevMonth(gvFlag);
                return true;
            }
            return false;
        }
    }

    /**
     * 移动到下一个月
     *
     * @param gvFlag
     */
    private void enterNextMonth(int gvFlag) {
        addGridView(); // 添加一个gridView
        jumpMonth++; // 下一个月

        calV = new CalendarAdapter(getActivity(), this.getResources(), jumpMonth, jumpYear, year_c, month_c, day_c, dataList);
        gridView.setAdapter(calV);
        addTextToTopTextView(currentMonth); // 移动到下一月后，将当月显示在头标题中
        gvFlag++;
        flipper.addView(gridView, gvFlag);
        flipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.push_left_in));
        flipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.push_left_out));
        flipper.showNext();
        flipper.removeViewAt(0);
    }

    /**
     * 移动到上一个月
     *
     * @param gvFlag
     */
    private void enterPrevMonth(int gvFlag) {
        addGridView(); // 添加一个gridView
        jumpMonth--; // 上一个月

        calV = new CalendarAdapter(getActivity(), this.getResources(), jumpMonth, jumpYear, year_c, month_c, day_c, dataList);
        gridView.setAdapter(calV);
        gvFlag++;
        addTextToTopTextView(currentMonth); // 移动到上一月后，将当月显示在头标题中
        flipper.addView(gridView, gvFlag);

        flipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.push_right_in));
        flipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.push_right_out));
        flipper.showPrevious();
        flipper.removeViewAt(0);
    }

    /**
     * 添加头部的年份 闰哪月等信息
     *
     * @param view
     */
    public void addTextToTopTextView(TextView view) {
        StringBuffer textDate = new StringBuffer();
        textDate.append(calV.getShowYear()).append("年").append(calV.getShowMonth()).append("月").append("\t");
        view.setText(textDate);
    }

    private void addGridView() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT);
        // 取得屏幕的宽度和高度
        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int Width = display.getWidth();
        int Height = display.getHeight();

        gridView = new GridView(getActivity());
        gridView.setNumColumns(7);
        gridView.setColumnWidth(40);
        if (Width == 720 && Height == 1280) {
            gridView.setColumnWidth(40);
        }
        gridView.setGravity(Gravity.CENTER_VERTICAL);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        // 去除gridView边框
        gridView.setVerticalSpacing(1);
        gridView.setHorizontalSpacing(1);
        gridView.setOnTouchListener(new View.OnTouchListener() {
            // 将gridview中的触摸事件回传给gestureDetector

            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int startPosition = calV.getStartPositon();
                int endPosition = calV.getEndPosition();
                String scheduleDay = calV.getDateByClickItem(position).split("\\.")[0]; // 这一天的阳历
                String scheduleYear = calV.getShowYear();
                String scheduleMonth = calV.getShowMonth();
                String strShowDate;

                if (position < startPosition - 7) {
                    strShowDate = scheduleYear + "-" + (Integer.parseInt(scheduleMonth) - 1) + "-" + scheduleDay;
                } else if (position > endPosition - 7) {
                    strShowDate = scheduleYear + "-" + (Integer.parseInt(scheduleMonth) + 1) + "-" + scheduleDay;
                } else {
                    strShowDate = scheduleYear + "-" + scheduleMonth + "-" + scheduleDay;
                }

                if (dataList.contains(strShowDate)) {
                    DecimalFormat df = new DecimalFormat("00");
                    yysj = strShowDate.split("-")[0]+"-"
                            +df.format(Integer.parseInt(strShowDate.split("-")[1]))+"-"
                            +df.format(Integer.parseInt(strShowDate.split("-")[2]));
                    Intent intent = new Intent(getActivity(), AppointExpertInfoActivity.class);
                    intent.putExtra("yysj", yysj);
                    intent.putExtra("dept", dept);
                    intent.putExtra("zblb",zblb);
                    startActivity(intent);
                }

            }
        });
        gridView.setLayoutParams(params);
    }

    @Override
    public void startHint() {

    }

    @Override
    public void endHint() {

    }
}
