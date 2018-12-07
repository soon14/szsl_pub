package com.bsoft.hospital.pub.suzhoumh.activity.app.monitor;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;

import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;

import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.util.AsyncTaskUtil;
import com.app.tanklib.util.StringUtil;
import com.app.tanklib.view.BsoftActionBar.Action;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.NullModel;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.moitor.MonitorSetting;

/**
 * 健康检测修改和添加
 * @author Administrator
 * 
 */
public class MonitorAddActivity extends BaseActivity {

	private DatePickerDialog datePicker;
	private TimePickerDialog timeDialog;
	//private ScrollView scrollView;
	private boolean firstLoad = true;
	private Calendar calendar = Calendar.getInstance();

	private TextView date, time;
	
	//日期和时间
	public String dateStr, timeStr;

	private boolean isShowDate = false;// 日期是否打开
	private boolean isShowTime = false;// 时间是否打开
	//提交
	private SubmitTask task;
	
	private MonitorSetting model;	
	// 测量类型
	private int monitortype = 1;//1血压 2血糖 3体重 4身高 5BMI 6心率 7腰围  8运动指数 9血氧
	private String monitorname = "";
	private String monitorvalue = "";
	private String monitortime = "";
	private long rid;
	
	// 血压 有两个值 ，做特殊处理
	private EditText sbp, dbp;
	//单个值类型的输入
	private LinearLayout ll_value;
	private TextView tv_value;
	private EditText et_value;
	
