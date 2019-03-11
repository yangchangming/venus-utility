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
package venus.web.listener;

import com.weibo.api.motan.common.MotanConstants;
import com.weibo.api.motan.util.MotanSwitcherUtil;
import venus.init.InitializationFactory;
import venus.util.VenusPathUtil;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * <p> Default Listener for web container </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2018-05-29 14:16
 */
public class VenusListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        VenusPathUtil.resetWebEnv(servletContextEvent.getServletContext());
        InitializationFactory.init();

        if (!MotanSwitcherUtil.isOpen(MotanConstants.REGISTRY_HEARTBEAT_SWITCHER)){
            MotanSwitcherUtil.setSwitcherValue(MotanConstants.REGISTRY_HEARTBEAT_SWITCHER, true);
        }
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }
}
