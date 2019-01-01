//Source file: D:\\venus_view\\Tech_Department\\Platform\\Venus\\4项目开发\\1工作区\\4实现\\venus\\frames\\mainframe\\login\\action\\FormAttribute.java

package venus.frames.mainframe.login.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;


/**
 * 用于同Form中的属性对象对应<br>
 * 
 * 对应于XML中的attr中的属性值构成的对象<br>
 * 
 * <LogonActionForm validate="true">
 * <attr label="登录名" name="userName" profileKey="SYS_LOGIN_NAME" />
 * <attr label="密码" name="userPass" profileKey="SYS_LOGIN_PASSWARD" />
 * <attr label="登录单位编码" name="unitCode" profileKey="SYS_LOGIN_UNITCODE" />
 * <attr label="登录业务日期" name="bizDate" profileKey="SYS_LOGIN_BIZDATE" />
 * <attr label="用户主机ID" name="hostId" profileKey="SYS_LOGIN_HOSTID" />
 * </LogonActionForm>
 * 
 * @author 张文韬
 */
public class FormAttribute implements Serializable {
	private String m_strKey = null;
	private String m_strName = null;
	private String m_strValue = null;
	private String m_strLabel = null;

	private String m_strRawTag = null;
	private String m_strType = null;
	private String m_strValidator = null;
	private String m_strRef = null;
	private String m_strOutputString = "";
	
	public static final String FORM_TEXT_TYPE = "text";
	public static final String FORM_PASSWORD_TYPE = "password";
	public static final String FORM_SELECT_TYPE = "select";	
	public static final String FORM_REF_CALENDAR_TYPE = "calendar";
	public static final String FORM_REF_OPTION_START_KEY = "option://";
	
	
	/**
	 * 构造函数
	 * @roseuid 3FBB62FC01E4
	 */
	public FormAttribute() {
		super();
	}

	/**
	 * 构造函数<br>
	 * 
	 * 用于存储formbean中的每个属性值对象，对应于XML文档中的相关属性
	 * @param key 对应于xml文档中相关节点下的profileKey属性值
	 * @param label 对应于xml文档中相关节点下的label属性值
	 * @param value 从页面传入的参数值
	 * @param name 对应于xml文档中相关节点下的name属性值
	 * @param rawTag
	 * @param type
	 * @param validator
	 * @param ref
	 * @roseuid 3FBB5A0002CA
	 */
	public FormAttribute(String key, String label, String value, String name, String rawTag, String type, String validator, String ref) {
		this.setKey(key);
		this.setLabel(label);
		this.setName(name);
		this.setRawTag(rawTag);
		this.setRef(ref);
		this.setValidator(validator);
		this.setValue(value);
		this.setType(type);
		
	}

	/**
	 * 取得m_strKey值
	 * @return String xml文档中相关节点下的profileKey属性值
	 * @roseuid 3FBB5ABF0237
	 */
	public String getKey() {
		return this.m_strKey;
	}

	/**
	 * 取得m_strLabel值
	 * @return String 对应于xml文档中相关节点下的label属性值
	 * @roseuid 3FBB5ACF01E9
	 */
	public String getLabel() {
		return this.m_strLabel;
	}

	/**
	 * 取得m_strValue值
	 * @return String 从页面传入的参数值
	 * @roseuid 3FBB5ADA0321
	 */
	public String getValue() {
		if( FORM_REF_CALENDAR_TYPE.equals(this.getRef()) && ( m_strValue == null || m_strValue.length()<2)){
			 

			Calendar C=new GregorianCalendar();

			 
			 
			this.m_strValue = C.get(Calendar.YEAR)+"-"+(C.get(Calendar.MONTH)<9?"0":"") + (C.get(Calendar.MONTH)+1)+"-"+C.get(Calendar.DAY_OF_MONTH);
				
		}
		return this.m_strValue;
	}

	/**
	 * 取得m_strName值
	 * @return String xml文档中相关节点下的name属性值
	 * @roseuid 3FBB5AE60042
	 */
	public String getName() {
		return this.m_strName;
	}

