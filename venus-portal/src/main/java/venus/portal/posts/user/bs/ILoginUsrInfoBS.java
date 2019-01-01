package venus.portal.posts.user.bs;

import venus.portal.posts.user.model.EwpLoginEntity;
import venus.portal.posts.user.model.EwpUsrInfoEntity;

import java.util.List;

/**
 * Created by qj on 14-2-13.
 */
public interface ILoginUsrInfoBS {

    public void insert(EwpLoginEntity vo);

    public void insert(EwpLoginEntity login, EwpUsrInfoEntity info);

    public int deleteMulti(String ids[]) throws Exception;

    public void delete(EwpLoginEntity vo);

    public EwpLoginEntity findById(String id);

    public EwpLoginEntity findByName(String name, String pwd);

    public void update(EwpLoginEntity vo);

    public void resetPasswordByID(String id, String pwd);

    public int getRecordCount(String queryCondition);

    /**
     * 通过查询条件获得所有的VO对象列表，带翻页，带排序字符
     *
     * @param no             当前页数
     * @param size           每页记录数
     * @param queryCondition 查询条件
     * @param orderStr       排序字符
     * @return 查询到的VO列表
     */
    public List queryByCondition(int no, int size, String queryCondition, String orderStr);

    public int getRecordCountUserInfoByCondition(String queryCondition);

    public List queryUserInfoByCondition(int no, int size, String queryCondition, String orderStr);

    public EwpUsrInfoEntity findUserById(String id);

    public void updateUserInfo(EwpUsrInfoEntity userInfo);

    public boolean isExistUser(String userName);
}
