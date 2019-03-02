/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.oa.syserror.bs.impl;

import org.springframework.stereotype.Service;
import venus.oa.syserror.bs.ISysErrorBs;
import venus.oa.syserror.dao.ISysErrorDao;
import venus.oa.syserror.vo.SysErrorVo;
import venus.pub.lang.OID;

import java.util.List;

/**
 * @author zangjian
 *
 */
@Service
public class SysErrorBs implements ISysErrorBs {
    
    private ISysErrorDao dao;
    
    /**
     * @return the dao
     */
    public ISysErrorDao getDao() {
        return dao;
    }

    /**
     * @param dao the dao to set
     */
    public void setDao(ISysErrorDao dao) {
        this.dao = dao;
    }

    public int delete(SysErrorVo vo) {
        return getDao().delete(vo);
    }

    public int deleteAll() {
        return getDao().deleteAll();
    }

    public int getRecordCount() {
        return getDao().getRecordCount();
    }

    public int getRecordCount(String queryCondition) {
        return getDao().getRecordCount(queryCondition);
    }

    public OID insert(SysErrorVo vo) {
        return getDao().insert(vo);
    }

    public List queryByCondition(int no, int size, String queryCondition) {
        return getDao().queryByCondition(no, size, queryCondition);
    }

}

