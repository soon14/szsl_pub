package com.bsoft.hospital.pub.suzhoumh.activity.my;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.tanklib.dialog.AlertDialogWithButton;
import com.app.tanklib.view.BsoftActionBar.Action;
import com.bsoft.hospital.pub.suzhoumh.AppApplication;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;

public class SettingAboutActivity extends BaseActivity {

    private String title;
    private ImageView ivQrCode;
    private TextView tvHospitalName;
    TextView tvPhone;
    String phone = "0512-62364720";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settingabout);
        findView();
        setBarCodeBackground();
        setClick();
    }


    /**
     * 不同医院对应不同的二维码
     */
    private void setBarCodeBackground() {
        String httpUrl = Constants.getHttpUrl();
        switch (httpUrl) {
            case Constants.HttpUrlEasternDistrict:
                //东区
                setIconDrawable(R.drawable.icon_barcode, Constants.typeHospitalNameEastern);
                break;
            case Constants.HttpUrlHeadquarters:
                //本部
                setIconDrawable(R.drawable.icon_barcode_quarters, Constants.typeHospitalNameHeadquarters);
                break;
            case Constants.HttpUrlNorthDistrict:
                //北区
                setIconDrawable(R.drawable.icon_barcode_north, Constants.typeHospitalNameNorth);
                break;
            default:
                //东区
                setIconDrawable(R.drawable.icon_barcode, Constants.typeHospitalNameEastern);
                break;
        }
    }

    /**
     * 设置二维码背景图片
     *
     * @param id 图片id
     */
    private void setIconDrawable(@DrawableRes int id, String text) {
        ivQrCode.setBackgroundDrawable(getResources().getDrawable(id));
        tvHospitalName.setText(text);
    }

    private void setClick() {
        tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialogWithButton dialog = new AlertDialogWithButton(baseContext);
                dialog.build(true, AppApplication.getWidthPixels() * 80 / 100)
                        .message(phone)
                        .color(R.color.actionbar_bg)
                        .setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("呼叫", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                                startActivity(intent);
                                dialog.dismiss();
                            }
                        }).show();

            }
        });
    }


    @Override
    public void findView() {
        findActionBar();
        actionBar.setBackAction(new Action() {

            @Override
            public int getDrawable() {
                return R.drawable.btn_back;
            }

            @Override
            public void performAction(View view) {
                finish();
            }

        });
        title = getIntent().getStringExtra("title");
        actionBar.setTitle(title);
        tvPhone = (TextView) findViewById(R.id.tv_phone);
        tvPhone.setText("联系方式：" + phone);
        ivQrCode = (ImageView) findViewById(R.id.iv_qr_code);
        tvHospitalName = (TextView) findViewById(R.id.tv_hospital_name);
    }

}