	private int currentType = 0;//0,添加,1,修改
	private Intent intent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.monitor_add);
		
		intent = getIntent();
		monitortype = intent.getIntExtra("monitortype", 0);
		monitorname = intent.getStringExtra("monitorname");
		monitorvalue = intent.getStringExtra("monitorvalue");
		monitortime = intent.getStringExtra("monitortime");
		currentType = intent.getIntExtra("currentType", 0);
		rid = intent.getLongExtra("rid", 0);
		
		findView();
		initView();
		initData();
	}

	@Override
	public void findView() {
		findActionBar();
		actionBar.setTitle(monitorname);
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
		actionBar.setRefreshTextView("完成", new Action() {

			@Override
			public void performAction(View view) {
				if (valiate()) {
					task = new SubmitTask();
					task.execute();
				} else {
					return;
				}
			}

			@Override
			public int getDrawable() {
				return 0;
			}
		});
		
	}

	private void initView()
	{
		//scrollView = (ScrollView) findViewById(R.id.scrollView);
		date = (TextView) findViewById(R.id.date);
		time = (TextView) findViewById(R.id.time);
		ll_value = (LinearLayout) findViewById(R.id.ll_value);
		tv_value = (TextView) findViewById(R.id.tv_value);
		et_value = (EditText) findViewById(R.id.et_value);
		findViewById(R.id.dateLayout).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null == datePicker) {
					datePicker = new MyDatePickerDialog(baseContext,
							new OnDateSetListener() {
								@Override
								public void onDateSet(DatePicker view,
										int year, int monthOfYear,
										int dayOfMonth) {
									date.setText(getDateStr(year, monthOfYear,
											dayOfMonth));
								}
							}, calendar.get(Calendar.YEAR), calendar
									.get(Calendar.MONTH), calendar
									.get(Calendar.DAY_OF_MONTH));
				}
				datePicker.show();
			}
		});
		findViewById(R.id.timeLayout).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null == timeDialog) {
					timeDialog = new MyTimePickerDialog(baseContext,
							new OnTimeSetListener() {
								@Override
								public void onTimeSet(TimePicker view,
										int hourOfDay, int minute) {
									time.setText(getTimeStr(hourOfDay, minute));
								}
							}, calendar.get(Calendar.HOUR_OF_DAY), calendar
									.get(Calendar.MINUTE), true);
				}
				timeDialog.show();
			}
		});
		/*scrollView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (null != getCurrentFocus()
						&& null != getCurrentFocus().getWindowToken()) {
					((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
							.hideSoftInputFromWindow(getCurrentFocus()
									.getWindowToken(), 0);
				}
				scrollView.setFocusable(true);
				scrollView.setFocusableInTouchMode(true);
				scrollView.requestFocus();
				return false;
			}
		});*/
	}
	
	private void initData()
	{
		createMonitorView();
		if(currentType == 1)//修改数据
		{
			if(monitortype==1)//血压
			{
				String str[] = monitorvalue.split("_");
				sbp.setText(str[0]);
				dbp.setText(str[1]);
				sbp.setSelection(sbp.getText().toString().length());
			}
			else//其他
			{
				et_value.setText(monitorvalue);
				et_value.setSelection(et_value.getText().toString().length());
			}
			String arr[] = monitortime.split(" ");
			dateStr = arr[0];
			timeStr = arr[1];
			date.setText(dateStr);
			time.setText(timeStr);
		}
	}
	
	private void createMonitorView() {
		switch (monitortype) {
		case 1:
			sbp = (EditText) findViewById(R.id.sbp);
			dbp = (EditText) findViewById(R.id.dbp);
			findViewById(R.id.xyLay).setVisibility(View.VISIBLE);
			break;
		case 2:
			ll_value.setVisibility(View.VISIBLE);
			tv_value.setText("血糖");
			et_value.setHint("输入血糖");
			et_value.setInputType(EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
			break;
		case 3:
			ll_value.setVisibility(View.VISIBLE);
			tv_value.setText("体重");
			et_value.setHint("输入体重");
			et_value.setInputType(EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
			break;
		case 4:
			ll_value.setVisibility(View.VISIBLE);
			tv_value.setText("身高");
			et_value.setHint("输入身高");
			et_value.setInputType(EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
			break;
		case 5:
			ll_value.setVisibility(View.VISIBLE);
			tv_value.setText("BMI");
			et_value.setHint("输入BMI");
			break;
		case 6:
			ll_value.setVisibility(View.VISIBLE);
			tv_value.setText("心率");
			et_value.setHint("输入心率");
			break;
		case 7:
			ll_value.setVisibility(View.VISIBLE);
			tv_value.setText("腰围");
			et_value.setHint("输入腰围");
			et_value.setInputType(EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
			break;
		case 8:
			ll_value.setVisibility(View.VISIBLE);
			tv_value.setText("运动指数");
			et_value.setHint("输入运动指数");
			break;
		case 9:
			ll_value.setVisibility(View.VISIBLE);
			tv_value.setText("血氧");
			et_value.setHint("输入血氧");
			break;
		default:
			break;
		}
		
	}

	public boolean valiate() {
			if (StringUtil.isEmpty(dateStr)) {
				Toast.makeText(baseContext, "日期请选择", Toast.LENGTH_SHORT).show();
				return false;
			}
			if (StringUtil.isEmpty(timeStr)) {
				Toast.makeText(baseContext, "时间请选择", Toast.LENGTH_SHORT).show();
				return false;
			}
			switch (monitortype) {
			case 1:
				if (StringUtil.isEmpty(sbp.getText().toString())) {
					Toast.makeText(baseContext, "请填收缩压", Toast.LENGTH_SHORT).show();
					showInput(sbp);
					return false;
				}
				if (StringUtil.isEmpty(dbp.getText().toString())) {
					Toast.makeText(baseContext, "请填写舒张压", Toast.LENGTH_SHORT)
							.show();
					showInput(dbp);
					return false;
				}
				
				break;
			case 2:
				if (StringUtil.isEmpty(et_value.getText().toString())) {
					Toast.makeText(baseContext, "请填血糖", Toast.LENGTH_SHORT).show();
					showInput(et_value);
					return false;
				}
				break;
			case 3:
				if (StringUtil.isEmpty(et_value.getText().toString())) {
					Toast.makeText(baseContext, "请填体重", Toast.LENGTH_SHORT).show();
					showInput(et_value);
					return false;
				}
				break;
			case 4:
				 if (StringUtil.isEmpty(et_value.getText().toString())) {
				 Toast.makeText(baseContext, "请填身高", Toast.LENGTH_SHORT).show();
				 showInput(et_value);
				 return false;
				 }
				break;
			case 5:
				if (StringUtil.isEmpty(et_value.getText().toString())) {
					Toast.makeText(baseContext, "请填BMI", Toast.LENGTH_SHORT).show();
					showInput(et_value);
					return false;
				}
				break;
			case 6:
				if (StringUtil.isEmpty(et_value.getText().toString())) {
					Toast.makeText(baseContext, "请填心率", Toast.LENGTH_SHORT).show();
					showInput(et_value);
					return false;
				}
				break;
			case 7:
				if (StringUtil.isEmpty(et_value.getText().toString())) {
					Toast.makeText(baseContext, "请填腰围", Toast.LENGTH_SHORT).show();
					showInput(et_value);
					return false;
				}
				break;
			case 8:
				if (StringUtil.isEmpty(et_value.getText().toString())) {
					Toast.makeText(baseContext, "请填运动指数", Toast.LENGTH_SHORT).show();
					showInput(et_value);
					return false;
				}
				break;
			case 9:
				if (StringUtil.isEmpty(et_value.getText().toString())) {
					Toast.makeText(baseContext, "请填血氧", Toast.LENGTH_SHORT).show();
					showInput(et_value);
					return false;
				}
				break;
			default:
				break;
			}
			switch(monitortype)
			{
			case 1:
				if(!NumberVerify(sbp.getText().toString())||!NumberVerify(dbp.getText().toString()))
				{
					Toast.makeText(baseContext, "数据格式不正确", Toast.LENGTH_SHORT).show();
					return false;
				}
				break;
			default:
				if(!NumberVerify(et_value.getText().toString()))
				{
					Toast.makeText(baseContext, "数据格式不正确", Toast.LENGTH_SHORT).show();
					return false;
				}
				break;
			}
			return true;
	}

	public String getDateStr(int year, int monthOfYear, int dayOfMonth) {
		StringBuffer sb = new StringBuffer();
		sb.append(year).append("-");
		sb.append(String.format("%02d", monthOfYear + 1)).append("-");
		sb.append(String.format("%02d", dayOfMonth));
		dateStr = sb.toString();
		return sb.toString();
	}

	public String getTimeStr(int hourOfDay, int minute) {
		StringBuffer sb = new StringBuffer();
		sb.append(String.format("%02d", hourOfDay)).append(":");
		sb.append(String.format("%02d", minute));
		timeStr = sb.toString();
		return sb.toString();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		AsyncTaskUtil.cancelTask(task);
	}

	class MyDatePickerDialog extends DatePickerDialog {

		public MyDatePickerDialog(Context context, OnDateSetListener callBack,
				int year, int monthOfYear, int dayOfMonth) {
			super(context, callBack, year, monthOfYear, dayOfMonth);
		}

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			isShowDate = true;
			super.onCreate(savedInstanceState);
			getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		}

		@Override
		protected void onStop() {
			isShowDate = false;
			super.onStop();
		}
	}

	class MyTimePickerDialog extends TimePickerDialog {

		public MyTimePickerDialog(Context context,
				TimePickerDialog.OnTimeSetListener callBack, int hourOfDay,
				int minute, boolean is24HourView) {
			super(context, callBack, hourOfDay, minute, is24HourView);
		}

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			isShowTime = true;
			super.onCreate(savedInstanceState);
			getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		}

		@Override
		protected void onStop() {
			isShowTime = false;
			super.onStop();
		}
	}

	private void showInput(EditText view) {
		((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
				.toggleSoftInput(InputMethodManager.SHOW_FORCED,
						InputMethodManager.HIDE_IMPLICIT_ONLY);
		view.requestFocus();
	}

	public String getMonitorInfo() {
		JSONObject ob = new JSONObject();
		try {
			switch (monitortype) {
			case 1:
				ob.put("sbp", sbp.getText().toString());
				ob.put("dbp", dbp.getText().toString());
				ob.put("unit", "mmhg");
				break;
			case 2:
				ob.put("sugar", et_value.getText().toString());
				ob.put("unit", "mmol/L");
				break;
			case 3:
				ob.put("weight", et_value.getText().toString());
				ob.put("unit", "Kg");
				break;
			case 4:
				ob.put("height", et_value.getText().toString());
				ob.put("unit", "m");
				break;
			case 5:
				ob.put("BMI", et_value.getText().toString());
				ob.put("unit", "Kg/m^2");
				break;
			case 6:
				ob.put("rate", et_value.getText().toString());
				ob.put("unit", "bpm");
				break;
			case 7:
				ob.put("waist",et_value.getText().toString());
				ob.put("unit", "cm");
				break;
			case 8:
				ob.put("sports",et_value.getText().toString());
				ob.put("unit", "步");
				break;
			case 9:
				ob.put("oxygen",et_value.getText().toString());
				ob.put("unit", "%");
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ob.toString();
	}

	class SubmitTask extends AsyncTask<Void, Object, ResultModel<NullModel>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			actionBar.startTextRefresh();
		}

		@Override
		protected ResultModel<NullModel> doInBackground(Void... params) {
			try
			{
				//添加
				if(currentType == 0)
				{
					return HttpApi
							.getInstance()
							.parserData(
									NullModel.class,
									"auth/health/monitor/add",
									new BsoftNameValuePair("devicesn", ""),
									new BsoftNameValuePair("monitorinfo",
											getMonitorInfo()),
									new BsoftNameValuePair("monitortype", String
											.valueOf(monitortype)),
									new BsoftNameValuePair("monitorsource", "2"),
									new BsoftNameValuePair("idcard", loginUser.idcard),
									new BsoftNameValuePair("uname", loginUser.realname),
									new BsoftNameValuePair("id", loginUser.id),
									new BsoftNameValuePair("sn", loginUser.sn),
									new BsoftNameValuePair("busdate",dateStr+" "+timeStr)
									);
				}
				//修改
				else
				{
					return HttpApi
							.getInstance()
							.parserData(
									NullModel.class,
									"auth/health/monitor/update",
									new BsoftNameValuePair("rid",String.valueOf(rid)),
									new BsoftNameValuePair("devicesn", ""),
									new BsoftNameValuePair("monitorinfo",
											getMonitorInfo()),
									new BsoftNameValuePair("monitortype", String
											.valueOf(monitortype)),
									new BsoftNameValuePair("monitorsource", "2"),
									new BsoftNameValuePair("idcard", loginUser.idcard),
									new BsoftNameValuePair("uname", loginUser.realname),
									new BsoftNameValuePair("id", loginUser.id),
									new BsoftNameValuePair("sn", loginUser.sn),
									new BsoftNameValuePair("busdate",dateStr+" "+timeStr)
									);
				}
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(ResultModel<NullModel> result) {
			actionBar.endTextRefresh();
			if (null != result) {
				if (result.statue == Statue.SUCCESS) {
					sendBroadcast(new Intent(
							Constants.MonitorChange_ACTION));
					if(currentType == 0)
					{
						Toast.makeText(baseContext, "上传信息成功", Toast.LENGTH_SHORT)
						.show();
					}
					else
					{
						Toast.makeText(baseContext, "修改信息成功", Toast.LENGTH_SHORT)
						.show();
					}
					back();
				} else {
					result.showToast(baseContext);
				}
			}
		}
	}

	/**
	 * 进行数字验证
	 * @param value
	 * @return
	 */
	public boolean NumberVerify(String value)
	{
		Pattern p = Pattern.compile("([1-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9])");
		Matcher m = p.matcher(value);
		if (m.matches()) {
			return true;
		}
		else
		{
			return false;
		}
	}

}
