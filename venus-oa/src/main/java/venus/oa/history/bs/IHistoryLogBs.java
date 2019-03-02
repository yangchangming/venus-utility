package venus.oa.history.bs;

import venus.oa.history.vo.HistoryLogVo;
import venus.oa.util.SqlBuilder;
import venus.pub.lang.OID;

import java.util.List;

public interface IHistoryLogBs {
	
	/**
	 * 增加历史日志
	 * @param vo
	 * @return
	 */
	public OID insert(Object vo);
	
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
	public int getRecordCount();
	
	/**
	 * 根据查询条件统计记录条数
	 * @param queryCondition
	 * @return
	 */
	public int getRecordCount(String queryCondition);
	
	/**
	 * 根据条件查询历史日志列表
	 * @param queryCondition
	 * @return
	 */
	public List queryByCondition(String queryCondition);
	
	/**
	 * 根据条件和排序字段查询历史日志列表
	 * @param queryCondition
	 * @param orderStr
	 * @return
	 */
	public List queryByCondition(String queryCondition, String orderStr);
	
	/**
	 * 根据条件分页查询历史日志列表
	 * @param no
	 * @param size
	 * @param queryCondition
	 * @return
	 */
	public List queryByCondition(int no, int size, String queryCondition, String orderStr);
	
	/**
	 * 根据查询条件统计记录条数
	 * @param sql
	 * @return
	 */
	public int getRecordCount(SqlBuilder sql);
	
	/**
	 * 根据条件分页查询历史日志列表
	 * @param no
	 * @param size
	 * @param sql
	 * @return
	 */
	public List queryByCondition(SqlBuilder sql);
}

