package venus.frames.mainframe.taskmgr;

import gap.commons.digest.DigestLoader;
import gap.license.exception.InvalidLicenseException;
import venus.frames.mainframe.log.ILog;
import venus.frames.mainframe.log.LogMgr;
import venus.frames.mainframe.util.ClassLocator;
import venus.frames.mainframe.util.ConfMgr;
import venus.frames.mainframe.util.DefaultConfReader;
import venus.pub.util.ReflectionUtils;

import javax.servlet.ServletException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

/**
 * 集中启动任务的守护服务
 * 
 * 该服务主要完成2件事情，
 * 
 * 1。是启动自身：守护线程，并启动SocketServer监听TCP端口，这样可以通过远端TCP访问?
 * 式操作该服务
 * 
 * 2。加载启动任务列表，启动这些任务
 * 
 * @author 岳国云
 */
public class ServiceKeeper {

	/**
	 * 启动守护服务的命令字
	 */
	public static final String CMD_STARTUP = "-startup";

	/**
	 * 停止守护服务的命令字
	 */
	public static final String CMD_SHUTDOWN = "-shutdown";

	/**
	 * 显示守护服务状态的命令字
	 */
	public static final String CMD_STATE = "-state";

	/**
	 * 显示守护服务帮助信息的命令字，使用于命令行启动模式的提示
	 */
	public static final String CMD_HELP = "-help";

	/**
	 * Socket Server Instance
	 */
	private ServerSocket m_ssListener = null;

	/**
	 * SocketServer listen port
	 */
	private int m_nPort = 10020;

	/**
	 * 启动任务列表
	 */
	private String[] m_aryServices = {"venus.frames.mainframe.oid.OidMgr" };

	/**
	 * 用于存储各启动任务实例的列表，便于以后最该列表做操作
	 */
	private Vector m_vecServices = new Vector();

	/**
	 * 用于存储各启动任务线程实例的列表，便于以后最该列表做操作
	 */
	private Vector m_vecSrvThreads = new Vector();

	/**
	 * 中用到的，用于存储该类的单一实例
	 * 
	 * used for singleton model
	 */
	private static ServiceKeeper m_Singleton = null;

	/**
	 * default Construction
	 * 
	 * @return void
	 * @roseuid 3F950E4E0095
	 */
	public ServiceKeeper() {
		super();
	}

	/**
	 * 封装notify();
	 * 
	 * @return void
	 * @roseuid 3F934B6E0332
	 */
	public synchronized void doNotify() {
		notify();
	}

	/**
	 * 封装wait();
	 * 
	 * @return void
	 * @roseuid 3F934B880351
	 */
	public synchronized void doWait() {
		
		try {
			wait();
		} catch (InterruptedException ie) {
			LogMgr.getLogger(this.getClass().getName()).error(
				"doWait() : InterruptedException",
				ie);
		}
	}

	/**
	 * 当将此对象作为垃圾收集时要执行的代码
	 * 抛出Throwable异常
	 *
	 * @throws Throwable
	 * @roseuid 3F934B920332
	 */
	public void finalize() throws Throwable {
		super.finalize();
	}

