package application.model.database;

import java.sql.*;

public final class ConnectDB {
	private static String userName = "root",
	pass="root",
	url = "jdbc:mysql://localhost:3306/",
	driver = "com.mysql.jdbc.Driver",
	ssl ="?useSSL=false";
	
	static int userCount;
	
	public static Connection conn = null; 
	public static Statement stmt = null;
	
	
	public static void makeConnection() {
		
		try{
			Class.forName(driver);
			conn = DriverManager.getConnection(url+ssl,userName,pass);
		
			if(!DBexists()){
				
				stmt = conn.createStatement();
				conn.setAutoCommit(false);
				String sql = "create database notify;";
				String sql0 = "use notify;";
				String sql1 = "create table users(user_id INT(11) NOT NULL UNIQUE AUTO_INCREMENT,"
						+ "username VARCHAR(45)	NOT NULL UNIQUE, password VARCHAR(45) NOT NULL,"
						+"f_name VARCHAR(45),l_name VARCHAR(45),PRIMARY KEY(user_id,username));";
				String sql2 = "create table events(user_id INT(11) NOT NULL ,"
						+ "event_type VARCHAR(45) NOT NULL,event_name VARCHAR(45) NOT NULL,"
						+ "date VARCHAR(45) NOT NULL, time VARCHAR(45) NOT NULL, recurrence VARCHAR(45) NOT NULL,notify TINYINT NOT NULL,"
						+ "other_info VARCHAR(100) );";
                                
				stmt.addBatch(sql);
				stmt.addBatch(sql0);
				stmt.addBatch(sql1);
				stmt.addBatch(sql2);
				stmt.executeBatch();
				conn.commit();
				System.out.println("connection successful");
			}
			
			conn = DriverManager.getConnection(url+"notify"+ssl,userName,pass);
			stmt = ConnectDB.conn.createStatement();
			ResultSet rs = stmt.executeQuery("select COUNT(*) from users;");
			System.out.println("connection successful!");
			System.out.println(userCount);
		
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		
	}
	
	//check from existing databases for notify
	private static boolean DBexists(){
		try {
		ResultSet rs = conn.getMetaData().getCatalogs();
		System.out.println("Connecting to DB......");
		
			while(rs.next()){
				String DBname = rs.getString(1);
				if(DBname.equals("notify")){
					return true;
					
				}
			}
		} 
		catch (SQLException e) {
		}
	
		return false;
	}
}
