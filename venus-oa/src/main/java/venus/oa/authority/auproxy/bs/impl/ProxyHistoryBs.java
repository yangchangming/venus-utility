package venus.oa.authority.auproxy.bs.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import venus.oa.authority.auproxy.bs.IProxyHistoryBs;
import venus.oa.authority.auproxy.dao.IProxyHistoryDao;
import venus.oa.authority.auproxy.vo.ProxyHistoryVo;

import java.util.List;

/**
 * @author changming.Y <changming.yang.ah@gmail.com>
 */
@Service
public class ProxyHistoryBs implements IProxyHistoryBs {

    @Autowired
    private IProxyHistoryDao proxyHistoryDao;

    /* (non-Javadoc)
     * @see venus.authority.au.auproxy.bs.IProxyHistoryBs#find(java.lang.String)
     */
    public ProxyHistoryVo find(String id) {
        return proxyHistoryDao.find(id);
    }

    /* (non-Javadoc)
     * @see venus.authority.au.auproxy.bs.IProxyHistoryBs#getRecordCount(java.lang.String)
     */
    public int getRecordCount(String queryCondition) {
        return proxyHistoryDao.getRecordCount(queryCondition);
    }

    /* (non-Javadoc)
     * @see venus.authority.au.auproxy.bs.IProxyHistoryBs#insert(venus.authority.au.auproxy.vo.ProxyHistoryVo)
     */
    public String insert(ProxyHistoryVo vo) {
        return proxyHistoryDao.insert(vo);
    }

    /* (non-Javadoc)
     * @see venus.authority.au.auproxy.bs.IProxyHistoryBs#queryByCondition(int, int, java.lang.String, java.lang.String)
     */
    public List queryByCondition(int no, int size, String queryCondition,
            String orderStr) {
        return proxyHistoryDao.queryByCondition(no, size, queryCondition, orderStr);
    }

    /* (non-Javadoc)
     * @see venus.authority.au.auproxy.bs.IProxyHistoryBs#update(venus.authority.au.auproxy.vo.ProxyHistoryVo)
     */
    public void update(ProxyHistoryVo vo) {
        proxyHistoryDao.update(vo);
    }

}

