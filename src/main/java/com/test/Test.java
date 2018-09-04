package com.test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.dao.BaseDao;
import com.dao.BranchDao;
import com.util.BranchBean;
import com.util.GlobalParam;

@Path("Test")
public class Test extends BaseDao{
	private static Logger logger = Logger.getLogger(Test.class);
	private DruidPooledConnection conn;
	
	@Path("/TestDb")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public JsonObject TestDb(){
	   javax.json.JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
		try {
            String sql ="select * from [dbo].[RemoveRecord] where id =6";
            conn = getConnection();
            Statement st  = conn.createStatement();
            ResultSet reset = st.executeQuery(sql);
            JsonObject jsonObject=null;
            if(reset.next()){
            	jsonObject = jsonObjectBuilder.add("Id",reset.getInt("Id"))
            			.add("orderid", reset.getString("Id")).build();
            }
            reset.close();
            st.close();
            conn.close();
            logger.info("test");
           return jsonObject;
        } catch (SQLException ex) {
        	return jsonObjectBuilder.add("errMsg","异常:"+ex.getCause()).build();
        }
	}
	@Path("/TestBranch")
	@GET	
	public JsonObject TestBranch(@QueryParam("sap_no") String sap_no ){
		BranchDao branchDao=new BranchDao();
		javax.json.JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
		try {
			BranchBean branch=branchDao.GetBranch(sap_no);
			if(branch.sapNo==null||"".equals(branch.sapNo)){
				JsonObject jsonObject=jsonObjectBuilder.add("message", "门店未找到").build();
				return jsonObject;
			}
			String l=sap_no.substring(0,1).toUpperCase();
			if(branch.branchSapNo==null||"".equals(branch.branchSapNo)){//门店未分配商户密钥
				List<BranchBean> branchList=branchDao.GetMerchantInfo();
				for(int i=0;i<branchList.size();i++){
					BranchBean branch1=branchList.get(i);
					if(branch1.isBranch==1&&!"".equals(branch.branchSapNo)&&branch1.branchSapNo.indexOf(l)>-1){	
						return jsonObjectBuilder.add("merchantId", branch1.merchantId).
								add("signkey",branch1.signKey).build();
					}
				}
			}
			else{
				return jsonObjectBuilder.add("merchantId", branch.merchantId).
						add("signkey",branch.signKey).build();
			}
		} catch (SQLException e) {		
			e.printStackTrace();
		}
		JsonObject jsonObject=jsonObjectBuilder.add("message", "门店未找到").build();
		return jsonObject;
	}
	@Path("/Reset")
	@GET
    @Produces( MediaType.APPLICATION_JSON)
    public JsonObject getIt(@QueryParam("isTest") String isTest,@QueryParam("orderdate") String orderdate,
    		@QueryParam("merchantId") String merchantId) {
	   javax.json.JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
	   switch(isTest){
		   case "yes":
			   GlobalParam.req_url="http://211.138.236.210/ips/cmpayService";
			   break;
		   case "no":
			   GlobalParam.req_url="http://211.138.236.210/cps/cmpayService";
			   break;
		   default:
			   GlobalParam.req_url="https://ipos.10086.cn/cps/cmpayService";//生产环境
			   break;
	   }
	   GlobalParam.orderdate=orderdate;
	   
	   if(merchantId!=null && merchantId!=""){
		 //  GlobalParam.merchantId=merchantId;
	   }
	   logger.error("请求test完毕！");
	   return  jsonObjectBuilder.add("req_url",GlobalParam.req_url).build();
	}
}
