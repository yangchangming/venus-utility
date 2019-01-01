/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.oa.authority.auproxy.bs;

import venus.oa.login.vo.LoginSessionVo;
import venus.pub.lang.OID;

/**
 * @author zangjian
 *
 */
public interface IAuProxyBs {

    /**
     * 关联用户时记录历史日志
     * @param partyId
     * @param parentRelId
     * @param relType
     * @param partyVo
     * @param vo
     * @return
     */
    public OID addRelation(String partyId, String parentRelId, String relType, LoginSessionVo vo);
    
    /**
     * 删除用户关联时记录历史日志
     * @param ids
     * @param partyVo
     * @param vo
     */
    public void deleteMulti(String ids[], LoginSessionVo vo);
    
}

