package venus.frames.base.dao;

import org.hibernate.LockMode;
import org.hibernate.type.Type;
import org.springframework.dao.DataAccessException;
import venus.VenusHelper;
import venus.frames.base.IGlobalsKeys;
import venus.frames.mainframe.log.ILog;
import venus.frames.mainframe.log.LogMgr;
import venus.frames.orm.hibernate.support.HibernateDaoSupport;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @author 孙代勇
 *  
 */
public abstract class BaseHibernateDao extends HibernateDaoSupport implements IGlobalsKeys {

    protected final ILog logger = LogMgr.getLogger(this);

    //-------------------------------------------------------------------------
    // Convenience methods for loading individual objects
    //-------------------------------------------------------------------------

    /**
     * 根据ID返回entityClass类型的持久化实例，如果找不到相应的记录，则返回null
     * 
     * @param entityClass
     *            持久化实例的类型
     * @param id
     *            持久化实例的id
     * @return 持久化实例或null
     * @throws venus.frames.base.dao.DataAccessException
     * @see net.sf.hibernate.Session#get(Class, Serializable)
     */
    public Object get(final Class entityClass, final Serializable id) throws DataAccessException {
        return this.getHibernateTemplate().get(entityClass, id);
    }

    /**
     * 根据ID返回entityClass类型的持久化实例，如果找不到相应的记录，则返回null 并指定数据操作锁模式为lockMode
     *
     * @param entityClass
     *            持久化实例的类型
     * @param id
     *            持久化实例的id
     * @param lockMode
     *            获得的锁模式
     * @return 持久化实例或null
     * @throws venus.frames.base.dao.DataAccessException
     *             in case of Hibernate errors
     * @see net.sf.hibernate.Session#get(Class, Serializable,
     *      net.sf.hibernate.LockMode)
     */
    public Object get(final Class entityClass, final Serializable id,
            final LockMode lockMode) throws DataAccessException {
        return this.getHibernateTemplate().get(entityClass, id, lockMode);
    }

    /**
     * 根据ID返回entityClass类型的持久化实例，如果找不到相应的记录，则抛出异常
     *
     * @param entityClass
     *            持久化实例的类型
     * @param id
     *            持久化实例的id
     * @return 持久化实例
     * @throws HibernateObjectRetrievalFailureException
     *             if the instance could not be found
     * @throws venus.frames.base.dao.DataAccessException
     *             in case of Hibernate errors
     * @see net.sf.hibernate.Session#load(Class, Serializable)
     */
    public Object load(final Class entityClass, final Serializable id)
            throws DataAccessException {
        return this.getHibernateTemplate().load(entityClass, id);
    }

    /**
     * 根据ID返回entityClass类型的持久化实例，如果找不到相应的记录，则返回null 并指定数据操作锁模式为lockMode
     *
     * @param entityClass
     *            持久化实例的类型
     * @param id
     *            持久化实例的id
     * @param lockMode
     *            获得的锁模式
     * @return 持久化实例
     * @throws HibernateObjectRetrievalFailureException
     *             if the instance could not be found
     * @throws venus.frames.base.dao.DataAccessException
     *             in case of Hibernate errors
     * @see net.sf.hibernate.Session#load(Class, Serializable)
     */
    public Object load(final Class entityClass, final Serializable id,
            final LockMode lockMode) throws DataAccessException {
        return this.getHibernateTemplate().load(entityClass, id, lockMode);
    }

    /**
     * 返回entityClass类型的所有持久化实例 Note: Use queries or criteria for retrieving a
     * specific subset.
     * 
     * @param entityClass
     *            持久化实例的类型
     * @return 包含0个或多个持久化实例的列表
     * @throws venus.frames.base.dao.DataAccessException
     *             if there is a Hibernate error
     * @see net.sf.hibernate.Session#createCriteria
     */
    public List loadAll(final Class entityClass) throws DataAccessException {
        return this.getHibernateTemplate().loadAll(entityClass);
    }

