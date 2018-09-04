package com.util;

public class NotifyBean  {
	public NotifyBean() {
		super();
	}
	public NotifyBean(String merchantId, String payNo, String returnCode,
			String message, String signType, String amount, String orderId,
			String cashAmt, String coupAmt, String vchAmt, String status,
			String reserved1, String reserved2, String orderDate) {
		super();
		this.merchantId = merchantId;
		this.payNo = payNo;
		this.returnCode = returnCode;
		this.message = message;
		this.signType = signType;
		this.amount = amount;
		this.orderId = orderId;
		this.cashAmt = cashAmt;
		this.coupAmt = coupAmt;
		this.vchAmt = vchAmt;
		this.status = status;
		this.reserved1 = reserved1;
		this.reserved2 = reserved2;
		this.orderDate = orderDate;
	}
	public String merchantId;
	public String payNo;
	public String returnCode;
	public String message;
	public String signType;

	public String amount;
	public String orderId;
	public String cashAmt;
	public String coupAmt;
	public String vchAmt;
	public String status;
	public String reserved1;
	public String reserved2;
	public String orderDate;
	
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getPayNo() {
		return payNo;
	}
	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getSignType() {
		return signType;
	}
	public void setSignType(String signType) {
		this.signType = signType;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getCashAmt() {
		return cashAmt;
	}
	public void setCashAmt(String cashAmt) {
		this.cashAmt = cashAmt;
	}
	public String getCoupAmt() {
		return coupAmt;
	}
	public void setCoupAmt(String coupAmt) {
		this.coupAmt = coupAmt;
	}
	public String getVchAmt() {
		return vchAmt;
	}
	public void setVchAmt(String vchAmt) {
		this.vchAmt = vchAmt;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	
	
}
