package venus.oa.notify;

import venus.oa.notify.impl.AbstractNotifyDecorator;
import venus.oa.sysparam.vo.SysParamVo;
import venus.oa.util.GlobalConstants;
import venus.oa.util.common.dao.impl.CommonDao;
import venus.springsupport.BeanFactoryHelper;

import java.lang.reflect.Method;

/**
 * 如果禁用PWDLIFECYCLE，那么用户列表中的密码过期时间应置空
 * @author changming.Y <changming.yang.ah@gmail.com>
 *
 */
public class PwdLifeCycleNotifyDecorator extends AbstractNotifyDecorator {

//    private static ILog log = LogMgr.getLogger(PwdLifeCycleNotifyDecorator.class);

    /* (non-Javadoc)
     * @see venus.authority.service.notify.impl.AbstractNotifyDecorator#notify(java.lang.Object)
     */
    @Override
    public Object notify(Object obj) {
        Object returnObject[]=(Object[]) obj;
        Object message[] = (Object[])returnObject[2];//第三个是参数
        SysParamVo sysvo = (SysParamVo)message[0];
        if(GlobalConstants.PWDLIFECYCLE.equals(sysvo.getPropertykey())){
            Method method = (Method) returnObject[1];
            if("0".equals(sysvo.getEnable())||"update".equals(method.getName())){//如果是禁用或者更新
//                log.debug("禁用PWDLIFECYCLE时用户列表中的密码过期时间应置空。");
                CommonDao dao = (CommonDao) BeanFactoryHelper.getBean("CommonDao");
                dao.update("UPDATE AU_USER SET RETIRE_DATE = NULL");
            }
        }
        return decorator.notify(obj);
    }
    
}