    /**
     * 返回给定类型的持久化实例列表,并限定其记录范围
     * 
     * @param entityClass
     *            持久化实例的类型
     * @param firstResult
     *            第一条记录起始位置
     * @param maxResult
     *            返回的最大记录条数
     * @return 包含0个或多个对象的列表
     * @throws DataAccessException
     */
    public List loadAll(final Class entityClass, final int firstResult,
            final int maxResult) throws DataAccessException {
        return this.getHibernateTemplate().loadAll(entityClass, firstResult,
                maxResult);
    }

    /**
     * 重新读取给定持久化实例的状态.
     * 
     * @param entity
     *            需要重新读取的持久化实例
     * @throws venus.frames.base.dao.DataAccessException
     *             in case of Hibernate errors
     * @see net.sf.hibernate.Session#refresh(Object)
     */
    public void refresh(final Object entity) throws DataAccessException {
        this.getHibernateTemplate().refresh(entity);
    }

    /**
     * 重新读取给定持久化实例的状态. 并指定数据操作锁模式为lockMode
     * 
     * @param entity
     *            需要重新读取的持久化实例
     * @param lockMode
     *            获得的锁模式
     * @throws venus.frames.base.dao.DataAccessException
     *             in case of Hibernate errors
     * @see net.sf.hibernate.Session#refresh(Object, net.sf.hibernate.LockMode)
     */
    public void refresh(final Object entity, final LockMode lockMode)
            throws DataAccessException {
        this.getHibernateTemplate().refresh(entity, lockMode);
    }

    /**
     * 检查给定对象是否在会话缓存中
     * 
     * @param entity
     *            需要被检查的持久化实例
     * @return whether the given object is in the Session cache
     * @throws venus.frames.base.dao.DataAccessException
     *             if there is a Hibernate error
     * @see net.sf.hibernate.Session#contains
     */
    public boolean contains(final Object entity) throws DataAccessException {
        return this.getHibernateTemplate().contains(entity);
    }

    /**
     * 从会话缓存中出去指定对象
     * 
     * @param entity
     *            需要被出去的对象
     * @throws venus.frames.base.dao.DataAccessException
     *             in case of Hibernate errors
     * @see net.sf.hibernate.Session#evict
     */
    public void evict(final Object entity) throws DataAccessException {
        this.getHibernateTemplate().evict(entity);
    }

    //-------------------------------------------------------------------------
    // Convenience methods for storing individual objects
    //-------------------------------------------------------------------------

    /**
     * 将指定对象持久化
     * 
     * @param entity
     *            待持久化的对象
     * @return 被持久化的对象ID
     * @throws venus.frames.base.dao.DataAccessException
     *             in case of Hibernate errors
     * @see net.sf.hibernate.Session#save(Object)
     */
    public Serializable save(final Object entity) throws DataAccessException {
        return this.getHibernateTemplate().save(entity);
    }

//    /**
//     * 根据ID将指定对象持久化
//     * 
//     * @param entity
//     *            待持久化的对象
//     * @param id
//     *            the identifier to assign
//     * @throws venus.frames.base.dao.DataAccessException
//     *             in case of Hibernate errors
//     * @see net.sf.hibernate.Session#save(Object, java.io.Serializable)
//     */
//    public void save(Object entity, Serializable id) throws DataAccessException {
//        this.getHibernateTemplate().merge(entity, id);
//    }

    /**
     * 根据ID持久化或更新持久化对象（对应于映射文件中的unsaved-value属性）
     * 
     * @param entity
     *            the persistent instance to save respectively update (to be
     *            associated with the Hibernate Session)
     * @throws DataAccessException
     *             in case of Hibernate errors
     * @see net.sf.hibernate.Session#saveOrUpdate(Object)
     */
    public void saveOrUpdate(Object entity) throws DataAccessException {
        this.getHibernateTemplate().saveOrUpdate(entity);
    }

    /**
     * 更新指定的持久化实例
     * 
     * @param entity
     *            待更新的持久化实例
     * @throws venus.frames.base.dao.DataAccessException
     *             in case of Hibernate errors
     * @see net.sf.hibernate.Session#update(Object)
     */
    public void update(Object entity) throws DataAccessException {
        this.getHibernateTemplate().update(entity);
    }

