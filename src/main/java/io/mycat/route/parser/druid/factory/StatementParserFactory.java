package io.mycat.route.parser.druid.factory;

import com.alibaba.druid.sql.parser.Lexer;
import com.alibaba.druid.sql.parser.SQLStatementParser;

import io.mycat.MycatServer;
import io.mycat.route.parser.druid.MycatSQLServerStatementParser;
import io.mycat.route.parser.druid.MycatStatementParser;

public class StatementParserFactory {
	public static SQLStatementParser getStatementParser(String sql){
		String dbType=MycatServer.getInstance().getConfig().getSystem().getDbType();
		if(dbType.equalsIgnoreCase("sqlserver")){
			return new MycatSQLServerStatementParser(sql);
		}
		return new MycatStatementParser(sql);
	}
	public static SQLStatementParser getStatementParser(Lexer lexer){
		String dbType=MycatServer.getInstance().getConfig().getSystem().getDbType();
		if(dbType.equalsIgnoreCase("sqlserver")){
			return new MycatSQLServerStatementParser(lexer);
		}
		return new MycatStatementParser(lexer);
	}
}	
