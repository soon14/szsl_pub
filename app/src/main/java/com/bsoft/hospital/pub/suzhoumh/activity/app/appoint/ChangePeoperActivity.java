package com.bsoft.hospital.pub.suzhoumh.activity.app.appoint;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tanklib.bitmap.CacheManage;
import com.app.tanklib.bitmap.IndexUrlCache;
import com.app.tanklib.bitmap.view.RoundImageView;
import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.util.AsyncTaskUtil;
import com.app.tanklib.util.StringUtil;
import com.app.tanklib.view.BsoftActionBar.Action;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.my.MyFamilyVo;

/**
 * 切换就诊人
 * 
 * @author Administrator
 * 
 */
public class ChangePeoperActivity extends BaseActivity {

	ProgressBar emptyProgress;
	GridView gridView;
	PeoperAdapter adapter;
	LayoutInflater mLayoutInflater;
	List<MyFamilyVo> dataList;
	MyFamilyVo familyVo;
	GetDataTask task;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pubappoint_changpeoper);
		this.mLayoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.urlMap = new IndexUrlCache(30);
		familyVo = (MyFamilyVo) getIntent().getSerializableExtra("vo");
		dataList = (List<MyFamilyVo>) getIntent().getSerializableExtra(
				"familyList");
		if (null != dataList && dataList.size() > 0) {
			findView();
			// 有缓存数据，则直接显示
			adapter.changeIndex(familyVo);
			adapter.notifyDataSetChanged();
		} else {
			dataList = new ArrayList<MyFamilyVo>();
			findView();
			task = new GetDataTask();
			task.execute();
		}
	}

	@Override
	public void findView() {
		findActionBar();
		actionBar.setTitle("切换就诊人");
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
		gridView = (GridView) findViewById(R.id.gridView);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		emptyProgress = (ProgressBar) findViewById(R.id.emptyProgress);
		adapter = new PeoperAdapter();
		gridView.setAdapter(adapter);
	}

	private class GetDataTask extends
			AsyncTask<Void, Void, ResultModel<ArrayList<MyFamilyVo>>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			emptyProgress.setVisibility(View.VISIBLE);
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
			if (null != result) {
				if (result.statue == Statue.SUCCESS) {
					if (null != result.list && result.list.size() > 0) {
						dataList = result.list;
						adapter.changeIndex(familyVo);
						adapter.notifyDataSetChanged();
					}
				} else {
					result.showToast(baseContext);
				}
			} else {
				Toast.makeText(baseContext, "加载失败", Toast.LENGTH_SHORT).show();
			}
			emptyProgress.setVisibility(View.GONE);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		AsyncTaskUtil.cancelTask(task);
	}

	class PeoperAdapter extends BaseAdapter {
		int currentId = 0;

		public PeoperAdapter() {
		}

		public void changeIndex(MyFamilyVo vo) {
			if (null != vo && null != dataList && dataList.size() > 0) {
				for (int i = 0; i < dataList.size(); i++) {
					if (dataList.get(i).id.equals(vo.id)&&vo.realname.equals(dataList.get(i).realname)) {
						currentId = i;
						break;
					}
				}
			}
		}

		@Override
		public int getCount() {
			return dataList.size();
		}

		@Override
		public MyFamilyVo getItem(int position) {
			return dataList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mLayoutInflater.inflate(
						R.layout.pubappoint_changepeoper_item, null);
				holder.header = (RoundImageView) convertView
						.findViewById(R.id.header);
				holder.headerImage = (ImageView) convertView
						.findViewById(R.id.headerImage);
				holder.name = (TextView) convertView.findViewById(R.id.name);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final MyFamilyVo item = dataList.get(position);
			holder.name.setText(item.realname);
			if (currentId == position) {
				holder.headerImage.setVisibility(View.VISIBLE);
			} else {
				holder.headerImage.setVisibility(View.GONE);
			}
			holder.header.resertStates();
			if (!StringUtil.isEmpty(item.header)) {
				holder.header.setImageUrl(item.header,
						CacheManage.IMAGE_TYPE_HEADER,R.drawable.avatar_none);
				// 加入图片管理
				urlMap.add(position, item.header);

			} else {
				holder.header.setImageResource(R.drawable.avatar_none);
			}
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					if (currentId != position) {
						currentId = position;
					}
					notifyDataSetChanged();
					getIntent().putExtra("data", item);
					setResult(RESULT_OK, getIntent());
					finish();
				}
			});
			return convertView;
		}

		public class ViewHolder {
			public RoundImageView header;
			public ImageView headerImage;
			public TextView name;
		}
	}

}
