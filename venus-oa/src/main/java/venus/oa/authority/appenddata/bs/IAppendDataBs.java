/*
 * 创建日期 2008-9-26
 */
package venus.oa.authority.appenddata.bs;

import java.util.Map;

/**
 *  2008-9-26
 * @author changming.Y <changming.yang.ah@gmail.com>
 */
public interface IAppendDataBs {
    /**
     * 功能:分析并保存针对功能权限基础上的组织机构的授权结果
     * 对当前授权情况和原先的授权情况进行比较，进行相应的新增和删除操作
     *
     * @param vId 访问者ID
     * @param vCode 访问者编号
     * @param vType 访问者类型
     * @param addCodeArray 打勾的节点的编号数组
     * @param delCodeArray 取消打勾的节点的编号数组
     * @param sNames 资源名称数组
     * @param sTypes 资源类型数组
     * @param authorizeId 功能权限ID
     * @return
     */
    public boolean saveFunOrgAu(String vId, String vCode, String vType,
								String[] addCodeArray, String[] delCodeArray, String[] sNames, String[] sTypes, String authorizeId);
	/**
	 * 功能数据权限的函数：通过功能权限的权限ID来获取扩展表的所有数据权限
	 * @param authorizeId 权限ID
	 * @return
	 */
	public Map getAppendByAuthorizeId(String authorizeId);

   /**
    * 功能: 获取访问者所有父级节点对某功能资源是否有指定数据权限的集合，待优化
	 * @param visiCode 访问者编码
	 * @param resourceId 资源ID
	 * @param appendValue 附加数据值
	 * @return
	 */
	public Map getExtendAppendAuByVisitorCode(String visiCode[], String resourceId, String appendValue);
	
	/**
	 * 功能: 根据团体ID、功能资源ID和数据CODE来获取团体及团体所有父级节点的功能数据权限集合
	 * @param partyId
	 * @return
	 */
	public Map getExtendAppendAuByPartyId(String partyId, String resourceId, String appendValue);
	/**
	 * 功能: 根据团体ID、功能资源ID来获取团体及团体所有父级节点的功能数据权限集合
	 * @param party_id
	 * @param resource_id
	 * @return
	 */
	public Map getExtendAppendAuByPartyId(String party_id, String resource_id);
	
	/**
	 * 通过权限Id来删除扩展表的扩展数据，该功能为功能数据删除功能菜单时提供扩展数据的级联删除功能
	 * @param id 权限表的主键
	 */
	public void deleteByAuthorizeId(String id);
}