	/**
	 * 设置m_strKey值
	 * @param arg0 设置m_strKey的参数
	 *
	 * @roseuid 3FBB5B2602F0
	 */
	public void setKey(String arg0) {
		this.m_strKey = arg0;
	}

	/**
	 * 设置m_strLabel值
	 * @param arg0 设置m_strLabel的参数
	 * @roseuid 3FBB5B2D02B1
	 */
	public void setLabel(String arg0) {
		this.m_strLabel = arg0;
	}

	/**
	 * 设置m_strValue值
	 * @param arg0 设置m_strValue的参数
	 * @roseuid 3FBB5B33006F
	 */
	public void setValue(String arg0) {
		this.m_strValue = arg0;
	}

	/**
	 * 设置m_strName值
	 * @param arg0 设置m_strName的参数
	 * @roseuid 3FBB5B4F011A
	 */
	public void setName(String arg0) {
		this.m_strName = arg0;
	}
	/**
	 * @return 返回 m_strRawTag。
	 */
	public String getRawTag() {
		return " id='"+this.getName()+"Id' "+m_strRawTag;
	}
	/**
	 * @param rawTag 要设置的 m_strRawTag。
	 */
	public void setRawTag(String rawTag) {
		m_strRawTag = rawTag;
	}
	/**
	 * @return 返回 m_strRef。
	 */
	public String getRef() {
		return m_strRef;
	}
	
	/**
	 * @return 返回 m_strRef。
	 */
	public String getRefTag() {
		if( FORM_REF_CALENDAR_TYPE.equals(getRef()) ) {
			
			return "";
			
		}
		return "";
	}
	/**
	 * @param ref 要设置的 m_strRef。
	 */
	public void setRef(String ref) {
		m_strRef = ref;
	}
	/**
	 * @return 返回 m_strType。
	 */
	public String getType() {
		return m_strType;
	}
	/**
	 * @param type 要设置的 m_strType。
	 */
	public void setType(String type) {
		m_strType = type;
		
		if (FORM_SELECT_TYPE.equals(type) ) {
			
			prepareOutputString();			
			
		}
	}
	private void prepareOutputString(){
		
		String strRef = getRef();
		
		StringBuffer sb = new StringBuffer("<SELECT NAME='"+getName()+"' SIZE='1'>");
		
		
		if (strRef.startsWith(FORM_REF_OPTION_START_KEY)){
			
			String strOptions = strRef.substring(FORM_REF_OPTION_START_KEY.length());
			
			ArrayList v = new ArrayList();
			String[] strAry = null;
			if (strOptions != null && strOptions.length() > 0) {
				StringTokenizer st = new StringTokenizer(strOptions, ";");

				while (st.hasMoreTokens()) {
					v.add(st.nextToken());
				}
			}
			
//			E001:子系统1;E002:子系统2
			/*
			<SELECT NAME="Cats" SIZE="1">
			<OPTION VALUE="1">Calico
			<OPTION VALUE="2">Tortie
			<OPTION VALUE="3" SELECTED>Siamese
			</SELECT>
			*/
			
			int len = v.size();
			if (len > 0) {
				for (int x = 0 ; x<len ; x++ ){
					
					String strOptionItem = (String)v.get(x);
					
					StringTokenizer st = new StringTokenizer(strOptionItem, "|");
					
					sb.append("<OPTION VALUE='"+st.nextToken()+"'>"+st.nextToken()+"</OPTION>");
				
				}

			}
			
			sb.append("</SELECT>");
			
			m_strOutputString = sb.toString();		

		
		}
		
	
	}
	
	/**
	 * @return 返回 m_strValidator。
	 */
	public String getValidator() {
		
		
		return m_strValidator.length()>0 ? " validators='"+m_strValidator+"' " : "";
	}
	
	/**
	 * @param validator 要设置的 m_strValidator。
	 */
	public void setValidator(String validator) {
		m_strValidator = validator;
	}
	
	public String getOutputString(){
		
		return m_strOutputString;
	}
}
