/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.oa.organization.relation.bs;

import venus.oa.login.vo.LoginSessionVo;

/**
 * @author zangjian
 *
 */
public interface IRelationBs {

    /**
     * 为团体添加关系
     * @param partyIds 团体ID数组
     * @param parentRelId 上一级关系ID
     * @param relType 关系类型
     */
    void addRelation(String partyIds[], String parentRelId, String relType, LoginSessionVo vo);
    
}

