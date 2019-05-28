package telemarketing.db;

public class SqlTansferForMybatis {

	public static String  parse(String sql){
		sql = sql.replace("<", "&lt;");
		sql = sql.replace(">", "&gt;");
		sql = sql.replace("<>", "&lt;&gt;");
		sql = sql.replace("&", "&amp;");
		sql = sql.replace("'", "&apos;");
		sql = sql.replace("\"", " &quot;");
		
		return sql;
			 
	}
}
