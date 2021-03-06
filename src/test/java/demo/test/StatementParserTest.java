package demo.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.druid.sql.visitor.SchemaStatVisitor;

import io.mycat.route.parser.druid.MycatSQLServerSchemaStatVisitor;
import io.mycat.route.parser.druid.MycatSQLServerStatementParser;
import io.mycat.route.parser.druid.MycatStatementParser;

public class StatementParserTest {
	public static void main(String[] args) {
		SQLStatementParser parser = null;
		String stmt;
//		stmt="select a.*,b.* from a left join b where b.id=a.id";
//		stmt="select  * from ta limit 0,5";
		stmt="select top 200 row_number() over (order by id) rownum ,* from ta ";
		parser = new MycatStatementParser(stmt);
		parser.getExprParser().getDbType();
		SQLStatement statement=parser.parseStatement();
		System.out.println(statement);
		SchemaStatVisitor visitor = new SchemaStatVisitor();
		List<String> tables = parseTables(statement, visitor);
		System.out.println(tables); 
		System.out.println("sql server model");
		
		parser=new MycatSQLServerStatementParser(stmt);
		statement=parser.parseStatement();
		System.out.println(statement);
		MycatSQLServerSchemaStatVisitor sqlserverVisitor=new MycatSQLServerSchemaStatVisitor();
		tables=parseTables(statement, sqlserverVisitor);
		System.out.println(tables);
//		SQLServerStatementParser selectParser = new SQLServerStatementParser(stmt);
//		SQLStatement st=selectParser.parseSelect(); 
//		System.out.println(st);
//		tables = parseTables(statement, visitor);
//		System.out.println(tables);
	}
	private static List<String> parseTables(SQLStatement stmt, SchemaStatVisitor schemaStatVisitor)
    {
        List<String> tables = new ArrayList<>();
        stmt.accept(schemaStatVisitor);

        if (schemaStatVisitor.getAliasMap() != null)
        {
            for (Map.Entry<String, String> entry : schemaStatVisitor.getAliasMap().entrySet())
            {
                String key = entry.getKey();
                String value = entry.getValue();
                if (value != null && value.indexOf("`") >= 0)
                {
                    value = value.replaceAll("`", "");
                }
                //表名前面带database的，去掉
                if (key != null)
                {
                    int pos = key.indexOf("`");
                    if (pos > 0)
                    {
                        key = key.replaceAll("`", "");
                    }
                    pos = key.indexOf(".");
                    if (pos > 0)
                    {
                        key = key.substring(pos + 1);
                    }

                    if (key.equals(value))
                    {
                        tables.add(key.toUpperCase());
                    }
                }
            }

        }
        return tables;
    }
}
