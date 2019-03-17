package venus.frames.base.bs;

import venus.frames.base.IGlobalsKeys;
import venus.frames.jdbc.datasource.ConfDataSource;

import javax.sql.DataSource;

/**
 * @author 孙代勇
 * @author 池建强
 * 
 * 业务服务的抽象基类,需要获取DataSource的业务服务类可以从此类派生
 * 
 * 继承ServletEndpointSupport,使得继承了BaseBusinessService的类
 * 可以通过Spring的remoting方式被发布成webservice
 */
public abstract class BaseBusinessService implements IGlobalsKeys {
	
	/**
	 * 获取数据源
	 * @param dsSrc db.xml文件中配置的数据源
	 * @return DataSource 数据源
	 */
	public DataSource getDataSource(String dsSrc) {
		return new ConfDataSource(dsSrc);
	}
}