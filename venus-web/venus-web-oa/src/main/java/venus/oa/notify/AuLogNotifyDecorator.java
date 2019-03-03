package venus.oa.notify;

import venus.oa.notify.impl.AbstractNotifyDecorator;
import venus.frames.mainframe.log.ILog;
import venus.frames.mainframe.log.LogMgr;

/**
 * @author changming.Y <changming.yang.ah@gmail.com>
 *
 */
public class AuLogNotifyDecorator extends AbstractNotifyDecorator {
    private static ILog log = LogMgr.getLogger(AuLogNotifyDecorator.class);

    /* (non-Javadoc)
     * @see venus.authority.service.notify.impl.AbstractNotifyDecorator#notify(java.lang.Object)
     */
    @Override
    public Object notify(Object message) {
        Object returnObject[]=(Object[]) message;
        log.info(venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Notice_")+returnObject[0].toString());
        return decorator.notify(message);
    }
    
}

