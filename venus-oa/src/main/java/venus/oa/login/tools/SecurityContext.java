/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.oa.login.tools;

import venus.oa.helper.LoginHelper;
import venus.oa.login.vo.LoginSessionVo;
import venus.frames.base.action.IRequest;
import venus.frames.i18n.util.LocaleHolder;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Map;

/**
 * 只读 的安全上下文
 * @author changming.Y <changming.yang.ah@gmail.com>
 *
 */
public interface SecurityContext {
    /**
     * @return 租户ID
     */
    public String getTenantId();
    /**
     * @return 租户名称
     */
    public String getTenantName();
    /**
     * @return 登陆名称
     */
    public String getLoginName();
    /**
     * @return 唯一标示
     */
    public String getPartyId();
    /**
     * @return 关系编码
     */
    public String getRelationCode();
    /**
     * @return 姓名
     */
    public String getName();
    /**
     * @return 租户的策略
     */
    public Map getTenantStrategy();
    /**
     * @return 语言
     */
    public String getLanguage();
    /**
     * @return 访问信息
     */
    public String getBrowseURL();
    
    //TODO 安全上下文可借鉴开源的实现
    public static final class Security implements SecurityContext {
        
        private String browseURL;
        private String language;        
        private String loginName;
        private String name;
        private String partyId;
        private String relationCode;
        private String tenantId;
        private String tenantName;
        private Map tenantStrategy;
        
        private Security(String browseURL,String language,String loginName,String name,String partyId,String relationCode,String tenantId,String tenantName,Map tenantStrategy){
            this.browseURL=browseURL;
            this.language=language;        
            this.loginName=loginName;
            this.name=name;
            this.partyId=partyId;
            this.relationCode=relationCode;
            this.tenantId=tenantId;
            this.tenantName=tenantName;
            this.tenantStrategy = tenantStrategy;
        }
        
        public static Security born(IRequest request){
            String browseURL = request.getActionPath();
            String language = LocaleHolder.getLocale().getLanguage();
            LoginSessionVo sessionVo= LoginHelper.getLoginVo((HttpServletRequest) request.getServletRequest());
            String loginName = sessionVo.getLogin_id();
            String name = sessionVo.getName();
            String partyId = sessionVo.getParty_id();
            String relationCode = sessionVo.getCurrent_code();
            String tenantId = null;
            String tenantName = null;
            Map tenantStrategy = Collections.emptyMap();
            return new Security(browseURL,language,loginName,name,partyId,relationCode,tenantId,tenantName,tenantStrategy);
        }

        /* (non-Javadoc)
         * @see venus.authority.login.tools.SecurityContext#getBrowseURL()
         */
        public String getBrowseURL() {
            return browseURL;
        }

        /* (non-Javadoc)
         * @see venus.authority.login.tools.SecurityContext#getLanguage()
         */
        public String getLanguage() {
            return language;
        }

        /* (non-Javadoc)
         * @see venus.authority.login.tools.SecurityContext#getLoginName()
         */
        public String getLoginName() {
            return loginName;
        }

        /* (non-Javadoc)
         * @see venus.authority.login.tools.SecurityContext#getName()
         */
        public String getName() {
            return name;
        }

        /* (non-Javadoc)
         * @see venus.authority.login.tools.SecurityContext#getPartyId()
         */
        public String getPartyId() {
            return partyId;
        }

        /* (non-Javadoc)
         * @see venus.authority.login.tools.SecurityContext#getRelationCode()
         */
        public String getRelationCode() {
            return relationCode;
        }

        /* (non-Javadoc)
         * @see venus.authority.login.tools.SecurityContext#getTenantId()
         */
        public String getTenantId() {
            return tenantId;
        }

        /* (non-Javadoc)
         * @see venus.authority.login.tools.SecurityContext#getTenantName()
         */
        public String getTenantName() {
            return tenantName;
        }

        /* (non-Javadoc)
         * @see venus.authority.login.tools.SecurityContext#getTenantStrategy()
         */
        public Map getTenantStrategy() {
            return tenantStrategy;
        }
        
    }
}
