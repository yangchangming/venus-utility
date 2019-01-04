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

import org.apache.log4j.Logger;
import venus.common.VenusConstants;
import venus.config.Config;
import venus.config.ConfigFactory;
import venus.core.ExtensionLoader;
import venus.core.SpiMeta;
import venus.datasource.ConnProvider;
import venus.datasource.DataSource;
import venus.exception.VenusFrameworkException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * <p> DataSource for oracle implements  </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2018-05-23 13:37
 */
@SpiMeta(name = "oracle")
public class DataSource4Oracle implements DataSource {

    private static final Logger logger = Logger.getLogger(DataSource4Oracle.class);

    public Connection getConnection() {
        List<Config> configs = ConfigFactory.fetchConfigByKeywords("datasource_oracle");
        if (configs!=null && configs.size()>0){
            // TODO: 18/5/22 multi same datasource, warning?
            Config config = configs.get(0);
            ConnProvider connProvider = ExtensionLoader.getExtensionLoader(ConnProvider.class).loadExtensionInstance(VenusConstants.DEFAULT_DATASOURCE_POOL_DRUID);
            try {
                return connProvider.getConnection(config);
            } catch (SQLException e) {
                logger.error("Failure for getting Connection!");
                throw new VenusFrameworkException(e.getMessage());
            }
        }
        return null;
    }
}