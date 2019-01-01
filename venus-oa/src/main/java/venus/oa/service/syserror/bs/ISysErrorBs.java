/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.oa.service.syserror.bs;

import venus.oa.service.syserror.vo.SysErrorVo;
import venus.pub.lang.OID;

import java.util.List;

/**
 * @author zangjian
 *
 */
public interface ISysErrorBs {

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
     * 根据条件分页查询错误数据列表
     * @param no
     * @param size
     * @param queryCondition
     * @return
     */
    public List queryByCondition(int no, int size, String queryCondition);

    /**
     * 插入数据导入时错误数据日志
     * @param vo
     * @return
     */
    public OID insert(final SysErrorVo vo);

    /**
     * 删除指定的错误数据
     * @param vo
     * @return
     */
    public int delete(SysErrorVo vo);

    /**
     * 删除全部错误数据
     * @return
     */
    public int deleteAll();    
    
}

