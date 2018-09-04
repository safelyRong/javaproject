package com.main;

import javax.json.JsonObject;

public interface ICmPayInterface {
	//云支付条码支付
	//订单金额,币种00-cny现金 01-cmy冲值,订单提交日期(格式：年年年年月月日日),订单号,有效期数量,有效期单位00-分,商品名陈,保留字段1,保留字段2,用户扫描码(18位条码),
	//是否支持电子券,是否支持代金券,是否支持帐户现金 0-支持，1-不支持
	public JsonObject CloudPay(String amount,String currency,String orderDate
			,String orderId,String period,String periodUnit,String productName,String reserved1
			,String reserved2,String userToken,String couFlag,String vchFlag,String cashFlag);
	//订单查询
	//订单号
	public JsonObject OrderSearch(String orderId);
	//订单退款
	//订单号,订单金额
	public JsonObject OrderRefund(String orderId,long amount);													
}
	