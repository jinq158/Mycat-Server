package io.mycat.route.parser.druid.factory;

import com.alibaba.druid.sql.visitor.SchemaStatVisitor;

import io.mycat.MycatServer;
import io.mycat.route.parser.druid.MycatSQLServerSchemaStatVisitor;
import io.mycat.route.parser.druid.MycatSchemaStatVisitor;

public class SchemaStatVistorFactory {
	public static SchemaStatVisitor getSchemaStatVistor(){
		String dbType=MycatServer.getInstance().getConfig().getSystem().getDbType();
		if(dbType.equalsIgnoreCase("sqlserver")){
			return new MycatSQLServerSchemaStatVisitor();
		}
		return new MycatSchemaStatVisitor();
	} 
}
