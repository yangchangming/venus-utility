/*
 * 创建日期 2008-7-31
 */
package venus.oa.service.sys.bs.impl;

import venus.oa.service.sys.bs.ISysParamsBs;
import venus.oa.service.sys.dao.ISysParamDao;
import venus.oa.service.sys.vo.SysParamVo;
import venus.pub.lang.OID;

import java.util.List;

/**
 *  2008-7-31
 * @author changming.Y <changming.yang.ah@gmail.com>
 */
public class SysParamsBs implements ISysParamsBs {
	private ISysParamDao dao;

	/* （非 Javadoc）
	 * @see venus.authority.sys.bs.ISysParamsBs#getDao()
	 */
	public ISysParamDao getDao() {
		return dao;
	}

	/* （非 Javadoc）
	 * @see venus.authority.sys.bs.ISysParamsBs#setDao(venus.authority.sys.dao.ISysParamDao)
	 */
	public void setDao(ISysParamDao dao) {
		this.dao=dao;
	}

	/* （非 Javadoc）
	 * @see venus.authority.sys.bs.ISysParamsBs#insert(venus.authority.login.loginlog.vo.SysParamVo)
	 */
	public OID insert(SysParamVo vo) {
		return getDao().addPara(vo);
	}

	/* （非 Javadoc）
	 * @see venus.authority.sys.bs.ISysParamsBs#delete(java.lang.String)
	 */
	public int delete(String id) {
		int sum = getDao().delete(id);
		return sum;
	}

	/* （非 Javadoc）
	 * @see venus.authority.sys.bs.ISysParamsBs#find(java.lang.String)
	 */
	public SysParamVo find(String id) {
		SysParamVo vo = getDao().find(id);
        return vo;
	}

	/* （非 Javadoc）
	 * @see venus.authority.sys.bs.ISysParamsBs#update(venus.authority.login.loginlog.vo.SysParamVo)
	 */
	public int update(SysParamVo vo) {
		return getDao().update(vo);
	}

	/* （非 Javadoc）
	 * @see venus.authority.sys.bs.ISysParamsBs#queryByCondition(java.lang.String)
	 */
	public List queryByCondition(String queryCondition) {
		List lResult = getDao().queryByCondition(queryCondition);
       return lResult;
	}

	/* （非 Javadoc）
	 * @see venus.authority.sys.bs.ISysParamsBs#queryByCondition(java.lang.String, java.lang.String)
	 */
	public List queryByCondition(String queryCondition, String orderStr) {
		List lResult = getDao().queryByCondition(queryCondition,orderStr);
	       return lResult;
	}

}

