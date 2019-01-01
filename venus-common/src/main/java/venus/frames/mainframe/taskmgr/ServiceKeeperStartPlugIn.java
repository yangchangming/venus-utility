package venus.frames.mainframe.taskmgr;


import venus.frames.base.action.DefaultServlet;
import venus.frames.base.action.DefaultServletException;
import venus.frames.base.action.IRequest;
import venus.frames.base.action.IResponse;
import venus.frames.base.action.plugin.IServletPlugin;


/**
 * 启动ServiceKeeper的插件类
 *
 *
 * 目前支持版本：
 *
 * 一个 IServletPlugin： 配置在 web.xml ini-classes 中，由DefaultServlet启动
 *
 *
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
		// TODO 自动生成方法存根

	}

	/* （非 Javadoc）
	 * @see venus.frames.mainframe.base.action.plugin.IServletPlugin#destroy(venus.frames.mainframe.base.action.DefaultServlet)
	 */
	public void destroy(DefaultServlet servlet) {
		// TODO 自动生成方法存根
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

		//启动线程
		t.start();
		}catch (Exception e){
		   e.printStackTrace();

		}

	}

        private void stopServiceKeeper() {
          String[] args = new String[1];
          args[0] = ServiceKeeper.CMD_SHUTDOWN;
          ServiceKeeper.main(args);
          /*
          if (this.t != null && this.t.isAlive()) {
            this.t.interrupt();
          }
          */

        }
      }