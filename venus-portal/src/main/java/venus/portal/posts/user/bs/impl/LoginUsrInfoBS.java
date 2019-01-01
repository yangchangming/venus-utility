package venus.portal.posts.user.bs.impl;

import org.apache.commons.lang.ArrayUtils;
import venus.frames.base.bs.BaseBusinessService;
import venus.portal.posts.user.bs.ILoginUsrInfoBS;
import venus.portal.posts.user.dao.ILoginDao;
import venus.portal.posts.user.dao.IUsrInfoDao;
import venus.portal.posts.user.model.EwpLoginEntity;
import venus.portal.posts.user.model.EwpUsrInfoEntity;
import venus.portal.posts.user.util.ILoginUsrInfoConstants;

import java.util.List;

/**
 * Created by qj on 14-2-13.
 */
public class LoginUsrInfoBS extends BaseBusinessService implements ILoginUsrInfoBS, ILoginUsrInfoConstants {
    private ILoginDao loginDao = null;
    private IUsrInfoDao usrInfoDao = null;

    public ILoginDao getLoginDao() {
        return loginDao;
    }

    public void setLoginDao(ILoginDao loginDao) {
        this.loginDao = loginDao;
    }

    public IUsrInfoDao getUsrInfoDao() {
        return usrInfoDao;
    }

    public void setUsrInfoDao(IUsrInfoDao usrInfoDao) {
        this.usrInfoDao = usrInfoDao;
    }

    public void insert(EwpLoginEntity vo) {
        this.loginDao.insert(vo);
        EwpUsrInfoEntity euie = new EwpUsrInfoEntity();
        euie.setId(vo.getId());
        euie.setRealName(vo.getName());
        this.usrInfoDao.insert(euie);
    }

    public void insert(EwpLoginEntity login, EwpUsrInfoEntity info) {
        this.loginDao.insert(login);
        info.setId(login.getId());
        if (info.getRealName() == null) {
            info.setRealName(login.getName());
        }
        this.usrInfoDao.insert(info);
    }

    public int deleteMulti(String ids[]) throws Exception {
        if(ArrayUtils.isEmpty(ids)) {
            return 0;
        }
        for (String id : ids) {
            delete(findById(id));
        }
        return ids.length;
    }

    public void delete(EwpLoginEntity vo) {
        getLoginDao().delete(vo);
        this.usrInfoDao.delete(this.usrInfoDao.findById(vo.getId()));
    }

    public EwpLoginEntity findById(String id) {
        return getLoginDao().findById(id);
    }

    public EwpLoginEntity findByName(String name, String pwd) {
        return getLoginDao().findByName(name, pwd, LOGIC_TRUE);
    }

    public void update(EwpLoginEntity vo) {
        getLoginDao().update(vo);
    }

    public void resetPasswordByID(String id, String pwd) {
        EwpLoginEntity vo = this.findById(id);
        vo.setPwd(pwd);
        this.update(vo);
    }

    public int getRecordCount(String queryCondition) {
        return getLoginDao().getRecordCount(queryCondition);
    }

    /**
     * 通过查询条件获得所有的VO对象列表，带翻页，带排序字符
     *
     * @param no             当前页数
     * @param size           每页记录数
     * @param queryCondition 查询条件
     * @param orderStr       排序字符
     * @return 查询到的VO列表
     */
    public List queryByCondition(int no, int size, String queryCondition, String orderStr) {
        return getLoginDao().queryByCondition(no, size, queryCondition, orderStr);
    }

    public int getRecordCountUserInfoByCondition(String queryCondition) {
        return this.usrInfoDao.getRecordCount(queryCondition);
    }


    public List queryUserInfoByCondition(int no, int size, String queryCondition, String orderStr) {
        return this.usrInfoDao.queryByCondition(no, size, queryCondition, orderStr);
    }

    public EwpUsrInfoEntity findUserById(String id) {
        return getUsrInfoDao().findById(id);
    }

    public void updateUserInfo(EwpUsrInfoEntity userInfo) {
        getUsrInfoDao().update(userInfo);
    }

    public boolean isExistUser(String userName) {
        EwpLoginEntity entity = getLoginDao().findByName(userName);
        if (entity != null) {
            return true;
        }
        return false;
    }
}
