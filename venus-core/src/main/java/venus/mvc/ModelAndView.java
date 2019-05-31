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

import java.util.HashMap;
import java.util.Map;

/**
 * <p> Http value data for process </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2019-05-30 08:49
 */
public class ModelAndView {

    private String view;
    private Map<String, Object> model = new HashMap<>();

    public String getView() {
        return view;
    }

    public ModelAndView setView(String view) {
        this.view = view;
        return this;
    }

    public ModelAndView addObject(String attributeName, Object attributeValue) {
        model.put(attributeName, attributeValue);
        return this;
    }

    public ModelAndView addAllObjects(Map<String, ?> modelMap) {
        model.putAll(modelMap);
        return this;
    }

    public Map<String, Object> getModel() {
        return model;
    }
}