	/**
	 * 转入启动SocketServer进入命令监听状态
	 * SocketServer典型模式
	 *
	 * @return void
	 * @roseuid 3F934B9C02F4
	 */
	public void listen() throws InterruptedException{

		try {
			//创建服务器端m_nPort端口通信实例
			m_ssListener = new ServerSocket(m_nPort);
			//监听程序
			while (true) {
                          if (Thread.interrupted()){
                            throw new InterruptedException();
                          }
				//监听是否有连接请求
				Socket client = m_ssListener.accept();
				//创建输入流并获取连接信息
				ObjectInputStream ois =
					new ObjectInputStream(client.getInputStream());
				String cmd = (String) ois.readObject();

				LogMgr.getLogger(this.getClass().getName()).info(
					"listen cmd:" + cmd);

				//如果获取信息为关闭请求
				if (CMD_SHUTDOWN.equals(cmd)) {
					while (m_vecServices.size() > 0) {
						try {
							IService srv = (IService) m_vecServices.elementAt(0);
							srv.shutdown();
						} catch (Exception e) {
							e.printStackTrace();
						}
						m_vecServices.removeElementAt(0);
					}
                                        while (m_vecSrvThreads.size() > 0) {
                                          try {
                                            Thread srvt = (Thread) m_vecSrvThreads.elementAt(0);
                                            if (srvt.isAlive()){
                                                srvt.interrupt();
                                            }

                                          }
                                          catch (Exception e) {
                                            e.printStackTrace();
                                          }
                                          m_vecSrvThreads.removeElementAt(0);
                                        }

					//关闭输入流、关闭连接、正常退出
					ois.close();
					client.close();
                                        m_ssListener.close();
                                        //Thread.currentThread().interrupt();
                                        LogMgr.getLogger(this.getClass().getName()).info("Stopping VENUS Task service...");
										return;
					//如果获取信息为显示当前状态请求，则创建一个输出流，发送服务器当前运行信息
				} else if (CMD_STATE.equals(cmd)) {

					// 显示当前状态
					ObjectOutputStream oos =
						new ObjectOutputStream(client.getOutputStream());
					oos.writeObject("services Keeper is well running now");
					oos.flush();
					//关闭输出流
					oos.close();
				}
			}
		} catch (java.io.IOException ioe) {
			LogMgr.getLogger(this.getClass().getName()).error(
				"listen(): IOException",
				ioe);
			return;
		} catch (ClassNotFoundException cnfe) {
			LogMgr.getLogger(this.getClass().getName()).error(
				"listen(): ClassNotFoundException",
				cnfe);
			return;
		} catch (Exception e) {
			LogMgr.getLogger(this.getClass().getName()).error(
				"listen(): Exception",
				e);
			return;
		}
	}

	/**
	 * 启动任务集中调度的守护服务程序，主要为命令行启动使用
	 *
	 * @param args
	 * @return void
	 * @roseuid 3F934BA6012F
	 */
	public static void main(String[] args) {

		//获得该类的全局单一实例
		ServiceKeeper handle = getSingleton();

		//默认启动服务
		if (args.length == 0) {
			handle.startService();
			//通过命令"-startup"启动服务
		} else if (CMD_STARTUP.equals(args[0])) {
			handle.startService();
			//通过命令“-shutdown”关闭服务
		} else if (CMD_SHUTDOWN.equals(args[0])) {
			handle.stopService();
			//通过命令“-state”显示当前服务状态
		} else if (CMD_STATE.equals(args[0])) {
			handle.showState();
			//通过命令“-help”获取帮助
		} else if (CMD_HELP.equals(args[0])) {
			handle.showUsage();
			//返回帮助
		} else {
			handle.showUsage();
		}


	}

	/**
	 * 在控制台显示传入的消息，如果Log4j日志记录功能存在，
	 * 则使用Log4j来完成，否则使用System.out.println(...).
	 *
	 * @param msg - 传入的消息
	 * @roseuid 3F934D6302FA
	 */
	private void showMessage(String msg) {

		//检索日志实现类是否存在
		boolean isLog = true;
		try {
			ClassLocator.loadClass("venus.frames.mainframe.log.LogMgr");
		} catch (ClassNotFoundException cnfe) {
			isLog = false;
		}

		//如果日志实现类存在，则使用其来显示消息，否则使用System.out.println()
		if (isLog) {
			this.getIlog().info(msg);
		} else {
			System.out.println(msg);
		}
	}

	/**
	 * 得到日志记录驱动实例
	 *
	 * @return ILog LOG驱动实例
	 */
	private ILog getIlog() {
		return LogMgr.getLogger(this);
	}

