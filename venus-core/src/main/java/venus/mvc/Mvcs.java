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
package venus.mvc;

import venus.core.Context;

/**
 * <p> MVC util </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2019-05-31 12:24
 */
public class Mvcs {

    /**
     * build RequestPath instance by http request
     *
     * @param context
     * @return
     */
    public static RequestPath buildPath(Context context){
        if (context!=null && context instanceof MvcContext){
            String path = ((MvcContext)context).getRequest().getPathInfo();
            if (path!=null && path.endsWith("/")){
                path = path.substring(0, path.length()-1);
            }
            String httpMethod = ((MvcContext)context).getRequest().getMethod();
            RequestPath requestPath = new RequestPath(httpMethod, path);
            if (requestPath!=null){
                ((MvcContext)context).setRequestPath(requestPath);
            }
            return requestPath;
        }else {
            return null;
        }
    }







}
