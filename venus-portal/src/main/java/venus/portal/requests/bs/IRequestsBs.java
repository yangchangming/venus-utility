package venus.portal.requests.bs;

import venus.portal.requests.dao.IRequestsDao;
import venus.portal.requests.model.Requests;

import java.util.List;

public interface IRequestsBs {

    public IRequestsDao getDao();

    public void setDao(IRequestsDao dao);

    public void insert(Requests vo);

    public int deleteMulti(String ids[]) throws Exception;
    
    public void delete(Requests vo);

    public Requests findById(String id);

    public void update(Requests vo);

    public int getRecordCount(String queryCondition);
    
    public List queryByCondition(int no, int size, String queryCondition, String orderStr);

}
