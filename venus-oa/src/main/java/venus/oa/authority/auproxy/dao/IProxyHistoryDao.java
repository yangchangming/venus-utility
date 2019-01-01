/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.oa.authority.auproxy.dao;

import venus.oa.authority.auproxy.vo.ProxyHistoryVo;

import java.util.List;

/**
 * @author changming.Y <changming.yang.ah@gmail.com>
 *
 */
public interface IProxyHistoryDao {
    /**
     * 根据Id进行查询
     * 
     * @param id 用于查找的id
     * @return 查询到的VO对象
     */
    public ProxyHistoryVo find(String id);
    
    /**
     * 添加历史记录
     * 
     * @param vo 用于添加的VO对象
     */
    public String insert(ProxyHistoryVo vo);
    
    /**
     * 通过查询条件获得所有的VO对象列表，带翻页，带排序字符
     * 
     * @param no 当前页数
     * @param size 每页记录数
     * @param queryCondition 查询条件
     * @param orderStr 排序字符
     * @return 查询到的VO列表
     */
    public List queryByCondition(int no, int size, String queryCondition, String orderStr);
    
    /**
     * 查询总记录数，带查询条件
     * 
     * @param queryCondition 查询条件
     * @return 总记录数
     */
    public int getRecordCount(String queryCondition);

    /**
     * @param vo
     */
    public void update(ProxyHistoryVo vo);
}

