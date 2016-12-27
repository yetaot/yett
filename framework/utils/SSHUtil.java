package framework.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.log4j.Logger;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import framework.logs.LogUtil;

public class SSHUtil {
private final static Logger logger = LogUtil.getInstance(SSHUtil.class);
	
	public static int execCmd(String host, String username, String password,
		      String cmd, String finishFlag) throws IOException {
		    if (logger.isDebugEnabled()) {
		      logger.debug("EXEC SSH COMMAND [" + cmd + "]");
		    }	 
		    Connection conn = getOpenedConnection(host, username, password);
		    Session sess = conn.openSession();
		    sess.execCommand(cmd);
			InputStream stdout = new StreamGobbler(sess.getStdout());
			BufferedReader reader = new BufferedReader(new InputStreamReader(stdout));		 
		    try {
				while (true) {
				  String line = reader.readLine();
				  if (logger.isDebugEnabled()) {
					    logger.debug(line);
					  }
				  if (line == null || finishFlag.equals(line))
				    break;
				  
				}
			} catch (Exception e) {
				throw new IOException("EXEC [" + cmd + "] ERROR!");
			} finally {
				try {if (reader != null) reader.close();} catch(Exception e) {};
			    try {if (sess != null) sess.close();} catch(Exception e) {};
			    try {if (conn != null) conn.close();} catch(Exception e) {};			
			}
		    //return sess.getExitStatus().intValue();			
			return 0;
	}	
	 
	 public static void execCmd(Connection conn, String cmd, String finishFlag) throws IOException {
		    if (logger.isDebugEnabled()) {
		      logger.debug("EXEC [" + cmd + "] ERROR!");
		    }	 
		    Session sess = conn.openSession();
		    sess.execCommand(cmd);
			InputStream stdout = new StreamGobbler(sess.getStdout());
			BufferedReader reader = new BufferedReader(new InputStreamReader(stdout));		 
		    try {
				while (true) {
				  String line = reader.readLine();
				  if (logger.isDebugEnabled()) {
					    logger.debug(line);
					  }
				  if (line == null || finishFlag.equals(line))
				    break;			  
				}
			} catch (Exception e) {
				throw new IOException("EXEC [" + cmd + "] ERROR!");
			} finally {
				logger.debug("CLOSE SSH SESSION");
				try {if (reader != null) reader.close();} catch(Exception e) {};
			    try {if (sess != null) sess.close();} catch(Exception e) {};
			}	
	}		 
	
	 public static Connection getOpenedConnection(String host, String username,
			String password) throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("LOGIN INFO: [" + host + "] UserName [" + username + "],Password: [" + password + "]");
		}

		Connection conn = new Connection(host);
		conn.connect();
		boolean isAuthenticated = conn.authenticateWithPassword(username,
				password);
		if (isAuthenticated == false)
			throw new IOException("ssh login authenicated failed!");
		return conn;
	}	
	
	 public static Connection getOpenedConnection(String host,int port, String username,
				String password) throws IOException {
			if (logger.isDebugEnabled()) {
				logger.debug("LOGIN INFO: [" + host + "] UserName [" + username + "],Password: [" + password + "]");
			}

			Connection conn = new Connection(host,port);
			conn.connect();
			boolean isAuthenticated = conn.authenticateWithPassword(username,
					password);
			if (isAuthenticated == false)
				throw new IOException("ssh login authenicated failed!");
			return conn;
		}
	 
	 
	 
	 public static void closeConnections(Connection connction, Session session) throws IOException {
			logger.debug("CLOSE SSH CONNECTION");
		    try {if (session != null) session.close();} catch(Exception e) {};
		    try {if (connction != null) connction.close();} catch(Exception e) {};			 
	}	
}
