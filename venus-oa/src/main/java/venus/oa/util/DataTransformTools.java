package venus.oa.util;

import venus.oa.util.transform.TransformStrategy;
import venus.oa.util.transform.json.JsonDataTools;

public class DataTransformTools {
	
	/**
	 * 将对象转换成相应格式的数据字符串
	 * @param vo
	 * @return String
	 */
	public static String transform(Object vo) {
		TransformStrategy ts = new JsonDataTools();
		return ts.transform(vo);
	}
	
	/**
	 * 根据相应格式解析数据并将数据注入到vo中
	 * @param data
	 * @param vo
	 * @return vo
	 */
	public static Object parse(String data,Object vo) {
		TransformStrategy ts = new JsonDataTools();
		return ts.parse(data,vo);
	}
}

