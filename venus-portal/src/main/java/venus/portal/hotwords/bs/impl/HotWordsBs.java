/*
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */

package venus.portal.hotwords.bs.impl;

import venus.frames.base.bs.BaseBusinessService;
import venus.portal.cache.data.DataCache;
import venus.portal.hotwords.bs.IHotWordsBs;
import venus.portal.hotwords.dao.IHotWordsDao;
import venus.portal.hotwords.model.HotWords;
import venus.portal.hotwords.util.IHotWordsConstants;

import java.util.List;

/**
 * @author chengliang
 */

public class HotWordsBs extends BaseBusinessService implements IHotWordsBs, IHotWordsConstants {

    /**
     * dao 表示: 数据访问层的实例
     */
    private IHotWordsDao dao = null;

    private DataCache dataCache;

    /**
     * 设置数据访问接口
     *
     * @return
     */
    public IHotWordsDao getDao() {
        return dao;
    }

    public void setDataCache(DataCache dataCache) {
        this.dataCache = dataCache;
    }

    /**
     * 获取数据访问接口
     *
     * @param dao
     */
    public void setDao(IHotWordsDao dao) {
        this.dao = dao;
    }

    /**
     * 插入单条记录
     *
     * @param vo 用于添加的VO对象
     */
    public void insert(HotWords vo) {
        vo.setEnableDate(vo.getCreateDate());
        dao.insert(vo);
        dataCache.refreshHotWords();
    }

    /**
     * 删除多条记录
     *
     * @param ids 用于删除的记录的id
     * @return 成功删除的记录数
     * @throws Exception
     */
    public int deleteMulti(String ids[]) throws Exception {
        for (String id : ids) {
            delete(findById(id));
        }
        dataCache.refreshHotWords();
        return 0;
    }

    /**
     * 删除单条记录
     *
     * @param vo 用于删除的VO对象
     */
    public void delete(HotWords vo) {
        dao.delete(vo);
        dataCache.refreshHotWords();
    }

    /**
     * 根据Id进行查询
     *
     * @param id 用于查找的id
     * @return 查询到的VO对象
     */
    public HotWords findById(String id) {
        return dao.findById(id);
    }

    /**
     * 更新单条记录
     *
     * @param vo 用于更新的VO对象
     * @return 成功更新的记录数
     */
    public void update(HotWords vo) {
        vo.setEnableDate(vo.getModifyDate());
        dao.update(vo);
        dataCache.refreshHotWords();
    }

    /**
     * 查询总记录数，带查询条件
     *
     * @param queryCondition 查询条件
     * @return 总记录数
     */
    public int getRecordCount(String queryCondition) {
        return dao.getRecordCount(queryCondition);
    }

    /**
     * 通过查询条件获得所有的VO对象列表，带翻页，带排序字符
     *
     * @param no             当前页数
     * @param size           每页记录数
     * @param queryCondition 查询条件
     * @param orderStr       排序字符
     * @return 查询到的VO列表
     */
    public List<HotWords> queryByCondition(int no, int size, String queryCondition, String orderStr) {
        return dao.queryByCondition(no, size, queryCondition, orderStr);
    }

    public List<HotWords> queryAll() {
        return dao.queryAll();
    }
}
