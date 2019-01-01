package venus.portal.posts.posts.bs.impl;

import venus.frames.base.bs.BaseBusinessService;
import venus.portal.posts.posts.bs.IPostsBS;
import venus.portal.posts.posts.dao.IPostsDao;
import venus.portal.posts.posts.model.EwpPostsEntity;
import venus.portal.posts.posts.util.IPostsConstants;

import java.util.List;

/**
 * Created by qj on 14-2-13.
 */
public class PostsBS extends BaseBusinessService implements IPostsBS, IPostsConstants {
    private IPostsDao dao = null;

    public IPostsDao getDao() {
        return dao;
    }

    public void setDao(IPostsDao dao) {
        this.dao = dao;
    }

    public void insert(EwpPostsEntity vo) {
        getDao().insert(vo);
    }

    public int deleteMulti(String ids[]) throws Exception {
        for (String id : ids) {
            delete(findById(id));
        }
        return 0;
    }

    public void delete(EwpPostsEntity vo) {
        getDao().delete(vo);
    }

    public EwpPostsEntity findById(String id) {
        return getDao().findById(id);
    }

    public void update(EwpPostsEntity vo) {
        getDao().update(vo);
    }

    public int getRecordCount(String queryCondition) {
        return getDao().getRecordCount(queryCondition);
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
    public List<EwpPostsEntity> queryByCondition(int no, int size, String queryCondition, String orderStr) {
        return getDao().queryByCondition(no, size, queryCondition, orderStr);
    }

    public List<EwpPostsEntity> queryByDocID(String docid) {
        return getDao().queryByCondition(0, 1000, " docId = '" + docid + "' ", "pubdate");
    }

}
