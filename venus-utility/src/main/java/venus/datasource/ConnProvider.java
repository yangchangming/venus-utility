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

import venus.config.Config;
import venus.core.Spi;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * <p> DataSource connection pool provider definition </p>
 * 1. User different connection pool to different kinds of datasource
 * 2. Some connection pool used by some datasource is be defined inner datasource implements
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2018-05-22 16:19
 */
@Spi
public interface ConnProvider {

    Connection getConnection(Config config) throws SQLException;

    void reset();
}
