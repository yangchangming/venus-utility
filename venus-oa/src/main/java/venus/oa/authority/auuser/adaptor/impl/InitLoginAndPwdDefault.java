/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.oa.authority.auuser.adaptor.impl;

import venus.oa.authority.auuser.adaptor.InitLoginAndPwd;
import venus.oa.authority.auuser.bs.IAuUserBs;
import venus.oa.authority.auuser.util.IAuUserConstants;
import venus.oa.util.PinYinTools;
import venus.frames.mainframe.util.Helper;

/**
 * @author changming.Y <changming.yang.ah@gmail.com>
 *
 */
public class InitLoginAndPwdDefault implements InitLoginAndPwd {

    /* (non-Javadoc)
     * @see venus.authority.au.auuser.adaptor.InitLoginAndPwd#customLoginAndPwd(java.lang.String, java.lang.String)
     */
    public String[] customLoginAndPwd(String partyId, String partyName) {        
        String pinyinName = PinYinTools.getPingYin(partyName);
        String loginId = pinyinName;
        IAuUserBs bs = (IAuUserBs) Helper.getBean(IAuUserConstants.BS_KEY);
        int count = increaseLoginIdExt(bs,pinyinName,0);
        if(count!=0)
            loginId = pinyinName+count;
        return new String[]{loginId,loginId};
    }
    
    private int increaseLoginIdExt(IAuUserBs bs, String pinyinName, int count){
        boolean hasLoginId = bs.hasLoginId(pinyinName+((0==count)?"":String.valueOf(count)) );
        if(hasLoginId){
            return increaseLoginIdExt(bs,pinyinName,count+1);
        }
        return count;
    }
}
