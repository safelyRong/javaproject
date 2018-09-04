package com.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.druid.pool.DruidPooledConnection;

public class  BaseDao {
	 private static Logger logger = Logger.getLogger(BaseDao.class);
	    private static BaseDao databasePool = null;
	    private static DruidDataSource dds = null;

	    protected static Connection connect = null;
	    protected static Connection connect2 = null;

	    static {
	        Properties properties = loadPropertyFile("classes/db_server.properties");
	        try {
	            dds = (DruidDataSource) DruidDataSourceFactory.createDataSource(properties);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    public BaseDao() {}

	    public static synchronized BaseDao getInstance() {
	        if (null == databasePool) {
	            databasePool = new BaseDao();
	        }
	        return databasePool;
	    }

	    public DruidPooledConnection getConnection() throws SQLException {
	        return dds.getConnection();
	    }

	    public static Properties loadPropertyFile(String fullFile) {
	        String webRootPath = null;
	        if (null == fullFile || fullFile.equals("")){
	            throw new IllegalArgumentException("Properties file path can not be null : " + fullFile);
	        }
	        webRootPath = BaseDao.class.getClassLoader().getResource("").getPath();
	        webRootPath = new File(webRootPath).getParent();
	        InputStream inputStream = null;
	        Properties p = null;
	        try {
	            inputStream = new FileInputStream(new File(webRootPath+ File.separator + fullFile));
	            p = new Properties();
	            p.load(inputStream);
	        } catch (FileNotFoundException e) {
	            logger.error(e);
	            throw new IllegalArgumentException("Properties file not found: "+ fullFile);
	        } catch (IOException e) {
	            logger.error(e);
	            throw new IllegalArgumentException("Properties file can not be loading: " + fullFile);
	        } finally {
	            try {
	                if (inputStream != null)
	                    inputStream.close();
	            } catch (IOException e) {
	                logger.error(e);
	                e.printStackTrace();
	            }
	        }
	        return p;
	    }

//	    public static DataSource createDataSource(Properties properties) throws Exception{
//	        return createDataSource(properties);
//	    }

	    public void commit(){
	        try {
	            getConnection().commit();
	        } catch (SQLException e) {
	            rollback();
	            logger.error(e);
	        }
	    }

	    public void rollback(){
	        try {
	            getConnection().rollback();
	        } catch (SQLException e) {
	            logger.error(e);
	        }
	    }

	    public void close(){
	        try {
	            getConnection().close();
	        } catch (SQLException e) {
	            logger.error(e);
	        }
	    }

	     /**
	     * 鏇村叿pst鑾峰彇鏈�悗鎿嶄綔鑷ID
	     * @param pst 褰撳墠鎿嶄綔 PreparedStatement
	     * @return id鍙�
	     */
	    protected Long getIDENTITY(PreparedStatement pst){
	        try{
	            ResultSet reset = pst.executeQuery("select @@IDENTITY");
	            Long rindex = -1l;
	            if(reset.next()){
	                rindex = reset.getLong(1);
	            }
	            reset.close();
	            return rindex;
	        }catch(Exception e){
	            logger.error(e);
	            return -1l;
	        }
	    }

	    protected void close(Connection conn, Statement st, ResultSet rs){
	        if(rs!=null){
	            try {
	                rs.close();
	            } catch (SQLException e) {
	                logger.error(e);
	            }
	        }
	        if(st!=null){
	            try {
	                st.close();
	            } catch (SQLException e) {
	                logger.error(e);
	            }
	        }
	    }
}
