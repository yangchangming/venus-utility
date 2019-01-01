
package venus.frames.orm.hibernate;

import org.hibernate.*;
import org.hibernate.type.Type;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import java.util.List;

/**
 * Helper class that simplifies Hibernate data access code, and converts
 * checked HibernateExceptions into unchecked DataAccessExceptions,
 * compatible to the org.springframework.dao exception hierarchy.
 * Uses the same SQLExceptionTranslator mechanism as JdbcTemplate.
 *
 * <p>Typically used to implement data access or business logic services that
 * use Hibernate within their implementation but are Hibernate-agnostic in
 * their interface. The latter resp. code calling the latter only have to deal
 * with domain objects, query objects, and org.springframework.dao exceptions.
 *
 * <p>The central method is "execute", supporting Hibernate code implementing
 * the HibernateCallback interface. It provides Hibernate Session handling
 * such that neither the HibernateCallback implementation nor the calling
 * code needs to explicitly care about retrieving/closing Hibernate Sessions,
 * or handling Session lifecycle exceptions. For typical single step actions,
 * there are various convenience methods (find, load, saveOrUpdate, delete).
 *
 * <p>Can be used within a service implementation via direct instantiation
 * with a SessionFactory reference, or get prepared in an application context
 * and given to services as bean reference. Note: The SessionFactory should
 * always be configured as bean in the application context, in the first case
 * given to the service directly, in the second case to the prepared template.
 *
 * <p>This class can be considered a programmatic alternative to
 * HibernateInterceptor. The major advantage is its straightforwardness, the
 * major disadvantage that no checked application exceptions can get thrown
 * from within data access code. Such checks and the actual throwing of such
 * exceptions can often be deferred to after callback execution, though.
 *
 * <p>Note that even if HibernateTransactionManager is used for transaction
 * demarcation in higher-level services, all those services above the data
 * access layer don't need need to be Hibernate-aware. Setting such a special
 * PlatformTransactionManager is a configuration issue: For example,
 * switching to JTA is just a matter of Spring configuration (use
 * JtaTransactionManager instead) that does not affect application code.
 *
 * <p>LocalSessionFactoryBean is the preferred way of obtaining a reference
 * to a specific Hibernate SessionFactory, at least in a non-EJB environment.
 * Alternatively, use a JndiObjectFactoryBean to fetch a SessionFactory
 * from JNDI (possibly set up via a JCA Connector).
 *
 * <p>Note: Spring's Hibernate support requires Hibernate 2.1 (as of Spring 1.0).
 *
 * @author Sun daiyong
 * @since 01.05.2005
 */
public class PageableHibernateTemplate extends HibernateTemplate implements PageableHibernateOperations {

	/**
	 * Create a new PageableHibernateTemplate instance.
	 */
	public PageableHibernateTemplate() {
	}

	/**
	 * Create a new PageableHibernateTemplate instance.
	 * @param sessionFactory SessionFactory to create Sessions
	 */
	public PageableHibernateTemplate(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	/**
	 * Create a new PageableHibernateTemplate instance.
	 * @param sessionFactory SessionFactory to create Sessions
	 * @param allowCreate if a new Session should be created
	 * if no thread-bound found
	 */
	public PageableHibernateTemplate(SessionFactory sessionFactory, boolean allowCreate) {
		super(sessionFactory, allowCreate);
	}

	public List loadAll(final Class entityClass, final int firstResult, final int maxResult) throws DataAccessException {
		return executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Criteria criteria = session.createCriteria(entityClass);
				int first = (firstResult < 0) ? 0 : firstResult;
				criteria.setFirstResult(first);
				criteria.setMaxResults(maxResult);
				return criteria.list();
			}
		});
	}
	

	public List find(final String queryString, final int firstResult, final int maxResult) throws DataAccessException {
		return executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Query queryObject = session.createQuery(queryString);
				int first = (firstResult < 0) ? 0 : firstResult;
				queryObject.setFirstResult(first);
				queryObject.setMaxResults(maxResult);
				return queryObject.list();
			}
		});	
	}

	public List find(final String queryString, final Object value, final int firstResult, final int maxResult)
		throws DataAccessException {
		return executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Query queryObject = session.createQuery(queryString);
				queryObject.setParameter(0, value);
				int first = (firstResult < 0) ? 0 : firstResult;
				queryObject.setFirstResult(first);
				queryObject.setMaxResults(maxResult);
				return queryObject.list();
			}
		});
	}

	public List find(final String queryString, final Object value, final Type type, final int firstResult, final int maxResult) throws DataAccessException {
		return executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Query queryObject = session.createQuery(queryString);
				queryObject.setParameter(0, value, type);
				int first = (firstResult < 0) ? 0 : firstResult;
				queryObject.setFirstResult(first);
				queryObject.setMaxResults(maxResult);
				return queryObject.list();
			}
		});
	}
	
	
	public List find(final String queryString, final Object[] values, final int firstResult, final int maxResult) throws DataAccessException {
		return executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Query queryObject = session.createQuery(queryString);
				for (int i = 0; i < values.length; i++) {
					queryObject.setParameter(i, values[i]);
				}
				int first = (firstResult < 0) ? 0 : firstResult;
				queryObject.setFirstResult(first);
				queryObject.setMaxResults(maxResult);
				return queryObject.list();
			}
		});
	}


	public List find(final String queryString, final Object[] values, final Type[] types, final int firstResult, final int maxResult) throws DataAccessException {
		if (values.length != types.length) {
			throw new IllegalArgumentException("Length of values array must match length of types array");
		}
		return executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Query queryObject = session.createQuery(queryString);
				for (int i = 0; i < values.length; i++) {
					queryObject.setParameter(i, values[i], types[i]);
				}
				int first = (firstResult < 0) ? 0 : firstResult;
				queryObject.setFirstResult(first);
				queryObject.setMaxResults(maxResult);
				return queryObject.list();
			}
		});
	}
	
}
