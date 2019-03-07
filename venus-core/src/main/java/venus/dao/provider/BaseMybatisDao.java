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
package venus.dao.provider;

import venus.core.SpiMeta;
import venus.dao.BaseDao;

/**
 * <p> Base mybatis dao provider, based on mybatis3 </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2019-03-06 17:16
 */
@SpiMeta(name = "mybatis")
public class BaseMybatisDao implements BaseDao {
}
