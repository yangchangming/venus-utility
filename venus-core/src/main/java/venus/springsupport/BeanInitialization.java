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
package venus.springsupport;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;
import venus.common.VenusConstants;
import venus.config.ConfigFactory;
import venus.core.SpiMeta;
import venus.datasource.GenericDataSource;
import venus.exception.VenusFrameworkException;
import venus.init.Initialization;
import venus.util.VenusPathUtil;

import javax.servlet.ServletContext;
import java.util.List;

/**
 * <p> Bean Initialization </p>
 * Initial all bean to spring application context, that is root application context for venus,
 * is not web application context, and U can read all beans from web app context that is build web-app by venus.
 *
 * Venus has two spring bean context, and one just no-web app, and another one is for web-app,
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2018-05-24 15:53
 */
@SpiMeta(name = "rootApplicationContext")
public class BeanInitialization implements Initialization {

    private static final Logger logger = Logger.getLogger(BeanInitialization.class);

    public void init() {
        try {
            ApplicationContext subContext = null;
            ApplicationContext rootApplicationContext = buildRootApplicationContext();
            if (VenusPathUtil.isAppEnv()) {
                subContext = buildFileSystemXmlApplicationContext(rootApplicationContext);
            }else if (VenusPathUtil.isWebEnv()){
                if (VenusPathUtil.getServletContext()==null){
                    logger.error("ServletContext must not be null!");
                    throw new VenusFrameworkException("ServletContext must not be null!");
                }
                subContext = buildXmlWebApplicationContext(VenusPathUtil.getServletContext(), rootApplicationContext);
            }
            if (subContext==null){
                subContext = rootApplicationContext;
            }
            BeanFactoryHelper.setBeanFactory(subContext);

            // TODO: 18/5/28 all bean initial has order? such as datasource bean must before other business bean?

            venus.log.Logger.keyInfo(logger, "RootApplicationContext initial Success![" + VenusPathUtil.getSystemEnv() + "]");

        }catch (Exception e){
            logger.error("Initial Root ApplicationContext failure. [" + e.getMessage() + "]");
            throw new VenusFrameworkException(e.getMessage());
        }
    }

    /**
     * Build Root ApplicationContext of Venus, just including datasource bean
     *
     * @return
     */
    protected ApplicationContext buildRootApplicationContext(){
        GenericApplicationContext rootContext = new GenericApplicationContext();
        ApplicationContext _rootContext = initDataSource(rootContext);
        if (_rootContext==null){
            throw new VenusFrameworkException("Failure for initial ApplicationContext of datasource!");
        }
        if (_rootContext instanceof GenericApplicationContext){
            ((GenericApplicationContext) _rootContext).refresh();
        }
        return _rootContext;
    }

    /**
     * build applicationContext, and not web environment
     *
     * @param parent
     * @return
     */
    protected ApplicationContext buildFileSystemXmlApplicationContext(ApplicationContext parent){
        List<String> _locations = ConfigFactory.fetchSpringConfigLocations();
        if (_locations != null && _locations.size() > 0) {
            String[] locations = new String[_locations.size()];
            for (int i = 0; i < _locations.size(); i++) {
                locations[i] = "/" + _locations.get(i);
            }
            FileSystemXmlApplicationContext sc = new FileSystemXmlApplicationContext(locations, parent);
            sc.refresh();
            return sc;
        }
        throw new VenusFrameworkException("No any spring configuration!");
    }

    /**
     * Build web ApplicationContext, and default spring configuration must will be /WEB-INF/applicationContext.xml
     * this web ApplicationContext must root in web environment
     *
     * @param servletContext
     * @param parent
     * @return
     */
    protected ApplicationContext buildWebApplicationContext(ServletContext servletContext, ApplicationContext parent){
        ContextLoader contextLoader = new ContextLoader();
        contextLoader.initWebApplicationContext(servletContext);
        ApplicationContext webApplicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
        if (webApplicationContext!=null && webApplicationContext instanceof ConfigurableWebApplicationContext){
            ConfigurableWebApplicationContext sc = (ConfigurableWebApplicationContext)webApplicationContext;
            sc.refresh();
        }else {
            throw new VenusFrameworkException("Web ApplicationContext type error![" + webApplicationContext.getDisplayName() + "]");
        }
        return webApplicationContext;
    }

    /**
     * Build web ApplicationContext, and y can specify the path of spring configuration
     *
     * @param servletContext
     * @param parent
     * @return
     */
    protected ApplicationContext buildXmlWebApplicationContext(ServletContext servletContext, ApplicationContext parent){
        XmlWebApplicationContext webApplicationContext = new XmlWebApplicationContext();
        webApplicationContext.setConfigLocations(fetchSpringConfigNames());
        webApplicationContext.setParent(parent);
        webApplicationContext.setServletContext(servletContext);
        webApplicationContext.refresh();
        configureAndRefreshWebApplicationContext(webApplicationContext, servletContext);
        return webApplicationContext;
    }

    private void configureAndRefreshWebApplicationContext(ApplicationContext applicationContext, ServletContext servletContext){
        servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, applicationContext);
    }

    /**
     * Spring configuration must location at "classpath:spring/"
     *
     * @return
     */
    private String[] fetchSpringConfigNames(){
        List<String> configNames = ConfigFactory.fetchSpringConfigNames();
        String[] _configNames = new String[configNames.size()];
        for(int i=0;i<configNames.size();i++){
            _configNames[i] = VenusConstants.CONFIG_SPRING_CLASSPATH_PREFIX + configNames.get(i);
        }
        return _configNames;
    }

    /**
     * Build bean of datasource into spring by code
     * including beans: dataSource | defaultLobHandler | nativeJdbcExtractor
     *
     * @param context
     * @return
     */
    private ApplicationContext initDataSource(ApplicationContext context){
        GenericApplicationContext _context = null;
        if (context!=null && context instanceof GenericApplicationContext){
            _context = (GenericApplicationContext)context;
        }else {
            logger.error("Illegal ApplicationContext type!");
            throw new VenusFrameworkException("Illegal ApplicationContext type");
        }

        BeanDefinition dataSourceBeanDefinition = BeanDefinitionBuilder.genericBeanDefinition(GenericDataSource.class).getBeanDefinition();
        if (!_context.containsBeanDefinition("dataSource")){
            _context.registerBeanDefinition("dataSource", dataSourceBeanDefinition);
        }

        BeanDefinition lobHandlerBeanDefinition = BeanDefinitionBuilder.
                genericBeanDefinition(org.springframework.jdbc.support.lob.DefaultLobHandler.class).getBeanDefinition();
        lobHandlerBeanDefinition.setLazyInit(true);
        if (!_context.containsBeanDefinition("defaultLobHandler")){
            _context.registerBeanDefinition("defaultLobHandler", lobHandlerBeanDefinition);
        }

        BeanDefinition nativeJdbcExtractorBeanDefinition = BeanDefinitionBuilder
                .genericBeanDefinition(org.springframework.jdbc.support.nativejdbc.SimpleNativeJdbcExtractor.class).getBeanDefinition();
        nativeJdbcExtractorBeanDefinition.setLazyInit(true);
        if (!_context.containsBeanDefinition("nativeJdbcExtractor")){
            _context.registerBeanDefinition("nativeJdbcExtractor", nativeJdbcExtractorBeanDefinition);
        }
        return _context;
    }

}