	/**
	 * 显示服务的当前状态
	 *
	 * 模拟Socket Client 驱动 Socket Server 显示守护服务的当前状态
	 *
	 * 1.根据Server的PORT和IP 同Server建立Socket连接
	 *
	 * 2.通过Socket流对象将查询状态的名字输出给Server，
	 *
	 * 3.并得到Server返回的结果显示出来
	 *
	 * 4.完成后关闭本次Socket及流
	 *
	 * @return void
	 * @roseuid 3F934DEB0201
	 */
	public void showState() {

		try {
			//创建一个本地的m_nPort端口的 与服务器的连接
			Socket sock = new Socket("127.0.0.1", m_nPort);
			//创建并获取该连接输出流
			ObjectOutputStream oos =
				new ObjectOutputStream(sock.getOutputStream());
			//向服务器发送当前服务状态流
			oos.writeObject(CMD_STATE);
			//创建并获取该连接输入流
			ObjectInputStream ois =
				new ObjectInputStream(sock.getInputStream());
			//输出从服务器端获取的信息
			showMessage((String) ois.readObject());
			//关闭输出流
			oos.close();
			//关闭输入流
			ois.close();
			//关闭请求连接
			sock.close();
            ArrayList<Double> list = new ArrayList<Double>();

		} catch (java.net.UnknownHostException uhe) {
			LogMgr.getLogger(this.getClass().getName()).error(
				"showState(): UnknownHostException",
				uhe);
		} catch (java.io.IOException ioe) {
			LogMgr.getLogger(this.getClass().getName()).error(
				"showState(): IOException",
				ioe);
		} catch (ClassNotFoundException cnfe) {
			LogMgr.getLogger(this.getClass().getName()).error(
				"showState(): ClassNotFoundException",
				cnfe);
		}
	}

	/**
	 * 显示使用方法，命令行操作时提示该类用法
	 *
	 * @return void
	 * @roseuid 3F934DF500E8
	 */
	public void showUsage() {

		//显示使用方法
		showMessage("");
		showMessage("  Task Schedule Service [Version 0.1]");
		showMessage("  Usage: java venus.frames.mainframe.taskmgr.ServiceKeeper[-option]");
		showMessage("  where options include:");
		showMessage("\t-startup  \t Start up service");
		showMessage("\t-shutdown  \t Shutdown service");
		showMessage("\t-state   \t Show service current state");
		showMessage("\t-help   \t Show this usage");

	}

	/**
	 * 启动服务
	 *
	 * 1.得到启动任务列表
	 * 2.根据IService接口，将这些任务的入口启动类实例化
	 * 3.为每个任务新建Thread，并以这些任务的名称命名，并启动，
	 * 在这些Thread中调用IService接口startup() 启动任务
	 * 4.将这些Thread加入一列表维护，便于管理监控这些任务
	 * 5.启动完成，转入启动SocketServer进入命令监听状态
	 *
	 * @return void
	 * @throws ServletException
	 * @roseuid 3F934DFE003D
	 */
	public void startService() {

//		try {
//			DigestLoader loader = DigestLoader.getLoader();
//			if (loader.isValid() && Math.random() > 0.2) {
//				chkLS(loader);
//			} else if (!loader.isValid()) {
//				chkLS(loader);
//			}
//		} catch (RuntimeException re) {
//			LogMgr.getLogger("udp.use.platform").error( re.getClass().getName()+" " + re.getMessage() );
//			return;
//		}

		showMessage("Starting ...");

		// 得到启动任务列表，启动任务调度
		for (int i = 0; i < m_aryServices.length; i++) {
			try {
				//将列表中每一个任务的入口启动类实例化
				final IService srv = (IService) ClassLocator.loadClass(m_aryServices[i]).newInstance();
				Thread t = new Thread(srv.getServiceName()) {
					public void run() {
						srv.startup();
					}
				};

				m_vecSrvThreads.addElement(t);
				t.start();
				m_vecServices.addElement(srv);

				showMessage(srv.getServiceName() + " started!");
			} catch (ClassNotFoundException cnfe) {
				LogMgr.getLogger(this.getClass().getName()).error("startService(): ClassNotFoundException", cnfe);
			} catch (IllegalAccessException iae) {
				LogMgr.getLogger(this.getClass().getName()).error("startService(): IllegalAccessException", iae);
			} catch (InstantiationException ie) {
				LogMgr.getLogger(this.getClass().getName()).error("startService(): InstantiationException", ie);
			}
		}

		showMessage("Task scheduler started successfully");
		showMessage("Service Keeper listening at port: " + String.valueOf(m_nPort));

		LogMgr.getLogger(this.getClass().getName()).info(" 进入命令监听状态....");
        try {
          listen();
        }
        catch (InterruptedException ex) {
          try {
			  showMessage("Task service shutdown successfully!");
            	this.finalize();
          } catch (Throwable ex1) {
            ex1.printStackTrace();
          }
        }
	}

