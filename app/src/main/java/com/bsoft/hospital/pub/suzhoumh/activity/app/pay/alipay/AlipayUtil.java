package com.bsoft.hospital.pub.suzhoumh.activity.app.pay.alipay;


import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.model.pay.PayAccountVo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 支付宝支付工具类
 */
public class AlipayUtil {

	public static String getPayInfo(PayAccountVo payAccountVo) {
		String orderInfo = getOrderInfo(payAccountVo.subject, payAccountVo.body, payAccountVo.price, payAccountVo.tradeno,
				payAccountVo.pid, payAccountVo.accountname,payAccountVo.notifyUrl);
		String sign = sign(orderInfo, payAccountVo.privatekey);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();
		return payInfo;
	}

	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	public static String getOrderInfo(String subject, String body,
			String price, String orderNo, String alipay_partner,
			String alipay_seller,String notify_url) {
		// 合作者身份ID
		String orderInfo = "partner=" + "\"" + alipay_partner + "\"";

		// 卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + alipay_seller + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + orderNo + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + Constants.getHttpUrl()+notify_url.substring(1) + "\"";

		// 接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	public static String sign(String content, String alipay_rsa_private) {
		return SignUtils.sign(content, alipay_rsa_private);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	public static String getSignType() {
		return "sign_type=\"RSA\"";
	}
}
