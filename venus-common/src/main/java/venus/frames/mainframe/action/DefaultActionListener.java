package venus.frames.mainframe.action;

import venus.frames.base.IGlobalsKeys;
import venus.frames.mainframe.actionevent.bs.IActionEventBS;
import venus.frames.mainframe.log.LogMgr;
import venus.frames.mainframe.util.BeanFactoryHolder;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * 缺省Action变更事件监听器
 * 
 * 主要用于响应 web端的 ACTION变更后
 * 
 * 驱动远端BS初始化或者处理数据等操作
 * 
 * @author 岳国云
 */
public class DefaultActionListener implements PropertyChangeListener,IGlobalsKeys {

	/**
	 * @roseuid 3FBAFAD800DA
	 */
	public DefaultActionListener() {
		super();
	}

	/**
	 * 缺省Action 变更事件监听器的响应事件方法
	 * 
	 * 先将传入参数转型为 ActionChangeEvent evt
	 * 
	 * 调用 evt.getPropertyName() 得到是什么事件：
	 * 
	 * 构建事件：ActionChangeEvent.BUILD_KEY,
	 * 调用代理 ActionEventBS_C create(...) 传入参数: 
	 *   sessionid: evt.getSessionId();
	 *   loginName: evt.getOldSatte();
	 * 
	 * 销毁事件：ActionChangeEvent.ERASE_KEY
	 * 调用代理 ActionEventBS_C erase(...) 传入参数: 
	 *   sessionid: evt.getSessionId();
	 *   oldState: evt.getOldSatte();
	 * 
	 * Action变更事件：ActionChangeEvent.ACTION_CHG_KEY
	 * 调用代理 ActionEventBS_C changeAction(...) 传入参数: 
	 *   sessionid: evt.getSessionId();
	 *    oldState: evt.getOldSatte();
	 *    newValue: evt.getNewValue()
	 *    oldValue: evt.getOldValue()
	 * 
	 * Path变更事件：ActionChangeEvent.PATH_CHG_KEY
	 * 调用代理 ActionEventBS_C changePath(...) 传入参数: 
	 *   sessionid: evt.getSessionId();
	 *   oldState: evt.getOldSatte();
	 *   newValue: evt.getNewValue()
	 *   oldValue: evt.getOldValue()
	 * 
	 * @param evt  ActionChangeEvent事件对象
	 * 
	 * @roseuid 3FBAE1890196
	 */
	public void propertyChange(PropertyChangeEvent evt) {

		/***********/
		LogMgr.getLogger(this.getClass().getName()).info(
			"DefaultActionListener.propertyChange(...)");
		/***********/

		if (evt == null) {
			LogMgr.getLogger(this.getClass().getName()).error(
				"propertyChange(PropertyChangeEvent evt):parameter can't be null");
			return;
		}

		//将evt由PropertyChangeEvent转型为ActionChangeEvent
		ActionChangeEvent aevt = null;
		try {
			aevt = (ActionChangeEvent) evt;
		} catch (ClassCastException e) {
			LogMgr.getLogger(this.getClass().getName()).error(
				"propertyChange(...): ActionChangeEvent type conversion exception",
				e);
			return;
		}

		//得到事件类型
		String key = aevt.getPropertyName();
		if (key == null) {
			LogMgr.getLogger(this.getClass().getName()).error(
				"propertyChange(...) : aevt.getPropertyName() return null");
			return;
		}

		IActionEventBS actioneventBS_C = (IActionEventBS) BeanFactoryHolder.getBeanFactory().getBean(ACTION_EVENT_BS_NAME);

		//如果为构建事件,则调用Client端create方法
		if (key == ActionChangeEvent.BUILD_KEY) {

				actioneventBS_C.create(aevt.getSessionId(), aevt.getOldState());

		}

		//如果为销毁事件,则调用Client端erase方法
		if (key == ActionChangeEvent.ERASE_KEY) {

				actioneventBS_C.erase(aevt.getSessionId(), aevt.getOldState());

		}

		//如果为Action变更事件,则调用Client端changeAction方法
		if (key == ActionChangeEvent.ACTION_CHG_KEY) {

			actioneventBS_C.changeAction(
					aevt.getSessionId(),
					aevt.getOldState(),
					(String) aevt.getOldValue(),
					(String) aevt.getNewValue());

		}

		//如果为Path变更事件,则调用Client端changePath方法
		if (key == ActionChangeEvent.PATH_CHG_KEY) {

				actioneventBS_C.changePath(
					aevt.getSessionId(),
					aevt.getOldState(),
					(String) aevt.getOldValue(),
					(String) aevt.getNewValue());

		}

	}
}
