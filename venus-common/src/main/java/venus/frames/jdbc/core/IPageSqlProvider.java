package venus.frames.jdbc.core;

/**
 * @author wujun
 */
public interface IPageSqlProvider {

	String PAGE_SQL_PROVIDER = "PAGE_SQL_PROVIDER";
	String getSql(String oldSqlStr, int firstResult, int maxResult);
}
