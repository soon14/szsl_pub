package com.bsoft.hospital.pub.suzhoumh.activity.app.pay;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.app.tanklib.view.BsoftActionBar;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.model.my.MyHospitalMoneyVo;


/**
 * Created by Administrator on 2016/1/22.
 */
public class ReceiptDetailActivity extends BaseActivity {

    private TextView tv_hospital;
    private TextView tv_num;
    private TextView tv_name;
    //private TextView tv_bed;
    private TextView tv_moeny;
    //private TextView tv_money_uppercase;
    private TextView tv_pay_type;
    private MyHospitalMoneyVo.Record record;
    private MyHospitalMoneyVo myHospitalMoneyVo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_detail);
        findView();
        initData();
    }

    @Override
    public void findView() {
        findActionBar();
        actionBar.setTitle("预缴金收据");
        actionBar.setBackAction(new BsoftActionBar.Action() {
            @Override
            public int getDrawable() {
                return R.drawable.btn_back;
            }

            @Override
            public void performAction(View view) {
                finish();
            }
        });
        tv_hospital = (TextView)findViewById(R.id.tv_hospital);
        tv_name = (TextView)findViewById(R.id.tv_name);
        tv_num = (TextView)findViewById(R.id.tv_num);
        tv_moeny = (TextView)findViewById(R.id.tv_money);
        tv_pay_type = (TextView)findViewById(R.id.tv_pay_type);
        //tv_bed = (TextView)findViewById(R.id.tv_bed);
        //tv_money_uppercase = (TextView)findViewById(R.id.tv_money_uppercase);
    }

    private void initData()
    {
        tv_hospital.setText("苏州市立医院");
        record = (MyHospitalMoneyVo.Record)getIntent().getSerializableExtra("record");
        myHospitalMoneyVo = (MyHospitalMoneyVo)getIntent().getSerializableExtra("myHospitalMoneyVo");
        tv_name.setText(myHospitalMoneyVo.brxm);
        tv_num.setText("NO."+record.jkxh);
        tv_pay_type.setText(record.jkfs);
        tv_moeny.setText("¥"+record.jkje);
        //tv_bed.setText(record.brch);
       // tv_money_uppercase.setText(record.jkjecn);

        /*if(record.jkfs.equals("9"))//支付宝
        {
            tv_pay_type.setBackgroundResource(R.drawable.receipt_alipay_bg);
        }
        else if(record.equals("10"))//微信
        {
            tv_pay_type.setBackgroundResource(R.drawable.receipt_weixin_bg);
        }
        else
        {
            tv_pay_type.setText("其他");
            tv_pay_type.setBackgroundResource(0);
        }*/
    }
}
