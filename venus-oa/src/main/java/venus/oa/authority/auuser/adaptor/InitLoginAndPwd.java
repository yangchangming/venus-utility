/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.oa.authority.auuser.adaptor;

/**
 * @author changming.Y <changming.yang.ah@gmail.com>
 *
 */
public interface InitLoginAndPwd {
    /*
     * 通过partyid或名称，有项目组自定义账户名和密码
     */
    public String[] customLoginAndPwd(String partyId, String partyName);
}
