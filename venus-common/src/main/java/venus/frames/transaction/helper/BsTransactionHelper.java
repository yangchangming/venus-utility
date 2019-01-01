/*
 * Created on 2004-11-16
 *
 *
 */
package venus.frames.transaction.helper;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

/**
 * @author 孙代勇
 * 
 * 在不显式使用事务管理的情况下,每个BusinessService的操作在一个独立的事务中执行,
 * 当需要将多个BusinessService的操作放在一个事务中运行时,使用此事务管理类来管理, 使用方式是:
 * 从此类派生具体子类,并覆盖executeInTransaction()方法, 将一系列业务服务操作放在此方法体中 例如:一个一个业务服务方法如下:
 * final DataSource ds = new ConfDataSource("TEST");
 *	BsTransactionHelper tt = new BsTransactionHelper(ds) {
 *			protected Object executeInTransaction() {
 *				FirstTemplateDao dao1 = new FirstTemplateDao(ds);
 *				FirstTemplateDao dao2 = new FirstTemplateDao(ds);
 *				dao1.testTransaction();
 *				dao2.testTransaction();
 *				return null;
 *			}
 *		};
 *		tt.execute();
 *
 */
public abstract class BsTransactionHelper {

	private TransactionTemplate transactionTemplate;

	/**
	 * 设置数据源
	 * 
	 * @param ds
	 *            数据源
	 */
	public void setDataSource(DataSource ds) {
		PlatformTransactionManager transactionManager = new DataSourceTransactionManager(
				ds);
		this.transactionTemplate = new TransactionTemplate(transactionManager);
		this.transactionTemplate
				.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
	}

	/**
	 * 使用此构造函数来实例化
	 * 
	 * @param ds
	 *            数据源
	 */
	public BsTransactionHelper(DataSource ds) {

		setDataSource(ds);
	}

	/**
	 * 将executeInTransaction(BaseJdbcDao jdbcDao)方法中的所有操作, 放在一个事务中运行
	 * 
	 * @return Object 放回对象
	 */
	public Object execute() {
		return transactionTemplate.execute(new TransactionCallback() {

			public Object doInTransaction(TransactionStatus status) {
				return executeInTransaction();
			}

		});

	}

	/**
	 * 抽象的方法,子类必须实现,方法体应为必须在一个事务中的一系列操作
	 * 
	 * 
	 * @return Object 返回结果给调用者
	 */
	protected abstract Object executeInTransaction();

}