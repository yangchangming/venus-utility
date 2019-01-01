package venus.oa.authority.auauthorize.bs;

import venus.oa.authority.auauthorize.vo.AuAuthorizeVo;
import venus.oa.login.vo.LoginSessionVo;

import java.util.List;
import java.util.Map;

/**
 * 团体类型BS
 * @author wumingqiang
 *
 */
public interface IAuAuthorizeBS {  
    /**
     *  添加
     * @param rvo
     * @return
     */
    public String insert(AuAuthorizeVo vo);

    /**
     * 更新
     * @param objVo
     * @return
     */
    public int update(AuAuthorizeVo vo);
    
    /**
     * 删除
     * @param id
     * @return
     */
    public int delete(String id);
    
    /**
     * 根据访问者ID删除授权情况（包括附加数据）
     * 
     * @param id
     * @return
     */
    public int deleteByVisitorId(String visitorId);

    /**
     * 根据资源ID删除授权情况（包括附加数据）
     * 
     * @param id
     * @return
     */
    public int deleteByResourceId(String resourceId);
	
    /**
     * 
     * 功能: 根据访问者ID和资源类型查询该访问者自身拥有的权限
     * 		如果资源类型为null，则查询全部资源类型的权限
     *
     * @param visitorId 访问者ID
     * @param resType 资源类型
     * @return
     */
    public Map getAuByVisitorId(String visitorId, String resType);
    
    /**
     * 
     * 功能: 根据访问者ID和资源类型查询该访问者自身拥有的数据权限
     *      剔除历史数据权限
     *
     * @param visitorId 访问者ID
     * @param resType 资源类型
     * @return
     */
    public Map getOrgAuByPartyIdWithOutHistory(String partyId, String resType);
    
    /**
     * 
     * 功能: 根据访问者ID和资源类型查询该访问者自身拥有的历史权限
     * 		如果资源类型为null，则查询全部资源类型的权限
     *
     * @param visitorId 访问者ID
     * @param resType 资源类型
     * @return
     */        
    public Map queryHistoryAuByVisitorId(String visitorId, String resType);    
           
    /**
     * 
     * 功能: 根据访问者Code数组和资源类型查询该访问者自身拥有的权限+在同一团体关系类型内它所继承的权限
     *		如果资源类型为null，则查询全部资源类型的权限
     *
     * @param visiCodes 访问者编号数组
     * @param resType 资源类型
     * @return
     */
    public Map getAuByVisitorCode(String visiCode, String resType);
    
    /**
     * 
     * 功能: 根据访问者Code数组和资源类型查询该访问者自身拥有的数据权限+在同一团体关系类型内它所继承的权限
     *      提出历史数据权限
     *
     * @param visiCodes 访问者编号数组
     * @param resType 资源类型
     * @return
     */
    public Map getOrgAuByVisitorCodeWithOutHistory(String visiCode, String resType);
    
    /**
     * 
     * 功能: 根据访问者Code数组和资源类型查询该访问者在同一团体关系类型内它所继承的权限
     *		如果资源类型为null，则查询全部资源类型的权限
     *
     * @param visiCodes 访问者编号数组
     * @param resType 资源类型
     * @return
     */
    public Map getExtendAuByVisitorCode(String visiCode, String resType);
    
    /**
     * 
     * 功能: 根据团体关系列表和资源类型查询该用户所拥有的权限，包括它继承的
     *
     * @param relList
     * @param sType
     * @return
     */
    public Map getAuByRelList(List relList, String sType);
    
    /**
     * 
     * 功能: 根据partyId、团体关系类型和资源类型查询该用户所拥有的权限，包括它继承的
     *
     * @param partyId
     * @param sType
     * @param relationTypeId
     * @return
     */
    public Map getAuByPartyId(String partyId, String sType, String relationTypeId);
    
    /**
     * 
     * 功能: 根据partyId和资源类型查询该用户所拥有的权限，包括它继承的
     *
     * @param partyId 用户partyId
     * @param sType 资源类型
     * @return
     */
    public Map getAuByPartyId(String partyId, String sType);
    
