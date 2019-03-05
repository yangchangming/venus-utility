package venus.oa.syserror.bs.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import venus.oa.syserror.bs.ISysErrorBs;
import venus.oa.syserror.dao.ISysErrorDao;
import venus.oa.syserror.vo.SysErrorVo;
import venus.pub.lang.OID;

import java.util.List;

/**
 * @author zangjian
 */
@Service
public class SysErrorBs implements ISysErrorBs {
    
    @Autowired
    private ISysErrorDao sysErrorDao;
    
    public int delete(SysErrorVo vo) {
        return sysErrorDao.delete(vo);
    }

    public int deleteAll() {
        return sysErrorDao.deleteAll();
    }

    public int getRecordCount() {
        return sysErrorDao.getRecordCount();
    }

    public int getRecordCount(String queryCondition) {
        return sysErrorDao.getRecordCount(queryCondition);
    }

    public OID insert(SysErrorVo vo) {
        return sysErrorDao.insert(vo);
    }

    public List queryByCondition(int no, int size, String queryCondition) {
        return sysErrorDao.queryByCondition(no, size, queryCondition);
    }

}

