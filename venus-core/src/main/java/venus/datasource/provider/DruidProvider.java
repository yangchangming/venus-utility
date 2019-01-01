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
package venus.datasource.provider;

import com.alibaba.druid.pool.DruidDataSource;
import venus.config.Config;
import venus.config.factor.DefaultPropertyConfig;
import venus.core.SpiMeta;
import venus.datasource.ConnProvider;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

/**
 * <p> Druid connection pool provider </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2018-05-22 16:39
 */
@SpiMeta(name = "druid")
public class DruidProvider implements ConnProvider {

    private DruidDataSource druidDataSource;

    public Connection getConnection(Config config) throws SQLException {
        if (druidDataSource == null) {
            druidDataSource = new DruidDataSource();

            if (config!=null && config instanceof DefaultPropertyConfig){
                Map<String, String> data = (Map<String, String>)config.getData();

                druidDataSource.setDriverClassName(data.containsKey("drvName")?data.get("drvName"):"");
                druidDataSource.setUsername(data.containsKey("usrName")?data.get("usrName"):"");
                druidDataSource.setPassword(data.containsKey("pwd")?data.get("pwd"):"");
                druidDataSource.setUrl(data.containsKey("conUrl")?data.get("conUrl"):"");

                if (data.containsKey("maxWait") && data.get("maxWait")!=null && Long.parseLong(data.get("maxWait")) > 0) {
                    druidDataSource.setMaxWait(Long.parseLong(data.get("maxWait")));
                }
                if (data.containsKey("maxActive") && data.get("maxActive")!=null && Integer.parseInt(data.get("maxActive")) > 0) {
                    druidDataSource.setMaxActive(Integer.parseInt(data.get("maxActive")));
                }
                if (data.containsKey("initialSize") && data.get("initialSize")!=null && Integer.parseInt(data.get("initialSize")) > 0) {
                    druidDataSource.setInitialSize(Integer.parseInt(data.get("initialSize")));
                }
                if (data.containsKey("minActive") && data.get("minActive")!=null && Integer.parseInt(data.get("minActive")) > 0) {
                    druidDataSource.setMinIdle(Integer.parseInt(data.get("minActive")));
                }
                if (data.containsKey("retryCount") && data.get("retryCount")!=null && Integer.parseInt(data.get("retryCount")) > 0) {
                    druidDataSource.setConnectionErrorRetryAttempts(Integer.parseInt(data.get("retryCount")));
                }
                if (data.containsKey("timeBetweenEvictionRunsMillis") && data.get("timeBetweenEvictionRunsMillis")!=null && Long.parseLong(data.get("timeBetweenEvictionRunsMillis")) > 0) {
                    druidDataSource.setTimeBetweenEvictionRunsMillis(Long.parseLong(data.get("timeBetweenEvictionRunsMillis")));
                }
                if (data.containsKey("maxPoolPreparedStatementPerConnectionSize") && data.get("maxPoolPreparedStatementPerConnectionSize")!=null && Integer.parseInt(data.get("maxPoolPreparedStatementPerConnectionSize")) > 0) {
                    druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(Integer.parseInt(data.get("maxPoolPreparedStatementPerConnectionSize")));
                }

                if (data.containsKey("removeAbandoned")){
                    druidDataSource.setRemoveAbandoned(Boolean.parseBoolean(data.get("removeAbandoned")));
                }
                if (data.containsKey("logAbandoned")){
                    druidDataSource.setLogAbandoned(Boolean.parseBoolean(data.get("logAbandoned")));
                }
                if (data.containsKey("poolPreparedStatements")){
                    druidDataSource.setPoolPreparedStatements(Boolean.parseBoolean(data.get("poolPreparedStatements")));
                }
                if (data.containsKey("testOnBorrow")){
                    druidDataSource.setTestOnBorrow(Boolean.parseBoolean(data.get("testOnBorrow")));
                }
                if (data.containsKey("testOnReturn")){
                    druidDataSource.setTestOnReturn(Boolean.parseBoolean(data.get("testOnReturn")));
                }
                if (data.containsKey("testWhileIdle")){
                    druidDataSource.setTestWhileIdle(Boolean.parseBoolean(data.get("testWhileIdle")));
                }
            }
        }
        return druidDataSource.getConnection();
    }

    public void reset() {
    }
}
