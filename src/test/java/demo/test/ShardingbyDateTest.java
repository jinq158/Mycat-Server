package demo.test;

import static demo.test.StaticMethod.getConnection;
import static demo.test.StaticMethod.getSqlServerConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ShardingbyDateTest {
	void clean(Connection c) throws SQLException {
		Statement st = c.createStatement();
		st.execute("delete from ta");
	}

	void insertPartitionbyID(Connection c, int fromid, int count) {
		Statement st=null;
		try {
			st = c.createStatement();

			long l = System.currentTimeMillis();
			for (int i = fromid; i < count; i++) {
				for (int j = 0; j < 10000; j++) {
				st.executeUpdate("insert into ta(createdate,jsonDate,transType,relationid)values('2014-0" + (i +1)
						+ "-12 00:01:55','sdf','TS01','" + UUID.randomUUID().toString() + "')");
				}
			}
			long end = System.currentTimeMillis();
			System.out.println("OK......time=" + (end - l) + " per=" + (end - l) / count + " fromid=" + fromid
					+ " count=" + count);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(st!=null) st.close();
				c.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	void select(Connection c) throws Exception {
		Statement st = c.createStatement();
		long l = System.currentTimeMillis();
		int count = 10000;
		ResultSet rs = st.executeQuery("select top 200 * from ta where createdate>'2014-01-01 00:00:00' and createdate<'2014-01-31 00:00:00' order by createdate ");
		while (rs.next()) {
//			System.out.println("id="+rs.getLong("id")+" relationid="+rs.getString("relationid")+" createtime="+rs.getTimestamp("createdate"));
		}
		long end = System.currentTimeMillis();
		System.out.println("finished......time=" + (end - l) + " per=" + (end - l) / count);
	}
	void selectbyId(Connection c) throws SQLException{
		Statement st = c.createStatement();
		long l = System.currentTimeMillis();
		int count = 10000;
		ResultSet rs = st.executeQuery("select * from ta where relationid='b97562d2-342d-45a0-8bd3-c6285541cacc'");
		long end = System.currentTimeMillis();
		System.out.println("finished......time=" + (end - l) + " per=" + (end - l) / count);
	}
	void selectbyIds(Connection c) throws SQLException{
		Statement st = c.createStatement();
		long l = System.currentTimeMillis();
		int count = 10000;
		ResultSet rs = st.executeQuery("select * from ta where id in(112358,112374,112367)");
		long end = System.currentTimeMillis();
		System.out.println("select by ids finished......time=" + (end - l) + " per=" + (end - l) / count);
	}

	public static void main(String args[]) throws Exception {
		ShardingbyDateTest t = new ShardingbyDateTest();
//		t.clean(getConnection());
		// t.clean(getSqlServerConnection());
//		t.insertPartitionbyID(getConnection(), 0, 4);
		
		// t.insertPartitionbyIDThread();
		// t.insertPartitionbyID(getSqlServerConnection(),10000);

		Connection conn=getConnection();
//		t.select(conn);
//		t.select(conn);
		t.selectbyId(conn);
		t.selectbyIds(conn);
		conn=getSqlServerConnection();

		t.select(conn);
//		 t.select(conn);
		 t.selectbyId(conn);

		// t.shardByDate();
	}

}