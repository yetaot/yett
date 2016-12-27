package framework.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import framework.exceptions.ExceptionHandler;
import framework.logs.LogUtil;

import play.db.DB;
import play.db.Model;
import play.db.jpa.JPA;

public class DBUtil {
	
	private final static  Logger logger = LogUtil.getInstance(DBUtil.class);
	
	/**
	 * 获取应用的JDBC连接
	 * @return
	 */
	public static Connection  getConnection(){
		return DB.getConnection();
	}
	
	
	public static ResultSet query(String sql){
		Connection conn = getConnection();
		Statement stmt;
		ResultSet rs=null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			ExceptionHandler.throwRuntimeException(e, "数据字典加载内存数据连接异常");
			logger.error("数据字典加载内存数据连接异常");
		}finally{
			try {
				if(conn!=null){
					conn.close();
					conn =null;
				}
			} catch (SQLException e) {
				logger.error("数据库连接关闭异常");
			}
		}
		return rs;
	}
	
	
	/**
	 * 重新设置数据库链接
	 * @param userName
	 * @param password
	 * @return
	 */
	public static Connection refreshConnection(String userName,String password){
		Connection connection=null;
		try {
			connection = DB.datasource.getConnection(userName, password);
			if(JPA.isEnabled()){
				Session session = 	((org.hibernate.ejb.EntityManagerImpl) JPA.em()).getSession();
				session.connection().setAutoCommit(false);
				session.connection().commit();
				session.connection().close();
				session.disconnect();	
				session.reconnect(connection);
			}else{
				//DB.close();
				//DB.setLocalConnection(connection);
			}
		} catch (SQLException e) {
			try {
				if(null!=connection) { connection.close();}
			} catch (SQLException e1) {
				logger.error(String.format("[%s]: connection close failed.", "refreshConnection::closeConnection"), e1);
			}
			logger.error(String.format("[%s]: create connection failed!", "refreshConneciton::resetConnection"),e);
		}
		return connection;
	}
}