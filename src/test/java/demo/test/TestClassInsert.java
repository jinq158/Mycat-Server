package demo.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static demo.test.StaticMethod.print;
import static demo.test.StaticMethod.getConnection;
import static demo.test.StaticMethod.getSqlServerConnection;
/**
 * @author mycat
 *
 */
public class TestClassInsert {
	void clean(Connection c) throws SQLException{
		Statement st = c.createStatement();
      st.execute("delete from travelrecord");
	}
	void insertPartitionbyID(Connection c,int fromid,int count) throws ClassNotFoundException, SQLException{
        Statement st = c.createStatement();
        long l=System.currentTimeMillis();
        for (int i = fromid; i < count; i++) {
        	st.executeUpdate("insert into travelrecord(id,name)values("+i+",'sdf')");
		}
        long end=System.currentTimeMillis();
        System.out.println("OK......time="+(end-l)+" per="+(end-l)/count+" fromid="+fromid+" count="+count);
	}
	void insertPartitionbyIDThread() throws ClassNotFoundException, SQLException, InterruptedException{
		final CountDownLatch begin=new CountDownLatch(1);
		final CountDownLatch end=new CountDownLatch(10);
		final ExecutorService exec=Executors.newFixedThreadPool(10);
		final int count=10000;
		long l=System.currentTimeMillis();
		for (int i = 0; i < 10; i++) {
			final int no=i;
			Runnable run=new Runnable() {
				public void run() {
					try {
						begin.await();
						Connection conn=getConnection();
						insertPartitionbyID(conn,no*(count/10),count/10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					finally {
						end.countDown();
					}
				}
			};
			exec.submit(run);
		}
		begin.countDown();
		System.out.println("start");
		end.await();
		System.out.println("over");
		exec.shutdown();
        long endtime=System.currentTimeMillis();
        System.out.println("finished......time="+(endtime-l)+" per="+(endtime-l)/count);
	}
	void select(Connection c) throws Exception{
		Statement st = c.createStatement();
		long l=System.currentTimeMillis();
        int count=10000;
        	ResultSet rs=st.executeQuery("select top 200 * from travelrecord ");
        	while(rs.next()){
        		rs.getLong("id");
        	}
        long end=System.currentTimeMillis();
        System.out.println("finished......time="+(end-l)+" per="+(end-l)/count);
	}
 
	void shardByDate() throws ClassNotFoundException, SQLException{
		Connection c =getConnection(); 
        Statement st = c.createStatement();
        st.execute("delete from shardbydate");
//        print( "test jdbc " , st.executeQuery("select * from travelrecord where name like 'sd%' limit 2,3")); 
        st.executeUpdate("insert into shardbydate(id,createtime)values(1,'2017-01-01 00:12:12'),(2,'2017-01-01 00:12:12'),(3,'2017-01-01 00:12:12'),(4,'2017-01-01 00:12:12')");
        System.out.println("OK......");
	}
    public static void main( String args[] ) throws Exception {
    	TestClassInsert t=new TestClassInsert();
    	t.clean(getConnection());
    	t.clean(getSqlServerConnection());
//    	t.insertPartitionbyID(getConnection(),0,10000);
    	t.insertPartitionbyIDThread();
//    	t.insertPartitionbyID(getSqlServerConnection(),10000);
    	
    	
    	
    	t.select(getConnection());
    	t.select(getSqlServerConnection());
    	
//    	t.shardByDate();
    }
        
}
