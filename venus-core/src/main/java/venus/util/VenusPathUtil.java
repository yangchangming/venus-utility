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
package venus.util;

import javax.servlet.ServletContext;

/**
 * <p> Path and Environment of system utils </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2018-05-12 23:49
 */
public class VenusPathUtil {

    private static String ENVIRONMENT = VenusConstants.SYSTEM_ENV_APP;
    private static ServletContext servletContext = null;

    public static String getClassPath(){
        String classPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        return classPath;
    }

    public static void resetWebEnv(ServletContext servletContext){
        if (servletContext!=null){
            VenusPathUtil.servletContext = servletContext;
            VenusPathUtil.ENVIRONMENT = VenusConstants.SYSTEM_ENV_WEB;
        }
    }

    public static boolean isWebEnv(){
        return VenusConstants.SYSTEM_ENV_WEB.equals(getSystemEnv());
    }

    public static boolean isAppEnv(){
        return VenusConstants.SYSTEM_ENV_APP.equals(getSystemEnv());
    }

    public static String getSystemEnv(){
        return VenusPathUtil.ENVIRONMENT;
    }

    public static ServletContext getServletContext(){
        return VenusPathUtil.servletContext;
    }

    public static String getRealPath(){
        return null;
    }
}
