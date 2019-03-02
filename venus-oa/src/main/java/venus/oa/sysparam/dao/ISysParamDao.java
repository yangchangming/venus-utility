/*
 * 创建日期 2008-7-31
 */
package venus.oa.sysparam.dao;

import venus.oa.sysparam.vo.SysParamVo;
import venus.pub.lang.OID;

import java.util.List;

/**
 *  2008-7-31
 * @author changming.Y <changming.yang.ah@gmail.com>
 */
public interface ISysParamDao {

	/**
	 * @param vo
	 * @return
	 */
	public OID addPara(SysParamVo vo);

	/**
	 * @param id
	 * @return
	 */
	public int delete(String id);

	/**
	 * @param id
	 * @return
	 */
	public SysParamVo find(String id);

	/**
	 * @param vo
	 * @return
	 */
	public int update(SysParamVo vo);

	/**
	 * @param queryCondition
	 * @return
	 */
	public List queryByCondition(String queryCondition);

	/**
	 * @param queryCondition
	 * @param orderStr
	 * @return
	 */
	public List queryByCondition(String queryCondition, String orderStr);

}

