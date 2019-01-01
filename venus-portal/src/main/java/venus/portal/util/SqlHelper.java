package venus.portal.util;

import org.apache.commons.lang3.StringUtils;
import venus.frames.base.action.IRequest;

import java.util.Iterator;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: qj
 * Date: 13-7-26
 * Time: 下午3:57
 * To change this template use File | Settings | File Templates.
 */
public class SqlHelper {

    public static String pushCondition(IRequest request, String pageRealValue, String thisField) {
        StringBuffer sf = new StringBuffer();
        if (null == thisField) {
            thisField = pageRealValue;
        }
        if (null == pageRealValue || "".equals(pageRealValue) || "".equals(request.getParameter(pageRealValue)) || null == request.getParameter(pageRealValue)) {
            return null;
        }
        sf.append(" " + thisField + " like '%" + request.getParameter(pageRealValue) + "%' ");
        return sf.toString();
    }

    public static String pushCondition(IRequest request, String pageRealValue) {
        return pushCondition(request, pageRealValue, null);
    }

    public static String build(String conditions[]) {
        StringBuffer queryCondition = new StringBuffer();
        boolean addFirstAnd = false;

        for (int i = 0; i < conditions.length; i++) {
            if (null != conditions[i]) {
                if (addFirstAnd) {
                    queryCondition.append(" and ");
                }
                addFirstAnd = true;
                queryCondition.append(conditions[i]);  //组装查询条件
                queryCondition.append(" ");
            }
        }
        return queryCondition.toString();
    }

    public static String build(Map<String, String> conditions) {
        StringBuffer queryCondition = new StringBuffer();
        boolean addFirstAnd = false;

        for (Iterator iter = conditions.entrySet().iterator(); iter.hasNext(); ) {
            Map.Entry<String, String> element = (Map.Entry<String, String>) iter.next();
            String strKey = element.getKey();
            String strObj = element.getValue();

            if (StringUtils.isNotEmpty(strKey) && StringUtils.isNotEmpty(strObj)) {
                if (addFirstAnd) {
                    queryCondition.append(" and ");
                }
                addFirstAnd = true;
                queryCondition.append(strKey);  //组装查询条件
                queryCondition.append("='");
                queryCondition.append(strObj);
                queryCondition.append("' ");
            }
        }
        return queryCondition.toString();
    }
}
