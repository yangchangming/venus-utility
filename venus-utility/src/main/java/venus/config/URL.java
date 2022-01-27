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
package venus.config;



import java.util.Map;

/**
 * <p> Configuration file URL </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2018-05-09 16:57
 */
public class URL {

    /**
     * config file type
     */
    private String type;

    /**
     * config file url, everywhere
     */
    private java.net.URL url;

    /**
     * Constructor
     *
     * @param url
     */
    public URL(java.net.URL url){
        this.url = url;
        if (url!=null){
            String path = url.getPath();
            if (path != null){
                String _path = path.toLowerCase();
                if (_path.endsWith("properties") || _path.endsWith(VenusConstants.CONFIG_TYPE_PROPERTY)){
                    this.type = VenusConstants.CONFIG_TYPE_PROPERTY;
                }
                if (_path.endsWith(VenusConstants.CONFIG_TYPE_XML)){
                    this.type = VenusConstants.CONFIG_TYPE_XML;
                }
                if (_path.endsWith(VenusConstants.CONFIG_TYPE_YAML)){
                    this.type = VenusConstants.CONFIG_TYPE_YAML;
                }
                if (_path.endsWith(VenusConstants.CONFIG_TYPE_ZOOKEEPER)){
                    this.type = VenusConstants.CONFIG_TYPE_ZOOKEEPER;
                }
                if (_path.endsWith(VenusConstants.CONFIG_TYPE_ANNOTATION)){
                    this.type = VenusConstants.CONFIG_TYPE_ANNOTATION;
                }
            }

        }
    }

    /**
     * Constructor
     *
     * @param url
     * @param type
     */
    public URL(java.net.URL url, String type){
        this.url = url;
        this.type = type;
    }


    private Map<String, Object> params;

    /**
     * If supported configuration files type
     *
     * @return
     */
    public boolean isValid(){
        if (this.type==null || "".equals(this.type.trim())){
            return false;
        }
        if (!VenusConstants.CONFIG_TYPE_ANNOTATION.equals(this.type) && !VenusConstants.CONFIG_TYPE_PROPERTY.equals(this.type)
                && !VenusConstants.CONFIG_TYPE_XML.equals(this.type) && !VenusConstants.CONFIG_TYPE_YAML.equals(this.type)
                && !VenusConstants.CONFIG_TYPE_ZOOKEEPER.equals(this.type)){
            return false;
        }
        return true;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public java.net.URL getUrl() {
        return url;
    }

    public void setUrl(java.net.URL url) {
        this.url = url;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
