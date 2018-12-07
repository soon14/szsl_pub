package com.bsoft.hospital.pub.suzhoumh.util.pop.impl;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bsoft.hospital.pub.suzhoumh.AppApplication;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.model.LoginUser;
import com.bsoft.hospital.pub.suzhoumh.util.SpUtil;
import com.bsoft.hospital.pub.suzhoumh.util.pop.CampusSelection;
import com.bsoft.hospital.pub.suzhoumh.util.pop.CustomPopWindow;

import butterknife.ButterKnife;


/**
 * @author :lizhengcao
 * @date :2018/7/4
 * E-mail:lizc@bsoft.com.cn
 * @类说明 院区选择的pw类
 */

public class CampusSelectionPopWindowImpl {

    private String[] largeNameHospital = {"苏州市立医院本部(道前街)", "苏州市立医院东区(白塔西路)", "苏州市立医院北区(广济路)"};
    private CustomPopWindow.PopupWindowBuilder builder;

    private Context mContext;
    private CustomPopWindow customPopWindow;
    private boolean isLoginWindow;
    private CampusSelection mCampusSelection;
    private AppApplication application;

    public CampusSelectionPopWindowImpl(Context context, AppApplication application, boolean isLoginWindow, CampusSelection campusSelection) {
        this.mContext = context;
        this.application = application;
        this.isLoginWindow = isLoginWindow;
        this.mCampusSelection = campusSelection;
    }


    public void setPopWindow() {
        if (builder == null)
            builder = new CustomPopWindow.PopupWindowBuilder(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_campus_selection_view, null);

        //本部
        TextView headquarters = (TextView) view.findViewById(R.id.tv_headquarters);
        //东区
        TextView easternDistrict = (TextView) view.findViewById(R.id.tv_eastern_district);
        //北区
        TextView northDistrict = (TextView) view.findViewById(R.id.tv_north_district);

        headquarters.setText(largeNameHospital[0]);
        easternDistrict.setText(largeNameHospital[1]);
        northDistrict.setText(largeNameHospital[2]);

        builder.setView(view);

        builder.enableBackgroundDark(true);
        builder.setBgDarkAlpha(0.7f);
        customPopWindow = builder.create();

        View v = new View(mContext);
        customPopWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);

        //取消
        view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customPopWindow.dissmiss();
            }
        });

        //本部
        headquarters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoginWindow)
                    mCampusSelection.setCampusSelectionDetail(largeNameHospital[0]);
                else {
                    Constants.setHospitalName(Constants.typeHospitalNameHeadquarters);
                    mCampusSelection.setCampusSelectionDetail(Constants.getHospitalName());
                    setLongitudeAndLatitude(Constants.LongitudeAndHeadquarters);
                }


                setURlIDName(
                        Constants.HttpUrlHeadquarters,
                        Constants.HOSPITAL_ID_Headquarters,
                        Constants.typeHospitalNameHeadquarters);
                customPopWindow.dissmiss();
            }
        });
        //东区
        easternDistrict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoginWindow)
                    mCampusSelection.setCampusSelectionDetail(largeNameHospital[1]);
                else {
                    Constants.setHospitalName(Constants.typeHospitalNameEastern);
                    mCampusSelection.setCampusSelectionDetail(Constants.getHospitalName());
                    setLongitudeAndLatitude(Constants.LongitudeAndLatitudeEastern);
                }
                setURlIDName(
                        Constants.HttpUrlEasternDistrict,
                        Constants.HOSPITAL_ID_Eastern,
                        Constants.typeHospitalNameEastern);
                customPopWindow.dissmiss();
            }
        });
        //北区
        northDistrict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoginWindow)
                    mCampusSelection.setCampusSelectionDetail(largeNameHospital[2]);
                else {
                    Constants.setHospitalName(Constants.typeHospitalNameNorth);
                    mCampusSelection.setCampusSelectionDetail(Constants.getHospitalName());
                    setLongitudeAndLatitude(Constants.LongitudeAndLatitudeNorth);
                }

                setURlIDName(
                        Constants.HttpUrlNorthDistrict,
                        Constants.HOSPITAL_ID_North,
                        Constants.typeHospitalNameNorth);

                customPopWindow.dissmiss();
            }
        });
    }

    private void setURlIDName(String url, String id, String name) {
        Constants.setHttpUrl(url);
        Constants.setHospitalID(id);
        Constants.setHospitalName(name);
    }

    /**
     * 下标0表示longitude，下标为1表示
     *
     * @param LongitudeAndLatitudeInfo
     */
    private void setLongitudeAndLatitude(String[] LongitudeAndLatitudeInfo) {
        LoginUser loginUser = application.getLoginUser();
        loginUser.longitude = LongitudeAndLatitudeInfo[0];
        loginUser.latitude = LongitudeAndLatitudeInfo[1];
        application.setLoginUser(loginUser);
    }

}
