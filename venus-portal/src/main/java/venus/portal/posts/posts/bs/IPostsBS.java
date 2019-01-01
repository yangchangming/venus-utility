package venus.portal.posts.posts.bs;

import venus.portal.posts.posts.model.EwpPostsEntity;

import java.util.List;

/**
 * Created by qj on 14-2-13.
 */
public interface IPostsBS {

    public void insert(EwpPostsEntity vo);

    public int deleteMulti(String ids[]) throws Exception;

    public void delete(EwpPostsEntity vo);

    public EwpPostsEntity findById(String id);

    public void update(EwpPostsEntity vo);

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
    public List<EwpPostsEntity> queryByCondition(int no, int size, String queryCondition, String orderStr);

    public List<EwpPostsEntity> queryByDocID(String docId);
}
