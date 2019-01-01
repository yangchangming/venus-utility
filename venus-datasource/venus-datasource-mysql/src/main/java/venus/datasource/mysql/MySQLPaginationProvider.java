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
package venus.datasource.mysql;

import venus.core.SpiMeta;
import venus.datasource.PaginationProvider;

/**
 * <p> Pagination for mysql </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2018-05-28 17:56
 */
@SpiMeta(name = "mysql")
public class MySQLPaginationProvider implements PaginationProvider {

    public String getSql(String sql, int firstResult, int maxResult) {
        if(firstResult < 0) {
            firstResult = 0;
        }
        StringBuffer sb = new StringBuffer();
        sb.append(sql).append(" limit ").append(firstResult).append(", ").append(maxResult);
        return sb.toString();
    }
}
