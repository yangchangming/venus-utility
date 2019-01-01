package venus.portal.gbox.resource.option.bs.impl;

import venus.portal.cache.data.DataCache;
import venus.portal.gbox.resource.api.OptionAPI;
import venus.portal.gbox.resource.option.bs.IOptionBs;
import venus.portal.gbox.resource.option.dao.IOptionDao;
import venus.portal.gbox.resource.option.util.IOptionConstants;
import venus.portal.gbox.resource.option.vo.OptionVo;
import venus.frames.base.bs.BaseBusinessService;

import java.util.List;

public class OptionBs extends BaseBusinessService implements IOptionBs, IOptionConstants {

    IOptionDao dao = null;

    private DataCache dataCache;

    /**
     * @return the dao
     */
    public IOptionDao getDao() {
        return dao;
    }

    /**
     * @param dao the dao to set
     */
    public void setDao(IOptionDao dao) {
        this.dao = dao;
    }

    public void setDataCache(DataCache dataCache) {
        this.dataCache = dataCache;
    }

    public DataCache getDataCache() {
        return dataCache;
    }

    /**
     * 根据Id进行查询
     *
     * @param id 用于查找的id
     * @return 查询到的VO对象
     */
    public OptionVo find(String id) {
        return getDao().find(id);
    }

    /**
     * 更新单条记录
     *
     * @param vo 用于更新的VO对象
     * @return 成功更新的记录数
     */
    public int update(OptionVo vo) {
        int sum = getDao().update(vo);
        OptionAPI.refresh();
        dataCache.refreshSysOption();
        return sum;
    }

    /**
     * 通过查询条件获得所有的VO对象列表，带翻页，带排序字符
     *
     * @return 查询到的VO列表
     */
    public List<OptionVo> queryAll() {
        return getDao().queryAll();
    }
}
