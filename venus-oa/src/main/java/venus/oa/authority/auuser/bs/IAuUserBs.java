/*
 * 系统名称:PlatForm
 * 
 * 文件名称: venus.authority.au.auuser.bs --> IAuUserBs.java
 * 
 * 功能描述:
 * 
 * 版本历史: 2006-06-09 15:32:04.478 创建1.0.0版 (甘硕)
 *  
 */

package venus.oa.authority.auuser.bs;

import venus.oa.authority.auuser.dao.IAuUserDao;
import venus.oa.authority.auuser.vo.AuUserVo;
import venus.pub.lang.OID;

import java.util.List;

/**
 * 功能、用途、现存BUG:
 * 
 * @author 甘硕
 * @version 1.0.0
 * @see 需要参见的其它类
 * @since 1.0.0
 */

public interface IAuUserBs {
    /**
     * 设置数据访问接口
     * 
     * @return
     */
    public IAuUserDao getDao();

    /**
     * 获取数据访问接口
     * 
     * @param dao
     */
    public void setDao(IAuUserDao dao);
    
    /**
     * 
     * 功能: 获取portal帐户信息
     *
     * @param portalUserId
     * @return
     */
    //public UserMappingEntry getPortalUserMap(String portalUserId);
    
    /**
     * 插入单条记录
     * 
     * @param vo 用于添加的VO对象
     * @return 若添加成功，返回新生成的Oid
     */
    public OID insert(AuUserVo vo);
    
    /**
     * 插入多条记录
     * @param codes
     * @return
     */
    public OID[] insertMulti(String[] codes) ;       
    
    /**
     * 插入单条记录，同时插入party表一条记录
     * 
     * @param vo 用于添加的VO对象
     * @return 若添加成功，返回新生成的Oid
     */
    public OID insert4party(AuUserVo vo);

    /**
     * 删除单条记录
     * 
     * @param id 用于删除的记录的id
     * @return 成功删除的记录数
     */
    public int delete(String id);

    /**
     * 删除多条记录
     * 
     * @param id 用于删除的记录的id
     * @return 成功删除的记录数
     */
    public int delete(String id[]);

    /**
     * 根据Id进行查询
     * 
     * @param id 用于查找的id
     * @return 查询到的VO对象
     */
    public AuUserVo find(String id);
    
    /**
     * 重置密码
     * @param id
     * @return
     */
    public int resetPassword(String id);
    /**
     * 启用
     * @param id
     * @return
     */
    public int enable(String id);
    /**
     * 禁用
     * @param id
     * @return
     */
    public int disable(String id);
    /**
     * 根据partyId进行查询
     * 
     * @param partyid 用于查找的partyid
     * @return 查询到的VO对象
     */
    public AuUserVo getByPartyId(String partyid);

    /**
     * 更新单条记录
     * 
     * @param vo 用于更新的VO对象
     * @return 成功更新的记录数
     */
    public int update(AuUserVo vo);
    
    /**
     * 更新单条记录，同时更新团体表相应的记录
     * 
     * @param vo 用于更新的VO对象
     * @return 成功更新的记录数
     */
    public int update4party(AuUserVo vo) ;

    /**
     * 查询所有的VO对象列表，不翻页
     * 
     * @return 查询到的VO列表
     */
    public List queryAll();
    
    /**
     * 查询所有的VO对象列表，不翻页，带排序字符
     * 
     * @param orderStr 排序字符
     * @return 查询到的VO列表
     */
    public List queryAll(String orderStr);

    /**
     * 查询所有的VO对象列表，带翻页
     * 
     * @param no 当前页数
     * @param size 每页记录数
     * @return 查询到的VO列表
     */
    public List queryAll(int no, int size);

    /**
     * 查询所有的VO对象列表，带翻页，带排序字符
     * 
     * @param no 当前页数
     * @param size 每页记录数
     * @param orderStr 排序字符
     * @return 查询到的VO列表
     */
    public List queryAll(int no, int size, String orderStr);

    /**
     * 查询总记录数
     * 
     * @return 总记录数
     */
    public int getRecordCount();

    /**
     * 查询总记录数，带查询条件
     * 
     * @param queryCondition 查询条件
     * @return 总记录数
     */
    public int getRecordCount(String queryCondition);
    
    /**
     * 通过查询条件获得所有的VO对象列表，不带翻页查全部
     *
     * @param queryCondition 查询条件
     * @return 查询到的VO列表
     */
    public List queryByCondition(String queryCondition);
    
    /**
     * 通过查询条件获得所有的VO对象列表，不带翻页查全部，带排序字符
     *
     * @param queryCondition 查询条件
     * @param orderStr 排序字符
     * @return 查询到的VO列表
     */
    public List queryByCondition(String queryCondition, String orderStr);
    
    /**
     * 通过查询条件获得所有的VO对象列表，带翻页
     * 
     * @param no 当前页数
     * @param size 每页记录数
     * @param queryCondition 查询条件
     * @return 查询到的VO列表
     */
    public List queryByCondition(int no, int size, String queryCondition);
    
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
     * 关联了组织机构，可以按公司或部门查
     * 
     * @param queryCondition 查询条件
     * @return 总记录数
     */
    public int getRecordCount4Org(String queryCondition);
    
    /**
     * 通过查询条件获得所有的VO对象列表，带翻页，带排序字符
     * 关联了组织机构，可以按公司或部门查
     * @param no
     * @param size
     * @param queryCondition
     * @param orderStr
     * @return
     */
    public List queryByCondition4Org(int no, int size, String queryCondition, String orderStr);
    
    /**
     * 查询总记录数，带查询条件，可以控制数据权限
     * 
     * @param queryCondition 查询条件
     * @return 总记录数
     */
    public int getRecordCount4Limit(String queryCondition);
    
    /**
     * 可以控制数据权限的查询
     * 
     * @param no 当前页数
     * @param size 每页记录数
     * @param queryCondition 查询条件
     * @return 查询到的VO列表
     */
    public List queryByCondition4Limit(int no, int size, String queryCondition); 
    
    /**
     * 账号是否存在
     * @param partyName
     * @return
     */
    public boolean hasLoginId(String loginId);
    
    /**
     * 安全认证
     * @param loginId 账户名称
     * @param passWord 密码(原文)
     * @return
     */
    public int authorize(String loginId, String passWord);
}

