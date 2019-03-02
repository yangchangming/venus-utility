/*
 * 系统名称:PlatForm
 * 
 * 文件名称: venus.authority.au.auresource.bs.impl --> AuResourceBs.java
 * 
 * 功能描述:
 * 
 * 版本历史: 2006-06-09 15:32:17.1 创建1.0.0版 (甘硕)
 *  
 */

package venus.oa.authority.auresource.bs.impl;

import org.springframework.stereotype.Service;
import venus.oa.authority.auauthorize.dao.IAuAuthorizeDao;
import venus.oa.authority.auresource.bs.IAuResourceBs;
import venus.oa.authority.auresource.dao.IAuResourceDao;
import venus.oa.authority.auresource.util.IAuResourceConstants;
import venus.oa.authority.auresource.vo.AuResourceVo;
import venus.oa.util.GlobalConstants;
import venus.frames.base.bs.BaseBusinessService;
import venus.frames.mainframe.util.Helper;
import venus.pub.lang.OID;

import java.util.List;

/**
 * 功能、用途、现存BUG:
 * 
 * @author 甘硕
 * @version 1.0.0
 * @see 需要参见的其它类
 * @since 1.0.0
 */
@Service
public class AuResourceBs extends BaseBusinessService implements IAuResourceBs, IAuResourceConstants {

    /**
     * dao 表示: 数据访问层的实例
     */
    private IAuResourceDao dao = null;

    /**
     * 设置数据访问接口
     * 
     * @return
     */
    public IAuResourceDao getDao() {
        return dao;
    }

    /**
     * 获取数据访问接口
     * 
     * @param dao
     */
    public void setDao(IAuResourceDao dao) {
        this.dao = dao;
    }

    /**
     * 插入单条记录
     * 
     * @param vo 用于添加的VO对象
     * @return 若添加成功，返回新生成的Oid
     */
    public OID insert(AuResourceVo vo) {
        //判断名称是否存在
        //List list = getDao().queryPartyByNameAndResourceType(vo);
        //if (list.size() > 0) {
        //    throw new BaseApplicationException("名称重复，请重新编辑");
        //}
        if(GlobalConstants.getResType_fild().equals(vo.getResource_type())
                || GlobalConstants.getResType_recd().equals(vo.getResource_type())) {
            updateTableName(vo);
        }
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
        //获得IAuAuthorizeDao
        IAuAuthorizeDao auAuthorizeDao = (IAuAuthorizeDao) Helper.getBean("auauthorize_dao");
        //先删除授权情况(包括附加数据)
        auAuthorizeDao.deleteByResourceId(id);
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
        //获得IAuAuthorizeDao
        IAuAuthorizeDao auAuthorizeDao = (IAuAuthorizeDao) Helper.getBean("auauthorize_dao");
        //循环删除
        for (int i=0; i<id.length; i++) {
            //删除授权情况（包括附加数据）
            if(id[i]!=null && id[i].length()>0) {
                auAuthorizeDao.deleteByResourceId(id[i]);
            }
        }

        int sum = getDao().delete(id);
        //RmLogHelper.log(TABLE_LOG_TYPE_NAME, "删除了" + sum + "条记录,id=" + String.valueOf(id));
        return sum;
    }

    /**
     * 根据Id进行查询
     * 
     * @param id 用于查找的id
     * @return 查询到的VO对象
     */
    public AuResourceVo find(String id) {
        AuResourceVo vo = getDao().find(id);
        //RmLogHelper.log(TABLE_LOG_TYPE_NAME, "察看了1条记录,id=" + id);
        return vo;
    }

    /**
     * 更新单条记录
     * 
     * @param vo 用于更新的VO对象
     * @return 成功更新的记录数
     */
    public int update(AuResourceVo vo) {
        //判断名称是否存在
        /*
        AuResourceVo oldVo = find(vo.getId());
        if (oldVo.getName() != null) {
            if (!oldVo.getName().equals(vo.getName())) {
                List list = getDao().queryPartyByNameAndResourceType(vo);
                if (list.size() > 0) {
                    throw new BaseApplicationException("名称重复，请重新编辑");
                }
            }
        }*/
        if(GlobalConstants.getResType_fild().equals(vo.getResource_type()) 
                || GlobalConstants.getResType_recd().equals(vo.getResource_type())) {
            updateTableName(vo);
        }
        int sum = getDao().update(vo);
        //RmLogHelper.log(TABLE_LOG_TYPE_NAME, "更新了" + sum + "条记录,id=" + String.valueOf(vo.getId()));
        return sum;
    }
    /**
     * 更新所有表的table_chinesename
     * 
     * @param vo 用于更新的VO对象
     * @return 成功更新的记录数
     */
    private int updateTableName(AuResourceVo vo) {
       int sum = getDao().updateTableName(vo);
        //RmLogHelper.log(TABLE_LOG_TYPE_NAME, "更新了" + sum + "条记录,id=" + String.valueOf(vo.getId()));
        return sum;
    }


