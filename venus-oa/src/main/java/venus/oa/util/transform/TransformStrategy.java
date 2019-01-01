package venus.oa.util.transform;

import java.util.Map;

public abstract class TransformStrategy {

	/**
	 * 将对象转换成相应格式的数据字符串
	 * @param vo
	 * @return String
	 */
	public abstract String transform(Object vo);
	
	/**
	 * 根据相应格式解析数据并将数据注入到vo中
	 * @param data
	 * @param vo
	 * @return vo
	 */
	public abstract Object parse(String data,Object vo);
	
	/**
	 * 解析json数据并返回Map类型的数据
	 * @param data
	 * @return map
	 */		
	public abstract Map parse(String data);
	
}

