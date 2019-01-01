package venus.oa.authority.appenddata.dao;

import venus.oa.authority.appenddata.vo.AuAppendVo;

import java.util.List;

/**
 *  2008-9-26
 * @author changming.Y <changming.yang.ah@gmail.com>
 */
public interface IAuAppendDataDao {
	   /**
     * 根据id删除扩展授权情况
     * 
     * @param id
     * @return
     */
    public int delete(String id);
	 /**
     * 添加记录到附加权限表
     * 
     * @param vo
     * @return
     */
    public String insert(AuAppendVo vo);
    /**
     * 
     * 功能:获取访问者所有父级节点对某功能资源是否有指定数据权限的集合，待优化
     *
     * @param visiCodes 访问者编号数组
     * @param appendValue 数据权限
     * @param resouredId 资源ID
     * @return
     */
    public List queryAppendByVisitorCode(String[] visiCodes, String resourceId, String appendValue) ;
	/**
	 * 通过功能权限的权限ID来获取扩展表的所有数据权限
	 * @param authorizeId
	 * @return
	 */
	public List queryAppendByAuthorizeId(String authorizeId);
	/**
	 * 通过权限Id来删除扩展数据
	 * @param id 权限表主键
	 */
	public void deleteByAuthorizeId(String id); 
}

