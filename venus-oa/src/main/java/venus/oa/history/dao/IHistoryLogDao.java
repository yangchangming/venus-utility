/*
 * 创建日期 2008-10-29
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package venus.oa.history.dao;

import venus.oa.history.vo.HistoryLogVo;
import venus.oa.util.SqlBuilder;
import venus.pub.lang.OID;

import java.util.List;

/**
 * @author zangjian
 *
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public interface IHistoryLogDao {
	
	/**
	 * 增加历史日志
	 * @param vo
	 * @return
	 */
	public OID insert(HistoryLogVo vo);
	
	/**
	 * 根据条件查询一条历史日志的明细
	 * @param id
	 * @return
	 */
	public HistoryLogVo find(String id);

	/**
	 * 根据条件查询历史资源的详细信息
	 * @param id
	 * @return
	 */
	public HistoryLogVo findDetail(String id);
	
	/**
	 * 统计记录条数
	 * @return
	 */
	public int getRecordCount(String queryCondition);
	
	/**
	 * 根据条件分页查询历史日志列表
	 * @param no
	 * @param size
	 * @param queryCondition
	 * @return
	 */
	public List queryByCondition(int no, int size, String queryCondition, String orderStr);

	/**
	 * 根据条件分页查询历史日志列表条数
	 * @param sql
	 * @return
	 */
	public int getRecordCount(SqlBuilder sql);

	/**
	 * 根据条件分页查询历史日志列表
	 * @param sql
	 * @return
	 */
	public List queryByCondition(SqlBuilder sql);
}

