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

import org.springframework.jdbc.datasource.AbstractDataSource;
import venus.common.VenusConstants;
import venus.core.ExtensionLoader;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * <p> Generic datasource for venus </p>
 * todo bind spring framework class, is suck?
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2018-05-02 10:51
 */
public class GenericDataSource extends AbstractDataSource {

    public Connection getConnection() throws SQLException {
        // TODO: 18/5/2 fetch the datasource by configuration or annotation or default
        DataSource dataSource = ExtensionLoader.getExtensionLoader(DataSource.class).loadExtensionInstance(VenusConstants.DEFAULT_DATASOURCE_MYSQL);
        return dataSource==null ? null : dataSource.getConnection();
    }

    public Connection getConnection(String username, String password) throws SQLException {
        return getConnection();
    }
}
