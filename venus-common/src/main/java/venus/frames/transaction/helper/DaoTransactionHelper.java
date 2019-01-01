/*
 * Created on 2004-11-15
 *
 */
package venus.frames.transaction.helper;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import venus.frames.base.dao.BaseTemplateDao;

import javax.sql.DataSource;


/**
 * @author 孙代勇
 * 
 * 在不显式使用事务管理的情况下,每个BaseJdbcDao的操作在一个独立的事务中执行,
 * 当需要将一个BaseJdbcDao的多个操作放在一个事务中运行时,使用此事务管理类来管理, 使用方式是:
 * 从此类派生具体子类,并覆盖executeInTransaction(BaseJdbcDao jdbcDao)方法, 将一系列数据存取操作放在此方法体中
 * 例如:在BaseJdbcDao的子类中有一个方法: 
 * public void daoMethod() {
 * 	DaoTransactionHelper tt = new DaoTransactionHelper(this){
 * 
 * 	protected Object executeInTransaction(BaseJdbcDao jdbcDao) {
 * 
 * 		jdbcDao.update("update test set user = 'sun' where id='1'");
 * 		jdbcDao.update("update test set user = 'daiyong' where id='2'"); return null; } 
 * 	};
 * 	tt.execute(); 
 * }
 */
public abstract class DaoTransactionHelper {

	private BaseTemplateDao jdbcDao;

	private TransactionTemplate transactionTemplate;

	/**
	 * 无参数的构造函数,以便BeanFactory创建对象
	 *  
	 */
	public DaoTransactionHelper() {
	}

	/**
	 * @param jdbcDao
	 *            设置JdbcDao.
	 */
	public void setJdbcDao(BaseTemplateDao jdbcDao) {
		DataSource dataSource = jdbcDao.getDataSource();
		PlatformTransactionManager transactionManager = new DataSourceTransactionManager(
				dataSource);
		this.transactionTemplate = new TransactionTemplate(transactionManager);
		this.transactionTemplate
				.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		this.jdbcDao = jdbcDao;
	}

	/**
	 * 
	 * @param jdbcDao
	 *            通常在Dao类中使用时,传入this变量
	 */
	public DaoTransactionHelper(BaseTemplateDao jdbcDao) {
		setJdbcDao(jdbcDao);
	}

	/**
	 * 将executeInTransaction(BaseJdbcDao jdbcDao)方法中的所有操作, 放在一个事务中运行
	 * 
	 * @return Object 放回对象
	 */
	public Object execute() {
		final BaseTemplateDao jdbc = jdbcDao;
		return transactionTemplate.execute(new TransactionCallback() {

			public Object doInTransaction(TransactionStatus status) {
				return executeInTransaction(jdbc);
			}

		});

	}

	/**
	 * 抽象的方法,之类必须实现,方法体应为必须在一个事务中的一系列操作
	 * 
	 * @param jdbcDao
	 *            具体的Dao类
	 * @return Object 返回结果给调用者
	 */
	protected abstract Object executeInTransaction(BaseTemplateDao jdbcDao);

}