    /**
     * 查询所有的VO对象列表，不翻页
     * 
     * @return 查询到的VO列表
     */
    public List queryAll() {
        List lResult = getDao().queryAll();
        //RmLogHelper.log(TABLE_LOG_TYPE_NAME, "查询了多条记录,recordSum=" + lResult.size() + ", cmd=queryAll()");
        return lResult;
    }

    /**
     * 查询所有的VO对象列表，不翻页，带排序字符
     * 
     * @param orderStr 排序字符
     * @return 查询到的VO列表
     */
    public List queryAll(String orderStr) {
        List lResult = getDao().queryAll(orderStr);
        //RmLogHelper.log(TABLE_LOG_TYPE_NAME, "查询了多条记录,recordSum=" + lResult.size() + ", cmd=queryAll(" + orderStr +
        // ")");
        return lResult;
    }

    /**
     * 查询所有的VO对象列表，带翻页
     * 
     * @param no 当前页数
     * @param size 每页记录数
     * @return 查询到的VO列表
     */
    public List queryAll(int no, int size) {
        List lResult = getDao().queryAll(no, size);
        //RmLogHelper.log(TABLE_LOG_TYPE_NAME, "查询了多条记录,recordSum=" + lResult.size() + ",cmd=queryAll(" + no + ", " +
        // size + ")");
        return lResult;
    }

    /**
     * 查询所有的VO对象列表，带翻页，带排序字符
     * 
     * @param no 当前页数
     * @param size 每页记录数
     * @param orderStr 排序字符
     * @return 查询到的VO列表
     */
    public List queryAll(int no, int size, String orderStr) {
        List lResult = getDao().queryAll(no, size, orderStr);
        //RmLogHelper.log(TABLE_LOG_TYPE_NAME, "查询了多条记录,recordSum=" + lResult.size() + ", cmd=queryAll(" + no + ", " +
        // size + ", " + orderStr + ")");
        return lResult;
    }

    /**
     * 查询总记录数
     * 
     * @return 总记录数
     */
    public int getRecordCount() {
        int sum = getDao().getRecordCount();
        //RmLogHelper.log(TABLE_LOG_TYPE_NAME, "查询到了总记录数,sum=" + sum);
        return sum;
    }

    /**
     * 查询总记录数，带查询条件
     * 
     * @param queryCondition 查询条件
     * @return 总记录数
     */
    public int getRecordCount(String queryCondition) {
        int sum = getDao().getRecordCount(queryCondition);
        //RmLogHelper.log(TABLE_LOG_TYPE_NAME, "查询到了总记录数,sum=" + sum + ", queryCondition=" + queryCondition);
        return sum;
    }

    /**
     * 通过查询条件获得所有的VO对象列表，不带翻页查全部
     * 
     * @param queryCondition 查询条件
     * @return 查询到的VO列表
     */
    public List queryByCondition(String queryCondition) {
        List lResult = getDao().queryByCondition(queryCondition);
        //RmLogHelper.log(TABLE_LOG_TYPE_NAME, "按条件查询了多条记录,recordSum=" + lResult.size() + ", queryCondition=" +
        // queryCondition);
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
        //RmLogHelper.log(TABLE_LOG_TYPE_NAME, "按条件查询了多条记录,recordSum=" + lResult.size() + ", queryCondition=" +
        // queryCondition + ", orderStr=" + orderStr);
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
    public List queryByCondition(int no, int size, String queryCondition) {
        List lResult = getDao().queryByCondition(no, size, queryCondition);
        //RmLogHelper.log(TABLE_LOG_TYPE_NAME, "按条件查询了多条记录,recordSum=" + lResult.size() + ", no=" + no + ", size=" +
        // size + ", queryCondition=" + queryCondition);
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
    public List queryByCondition(int no, int size, String queryCondition, String orderStr) {
        List lResult = getDao().queryByCondition(no, size, queryCondition, orderStr);
        //RmLogHelper.log(TABLE_LOG_TYPE_NAME, "按条件查询了多条记录,recordSum=" + lResult.size() + ", no=" + no + ", size=" +
        // size + ", queryCondition=" + queryCondition + ", orderStr=" + orderStr);
        return lResult;
    }

    /**
     * 禁用资源
     * 
     * @param vo
     * @return
     */
    public boolean disable(AuResourceVo vo) {
        //获得IAuAuthorizeDao
        String id = vo.getId();
        IAuAuthorizeDao auAuthorizeDao = (IAuAuthorizeDao) Helper.getBean("auauthorize_dao");
        //删除授权情况(包括附加数据)
        auAuthorizeDao.deleteByResourceId(id);
        getDao().update(vo);
        return true;

    }

    /**
     * 通过资源名查询该资源的所有表名
     * 
     * @param queryCondition 资源名条件
     * @return 查询到的VO列表
     */
    public List queryAllTableName(String queryCondition) {
        return getDao().queryAllTableName(queryCondition);
    }

    /**
     * 通过资源名查询该表的所有列名
     * 
     * @param queryCondition 资源名条件
     * @return 查询到的VO列表
     */
    public List queryTableField(String  tableName,String queryCondition) {
        return getDao().queryTableField( tableName,queryCondition);
    }
}

