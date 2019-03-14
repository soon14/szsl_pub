package com.bsoft.hospital.pub.suzhoumh.activity.cloud;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.view.BsoftActionBar;
import com.app.tanklib.view.IndicatorFragmentActivity;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.CityVO;
import com.bsoft.hospital.pub.suzhoumh.model.CloudExpressReceiverModel;
import com.bsoft.hospital.pub.suzhoumh.model.DistrictVO;
import com.bsoft.hospital.pub.suzhoumh.model.NullModel;
import com.bsoft.hospital.pub.suzhoumh.model.ProvinceVO;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.app.appoint.RegistrationInfoVo;
import com.bsoft.hospital.pub.suzhoumh.picker.OptionsPickerView;
import com.bsoft.hospital.pub.suzhoumh.util.AssertUtil;
import com.bsoft.hospital.pub.suzhoumh.util.JsonUtil;
import com.bsoft.hospital.pub.suzhoumh.util.KeyboardUtils;
import com.bsoft.hospital.pub.suzhoumh.util.ObjectUtil;
import com.bsoft.hospital.pub.suzhoumh.util.StringUtils;
import com.bsoft.hospital.pub.suzhoumh.util.ToastUtils;

import org.w3c.dom.Element;
import org.w3c.dom.Text;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * @author :lizhengcao
 * @date :2019/3/13
 * E-mail:lizc@bsoft.com.cn
 * @类说明 新增收货地址
 */
