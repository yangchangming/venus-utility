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
import org.apache.catalina.core.AprLifecycleListener;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardServer;
import org.apache.catalina.startup.Tomcat;
import org.apache.log4j.Logger;
import venus.exception.VenusFrameworkException;
import venus.mvc.DispatcherServlet;

import java.io.File;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URISyntaxException;

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
    private static int port = 9999;
    private static int shutdownPort = 9998;

    private TomcatBoot(){}

    private void init(){
        if (server!=null || starting){
            return;
        }
        try {
            this.server = new Tomcat();
            server.setBaseDir("");
            server.setPort(port);
            server.setHostname("localhost");


            server.getHost().setAppBase(".");
            StandardServer standardServer = (StandardServer) server.getServer();
            AprLifecycleListener listener = new AprLifecycleListener();
            standardServer.addLifecycleListener(listener);
            server.getServer().setPort(shutdownPort);

            StandardContext standardContext = new StandardContext();
            standardContext.setPath("/");           //contextPath
            standardContext.setDocBase("/");       //文件目录位置
            standardContext.addLifecycleListener(new Tomcat.DefaultWebXmlListener());
            standardContext.addLifecycleListener(new Tomcat.FixContextListener());
            standardContext.setSessionCookieName("venus-session");
            server.getHost().addChild(standardContext);

//            server.addServlet("/", "defaultServlet", new DefaultServlet()).setLoadOnStartup(0);
//            standardContext.addServletMapping("/*", "defaultServlet");

            server.addServlet("/", "dispatcherServlet", new DispatcherServlet()).setLoadOnStartup(1);
            standardContext.addServletMapping("/*", "dispatcherServlet");

//            StandardContext ctx = (StandardContext) server.addWebapp("", Path.fetchAppBase4Web());
//            ctx.setParentClassLoader(this.getClass().getClassLoader());
//            WebResourceRoot resources = new StandardRoot(ctx);
//            ctx.setResources(resources);
//            server.addServlet("", "dispatcherServlet", new DispatcherServlet()).setLoadOnStartup(0);
//            ctx.addServletMappingDecoded("/*", "dispatcherServlet");

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

    public static TomcatBoot of(int port){
        if (port>0){
            TomcatBoot.port = port;
        }
        return TomcatBoot.of();
    }

    public void start(){
        if (starting){
            logger.warn("Server has running.");
            return;
        }
        if (server==null){
            init();
        }
        try {
            server.start();
            String address = server.getServer().getAddress();
            int port = server.getConnector().getPort();
            venus.log.Logger.keyInfo(logger, "Tomcat is running on [http://"+ address + ":" + port +"]");
            server.getServer().await();
        } catch (LifecycleException e) {
            throw new VenusFrameworkException("Tomcat start failure." + e.getCause().getMessage());
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

    public void shutDown() throws Exception{
        Socket socket = new Socket("localhost", shutdownPort);
        OutputStream stream = socket.getOutputStream();
        for(int i = 0;i < "shutdown".length();i++){
            stream.write("shutdown".charAt(i));
        }
        stream.flush();
        stream.close();
        socket.close();
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
            venus.log.Logger.keyInfo(logger, "Tomcat application resolved root folder: ["+ root.getAbsolutePath() +"]");
            return root;
        } catch (URISyntaxException ex) {
            throw new VenusFrameworkException(ex.getMessage());
        }
    }

    public static void main(String[] args){
        TomcatBoot.of(8899).start();
    }
}
