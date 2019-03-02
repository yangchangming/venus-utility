/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.oa.authority.auproxy.bs.impl;

import org.springframework.stereotype.Service;
import venus.oa.authority.auproxy.bs.IProxyHistoryBs;
import venus.oa.authority.auproxy.dao.IProxyHistoryDao;
import venus.oa.authority.auproxy.vo.ProxyHistoryVo;

import java.util.List;

/**
 * @author changming.Y <changming.yang.ah@gmail.com>
 *
 */
@Service
public class ProxyHistoryBs implements IProxyHistoryBs {
    
    /**
     * dao 表示: 数据访问层的实例
     */
    private IProxyHistoryDao dao = null;

    /* (non-Javadoc)
     * @see venus.authority.au.auproxy.bs.IProxyHistoryBs#find(java.lang.String)
     */
    public ProxyHistoryVo find(String id) {
        return dao.find(id);
    }

    /* (non-Javadoc)
     * @see venus.authority.au.auproxy.bs.IProxyHistoryBs#getRecordCount(java.lang.String)
     */
    public int getRecordCount(String queryCondition) {
        return dao.getRecordCount(queryCondition);
    }

    /* (non-Javadoc)
     * @see venus.authority.au.auproxy.bs.IProxyHistoryBs#insert(venus.authority.au.auproxy.vo.ProxyHistoryVo)
     */
    public String insert(ProxyHistoryVo vo) {
        return dao.insert(vo);
    }

    /* (non-Javadoc)
     * @see venus.authority.au.auproxy.bs.IProxyHistoryBs#queryByCondition(int, int, java.lang.String, java.lang.String)
     */
    public List queryByCondition(int no, int size, String queryCondition,
            String orderStr) {
        return dao.queryByCondition(no, size, queryCondition, orderStr);
    }

    /**
     * @param dao the dao to set
     */
    public void setDao(IProxyHistoryDao dao) {
        this.dao = dao;
    }

    /* (non-Javadoc)
     * @see venus.authority.au.auproxy.bs.IProxyHistoryBs#update(venus.authority.au.auproxy.vo.ProxyHistoryVo)
     */
    public void update(ProxyHistoryVo vo) {
        dao.update(vo);
    }

}

