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
package venus.oa.boot;

import com.weibo.api.motan.common.MotanConstants;
import com.weibo.api.motan.util.MotanSwitcherUtil;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import venus.init.InitializationFactory;

/**
 * <p> Venus OA server bootstrap </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2019-02-28 15:02
 */
@SpringBootApplication
//@ImportResource({"classpath:spring/applicationContext-motan.xml","classpath:spring/applicationContext.xml"})
public class VenusOABootstrap {

    private static final Logger logger = Logger.getLogger(VenusOABootstrap.class);

    public static void main(String[] args){
//      starting Venus framework
        InitializationFactory.init();

        SpringApplication app = new SpringApplication(VenusOABootstrap.class);
        app.setWebEnvironment(false);
        app.run(args);
        MotanSwitcherUtil.setSwitcherValue(MotanConstants.REGISTRY_HEARTBEAT_SWITCHER, true);

        venus.log.Logger.keyInfo(logger, "Venus OA server start success!");
    }

}
