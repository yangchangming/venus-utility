package venus.oa.util.transform.json;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import venus.oa.util.transform.TransformStrategy;
import venus.frames.mainframe.log.ILog;
import venus.frames.mainframe.log.LogMgr;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

public class JsonDataTools extends TransformStrategy {
	private final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
	private static ILog log = LogMgr.getLogger(JsonDataTools.class);
	private final Object[] types = {
			Class.class,
			String.class,
			java.sql.Timestamp.class,
			java.sql.Date.class,
			java.util.Date.class,			
			int.class,
			char.class,
			boolean.class,
			long.class,
			float.class,
			double.class,
			Integer.class,
			Character.class,
			Boolean.class,
			Long.class,
			Float.class,
			Double.class,
			BigInteger.class,
			BigDecimal.class
	};	
	public JsonDataTools() {}
	
	/**
	 * json使用配置,注册日期类型
	 * @return
	 */
	private JsonConfig initJsonConfig() { //为json注册Date和Timestamp对象类型
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(java.sql.Timestamp.class, (JsonValueProcessor) new TimestampJsonValueProcessor());
		jsonConfig.registerJsonValueProcessor(java.sql.Date.class, (JsonValueProcessor) new DateJsonValueProcessor());
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, (JsonValueProcessor) new DateJsonValueProcessor());
		return jsonConfig;
	}
	
	/**
	 * 判断vo里面的属性是否支持
	 * @param obj
	 * @return
	 * @throws Exception 
	 */
	private boolean isSupportType(Object obj) {
		if (obj == null || "".equals(obj))
			return false;
		try {
			Object[] objs = BeanUtils.describe(obj).keySet().toArray();
			for (int i = 0; i < objs.length; i++) {
				PropertyDescriptor pd = PropertyUtils.getPropertyDescriptor(obj, objs[i].toString());
				if (!isSupportClass(pd.getPropertyType())) {
					throw new JSONException( "Unsupported type");
				}
			}
		} catch (Exception e) {
			log.equals(this.getClass() + " mapToBean : " + e.getMessage());
			return false;
		}
		return true;
	}	
	
	/**
	 * 判断class是否能被支持
	 * @param clazz
	 * @return
	 */
	private boolean isSupportClass(Class clazz) {
		for (int i = 0; i < types.length; i++) {
			if ( clazz == types[i])
				return true;
		}
		return false;
	}
	
	/**
	 * 将对象转换成json
	 * @param vo
	 * @return json字符串
	 */
	public String transform(Object vo) {	
		if (vo == null || "".equals(vo) || !isSupportType(vo))
			return null;
		try {
			return JSONObject.fromObject(vo,initJsonConfig()).toString();
		} catch (JSONException je) {
			log.equals(this.getClass() + " transform : " + je.getMessage());
			throw je;
		}
	}
	
	/**
	 * 解析json数据并将数据注入到vo中
	 * @param data
	 * @param vo
	 * @return vo
	 */
	public Object parse(String data,Object vo) {
		if (data == null || data.length() == 0 || !isSupportType(vo))
			return null;
		try {
			Map map = (Map)JSONObject.toBean(JSONObject.fromObject(data,initJsonConfig()),Map.class);
			return mapToBean(map,vo);
		} catch (JSONException je) {
			log.equals(this.getClass() + " parse : " + je.getMessage());
			throw je;
		}
	}
	
	/**
	 * 解析json数据并返回Map类型的数据
	 * @param data
	 * @return map
	 */	
	public Map parse(String data) {
		if (data == null || data.length() == 0)
			return null;		
		try {
			return  (Map)JSONObject.toBean(JSONObject.fromObject(data,initJsonConfig()),Map.class);
		} catch (JSONException je) {
			log.equals(this.getClass() + " parse : " + je.getMessage());
			throw je;
		}
	}
	
	/**
	 * 将json数据解析出来的map转换成与之结构相同的vo
	 * @param map
	 * @param vo
	 * @return vo
	 */
	private Object mapToBean(Map map,Object vo) {
		if (map == null || map.isEmpty())
			return vo;
		Object[] objs = map.keySet().toArray();
		String key = null;
		Object[] parameter = new Object[]{new Object()};
		try {
			for (int i = 0; i < objs.length; i++) {
				key = (String)objs[i];
				PropertyDescriptor pd = PropertyUtils.getPropertyDescriptor(vo, key);
				if (pd == null) //如果map中的key与vo中的变量不匹配，跳过，进行下一个值的注入
					continue;				
				Method writeMethod = pd.getWriteMethod();
				writeMethod.setAccessible(true);
				if (writeMethod == null) //如果vo中不存在set方法，跳过，进行下一个值的注入
					continue;
				Object param = map.get(key);
				if (param instanceof JSONObject) { //param为JSONObject实例,值为null
					param = new JSONObject().get(key);
				}
				parameter[0] = formatTransform(param,pd.getPropertyType());
				writeMethod.invoke(vo, parameter);
			}
		} catch(Exception e) {
			log.equals(this.getClass() + " mapToBean : " + e.getMessage());
			return null;
		} 
		return vo;
	}
	
	/**
	 * 将根据指定类型进行转换
	 * @param obj
	 * @param clazz
	 * @return
	 */
	private Object formatTransform(Object obj, Class clazz) {
		if (obj == null || "".equals(obj))
			return null;
		
		if (clazz == null || "".equals(clazz))
			clazz = obj.getClass();
		
		try { //遍历可以支持的数据格式  
			
			if (clazz == java.sql.Timestamp.class)
				return java.sql.Timestamp.valueOf(String.valueOf(obj));
			
			if (clazz == java.sql.Date.class) {
				java.util.Date utilDate = new SimpleDateFormat(DEFAULT_DATE_PATTERN).parse(String.valueOf(obj));
				return new java.sql.Date((utilDate).getTime());			
			}
			
			if (clazz == java.util.Date.class)
				return new SimpleDateFormat(DEFAULT_DATE_PATTERN).parse(String.valueOf(obj));
			
			if (clazz == char.class || clazz == Character.class)
				return new Character(String.valueOf(obj).toCharArray()[0]);			
			
			if (clazz == float.class || clazz == Float.class)
				return new Float(Float.parseFloat(String.valueOf(obj))); 
			
			if (clazz == long.class || clazz == Long.class)
				return new Long(Long.parseLong(String.valueOf(obj)));
			
			if (clazz == BigInteger.class)
				return new BigInteger(String.valueOf(obj));
			
			if (clazz == BigDecimal.class)
				return  new BigDecimal(String.valueOf(obj));			
			
		} catch (ParseException pe) {
			log.equals(this.getClass() + " formatTransform : " + pe.getMessage());
			return null;
		} catch (Exception e) {
			log.equals(this.getClass() + " formatTransform : " + e.getMessage());
			return null;
		}
		return obj;
	}

	public static final void main(String[] args) {

	}
	
}

