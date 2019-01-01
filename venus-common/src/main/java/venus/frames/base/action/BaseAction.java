package venus.frames.base.action;

import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;
import venus.frames.base.IGlobalsKeys;
import venus.frames.base.bean.DefaultForm;
import venus.frames.mainframe.action.DefaultMapping;
import venus.frames.mainframe.action.Errors;
import venus.frames.mainframe.action.HttpRequest;
import venus.frames.mainframe.action.HttpResponse;
import venus.frames.mainframe.log.ILog;
import venus.frames.mainframe.log.LogMgr;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.util.Hashtable;
import java.util.Locale;

/**
 * Action 的基类
 * 
 * 主要重载父类（来自Struts的类）的 perform 方法
 * 
 * 引入我们自己的统一方法 service()
 * 
 * ------------struts 配置文件片断----------------- <action name="tstActionForm"
 * type="tststruts.tstAction" validate="true" scope="request" path="/tst" >
 * <forward name="failure" path="/jsp/login/loginForm.jsp"/> <forward
 * name="success" path="/jsp/login/loginForm.jsp"/> <forward name="form"
 * path="/jsp/login/loginForm.jsp"/> <forward name="hello" path="/hello.html"/>
 * </action>
 * 
 * @author 岳国云
 */
public class BaseAction extends Action implements IGlobalsKeys {

    protected final ILog logger = LogMgr.getLogger(this);

    /**
     * 
     * 用于存放所有该 Action 对应的所有mapping 配置数据对象的列表
     */
    private Hashtable m_hashActionMappings = new Hashtable();

    /**
     * 缺省构造器
     * 
     * @roseuid 3F42C60F016E
     */
    public BaseAction() {
        super();
    }

    /*
     * public ActionForward perform( ActionMapping mapping, ActionForm form,
     * HttpServletRequest request, HttpServletResponse response) throws
     * IOException, ServletException {
     * 
     * return execute(mapping,form,request,response); }
     */

    protected HttpServletRequest getHttpServletRequest(IRequest request) {

        return (HttpServletRequest) (request.getServletRequest());

    }

    /**
     * 主要重载父类（来自Struts的类）的 perform 方法 struts 的方法不提倡使用
     * 
     * 引入我们自己的统一方法 service()
     * 
     * 具体过程：
     * 
     * 1.包装传入的参数 2.自调用 service()，将处理方法转向给我们定义的方法： service(formBean,req,res);
     * 
     * 从中得到结果 "返回页面URL IForward"
     * 
     * 如果返回的结果(和res.getForward())为 null 则返回 actionMapping.findForward("error")
     * 
     * 从中转型出 ActionForward 并返回
     * 
     * @param actionMapping -
     *            当前actionMapping对象
     * @param actionForm -
     *            当前actionForm对象
     * @param httpServletRequest -
     *            当前httpServletRequest对象
     * @param httpServletResponse -
     *            当前httpServletResponse对象
     * 
     * @return ActionForward - 页面跳转ActionForward对象
     * 
     * @throws IOException
     * @throws ServletException
     * @roseuid 3F41EDAC00C0
     */

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        DefaultMapping dm = null;
        //包装ActionForm为DefaultForm
        DefaultForm formBean = null;

        if (form instanceof DefaultForm) {
            formBean = (DefaultForm) form;
        } else {
            LogMgr
                    .getLogger(this.getClass().getName())
                    .debug(
                            "form is not instanceof venus.frames.mainframe.base.bean.DefaultForm  in execute(...) so send null to service(...) !");
        }
        //如果在mapping 配置数据对象的列表中存在以mapping.getPath()
        //为KEY的mapping实例则取出此实例，否则对当前ActionMapping进行包装
        String mappingPath = mapping.getPath();
        if (m_hashActionMappings.containsKey(mappingPath)) {
            dm = (DefaultMapping) m_hashActionMappings.get(mappingPath);
        } else {
            dm = new DefaultMapping(mapping);
            m_hashActionMappings.put(mappingPath, dm);
        }

        //包装当前HttpServletRequest和HttpServletResponse
        HttpRequest req = null;
        HttpResponse res = null;

