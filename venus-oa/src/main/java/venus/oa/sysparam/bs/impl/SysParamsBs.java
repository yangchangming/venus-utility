/*
 * 创建日期 2008-7-31
 */
package venus.oa.sysparam.bs.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import venus.oa.sysparam.bs.ISysParamsBs;
import venus.oa.sysparam.dao.ISysParamDao;
import venus.oa.sysparam.vo.SysParamVo;
import venus.pub.lang.OID;

import java.util.List;

/**
 *  2008-7-31
 * @author changming.Y <changming.yang.ah@gmail.com>
 */
@Service
public class SysParamsBs implements ISysParamsBs {

	@Autowired
	private ISysParamDao sysParamDao;

	/* （非 Javadoc）
	 * @see venus.authority.sys.bs.ISysParamsBs#insert(venus.authority.login.loginlog.vo.SysParamVo)
	 */
	public OID insert(SysParamVo vo) {
		return sysParamDao.addPara(vo);
	}

	/* （非 Javadoc）
	 * @see venus.authority.sys.bs.ISysParamsBs#delete(java.lang.String)
	 */
	public int delete(String id) {
		int sum = sysParamDao.delete(id);
		return sum;
	}

	/* （非 Javadoc）
	 * @see venus.authority.sys.bs.ISysParamsBs#find(java.lang.String)
	 */
	public SysParamVo find(String id) {
		SysParamVo vo = sysParamDao.find(id);
        return vo;
	}

	/* （非 Javadoc）
	 * @see venus.authority.sys.bs.ISysParamsBs#update(venus.authority.login.loginlog.vo.SysParamVo)
	 */
	public int update(SysParamVo vo) {
		return sysParamDao.update(vo);
	}

	/* （非 Javadoc）
	 * @see venus.authority.sys.bs.ISysParamsBs#queryByCondition(java.lang.String)
	 */
	public List queryByCondition(String queryCondition) {
		List lResult = sysParamDao.queryByCondition(queryCondition);
       return lResult;
	}

	/* （非 Javadoc）
	 * @see venus.authority.sys.bs.ISysParamsBs#queryByCondition(java.lang.String, java.lang.String)
	 */
	public List queryByCondition(String queryCondition, String orderStr) {
		List lResult = sysParamDao.queryByCondition(queryCondition,orderStr);
	       return lResult;
	}

}

