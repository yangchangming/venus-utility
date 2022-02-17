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
package venus.datasource;

import venus.core.Spi;

/**
 * <p> Pagination provider for database </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2018-05-28 17:54
 */
@Spi
public interface PaginationProvider {

    String PAGE_SQL_PROVIDER = "PAGE_SQL_PROVIDER";

    String getSql(String oldSqlStr, int firstResult, int maxResult);
}
