package venus.portal.posts.posts.dao;

import venus.portal.posts.posts.model.EwpPostsEntity;

import java.util.List;

/**
 * Created by qj on 14-2-11.
 */
public interface IPostsDao {

    public void insert(EwpPostsEntity vo);

    public void delete(EwpPostsEntity vo);

    public EwpPostsEntity findById(String id);

    public void update(EwpPostsEntity vo);

    public int getRecordCount(String queryCondition);

    public List<EwpPostsEntity> queryByCondition(int no, int size, String queryCondition, String orderStr);

}
