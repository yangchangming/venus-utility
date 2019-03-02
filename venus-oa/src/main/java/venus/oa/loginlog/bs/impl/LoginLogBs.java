package venus.oa.loginlog.bs.impl;

import org.springframework.stereotype.Service;
import venus.oa.loginlog.bs.ILoginLogBs;
import venus.oa.loginlog.dao.ILoginLogDao;
import venus.oa.loginlog.util.ILoginLogConstants;
import venus.oa.loginlog.vo.LoginLogVo;
import venus.oa.login.vo.LoginSessionVo;
import venus.oa.util.SqlBuilder;
import venus.frames.base.bs.BaseBusinessService;
import venus.pub.lang.OID;

import java.util.List;

@Service
public class LoginLogBs extends BaseBusinessService implements ILoginLogBs, ILoginLogConstants {
    
    private ILoginLogDao dao = null;

    public ILoginLogDao getDao() {
        return dao;
    }

    public void setDao(ILoginLogDao dao) {
        this.dao = dao;
    }

    /**
     * 插入单条记录
     * 
     * @param vo 用于添加的VO对象
     * @return 若添加成功，返回新生成的Oid
     */
    public OID insert(LoginLogVo vo) {
		OID oid = getDao().insert(vo);
        //RmLogHelper.log(TABLE_LOG_TYPE_NAME, "插入了1条记录,id=" + String.valueOf(oid));
		return oid;
    }

    /**
     * 删除单条记录
     * 
     * @param id 用于删除的记录的id
     * @return 成功删除的记录数
     */
    public int delete(String id) {
		int sum = getDao().delete(id);
		//RmLogHelper.log(TABLE_LOG_TYPE_NAME, "删除了" + sum + "条记录,id=" + String.valueOf(id));
		return sum;
    }

    /**
     * 删除多条记录
     * 
     * @param id 用于删除的记录的id
     * @return 成功删除的记录数
     */
    public int delete(String id[]) {
		int sum = getDao().delete(id);
        //RmLogHelper.log(TABLE_LOG_TYPE_NAME, "删除了" + sum + "条记录,id=" + String.valueOf(id));
		return sum;
    }
    
    /**
     * 删除全部记录
     * @return 成功删除的记录数
     */
    public int deleteAll() {
    	return getDao().deleteAll();
    }    

    /**
     * 根据Id进行查询
     * 
     * @param id 用于查找的id
     * @return 查询到的VO对象
     */
    public LoginLogVo find(String id) {
		LoginLogVo vo = getDao().find(id);
        //RmLogHelper.log(TABLE_LOG_TYPE_NAME, "察看了1条记录,id=" + id);
		return vo;
    }

    /**
     * 更新单条记录
     * 
     * @param vo 用于更新的VO对象
     * @return 成功更新的记录数
     */
    public int update(LoginLogVo vo) {
		int sum = getDao().update(vo);
        //RmLogHelper.log(TABLE_LOG_TYPE_NAME, "更新了" + sum + "条记录,id=" + String.valueOf(vo.getId()));
		return sum;
    }

    /**
     * 查询总记录数，带查询条件
     * 
     * @param queryCondition 查询条件
     * @return 总记录数
     */
    public int getRecordCount(String queryCondition,LoginSessionVo AuthorizedContext) {
		int sum = getDao().getRecordCount(queryCondition,AuthorizedContext);
		//RmLogHelper.log(TABLE_LOG_TYPE_NAME, "查询到了总记录数,sum=" + sum + ", queryCondition=" + queryCondition);
		return sum;
    }
    
    /**
     * 不带条件查询，也即查询获得所有的VO对象列表，不带翻页，默认排序
     *
     * @return 查询到的VO列表
     */
    public List queryByCondition() {
		List lResult = getDao().queryByCondition();
        //RmLogHelper.log(TABLE_LOG_TYPE_NAME, "不带条件查询了多条记录,recordSum=" + lResult.size());
		return lResult;
    }
    
    /**
     * 通过查询条件获得所有的VO对象列表，不带翻页查全部
     *
     * @param queryCondition 查询条件
     * @return 查询到的VO列表
     */
    public List queryByCondition(String queryCondition) {
		List lResult = getDao().queryByCondition(queryCondition);
        //RmLogHelper.log(TABLE_LOG_TYPE_NAME, "按条件查询了多条记录,recordSum=" + lResult.size() + ", queryCondition=" + queryCondition);
		return lResult;
    }
    
    /**
     * 通过查询条件获得所有的VO对象列表，不带翻页查全部，带排序字符
     *
     * @param queryCondition 查询条件
     * @param orderStr 排序字符
     * @return 查询到的VO列表
     */
    public List queryByCondition(String queryCondition, String orderStr) {
		List lResult = getDao().queryByCondition(queryCondition, orderStr);
		//RmLogHelper.log(TABLE_LOG_TYPE_NAME, "按条件查询了多条记录,recordSum=" + lResult.size() + ", queryCondition=" + queryCondition + ", orderStr=" + orderStr);
		return lResult;
    }
    
    /**
     * 通过查询条件获得所有的VO对象列表，带翻页
     * 
     * @param no 当前页数
     * @param size 每页记录数
     * @param queryCondition 查询条件
     * @return 查询到的VO列表
     */
    public List queryByCondition(int no, int size, String queryCondition,LoginSessionVo AuthorizedContext) {
		List lResult = getDao().queryByCondition(no, size, queryCondition,AuthorizedContext);
		//RmLogHelper.log(TABLE_LOG_TYPE_NAME, "按条件查询了多条记录,recordSum=" + lResult.size() + ", no=" + no + ", size=" + size + ", queryCondition=" + queryCondition);
		return lResult;
    }
    
    /**
     * 通过查询条件获得所有的VO对象列表，带翻页，带排序字符
     * 
     * @param no 当前页数
     * @param size 每页记录数
     * @param queryCondition 查询条件
     * @param orderStr 排序字符
     * @return 查询到的VO列表
     */
    public List queryByCondition(int no, int size, String queryCondition, String orderStr,LoginSessionVo AuthorizedContext) {
		List lResult = getDao().queryByCondition(no, size, queryCondition, orderStr,AuthorizedContext);
		//RmLogHelper.log(TABLE_LOG_TYPE_NAME, "按条件查询了多条记录,recordSum=" + lResult.size() + ", no=" + no + ", size=" + size + ", queryCondition=" + queryCondition + ", orderStr=" + orderStr);
		return lResult;
    }
    

    
    /**
     * 通用的方法，执行更新，返回更新的记录条数
     *
     * @param strsql 要执行的sql语句
     * @return 更新记录条数
     */
    public int doUpdate(String strsql) {
        int sum = getDao().doUpdate(strsql);
        //RmLogHelper.log(TABLE_LOG_TYPE_NAME, "执行一条update类的语句，返回sum=" + sum);
        return sum;
    }

	/* (non-Javadoc)
	 * @see venus.authority.login.loginlog.bs.ILoginLogBs#getRecordCount(venus.authority.util.SqlBuilder)
	 */
	public int getRecordCount(SqlBuilder sql) {
		return getDao().getRecordCount(sql);
	}

	public List queryByCondition(SqlBuilder sql) {
		return getDao().queryByCondition(sql);
	}
}

