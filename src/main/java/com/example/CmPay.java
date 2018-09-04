package com.example;

import java.net.URLDecoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;
import com.dao.BaseDao;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.hisun.iposm.HiiposmUtil;
import com.util.GlobalParam;
import com.util.NotifyBean;
import com.util.PayBean;

@Path("Cmpay")
public class CmPay{
	
	private static Logger logger = Logger.getLogger(CmPay.class);
	//冲正
	@Path("/CloudTxnCancel")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public JsonObject CloudTxnCancel(@QueryParam("sap_id") String sap_id,@QueryParam("merchantId") String merchantId,
			@QueryParam("signKey") String signKey,@QueryParam("orequestId") String orequestId,@QueryParam("oorderDate") String oorderDate){
		String type = "CloudTxnCancel";
		String requestId=String.valueOf(System.currentTimeMillis());//GlobalParam.requestId;
		String signType=GlobalParam.signType;
		String version=GlobalParam.version ;
		javax.json.JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
		try {
			
			//-- 签名
			String signData = GlobalParam.characterSet  + merchantId + requestId + signType + type+version+orequestId+oorderDate;
			HiiposmUtil util = new HiiposmUtil();
			String hmac = util.MD5Sign(signData,signKey);	
			
			//-- 请求报文
			String buf = "characterSet=" + GlobalParam.characterSet + "&merchantId="
					+ merchantId + "&requestId=" + requestId + "&signType="
					+ signType + "&type=" + type + "&version=" + version
					+ "&orequestId=" + orequestId + "&oorderDate=" + oorderDate;
		
			//-- 带上消息摘要
			buf = "hmac=" + hmac + "&" + buf;
			String res = util.sendAndRecv(GlobalParam.req_url, buf, GlobalParam.characterSet);
			String code = util.getValue(res, "returnCode");
			String message = URLDecoder.decode(util.getValue(res, "message"),"UTF-8");
			if (!code.equals("000000")) {
				logger.error("returnCode:"+code+",message:"+message+",sap_id:"+sap_id+",buf:"+buf);
				return jsonObjectBuilder.add("returnCode",code).add("message",message).build();
			}
			String hmac1 = util.getValue(res, "hmac");
			String vfsign = util.getValue(res, "merchantId")
					+ util.getValue(res, "requestId")
					+ util.getValue(res, "signType")
					+ util.getValue(res, "type")
                    + util.getValue(res, "version")
					+ util.getValue(res, "returnCode")
					+ message;
			 // -- 验证签名
			boolean flag = false;
			flag = util.MD5Verify(vfsign, hmac1, signKey);
			if (!flag) {
				logger.error("returnCode:"+code+",message:验签中心平台失败"+",sap_id:"+sap_id+",buf:"+buf+",vfsign:"+vfsign);
				return jsonObjectBuilder.add("returnCode",code).add("message","验签中心平台失败").build();
			}
	        JsonObject jsonObject = jsonObjectBuilder
	        		.add("requestId",util.getValue(res, "requestId"))
	        		.add("signType", util.getValue(res, "signType"))
	        		.add("type",util.getValue(res, "type"))
	        		.add("version", util.getValue(res, "version"))
	        		.add("returnCode",util.getValue(res, "returnCode"))
	        		.add("message", util.getValue(res, "message"))
	        		.build();
	       return jsonObject;
		}
		catch(Exception ex){
			logger.warn("message异常:"+ex.getCause()+",sap_id:"+sap_id+",orequestId:"+orequestId+",oorderDate:"+oorderDate);
			return jsonObjectBuilder.add("message","异常:"+ex.getCause()).build();
		}
	}
	//云支付
	@Path("/CloudQuickPay")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public JsonObject CloudPay(PayBean payBean){
		String type = "CloudQuickPay";
		String requestId=String.valueOf(System.currentTimeMillis());//GlobalParam.requestId;
		String signType=GlobalParam.signType;
		String version=GlobalParam.version ;
		
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	    //不在 接口中设置,由参数传入
	    payBean.orderId = payBean.orderId==null?sdf.format(new Date()):payBean.orderId;
	    payBean.orderDate = payBean.orderDate==null?payBean.orderId.substring(0,8):payBean.orderDate;
		javax.json.JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
		try {
			//-- 签名
			String signData = GlobalParam.characterSet  + GlobalParam.notifyUrl
					 + payBean.merchantId + requestId + signType + type
					+ version + payBean.amount  + payBean.currency
					+ payBean.orderDate + payBean.orderId  + payBean.period + payBean.periodUnit + URLDecoder.decode(payBean.productName,"UTF-8") 
					+ URLDecoder.decode(payBean.reserved1,"UTF-8") + URLDecoder.decode(payBean.reserved2,"UTF-8") + payBean.userToken 
					 + payBean.couFlag + payBean.vchFlag + payBean.cashFlag;
			HiiposmUtil util = new HiiposmUtil();
			String hmac = util.MD5Sign(signData, payBean.signKey);
			//-- 请求报文
			String buf = "characterSet=" + GlobalParam.characterSet 
				    + "&notifyUrl=" + GlobalParam.notifyUrl +  "&merchantId="
					+ payBean.merchantId + "&requestId=" + requestId + "&signType="
					+ signType + "&type=" + type + "&version=" + version
					+ "&amount=" + payBean.amount 
					+ "&currency=" + payBean.currency + "&orderDate=" + payBean.orderDate
					+ "&orderId=" + payBean.orderId +  "&period=" + payBean.period
					+ "&periodUnit=" + payBean.periodUnit 
					+ "&productName=" + payBean.productName  
					+ "&reserved1=" + payBean.reserved1
					+ "&reserved2=" + payBean.reserved2 + "&userToken=" + payBean.userToken 
					+ "&couFlag=" + payBean.couFlag+ "&vchFlag=" + payBean.vchFlag+ "&cashFlag=" + payBean.cashFlag;
			//-- 带上消息摘要
			buf = "hmac=" + hmac + "&" + buf;
			String res = util.sendAndRecv(GlobalParam.req_url, buf, GlobalParam.characterSet);
			String code = util.getValue(res, "returnCode");
			String message = URLDecoder.decode(util.getValue(res, "message"),"UTF-8");
			if (!code.equals("000000")) {
				logger.error("returnCode:"+code+",message:"+message+",sap_id:"+payBean.sap_id+",buf:"+buf);
				return jsonObjectBuilder.add("returnCode",code).add("message",message).build();
			}
			String hmac1 = util.getValue(res, "hmac");
			String vfsign = util.getValue(res, "merchantId")
					+ util.getValue(res, "requestId")
					+ util.getValue(res, "signType")
					+ util.getValue(res, "type")
                    + util.getValue(res, "version")
					+ util.getValue(res, "returnCode")
					+ message
					+ util.getValue(res, "orderId")
					+ util.getValue(res, "amount")
			        + util.getValue(res, "coupAmt")
					+ util.getValue(res, "vchAmt")
					+ util.getValue(res, "cashAmt")
					+ util.getValue(res, "reserved1")
					+ util.getValue(res, "reserved2");
			 // -- 验证签名
			boolean flag = false;
			flag = util.MD5Verify(vfsign, hmac1, payBean.signKey);
			if (!flag) {
				logger.error("returnCode:"+code+",message:验签中心平台失败"+",sap_id:"+payBean.sap_id+",buf:"+buf+",vfsign:"+vfsign);
				return jsonObjectBuilder.add("returnCode",code).add("message","验签中心平台失败").build();
			}
	        JsonObject jsonObject = jsonObjectBuilder
	        		.add("requestId",util.getValue(res, "requestId"))
	        		.add("signType", util.getValue(res, "signType"))
	        		.add("returnCode",util.getValue(res, "returnCode"))
	        		.add("message", util.getValue(res, "message"))
	        		.add("orderId",util.getValue(res, "orderId"))
	        		.add("amount", util.getValue(res, "amount"))
	        		.add("coupAmt",util.getValue(res, "coupAmt"))
	        		.add("vchAmt", util.getValue(res, "vchAmt"))
	        		.add("cashAmt", util.getValue(res, "cashAmt"))
	        		.add("reserved1",util.getValue(res, "reserved1"))
	        		.add("reserved2", util.getValue(res, "reserved2"))
	        		.build();
	       return jsonObject;
		}catch(Exception ex){
			logger.warn("message异常:"+ex.getCause()+",amount:"+payBean.amount+",userToken:"+payBean.userToken+",sap_id:"+payBean.sap_id);
			return jsonObjectBuilder.add("message","异常:"+ex.getCause()).build();
		}
	}
    //订单结果轮询
	@Path("/QueryByYpos")
	@GET
	@Produces( MediaType.APPLICATION_JSON)
	public JsonObject OrderQueryByYPos(@QueryParam("sap_id") String sap_id,@QueryParam("merchantId") String merchantId,
			@QueryParam("signKey") String signKey,@QueryParam("orderId") String orderId,@QueryParam("orderDate") String orderDate){
		String type = "OrderQueryByYPOS";
		String requestId=String.valueOf(System.currentTimeMillis());
		String signType=GlobalParam.signType;
		String version=GlobalParam.version ;
		javax.json.JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
		try {
			//-- 签名
			String signData = GlobalParam.characterSet  + merchantId + requestId + signType + type+version+orderDate+orderId;
			HiiposmUtil util = new HiiposmUtil();
			String hmac = util.MD5Sign(signData,signKey);	
			
			//-- 请求报文
			String buf = "characterSet=" + GlobalParam.characterSet 
				    +"&merchantId="+ merchantId + "&requestId=" + requestId + "&signType="
					+ signType + "&type=" + type + "&version=" + version
					 + "&orderDate=" + orderDate+ "&orderId=" + orderId;
			
			//-- 带上消息摘要
			buf = "hmac=" + hmac + "&" + buf;
			String res = util.sendAndRecv(GlobalParam.req_url, buf, GlobalParam.characterSet);
			
			String code = util.getValue(res, "returnCode");
			String message = URLDecoder.decode(util.getValue(res, "message"),"UTF-8");
			if (!code.equals("000000")) {
				logger.error("returnCode:"+code+",message:"+message+",sap_id:"+sap_id+",buf:"+buf);
				return jsonObjectBuilder.add("returnCode",code).add("message",message).build();
			}
			String hmac1 = util.getValue(res, "hmac");
			String vfsign = util.getValue(res, "merchantId")
					+ util.getValue(res, "requestId")
					+ util.getValue(res, "signType")
					+ util.getValue(res, "type")
                    + util.getValue(res, "version")
					+ util.getValue(res, "returnCode")
					+ URLDecoder.decode(util.getValue(res, "message"),"UTF-8")
					+ util.getValue(res, "orderId")
					+ util.getValue(res, "merchantName")
					+ util.getValue(res, "mblNo")
					+ util.getValue(res, "ordSts")
					+ util.getValue(res, "payDate")
					+ util.getValue(res, "amount")
					+ util.getValue(res, "cashAmount")
					+ util.getValue(res, "bonAmount")
					+ util.getValue(res, "splAmount")
					+ util.getValue(res, "vchAmount")
					+ util.getValue(res, "merOprNo")
					+ util.getValue(res, "storeId")
					+ util.getValue(res, "posId");
			 // -- 验证签名
			boolean flag = false;
			flag = util.MD5Verify(vfsign, hmac1, signKey);
			if (!flag) {
				logger.error("returnCode:"+code+",message:验签中心平台失败"+",sap_id:"+sap_id+",buf:"+buf+",vfsign:"+vfsign);
				return jsonObjectBuilder.add("returnCode",code).add("message","验签中心平台失败").build();
			}
	        JsonObject jsonObject = jsonObjectBuilder
	        		.add("requestId",util.getValue(res, "requestId"))
	        		.add("signType", util.getValue(res, "signType"))
	        		.add("type",util.getValue(res, "type"))
	        		.add("version", util.getValue(res, "version"))
	        		.add("returnCode",util.getValue(res, "returnCode"))
	        		.add("message", util.getValue(res, "message"))
	        		.add("orderId", util.getValue(res, "orderId"))
	        		.add("merchantName", util.getValue(res, "merchantName"))
	        		.add("mblNo", util.getValue(res, "mblNo"))
	        		.add("ordSts", util.getValue(res, "ordSts"))
	        		.add("payDate", util.getValue(res, "payDate"))
	        		.add("amount", util.getValue(res, "amount"))
	        		.add("cashAmount", util.getValue(res, "cashAmount"))
	        		.add("bonAmount", util.getValue(res, "bonAmount"))
	        		.add("splAmount", util.getValue(res, "splAmount"))
	        		.add("vchAmount", util.getValue(res, "vchAmount"))
	        		.add("merOprNo", util.getValue(res, "merOprNo"))
	        		.add("storeId", util.getValue(res, "storeId"))
	        		.add("posId", util.getValue(res, "posId"))
	        		.build();
	       return jsonObject;
		}
		catch(Exception ex){
			logger.warn("message异常:"+ex.getCause()+",sap_id:"+sap_id+",orderId:"+orderId+",orderDate:"+orderDate);
			return jsonObjectBuilder.add("message","异常:"+ex.getCause()).build();
		}
	}
	//订单查询
	@Path("/Search")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public JsonObject OrderSearch(@QueryParam("sap_id") String sap_id,@QueryParam("merchantId") String merchantId,
			@QueryParam("signKey") String signKey,@QueryParam("orderId") String orderId) {
        String type = "OrderQueryCPS";
	    String requestId=String.valueOf(System.currentTimeMillis());//GlobalParam.requestId;
	    String signType=GlobalParam.signType;
	    String version=GlobalParam.version ;
	    javax.json.JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
		try {
			//-- 签名
			String signData = merchantId + requestId + signType + type
					+ version + orderId;
			HiiposmUtil util = new HiiposmUtil();
			String hmac = util.MD5Sign(signData,signKey);
			//-- 请求报文
			String buf = "merchantId=" + merchantId + "&requestId="
					+ requestId + "&signType=" + signType + "&type=" + type
					+ "&version=" +version + "&orderId=" + orderId;
			buf = "hmac=" + hmac + "&" + buf;
			//发起http请求，并获取响应报文
			String res = util.sendAndRecv(GlobalParam.req_url, buf, GlobalParam.characterSet);
			String message = URLDecoder.decode(util.getValue(res, "message"),"UTF-8");
			String code = util.getValue(res, "returnCode");
			if (!code.equals("000000")) {
				logger.error("returnCode:"+code+",message:"+message+",sap_id:"+sap_id+",buf:"+buf);
				return jsonObjectBuilder.add("returnCode",code).add("message",message).build();
			}
			//获取手机支付平台返回的签名消息摘要，用来验签
			String hmac1 = util.getValue(res, "hmac");
				
			String vfsign = util.getValue(res, "merchantId")
					+ util.getValue(res, "payNo")
					+ util.getValue(res, "returnCode")
					+ URLDecoder.decode(util.getValue(res, "message"),"UTF-8")
					+ util.getValue(res, "signType")
					+ util.getValue(res, "type")
					+ util.getValue(res, "version")
					+ util.getValue(res, "amount")
					+ util.getValue(res, "amtItem")
					+ util.getValue(res, "bankAbbr")
					+ util.getValue(res, "mobile")
					+ util.getValue(res, "orderId")
					+ util.getValue(res, "payDate")
					+ URLDecoder.decode(util.getValue(res, "reserved1"),
							"UTF-8")
					+ URLDecoder.decode(util.getValue(res, "reserved2"),
							"UTF-8") + util.getValue(res, "status")
					+ util.getValue(res, "payType")
					+ util.getValue(res, "orderDate")
					+ util.getValue(res, "fee");
			
			
			boolean flag = false;
			// -- 验证签名
			flag = util.MD5Verify(vfsign, hmac1,  signKey);
			if (!flag) {
				logger.error("returnCode:"+code+",message:"+message+",sap_id:"+sap_id+",buf:"+buf);
				return jsonObjectBuilder.add("returnCode",code).add("message","验签中心平台失败").build();
			}
			 JsonObject jsonObject = jsonObjectBuilder
		        		.add("payNo",util.getValue(res, "payNo"))
		        		.add("signType", util.getValue(res, "signType"))
		        		.add("returnCode",util.getValue(res, "returnCode"))
		        		.add("message", util.getValue(res, "message"))
		        		.add("orderId",util.getValue(res, "orderId"))
		        		.add("amount", util.getValue(res, "amount"))
		        		.add("amtItem",util.getValue(res, "amtItem"))
		        		.add("bankAbbr", util.getValue(res, "bankAbbr"))
		        		.add("mobile", util.getValue(res, "mobile"))
		        		.add("payDate",util.getValue(res, "payDate"))	
		        		.add("reserved1",util.getValue(res, "reserved1"))
		        		.add("reserved2", util.getValue(res, "reserved2"))
		        		.add("status", util.getValue(res, "status"))
		        		.add("payType",util.getValue(res, "payType"))
		        		.add("orderDate", util.getValue(res, "orderDate"))
		        		.add("fee",util.getValue(res, "fee"))
		        		.build();
		    return jsonObject;
		}catch(Exception ex)
		{
			logger.warn("message异常:"+ex.getCause()+",orderId:"+orderId+",sap_id:"+sap_id);
			return jsonObjectBuilder.add("message","异常:"+ex.getCause()).build();
		}
	}
	//退款
	@Path("/Refund")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public JsonObject OrderRefund(@QueryParam("sap_id") String sap_id,@QueryParam("merchantId") String merchantId,@QueryParam("signKey") String signKey,@QueryParam("orderId") String orderId,@QueryParam("amount") long amount) {

	    String type = "OrderRefundCPS"; 
	    String requestId=String.valueOf(System.currentTimeMillis());//GlobalParam.requestId;
	    String signType=GlobalParam.signType;
	    String version=GlobalParam.version ;
	    javax.json.JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
		try 
		{
			HiiposmUtil util = new HiiposmUtil();
			//-- 签名
			String signData = merchantId + requestId +signType
					+ type + version + orderId + amount;
			String hmac = util.MD5Sign(signData, signKey);			
			//-- 请求报文
			String buf = "merchantId=" +merchantId + "&requestId=" + requestId
			           + "&signType=" + signType + "&type=" + type
			           + "&version=" + version + "&orderId=" + orderId
			           + "&amount=" + amount;
			buf = "hmac=" + hmac + "&" + buf;			
			//发起http请求，并获取响应报文
			String res = util.sendAndRecv(GlobalParam.req_url, buf, GlobalParam.characterSet);
			String message=URLDecoder.decode(util.getValue(res, "message"),"UTF-8");
			//手机支付返回报文的消息摘要，用于商户验签
			String hmac1 = util.getValue(res, "hmac");
			String vfsign = util.getValue(res, "merchantId")
			      + util.getValue(res, "payNo")
			      + util.getValue(res, "returnCode")
					+ message
					+ util.getValue(res, "signType")
					+ util.getValue(res, "type")
					+ util.getValue(res, "version")
					+ util.getValue(res, "amount")
					+ util.getValue(res, "orderId")
					+ util.getValue(res, "status");
			//获取返回码
			String code = util.getValue(res, "returnCode");
			if (!code.equals("000000")) {
				logger.error("returnCode:"+code+",message:"+message+",sap_id:"+sap_id+",buf:"+buf);
				return jsonObjectBuilder.add("returnCode",code).add("message",message).build();
			}
			// -- 验证签名
			boolean flag = false;
			flag = util.MD5Verify(vfsign, hmac1, signKey);
			if (!flag) {
				logger.error("returnCode:"+code+",message:验签中心平台失败"+",sap_id:"+sap_id+",buf:"+buf);
				return jsonObjectBuilder.add("returnCode",code).add("message","验签中心平台失败").build();
			}
			 JsonObject jsonObject = jsonObjectBuilder
		        		.add("payNo",util.getValue(res, "payNo"))
		        		.add("returnCode",util.getValue(res, "returnCode"))
		        		.add("message", util.getValue(res, "message"))
		        		.add("signType", util.getValue(res, "signType"))
		        		.add("type",util.getValue(res, "type"))
		        		.add("version", util.getValue(res, "version"))
		        		.add("amount",util.getValue(res, "amount"))
		        		.add("orderid", util.getValue(res, "orderid"))
		        		.add("status", util.getValue(res, "status"))
		        		.build();
		    return jsonObject;
		
		}catch(Exception ex){
			logger.warn("message异常:"+ex.getCause()+",orderId:"+orderId+",sap_id:"+sap_id);
			return jsonObjectBuilder.add("message","异常:"+ex.getMessage()).build();
		}
		
	}
	//后台通知//订单支付成功后，调用此方法
	@Path("/Notify")
	@POST
	//@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public String NotifyMsg(NotifyBean notifyBean){
		//订单处理结果
		StringBuilder sb=new StringBuilder();
		sb.append("merchantId=").append(notifyBean.merchantId);
		sb.append("&payNo=").append(notifyBean.payNo);
		sb.append("&returnCode=").append(notifyBean.returnCode);
		sb.append("&message=").append(notifyBean.message);
		sb.append("&amount=").append(notifyBean.amount);
		sb.append("&orderId=").append(notifyBean.orderId);
		sb.append("&cashAmt=").append(notifyBean.cashAmt);
		sb.append("&coupAmt=").append(notifyBean.coupAmt);
		sb.append("&vchAmt=").append(notifyBean.vchAmt);
		sb.append("&status=").append(notifyBean.status);
		sb.append("&reserved1=").append(notifyBean.reserved1);
		sb.append("&reserved2=").append(notifyBean.reserved2);
		sb.append("&orderDate=").append(notifyBean.reserved2);
		logger.info("notify:"+sb.toString());
		return "{\"resulte:\""+"\"SUCCESS\"}";
	}
}
