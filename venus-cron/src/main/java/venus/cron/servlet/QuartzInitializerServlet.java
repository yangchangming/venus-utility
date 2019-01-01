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
package venus.cron.servlet;

import org.apache.log4j.Logger;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p> QuartzInitializerServlet </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2016-03-22
 */
public class QuartzInitializerServlet extends HttpServlet {

    private Logger logger = Logger.getLogger(QuartzInitializerServlet.class);

    public static final String QUARTZ_FACTORY_KEY = "org.quartz.impl.StdSchedulerFactory.KEY";

    private boolean performShutdown = true;

    private Scheduler scheduler = null;

    public void init(ServletConfig cfg) throws javax.servlet.ServletException {

        super.init(cfg);

        log("Quartz Initializer Servlet loaded, initializing Scheduler...");

        StdSchedulerFactory factory;
        try {
            //修改在上下文中的参数里取得配置文件路径
            String configFile = cfg.getServletContext().getRealPath(cfg.getInitParameter("config-file"));
            log("Load quartz config file:" + configFile);
            String shutdownPref = cfg.getInitParameter("shutdown-on-unload");
//            System.out.println("shutdownPref: " + shutdownPref);

            if (shutdownPref != null) {
                performShutdown = Boolean.valueOf(shutdownPref).booleanValue();
            }
            // get Properties
            if (configFile != null) {
                factory = new StdSchedulerFactory(configFile);
            } else {
                factory = new StdSchedulerFactory();
            }

            // Always want to get the scheduler, even if it isn't starting,
            // to make sure it is both initialized and registered.
            scheduler = factory.getScheduler();

            // Should the Scheduler being started now or later
            String startOnLoad = cfg
                    .getInitParameter("start-scheduler-on-load");
            /*
             * If the "start-scheduler-on-load" init-parameter is not specified,
             * the scheduler will be started. This is to maintain backwards
             * compatability.
             */
            if (startOnLoad == null || (Boolean.valueOf(startOnLoad).booleanValue())) {
                // Start now
                scheduler.start();
                log("Scheduler has been started...");
            } else {
                log("Scheduler has not been started. Use scheduler.start()");
            }

            String factoryKey = cfg.getInitParameter("servlet-context-factory-key");
            if (factoryKey == null) {
                factoryKey = QUARTZ_FACTORY_KEY;
            }

            log("Storing the Quartz Scheduler Factory in the servlet context at key: "
                    + factoryKey);
            cfg.getServletContext().setAttribute(factoryKey, factory);

        } catch (Exception e) {
            log("Quartz Scheduler failed to initialize: " + e.toString());
            throw new ServletException(e);
        }
    }

    public void destroy() {

        if (!performShutdown) {
            return;
        }

        try {
            if (scheduler != null) {
                scheduler.shutdown();
            }
        } catch (Exception e) {
            log("Quartz Scheduler failed to shutdown cleanly: " + e.toString());
            e.printStackTrace();
        }

        log("Quartz Scheduler successful shutdown.");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }


}

