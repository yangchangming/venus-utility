package venus.portal.posts.user.dao;

import venus.portal.posts.user.model.EwpLoginEntity;

import java.util.List;

/**
 * Created by qj on 14-2-12.
 */
public interface ILoginDao {

    public void insert(EwpLoginEntity vo);

    public void delete(EwpLoginEntity vo);

    public EwpLoginEntity findById(String id);

    public EwpLoginEntity findByName(String name, String pwd, String enable);

    public EwpLoginEntity findByName(String name);

    public void update(EwpLoginEntity vo);

    public int getRecordCount(String queryCondition);

    public List queryByCondition(int no, int size, String queryCondition, String orderStr);

    public boolean checkUserNameUnique(String userName);
}
