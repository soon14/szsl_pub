package com.bsoft.hospital.pub.suzhoumh.model.my;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.app.tanklib.model.AbsBaseVoSerializ;
import com.app.tanklib.util.StringUtil;

/**
 * @author zkl
 */

public class UserDetailVo extends AbsBaseVoSerializ {
    private static final long serialVersionUID = 1L;

    public String mobile;
    public String idcard;
    public long birthdate = -1;
    public String realname;
    public int sexcode;
    public String addressstr;
    public String provinceid;
    public String districtid;
    public String cityid;
    public String streetid;
    public String streetstr;
    public String detailadr;
    public String nature;
    public String isvalidate;
    public ArrayList<MyCardVo> cards = new ArrayList<MyCardVo>();
    public ArrayList<MyContactVo> contacts = new ArrayList<MyContactVo>();

    @Override
    public void buideJson(JSONObject job) {
        try {
            if (!job.isNull("mobile")) {
                this.mobile = job.getString("mobile");
            }
            if (!job.isNull("sexcode")) {
                this.sexcode = job.getInt("sexcode");
            }
            if (!job.isNull("birthdate")) {
                if (!StringUtil.isEmpty(job.getString("birthdate"))) {
                    this.birthdate = job.getLong("birthdate");
                }
            }
            if (!job.isNull("realname")) {
                this.realname = job.getString("realname");
            }
            if (!job.isNull("idcard")) {
                this.idcard = job.getString("idcard");
            }
            if (!job.isNull("addressstr")) {
                this.addressstr = job.getString("addressstr");
            }
            if (!job.isNull("detailadr")) {
                this.detailadr = job.getString("detailadr");
            }
            if (!job.isNull("provinceid")) {
                this.provinceid = job.getString("provinceid");
            }
            if (!job.isNull("districtid")) {
                this.districtid = job.getString("districtid");
            }
            if (!job.isNull("cityid")) {
                this.cityid = job.getString("cityid");
            }
            if (!job.isNull("streetid")) {
                this.streetid = job.getString("streetid");
            }
            if (!job.isNull("streetstr")) {
                this.streetstr = job.getString("streetstr");
            }
            if (!job.isNull("cards")) {
                JSONArray cardarr = job.getJSONArray("cards");
                if (null != cardarr && cardarr.length() > 0) {
                    cards = new ArrayList<MyCardVo>();
                    for (int i = 0; i < cardarr.length(); i++) {
                        MyCardVo vo = new MyCardVo();
                        vo.buideJson(cardarr.getJSONObject(i));
                        cards.add(vo);
                    }
                }
            }
            if (!job.isNull("contacts")) {
                JSONArray contactarr = job.getJSONArray("contacts");
                if (null != contactarr && contactarr.length() > 0) {
                    contacts = new ArrayList<>();
                    for (int i = 0; i < contactarr.length(); i++) {
                        MyContactVo vo = new MyContactVo();
                        vo.buideJson(contactarr.getJSONObject(i));
                        contacts.add(vo);
                    }
                }
            }
            if (!job.isNull("nature")) {
                this.districtid = job.getString("nature");
            }
            if (!job.isNull("isvalidate")) {
                this.districtid = job.getString("isvalidate");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public JSONObject toJson() {
        return null;
    }

    // @Override
    // public boolean equals(Object obj) {
    // return this.showId == ((DynamicShow) obj).showId;
    // }

}
