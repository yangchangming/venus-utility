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
package venus.common;


import java.io.Serializable;

/**
 * <p> Venus Global constants </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2018-05-04 00:11
 */
public interface VenusConstants extends Serializable {

    String CONFIG_TYPE_ANNOTATION = "annotation";
    String CONFIG_TYPE_PROPERTY = "property";
    String CONFIG_TYPE_XML = "xml";
    String CONFIG_TYPE_YAML = "yaml";
    String CONFIG_TYPE_ZOOKEEPER = "zookeeper";

    String DEFAULT_DATASOURCE_MYSQL = "mysql";
    String DEFAULT_DATASOURCE_POOL_DRUID = "druid";

    String CONFIG_SPRING_PREFIX = "spring";
    String CONFIG_APPLICATION_CONTEXT_PREFIX = "applicationContext";
    String CONFIG_EXTENSION_SEPARATOR = ".";
    String CONFIG_EXTENSION_XML = "xml";

    String CONFIG_SPRING_CLASSPATH_PREFIX = "classpath:spring/";


    String BEAN_ID_DATASOURCE = "dataSource";

    String SYSTEM_ENV_WEB = "web";
    String SYSTEM_ENV_APP = "app";


}