        //对request转型
        if (request instanceof HttpRequest) {
            req = (HttpRequest) request;
        } else {
            req = new HttpRequest(request);
            LogMgr
                    .getLogger(this.getClass().getName())
                    .debug(
                            "request is not instanceof HttpRequest in execute(...) so new a HttpRequest(request) !");

            //return mapping.findForward("error");
        }
        //对response转型
        if (response instanceof HttpResponse) {
            res = (HttpResponse) response;
        } else {
            res = new HttpResponse(response);
            LogMgr
                    .getLogger(this.getClass().getName())
                    .debug(
                            "response is not instanceof HttpResponse in execute(...) so new a HttpResponse(response) !");

            //return mapping.findForward("error");
        }

        //设置请求对象对应的配置数据对象，放入HttpRequest

        //if (dm instanceof IMappingCfg) {
        req.setMapping((IMappingCfg) dm);
        //} else {
        //	return mapping.findForward("error");
        //}

        //调用service（...）方法，返回Forward转到相应的URL
        //try {
        ActionForward af = (ActionForward) service(formBean, req, res);
        if ( res.isCommitted() )
        	return null;
        
        if (af != null) {
            return af;
        } else {
            //如果返回为空，则转向错误页面
            logger.info("service return null!");
            request.setAttribute("Message","pls check struts config file");
            return mapping.findForward(ERROR_PAGE_KEY);
        }
        /*
         * } catch (DefaultServletException dse) { //如果出现异常，转向错误页面 info("service
         * return null!"); return mapping.findForward(ERROR_PAGE_KEY); } catch
         * (Exception dse) { //如果出现民异常，转向错误页面 info("service return null!");
         * return mapping.findForward(ERROR_PAGE_KEY); }
         */

    }

    /**
     * 我们自己定义的统一处理方法
     * 
     * @param formBean -
     *            传入的表单的对象
     * @param request -
     *            传入的请求对象
     * @param response -
     *            供传出的响应对象
     * @return IForward - 跳转页面对象
     * @throws DefaultServletException
     * @roseuid 3F83AF440343
     */

    public IForward service(DefaultForm formBean, IRequest request,
                            IResponse response) throws Exception {
        return null;
    }

    /**
     * 调用日志记录方法
     * 
     * @param message
     *            日志信息
     * @return void
     * @roseuid 3FAA2C0F037A
     */
    public void debug(Object message) {
        getIlog().debug(message);
    }

    /**
     * 调用日志记录方法
     * 
     * @param message
     *            日志信息
     * @return void
     * @roseuid 3FAA2C0F0399
     */
    public void info(Object message) {
        getIlog().info(message);
    }

    /**
     * 调用日志记录方法
     * 
     * @param message
     *            日志信息
     * @return void
     * @roseuid 3FAA2C0F03A9
     */
    public void warn(Object message) {
        getIlog().warn(message);
    }

    /**
     * 调用日志记录方法
     * 
     * @param message
     *            日志信息
     * @return void
     * @roseuid 3FAA2C0F03B9
     */
    public void error(Object message) {
        getIlog().error(message);
    }

    /**
     * 调用日志记录方法
     * 
     * @param message
     *            日志信息
     * @roseuid 3FAA2C0F03D8
     */
    public void fatal(Object message) {
        getIlog().fatal(message);
    }

    /**
     * 调用日志记录方法
     * 
     * @param nLevel
     *            日志级别
     * @param message
     *            日志信息
     * @return void
     * @roseuid 3FAA2C0F03E7
     */
    public void log(int nLevel, Object message) {
        getIlog().log(nLevel, message);
    }

    /**
     * 调用日志记录方法
     * 
     * @param message
     *            日志信息
     * @param t
     *            异常诱因
     * @return void
     * @roseuid 3FAA2C10001F
     */
    public void debug(Object message, Throwable t) {
        getIlog().debug(message, t);
    }

    /**
     * 调用日志记录方法
     * 
     * @param message
     *            日志信息
     * @param t
     *            异常诱因
     * @return void
     * @roseuid 3FAA2C10003E
     */
    public void info(Object message, Throwable t) {
        getIlog().info(message, t);
    }

    /**
     * 调用日志记录方法
     * 
     * @param message
     *            日志信息
     * @param t
     *            异常诱因
     * @return void
     * @roseuid 3FAA2C10006D
     */
    public void warn(Object message, Throwable t) {
        getIlog().warn(message, t);
    }

    /**
     * 调用日志记录方法
     * 
     * @param message
     *            日志信息
     * @param t
     *            异常诱因
     * @return void
     * @roseuid 3FAA2C10008C
     */
    public void error(Object message, Throwable t) {
        getIlog().error(message, t);
    }

    /**
     * 调用日志记录方法
     * 
     * @param message
     *            日志信息
     * @param t
     *            异常诱因
     * @return void
     * @roseuid 3FAA2C1000AB
     */
    public void fatal(Object message, Throwable t) {
        getIlog().fatal(message, t);
    }

    /**
     * 调用日志记录方法
     * 
     * @param nLevel
     *            日志级别
     * @param message
     *            日志信息
     * @param t
     *            异常诱因
     * @return void
     * @roseuid 3FAA2C1000DA
     */
    public void log(int nLevel, Object message, Throwable t) {
        getIlog().log(nLevel, message, t);
    }

    /**
     * 得到日志记录驱动实例
     * 
     * @return ILog LOG驱动实例
     */
    private ILog getIlog() {

        //return LogMgr.getLogger(this.getClass().getName());
        return this.logger;
    }

    /* override func in struts1.1 action */

    // ---------------------------------------------------- Protected Methods

    /**
     * Generate a new transaction token, to be used for enforcing a single
     * request for a particular transaction.
     * 
     * @param request
     *            The request we are processing
     */
    protected String generateToken(IRequest request) {
        return generateToken(getHttpServletRequest(request));
    }

    /**
     * Return the default data source for the current module.
     * 
     * @param request
     *            The servlet request we are processing
     * 
     * @since Struts 1.1
     */
    protected DataSource getDataSource(IRequest request) {

        return getDataSource((HttpServletRequest) (request.getServletRequest()));

    }

    /**
     * Return the specified data source for the current module.
     * 
     * @param request
     *            The servlet request we are processing
     * @param key
     *            The key specified in the
     *            <code>&lt;message-resources&gt;</code> element for the
     *            requested bundle
     * 
     * @since Struts 1.1
     */
    protected DataSource getDataSource(IRequest request, String key) {

        return getDataSource(getHttpServletRequest(request), key);

    }

    /**
     * Return the user's currently selected Locale.
     * 
     * @param request
     *            The request we are processing
     */
    protected Locale getLocale(IRequest request) {

        return getLocale(getHttpServletRequest(request));

    }

    /**
     * Set the user's currently selected Locale.
     * 
     * @param request
     *            The request we are processing
     * @param locale
     *            The user's selected Locale to be set, or null to select the
     *            server's default Locale
     */
    protected void setLocale(IRequest request, Locale locale) {

        setLocale(getHttpServletRequest(request), locale);

    }

    /**
     * Return the default message resources for the current module.
     * 
     * @param request
     *            The servlet request we are processing
     * @since Struts 1.1
     */
    protected MessageResources getResources(IRequest request) {

        return getResources(getHttpServletRequest(request));

    }

    /**
     * Return the specified message resources for the current module.
     * 
     * @param request
     *            The servlet request we are processing
     * @param key
     *            The key specified in the
     *            <code>&lt;message-resources&gt;</code> element for the
     *            requested bundle
     * 
     * @since Struts 1.1
     */
    protected MessageResources getResources(IRequest request, String key) {

        return getResources(getHttpServletRequest(request), key);

    }

    /**
     * <p>
     * Returns <code>true</code> if the current form's cancel button was
     * pressed. This method will check if the <code>Globals.CANCEL_KEY</code>
     * request attribute has been set, which normally occurs if the cancel
     * button generated by <strong>CancelTag </strong> was pressed by the user
     * in the current request. If <code>true</code>, validation performed by
     * an <strong>ActionForm </strong>'s <code>validate()</code> method will
     * have been skipped by the controller servlet.
     * </p>
     * 
     * @param request
     *            The servlet request we are processing
     * @see org.apache.struts.taglib.html.CancelTag
     */
    protected boolean isCancelled(IRequest request) {

        return isCancelled(getHttpServletRequest(request));

    }

    /**
     * Return <code>true</code> if there is a transaction token stored in the
     * user's current session, and the value submitted as a request parameter
     * with this action matches it. Returns <code>false</code> under any of
     * the following circumstances:
     * <ul>
     * <li>No session associated with this request</li>
     * <li>No transaction token saved in the session</li>
     * <li>No transaction token included as a request parameter</li>
     * <li>The included transaction token value does not match the transaction
     * token in the user's session</li>
     * </ul>
     * 
     * @param request
     *            The servlet request we are processing
     */
    protected boolean isTokenValid(IRequest request) {

        return isTokenValid(getHttpServletRequest(request));

    }

    /**
     * Return <code>true</code> if there is a transaction token stored in the
     * user's current session, and the value submitted as a request parameter
     * with this action matches it. Returns <code>false</code>
     * <ul>
     * <li>No session associated with this request</li>
     * <li>No transaction token saved in the session</li>
     * <li>No transaction token included as a request parameter</li>
     * <li>The included transaction token value does not match the transaction
     * token in the user's session</li>
     * </ul>
     * 
     * @param request
     *            The servlet request we are processing
     * @param reset
     *            Should we reset the token after checking it?
     */
    protected boolean isTokenValid(IRequest request, boolean reset) {

        return isTokenValid(getHttpServletRequest(request), reset);
    }

    /**
     * Reset the saved transaction token in the user's session. This indicates
     * that transactional token checking will not be needed on the next request
     * that is submitted.
     * 
     * @param request
     *            The servlet request we are processing
     */
    protected void resetToken(IRequest request) {
        resetToken(getHttpServletRequest(request));
    }

    /**
     * Save the specified error messages keys into the appropriate request
     * attribute for use by the &lt;html:errors&gt; tag, if any messages are
     * required. Otherwise, ensure that the request attribute is not created.
     * 
     * @param request
     *            The servlet request we are processing
     * @param errors
     *            Error messages object
     */
    protected void saveErrors(IRequest request, ActionErrors errors) {

        saveErrors(getHttpServletRequest(request), errors);

    }

    protected void saveError(IRequest request, BaseActionError error) {

        saveErrors(getHttpServletRequest(request), new Errors(error));

    }

    protected void saveError(IRequest request, String errStr) {

        saveErrors(getHttpServletRequest(request), new Errors(
                new BaseActionError(null, errStr)));

    }

    protected void saveError(IRequest request, Exception err) {

        saveErrors(getHttpServletRequest(request), new Errors(
                new BaseActionError(err)));

    }

    /**
     * Save the specified messages keys into the appropriate request attribute
     * for use by the &lt;html:messages&gt; tag (if messages="true" is set), if
     * any messages are required. Otherwise, ensure that the request attribute
     * is not created.
     * 
     * @param request
     *            The servlet request we are processing
     * @param messages
     *            Messages object
     * @since Struts 1.1
     */
    protected void saveMessages(IRequest request, ActionMessages messages) {

        saveMessages(getHttpServletRequest(request), messages);

    }

    /**
     * Save a new transaction token in the user's current session, creating a
     * new session if necessary.
     * 
     * @param request
     *            The servlet request we are processing
     */
    protected void saveToken(IRequest request) {
        saveToken(getHttpServletRequest(request));
    }

    /**
     * Save a form instance in the user's current request,
     * 
     * @param request
     *            The servlet request we are processing
     */
    protected void saveFormBean(IRequest request, DefaultForm formBean) {
        request.setAttribute(request.getMapping().getFormName(), formBean);
    }

    public boolean isDebugEnabled() {
        return getIlog().isDebugEnabled();
    }

    public boolean isInfoEnabled() {
        return getIlog().isInfoEnabled();
    }

}