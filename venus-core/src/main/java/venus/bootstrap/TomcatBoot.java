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
package venus.bootstrap;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.StandardRoot;
import org.apache.log4j.Logger;
import venus.exception.VenusFrameworkException;
import venus.mvc.DispatcherServlet;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Files;

/**
 * <p> Bootstrap as tomcat </p>
 * Usage: TomcatBoot.of()...
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2019-06-14 09:29
 */
public final class TomcatBoot {

    private static Logger logger = Logger.getLogger(TomcatBoot.class);
    private Tomcat server;
    private boolean starting = false;
    private static TomcatBoot instance = new TomcatBoot();

    private TomcatBoot(){
        init();
    }

    private void init(){
        if (server!=null || starting){
            return;
        }
        try {
            this.server = new Tomcat();
            server.setBaseDir("");
            server.setPort(9999);

            File root = getRootFolder();
            File webContentFolder = new File(root.getAbsolutePath(), "src/main/resources/");
            if (!webContentFolder.exists()) {
                webContentFolder = Files.createTempDirectory("venus-doc-base").toFile();
            }

            logger.info("Tomcat:configuring app with basedir: ["+ webContentFolder.getAbsolutePath() +"]");
            StandardContext ctx = (StandardContext) server.addWebapp("", webContentFolder.getAbsolutePath());
            ctx.setParentClassLoader(this.getClass().getClassLoader());
            WebResourceRoot resources = new StandardRoot(ctx);
            ctx.setResources(resources);
            server.addServlet("", "dispatcherServlet", new DispatcherServlet()).setLoadOnStartup(0);
            ctx.addServletMappingDecoded("/*", "dispatcherServlet");
        }catch (Exception e){
            throw new VenusFrameworkException(e.getMessage());
        }
    }

    public static TomcatBoot of(){
        if (instance==null){
            instance = new TomcatBoot();
        }
        return instance;
    }

    public void start(){
        if (starting){
            logger.warn("Server is running.");
            return;
        }
        if (server==null){
            init();
        }
        try {
            server.start();
            String address = server.getServer().getAddress();
            int port = server.getConnector().getPort();
//            logger.info("Tomcat is running on [http://"+ address + ":" + port +"]");
            venus.log.Logger.keyInfo(logger, "Tomcat is running on [http://"+ address + ":" + port +"]");

            server.getServer().await();
        } catch (LifecycleException e) {
            throw new VenusFrameworkException("Tomcat start failure." + e.getMessage());
        }
    }

    public void stop(){
        if (starting){
            try {
                server.stop();
            } catch (LifecycleException e) {
                throw new VenusFrameworkException("Tomcat stop failure." + e.getMessage());
            }
        }
    }

    private File getRootFolder() {
        try {
            File root;
            String runningJarPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath().replaceAll("\\\\", "/");
            int lastIndexOf = runningJarPath.lastIndexOf("/target/");
            if (lastIndexOf < 0) {
                root = new File("");
            } else {
                root = new File(runningJarPath.substring(0, lastIndexOf));
            }
            logger.info("Tomcat:application resolved root folder: ["+ root.getAbsolutePath() +"]");
            return root;
        } catch (URISyntaxException ex) {
            throw new VenusFrameworkException(ex.getMessage());
        }
    }

    public static void main(String[] args){
        TomcatBoot.of().start();
    }
}
