package venus.frames.mainframe.util;

import venus.commons.xmlenum.EnumRepository;
import venus.frames.base.action.DefaultServlet;
import venus.frames.base.action.DefaultServletException;
import venus.frames.base.action.IRequest;
import venus.frames.base.action.IResponse;
import venus.frames.base.action.plugin.IServletPlugin;

/**
 * @author changming.y
 */
public class InitEnumLoaderPlugin implements IServletPlugin {

	/*
	 * @see venus.frames.base.action.plugin.IServletPlugin#destroy(venus.frames.base.action.DefaultServlet)
	 */
	public void destroy(DefaultServlet arg0) {
	}

	/* 
	 * @see venus.frames.base.action.plugin.IServletPlugin#init(venus.frames.base.action.DefaultServlet)
	 */
	public void init(DefaultServlet arg0) throws DefaultServletException {
		EnumRepository er = EnumRepository.getInstance();
		er.loadFromDir();
	}

	/* 
	 * @see venus.frames.base.action.plugin.IServletPlugin#service(venus.frames.base.action.DefaultServlet, venus.frames.base.action.IRequest, venus.frames.base.action.IResponse)
	 */
	public void service(DefaultServlet arg0, IRequest arg1, IResponse arg2) throws DefaultServletException {
	}
}
