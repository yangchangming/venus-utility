/*
 * Copyright 2002-2004 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 

package venus.frames.orm.hibernate;

import org.hibernate.type.Type;
import org.springframework.dao.DataAccessException;

import java.util.List;


/**
 * Interface that specifies a basic set of Hibernate operations for page.
 * Implemented by PageableHibernateTemplate. Not often used, but a useful option
 * to enhance testability, as it can easily be mocked or stubbed.
 *
 * <p>Provides HibernateTemplate's data access methods that mirror
 * various Session methods.
 *
 * @author Sun daiyong
 * @since 01.04.2005
 * @see PageableHibernateTemplate
 * @see net.sf.hibernate.Session
 */
public interface PageableHibernateOperations {

	
	/**
	 * 返回给定类型的持久化实例列表,并限定其记录范围
	 * @param entityClass a persistent class
	 * @param firstResult 第一条记录起始位置
	 * @param maxResult		返回的最大记录条数
	 * @return 包含0个或多个对象的列表
	 * @throws DataAccessException
	 */
	List loadAll(Class entityClass, int firstResult, int maxResult) throws DataAccessException;

	
	/**
	 * 执行对持久化实例的查询,并限定其范围
	 * @param queryString Hibernate查询语言的查询表达式
	 * @param firstResult 起始记录
	 * @param maxResult 查询记录数
	 * @return 包含0或多条持久化实例的对象列表
	 * @throws venus.frames.base.dao.DataAccessException Hibernate错误时,抛出此异常
	 */
	List find(String queryString, int firstResult, int maxResult) throws DataAccessException;


	/**
	 * 执行一个对持久化实例的查询,邦定一个值到查询串中的参数"?".
	 * @param queryString Hibernate查询语言的查询表达式
	 * @param value 参数值
	 * @param firstResult 起始记录
	 * @param maxResult 查询记录数
	 * @return 包含0个或多个持久化实例的列表
	 * @throws DataAccessException Hibernate错误时抛出此实例
	 */
	List find(String queryString, Object value, int firstResult, int maxResult) throws DataAccessException;

	
	/**
	 * 执行一个对持久化实例的查询,邦定一个给定类型的值到查询串中的参数"?"
	 * @param queryString Hibernate查询语言的查询表达式
	 * @param value 参数值
	 * @param type 参数的Hibernate类型
	 * @param firstResult 起始记录
	 * @param maxResult 查询记录数
	 * @return 包含0个或多个持久化实例的列表
	 * @throws DataAccessException Hibernate错误时抛出此实例
	 */
	List find(String queryString, Object value, Type type, int firstResult, int maxResult) throws DataAccessException;

	
	/**
	 * 执行一个对持久化实例的查询,邦定一个给定类型的值到查询串中的参数"?"
	 * @param queryString Hibernate查询语言的查询表达式
	 * @param values 参数值
	 * @param firstResult 起始记录
	 * @param maxResult 查询记录数
	 * @return 包含0个或多个持久化实例的列表
	 * @throws DataAccessException Hibernate错误时抛出此实例
	 */
	List find(String queryString, Object[] values, int firstResult, int maxResult) throws DataAccessException;

	
	/**
	 * 执行一个对持久化实例的查询,邦定一个给定类型的值到查询串中的参数"?"
	 * @param queryString Hibernate查询语言的查询表达式
	 * @param values 参数值
	 * @param types 参数的Hibernate类型
	 * @param firstResult 起始记录
	 * @param maxResult 查询记录数
	 * @return 包含0个或多个持久化实例的列表
	 * @throws DataAccessException Hibernate错误时抛出此实例
	 */
	List find(String queryString, Object[] values, Type[] types, int firstResult, int maxResult) throws DataAccessException;

}
