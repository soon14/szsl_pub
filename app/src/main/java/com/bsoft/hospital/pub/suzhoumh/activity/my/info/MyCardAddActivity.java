package com.bsoft.hospital.pub.suzhoumh.activity.my.info;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.util.AsyncTaskUtil;
import com.app.tanklib.util.StringUtil;
import com.app.tanklib.view.BsoftActionBar.Action;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.ChoiceItem;
import com.bsoft.hospital.pub.suzhoumh.model.CodeModel;
import com.bsoft.hospital.pub.suzhoumh.model.NullModel;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.my.HosVo;
import com.bsoft.hospital.pub.suzhoumh.model.my.MyCardVo;

/**
 * @author Tank E-mail:zkljxq@126.com
 * 
 * @类说明
 */
public class MyCardAddActivity extends BaseActivity {

	RelativeLayout layout1, layout2;
	EditText card;
	TextView cardText, address;
	ImageView cardclear;

	ChoiceItem cardResult, cityResult;
	HosVo hosVo;

	GetTask task;
	EditTask editTask;

	MyCardVo vo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mycard_add);
		vo = (MyCardVo) getIntent().getSerializableExtra("vo");
		findView();
		setClick();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.MyCardAddCardType_ACTION);
		filter.addAction(Constants.MyCardAddCity_ACTION);
		filter.addAction(Constants.MyCardAddHos_ACTION);
		this.registerReceiver(this.broadcastReceiver, filter);
		if (null == vo) {
			cardResult = new ChoiceItem("2", "就诊卡");
			if ("1".equals(cardResult.index)) {
				address.setText("选择城市");
			} else if ("2".equals(cardResult.index)) {
				address.setText("选择医院");
			}
		} else {
			cardResult = new ChoiceItem(vo.cardType + "",
					vo.cardType == 1 ? "医保卡" : "就诊卡");
			card.setText(vo.cardNum + "");
			cardText.setText(cardResult.itemName);
			if (vo.cardType == 1) {
				cityResult = new ChoiceItem(vo.belong, vo.belongName);
				address.setText(cityResult.itemName);
			} else {
				hosVo = new HosVo();
				hosVo.id = vo.belong;
				hosVo.title = vo.belongName;
				address.setText(hosVo.title);
			}
		}
	}

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (Constants.MyCardAddCardType_ACTION.equals(intent.getAction())) {
				cardResult = (ChoiceItem) intent.getSerializableExtra("result");
				cardText.setText(cardResult.itemName);
				if ("1".equals(cardResult.index)) {
					address.setText("选择城市");
				} else if ("2".equals(cardResult.index)) {
					address.setText("选择医院");
				}
			} else if (Constants.MyCardAddCity_ACTION
					.equals(intent.getAction())) {
				cityResult = (ChoiceItem) intent.getSerializableExtra("result");
				address.setText(cityResult.itemName);
			} else if (Constants.MyCardAddHos_ACTION.equals(intent.getAction())) {
				hosVo = (HosVo) intent.getSerializableExtra("result");
				address.setText(hosVo.title);
			}
		}
	};

	void setClick() {
		layout1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/*Intent intent = new Intent(baseContext, DicActivity.class);
				intent.putExtra("data", ModelCache.getInstance()
						.getCardTypeList());
				intent.putExtra("result", cardResult);
				intent.putExtra("title", "卡类型");
				intent.putExtra("action", Constants.MyCardAddCardType_ACTION);
				startActivity(intent);*/
			}
		});
		layout2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null == cardResult) {
					Toast.makeText(baseContext, "请先选择卡类型", Toast.LENGTH_SHORT)
							.show();
				} else {
					/*if ("1".equals(cardResult.index)) {
						Intent intent = new Intent(baseContext,
								CityActivity.class);
						intent.putExtra("title", "选择城市");
						intent.putExtra("action",
								Constants.MyCardAddCity_ACTION);
						startActivity(intent);
					} else if ("2".equals(cardResult.index)) {
						Intent intent = new Intent(baseContext,
								HosActivity.class);
						intent.putExtra("action", Constants.MyCardAddHos_ACTION);
						startActivity(intent);
					}*/
				}
			}
		});
		card.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
				if (card.getText().toString().length() == 0) {
					cardclear.setVisibility(View.INVISIBLE);
				} else {
					cardclear.setVisibility(View.VISIBLE);
				}
			}
		});
		cardclear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				card.setText("");
			}
		});
	}

	@Override
	public void findView() {
		findActionBar();
		if (null == vo) {
			actionBar.setTitle("新增卡");
		} else {
			actionBar.setTitle("编辑卡");
		}
		actionBar.setBackAction(new Action() {
			@Override
			public void performAction(View view) {
				if (null != getCurrentFocus()
						&& null != getCurrentFocus().getWindowToken()) {
					((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
							.hideSoftInputFromWindow(getCurrentFocus()
									.getWindowToken(), 0);
				}
				back();
			}

			@Override
			public int getDrawable() {
				return R.drawable.btn_back;
			}
		});
		actionBar.setRefreshTextView("提交", new Action() {

			@Override
			public void performAction(View view) {
				if (StringUtil.isEmpty(card.getText().toString())) {
					Toast.makeText(baseContext, "卡号为空，请输入", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				if (card.getText().toString().length() < 4
						|| card.getText().toString().length() > 50) {
					Toast.makeText(baseContext, "卡号数范围必须在4到50位之间",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if ("1".equals(cardResult.index)) {
					if (cityResult == null) {
						Toast.makeText(baseContext, "归属地为空，请选择",
								Toast.LENGTH_SHORT).show();
						return;
					}
				} else if ("2".equals(cardResult.index)) {
					if (hosVo == null) {
						Toast.makeText(baseContext, "归属地为空，请选择",
								Toast.LENGTH_SHORT).show();
						return;
					}
				}
				if (null == vo) {
					task = new GetTask();
					task.execute();
				} else {
					editTask = new EditTask();
					editTask.execute();
				}
			}

			@Override
			public int getDrawable() {
				return 0;
			}
		});
		layout1 = (RelativeLayout) findViewById(R.id.layout1);
		layout2 = (RelativeLayout) findViewById(R.id.layout2);
		card = (EditText) findViewById(R.id.card);
		cardclear = (ImageView) findViewById(R.id.cardclear);
		cardText = (TextView) findViewById(R.id.cardText);
		address = (TextView) findViewById(R.id.address);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		AsyncTaskUtil.cancelTask(editTask);
		AsyncTaskUtil.cancelTask(task);
		if (null != broadcastReceiver) {
			unregisterReceiver(broadcastReceiver);
			broadcastReceiver = null;
		}
	}

	class GetTask extends AsyncTask<Void, Object, ResultModel<CodeModel>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			actionBar.startTextRefresh();
		}

		@Override
		protected ResultModel<CodeModel> doInBackground(Void... params) {
			return HttpApi
					.getInstance()
					.parserData(
							CodeModel.class,
							"auth/ainfo/card/add",
							new BsoftNameValuePair("id", loginUser.id),
							new BsoftNameValuePair("cardtype", cardResult.index),
							new BsoftNameValuePair("cardnum", card.getText()
									.toString()),
							new BsoftNameValuePair(
									"belong",
									"1".equals(cardResult.index) ? cityResult.index
											: hosVo.id),
							new BsoftNameValuePair(
									"belongname",
									"1".equals(cardResult.index) ? cityResult.itemName
											: hosVo.title),
							new BsoftNameValuePair("sn", loginUser.sn));
		}

		@Override
		protected void onPostExecute(ResultModel<CodeModel> result) {
			actionBar.endTextRefresh();
			if (null != result) {
				if (result.statue == Statue.SUCCESS && null != result.data
						&& !StringUtil.isEmpty(result.data.id)) {
					Toast.makeText(baseContext, "保存卡成功", Toast.LENGTH_SHORT)
							.show();
					Intent intent = new Intent(Constants.MyCard_ACTION);
					MyCardVo vo = new MyCardVo();
					vo.cardNum = card.getText().toString();
					if ("1".equals(cardResult.index)) {
						vo.belong = cityResult.index;
						vo.belongName = cityResult.itemName;
					} else {
						vo.belong = hosVo.id;
						vo.belongName = hosVo.title;
					}
					vo.cardType = Integer.valueOf(cardResult.index);
					vo.id = result.data.id;
					if (!StringUtil.isEmpty(result.data.phrid)) {
						loginUser.phrid = result.data.phrid;
						application.setLoginUser(loginUser);
					}
					intent.putExtra("vo", vo);
					sendBroadcast(intent);
					sendBroadcast(new Intent(Constants.HomeUpdate_ACTION));
					if (null != getCurrentFocus()
							&& null != getCurrentFocus().getWindowToken()) {
						((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
								.hideSoftInputFromWindow(getCurrentFocus()
										.getWindowToken(), 0);
					}
					back();
				} else {
					result.showToast(baseContext);
				}
			}
		}
	}

	class EditTask extends AsyncTask<Void, Object, ResultModel<NullModel>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			actionBar.startTextRefresh();
		}

		@Override
		protected ResultModel<NullModel> doInBackground(Void... params) {
			return HttpApi
					.getInstance()
					.parserData(
							NullModel.class,
							"auth/ainfo/card/update",
							new BsoftNameValuePair("cid", vo.id),
							new BsoftNameValuePair("id", loginUser.id),
							new BsoftNameValuePair("cardtype", cardResult.index),
							new BsoftNameValuePair("cardnum", card.getText()
									.toString()),
							new BsoftNameValuePair(
									"belong",
									"1".equals(cardResult.index) ? cityResult.index
											: hosVo.id),
							new BsoftNameValuePair(
									"belongname",
									"1".equals(cardResult.index) ? cityResult.itemName
											: hosVo.title),
							new BsoftNameValuePair("sn", loginUser.sn));
		}

		@Override
		protected void onPostExecute(ResultModel<NullModel> result) {
			actionBar.endTextRefresh();
			if (null != result) {
				if (result.statue == Statue.SUCCESS) {
					Toast.makeText(baseContext, "修改卡成功", Toast.LENGTH_SHORT)
							.show();
					Intent intent = new Intent(Constants.MyCardEdit_ACTION);
					vo.cardNum = card.getText().toString();
					if ("1".equals(cardResult.index)) {
						vo.belong = cityResult.index;
						vo.belongName = cityResult.itemName;
					} else {
						vo.belong = hosVo.id;
						vo.belongName = hosVo.title;
					}
					vo.cardType = Integer.valueOf(cardResult.index);
					intent.putExtra("vo", vo);
					sendBroadcast(intent);
					sendBroadcast(new Intent(Constants.HomeUpdate_ACTION));
					if (null != getCurrentFocus()
							&& null != getCurrentFocus().getWindowToken()) {
						((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
								.hideSoftInputFromWindow(getCurrentFocus()
										.getWindowToken(), 0);
					}
					back();
				} else {
					result.showToast(baseContext);
				}
			}
		}
	}

}
