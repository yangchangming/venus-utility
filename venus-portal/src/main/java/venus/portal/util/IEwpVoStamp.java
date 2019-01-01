package venus.portal.util;

/**
 *@author zhangrenyang 
 *@date  2011-9-28
 */
public interface IEwpVoStamp {
  //创建日期和IP
    public void setCreate_date(String create_date);

    public String getCreate_date();

    public void setCreate_ip(String create_ip);

    public String getCreate_ip();
    
    //启用/禁用日期和IP
    public void setEnable_date(String enable_date);

    public String getEnable_date();

    public void setEnable_ip(String enable_ip);

    public String getEnable_ip();
    
    //修改日期和IP
    public void setModify_date(String modify_date);

    public String getModify_date();

    public void setModify_ip(String modify_ip);

    public String getModify_ip();

    //生效日期
    public void setStart_date(String start_date);

    public String getStart_date();
    
    //失效日期
    public void setEnd_date(String end_date);

    public String getEnd_date();
}
