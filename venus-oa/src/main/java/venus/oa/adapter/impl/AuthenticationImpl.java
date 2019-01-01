package venus.oa.adapter.impl;

import venus.frames.mainframe.util.Helper;
import venus.oa.adapter.IAuthentication;
import venus.oa.adapter.ICredentials;
import venus.oa.authority.auuser.bs.IAuUserBs;
import venus.oa.authority.auuser.util.IAuUserConstants;

public final class AuthenticationImpl implements IAuthentication {

    public boolean authenticate(final ICredentials credentials) {
        IAuUserBs bs = (IAuUserBs) Helper.getBean(IAuUserConstants.BS_KEY);
        return 1==bs.authorize(credentials.getLoginId(), credentials.getPassword());
    }

}
