package demo.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class StaticMethod {
	 static void print( String name , ResultSet res )
             throws SQLException {
             System.out.println( name);
             ResultSetMetaData meta=res.getMetaData();                       
             //System.out.println( "\t"+res.getRow()+"条记录");
             String  str="";
             for(int i=1;i<=meta.getColumnCount();i++){
                 str+=meta.getColumnName(i)+"   ";
                 //System.out.println( meta.getColumnName(i)+"   ");
             }
             System.out.println("\t"+str);
             str="";
             while ( res.next() ){
                 for(int i=1;i<=meta.getColumnCount();i++){  
                     str+= res.getString(i)+"   ";       
                     } 
                 System.out.println("\t"+str);
                 str="";
             }
         }
	 static Connection getConnection() throws ClassNotFoundException, SQLException{
		 System.out.println("mycat conn=");
		 String jdbcdriver="com.mysql.jdbc.Driver";
	        String jdbcurl="jdbc:mysql://localhost:8066/TESTDB?useUnicode=true&characterEncoding=utf-8";
	        String username="root";
	        String password="123456";
	        System.out.println("开始连接mysql:"+jdbcurl);
	        Class.forName(jdbcdriver);
	        Connection c = DriverManager.getConnection(jdbcurl,username,password); 
	        return c;
	 }
	 static Connection getSqlServerConnection() throws ClassNotFoundException, SQLException{
		 System.out.println("sqlserver conn=");
		 String jdbcdriver="com.microsoft.sqlserver.jdbc.SQLServerDriver";
	        String jdbcurl="jdbc:sqlserver://192.168.237.140:1433;databaseName=db1";
	        String username="sa";
	        String password="123";
	        System.out.println("开始连接sqlserver:"+jdbcurl);
	        Class.forName(jdbcdriver);
	        Connection c = DriverManager.getConnection(jdbcurl,username,password); 
	        return c;
	 }
}
