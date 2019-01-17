package com.bsoft.hospital.pub.suzhoumh.activity.app.healthtool;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tanklib.Preferences;
import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.util.AsyncTaskUtil;
import com.app.tanklib.view.BsoftActionBar.Action;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.my.family.DialogListAdapter;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.my.MyFamilyVo;
import com.bsoft.hospital.pub.suzhoumh.util.DateUtil;
import com.bsoft.hospital.pub.suzhoumh.util.DialogUtil;

public class MedicineRemindAddActivity extends BaseActivity implements OnClickListener{

	private TextView tv_rate;//频次
	private TextView tv_start_time;//提醒时间
	private TextView tv_turn;
	private EditText et_remind_day;//提醒天数
	private EditText et_name;
	private EditText et_medicine;
	private LinearLayout ll_arrow;
	private Button btn_submit;
	
	private String days;//天数
	private int times = 0;//次数
	private MedicineRemindModel model;
	public Calendar cd;
	private int isremind=1;//1,有提醒，0没提醒
	private boolean isTurnOn=true;
	
	private int type = 0;//0是添加，1是修改
	
	private List<String> args = new ArrayList<String>();//频次
	
	private List<MyFamilyVo> familylist = new ArrayList<MyFamilyVo>();
	private List<String> familys = new ArrayList<String>();
	
