package com.bsoft.hospital.pub.suzhoumh.activity.app.healthtool;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.util.AsyncTaskUtil;
import com.app.tanklib.view.BsoftActionBar.Action;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.view.FooterView;

/**
 * 用药提醒主界面
 * @author Administrator
 *
 */
public class MedicineRemindActivity extends BaseActivity implements OnClickListener{

	private ListView lv_remind;
	private Button btn_add;
	private List<MedicineRemindModel> list;
	private int pageNo = 1;
	private int pageSize = 10;
	private FooterView footerview;
	private GetDataTask task;
	private DelDataTask deltask;
	private MedicineRemindAdapter adapter;
	private int deletePosition = 0;//要删除的用药提醒位置
	
	public Builder builder;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.medicine_remind);
		findView();
		initView();
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
		actionBar.setTitle("用药提醒");
		actionBar.setRefreshTextView("添加", new Action(){

			@Override
			public int getDrawable() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public void performAction(View view) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MedicineRemindActivity.this,MedicineRemindAddActivity.class);
				intent.putExtra("type", 0);//添加
				startActivityForResult(intent, 0);
			}
			
		});
	}

	private void initView()
	{
		lv_remind = (ListView)findViewById(R.id.lv_remind);
		btn_add = (Button)findViewById(R.id.btn_add);
		footerview = new FooterView(this);
		footerview.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				pageNo++;
				AsyncTaskUtil.cancelTask(task);
        		task = new GetDataTask();
        		task.execute();
			}
		});
	}

	private void initData()
	{
		list = new ArrayList<MedicineRemindModel>();
		adapter = new MedicineRemindAdapter(baseContext, list);
		lv_remind.setAdapter(adapter);
		lv_remind.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				/*Intent intent = new Intent(MedicineRemindActivity.this,MedicineRemindAddActivity.class);
				intent.putExtra("type", 1);//修改
				intent.putExtra("model", list.get(position));
				startActivityForResult(intent, 1);
				showSelectDialog(position);*/
				Toast.makeText(baseContext, "测试", Toast.LENGTH_SHORT).show();
			}
			
		});
		btn_add.setOnClickListener(this);
		task = new GetDataTask();
		task.execute();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
			case R.id.btn_add:
				Intent intent = new Intent(MedicineRemindActivity.this,MedicineRemindAddActivity.class);
				intent.putExtra("type", 0);//添加
				startActivityForResult(intent, 0);
				break;
		}
	}
	
	/**
	 * 查询任务
	 * @author Administrator
	 *
	 */
	private class GetDataTask extends
			AsyncTask<Void, Void, ResultModel<ArrayList<MedicineRemindModel>>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			actionBar.startTextRefresh();;
		}

		@Override
		protected ResultModel<ArrayList<MedicineRemindModel>> doInBackground(
				Void... params) {
			return HttpApi.getInstance().parserArray(MedicineRemindModel.class,
					"auth/drugremind/list", 
					new BsoftNameValuePair("pageNo", String.valueOf(pageNo)),
					new BsoftNameValuePair("pageSize",String.valueOf(pageSize)),
					new BsoftNameValuePair("sn", loginUser.sn),
					new BsoftNameValuePair("id", loginUser.id)
					);
		}

		@Override
		protected void onPostExecute(ResultModel<ArrayList<MedicineRemindModel>> result) {
			super.onPostExecute(result);
			if (null != result) {
				if (result.statue == Statue.SUCCESS) {
					if (null != result.list && result.list.size() > 0) {
						list.addAll( result.list);
						if(pageNo<result.totalPage)
						{
							if(lv_remind.getFooterViewsCount()==0)
							{
								lv_remind.addFooterView(footerview);
							}
							/*(!hasMoreData) {
							    listview.removeFooterView(footview);
							} (listview.getFooterViewsCount() == 0) {
							    listview.addFooterView(footview);
							}*/
						}
						else
						{
							lv_remind.removeFooterView(footerview);
						}
						adapter.refresh(list);
					} else {
						list.clear();
						adapter.refresh(list);
						lv_remind.removeFooterView(footerview);
					}
				} else {
					result.showToast(baseContext);
				}
			} else {
				Toast.makeText(baseContext, "加载失败", Toast.LENGTH_SHORT).show();
			}
			actionBar.endTextRefresh();;
		}
	}
	
	/**
	 * 删除任务
	 * @author Administrator
	 *
	 */
	private class DelDataTask extends
			AsyncTask<Void, Void, ResultModel<ArrayList<MedicineRemindModel>>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			actionBar.startTextRefresh();;
		}

		@Override
		protected ResultModel<ArrayList<MedicineRemindModel>> doInBackground(
				Void... params) {
			return HttpApi.getInstance().parserArray(MedicineRemindModel.class,
					"auth/drugremind/delete", 
					new BsoftNameValuePair("sn", loginUser.sn),
					new BsoftNameValuePair("id", loginUser.id),
					new BsoftNameValuePair("rid",list.get(deletePosition).id)
					);
		}

		@Override
		protected void onPostExecute(ResultModel<ArrayList<MedicineRemindModel>> result) {
			super.onPostExecute(result);
			if (null != result) {
				if (result.statue == Statue.SUCCESS) {
					if (null != result) {
						if (result.statue == Statue.SUCCESS) {
							Toast.makeText(baseContext, "删除成功", Toast.LENGTH_SHORT)
									.show();
							list.remove(deletePosition);
							adapter.refresh(list);
							
						} else {
							result.showToast(baseContext);
						}
					}
				} else {
					result.showToast(baseContext);
				}
			} else {
				Toast.makeText(baseContext, "删除失败", Toast.LENGTH_SHORT).show();
			}
			actionBar.endTextRefresh();;
		}
	}
	
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		if(arg1==RESULT_OK)
		{
			list.clear();
			task = new GetDataTask();
			task.execute();
		}
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		AsyncTaskUtil.cancelTask(task);
		AsyncTaskUtil.cancelTask(deltask);
	}

	class MedicineRemindAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private Context context;
		private List<MedicineRemindModel> list;
		private float DownX;
		private float UpX;
		public MedicineRemindAdapter(Context context, List<MedicineRemindModel> list) {
			super();
			this.context = context;
			this.list = list;
			inflater = LayoutInflater.from(context);

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			//return list.get(position);
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.activity_medicinewarn_main_list, null);
				viewHolder = new ViewHolder();

				viewHolder.tv_reminder_personname = (TextView)convertView.findViewById(
						R.id.tv_reminder_personname);
				viewHolder.tv_reminder_medicine = (TextView)convertView.findViewById(
						R.id.tv_reminder_medicine);
				viewHolder.tv_reminder_frequency = (TextView)convertView.findViewById(
						R.id.tv_reminder_frequency);
				viewHolder.btn_del = (Button)convertView.findViewById(R.id.btn_del);
				viewHolder.rl_content = (RelativeLayout)convertView.findViewById(R.id.rl_content);
				/*convertView.setOnTouchListener(new OnTouchListener(){  
					  
	                @Override  
	                public boolean onTouch(View v, MotionEvent event) {  
	                    switch(event.getAction())//根据动作来执行代码     
	                    {    
	                    case MotionEvent.ACTION_MOVE://滑动     
	                        //Toast.makeText(context, "move...", Toast.LENGTH_SHORT).show();  
	                        break;    
	                    case MotionEvent.ACTION_DOWN://按下     
	                        //Toast.makeText(context, "down...", Toast.LENGTH_SHORT).show();  
	                        DownX = event.getX();  
	                        break;    
	                    case MotionEvent.ACTION_UP://松开     
	                        UpX = event.getX();  
	                        //Toast.makeText(context, "up..." + Math.abs(UpX-DownX), Toast.LENGTH_SHORT).show();  
	                        if(Math.abs(UpX-DownX) > 20){  
	                            ViewHolder holder = (ViewHolder) v.getTag();  
	                            holder.btn_del.setVisibility(View.VISIBLE);  
	                        }  
	                        break;    
	                    default:    
	                    }    
	                    return true;   
	                }  
	            });  */
				convertView.setTag(viewHolder);
				
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			
			viewHolder.tv_reminder_personname.setText(list.get(position).username);
			viewHolder.tv_reminder_medicine.setText(list.get(position).medname);
			
			viewHolder.tv_reminder_frequency.setText(list.get(position).drugrepeat+"天"+list.get(position).times+"次");
			
			viewHolder.rl_content.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(MedicineRemindActivity.this,MedicineRemindAddActivity.class);
					intent.putExtra("type", 1);//修改
					intent.putExtra("model", list.get(position));
					startActivityForResult(intent, 1);
				}
				
			});
			viewHolder.btn_del.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					new AlertDialog.Builder(MedicineRemindActivity.this)
    				.setMessage("确定删除该用药提醒？")
					.setTitle("提示")
					.setPositiveButton("确定",new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							showLoadingView();
							deletePosition = position;
							deltask = new DelDataTask();
							deltask.execute();
						}
					})
					.setNegativeButton("取消", null)
					.create().show();
				}
				
			});
			return convertView;
		}

		public class ViewHolder {
			TextView tv_reminder_personname;
			TextView tv_reminder_medicine;
			TextView tv_reminder_frequency;
			TextView btn_reminder_delete;
			RelativeLayout rl_content;
			Button btn_del;

		}

		public void refresh(List<MedicineRemindModel> l) {
			if(l!=null && l.size()>0){
				this.list = l;
			}else{
				this.list.clear();
			}
			notifyDataSetChanged();
		}

		public void add(MedicineRemindModel doc) {
			list.add(doc);
			notifyDataSetChanged();
		}

		public void add(List<MedicineRemindModel> docs) {
			list.addAll(docs);
			notifyDataSetChanged();

		}
	}
	
	// item项弹出的dialog
	private void showSelectDialog(final int position) {
		builder = new Builder(MedicineRemindActivity.this);
		builder.setItems(new String[] { "修改", "删除" },
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						if (which == 0)// 修改
						{
							Intent intent = new Intent(MedicineRemindActivity.this,MedicineRemindAddActivity.class);
							intent.putExtra("type", 1);//修改
							intent.putExtra("model", list.get(position));
							startActivityForResult(intent, 1);
						} else if (which == 1)// 删除
						{
							new AlertDialog.Builder(MedicineRemindActivity.this)
		    				.setMessage("确定删除该用药提醒？")
							.setTitle("提示")
							.setPositiveButton("确定",new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									showLoadingView();
									deletePosition = position;
									deltask = new DelDataTask();
									deltask.execute();
								}
							})
							.setNegativeButton("取消", null)
							.create().show();
						}
					}
				});
		builder.create().show();
	}
}
