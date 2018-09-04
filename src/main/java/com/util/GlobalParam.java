package com.util;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Request;
public  class GlobalParam {
	
	//本地应用上下文地址
	public static String localAddr = "http://10.1.12.114/PayService/Cmpay";
	//编码格式
	public static String characterSet = "00"; //00--GBK;01--GB2312;02--UTF-8
	//页面通知地址
	//public static String callbackUrl = localAddr + "/callback.jsp";
	//后台通知地址
	public static String notifyUrl = localAddr + "/Notify";
	
	//获取用户的IP地址，作为防钓鱼的一种方法
	public static String GetClientIp(HttpHeaders headers,Request request){
		
		String clientIp = headers.getHeaderString("x-forwarded-for");//request.getHeader("x-forwarded-for");
		if ((clientIp == null) || (clientIp.length() == 0)
				|| ("unknown".equalsIgnoreCase(clientIp))) {
			clientIp = headers.getHeaderString("Proxy-Client-IP");
		}
		if ((clientIp == null) || (clientIp.length() == 0)
				|| ("unknown".equalsIgnoreCase(clientIp))) {
			clientIp =headers.getHeaderString("WL-Proxy-Client-IP");
		}
		if ((clientIp == null) || (clientIp.length() == 0)
				|| ("unknown".equalsIgnoreCase(clientIp))) {
			//clientIp = request.getRemoteAddr();
		}
		
		return clientIp;
	}
	//public String ipAddress = clientIp;
	//商户请求编号
	//public static String requestId = String.valueOf(System.currentTimeMillis());
	public static String signType = "MD5";
	public static String version = "2.0.0";
	//商户编号
	//public static String merchantId = "888009941110054";
	//商户密钥
	//public static String signKey = "ioN00rrcw00T5N0Xj00LlQuO9gi7Gz8EQ6SlQQ1ilFia2ibWL1MPbUxYSykgQHx5";
	public static String req_url = "https://ipos.10086.cn/cps/cmpayService";//生产环境
	
	//平台测试环境
	//public static String req_url = "http://211.138.236.210/cps/cmpayService";
	//public static String req_url = "http://211.138.236.210/ips/cmpayService";
	public static String req_url_cps = "http://ipos.10086.cn/cps/cmpayService";
	
	public static String orderdate="";//测试用
}
