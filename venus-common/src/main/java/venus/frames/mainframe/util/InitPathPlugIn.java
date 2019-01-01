package venus.frames.mainframe.util;

import venus.frames.base.action.DefaultServlet;
import venus.frames.base.action.IRequest;
import venus.frames.base.action.IResponse;
import venus.frames.base.action.plugin.IServletPlugin;
import venus.frames.mainframe.log.LogMgr;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 初始化系统路径管理的插件类<br>
 * <p/>
 * 该类作为启动路径配置数据的按钮，由于路径信息不具备，则不能使用系统路径系统来读取<br>
 * 配置文件等。所以该插件会根据不同的接口插件方式实现<br>
 * <p/>
 * 目前支持2个版本：<br>
 * 一个IServletPlugin：配置在 web.xml 中，由DefaultServlet启动；<br>
 * 另一个是在ServiceKeeper中随命令行启动，目前第一叠代暂时不实现。
 *
 * @author 张文韬
 */
public class InitPathPlugIn implements IServletPlugin {

    /**
     * @roseuid 3F9F7B990203
     */
    public InitPathPlugIn() {
        super();
    }

    /**
     * 初始化路径数据，不依赖XML配置文件，不依赖系统部署模式
     * <p/>
     * 暂时不实现
     *
     * @roseuid 3F9E575B021F
     */
    public void initAnyWhere() {

    }

    /**
     * 初始化路径信息，遵循IServletPlugin的接口定义
     * <p/>
     * -----------------------------------------------
     * 配置数据可以配在WEB.XML中servlet数据中：
     * <p/>
     * <init-param>
     * <param-name>init-classes</param-name>
     * <param-value>venus.frames.mainframe.util.InitPathPlugIn</param-value>
     * </init-param>
     * <init-param>
     * <param-name>log4j-config-file</param-name>
     * <param-value>c:\\log4j.properties</param-value>
     * </init-param>
     * <init-param>
     * <param-name>USE_HOME</param-name>
     * <param-value>/venus/</param-value>
     * </init-param>
     * <init-param>
     * <param-name>USE_VENUS_ROOT</param-name>
     * <param-value>/root/</param-value>
     * </init-param>
     * <init-param>
     * <param-name>USE_VENUS_TMP</param-name>
     * <param-value>/tmp/</param-value>
     * </init-param>
     * -----------------------------------------------
     *
     * @param servlet - 初始化是传入的SERVLET句柄
     * @roseuid 3F9E573A004B
     */
    public void init(DefaultServlet servlet) {

        //获取初始路径参数
//        String strUseHome = sc.getInitParameter(PathMgr.USE_HOME);
//        String strRoot = sc.getInitParameter(PathMgr.USE_VENUS_ROOT);
//        String strTmp = sc.getInitParameter(PathMgr.USE_VENUS_TMP);
//        String strLog4j = sc.getInitParameter("log4j-config-file");
//        String strModuleName = sc.getInitParameter(PathMgr.WAR_NAME);

        String strUseHome = null;
        String strRoot = "/";
        String strTmp = "/tmp/";
        String strLog4j = null;
        String strModuleName = "authority";

        try {

            if (strModuleName != null) {
                if (strUseHome != null)
                    System.setProperty(PathMgr.USE_HOME + "." + strModuleName, strUseHome);
                if (strRoot != null)
                    System.setProperty(PathMgr.USE_VENUS_ROOT + "." + strModuleName, strRoot);
                if (strTmp != null)
                    System.setProperty(PathMgr.USE_VENUS_TMP + "." + strModuleName, strTmp);
            } else {
                if (strUseHome != null)
                    System.setProperty(PathMgr.USE_HOME, strUseHome);
                if (strRoot != null)
                    System.setProperty(PathMgr.USE_VENUS_ROOT, strRoot);
                if (strTmp != null)
                    System.setProperty(PathMgr.USE_VENUS_TMP, strTmp);

            }

            //初始化配置信息
            PathMgr.init(servlet.getServletContext(), strModuleName);

            PathMgr.configLog4j(strLog4j);
        } catch (SecurityException e) {
            LogMgr.getLogger(this).error("没有写权限!", e);
        } catch (Exception e) {
            LogMgr.getLogger(this).error("路径信息错误!", e);
            return;
        }
    }

    /**
     * 遵循IServletPlugin的接口定义
     * <p/>
     * 该方法暂时不实现
     *
     * @param servlet
     * @param request
     * @param response
     * @roseuid 3F9F55E300FA
     */
    public void service(DefaultServlet servlet, IRequest request, IResponse response) {
    }

    /**
     * 遵循IServletPlugin的接口定义
     * <p/>
     * 暂时不实现
     *
     * @param context - 初始化是传入的SERVLET句柄
     * @roseuid 3F9F55ED03A9
     */
    public void destroy(DefaultServlet servlet) {
    }
}
