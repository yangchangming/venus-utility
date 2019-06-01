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
package venus.mvc.bean;

/**
 * <p> Request handler wrapper </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2019-06-01 17:34
 */
public class RequestHandlerWrapper {

    private String value;

    private int order;

    private String type;

    private Object requestHandler;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getRequestHandler() {
        return requestHandler;
    }

    public void setRequestHandler(Object requestHandler) {
        this.requestHandler = requestHandler;
    }
}
