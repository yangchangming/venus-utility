package venus.frames.util.exception.handler;

import gap.commons.digest.DigestLoader;
import gap.license.exception.InvalidLicenseException;
import venus.frames.base.action.IForward;
import venus.frames.base.action.IRequest;
import venus.frames.mainframe.action.BaseExceptionHandler;
import venus.frames.mainframe.log.ILog;
import venus.frames.mainframe.log.LogMgr;
import venus.frames.mainframe.util.Helper;
import venus.frames.mainframe.util.IConfReader;
//import venus.frames.web.message.messageAgent;
import venus.frames.web.message.MessageAgent;
import venus.pub.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 * User: cj
 * Date: 13-9-23
 * Time: 下午1:40
 * To change this template use File | Settings | File Templates.
 */
public class BaseGlobalExceptionHandler extends BaseExceptionHandler {
    public static String MassageStyle;
    public static boolean isDisplayRealError;
    public static final String MESSAGE_STYLE_KEY = "MassageStyle";
    public static final String MESSAGE_DISPLAY_KEY = "DisplayRealError";
    private static final String CONF_KEY = "venus.frames.util.exception.GlobalExceptionHandlerWithMessageAgent";
    private static ILog logger = LogMgr.getLogger(BaseGlobalExceptionHandler.class);

    static {
        IConfReader icf = Helper.getConfReader(CONF_KEY);

        MassageStyle = icf.readStringAttribute(MESSAGE_STYLE_KEY);
        int flag = icf.readIntAttribute(MESSAGE_DISPLAY_KEY);
        if (flag == -1 || flag == 1) {
            isDisplayRealError = true;
        } else {
            isDisplayRealError = false;
        }
    }

    protected void preHandleException(Exception ex) {
        // 记日志
        logGlobalException(ex);
        // 验证书
//        chkLisence();
    }

    protected IForward sendErrorMessage(IRequest request, Exception ex) {
        if (isDisplayRealError) {
            return MessageAgent.sendErrorMessage(request, ex, MassageStyle);
        } else {
            return MessageAgent.sendErrorMessage(request, ex, MassageStyle, isDisplayRealError);
        }
    }

    private void logGlobalException(Exception ex) {
        if (ex.getClass().getName().indexOf("license") > 0 && (ex.getMessage() != null && ex.getMessage().indexOf("license") > 0)) {
            logger.error("udp.use.platform: " + ex.getClass().getName() + ": " + ex.getMessage());
        } else {
            logger.error("Global exception handler catched exception.", ex);
        }
    }

    private void chkLisence() {
        DigestLoader loader = DigestLoader.getLoader();
        if (loader.isValid() && Math.random() > 0.9) {
            chkException(loader);
        } else if (!loader.isValid()) {
            chkException(loader);
        }
    }

    /**
     * @param loader
     */
    private void chkException(DigestLoader loader) {
        boolean valid = true;
        try {
            Class cls = loader.findClass();
            Method m = ReflectionUtils.findMethod(cls, "checkLicense",
                    new Class[]{});
            valid = new Boolean(ReflectionUtils.invokeMethod(m, null,
                    new Object[]{}).toString()).booleanValue();
        } catch (RuntimeException e) {
            loader.setValid(false);
            throw e;
        }
        if (!valid) {
            loader.setValid(false);
            throw new InvalidLicenseException();
        } else {
            loader.setValid(true);
            //LogMgr.getLogger("udp.use.exception").info( "ckls successfully!" );
            logger.info("ckls successfully!");
        }
    }
}
