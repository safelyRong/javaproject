package com.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


import com.alibaba.druid.pool.DruidPooledConnection;
import com.util.BranchBean;

public class BranchDao extends BaseDao{
	
	private DruidPooledConnection conn;	
	public BranchBean GetBranch(String sapNo) throws SQLException{
		String sql="select b.sap_no as sapNo,mc.loginid,mc.mechantid,mc.signkey,mc.loginpwd,mc.paypwd,mc.name,mc.sapno branchSapNo from nst_branch b "+
" left join nst_bran_mechantinfo mc on b.sap_no=mc.sapno"+
" where b.deleted=0 and b.sap_no ='"+sapNo+"'";
		 BranchBean branch=null;
		 try {
            conn = getConnection();
            Statement st  = conn.createStatement();
            ResultSet reset = st.executeQuery(sql);
            if(reset.next()){
            	branch=new BranchBean();
            	branch.loginId=reset.getString("loginId");
            	branch.merchantId=reset.getString("mechantid");
            	branch.signKey=reset.getString("signKey");
            	branch.loginPwd=reset.getString("loginPwd");
            	branch.loginPayPwd=reset.getString("paypwd");
            	branch.branName=reset.getString("name");
            	branch.sapNo=reset.getString("sapNo");
            	branch.branchSapNo=reset.getString("branchSapNo");
            }
            reset.close();
            st.close();
            conn.close();
        } catch (SQLException ex) {
        	throw new SQLException(ex.getCause().toString());
        }
		return branch;
	}
	public List<BranchBean> GetMerchantInfo() throws SQLException{
	
	    List<BranchBean> branchList=new ArrayList<BranchBean>();
		String sql="select * from nst_bran_mechantinfo";
		try{	
			conn = getConnection();
	        Statement st  = conn.createStatement();
	        ResultSet reset = st.executeQuery(sql);
	        
	        while(reset.next()){
	        	BranchBean branch=new BranchBean();
	        	branch.loginId=reset.getString("loginId");
            	branch.merchantId=reset.getString("mechantid");
            	branch.signKey=reset.getString("signKey");
            	branch.loginPwd=reset.getString("loginPwd");
            	branch.loginPayPwd=reset.getString("paypwd");
            	branch.branName=reset.getString("name");
            	branch.branchSapNo=reset.getString("sapno");
            	branch.isBranch=reset.getInt("isBranch");
	        	branchList.add(branch);
	        }
	        reset.close();
	        st.close();
	        conn.close();	
        }catch (SQLException ex) {
		   throw new SQLException(ex.getCause().toString());
		}
		return branchList;
	}
}
