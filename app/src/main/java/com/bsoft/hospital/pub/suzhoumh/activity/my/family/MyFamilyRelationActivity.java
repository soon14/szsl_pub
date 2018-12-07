package com.bsoft.hospital.pub.suzhoumh.activity.my.family;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.app.tanklib.view.BsoftActionBar.Action;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.AppApplication;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.model.my.RelationVo;

public class MyFamilyRelationActivity extends BaseActivity{

	
	private int selectRelationPosition = 0;
	private ListView lv_content;
	private List<RelationVo> relationlist;
	private List<String> relationStringlist = new ArrayList<String>();
	private DialogListAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myfamily_relation);
		findView();
		initData();
	}

	@Override
	public void findView() {
		// TODO Auto-generated method stub
		findActionBar();
		actionBar.setTitle("关系");
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
		
		lv_content = (ListView) findViewById(R.id.lv_content);
	}
	
	private void initData()
	{
		selectRelationPosition = getIntent().getIntExtra("selectRelationPosition", 0);
		relationlist = AppApplication.relationlist;
		for(int i=0;i<relationlist.size();i++)
		{
			relationStringlist.add(relationlist.get(i).title);
		}
		adapter = new DialogListAdapter(baseContext,relationStringlist,selectRelationPosition);
		lv_content.setAdapter(adapter);
		
		lv_content.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("selectRelationPosition", position);
				setResult(RESULT_OK, intent);
				finish();
			}
			
		});
	}
}
