package com.bsoft.hospital.pub.suzhoumh.activity.app.map;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.adapter.RouteDetailAdapter;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.model.RouteStepVo;
import com.app.tanklib.view.BsoftActionBar.Action;

/**
 * @author Tank E-mail:zkljxq@126.com
 * @类说明
 */
public class RouteDetailActivity extends BaseActivity {
	RouteDetailAdapter adapter;
	ListView listView;
	ProgressBar emptyProgress;
	LayoutInflater mLayoutInflater;

	View headerView, bottomView;

	RouteStepVo vo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.base_list);
		this.mLayoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		vo = (RouteStepVo) getIntent().getSerializableExtra("vo");
		findView();
		setClick();
	}

	void setClick() {
		adapter = new RouteDetailAdapter(this);
		listView.addHeaderView(headerView);
		listView.addFooterView(bottomView);
		adapter.addData(vo.steps);
		listView.setAdapter(adapter);

	}

	public void findView() {
		findActionBar();
		actionBar.setTitle("详情");
		actionBar.setBackAction(new Action() {
			@Override
			public void performAction(View view) {
				finish();
			}

			@Override
			public int getDrawable() {
				return R.drawable.btn_back;
			}
		});

		listView = (ListView) findViewById(R.id.listView);
		emptyProgress = (ProgressBar) findViewById(R.id.emptyProgress);
		headerView = mLayoutInflater.inflate(R.layout.seek_routeitem_header,
				null);
		((TextView) headerView.findViewById(R.id.title)).setText(vo.time
				+ "    " + vo.distance);
		((TextView) headerView.findViewById(R.id.starttext)).setText("起点");
		bottomView = mLayoutInflater.inflate(R.layout.seek_routeitem_bottom,
				null);
		((TextView) bottomView.findViewById(R.id.endtext)).setText("终点("
				+ vo.endtext + ")");
	}

	// private class GetDataTask extends AsyncTask<Void, Void,
	// PageList<StreetVo>> {
	// @Override
	// protected void onPreExecute() {
	// super.onPreExecute();
	// emptyProgress.setVisibility(View.VISIBLE);
	// }
	//
	// @Override
	// protected PageList<StreetVo> doInBackground(Void... params) {
	// PageList<StreetVo> mList = new PageList<StreetVo>();
	// return (PageList<StreetVo>) HttpApi.getInstance().list(mList,
	// StreetVo.class, "ecard.ifc.region.streets",
	// new BsoftNameValuePair("districtId", codeId));
	// }
	//
	// @Override
	// protected void onPostExecute(PageList<StreetVo> result) {
	// super.onPostExecute(result);
	// if (null != result) {
	// if (result.statue == Statue.SUCCESS) {
	// if (null != result.list && result.list.size() > 0) {
	// adapter.addData(result.list);
	// } else {
	// Toast.makeText(baseContext, "数据为空", Toast.LENGTH_SHORT)
	// .show();
	// }
	// } else {
	// result.showToast(baseContext);
	// }
	// } else {
	// Toast.makeText(baseContext, "加载失败", Toast.LENGTH_SHORT).show();
	// }
	// emptyProgress.setVisibility(View.GONE);
	// }
	// }

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
