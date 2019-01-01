package venus.portal.gbox.resource.resourcetype.bs.impl;

import venus.portal.gbox.resource.api.ResourceTypeAPI;
import venus.portal.gbox.resource.resourcetype.bs.IResourceTypeBs;
import venus.portal.gbox.resource.resourcetype.dao.IResourceTypeDao;
import venus.portal.gbox.resource.resourcetype.util.IResourceTypeConstants;
import venus.portal.gbox.resource.resourcetype.vo.ResourceTypeVo;
import venus.frames.base.bs.BaseBusinessService;
import venus.pub.lang.OID;

import java.util.List;

public class ResourceTypeBs extends BaseBusinessService implements IResourceTypeBs, IResourceTypeConstants {
    
    /**
     * dao 表示: 数据访问层的实例
     */
    private IResourceTypeDao dao = null;

    /**
     * 设置数据访问接口
     * 
     * @return
     */
    public IResourceTypeDao getDao() {
        return dao;
    }

    /**
     * 获取数据访问接口
     * 
     * @param dao
     */
    public void setDao(IResourceTypeDao dao) {
        this.dao = dao;
    }


    /**
     * 插入单条记录
     * 
     * @param vo 用于添加的VO对象
     * @return 若添加成功，返回新生成的Oid
     */
    public OID insert(ResourceTypeVo vo) {
		OID oid = getDao().insert(vo);
		ResourceTypeAPI.refresh();
		return oid;
    }

    /**
     * 删除多条记录
     * 
     * @param id 用于删除的记录的id
     * @return 成功删除的记录数
     */
    public int delete(String id[]) {
		int sum = getDao().delete(id);
		ResourceTypeAPI.refresh();
		return sum;
    }

    /**
     * 根据Id进行查询
     * 
     * @param id 用于查找的id
     * @return 查询到的VO对象
     */
    public ResourceTypeVo find(String id) {
		ResourceTypeVo vo = getDao().find(id);
		return vo;
    }

    /**
     * 更新单条记录
     * 
     * @param vo 用于更新的VO对象
     * @return 成功更新的记录数
     */
    public int update(ResourceTypeVo vo) {
		int sum = getDao().update(vo);
		ResourceTypeAPI.refresh();
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
		return sum;
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
    public List<ResourceTypeVo> queryByCondition(int no, int size, String queryCondition, String orderStr) {
		List<ResourceTypeVo> lResult = getDao().queryByCondition(no, size, queryCondition, orderStr);
		return lResult;
    }
    
    /**
     * 获得所有的VO对象列表
     */
    public List<ResourceTypeVo> queryAll() {
        return getDao().queryAll();
    }

}