    /**
     * 更新指定的持久化实例
     * <p>
     * Obtains the specified lock mode if the instance exists, implicitly
     * checking whether the corresponding database entry still exists (throwing
     * an OptimisticLockingFailureException if not found).
     * 
     * @param entity
     *            待更新的持久化实例
     * @param lockMode
     *            获得的锁模式
     * @throws venus.frames.base.dao.DataAccessException
     *             in case of Hibernate errors
     * @see HibernateOptimisticLockingFailureException
     * @see net.sf.hibernate.Session#update(Object)
     */
    public void update(Object entity, LockMode lockMode)
            throws DataAccessException {
        this.getHibernateTemplate().update(entity, lockMode);
    }

    /**
     * 删除指定的持久化实例
     * 
     * @param entity
     *            待删除的持久化实例
     * @throws venus.frames.base.dao.DataAccessException
     *             in case of Hibernate errors
     * @see net.sf.hibernate.Session#delete(Object)
     */
    public void delete(Object entity) throws DataAccessException {
        this.getHibernateTemplate().delete(entity);
    }

    /**
     * 删除指定的持久化实例
     * <p>
     * Obtains the specified lock mode if the instance exists, implicitly
     * checking whether the corresponding database entry still exists (throwing
     * an OptimisticLockingFailureException if not found).
     * 
     * @param entity
     *            待删除的持久化实例
     * @param lockMode
     *            the lock mode to obtain
     * @throws venus.frames.base.dao.DataAccessException
     *             in case of Hibernate errors
     * @see HibernateOptimisticLockingFailureException
     * @see net.sf.hibernate.Session#delete(Object)
     */
    public void delete(Object entity, LockMode lockMode)
            throws DataAccessException {
        this.getHibernateTemplate().delete(entity, lockMode);
    }

    /**
     * 删除集合中的所有持久化实例
     * <p>
     * This can be combined with any of the find methods to delete by query in
     * two lines of code, similar to Session's delete by query methods.
     * 
     * @param entities
     *            the persistent instances to delete
     * @throws venus.frames.base.dao.DataAccessException
     *             in case of Hibernate errors
     * @see net.sf.hibernate.Session#delete(String)
     */
    public void deleteAll(Collection entities) throws DataAccessException {
        this.getHibernateTemplate().deleteAll(entities);
    }

    /**
     * 立即执行所有对数据库的save、updates和deletes操作。
     * <p>
     * Only invoke this for selective eager flushing, for example when JDBC code
     * needs to see certain changes within the same transaction. Else, it's
     * preferable to rely on auto-flushing at transaction completion.
     * 
     * @throws venus.frames.base.dao.DataAccessException
     *             in case of Hibernate errors
     * @see net.sf.hibernate.Session#flush
     */
    public void flush() throws DataAccessException {
        this.getHibernateTemplate().flush();
    }

    /**
     * 除去会话缓存中的所有对象，并取消所有的save、upates和deletes操作
     * 
     * @throws venus.frames.base.dao.DataAccessException
     *             in case of Hibernate errors
     * @see net.sf.hibernate.Session#clear
     */
    public void clear() throws DataAccessException {
        this.getHibernateTemplate().clear();
    }

    //-------------------------------------------------------------------------
    // Convenience finder methods for HQL strings
    //-------------------------------------------------------------------------

    /**
     * 执行对持久化实例的查询
     * 
     * @param queryString
     *            Hibernate查询语言的查询表达式
     * @return 包含0或多条持久化实例的对象列表
     * @throws venus.frames.base.dao.DataAccessException
     *             in case of Hibernate errors
     * @see net.sf.hibernate.Session#find(String)
     * @see net.sf.hibernate.Session#createQuery
     */
    public List find(String queryString) throws DataAccessException {
 
        if (VenusHelper.SQL_FILTER) {
            queryString = VenusHelper.doSqlFilter(queryString);
        }
        
        return this.getHibernateTemplate().find(queryString);
    }

    /**
     * 执行对持久化实例的查询,并限定其范围
     * 
     * @param queryString
     *            Hibernate查询语言的查询表达式
     * @param firstResult
     *            起始记录
     * @param maxResult
     *            查询记录数
     * @return 包含0或多条持久化实例的对象列表
     * @throws venus.frames.base.dao.DataAccessException
     *             Hibernate错误时,抛出此异常
     */
    public List find(String queryString, int firstResult, int maxResult)
            throws DataAccessException {
 
        if (VenusHelper.SQL_FILTER) {
            queryString = VenusHelper.doSqlFilter(queryString);
        }
        
        return this.getHibernateTemplate().find(queryString, firstResult,
                maxResult);
    }