    /**
     * 
     * 功能:保存授权结果
     * 对当前授权情况和原先的授权情况进行比较，进行相应的新增、修改和删除操作
     *
     * @param vId 访问者ID
     * @param voList 当前授权情况vo列表
     * @param auType 资源类型
     * @return
     */
    public boolean saveAu(String vId, List voList, String auType);
    
    /**
     * 
     * 功能:保存授权结果
     * 对当前授权情况和原先的授权情况进行比较，进行相应的新增、修改和删除操作
     * @param vId 访问者ID
     * @param voList 当前授权情况vo列表
     * @param auType 资源类型
     * @param sessionVo 登录用户session
     * @return
     */
    public boolean saveAu(String vId, List voList, String auType, LoginSessionVo sessionVo);
    
    /**
     * 
     * 功能:分析并保存针对组织机构的授权结果
     * 对当前授权情况和原先的授权情况进行比较，进行相应的新增和删除操作
     *
     * @param vId 访问者ID
     * @param vCode 访问者编号
     * @param vType 访问者类型
     * @param addCodeArray 打勾的节点的编号数组
     * @param delCodeArray 取消打勾的节点的编号数组
     * @param sNames 资源名称数组
     * @param sTypes 资源类型数组
     * @return
     */
    public boolean saveOrgAu(String vId, String vCode, String vType,
                             String[] addCodeArray, String[] delCodeArray, String[] sNames, String[] sTypes);
    
    /**
     * 
     * 功能:分析并保存针对组织机构的授权结果
     * 对当前授权情况和原先的授权情况进行比较，进行相应的新增和删除操作
     *
     * @param vId 访问者ID
     * @param vCode 访问者编号
     * @param vType 访问者类型
     * @param addCodeArray 打勾的节点的编号数组
     * @param delCodeArray 取消打勾的节点的编号数组
     * @param sNames 资源名称数组
     * @param sTypes 资源类型数组
     * @param sessionVo 登录用户session
     * @return
     */
    public boolean saveOrgAu(String vId, String vCode, String vType,
                             String[] addCodeArray, String[] delCodeArray, String[] sNames, String[] sTypes, LoginSessionVo sessionVo);
    
    /**
     * 将用户转换为访问者
     * @param partyId
     * @return list 中为AuVisitorVo
     */
    public List parsePartyIdToVisitor(String partyId);
    
    /**
     * 功能: 根据资源Code查询对该资源拥有允许访问权限的人员partyId列表
     * @param resCode 资源编号
     * @return
     */
    public String[] getPartyIdByResourceCode(String resCode);
    
    /**
     * 功能: 根据访问者编号数组和团体类型获取相关的（自身及下属）partyid列表；
     * partyTypeId为可选参数，当为null或""时不起作用，将查询全部类型
     * @param visitorCode 访问者编号数组
     * @param partyTypeId 团体类型
     * @return
     */
    public List parseVisitorToPartyId(String visitorCode[], String partyTypeId);

    /**
     * 根据主键查找
     * @param id
     * @return
     */
    public AuAuthorizeVo find(String id);
    
    /**
     * 功能: 根据访问者编号数组获取相应的partyrelation表自身的code
     * @param visitorCode 访问者编号数组
     * @return
     */
    public List parseVisitorToRelCode(String visitorCode[]);
    
    /**
     * 
     * 功能: 在同一团体关系类型内，根据优先级过滤权限，并返回过滤结果
     *      优先级：拒绝>允许>未授权
     * @param auList
     * @return
     */
    public Map judgeAu4OneRelation(List auList);
    
    /**
     * 
     * 功能: 在不同团体关系类型内，根据优先级过滤权限，并返回过滤结果
     *      优先级：允许>未授权>拒绝
     * @param auList
     * @return
     */
    public Map judgeAu4DifRelation(List auList);
}

