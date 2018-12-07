package com.bsoft.hospital.pub.suzhoumh.activity.app.healthtool;


import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.util.AsyncTaskUtil;
import com.app.tanklib.util.StringUtil;
import com.app.tanklib.view.BsoftActionBar.Action;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.view.FooterView;
import com.bsoft.hospital.pub.suzhoumh.view.list.ListViewExt;

public class MedicineSearchActivity  extends BaseActivity {
	
	private EditText et_search;
	private ImageButton ib_search_clear;
	//private ListView lv_medicine;
	private TextView tv_medicine_fuzzy_twomore;
	private ImageView iv_circle;
	
	private MedicineAdapter adapter;
	private List<MedicSimple> list = new ArrayList<MedicSimple>();
	
	private int pageSize = 10;
    private int pageNo = 1;
    private boolean hasFooter;
    
    private String search_content;
	private GetDataTask task;
    private FooterView footerview;
    
    private ListViewExt lv_medicine;
    
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.medicine_search);
		findView();
		initWidget();
		initData();// 加载数据
	}

	private void initWidget() {
		et_search = (EditText) findViewById(R.id.et_search);
		et_search.addTextChangedListener(mTextWatcher);
		et_search.setFocusableInTouchMode(true);
		et_search.setFocusable(true);
		
		ib_search_clear = (ImageButton) findViewById(R.id.ib_search_clear);
		ib_search_clear.setOnClickListener(mOnClickListener);
		ib_search_clear.setVisibility(View.INVISIBLE);
		
		lv_medicine = (ListViewExt) findViewById(R.id.lv_result);
		tv_medicine_fuzzy_twomore = (TextView) findViewById(R.id.tv_medicine_fuzzy_twomore);
		iv_circle = (ImageView) findViewById(R.id.iv_circle);
		
		lv_medicine.setOnTouchListener(mOnTouchListener);
		lv_medicine.removeHeaderView();
	}
	
	private OnTouchListener mOnTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent ev) {
			
			switch (v.getId()) {
			case R.id.et_search: {
				et_search.setFocusableInTouchMode(true);
				et_search.setFocusable(true);
				break;
			}
			case R.id.lv_result : {
				switch(ev.getAction()){
				case MotionEvent.ACTION_DOWN:
					lv_medicine.doActionDown(ev.getY());
					break;
				case MotionEvent.ACTION_MOVE:
					lv_medicine.doActionMove(ev.getY(),hasFooter);
					break;
				case MotionEvent.ACTION_UP:
					switch(lv_medicine.doActionUp(hasFooter)){
					case 1 : 
						break;
					case 2 : 
						pageNo++;
						AsyncTaskUtil.cancelTask(task);
		        		task = new GetDataTask();
		        		task.execute();
						break;
					default : break;
					}
					break;
				
				default:
					break;
				}
			}
			default : {
				//NormalUtil.hideSoftInput(mBaseActivity, et_search);
				return false;
			}
			}
			return false;
		}

	};
	
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
		actionBar.setTitle("药品查询");
		
	}
	
	private void initData() {
		adapter = new MedicineAdapter(MedicineSearchActivity.this, list);
		lv_medicine.setAdapter(adapter);
		lv_medicine.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MedicineSearchActivity.this,MedicineDetailActivity.class);
				intent.putExtra("medic_id",list.get(position).id);
				startActivity(intent);
			}
			
		});
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
	
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//取消任务
		AsyncTaskUtil.cancelTask(task);
	}


	/**
	 * 查询任务
	 * @author Administrator
	 *
	 */
	private class GetDataTask extends
			AsyncTask<Void, Void, ResultModel<ArrayList<MedicSimple>>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			actionBar.startTextRefresh();
		}

		@Override
		protected ResultModel<ArrayList<MedicSimple>> doInBackground(
				Void... params) {
			return HttpApi.getInstance().parserArray(MedicSimple.class,
					"auth/drug/list", 
					new BsoftNameValuePair("pageNo", String.valueOf(pageNo)),
					new BsoftNameValuePair("pageSize",String.valueOf(pageSize)),
					new BsoftNameValuePair("query", search_content),
					new BsoftNameValuePair("sn", loginUser.sn),
					new BsoftNameValuePair("id", loginUser.id)
					);
		}

		@Override
		protected void onPostExecute(ResultModel<ArrayList<MedicSimple>> result) {
			super.onPostExecute(result);
			if (null != result) {
				if (result.statue == Statue.SUCCESS) {
					if (null != result.list && result.list.size() > 0) {
						lv_medicine.addFooter();
						list.addAll(result.list);
						adapter.refresh(list);
						hasFooter = true;
						if(result.list.size()<pageSize){//没有更多数据
							lv_medicine.removeFooter();
							hasFooter = false;
						}
						/*if(pageNo<result.totalPage)
						{
							hasFooter = true;
						}
						else
						{
							hasFooter = false;
						}*/
					} else {
						hasFooter = false;
						list.clear();
						adapter.refresh(list);
						Toast.makeText(baseContext, "没有更多数据", Toast.LENGTH_SHORT)
								.show();
					}
				} else {
					result.showToast(baseContext);
				}
			} else {
				Toast.makeText(baseContext, "加载失败", Toast.LENGTH_SHORT).show();
			}
			actionBar.endTextRefresh();
			lv_medicine.stopRefresh();
			lv_medicine.stopLoad();
		}
	}
	
	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {

			switch (view.getId()) {
			case R.id.ib_search_clear:
				et_search.setText("");
				break;
			default:
				break;
			}
		}
	};
		
	private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int arg1, int arg2,
                int arg3) {
        }
       
        @Override
        public void onTextChanged(CharSequence s, int arg1, int arg2,
                int arg3) {
        }
       
        @Override
        public void afterTextChanged(Editable s) {
        	
        	search_content = s.toString();
        	if(search_content.length()>1){
        		pageNo = 1;
        		//清空原来的数据
        		list.clear();
        		adapter.refresh(list);
        		tv_medicine_fuzzy_twomore.setVisibility(View.GONE);
        		AsyncTaskUtil.cancelTask(task);
        		task = new GetDataTask();
        		task.execute();
        	}else{
        		//清空原来的数据
        		list.clear();
				adapter.refresh(list);
				hasFooter = false;
				lv_medicine.removeFooter();
				tv_medicine_fuzzy_twomore.setVisibility(View.VISIBLE);
				/*if(lv_medicine.getFooterViewsCount()>0)
				{
					lv_medicine.removeFooterView(footerview);
				}*/
        	}
			
			if (StringUtil.isEmpty(s.toString())) {
				ib_search_clear.setVisibility(View.INVISIBLE);
			}
			if (!StringUtil.isEmpty(s.toString())) {
				ib_search_clear.setVisibility(View.VISIBLE);
			}
        }
    };
    
	private void showCircle(){
		/*Animation anim = AnimationUtils.loadAnimation(MedicineSearchActivity.this, R.anim.circle);
		LinearInterpolator lir = new LinearInterpolator();
		anim.setInterpolator(lir);
		iv_circle.startAnimation(anim);
		iv_circle.setVisibility(View.VISIBLE);*/
	}
	
	private void hideCircle(){
		iv_circle.clearAnimation();
		iv_circle.setVisibility(View.GONE);
	}

}
