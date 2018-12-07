package com.bsoft.hospital.pub.suzhoumh.activity.adapter;

import java.util.ArrayList;
import java.util.List;

import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.view.TipsView;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.fragment.index.MessageFragment;
import com.bsoft.hospital.pub.suzhoumh.model.Message;
import com.bsoft.hospital.pub.suzhoumh.push.PushInfo;
import com.bsoft.hospital.pub.suzhoumh.util.DateUtil;
import com.bsoft.hospital.pub.suzhoumh.view.CountView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MessageAdapter extends BaseAdapter {
	private LayoutInflater mLayoutInflater;
	private List<Message> dataList = new ArrayList<Message>();
	public MessageFragment messageFragment;
	Context baseActivity;

	public MessageAdapter(Context baseActivity, MessageFragment messageFragment) {
		this.baseActivity = baseActivity;
		this.messageFragment = messageFragment;
		this.mLayoutInflater = (LayoutInflater) baseActivity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void clearCount() {
		if (null != dataList && dataList.size() > 0) {
			for (Message ms : dataList) {
				ms.count = 0;
			}
		}
		notifyDataSetChanged();
	}

	public void addData(List<Message> msgs) {
		if (null != msgs) {
			dataList = msgs;
		} else {
			dataList = new ArrayList<Message>();
		}
		notifyDataSetChanged();
	}

	public void addMoreData(List<Message> msgs) {
		if (null != msgs) {
			dataList.addAll(msgs);
		}
		notifyDataSetChanged();
	}

	public void remove(int index) {
		dataList.remove(index);
		notifyDataSetChanged();
	}

	public void changeCount(int kinds) {
		if (null != dataList && dataList.size() > 0) {
			for (Message vo : dataList) {
				if (vo.kinds == kinds) {
					vo.count = 0;
				}
			}
			notifyDataSetChanged();
		}
	}

	public void doPush(PushInfo info) {
		boolean flage = false;
		if (null != dataList && dataList.size() > 0) {
			for (Message vo : dataList) {
				if (vo.kinds == info.getKinds()) {
					vo.count++;
					vo.content = info.description;
					flage = true;
				}
			}
		}
		if (!flage) {
			// 新增
			Message message = new Message();
			message.content = info.description;
			message.count = 1;
			message.date = DateUtil.getNowTime();
			message.kinds = info.getKinds();
			message.name = message.getName();
			dataList.add(0, message);
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public Message getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.message_item, null);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.text = (TextView) convertView.findViewById(R.id.text);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.count = (CountView) convertView.findViewById(R.id.count);
			holder.header = (ImageView) convertView.findViewById(R.id.header);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final Message item = dataList.get(position);
		holder.text.setText(item.content);
		//holder.time.setText(DateUtil.getDateTime(item.date));
		holder.time.setText(DateUtil.getDayAndTime(item.date));
		holder.count.setCount(item.count);
		if (messageFragment.getTipsView() != null) {
			messageFragment.getTipsView().attach(holder.count,
					new TipsView.Listener() {
						@Override
						public void onStart() {
							holder.count.setVisibility(View.INVISIBLE);
							messageFragment.getListView()
									.requestDisallowInterceptTouchEvent(true);
						}

						@Override
						public void onComplete() {
							new Thread() {
								public void run() {
									HttpApi.getInstance()
											.post("auth/msg/watchkinds",
													new BsoftNameValuePair(
															"kinds",
															String.valueOf(item.kinds)),
													new BsoftNameValuePair(
															"id",
															messageFragment.loginUser.id),
													new BsoftNameValuePair(
															"sn",
															messageFragment.loginUser.sn));
								};
							}.start();
							/*Intent intent = new Intent(
									Constants.MessageCount_ACTION);
							intent.putExtra("kinds", item.kinds);
							baseActivity.sendBroadcast(intent);
							AppApplication.messageCount = AppApplication.messageCount
									- item.count;
							baseActivity.sendBroadcast(new Intent(
									Constants.HomeMessageCount_ACTION));*/
						}

						@Override
						public void onCancel() {
							holder.count.setVisibility(View.VISIBLE);
						}
					});
		}
		switch (item.kinds) {
		case 1:
			holder.header.setImageResource(R.drawable.icon_message1);
			holder.name.setText("系统管理");
			break;
		case 2:
			holder.header.setImageResource(R.drawable.icon_message2);
			holder.name.setText("预约挂号");
			break;
		case 3:
			holder.header.setImageResource(R.drawable.icon_message3);
			holder.name.setText("陪诊服务");
			break;
		case 4:
			holder.header.setImageResource(R.drawable.icon_message4);
			holder.name.setText("计免");
			break;
		case 5:
			holder.header.setImageResource(R.drawable.icon_message5);
			holder.name.setText("儿童");
			break;
		case 6:
			holder.header.setImageResource(R.drawable.icon_message7);
			holder.name.setText("孕妇");
			break;
		case 7:
			holder.header.setImageResource(R.drawable.icon_message6);
			holder.name.setText("慢病");
			break;
		case 11:
			holder.header.setImageResource(R.drawable.icon_message4);
			holder.name.setText("留言板");
			
		default:
			break;
		}
		return convertView;
	}

	public static class ViewHolder {
		public TextView name, text, time;
		public ImageView header;
		public CountView count;
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
