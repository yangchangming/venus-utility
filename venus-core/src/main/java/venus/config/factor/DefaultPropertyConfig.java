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
package venus.config.factor;

import venus.config.Config;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * <p> Default Property Configuration </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2018-05-03 11:07
 */
public class DefaultPropertyConfig implements Config {

    private Map<String, String> data = new HashMap<String, String>();

    private String type;

    /**
     * add property pair to data, if no exist
     *
     * @param propertyPair
     */
    private void _refreshData(Map<String, String> propertyPair){
        if (propertyPair!=null && propertyPair.size()>0){
            Set<String> keys = propertyPair.keySet();
            for (String key : keys) {
                this.data.putIfAbsent(key, propertyPair.get(key));
            }
        }
    }

    public void refreshData(Object data) {
        if (data!=null && data instanceof Map){
            Map<String, String> propertyPair = (Map<String, String>)data;
            _refreshData(propertyPair);
        }
    }

    public Object getData() {
        return data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