public class NewShippingAddressActivity extends BaseActivity {

    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_mobile)
    EditText etMobile;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.et_postcode)
    EditText etPostcode;
    @BindView(R.id.tv_province)
    TextView tvProvince;

    private ArrayList<ProvinceVO> mProvinceList;
    private ArrayList<ArrayList<CityVO>> mCityList;
    private ArrayList<ArrayList<ArrayList<DistrictVO>>> mDistrictList;
    private OptionsPickerView mPickerView;
    private int mProvincePosition;
    private ProvinceVO mProvinceVO;
    private int mCityPosition;
    private CityVO mCityVO;
    private int mDistrictPosition;
    private DistrictVO mDistrictVO;
    private String name;
    private String address;
    private String mobile;
    private String postCode;
    private String province;
    private boolean isEdit;
    private String addrId;
    private CloudExpressReceiverModel data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_shipping_new);
        mUnbinder = ButterKnife.bind(this);
        initData();
        findView();
    }

    private void initData() {
        isEdit = getIntent().getBooleanExtra("isEdit", false);
        if (isEdit) {
            data = (CloudExpressReceiverModel) getIntent().getSerializableExtra("cloudExpressReceiverModel");
            etName.setText(data.collectName);
            etMobile.setText(data.collectPhone);
            etAddress.setText(data.collectDetailAdress);
            etPostcode.setText(data.postCode);
            tvProvince.setText(String.format("%s%s%s", data.collectProvincial, data.collectCity, data.collectArea));
            addrId = data.addrId;
        }
    }

    @OnClick({R.id.layout_province, R.id.btn_save})
    public void doClick(View v) {
        switch (v.getId()) {
            case R.id.layout_province:
                KeyboardUtils.hideSoftInput(this);
                showProvincePickerView();
                break;
            case R.id.btn_save:
                saveNewShippingAddress();
                break;
        }
    }

    private void saveNewShippingAddress() {
        name = getEditStr(etName);
        address = getEditStr(etAddress);
        mobile = getEditStr(etMobile);
        postCode = getEditStr(etPostcode);
        province = tvProvince.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            ToastUtils.showToastShort("收件人姓名不能为空");
            return;
        }

        if (TextUtils.isEmpty(mobile)) {
            ToastUtils.showToastShort("手机号码不能为空");
            return;
        }

        if (mobile.length() != 11) {
            ToastUtils.showToastShort("手机号码的位数不正确");
            return;
        }

        if (TextUtils.isEmpty(province)) {
            ToastUtils.showToastShort("省市区不能为空");
            return;
        }

        if (TextUtils.isEmpty(address)) {
            ToastUtils.showToastShort("详细地址不能为空");
            return;
        }

        if (TextUtils.isEmpty(postCode)) {
            ToastUtils.showToastShort("邮政编码不能为空");
            return;
        }
        KeyboardUtils.hideSoftInput(this);
        new GetDataTask().execute();
    }


    /**
     * 保存常用地址
     */
    @SuppressLint("StaticFieldLeak")
    private class GetDataTask extends
            AsyncTask<Void, Void, ResultModel<List<NullModel>>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            actionBar.startTextRefresh();
        }

        @SuppressWarnings("unchecked")
        @Override
        protected ResultModel<List<NullModel>> doInBackground(
                Void... params) {
            String method;
            String collectProvincial;
            String provinceCode;
            String collectCity;
            String cityCode;
            String collectArea;
            String areaCode;
            if (isEdit) {
                method = "auth/cloudClinic/updatePostAddrByAddrId";
                collectProvincial = data.collectProvincial;
                provinceCode = data.provinceCode;
                collectCity = data.collectCity;
                cityCode = data.cityCode;
                collectArea = data.collectArea;
                areaCode = data.areaCode;
            } else {
                method = "auth/cloudClinic/savePostAddr";
                collectProvincial = mProvinceVO.des;
                provinceCode = mProvinceVO.code;
                collectCity = mCityVO.des;
                cityCode = mCityVO.code;
                collectArea = mDistrictVO.des;
                areaCode = mDistrictVO.code;
            }
            List<BsoftNameValuePair> pairs = new ArrayList<>();
            if (isEdit) {
                pairs.add(new BsoftNameValuePair("addrId", addrId));
                pairs.add(new BsoftNameValuePair("userId", loginUser.id));
            }
            pairs.add(new BsoftNameValuePair("collectName", name));
            pairs.add(new BsoftNameValuePair("collectPhone", mobile));
            pairs.add(new BsoftNameValuePair("collectProvincial", collectProvincial));
            pairs.add(new BsoftNameValuePair("provinceCode", provinceCode));
            pairs.add(new BsoftNameValuePair("collectCity", collectCity));
            pairs.add(new BsoftNameValuePair("cityCode", cityCode));
            pairs.add(new BsoftNameValuePair("collectArea", collectArea));
            pairs.add(new BsoftNameValuePair("areaCode", areaCode));
            pairs.add(new BsoftNameValuePair("collectDetailAdress", address));
            pairs.add(new BsoftNameValuePair("postCode", postCode));
            pairs.add(new BsoftNameValuePair("id", loginUser.id));
            pairs.add(new BsoftNameValuePair("sn", loginUser.sn));

            BsoftNameValuePair[] pairsArr = new BsoftNameValuePair[pairs.size()];
            for (int i = 0; i < pairsArr.length; i++) {
                pairsArr[i] = pairs.get(i);
            }
            return HttpApi.getInstance().parserArray(NullModel.class, method,
                    pairsArr);

        }

        @Override
        protected void onPostExecute(ResultModel<List<NullModel>> result) {
            super.onPostExecute(result);
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    if (isEdit)
                        ToastUtils.showToastShort("收货地址更新成功");
                    else
                        ToastUtils.showToastShort("收货地址保存成功");
                    startActivity(new Intent(baseContext, CloudExpressReceiverActivity.class));
                    finish();
                } else {
                    result.showToast(baseContext);
                }
            } else {
                ToastUtils.showToastShort("加载失败");
            }
            actionBar.endTextRefresh();
        }
    }

    private String getEditStr(EditText et) {
        return et.getText().toString().trim();
    }

    private void showProvincePickerView() {

        if (ObjectUtil.isEmpty(mProvinceList)
                || ObjectUtil.isEmpty(mCityList)
                || ObjectUtil.isEmpty(mDistrictList)) {
            initProvinceData();

            mPickerView = new OptionsPickerView(this);
            mPickerView.setOnOptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3) {
                    mProvincePosition = options1;
                    mProvinceVO = mProvinceList.get(mProvincePosition);
                    mCityPosition = options2;
                    mCityVO = mCityList.get(mProvincePosition).get(mCityPosition);
                    mDistrictPosition = options3;
                    mDistrictVO = mDistrictList.get(mProvincePosition).get(mCityPosition)
                            .get(mDistrictPosition);
                    tvProvince.setText(String.format("%s%s%s", mProvinceVO.des, mCityVO.des, mDistrictVO.des));
                    /*callOnNextInput(2);*/
                }
            });
            mPickerView.setCancelable(true);
            mPickerView.setTitle("请选择省市区");
            mPickerView.setPicker(mProvinceList, mCityList, mDistrictList, true);
            mPickerView.setCyclic(false);
        }
        mPickerView.setSelectOptions(mProvincePosition, mCityPosition, mDistrictPosition);
        mPickerView.show();
    }

    private void initProvinceData() {
        String provinceJsonData = AssertUtil.getJson(this, "province.json");
        List<ProvinceVO> list = JsonUtil.jsonToArray(provinceJsonData, ProvinceVO.class);
        mProvinceList = new ArrayList<>();
        mProvinceList.addAll(list);
        mCityList = new ArrayList<>();
        mDistrictList = new ArrayList<>();
        for (ProvinceVO provinceVO : mProvinceList) {
            ArrayList<CityVO> cityList = provinceVO.children;
            if (ObjectUtil.isEmpty(cityList)) {
                cityList = new ArrayList<>();
            }
            mCityList.add(cityList);
            ArrayList<ArrayList<DistrictVO>> districtList = new ArrayList<>();
            for (CityVO cityVO : cityList) {
                districtList.add(cityVO.children);
            }
            mDistrictList.add(districtList);
        }

        //如果已经有选择的省市区，找到对应的position
        if (mProvinceVO != null && mCityVO != null && mDistrictVO != null) {
            for (int i = 0; i < mProvinceList.size(); i++) {
                if (StringUtils.equals(mProvinceList.get(i).code, mProvinceVO.code)) {
                    mProvincePosition = i;
                    break;
                }
            }
            for (int i = 0; i < mCityList.size(); i++) {
                if (StringUtils.equals(mCityList.get(mProvincePosition).get(i).code,
                        mCityVO.code)) {
                    mCityPosition = i;
                    break;
                }
            }
            for (int i = 0; i < mDistrictList.size(); i++) {
                if (StringUtils.equals(
                        mDistrictList.get(mProvincePosition).get(mCityPosition).get(i).code,
                        mDistrictVO.code)) {
                    mDistrictPosition = i;
                    break;
                }
            }
        }
    }

    @Override
    public void findView() {
        findActionBar();
        if (isEdit)
            actionBar.setTitle("修改收货地址");
        else
            actionBar.setTitle("新增收货地址");
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
    }
}
