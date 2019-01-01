/*
 *  Copyright 2015-2018 DataVens, Inc.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package venus.datasource.oracle;

import venus.core.SpiMeta;
import venus.datasource.PaginationProvider;

/**
 * <p> Pagination for oracle </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2018-05-28 18:00
 */
@SpiMeta(name = "oracle")
public class OraclePaginationProvider implements PaginationProvider {

    public String getSql(String sql, int firstResult, int maxResult) {
        //0< X <21 ==1~20
        int low = firstResult;
        int up = firstResult + maxResult + 1;
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT * FROM(SELECT A.*, rownum as rid FROM( ").append(sql).append(") A WHERE rownum<"+up+") WHERE rid >" + low );
        //sb.append(") A)WHERE rid >" + low + " AND rid<" + up);
        return sb.toString();
    }
}
