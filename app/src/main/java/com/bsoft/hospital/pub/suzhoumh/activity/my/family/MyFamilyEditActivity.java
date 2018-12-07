package com.bsoft.hospital.pub.suzhoumh.activity.my.family;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.time.WheelMain;
import com.app.tanklib.util.AsyncTaskUtil;
import com.app.tanklib.util.StringUtil;
import com.app.tanklib.view.BsoftActionBar.Action;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.AppApplication;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.my.info.MyInfoNatureActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.my.info.MyInfoSexActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.ChoiceItem;
import com.bsoft.hospital.pub.suzhoumh.model.NullModel;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.my.MyFamilyVo;
import com.bsoft.hospital.pub.suzhoumh.model.my.RelationVo;
import com.bsoft.hospital.pub.suzhoumh.model.my.MyFamilyVo.Cards;
import com.bsoft.hospital.pub.suzhoumh.util.IDCard;
import com.bsoft.hospital.pub.suzhoumh.view.AppProgressDialog;

/**
 * 家庭成员修改
 *
 * @author Administrator
 */
public class MyFamilyEditActivity extends BaseActivity implements OnClickListener {

    private AppProgressDialog progressDialog;
    private EditText name, idcard, mobile;
    private EditText card1;
    private ImageView idcardclear, mobileclear;
    public TextView sex, birthday, relation;
    private Button del;
    private RelativeLayout sexLayout, relationLayout;
    private LayoutInflater mInflater;

    private WheelMain wheelMain;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private String sTime = "";

    private ChoiceItem sexResult, relationResult;

    private EditTask edittask;
    private GetRelationTask relationtask;
    private DelTask deltask;

    private Dialog builder;
    private View viewDialog;
    private List<RelationVo> relationlist = new ArrayList<RelationVo>();


    private List<String> sexlist = new ArrayList<String>();//性别列表
    private List<String> relationStringlist = new ArrayList<String>();//关系列表

    //private int selectSexPosition = 0;//当前选择性别的
    private int selectRelationPosition = 0;//当前选择的关系
    private List<Cards> cardlist = new ArrayList<Cards>();
    private MyFamilyVo myfamilyvo;

    private Boolean canSaved = false;//是否能保存

    private String sexcode = "";
    private String cardsString = "";