	/**
	 * 停止服务。
	 * 
	 * 模拟Socket Client 驱动 Socket Server 停止服务
	 * 
	 * 1.根据Server的PORT和IP 同Server建立Socket连接
	 * 
	 * 2.通过Socket流对象将停止服务的名字输出给Server，
	 * 
	 * 3.完成后关闭本次Socket及流
	 * 
	 * @return void
	 * @roseuid 3F934E0F01E3
	 */
	public void stopService() {

		try {
			//创建一个本地的m_nPort端口的与服务器的连接
			Socket sock = new Socket("127.0.0.1", m_nPort);
			//创建并获取该连接输出流
			ObjectOutputStream oos = new ObjectOutputStream(sock.getOutputStream());
			//向服务器发送停止服务请求
			oos.writeObject(CMD_SHUTDOWN);
			//刷新输出流缓冲区
			oos.flush();
			//关闭输出连接
			oos.close();
			//关闭客户端通信连接
			sock.close();
			//输出关闭信息


		} catch (java.net.UnknownHostException uhe) {
			LogMgr.getLogger(this.getClass().getName()).error(
				"stopService(): UnknownHostException",
				uhe);
		} catch (java.io.IOException ioe) {
			LogMgr.getLogger(this.getClass().getName()).error(
				"stopService(): IOException",
				ioe);
		}
	}

	/**
	 * 加载配置数据
	 * 
	 * m_nPort: SocketServer listen port
	 * 
	 * m_aryServices:  启动任务列表
	 * 
	 * the xml conf is :
	 * <venus.frames.mainframe.taskmgr.ServiceKeeper Port="10020">
	 * <startup name="venus.frames.mainframe.rpc.server.ServiceListener"/>
	 * <startup name="venus.frames.mainframe.oid.OidMgr"/>
	 * </venus.frames.mainframe.taskmgr.ServiceKeeper>
	 * 
	 * @return void
	 * @roseuid 3F94C41901DE
	 */
	private void loadConf() {


		//以本类名为标识从配置文件读取并配置数据(端口与启动任务列表)
		try {
			DefaultConfReader dcr =
				new DefaultConfReader(
					ConfMgr.getNode(this.getClass().getName()));
			this.m_nPort = Integer.parseInt(dcr.readStringAttribute("Port"));
			this.m_aryServices = dcr.readChildStringAry("startup", "name");
		} catch (NullPointerException e) {
			LogMgr.getLogger(this.getClass().getName()).info(
				"loadConf()：读取配置文件错误");
		}
	}

	/**
	 * @param loader
	 */
	private void chkLS(DigestLoader loader) {

		boolean valid = true;
		try {
			Class cls = loader.findClass();
			Method m = ReflectionUtils.findMethod(cls, "checkLicense",
					new Class[] {});
			valid = new Boolean(ReflectionUtils.invokeMethod(m, null,
					new Object[] {}).toString()).booleanValue();
		} catch (RuntimeException e) {
			loader.setValid(false);
			throw e;
		}
		if (!valid) {
			loader.setValid(false);
			throw new InvalidLicenseException();
		} else {
			LogMgr.getLogger("udp.use.platform").info(
			"ckls finished!");
			loader.setValid(true);
		}
	}
	
	/**
	 * 通过此静态方法获得该类的全局单一实例
	 * 
	 * 如果该实例未构建则构建初始化并加载配置数据
	 * 
	 * @return venus.frames.mainframe.taskmgr.ServiceKeeper - 该类的全局单一实例
	 * @roseuid 3F950EEB01D5
	 */
	public static ServiceKeeper getSingleton() {
		//如果当前全局单一实例未构建，则初始化并加载配置数据
		if (m_Singleton == null) {
			m_Singleton = new ServiceKeeper();
			m_Singleton.loadConf();
		}
		return m_Singleton;
	}
}
