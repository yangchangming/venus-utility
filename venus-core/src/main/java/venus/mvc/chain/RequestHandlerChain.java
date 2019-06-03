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
package venus.mvc.chain;

import org.apache.log4j.Logger;
import venus.core.Context;
import venus.exception.VenusFrameworkException;
import venus.mvc.MvcContext;
import venus.mvc.bean.RequestHandlerWrapper;
import venus.mvc.render.DefaultRender;
import venus.mvc.render.InternalErrorRender;
import venus.mvc.render.Render;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> Request handler chain </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2019-05-30 11:37
 */
public class RequestHandlerChain {

    private static Logger logger = Logger.getLogger(RequestHandlerChain.class);
    private Context context;
    private List<Object> handlerWrapperList = new ArrayList<>(); //RequestHandlerWrapper
    private Render render;

    /**
     * Constructor
     *
     * @param handlerWrapperList
     */
    public RequestHandlerChain(List<Object> handlerWrapperList){
        this.handlerWrapperList = handlerWrapperList;
    }

    /**
     * Constructor
     *
     * @param context
     */
    public RequestHandlerChain(Context context){
        this.context = context;
    }

    /**
     * do handler chain
     */
    public void doNext(){
        try{
            for (Object handlerWrapper : handlerWrapperList) {
                if (handlerWrapper!=null && handlerWrapper instanceof RequestHandlerWrapper){
                    Object handler = ((RequestHandlerWrapper) handlerWrapper).getRequestHandler();
                    if(!((venus.mvc.handler.RequestHandler) handler).handle((MvcContext) context)){
                        break;
                    }
                }else{
                    logger.warn("Type of RequestHandler is diff, or null. [" + handlerWrapper.toString() + "]");
                }
            }
        }catch (Exception e){
            logger.error("Do request handler chain error. " + e.getMessage());
            render = new InternalErrorRender();
        }finally {
            if (render==null){
                render = new DefaultRender();
            }
            try {
                render.render((MvcContext) context);
            } catch (Exception e) {
                logger.error("Render failure. " + e.getMessage());
                throw new VenusFrameworkException("Render failure");
            }
        }
    }

    public void setHandlerWrapperList(List<Object> handlerWrapperList) {
        this.handlerWrapperList = handlerWrapperList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