    private String nature = "";
    private RelativeLayout natureLayout;
    private TextView tv_nature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.myfamily_add);
        mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        findView();
        initData();
        setViewState();
    }

    private void initData() {
        myfamilyvo = (MyFamilyVo) this.getIntent().getSerializableExtra("myfamilyvo");
        if (AppApplication.relationlist != null && AppApplication.relationlist.size() > 0) {
            this.relationlist = AppApplication.relationlist;
            for (int i = 0; i < relationlist.size(); i++) {
                relationStringlist.add(relationlist.get(i).title);
            }
            try {
                relation.setText(relationStringlist.get(Integer.valueOf(myfamilyvo.relation)));
                selectRelationPosition = Integer.valueOf(myfamilyvo.relation);
            } catch (NumberFormatException e) {
            }
        } else {
            relationtask = new GetRelationTask();
            relationtask.execute();
        }
        sexlist.add("男");
        sexlist.add("女");

        name.setText(myfamilyvo.realname);
        idcard.setText(IDCard.getHideCardStr(myfamilyvo.idcard));
        mobile.setText(IDCard.getHideMobileStr(myfamilyvo.mobile));

		/*if(myfamilyvo.accountcards!=null&&myfamilyvo.accountcards.size()>0)
        {
			for(int i=0;i<myfamilyvo.accountcards.size();i++)
			{
				Cards card = myfamilyvo.accountcards.get(i);
				if(card.cardtype.equals("1")&&card.cardnum!=null)
				{
					card1.setText(card.cardnum);
				}
			}
		}*/

        if (myfamilyvo.sexcode == 1) {
            sex.setText("男");
            //selectSexPosition = 0;
        } else if (myfamilyvo.sexcode == 2) {
            sex.setText("女");
            //selectSexPosition = 1;
        }

        if (!StringUtil.isEmpty(myfamilyvo.nature)) {

            if (myfamilyvo.nature.equals(MyInfoNatureActivity.ZF)) {
                tv_nature.setText("自费");
            } else if (myfamilyvo.nature.equals(MyInfoNatureActivity.SZYB)) {
                tv_nature.setText("苏州医保");
            } else if (myfamilyvo.nature.equals(MyInfoNatureActivity.YQYB)) {
                tv_nature.setText("园区医保");
            }
        }

    }

    private void setViewState() {
        if (canSaved) {
            name.setEnabled(true);
            idcard.setEnabled(true);
            idcard.setText(myfamilyvo.idcard);
            mobile.setText(myfamilyvo.mobile);
            sex.setEnabled(true);
            mobile.setEnabled(true);
            card1.setEnabled(true);
            relation.setEnabled(true);
            del.setVisibility(View.GONE);
            name.setSelection(name.getText().length());
            setClick();
        } else {
            name.setEnabled(false);
            idcard.setEnabled(false);
            idcard.setText(IDCard.getHideCardStr(myfamilyvo.idcard));
            mobile.setText(IDCard.getHideMobileStr(myfamilyvo.mobile));
            sex.setEnabled(false);
            mobile.setEnabled(false);
            card1.setEnabled(false);
            relation.setEnabled(false);
            del.setVisibility(View.VISIBLE);
            del.setOnClickListener(this);
        }
    }

    private void setClick() {

        idcard.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                if (idcard.getText().toString().length() == 0) {
                    idcardclear.setVisibility(View.INVISIBLE);
                } else {
                    idcardclear.setVisibility(View.VISIBLE);
                }
            }
        });
        idcard.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View arg0, boolean arg1) {
                if (!arg1) {
                    if (!StringUtil.isEmpty(idcard.getText().toString())) {
                        String cardValidate = IDCard.IDCardValidate(idcard.getText().toString());
                        if (!StringUtil.isEmpty(cardValidate)) {
                            Toast.makeText(MyFamilyEditActivity.this, cardValidate, Toast.LENGTH_SHORT).show();
                        } else {
                            if ("请选择".equals(sex.getText().toString())) {
                                sexResult = IDCard.getChoiceSex(idcard.getText().toString());
                                sex.setText(sexResult.itemName);
                            }
                            if ("请选择".equals(birthday.getText().toString())) {
                                sTime = IDCard.getBirthDay(idcard.getText().toString());
                                birthday.setText(sTime);
                            }
                        }
                    }
                }
            }
        });
        mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                if (mobile.getText().toString().length() == 0) {
                    mobileclear.setVisibility(View.INVISIBLE);
                } else {
                    mobileclear.setVisibility(View.VISIBLE);
                }
            }
        });
        mobileclear.setOnClickListener(this);
        idcardclear.setOnClickListener(this);
        sexLayout.setOnClickListener(this);
        relationLayout.setOnClickListener(this);
        natureLayout.setOnClickListener(this);
    }

    @Override
    public void findView() {
        findActionBar();
        actionBar.setTitle("家庭成员编辑");
        actionBar.setBackAction(new Action() {
            @Override
            public void performAction(View view) {
                if (null != getCurrentFocus() && null != getCurrentFocus().getWindowToken()) {
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }

                back();
            }

            @Override
            public int getDrawable() {
                return R.drawable.btn_back;
            }
        });
        actionBar.setRefreshTextView("编辑", editAction);
        name = (EditText) findViewById(R.id.name);
        idcard = (EditText) findViewById(R.id.idcard);
        card1 = (EditText) findViewById(R.id.card1);
        mobile = (EditText) findViewById(R.id.mobile);
        sex = (TextView) findViewById(R.id.sex);
        birthday = (TextView) findViewById(R.id.birthday);
        relation = (TextView) findViewById(R.id.relation);
        del = (Button) findViewById(R.id.del);
        del.setVisibility(View.VISIBLE);
        sexLayout = (RelativeLayout) findViewById(R.id.sexLayout);
        relationLayout = (RelativeLayout) findViewById(R.id.relationLayout);
        natureLayout = (RelativeLayout) findViewById(R.id.natureLayout);
        tv_nature = (TextView) findViewById(R.id.nature);
        idcardclear = (ImageView) findViewById(R.id.idcardclear);
        mobileclear = (ImageView) findViewById(R.id.mobileclear);
        //mainView = findViewById(R.id.mainView);
    }

    Action editAction = new Action() {

        @Override
        public int getDrawable() {
            return 0;
        }

        @Override
        public void performAction(View view) {
            if (canSaved)//点击保存
            {
                save();
            } else//点击编辑
            {
                canSaved = true;
                actionBar.setRefreshTextView("保存", editAction);
                setViewState();
            }
        }

    };

    /**
     * 修改成员
     */
    public void save() {
        if (StringUtil.isEmpty(name.getText().toString())) {
            name.requestFocus();
            Toast.makeText(MyFamilyEditActivity.this, "姓名不能为空，请输入", Toast.LENGTH_SHORT).show();
            return;
        }

        if (StringUtil.isEmpty(idcard.getText().toString())) {
            idcard.requestFocus();
            Toast.makeText(MyFamilyEditActivity.this, "身份证号不能为空，请输入", Toast.LENGTH_SHORT).show();
            return;
        }
        String cardValidate = IDCard.IDCardValidate(idcard.getText().toString());
        if (!StringUtil.isEmpty(cardValidate)) {
            idcard.requestFocus();
            Toast.makeText(MyFamilyEditActivity.this, cardValidate, Toast.LENGTH_SHORT).show();
            return;
        }
        if (sex.getText().toString().equals("请选择")) {
            Toast.makeText(MyFamilyEditActivity.this, "性别还未选择，请选择", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!sex.getText().toString().equals(IDCard.getSex(idcard.getText().toString()))) {
            Toast.makeText(MyFamilyEditActivity.this, "性别和身份证性别不一致，请重新选择", Toast.LENGTH_SHORT).show();
            return;
        }
        if (tv_nature.getText().toString().equals("请选择")) {
            Toast.makeText(MyFamilyEditActivity.this, "病人性质还未选择，请选择", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtil.isEmpty(mobile.getText().toString())) {
            mobile.requestFocus();
            Toast.makeText(MyFamilyEditActivity.this, "电话号码不能为空，请输入", Toast.LENGTH_SHORT).show();
            return;
        } else if (!StringUtil.isMobilPhoneNumber(mobile.getText().toString())) {
            mobile.requestFocus();
            Toast.makeText(MyFamilyEditActivity.this, "电话号码不符合，请重新输入", Toast.LENGTH_SHORT).show();
            return;
        }
        if (relation.getText().toString().equals("请选择")) {
            Toast.makeText(MyFamilyEditActivity.this, "与本人关系还未选择，请选择", Toast.LENGTH_SHORT).show();
            return;
        }
        if (sex.getText().equals("男")) {
            sexcode = "1";
        } else {
            sexcode = "2";
        }
        if (!card1.getText().toString().equals("")) {
            JSONObject object = new JSONObject();
            HashMap<String, String> map1 = new HashMap<String, String>();
            map1.put("cardtype", "1");
            map1.put("cardnum", card1.getText().toString());
            /*Map<String,String> map2 = new HashMap<String,String>();
            map2.put("cardtype", "2");*/
            List<HashMap> list = new ArrayList<HashMap>();
            list.add(map1);
            object.put("accountcards", list);
            cardsString = object.toJSONString();
        }
        edittask = new EditTask();
        edittask.execute();
    }

    /**
     * 删除成员
     */
    private void del() {
        new AlertDialog.Builder(MyFamilyEditActivity.this).setMessage("确定删除该成员？").setTitle("提示").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deltask = new DelTask();
                deltask.execute();
            }
        }).setNegativeButton("取消", null).create().show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AsyncTaskUtil.cancelTask(edittask);
        AsyncTaskUtil.cancelTask(deltask);
        AsyncTaskUtil.cancelTask(relationtask);
    }

    class EditTask extends AsyncTask<Void, Object, ResultModel<MyFamilyVo>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            actionBar.startTextRefresh();
        }

        @Override
        protected ResultModel<MyFamilyVo> doInBackground(Void... params) {

            return HttpApi.getInstance().parserData(MyFamilyVo.class, "auth/family/modify", new BsoftNameValuePair("rid", myfamilyvo.id), new BsoftNameValuePair("realname", name.getText().toString()), new BsoftNameValuePair("sexcode", sexcode), new BsoftNameValuePair("idcard", idcard.getText().toString()), new BsoftNameValuePair("nature", nature), new BsoftNameValuePair("mobile", mobile.getText().toString()), new BsoftNameValuePair("relation", String.valueOf(selectRelationPosition)), new BsoftNameValuePair("id", loginUser.id), new BsoftNameValuePair("sn", loginUser.sn));
        }

        @Override
        protected void onPostExecute(ResultModel<MyFamilyVo> result) {
			/*if (progressDialog != null) {
				progressDialog.stop();
				progressDialog = null;
			}*/
            actionBar.endTextRefresh();
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    if (null != result.data) {
                        Toast.makeText(baseContext, "修改家庭成员成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Constants.MyFamily_ACTION);
                        // MyFamilyVo vo = new MyFamilyVo();
                        // vo.idcard = idcard.getText().toString();
                        // vo.name = name.getText().toString();
                        // vo.relation = relationResult.index;
                        // // vo.uid=;
                        // vo.activated = 0;
                        intent.putExtra("vo", result.data);
                        sendBroadcast(intent);
                        if (null != getCurrentFocus() && null != getCurrentFocus().getWindowToken()) {
                            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                        }
                        back();
                    } else {
                        Toast.makeText(baseContext, "修改家庭成员失败", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    result.showToast(baseContext);
                }
            }
        }
    }

    class GetRelationTask extends AsyncTask<Void, Object, ResultModel<List<RelationVo>>> {

        @Override
        protected ResultModel<List<RelationVo>> doInBackground(Void... params) {
            return HttpApi.getInstance().parserArray(RelationVo.class, "auth/family/getrelation", new BsoftNameValuePair("id", loginUser.id), new BsoftNameValuePair("sn", loginUser.sn));
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            actionBar.startTextRefresh();
        }

        @Override
        protected void onPostExecute(ResultModel<List<RelationVo>> result) {
            actionBar.endTextRefresh();
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    if (null != result.list) {
                        relationlist = result.list;
                        AppApplication.relationlist = relationlist;
                        for (int i = 0; i < relationlist.size(); i++) {
                            relationStringlist.add(relationlist.get(i).title);
                        }
                        try {
                            relation.setText(relationStringlist.get(Integer.valueOf(myfamilyvo.relation)));//设置关系
                            selectRelationPosition = Integer.valueOf(myfamilyvo.relation);
                        } catch (NumberFormatException e) {
                        }
                    }
                } else {
                    result.showToast(baseContext);
                }
            }
        }
    }

    class DelTask extends AsyncTask<Void, Object, ResultModel<NullModel>> {

        @Override
        protected ResultModel<NullModel> doInBackground(Void... params) {
            return HttpApi.getInstance().parserArray(RelationVo.class, "auth/family/delete", new BsoftNameValuePair("id", loginUser.id), new BsoftNameValuePair("sn", loginUser.sn), new BsoftNameValuePair("rid", myfamilyvo.id));
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            actionBar.startTextRefresh();
        }

        @Override
        protected void onPostExecute(ResultModel<NullModel> result) {
            // TODO Auto-generated method stub
            actionBar.endTextRefresh();
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    Toast.makeText(baseContext, "删除家庭成员成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Constants.MyFamily_ACTION);
                    sendBroadcast(intent);
                    back();
                } else {
                    result.showToast(baseContext);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.idcardclear:
                idcard.setText("");
                break;
            case R.id.del:
                del();
                break;
            case R.id.mobileclear:
                mobile.setText("");
                break;
            case R.id.sexLayout:
                Intent intent = new Intent(baseContext, MyInfoSexActivity.class);
                if (sex.getText().toString().equals("男")) {
                    intent.putExtra("sex", 1);
                } else if (sex.getText().toString().equals("女")) {
                    intent.putExtra("sex", 2);
                }
                startActivityForResult(intent, 1);
                //DialogListAdapter sexadapter =new DialogListAdapter(baseContext,sexlist,selectSexPosition);
                //showSelectDialog("性别",sexadapter,0);
                break;
            case R.id.relationLayout:
                Intent relationIntent = new Intent(baseContext, MyFamilyRelationActivity.class);
                relationIntent.putExtra("selectRelationPosition", selectRelationPosition);
                startActivityForResult(relationIntent, 2);
                //DialogListAdapter relationadapter =new DialogListAdapter(baseContext,relationStringlist,selectRelationPosition);
                //showSelectDialog("关系",relationadapter,1	);
                break;
            case R.id.natureLayout:
                Intent natureIntent = new Intent(baseContext, MyInfoNatureActivity.class);
                Log.d("test", tv_nature.getText().toString());
                if (tv_nature.getText().toString().equals("自费")) {
                    nature = "1";
                } else if (tv_nature.getText().toString().equals("医保")) {
                    nature = "2";
                }
                if (tv_nature.getText().equals("自费")) {
                    nature = MyInfoNatureActivity.ZF;
                } else if (tv_nature.getText().equals("园区医保")) {
                    nature = MyInfoNatureActivity.YQYB;
                } else if (tv_nature.getText().equals("苏州医保")) {
                    nature = MyInfoNatureActivity.SZYB;
                }
                natureIntent.putExtra("nature", nature);
                startActivityForResult(natureIntent, 3);
                break;
        }
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
        if (arg1 == RESULT_OK) {
            switch (arg0) {
                case 1://选择性别过来的
                    sexcode = arg2.getStringExtra("sexcode");
                    if (sexcode.equals("1")) {
                        sex.setText("男");
                    } else if (sexcode.equals("2")) {
                        sex.setText("女");
                    }
                    break;
                case 2://选择关系过来的
                    selectRelationPosition = arg2.getIntExtra("selectRelationPosition", 0);
                    relation.setText(relationStringlist.get(selectRelationPosition));
                    break;
                case 3:
                    nature = arg2.getStringExtra("nature");

                    if (nature.equals(MyInfoNatureActivity.ZF)) {
                        tv_nature.setText("自费");
                    } else if (nature.equals(MyInfoNatureActivity.SZYB)) {
                        tv_nature.setText("苏州医保");
                    } else if (nature.equals(MyInfoNatureActivity.YQYB)) {
                        tv_nature.setText("园区医保");
                    }
                    break;
            }
        }
    }
	
	/*
	*//**
     * 显示选择对话框
     * @param title
     * @param dialogAdapter
     *//*
	private void showSelectDialog(String title,DialogListAdapter dialogAdapter,final int selectType)
	{
		builder = new Dialog(baseContext, R.style.alertDialogTheme);
		viewDialog = mInflater.inflate(R.layout.dialog_list,
				null);
		ListView listview = (ListView)viewDialog.findViewById(R.id.lv_content);
		TextView tv_title = (TextView)viewDialog.findViewById(R.id.tv_title);
		tv_title.setText(title);
		listview.setAdapter(dialogAdapter);
		listview.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				switch(selectType)
				{
				case 0://性别
					selectSexPosition = position;
					sex.setText(sexlist.get(position));
					builder.dismiss();
					break;
				case 1://关系
					selectRelationPosition = position;
					relation.setText(relationStringlist.get(position));
					builder.dismiss();
					break;
				}
			}
		});
		// 设置对话框的宽高
		LayoutParams layoutParams = new LayoutParams(AppApplication
				.getWidthPixels() * 85 / 100, LayoutParams.WRAP_CONTENT);
		builder.setContentView(viewDialog, layoutParams);
		builder.show();
	}*/
}
