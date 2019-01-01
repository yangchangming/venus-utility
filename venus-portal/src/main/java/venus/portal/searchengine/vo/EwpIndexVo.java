package venus.portal.searchengine.vo;

import org.jsoup.Jsoup;
//import udp.searchengine.vo.DbIndexVo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qj-p
 *
 */
public class EwpIndexVo /* extends DbIndexVo */ {
    private String id;
    private String content;
    public String path;
    
    public void setContent(String content){
        this.content=content;
    }
    /* (non-Javadoc)
     * @see udp.searchengine.vo.DbIndexVo#getContents()
     */
    public List getContents() {
        List Contents=new ArrayList();
        
        Contents.add(Jsoup.parse(content).text());//屏蔽掉html的标签,获取文字信息
        return Contents;
    }

    public void setId(String id){
        this.id=id;
    }
    /* (non-Javadoc)
     * @see udp.searchengine.vo.DbIndexVo#getId()
     */
    public String getId() {
        return id;
    }

    /* (non-Javadoc)
     * @see udp.searchengine.vo.DbIndexVo#getPath()
     */
    public String getPath() {
        return path;
    }
    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

}
