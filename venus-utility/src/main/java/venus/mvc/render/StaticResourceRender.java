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
package venus.mvc.render;

import venus.exception.VenusFrameworkException;
import venus.mvc.MvcContext;

import java.io.*;
import java.net.URL;

/**
 * <p> Static resource Render </p>
 * 1. 静态资源文件的处理，存在bug：图片文件返回不完成，流不完整，暂时废弃
 * 2. 所有静态资源处理用web容器的的DefaultServlet处理
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2019-06-03 16:40
 */
public class StaticResourceRender implements Render {

    @Override
    public void render(MvcContext context) {
        String pathInfo = context.getRequest().getPathInfo();
        String realAppBasePath = "";
        URL url = java.lang.Thread.currentThread().getContextClassLoader().getResource("".replace(".", "/"));
        if (url!=null) {
            realAppBasePath = url.getPath();
        }
        if (realAppBasePath!=null && !"".equals(realAppBasePath) && !realAppBasePath.endsWith("/")){
            realAppBasePath = realAppBasePath + "/";
        }

        InputStream fileInputStream = null;
        ByteArrayOutputStream resourceStream = null;
        PrintWriter writer = null;
        try {
            File resource = new File(realAppBasePath + pathInfo);
            if (!resource.exists() || !resource.isFile()){
                throw new RuntimeException();
            }

            context.getResponse().setCharacterEncoding("UTF-8");
            writer = context.getResponse().getWriter();

            fileInputStream = new FileInputStream(resource);
            resourceStream = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int temp = fileInputStream.read(bytes);
            while (temp!=-1){
                resourceStream.write(bytes, 0, temp);
                temp = fileInputStream.read(bytes);
            }
            resourceStream.flush();
        }catch (IOException e){
            throw new VenusFrameworkException("Render failure. [" + e.getMessage() + "]");
        }finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (resourceStream != null) {
                    resourceStream.close();
                }
            }catch (IOException e){
                throw new VenusFrameworkException(e.getCause().getMessage());
            }
            try {
                writer.write(new String(resourceStream.toByteArray(), "utf-8"));
                writer.flush();
            } catch (UnsupportedEncodingException e) {
                throw new VenusFrameworkException(e.getCause().getMessage());
            }
        }
    }
}
