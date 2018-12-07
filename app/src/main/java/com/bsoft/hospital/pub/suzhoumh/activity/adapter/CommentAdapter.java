package com.bsoft.hospital.pub.suzhoumh.activity.adapter;
import java.util.ArrayList;
import java.util.List;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.Comment;
import com.bsoft.hospital.pub.suzhoumh.util.DateUtil;
import com.app.tanklib.bitmap.CacheManage;
import com.app.tanklib.bitmap.view.RoundImageView;
import com.app.tanklib.util.StringUtil;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
public class CommentAdapter extends BaseAdapter {
	private LayoutInflater mLayoutInflater;
	private List<Comment> dataList = new ArrayList<Comment>();
	BaseActivity baseActivity;

	public CommentAdapter(BaseActivity baseActivity) {
		this.baseActivity = baseActivity;
		this.mLayoutInflater = (LayoutInflater) baseActivity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void addData(ArrayList<Comment> msgs) {
		if (null != msgs) {
			dataList = msgs;
		} else {
			dataList = new ArrayList<Comment>();
		}
		notifyDataSetChanged();
	}

	public void addMoreData(ArrayList<Comment> msgs) {
		if (null != msgs && msgs.size() > 0) {
			dataList.addAll(msgs);
			notifyDataSetChanged();
		}
	}

	public void addData(Comment vo) {
		dataList.add(0, vo);
		notifyDataSetChanged();
	}

	public void remove(int index) {
		dataList.remove(index);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public Comment getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.comment_item, null);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.content = (TextView) convertView.findViewById(R.id.content);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.header = (RoundImageView) convertView
					.findViewById(R.id.header);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Comment item = dataList.get(position);
		holder.name.setText(item.realname);
		holder.content.setText(item.content);
		holder.time.setText(DateUtil.getDateTime(item.createdate));

		holder.header.resertStates();
		if (!StringUtil.isEmpty(item.header)) {
			holder.header.setImageUrl(HttpApi.getImageUrl(item.header,
					CacheManage.IMAGE_TYPE_HEADER),
					CacheManage.IMAGE_TYPE_HEADER,R.drawable.avatar_none);
			baseActivity.urlMap.add(position, HttpApi.getImageUrl(item.header,
					CacheManage.IMAGE_TYPE_HEADER));
		} else {
			holder.header.setImageResource(R.drawable.avatar_none);
		}

		return convertView;
	}

	public static class ViewHolder {
		public TextView name, content, time;
		public RoundImageView header;
	}

	public static String text(String input) {
		if (null == input) {
			return "";
		}
		// if (input.indexOf("上海市闵行区") != -1) {
		// input = input.replace("上海市闵行区", "");
		// }
		if (input.length() > 20) {
			return input.substring(0, 20);
		} else {
			return input;
		}
	}

	public static String ToDBC(String input) {
		if (null == input) {
			return "";
		}
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}

}
