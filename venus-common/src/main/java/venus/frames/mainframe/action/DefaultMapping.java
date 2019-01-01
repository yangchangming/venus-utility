package venus.frames.mainframe.action;

import org.apache.struts.action.ActionMapping;
import venus.frames.base.action.DefaultForward;
import venus.frames.base.action.IForward;
import venus.frames.base.action.IMappingCfg;

import java.util.Hashtable;

/**
 * 用于存储该业务组件Action 的配置数据的对象缺省的具体实现对象
 * 
 * 包装了org.apache.struts.action.ActionMapping
 * 
 * @author 岳国云
 */
public class DefaultMapping implements IMappingCfg {

	/**
	 * 存储被包装的 org.apache.struts.action.ActionMapping
	 */
	private ActionMapping m_actionmapping = null;

	/**
	 * 存储该 map 中所有forward 的实例
	 */
	private Hashtable m_hashForwards = new Hashtable();

	/**
	 * 构造器
	 * 
	 * @param am 
	 * @roseuid 3FAB5CCA02BE
	 */
	public DefaultMapping(ActionMapping am) {
		this.m_actionmapping = am;
	}

	/**
	 * 返回该对象内包装的 org.apache.struts.action.ActionMapping 对象
	 * 
	 * @return ActionMapping - org.apache.struts.action.ActionMapping 对象
	 * @roseuid 3FACF6990002
	 */
	public ActionMapping getActionMapping() {
		return this.m_actionmapping;
	}

	/**
	 * 得到配置中的该Action配置数据中传入的参数
	 * 
	 * @return String - 配置中的该Action配置数据中传入的参数
	 * @roseuid 3FAD00BA00A1
	 */
	public String getParameter() {

		if (m_actionmapping == null)
			return null;

		String parameter = null;
		parameter = this.m_actionmapping.getParameter();
		return parameter;
	}

	/**
	 * 在该配置对象中根据名字查到目标URL
	 * 
	 * @param name  - 配置文件中的页面跳转逻辑名
	 * @return venus.frames.mainframe.base.action.IForward 页面跳转对象
	 * @roseuid 3FAD01440226
	 */
	public IForward findForward(String name) {

		//如果存储有该名字的实例，则取出并返回该实例
		if (m_hashForwards.containsKey(name))
			return (IForward) m_hashForwards.get(name);

		//如果没有，则构建DefaultForward实例，然后存储并返回此实例
		if (this.m_actionmapping != null) {
			DefaultForward df =
				new DefaultForward(this.m_actionmapping.findForward(name));
			m_hashForwards.put(name, df);
			return df;
		}
		return null;
	}

	/**
	 * 返回该 Action 组件对输入的表单数据是否需要校验 
	 * 
	 * @return boolean 
	 * @roseuid 3FAD01440244
	 */
	public boolean getValidate() {
		boolean validate = true;
		if (this.m_actionmapping != null) {
			validate = this.m_actionmapping.getValidate();
		}
		return validate;
	}

	/**
	 * 返回该 Action 组件配置中name 参数的值
	 * 
	 * @return String - 该 Action 组件配置中name 参数的值
	 * @roseuid 3FAD0144026C
	 */
	public String getFormName() {
		if (this.m_actionmapping != null) {
			return this.m_actionmapping.getName();
		}
		return null;
	}

	/**
	 * 返回该 Action 组件对应的路径名
	 * 
	 * @return String - 该 Action 组件对应的路径名
	 * @roseuid 3FAD0144028A
	 */
	public String getPath() {
		if (this.m_actionmapping != null) {
			return this.m_actionmapping.getPath();
		}
		return null;
	}

	/**
	 * 返回该 Action 组件有效生命周期范围
	 * 
	 * @return String - 该 Action 组件有效生命周期范围
	 * @roseuid 3FAD014402C6
	 */
	public String getScope() {
		String scope = null;
		
		if (m_actionmapping != null) {
			scope = this.m_actionmapping.getScope();
		}
		return scope;
	}

	/**
	 * 为该 Action 组件配置数据对象中加入新的转向url
	 * 
	 * @param forward - 该 Action 组件配置数据对象中加入新的转向url
	 * @roseuid 3FAD014402E4
	 */
/*
	public void addForward(IForward forward) {
		if (forward != null) {
			m_hashForwards.put(forward.getName(), forward);
			if (m_actionmapping != null)
				this.m_actionmapping.addForward((ActionForward) forward);
		}
	}
*/
	/**
	 * 返回该Action 组件对应的缺省INPUT页面的URL
	 * 
	 * @return String - 该Action 组件对应的缺省INPUT页面的URL
	 * @roseuid 3FAD0144030C
	 */
	public String getInputPage() {
		
		if (this.m_actionmapping != null) {
			return this.m_actionmapping.getInput();
		}
		return null;
	}
	
	/**
	 * 返回该Action 组件对应的缺省INPUT页面的URL
	 * 
	 * @return String - 该Action 组件对应的缺省INPUT页面的URL
	 * @roseuid 3FAD0144030C
	 */
	public IForward getInputForward() {
		
		if (this.m_actionmapping != null) {
			return new DefaultForward(this.m_actionmapping.getInputForward());
		}
		return null;
	}
}
