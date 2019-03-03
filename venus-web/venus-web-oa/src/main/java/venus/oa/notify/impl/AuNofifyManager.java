package venus.oa.notify.impl;

import venus.frames.mainframe.log.ILog;
import venus.frames.mainframe.log.LogMgr;
import venus.frames.mainframe.util.Helper;
import venus.oa.notify.NotifyDecorator;
import venus.oa.notify.NotifyManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author changming.Y <changming.yang.ah@gmail.com>
 *
 */
public class AuNofifyManager implements NotifyManager {
    private static ILog log = LogMgr.getLogger(AuNofifyManager.class);
    private List auNotifies = new ArrayList();
    private AbstractNotifyDecorator decorator = null;
    /**
     * @param auNotifies the auNotifies to set
     */
    public void setAuNotifies(List auNotifies) {
        this.auNotifies = auNotifies;
        for(int i=0;i<auNotifies.size();i++){
            log.debug(auNotifies.get(i).toString()+venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Inform_the_modifier_was_injected_"));
        }
        init();
    }
    
    private void init(){
      //上一个修饰器
        AbstractNotifyDecorator lastDecorator = null;
        //当前修饰器
        AbstractNotifyDecorator recentDecorator = null;
        //按照list顺序将修饰器递归设置
        for(int i=auNotifies.size()-1;i>=0;i--){
            recentDecorator = (AbstractNotifyDecorator) Helper.getBean((String) auNotifies.get(i));
            //如果第一次循环，则设置默认修饰器
            if(i==auNotifies.size()-1){
                recentDecorator.setDecorator(new NotifyDecorator(){
                    public Object notify(Object message) {
                        log.debug(venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Notify_the_modifier_key_is_loaded")+"!");
                        return null;
                    }});                
            }else{//如果不是第一次循环，则按递归情况设置修饰器
                recentDecorator.setDecorator(lastDecorator);
            }
            lastDecorator = recentDecorator;
        }
        decorator = recentDecorator;
    }

    /* (non-Javadoc)
     * @see venus.authority.service.notify.NotifyManager#nofiryManage(java.lang.Object)
     */
    public Object notifyManage(Object message) {        
        return decorator.notify(message);
    }

}

