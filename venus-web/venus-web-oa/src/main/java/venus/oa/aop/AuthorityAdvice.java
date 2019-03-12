package venus.oa.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import venus.frames.mainframe.log.ILog;
import venus.frames.mainframe.log.LogMgr;
import venus.oa.notify.NotifyManager;
import venus.springsupport.BeanFactoryHelper;

import java.lang.reflect.Method;

/**
 * aroundAdvice
 * @author changming.Y <changming.yang.ah@gmail.com>
 *
 */
public class AuthorityAdvice implements MethodInterceptor  {
    private static ILog log = LogMgr.getLogger(AuthorityAdvice.class);
    private String notifyManager;
    /**
     * 1-前通知
     * 2-后通知
     * 3-环绕通知
     */
    private int position=2;
        
    /**
     * 1-前通知，2-后通知，3-环绕通知
     * @return the position
     */
    public int getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    /**
     * @param position    1-前通知，2-后通知，3-环绕通知
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * @return the notifyManager
     */
    public String getNotifyManager() {
        return notifyManager;
    }

    /**
     * @param notifyManager the notifyManager to set
     */
    public void setNotifyManager(String notifyManager) {
        this.notifyManager = notifyManager;
    }

    /**
     * 通知
     * @param obj
     * @param method
     * @param args
     * @param target
     * @param position 前advice或后advice标识
     */
    private void notify(Object obj, Method method, Object[] args,Object target,int position){
      //调用业务实现
        NotifyManager manager = (NotifyManager) BeanFactoryHelper.getBean(notifyManager);
        Object object[] = new Object[]{
          obj,
          method,
          args,
          target      
        };
        manager.notifyManage(object);
    }

    /* (non-Javadoc)
     * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
     */
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();;
        Object[] args = invocation.getArguments();
        Object target = invocation.getThis();
        if(1==position||3==position){
            log.debug("AOP-Before，class："+AuthorityAdvice.class.getName()+"，method："+method.getName());
            notify(null, method, args,target,1);
        }
        Object returnObject = invocation.proceed();
        if(2==position||3==position){
            log.debug("AOP-After，class："+AuthorityAdvice.class.getName()+"，method："+method.getName());
            notify(returnObject, method, args,target,2);
        }
        return returnObject;
    }

}

