package com.util;

public class BranchBean {
	public String loginId;
	public String merchantId;
	public String signKey;
	public String loginPwd;
	public String loginPayPwd;
	public String branName;
	public String sapNo;
	public int isBranch;//是否分部
	public String branchSapNo;
	public String getLoginId() {
		return loginId;
	}

	public String getBranchSapNo() {
		return branchSapNo;
	}
	public void setBranchSapNo(String branchSapNo) {
		this.branchSapNo = branchSapNo;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
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
	public String getLoginPwd() {
		return loginPwd;
	}
	public void setLoginPwd(String loginPwd) {
		this.loginPwd = loginPwd;
	}
	public String getLoginPayPwd() {
		return loginPayPwd;
	}
	public void setLoginPayPwd(String loginPayPwd) {
		this.loginPayPwd = loginPayPwd;
	}
	public String getBranName() {
		return branName;
	}
	public void setBranName(String branName) {
		this.branName = branName;
	}
	public String getSapNo() {
		return sapNo;
	}
	public void setSapNo(String sapNo) {
		this.sapNo = sapNo;
	}
	
	
}
