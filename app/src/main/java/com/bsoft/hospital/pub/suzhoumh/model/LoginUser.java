package com.bsoft.hospital.pub.suzhoumh.model;

import android.text.TextUtils;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.app.tanklib.model.AbsBaseVoSerializ;
import com.bsoft.hospital.pub.suzhoumh.model.my.MyCardVo;

/**
 * @author zkl
 */

public class LoginUser extends AbsBaseVoSerializ {
    private static final long serialVersionUID = 1L;

    public int cardtype;
    public String ecardcode;
    public String idcard;
    //接口返回的是id字段
    public String id;
    public String token;
    public String username;
    public long birthdate = -1;
    public String header;
    public String mobile;
    public String realname;
    public String latitude;//纬度
    public String longitude;//经度
    // 1男 2女
    public int sexcode;
    public String phrid = "";
    public String sn = "";
    public String nature;// 0自费 1苏州医保 2园区医保
    public String isvalidate;//0未签约 1已签约
    public ArrayList<MyCardVo> cards = new ArrayList<MyCardVo>();


    @Override
    public void buideJson(JSONObject job) {
        try {
            if (!job.isNull("phrid")) {
                this.phrid = job.getString("phrid");
            }
            if (!job.isNull("sn")) {
                this.sn = job.getString("sn");
            }
            if (!job.isNull("cardtype")) {
                this.cardtype = job.getInt("cardtype");
            }
            if (!job.isNull("id")) {
                this.id = job.getString("id");
            }
            if (!job.isNull("ecardcode")) {
                this.ecardcode = job.getString("ecardcode");
            }
            if (!job.isNull("token")) {
                this.token = job.getString("token");
            }
            if (!job.isNull("username")) {
                this.username = job.getString("username");
            }
            if (!job.isNull("birthdate")) {
                this.birthdate = job.getInt("birthdate");
            }
            if (!job.isNull("idcard")) {
                this.idcard = job.getString("idcard");
            }
            if (!job.isNull("header")) {
                this.header = job.getString("header");
            }
            if (!job.isNull("mobile")) {
                this.mobile = job.getString("mobile");
            }
            if (!job.isNull("realname")) {
                this.realname = job.getString("realname");
            }
            if (!job.isNull("sexcode")) {
                this.sexcode = job.getInt("sexcode");
            }
            if (!job.isNull("nature")) {
                this.nature = job.getString("nature");
            }
            if (!job.isNull("isvalidate")) {
                this.nature = job.getString("isvalidate");
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public MyCardVo getCardByType(String cardType) {
        MyCardVo myCardVo = new MyCardVo();
        if (cards != null) {
            for (int i = 0; i < cards.size(); i++) {
                if (String.valueOf(cards.get(i).cardType).equals(cardType)) {
                    myCardVo = cards.get(i);
                    break;
                }
            }
        }
        return myCardVo;
    }

    @Override
    public JSONObject toJson() {
        JSONObject ob = new JSONObject();
        try {
            ob.put("cardtype", cardtype);
            ob.put("ecardcode", ecardcode);
            ob.put("idcard", idcard);
            ob.put("token", token);
            ob.put("username", username);
            ob.put("birthdate", birthdate);
            ob.put("header", header);
            ob.put("mobile", mobile);
            ob.put("realname", realname);
            ob.put("sexcode", sexcode);
            ob.put("nature", nature);
            ob.put("isvalidate", nature);
            ob.put("phrid", phrid);
            ob.put("sn", sn);
            ob.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ob;
    }

    public void buideLocalJson(String json) {
        try {
            JSONObject ob = new JSONObject(json);
            if (!ob.isNull("cardtype")) {
                this.cardtype = ob.getInt("cardtype");
            }
            if (!ob.isNull("ecardcode")) {
                this.ecardcode = ob.getString("ecardcode");
            }
            if (!ob.isNull("idcard")) {
                this.idcard = ob.getString("idcard");
            }
            if (!ob.isNull("token")) {
                this.token = ob.getString("token");
            }
            if (!ob.isNull("username")) {
                this.username = ob.getString("username");
            }
            if (!ob.isNull("birthdate")) {
                this.birthdate = ob.getInt("birthdate");
            }
            if (!ob.isNull("header")) {
                this.header = ob.getString("header");
            }
            if (!ob.isNull("mobile")) {
                this.mobile = ob.getString("mobile");
            }
            if (!ob.isNull("realname")) {
                this.realname = ob.getString("realname");
            }
            if (!ob.isNull("sexcode")) {
                this.sexcode = ob.getInt("sexcode");
            }
            if (!ob.isNull("phrid")) {
                this.phrid = ob.getString("phrid");
            }
            if (!ob.isNull("sn")) {
                this.sn = ob.getString("sn");
            }
            if (!ob.isNull("id")) {
                this.id = ob.getString("id");
            }
            if (!ob.isNull("nature")) {
                this.nature = ob.getString("nature");
            }
            if (!ob.isNull("isvalidate")) {
                this.nature = ob.getString("isvalidate");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //判断医保 0自费 1苏州医保 2园区医保
    public boolean natureJudje() {

        return TextUtils.equals("1", nature);
    }

    // @Override
    // public boolean equals(Object obj) {
    // return this.showId == ((DynamicShow) obj).showId;
    // }

}
