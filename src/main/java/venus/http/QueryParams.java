package venus.http;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * Query parameters Value object
 */
@Data
@ToString
public class QueryParams implements Serializable {

    private static final long serialVersionUID = -4869594085374385813L;

    private int pageNum = 1;

    private int pageSize = 10;

    //排序字段
    private String field;

    //排序规则，asc升序，desc降序
    private String order;

}
