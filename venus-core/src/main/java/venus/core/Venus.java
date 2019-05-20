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
package venus.core;

/**
 * <p> Venus - domain model for application </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2019-05-20 16:43
 */
public class Venus {

    private String env;

    private String type;

    private String name;

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String version(){
        return "0.1-SNAPSHOT";
    }

    public static Class<?> bootstrap(){
        // TODO: 19/5/20 获取启动类注解配置信息 @bootstrap
        return null;
    }

    public static String path(){
        return Thread.currentThread().getContextClassLoader().getResource("").getPath();
    }

    public static String osName(){
        return System.getProperties().get("os.name").toString();
    }

    public static String osArch(){
        return System.getProperties().get("os.arch").toString();
    }

    public static String javaVersion(){
        return System.getProperties().get("java.version").toString();
    }

    public static String timeZone(){
        return System.getProperties().get("user.timezone").toString();
    }

    public static String webContainer(){
        // TODO: 19/5/20 fetch web container info, if exist web container
        return "";
    }
}
