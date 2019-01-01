package venus.portal.posts.user.dao;

import venus.portal.posts.user.model.EwpUsrInfoEntity;

import java.util.List;

/**
 * Created by qj on 14-2-12.
 */
public interface IUsrInfoDao {

    public void insert(EwpUsrInfoEntity vo);

    public void delete(EwpUsrInfoEntity vo);

    public EwpUsrInfoEntity findById(String id);

    public void update(EwpUsrInfoEntity vo);

    public int getRecordCount(String queryCondition);

    public List queryByCondition(int no, int size, String queryCondition, String orderStr);

}