    /**
     * 执行一个对持久化实例的查询,邦定一个值到查询串中的参数"?".
     * 
     * @param queryString
     *            Hibernate查询语言的查询表达式
     * @param value
     *            参数值
     * @return 包含0个或多个持久化实例的列表
     */
    public List find(String queryString, Object value)
            throws DataAccessException {
 
        if (VenusHelper.SQL_FILTER) {
            queryString = VenusHelper.doSqlFilter(queryString);
        }
        
        return this.getHibernateTemplate().find(queryString, value);
    }

    /**
     * 执行一个对持久化实例的查询,邦定一个值到查询串中的参数"?".
     * 
     * @param queryString
     *            Hibernate查询语言的查询表达式
     * @param value
     *            参数值
     * @param firstResult
     *            起始记录
     * @param maxResult
     *            查询记录数
     * @return 包含0个或多个持久化实例的列表
     * @throws DataAccessException
     *             Hibernate错误时抛出此实例
     */
    public List find(String queryString, Object value, int firstResult, int maxResult) throws DataAccessException {
        if (VenusHelper.SQL_FILTER) {
            queryString = VenusHelper.doSqlFilter(queryString);
        }
        return this.getHibernateTemplate().find(queryString, value, firstResult, maxResult);
    }

    /**
     * 执行一个对持久化实例的查询,邦定一个给定类型的值到查询串中的参数"?"
     * 
     * @param queryString
     *            Hibernate查询语言的查询表达式
     * @param value
     *            参数值
     * @param type
     *            参数的Hibernate类型
     * @return 包含0个或多个持久化实例的列表
     * @throws venus.frames.base.dao.DataAccessException
     *             in case of Hibernate errors
     * @see net.sf.hibernate.Session#find(String, Object,
     *      net.sf.hibernate.type.Type)
     * @see net.sf.hibernate.Session#createQuery
     */
    public List find(String queryString, Object value, Type type)
            throws DataAccessException {
 
        if (VenusHelper.SQL_FILTER) {
            queryString = VenusHelper.doSqlFilter(queryString);
        }
        
        return this.getHibernateTemplate().find(queryString, value, type);
    }

    /**
     * 执行一个对持久化实例的查询,邦定一个给定类型的值到查询串中的参数"?"
     * 
     * @param queryString
     *            Hibernate查询语言的查询表达式
     * @param value
     *            参数值
     * @param type
     *            参数的Hibernate类型
     * @param firstResult
     *            起始记录
     * @param maxResult
     *            查询记录数
     * @return 包含0个或多个持久化实例的列表
     * @throws DataAccessException
     *             Hibernate错误时抛出此实例
     */
    List find(String queryString, Object value, Type type, int firstResult,
            int maxResult) throws DataAccessException {

        if (VenusHelper.SQL_FILTER) {
            queryString = VenusHelper.doSqlFilter(queryString);
        }
        
        return this.getHibernateTemplate().find(queryString, value, type,
                firstResult, maxResult);
    }

    /**
     * 执行一个对持久化实例的查询,邦定一个给定类型的值到查询串中的参数"?"
     * 
     * @param queryString
     *            Hibernate查询语言的查询表达式
     * @param values
     *            参数值
     * @return a包含0个或多个持久化实例的列表
     * @throws venus.frames.base.dao.DataAccessException
     *             in case of Hibernate errors
     * @see net.sf.hibernate.Session#find(String, Object[],
     *      net.sf.hibernate.type.Type[])
     * @see net.sf.hibernate.Session#createQuery
     */
    public List find(String queryString, Object[] values)
            throws DataAccessException {
 
        if (VenusHelper.SQL_FILTER) {
            queryString = VenusHelper.doSqlFilter(queryString);
        }
        
        return this.getHibernateTemplate().find(queryString, checkArgs(values));
    }

