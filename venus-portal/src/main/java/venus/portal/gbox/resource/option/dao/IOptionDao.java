package venus.portal.gbox.resource.option.dao;

import venus.portal.gbox.resource.option.vo.OptionVo;

import java.util.List;

public interface IOptionDao {

    /**
     * 根据Id进行查询
     * 
     * @param id 用于查找的id
     * @return 查询到的VO对象
     */
    public OptionVo find(String id);
    
    
    /**
     * 更新单条记录
     * 
     * @param vo 用于更新的VO对象
     * @return 成功更新的记录数
     */
    public int update(OptionVo vo);
    
    /**
     * 通过查询条件获得所有的VO对象列表，带翻页，带排序字符
     * 
     * @return 查询到的VO列表
     */
    public List<OptionVo> queryAll();
}
