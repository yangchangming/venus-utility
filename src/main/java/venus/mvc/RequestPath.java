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

/**
 * <p> Request path </p>
 * 1. generate a RequestPath instance for every http request
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2019-05-31 11:37
 */
public class RequestPath {

    private String httpMethod;
    private String path;
    private String basePath;
    private String methodPath;

    public RequestPath(String httpMethod, String path){
        this.httpMethod = httpMethod;
        this.path = path;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public String getMethodPath() {
        return methodPath;
    }

    public void setMethodPath(String methodPath) {
        this.methodPath = methodPath;
    }


    @Override
    public int hashCode() {
        if (this.path!=null){
            return this.path.hashCode();
        }else {
            return super.hashCode();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestPath that = (RequestPath) o;
        if (httpMethod != null ? !httpMethod.equals(that.httpMethod) : that.httpMethod != null) return false;
        if (path != null ? !path.equals(that.path) : that.path != null) return false;
        return methodPath != null ? methodPath.equals(that.methodPath) : that.methodPath == null;
    }
}