	private GetFamilyTask familyTask;
	private SaveDataTask saveTask;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.medicine_add);
		
		type = getIntent().getIntExtra("type", 0);
		findView();
		initData();
	}

	@Override
	public void findView() {
		// TODO Auto-generated method stub
		findActionBar();
		actionBar.setBackAction(new Action(){

			@Override
			public int getDrawable() {
				// TODO Auto-generated method stub
				return R.drawable.btn_back;
			}

			@Override
			public void performAction(View view) {
				// TODO Auto-generated method stub
				finish();
			}
			
		});
		actionBar.setTitle("添加提醒");
		tv_rate = (TextView) findViewById(R.id.tv_rate);
		tv_start_time = (TextView) findViewById(R.id.tv_start_time);
		tv_turn = (TextView) findViewById(R.id.tv_turn);
		et_remind_day = (EditText) findViewById(R.id.et_remind_day);
		btn_submit= (Button) findViewById(R.id.btn_submit);
		et_name = (EditText) findViewById(R.id.et_name);
		et_medicine = (EditText) findViewById(R.id.et_medicine);
		ll_arrow = (LinearLayout) findViewById(R.id.ll_arrow);
	}

	private void initData()
	{
		tv_rate.setOnClickListener(this);
		tv_start_time.setOnClickListener(this);
		tv_turn.setOnClickListener(this);
		btn_submit.setOnClickListener(this);
		ll_arrow.setOnClickListener(this);
		saveTask = new SaveDataTask();
		
		Date date = new Date();    
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String date1 = format.format(date);
        tv_start_time.setText(date1);
		cd = Calendar.getInstance();
		cd.setTime(date);
		
		if(type==0)
		{
			model = new MedicineRemindModel();
			et_name.setText(loginUser.realname);
			actionBar.setTitle("添加提醒");
		}
		else
		{
			actionBar.setTitle("修改提醒");
			model = (MedicineRemindModel) getIntent().getSerializableExtra("model");
			tv_rate.setText(model.drugrepeat+"天"+model.times+"次");
			et_name.setText(model.username);
			et_medicine.setText(model.medname);
			et_remind_day.setText(model.days);
			tv_start_time.setText(DateUtil.getDateTime("yyyy-MM-dd",Long.parseLong(model.begindate)));
			if(model.isremind.equals("1"))
			{
				isTurnOn = false;
				turnOnOff();
			}
			else
			{
				isTurnOn = true;
				turnOnOff();
			}
		}
		et_name.setSelection(et_name.getText().toString().length());
		
		for(int i=0;i<4;i++)
		{
			int j = i+1;
			args.add("1天"+String.valueOf(j)+"次");
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.tv_rate:
			DialogListAdapter adapter =new DialogListAdapter(baseContext,args,getPosition());
			DialogUtil.showSelectDialog(baseContext, "请选择频次", adapter, new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					int times = position+1;
					model.drugrepeat = "1";//1天
					model.times = String.valueOf(times);//几次
					DialogUtil.close();
					setTimes();
				}
				
			});
			break;
		case R.id.tv_start_time:
			showStartTime();
			break;
		case R.id.tv_turn:
			turnOnOff();
			break;
		case R.id.btn_submit:
			submit();
			break;
		case R.id.ll_arrow:
			if(familys!=null&&familys.size()>0)
			{
				showFaimlySelectDailog();
			}
			else
			{
				familyTask = new GetFamilyTask();
				familyTask.execute();
			}
			break;
		}
	}
	
	private void turnOnOff()
	{
		 if(isTurnOn){
             isTurnOn=false;
             tv_turn.setBackgroundDrawable(MedicineRemindAddActivity.this.getResources().getDrawable(R.drawable.checkbox_n));
             isremind=0;
         }else{
             isTurnOn=true;
             tv_turn.setBackgroundDrawable(MedicineRemindAddActivity.this.getResources().getDrawable(R.drawable.checkbox_p));
             isremind=1;
         }
	}
	
	/**
	 * 保存
	 */
	private void submit()
	{
		if(et_name.getText().toString().equals(""))
		{
			Toast.makeText(baseContext, "请输入服药者", Toast.LENGTH_SHORT).show();
			return;
		}
		if(et_medicine.getText().toString().equals(""))
		{
			Toast.makeText(baseContext, "请输入药品", Toast.LENGTH_SHORT).show();
			return;
		}
		if(et_remind_day.getText().toString().equals(""))
		{
			Toast.makeText(baseContext, "请输入提醒天数", Toast.LENGTH_SHORT).show();
			return;
		}
		
		model.username = et_name.getText().toString();
		model.medname = et_medicine.getText().toString();
		model.days = et_remind_day.getText().toString();
		model.isremind = String.valueOf(isremind);
		model.begindate = tv_start_time.getText().toString();
		model.drugrepeat = "1";
		//设置默认的次数和时间
		if(model.times==null)
		{
			model.times = "1";
		}
		if(model.timesde == null)
		{
			model.timesde = "08:00";
		}
		saveTask.execute();
		
	}
	
	/**
	 * 保存和修改用药提醒
	 * @author Administrator
	 *
	 */
	class SaveDataTask extends
			AsyncTask<Void, Object, ResultModel<MedicineRemindModel>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			actionBar.startTextRefresh();;
		}

		protected ResultModel<MedicineRemindModel> doInBackground(Void... params) {
			if(type == 0)
			{
				return HttpApi.getInstance().parserData(MedicineRemindModel.class,
						"auth/drugremind/add",
						new BsoftNameValuePair("id", loginUser.id),
						new BsoftNameValuePair("sn", loginUser.sn),
						new BsoftNameValuePair("username", model.username),
						new BsoftNameValuePair("medname", model.medname),
						new BsoftNameValuePair("drugrepeat", model.drugrepeat),
						new BsoftNameValuePair("times", model.times),
						new BsoftNameValuePair("timesde", model.timesde),
						new BsoftNameValuePair("begindate", model.begindate),
						new BsoftNameValuePair("days", model.days),
						new BsoftNameValuePair("isremind", model.isremind));
			}
			else
			{
				return HttpApi.getInstance().parserData(MedicineRemindModel.class,
						"auth/drugremind/update",
						new BsoftNameValuePair("id", loginUser.id),
						new BsoftNameValuePair("sn", loginUser.sn),
						new BsoftNameValuePair("username", model.username),
						new BsoftNameValuePair("medname", model.medname),
						new BsoftNameValuePair("drugrepeat", model.drugrepeat),
						new BsoftNameValuePair("times", model.times),
						new BsoftNameValuePair("timesde", model.timesde),
						new BsoftNameValuePair("begindate", model.begindate),
						new BsoftNameValuePair("days", model.days),
						new BsoftNameValuePair("isremind", model.isremind),
						new BsoftNameValuePair("rid",model.id)
						);
			}
		}

		@Override
		protected void onPostExecute(ResultModel<MedicineRemindModel> result) {
			actionBar.endTextRefresh();;
			if (null != result) {
				if (result.statue == Statue.SUCCESS) {
					Toast.makeText(baseContext, "保存成功", Toast.LENGTH_SHORT)
							.show();
					if(model.isremind.equals("1"))
					{
						deleteAlarm();
						setAlarm();
					}
					setResult(RESULT_OK);
					finish();

				} else {
					result.showToast(baseContext);
				}
			}
		}

	}

	/**
	 * 删除闹钟
	 */
	private void deleteAlarm()
	{
		// 删除闹钟
		Intent deleteclock = new Intent(MedicineRemindAddActivity.this, CallAlarmReceiver.class);
		deleteclock.setAction("com.bsoft.hospital.pub.medicineremind");
		if (null != model.id && !("").equals(model.id)) {
			String str=Preferences.getInstance().getStringData("clock_"+model.id);
			if(str!=null&&!str.equals(""))
			{
				String arr[]=str.split("_");
				for(int i=0;i<arr.length;i++)
				{
					if(arr[i]!=null&&!arr[i].equals(""))
					{
						int as = Integer.parseInt(arr[i]);
						PendingIntent sender = PendingIntent.getBroadcast(
								MedicineRemindAddActivity.this, as, deleteclock, 0);
						/* 由AlarmManager中删除 */
						AlarmManager am;
						am = (AlarmManager) getSystemService(ALARM_SERVICE);
						am.cancel(sender);
					}
				}
			}
			
		}
	}

	/**
	 * 设置闹钟
	 */
	private void setAlarm()
	{
		try
		{
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
			String[] s = model.timesde.toString().split(",");
			if (null == s || s.length <= 0) {
				return;
			}
			StringBuffer clock_id = new StringBuffer();//闹钟记录的id
			for (int i = 0; i < s.length; i++) {
				Calendar dayc1 = Calendar.getInstance();
				Date daystart = df.parse(tv_start_time.getText() + " "
						+ s[i]);
				dayc1.setTime(daystart);
				// dayc1.setTime(new Date());
				// dayc1.add(Calendar.SECOND, 15);
				int clock_child_id = (int)(Math.random()*10000);//每次闹钟的id
				Intent intent = new Intent(MedicineRemindAddActivity.this, CallAlarmReceiver.class);
				intent.setAction("com.bsoft.hospital.pub.medicineremind");
				//intent.putExtra("clock_parent_id",model.id);
				intent.putExtra("clock_child_id", String.valueOf(clock_child_id));
				Preferences.getInstance().setStringData("clock_"+clock_child_id+"_days", model.days);//设置提醒的天数
				clock_id.append(clock_child_id).append("_");
				PendingIntent sender = PendingIntent.getBroadcast(
						baseContext, clock_child_id, intent, 0);
				/*PendingIntent sender = PendingIntent.getBroadcast(
						baseContext, Clock_ids, intent, 0);*/
				/* setRepeating()可让闹钟重复运行 */
				int a = 1;
				if (null != model.drugrepeat && !("").equals(model.drugrepeat)) {
					a = Integer.parseInt(model.drugrepeat);
				}
				 am.setRepeating(AlarmManager.RTC_WAKEUP,
				 dayc1.getTimeInMillis(), 1000 * 60 * 60 * 24
				 * a, sender);
				 
				 
				/*am.set(AlarmManager.RTC_WAKEUP, dayc1.getTimeInMillis(),
						sender);*/
			}
			Preferences.getInstance().setStringData("clock_"+model.id, clock_id.toString());
			/*Preference
			SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(baseContext);;
			Editor et = sp.edit();
			Set set = new HashSet();
			et.putStringSet("clock_alarm", set);*/
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 选择开始时间
	 */
	private void showStartTime()
	{
		 new DatePickerDialog(MedicineRemindAddActivity.this, new OnDateSetListener(){
		      public void onDateSet(DatePicker view, int year,
		        int monthOfYear, int dayOfMonth) {
                   String mm;  
                   String dd;  
                   if(monthOfYear<=9)  
                   {  
                   	monthOfYear = monthOfYear+1;  
                       mm="0"+monthOfYear;  
                   }  
                   else{  
                   	monthOfYear = monthOfYear+1;  
                       mm=String.valueOf(monthOfYear);  
                       }  
                   if(dayOfMonth<=9)  
                   {  
                   	dayOfMonth = dayOfMonth;  
                       dd="0"+dayOfMonth;  
                   }  
                   else{  
                   	dayOfMonth = dayOfMonth;  
                       dd=String.valueOf(dayOfMonth);  
                       }  
                   dayOfMonth = dayOfMonth;  
                   tv_start_time.setText(year+"-"+monthOfYear+"-"+dayOfMonth);
		    	  String mon=monthOfYear+"";
		    	  if(mon.length()==1){
		    		  mon="0"+mon;
		    	  }
		    	  String day=dayOfMonth+"";
		    	  if(day.length()==1){
		    		  day = "0"+day;
		    	  }
		      }
		      }, 
		     cd.get(Calendar.YEAR), 
		     cd.get(Calendar.MONTH),
		     cd.get(Calendar.DAY_OF_MONTH)).show();
	}

	/**
	 * 得到当前已选择的频次
	 * @return
	 */
	private int getPosition()
	{
		int position = 0;
		if(model.times!=null&&!model.times.equals(""))
		{
			position = Integer.parseInt(model.drugrepeat)-1;
		}
		return position;
	}
	
	/**
	 * 设置次数
	 */
	private void setTimes()
	{
		CustomDialog.Builder builder1 = new CustomDialog.Builder(
				MedicineRemindAddActivity.this);
		builder1.setMedicinieWarnModel(model);
		builder1.setTitle("请设置时间");
		builder1.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int which) {
						/*tv_times.setText(MedicineWarnModel.getTimes()
								+ "次");
						take_medicine_cs.setText(MedicineWarnModel
								.getTimes() + "次");*/
						tv_rate.setText(model.drugrepeat+"天"+model.times+"次");
						Log.i("times2",model.timesde);
						dialog.dismiss();

					}
				});
		builder1.setNegativeButton("取消",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int which) {
						dialog.dismiss();
					}
				});

		builder1.create().show();
	}
	
	/**
	 * 获取家庭成员列表
	 * @author Administrator
	 *
	 */
	private class GetFamilyTask extends
			AsyncTask<Void, Void, ResultModel<ArrayList<MyFamilyVo>>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			actionBar.startTextRefresh();
		}

		@Override
		protected ResultModel<ArrayList<MyFamilyVo>> doInBackground(
				Void... params) {
			return HttpApi.getInstance().parserArray(MyFamilyVo.class,
					"auth/family/list",
					new BsoftNameValuePair("id", loginUser.id),
					new BsoftNameValuePair("sn", loginUser.sn));
		}

		@Override
		protected void onPostExecute(ResultModel<ArrayList<MyFamilyVo>> result) {
			super.onPostExecute(result);
			actionBar.endTextRefresh();
			if (null != result) {
				if (result.statue == Statue.SUCCESS) {
					if (null != result.list && result.list.size() > 0) {
						familylist = result.list;
						for(int i=0;i<familylist.size();i++)
						{
							familys.add(familylist.get(i).realname);
						}
						showFaimlySelectDailog();
					} 
					else
					{
						Toast.makeText(baseContext, "当前没有家庭成员", Toast.LENGTH_SHORT).show();
					}
				}
				else
				{
					Toast.makeText(baseContext, "家庭成员获取失败", Toast.LENGTH_SHORT).show();
				}
			} 
		}
	}

	/**
	 * 显示家庭成员选择对话框
	 */
	private void showFaimlySelectDailog()
	{
		DialogListAdapter adapter =new DialogListAdapter(baseContext,familys,0);
		DialogUtil.showSelectDialog(baseContext, "请选择服药者", adapter, new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent,
					View view, int position, long id) {
				// TODO Auto-generated method stub
				et_name.setText(familys.get(position));
				DialogUtil.close();
			}
		});
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		AsyncTaskUtil.cancelTask(saveTask);
		AsyncTaskUtil.cancelTask(familyTask);
	}

}
