package venus.oa.service.history.bs.impl;

import venus.oa.service.history.bs.IHistoryLogBs;
import venus.oa.service.history.dao.IHistoryLogDao;
import venus.oa.service.history.model.HistoryLogModel;
import venus.oa.service.history.vo.HistoryLogVo;
import venus.oa.util.SqlBuilder;
import venus.frames.base.bs.BaseBusinessService;
import venus.pub.lang.OID;

import java.util.List;

public class HistoryLogBs extends BaseBusinessService implements IHistoryLogBs {
	private IHistoryLogDao dao=null;
    
    private HistoryLogModel model;
    
	/**
	 * @param model 要设置的 model。
	 */
	public void setModel(HistoryLogModel model) {
		this.model = model;
	}
    /**
     * 得到BS对象
     * 
     * @return BS对象
     */
    public HistoryLogModel getModel() {
        return model;
    }
    
	/**
	 * @param dao 要设置的 dao。
	 */
	public void setDao(IHistoryLogDao dao) {
		this.dao = dao;
	}
	
	/**
	 * 增加历史日志
	 * @param vo
	 * @return
	 */
	public OID insert (Object vo) {
		return getModel().onLog(vo,dao);
	}
	
	/**
	 * 根据条件查询一条历史日志的明细
	 * @param id
	 * @return
	 */
	public HistoryLogVo find (String id) {
		return dao.find(id);
	}
	
	/**
	 * 根据条件查询历史资源的详细信息
	 * @param id
	 * @return
	 */
	public HistoryLogVo findDetail(String id) {
		return dao.findDetail(id);
	}	
	
	/**
	 * 统计记录条数
	 * @return
	 */
	public int getRecordCount() {
		return getRecordCount("");
	}
	
	/**
	 * 根据查询条件统计记录条数
	 * @param queryCondition
	 * @return
	 */
	public int getRecordCount(String queryCondition) {
		return dao.getRecordCount(queryCondition);
	}
	
	/**
	 * 根据条件查询历史日志列表
	 * @param queryCondition
	 * @return
	 */
	public List queryByCondition (String queryCondition) {
		return queryByCondition(-1, -1, queryCondition, null);
	}
	
	/**
	 * 根据条件和排序字段查询历史日志列表
	 * @param queryCondition
	 * @param orderStr
	 * @return
	 */
	public List queryByCondition(String queryCondition, String orderStr) {
		return queryByCondition(-1, -1, queryCondition, orderStr);
	}
	
	/**
	 * 根据条件分页查询历史日志列表
	 * @param no
	 * @param size
	 * @param queryCondition
	 * @return
	 */
	public List queryByCondition(int no, int size, String queryCondition, String orderStr) {
		return dao.queryByCondition(no,  size, queryCondition, orderStr);
	}

	/* （非 Javadoc）
	 * @see venus.authority.service.history.bs.IHistoryLogBs#getRecordCount(venus.authority.util.SqlBuilder)
	 */
	public int getRecordCount(SqlBuilder sql) {
		return dao.getRecordCount(sql);
	}

	/* （非 Javadoc）
	 * @see venus.authority.service.history.bs.IHistoryLogBs#queryByCondition(venus.authority.util.SqlBuilder)
	 */
	public List queryByCondition(SqlBuilder sql) {
		return dao.queryByCondition(sql);
	}
}

