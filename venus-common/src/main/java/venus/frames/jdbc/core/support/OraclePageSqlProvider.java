/*
 * 创建日期 2005-9-26
 */
package venus.frames.jdbc.core.support;

import venus.frames.jdbc.core.IPageSqlProvider;

/**
 * @author wujun
 */
public class OraclePageSqlProvider implements IPageSqlProvider
{

    public String getSql(String oldSqlStr, int firstResult, int maxResult)
    {

        //0< X <21 ==1~20
        int low = firstResult;
        int up = firstResult + maxResult + 1;

        StringBuffer sb = new StringBuffer();
        sb.append("SELECT * FROM(SELECT A.*, rownum as rid FROM( ");
        sb.append(oldSqlStr);
        //sb.append(") A)WHERE rid >" + low + " AND rid<" + up);
        sb.append(") A WHERE rownum<"+up+") WHERE rid >" + low );

        return sb.toString();
    }

}