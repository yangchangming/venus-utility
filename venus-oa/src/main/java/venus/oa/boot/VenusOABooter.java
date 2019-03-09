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

/**
 * <p> Venus oa server booter </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2018-08-23 16:50
 */
//@SpringBootApplication
//@ImportResource("classpath:spring/applicationContext.xml")
public class VenusOABooter {

    private static final Logger logger = Logger.getLogger(VenusOABooter.class);

    public static void main(String[] args){
        SpringApplication app = new SpringApplication(VenusOABooter.class);
        app.setWebEnvironment(false);
        app.run(args);
        MotanSwitcherUtil.setSwitcherValue(MotanConstants.REGISTRY_HEARTBEAT_SWITCHER, true);
        venus.log.Logger.keyInfo(logger, "Venus OA server start success!");
    }

    /**
     * 注解方式实现motan的配置，都会被扫描到
     * @return
     */
//    @Bean
//    public AnnotationBean motanAnnotationBean(){
//        AnnotationBean motanAnnotationBean = new AnnotationBean();
////        motanAnnotationBean.setPackage("venus.oa.test.impl");
//        return motanAnnotationBean;
//    }
//
//
//    @Bean(name = "venus-oa-motan")
//    public ProtocolConfigBean protocolConfig() {
//        ProtocolConfigBean config = new ProtocolConfigBean();
//        config.setDefault(true);
////        config.setName(MotanConstants.PROTOCOL_MOTAN);
//
//        // Injvm 协议是一个伪协议，它不开启端口，不发起远程调用，只在 JVM 内直接关联，但执行 Motan 的 Filter 链
//        config.setName(MotanConstants.PROTOCOL_INJVM);
//        config.setMaxContentLength(1048576);
//        config.setMaxServerConnection(80000);
//        config.setMaxWorkerThread(2000);
//        config.setMinWorkerThread(20);
//        return config;
//    }
//
//    @Bean(name = "registryConfig")
//    public RegistryConfigBean registryConfig() {
//        RegistryConfigBean config = new RegistryConfigBean();
//        config.setRegProtocol(MotanConstants.REGISTRY_PROTOCOL_LOCAL);
//        return config;
//    }
//
//    @Bean
//    public BasicServiceConfigBean baseServiceConfig() {
//        BasicServiceConfigBean config = new BasicServiceConfigBean();
//        config.setExport("venus-oa-motan:8002");
//        config.setGroup("venus-oa-group");
//        config.setAccessLog(true);
//        config.setShareChannel(true);
//        config.setModule("venus-oa-rpc");
//        config.setApplication("venus-oa-app");
//        config.setRegistry("registryConfig");
//        return config;
//    }


}
