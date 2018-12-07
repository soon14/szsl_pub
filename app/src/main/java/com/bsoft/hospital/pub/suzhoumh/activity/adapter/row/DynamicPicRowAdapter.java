package com.bsoft.hospital.pub.suzhoumh.activity.adapter.row;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.tanklib.bitmap.CacheManage;
import com.app.tanklib.bitmap.IndexUrlCache;
import com.app.tanklib.bitmap.view.ProgressImageView;
import com.app.tanklib.bitmap.view.RoundImageView;
import com.app.tanklib.util.StringUtil;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.AppApplication;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.DynamicShow;
import com.bsoft.hospital.pub.suzhoumh.util.DateUtil;

/**
 * 类说明
 * 
 * @author zkl
 * @version 2012-7-14 下午11:45:00
 * 
 */

public class DynamicPicRowAdapter {

	public static void bindView(View view, final DynamicShow vo,
			final Context paramActivity, IndexUrlCache urlMap,
			final int position) {
		final Holder paramHolder = (Holder) view.getTag();
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		params.width = AppApplication.getWidthPixels();
		params.height = params.width * 2 / 3;
		if (null != paramHolder.progressImageView.getImageView()) {
			paramHolder.progressImageView.getImageView().getDrawable()
					.setCallback(null);
		}
		paramHolder.progressImageView.setLatout(params);
		paramHolder.progressImageView.mIgImageView.resertStates();
		if (!StringUtil.isEmpty(vo.imgurl)) {
			paramHolder.progressImageView.setImageUrl(HttpApi.getImageUrl(
					vo.imgurl, CacheManage.IMAGE_TYPE_PROGRESS));

			// 加入图片管理
			urlMap.add(position, HttpApi.getImageUrl(vo.imgurl,
					CacheManage.IMAGE_TYPE_PROGRESS));
		}

		// // 增加双击事件
		// paramHolder.progressImageView.setOnTouchListener(new
		// OnDoubleClick(vo,
		// paramActivity, paramHolder));

		paramHolder.header.resertStates();
		if (!StringUtil.isEmpty(vo.header)) {
			if (vo.uid == 1) {
				paramHolder.header.setImageUrl(HttpApi.getImageUrl(vo.header,
						CacheManage.IMAGE_TYPE_HEADER),
						CacheManage.IMAGE_TYPE_HEADER, R.drawable.admin_header);
			} else {
				paramHolder.header.setImageUrl(HttpApi.getImageUrl(vo.header,
						CacheManage.IMAGE_TYPE_HEADER),
						CacheManage.IMAGE_TYPE_HEADER, R.drawable.doc_header);
			}

			// 加入图片管理
			urlMap.add(position, HttpApi.getImageUrl(vo.header,
					CacheManage.IMAGE_TYPE_HEADER));
		} else {
			if (vo.uid == 1) {
				paramHolder.header.setImageResource(R.drawable.admin_header);
			} else {
				paramHolder.header.setImageResource(R.drawable.doc_header);
			}
		}

		paramHolder.header.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (vo.uid != 1) {
					/*Intent intent = new Intent(paramActivity,
							DoctorHomeActivity.class);
					intent.putExtra("id", vo.uid);
					paramActivity.startActivity(intent);*/
				}
			}
		});
		paramHolder.name.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (vo.uid != 1) {
					/*Intent intent = new Intent(paramActivity,
							DoctorHomeActivity.class);
					intent.putExtra("id", vo.uid);
					paramActivity.startActivity(intent);*/
				}
			}
		});
		if (vo.uid == 1) {
			paramHolder.name.setText("健康助手");
		} else {
			paramHolder.name.setText(vo.realname);
		}
		//paramHolder.time.setText(DateUtil.getDateTime(vo.createdate));
		paramHolder.time.setText(DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss",vo.createdate));
		paramHolder.content.setText(vo.content);
		paramHolder.commentCount.setText("评论  " + vo.replycount);
		paramHolder.title.setText(vo.title);

	}

	public static View newView(Context paramContext, ViewGroup paramViewGroup) {
		LayoutInflater mInflater = LayoutInflater.from(paramContext);
		View localView = mInflater.inflate(R.layout.dynamic_pic,
				paramViewGroup, false);
		Holder localHolder = new Holder();
		localHolder.progressImageView = (ProgressImageView) localView
				.findViewById(R.id.progressImageView);
		localHolder.header = (RoundImageView) localView
				.findViewById(R.id.header);

		localHolder.name = (TextView) localView.findViewById(R.id.name);
		localHolder.time = (TextView) localView.findViewById(R.id.time);
		localHolder.content = (TextView) localView.findViewById(R.id.content);
		localHolder.commentCount = (TextView) localView
				.findViewById(R.id.commentCount);
		localHolder.title = (TextView) localView.findViewById(R.id.title);
		localView.setTag(localHolder);
		return localView;
	}

	public static class Holder {
		public ProgressImageView progressImageView;
		public RoundImageView header;
		public TextView name, time, content, commentCount,title;
	}

}
