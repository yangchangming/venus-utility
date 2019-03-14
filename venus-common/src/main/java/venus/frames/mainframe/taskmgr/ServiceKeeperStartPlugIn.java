package venus.frames.mainframe.taskmgr;

import venus.frames.base.action.DefaultServlet;
import venus.frames.base.action.DefaultServletException;
import venus.frames.base.action.IRequest;
import venus.frames.base.action.IResponse;
import venus.frames.base.action.plugin.IServletPlugin;

/**
 * 启动ServiceKeeper的插件类
 */
public class ServiceKeeperStartPlugIn implements IServletPlugin {

        public Thread t ;

	/* （非 Javadoc）
	 * @see venus.frames.mainframe.base.action.plugin.IServletPlugin#init(venus.frames.mainframe.base.action.DefaultServlet)
	 */
	public void init(DefaultServlet servlet) throws DefaultServletException {
		startServiceKeeper();
	}

	/* （非 Javadoc）
	 * @see venus.frames.mainframe.base.action.plugin.IServletPlugin#service(venus.frames.mainframe.base.action.DefaultServlet, venus.frames.mainframe.base.action.IRequest, venus.frames.mainframe.base.action.IResponse)
	 */
	public void service(DefaultServlet servlet, IRequest request, IResponse response) throws DefaultServletException {
	}

	/* （非 Javadoc）
	 * @see venus.frames.mainframe.base.action.plugin.IServletPlugin#destroy(venus.frames.mainframe.base.action.DefaultServlet)
	 */
	public void destroy(DefaultServlet servlet) {
		stopServiceKeeper() ;
	}

	public void startServiceKeeper(){
		try {
			t = new Thread("start servicekeeper"){
			public void run(){
				String[] args = new String[1];
				args[0] = ServiceKeeper.CMD_STARTUP;
				ServiceKeeper.main(args);
			}
		};
		t.start();
		}catch (Exception e){
		   e.printStackTrace();
		}
	}

	private void stopServiceKeeper() {
		String[] args = new String[1];
		args[0] = ServiceKeeper.CMD_SHUTDOWN;
	    ServiceKeeper.main(args);
	}

}