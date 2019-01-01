package venus.portal.cache.plugin;

import venus.frames.base.action.DefaultServlet;
import venus.frames.base.action.DefaultServletException;
import venus.frames.base.action.IRequest;
import venus.frames.base.action.IResponse;
import venus.frames.base.action.plugin.IServletPlugin;
import venus.frames.mainframe.util.Helper;
import venus.portal.cache.data.DataLoader;

/**
 * EWP应用缓存初始化。
 */
public class InitEwpCachePlugin implements IServletPlugin {

    private DataLoader dataLoader;

    /* (non-Javadoc)
     * @see venus.frames.base.action.plugin.IServletPlugin#init(venus.frames.base.action.DefaultServlet)
     */
    public void init(DefaultServlet ds) throws DefaultServletException {
        this.dataLoader = (DataLoader) Helper.getBean("dataLoader");
        this.dataLoader.init();
    }

    /* (non-Javadoc)
     * @see venus.frames.base.action.plugin.IServletPlugin#service(venus.frames.base.action.DefaultServlet, venus.frames.base.action.IRequest, venus.frames.base.action.IResponse)
     */
    public void service(DefaultServlet ds, IRequest request, IResponse response)
            throws DefaultServletException {
        // TODO
    }

    /* (non-Javadoc)
     * @see venus.frames.base.action.plugin.IServletPlugin#destroy(venus.frames.base.action.DefaultServlet)
     */
    public void destroy(DefaultServlet ds) {
        dataLoader.destory();
    }
}