    /**
     * 执行一个对持久化实例的查询,邦定一个给定类型的值到查询串中的参数"?"
     * 
     * @param queryString
     *            Hibernate查询语言的查询表达式
     * @param values
     *            参数值
     * @param firstResult
     *            起始记录
     * @param maxResult
     *            查询记录数
     * @return 包含0个或多个持久化实例的列表
     * @throws DataAccessException
     *             Hibernate错误时抛出此实例
     */
    public List find(String queryString, Object[] values, int firstResult,
            int maxResult) throws DataAccessException {
 
        if (VenusHelper.SQL_FILTER) {
            queryString = VenusHelper.doSqlFilter(queryString);
        }
        
        return this.getHibernateTemplate().find(queryString, checkArgs(values),
                firstResult, maxResult);
    }

    /**
     * 执行一个对持久化实例的查询,邦定一个给定类型的值到查询串中的参数"?"
     * 
     * @param queryString
     *            Hibernate查询语言的查询表达式
     * @param values
     *            参数值
     * @param types
     *            参数的Hibernate类型
     * @return 包含0个或多个持久化实例的列表
     * @throws venus.frames.base.dao.DataAccessException
     *             in case of Hibernate errors
     * @see net.sf.hibernate.Session#find(String, Object[],
     *      net.sf.hibernate.type.Type[])
     * @see net.sf.hibernate.Session#createQuery
     */
    public List find(String queryString, Object[] values, Type[] types)
            throws DataAccessException {
  
        if (VenusHelper.SQL_FILTER) {
            queryString = VenusHelper.doSqlFilter(queryString);
        }
        
        return this.getHibernateTemplate().find(queryString, values, types);
    }

    /**
     * 执行一个对持久化实例的查询,邦定一个给定类型的值到查询串中的参数"?"
     * 
     * @param queryString
     *            Hibernate查询语言的查询表达式
     * @param values
     *            参数值
     * @param types
     *            参数的Hibernate类型
     * @param firstResult
     *            起始记录
     * @param maxResult
     *            查询记录数
     * @return 包含0个或多个持久化实例的列表
     * @throws DataAccessException
     *             Hibernate错误时抛出此实例
     */
    public List find(String queryString, Object[] values, Type[] types,
            int firstResult, int maxResult) throws DataAccessException {
 
        if (VenusHelper.SQL_FILTER) {
            queryString = VenusHelper.doSqlFilter(queryString);
        }
        
        return this.getHibernateTemplate().find(queryString, values, types,
                firstResult, maxResult);
    }

    /**
     * 删除所有由queryString查询到的对象，并返回删除的对象数目。
     * 
     * @param queryString
     *            HQL查询字符串
     * @return 删除的对象数目
     * @throws venus.frames.base.dao.DataAccessException
     *             in case of Hibernate errors
     * @see net.sf.hibernate.Session#delete(String)
     */
    public void delete(String queryString) throws DataAccessException {
        this.getHibernateTemplate().getSessionFactory().getCurrentSession().delete(queryString);
    }

    /**
     * 删除所有由queryString查询到的对象，并返回删除的对象数目。
     * 
     * @param queryString
     *            HQL查询字符串
     * @param value
     *            查询参数值
     * @param type
     *            查询参数的类型
     * @return 删除的对象数目
     * @throws venus.frames.base.dao.DataAccessException
     *             in case of Hibernate errors
     * @see net.sf.hibernate.Session#delete(String, Object,
     *      net.sf.hibernate.type.Type)
     */
//    public void delete(String queryString, Object value, Type type)
//            throws DataAccessException {
//        this.getHibernateTemplate().bulkUpdate(queryString, value, type);
//    }

    /**
     * 删除所有由queryString查询到的对象，并返回删除的对象数目。
     * 
     * @param queryString
     *            HQL查询字符串
     * @param values
     *            查询参数值
     * @param types
     *            查询参数的类型
     * @return 删除的对象数目
     * @throws venus.frames.base.dao.DataAccessException
     *             in case of Hibernate errors
     * @see net.sf.hibernate.Session#delete(String, Object[],
     *      net.sf.hibernate.type.Type[])
     */
//    public int delete(String queryString, Object[] values, Type[] types)
//            throws DataAccessException {
//        return this.getHibernateTemplate().bulkUpdate(queryString, values, types);
//    }

    private Object[] checkArgs(Object[] args) {
        return args;
    }
}