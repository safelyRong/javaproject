package com.util;

import java.io.Serializable;

public class PayBean implements Serializable{
	

	public PayBean(String amount, String currency,
			String orderDate, String orderId, String period, String periodUnit,
			String productName, String reserved1, String reserved2,
			String userToken, String couFlag, String vchFlag, String cashFlag,
			String sap_id, String merchantId, String signKey) {
		super();
		this.amount = amount;
		this.currency = currency;
		this.orderDate = orderDate;
		this.orderId = orderId;
		this.period = period;
		this.periodUnit = periodUnit;
		this.productName = productName;
		this.reserved1 = reserved1;
		this.reserved2 = reserved2;
		this.userToken = userToken;
		this.couFlag = couFlag;
		this.vchFlag = vchFlag;
		this.cashFlag = cashFlag;
		this.sap_id = sap_id;
		this.merchantId = merchantId;
		this.signKey = signKey;
	}
	public PayBean() {
	}


	
	public String amount;
	public String currency;
	public String orderDate;
	public String orderId;
	public String period;
	public String periodUnit;
	public String productName;
	public String reserved1;
	public String reserved2;
	public String userToken;
	public String couFlag;
	public String vchFlag;
	public String cashFlag;
	
	public String sap_id;//门店号 
	public String merchantId;//商户编号
	public String signKey;//商户密钥
	
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public String getPeriodUnit() {
		return periodUnit;
	}
	public void setPeriodUnit(String periodUnit) {
		this.periodUnit = periodUnit;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getReserved1() {
		return reserved1;
	}
	public void setReserved1(String reserved1) {
		this.reserved1 = reserved1;
	}
	public String getReserved2() {
		return reserved2;
	}
	public void setReserved2(String reserved2) {
		this.reserved2 = reserved2;
	}
	public String getUserToken() {
		return userToken;
	}
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
	public String getCouFlag() {
		return couFlag;
	}
	public void setCouFlag(String couFlag) {
		this.couFlag = couFlag;
	}
	public String getVchFlag() {
		return vchFlag;
	}
	public void setVchFlag(String vchFlag) {
		this.vchFlag = vchFlag;
	}
	public String getCashFlag() {
		return cashFlag;
	}
	public void setCashFlag(String cashFlag) {
		this.cashFlag = cashFlag;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getSignKey() {
		return signKey;
	}

	public void setSignKey(String signKey) {
		this.signKey = signKey;
	}
}
