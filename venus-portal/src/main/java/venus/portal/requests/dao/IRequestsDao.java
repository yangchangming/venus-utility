package venus.portal.requests.dao;

import venus.portal.requests.model.Requests;

import java.util.List;

public interface IRequestsDao {
    
    public void insert(Requests vo);

    public void delete(Requests vo);

     public Requests findById(String id);

    public void update(Requests vo);

    public int getRecordCount(String queryCondition);
    
    public List queryByCondition(int no, int size, String queryCondition, String orderStr);

}
