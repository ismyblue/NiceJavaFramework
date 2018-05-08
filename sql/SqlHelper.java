package com.ismyblue.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Properties;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class SqlHelper {

	//定义读取配置文件所需变量
	private static Properties properties = null;
	private static InputStream inputStream = null;

	//定义连接数据库有关参数
	private static String driver = null;
	private static String url = null;
	private static String user = null;
	private static String password = null;
	private static int initialSize = 0;
	
	
	//定义连接所需变量
	//private static Connection connection = null;
	private static ConnectionPool connectionPool = null;
	//private static PreparedStatement preparedStatement = null;
	//private static Statement statement = null;
	//private static CallableStatement callableStatement = null;
	//private static ResultSet resultSet = null;
	
	
		
	/**
	 * 加载驱动
	 * @throws IOException 
	 */
	static {
		properties = new Properties();		
		try {
			inputStream = new FileInputStream("src/com/ismyblue/util/dbinfo.properties");
			
			try {
				properties.load(inputStream);				
			} catch (IOException e) {
				e.printStackTrace();
			}
			driver = properties.getProperty("driver");
			url = properties.getProperty("url");
			user = properties.getProperty("user");
			password = properties.getProperty("password");
			initialSize = Integer.parseInt(properties.getProperty("initialSize"));
					
			try {
				Class.forName(driver);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}		
			
			connectionPool = new ConnectionPool();				
			
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
		}finally {
			try {inputStream.close();} catch (IOException e) {e.printStackTrace();}
			inputStream = null;
		}
	}
	
	
	public static void listDbinfo() {
		System.out.printf("driver:%s\nurl:%s\nuser:%s\npassword:%s\n", driver, url, user, password);
	}
	
	/**
	 * 从资源池内获得一个Connection
	 * @return Connection
	 */
	public static Connection getConnection() {
		return connectionPool.getConnectionFromPool();
	}
	
	/**
	 * 关闭所有连接
	 * @param connection
	 * @param statement
	 * @param resultSet
	 */
	public static void closeAll(Connection connection, Statement statement, ResultSet resultSet) throws SQLException {
		if(resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}			
			resultSet = null;
		}
		if(statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			statement = null;
		}
		if(connection != null) {
			connectionPool.Release(connection);
			//connection.close();
			
			connection = null;
		}
	}
	
	/**
	 * 把PreparedStatement里的sql语句占位符全部设置完毕
	 * @param preparedStatement
	 * @param params
	 * @return PreparedStatement 
	 */
	private static void setPreparedStatementAllParams(PreparedStatement preparedStatement,String[] params) {
		if(params == null || (params.length == 1 && params[0] == null))
			return ;
		int col = params.length;
		try {
			for(int i = 0 ;i < col;i++) {
				try {
					preparedStatement.setDate(i+1, java.sql.Date.valueOf(params[i]));
				}catch (Exception e) {
					try{
						preparedStatement.setDouble(i+1, Double.parseDouble(params[i]));
					}catch (Exception e1) {		
						
						try {
							preparedStatement.setInt(i+1, Integer.parseInt(params[i]));
						}catch (Exception e2) {
							try {
								preparedStatement.setString(i+1, params[i]);
							}catch (Exception e3) {
								System.out.println("SqlHelper-setAllParems-Error1:");
								e3.printStackTrace();
							}
						}
					}
				}
			}
		}catch (Exception e) {
			System.out.println("SqlHelper-setAllParems-Error2:");
			e.printStackTrace();
		}
	}
	
	/**
	 * update操作，insert, update, delete
	 * @param sql 
	 * @param params 多个参数
	 * @return
	 */
	public static boolean executeNonQuery(String sql, String[] params) {		
		Connection theConn = SqlHelper.getConnection();					
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = theConn.prepareStatement(sql);
			setPreparedStatementAllParams(preparedStatement, params);
			//System.out.println(preparedStatement);
			return preparedStatement.executeUpdate()>0?true:false;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				closeAll(theConn, preparedStatement, null);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
		return false;
	}

	/**
	 * update操作，insert, update, delete
	 * @param sql 
	 * @param param 单个参数
	 * @return
	 */	
	public static boolean executeNonQuery(String sql, String param) {
		String[] params = {param};
		return SqlHelper.executeNonQuery(sql, params);
	}


	/**
	 * 执行多条带多参数的sql语句， insert, update, delete，考虑事务，全部操作成功则提交，若有步骤失败则回滚
	 * @param sql语句一维数组，
	 * @param params二维数组
	 * @return
	 */
	@SuppressWarnings("resource")
	public static boolean executeNonQuery(String[] sqls, String[][] paramss) {
		int sqlNum = sqls.length;
		int paramsNum = paramss.length;		
		if(sqlNum != paramsNum) {
			System.out.println("sql语句和参数数量不一致！");
			return false;
		}
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = SqlHelper.getConnection();
			connection.setAutoCommit(false);
			for(int i = 0;i < sqlNum;i++) {				
				preparedStatement = connection.prepareStatement(sqls[i]);
				setPreparedStatementAllParams(preparedStatement, paramss[i]);
				if(preparedStatement.executeUpdate()<0)	{
					preparedStatement.close();
					throw new Exception("第"+i+"条sql语句执行失败！");
				}
										
			}
		}catch (Exception e) {
			e.printStackTrace();
			try {//全部回滚
				connection.rollback();
				return false;
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}finally {
			try {//成功则提交，失败后回滚后也提交
				connection.commit();
			} catch (SQLException e) {				
				e.printStackTrace();
			}//关闭连接
			try {
				SqlHelper.closeAll(connection, preparedStatement, null);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}		
		return true;		
	}	
	
	/**
	 * 查询操作，select,一条sql语句，多个参数
	 * @param sql
	 * @param params
	 * @return ResultSet
	 */
	public static ResultSet executeQueryReturnResultSet(String sql,String[] params) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;		
		try {
			connection = SqlHelper.getConnection(); 
			preparedStatement = connection.prepareStatement(sql);
			setPreparedStatementAllParams(preparedStatement, params);
			resultSet = preparedStatement.executeQuery();			
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally {
			//SqlHelper.closeAll(connection, preparedStatement, resultSet);			
		}		
		return resultSet;		
	}
	
	/**
	 * 查询操作，select,一条sql语句，一个参数
	 * @param sql
	 * @param param
	 * @return
	 */
	public static ResultSet executeQueryReturnResultSet(String sql,String param) {
		String[] params = {param};		
		return SqlHelper.executeQueryReturnResultSet(sql, params);
	}
	
	/**
	 * 从数据库表取出的字段转换成java数据类型
	 * @param ob 一个字段
	 * @param SqlType 这个字段在数据库表的数据类型
	 * @return
	 */
	private static Object objectToJavaType(Object ob,int SqlType) {		
		switch (SqlType) {
		case java.sql.Types.DATE:
			//java.sql.Date date = (java.sql.Date)ob;
			//return new java.util.Date(date.getTime());
			return (java.sql.Date)ob;
		case java.sql.Types.DOUBLE:
			return (double)ob;
		case java.sql.Types.FLOAT:			
			return (float)ob;
		case java.sql.Types.DECIMAL:
			return (int)ob;
		case java.sql.Types.INTEGER:
			return (int)ob;
		case java.sql.Types.CHAR:
			return (char)ob;
		case java.sql.Types.VARCHAR:
		case java.sql.Types.NCHAR:
		case java.sql.Types.NVARCHAR:
			return (String)ob;
		default:
			return ob;
		}
	}
		
	/**
	 * 查询数据库 select,返回一个对象数组的列表。
	 * 其中每个对象数组的里面存的对象是不同的数据类型。已经从sql数据类型转换成java数据类型了。
	 * @param sql 语句
	 * @param params 多个参数
	 * @return 一个对象数组的列表
	 */
	public static ArrayList<Object[]> executeQueryReturnArrayList(String sql,String[] params) {	
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ArrayList<Object[]> arrayList = null;
		try {
			connection = SqlHelper.getConnection();
			if(connection == null) {
				System.out.println("连接不足！！");
				return null;
			}
			//System.out.println(connection);
			preparedStatement = connection.prepareStatement(sql);
			//System.out.println(preparedStatement);
			setPreparedStatementAllParams(preparedStatement, params);
			//System.out.println(preparedStatement);
			resultSet = preparedStatement.executeQuery();
			ResultSetMetaData metaData = resultSet.getMetaData();			
			int colCount = metaData.getColumnCount();
			arrayList = new ArrayList<Object[]>();
			while(resultSet.next()) {
				Object[] objects = new Object[colCount];	
				for(int i = 0;i < colCount;i++) {	
					objects[i] = objectToJavaType(resultSet.getObject(i+1), metaData.getColumnType(i+1));
				}
				arrayList.add(objects);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				SqlHelper.closeAll(connection, preparedStatement, resultSet);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return arrayList;		
	}

	/**
	 * 查询数据库 select,返回一个对象数组的列表。
	 * 其中每个对象数组的里面存的对象是不同的数据类型。已经从sql数据类型转换成java数据类型了。
	 * @param sql 语句
	 * @param params 单个参数
	 * @return 一个对象数组的列表
	 */
	public static ArrayList<Object[]> executeQueryReturnArrayList(String sql, String param) {
		String[] params = {param};
		return SqlHelper.executeQueryReturnArrayList(sql, params);		
	}

	/**
	 * 执行固定sql语句,update, insert, delete
	 * @param sql
	 * @return
	 */
	public static boolean executeUpdate(String sql) {
		Connection connection = null;
		Statement statement = null;
		try {
			connection = SqlHelper.getConnection();
			statement = connection.createStatement();
			return statement.executeUpdate(sql)>0?true:false;				
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				SqlHelper.closeAll(connection, statement, null);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 执行多行固定sql语句,update, insert, delete 事务回滚
	 * @param sqls
	 * @return
	 */
	public static boolean executeUpdate(String sqls[]) {
		Connection connection = null;
		Statement statement = null;
		try {
			connection = SqlHelper.getConnection();
			statement = connection.createStatement();
			int count = sqls.length;
			connection.setAutoCommit(false);
			for(int i = 0;i < count;i++) {
				statement.executeUpdate(sqls[i]);
			}
		} catch (Exception e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();	
			return false;
		}finally {			
			try {
				connection.commit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				SqlHelper.closeAll(connection, statement, null);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;
	}
	
	static class ConnectionPool{
		
		private LinkedList<Connection> conns = null;
		
		public ConnectionPool() {
			//初始化连接池
			conns = new LinkedList<Connection>();
			for(int i = 0;i < initialSize;i++) {
				try {
					conns.add(DriverManager.getConnection(url, user, password));
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		/**
		 * 从连接池获取一个连接，连接池没有连接则等待
		 * @return
		 */
		public synchronized Connection getConnectionFromPool() {
			
				Connection conn = null;
				while(conns.size() <= 0) {							
					try {
						wait();
					} catch (InterruptedException e) {						
						e.printStackTrace();
					}	
				}				
				if(conns.size() > 0) {
					conn = conns.getFirst();
					conns.removeFirst();		
				}		
				notify();
				return conn;	
			
		}
		
		public synchronized void Release(Connection conn) {			
			conns.add(conn);
			notifyAll();			
		}
		
	}
	
	
}