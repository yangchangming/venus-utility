package venus.frames.mainframe.ncache;

import venus.frames.base.action.DefaultServlet;
import venus.frames.base.action.DefaultServletException;
import venus.frames.base.action.IRequest;
import venus.frames.base.action.IResponse;
import venus.frames.base.action.plugin.IServletPlugin;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lixizhi
 *
 */
public class InitCachePlugin implements IServletPlugin {

	/*
	 * @see venus.frames.base.action.plugin.IServletPlugin#destroy(venus.frames.base.action.DefaultServlet)
	 */
	public void destroy(DefaultServlet servlet) {
	}

	/* 
	 * @see venus.frames.base.action.plugin.IServletPlugin#init(venus.frames.base.action.DefaultServlet)
	 */
	public void init(DefaultServlet servlet) throws DefaultServletException {
		CacheFactory.getCacheManager();
	}

	/* 
	 * @see venus.frames.base.action.plugin.IServletPlugin#service(venus.frames.base.action.DefaultServlet, venus.frames.base.action.IRequest, venus.frames.base.action.IResponse)
	 */
	public void service(DefaultServlet servlet, IRequest request, IResponse response) throws DefaultServletException {
		// TODO Auto-generated method stub
	}

}
