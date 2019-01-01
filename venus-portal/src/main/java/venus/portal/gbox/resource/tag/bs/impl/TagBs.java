
package venus.portal.gbox.resource.tag.bs.impl;

import venus.frames.base.bs.BaseBusinessService;
import venus.portal.gbox.resource.tag.bs.ITagBs;
import venus.portal.gbox.resource.tag.dao.ITagDao;
import venus.portal.gbox.resource.tag.util.ITagConstants;
import venus.portal.gbox.resource.tag.vo.TagVo;
import venus.pub.lang.OID;

import java.util.List;

public class TagBs extends BaseBusinessService implements ITagBs, ITagConstants {

    private ITagDao dao = null;

    /**
     * @return the dao
     */
    public ITagDao getDao() {
        return dao;
    }

    /**
     * @param dao the dao to set
     */
    public void setDao(ITagDao dao) {
        this.dao = dao;
    }
    
    /**
     * 插入单条记录
     * 
     * @param vo 用于添加的VO对象
     * @return 若添加成功，返回新生成的Oid
     */
    public OID insert(TagVo vo) {
        return getDao().insert(vo);
    }
    
    /**
     * 删除单条记录
     * 
     * @param id 用于删除的记录的id
     * @return 成功删除的记录数
     */
    public int delete(String id) {
        return getDao().delete(id); 
    }

    /**
     * 删除多条记录
     * 
     * @param id 用于删除的记录的id
     * @return 成功删除的记录数
     */
    public int delete(String id[]) {
        return getDao().delete(id);
    }

    /**
     * 根据name进行查询
     * 
     * @param name 用于查找的name
     * @return 查询到的VO对象
     */
    public TagVo find(String name) {
        return getDao().find(name);
    }

    /**
     * 更新单条记录
     * 
     * @param vo 用于更新的VO对象
     * @return 成功更新的记录数
     */
    public int update(TagVo vo) {
        return getDao().update(vo);
    }
    
    /**
     * 更新tag点击数
     * 
     * @param vo 用于更新的VO对象
     * @return 成功更新的记录数
     */
    public int updateClicks(TagVo vo) {
        return getDao().updateClicks(vo);
    }    

    /**
     * 查询总记录数，带查询条件
     * 
     * @param queryCondition 查询条件
     * @return 总记录数
     */
    public int getRecordCount(String queryCondition) {
        return getDao().getRecordCount(queryCondition);
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
    public List<TagVo> queryByCondition(String queryCondition) {
        return getDao().queryByCondition(queryCondition);
    }
}
