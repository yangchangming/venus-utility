package venus.pub.util;

import java.lang.reflect.Array;
import java.util.*;

public class Convertor {

	/**
	 * Convertor 构造子注解。
	 */
	public Convertor() {
		super();
	}

	/**
	 * 二维数组的纵向连接；
	 * @param obj1 二维数组
	 * @param obj2 二维数组
	 * @return java.lang.Object[][]
	 * @exception UtilException 如果维数不匹配抛出异常
	 */
	public static final Object[][] connectArrayByVertical(
		Object[][] obj1,
		Object[][] obj2)
		throws UtilException {
		//如果二者都为空则返回空值
		if (obj1 == null && obj2 == null) {
			return null;
		}
		//如果其中一个为空另一个不为空则返回不为空的那个对象
		if (obj1 == null) {
			Object[][] obj = obj2;
			return obj;
		}
		if (obj2 == null) {
			Object[][] obj = obj1;
			return obj;
		}
		//如果维数不匹配抛出异常，即列数不相等
		if (obj1[0].length != obj2[0].length) {
			throw new UtilException("维数不匹配");
		}
		//分别取得数组的二维长度
		int v1 = obj1.length;
		int v2 = obj2.length;
		int v3 = obj1[0].length;
		Object[][] obj = new Object[v1 + v2][v3];
		//将两个数组合并成一个数组obj,逐行赋值
		for (int i = 0; i < v1; i++) {
			obj[i] = obj1[i];
		}
		for (int i = v1; i < v1 + v2; i++) {
			obj[i] = obj2[i - v1];
		}
		return obj;

	}

	/**
	 * 将对象数组转换成Vector
	 * @param obj 待转换的对象数组
	 * @return 由对象数组转化而来的Vector
	 */
	public static final Vector convertArrayToVector(Object[] obj) {
		Vector v = new Vector();
		//如果数组为空则返回空的Vector
		if (obj == null) {
			return v;
		}
		//将obj中的元素逐个导入到Vector中
		for (int i = 0; i < obj.length; i++) {
			v.addElement(obj[i]);
		}
		return v;
	}

	/**
	 * 将ENUMERATION转换成VECTOR
	 * @param enum 待转换的Enumeration对象
	 * @return 由Enumeration转化而来的Vector 
	 */
	public static final Vector convertEnumToVector(Enumeration genum) {
		Vector v = new Vector();
		//如果genum为空则返回空的Vector
		if (genum == null) {
			return v;
		}
		//将genum中的元素逐个导入到Vector中
		while (genum.hasMoreElements()) {
			v.addElement(genum.nextElement());
		}
		return v;
	}

	/**
	 * 将Vector转换成(多维)数组，前提条件是Vector中的对象必须为同一类型
	 * @param v 待转换的Vector对象
	 * @return Object 
	 */
	public static final Object convertVectorToArray(Vector v) {
		//如果v为空则返回null
		if (v.size() == 0) {
			return null;
		}
		//将Vector中的元素转移到一个数组对象中，前提条件是Vector中的对象
		//必须为同一类型
		Class classType = v.elementAt(0).getClass();
		Object o = Array.newInstance(classType, v.size());
		for (int i = 0; i < v.size(); i++) {
			Array.set(o, i, v.elementAt(i));
		}
		return o;
	}

	/**
	 * 将Vector转换成指定类型数组
	 * @param v 待转换的Vector
	 * @param classType 指定的类型
	 * @return Object
	 */
	public static final Object convertVectorToArray(
		Vector v,
		Class classType) {
		//如果v为空返回null
		if (v.size() == 0) {
			return null;
		}
		//进行转换
		Object o = Array.newInstance(classType, v.size());
		for (int i = 0; i < v.size(); i++) {
			Array.set(o, i, v.elementAt(i));
		}
		return o;
	}

	/**
	 * 将一个新的对象添加到数组中；
	 * @param array 对象数组
	 * @param newObject 要添加的对象
	 * @return java.lang.Object[]
	 * @throws UtilException
	 */
	public static final Object[] addObjectToArray(
		Object[] array,
		Object newObject) throws UtilException {
		if (array != null && newObject.getClass().getName() != array[0].getClass().getName()) {
			throw new UtilException("数据类型不匹配！");
		}
		//先将数组中的元素转移到一个Vector中
		Vector v = convertArrayToVector(array);
		//添加新的元素
		v.add(newObject);
		//返回数组
		return v.toArray();
	}

	/**
	 * 二维数组的横向连接；
	 * @param obj1
	 * @param obj2
	 * @return java.lang.Object[][]
	 * @exception UtilException 如果维数不匹配抛出异常
	 */
	public static final Object[][] connectArrayByHorizontal(
		Object[][] obj1,
		Object[][] obj2)
		throws UtilException {
		//如果二者都为空则返回空值
		if (obj1 == null && obj2 == null) {
			return null;
		}
		//如果其中一个为空另一个不为空则返回不为空的那个对象
		if (obj1 == null) {
			Object[][] obj = obj2;
			return obj;
		}
		if (obj2 == null) {
			Object[][] obj = obj1;
			return obj;
		}
		//如果维数不匹配抛出异常，即行数不相等
		if (obj1.length != obj2.length) {
			throw new UtilException("维数不匹配");
		}
		//分别取得数组的二维长度
		int v1 = obj1[0].length;
		int v2 = obj2[0].length;
		int v3 = obj1.length;
		Object[][] obj = new Object[v3][v1 + v2];
		//将两个数组合并成一个数组obj,逐列赋值
		for (int i = 0; i < v1; i++)
			for (int j = 0; j < v3; j++) {
				obj[j][i] = obj1[j][i];
			}
		for (int i = v1; i < v1 + v2; i++)
			for (int j = 0; j < v3; j++) {
				obj[j][i] = obj2[j][i - v1];
			}
		return obj;
	}

	/**
	 * 将对象数组转换成Vector；
	 * @param obj 待转换的对象数组
	 * @return Vector
	 */
	public static final Vector convertArrayListToVector(ArrayList obj) {
		Vector v = new Vector();
		//如果obj为空则返回空Vector
		if (obj == null) {
			return v;
		}
		//进行转换
		for (int i = 0; i < obj.size(); i++) {
			v.addElement(obj.get(i));
		}
		return v;
	}

	/**
	 * 将对象数组转换成ArrayList
	 * @param obj 待转换的数组
	 * @return ArrayList
	 */
	public static final ArrayList convertArrayToArrayList(Object[] obj) {
		ArrayList al = new ArrayList();
		//如果obj为空，则返回空的ArrayList
		if (obj == null) {
			return al;
		}
		//进行转换
		for (int i = 0; i < obj.length; i++) {
			al.add(obj[i]);
		}
		//返回值
		return al;
	}

	/**
	 * 将Vector转换成ArrayList；
	 * @param v 待转换的Vector
	 * @return ArrayList
	 */
	public static final ArrayList convertVectorToArrayList(
		Vector v) {
		ArrayList al = new ArrayList();
		//如果v为空，则返回空的ArrayList
		if (v == null) {
			return al;
		}
		//进行转换
		for (int i = 0; i < v.size(); i++) {
			al.add(v.elementAt(i));
		}
		return al;
	}

	/**
	 * 查找给定二维数组中给定列中是否有元素与给定字符串完全匹配(区分大小写)；
	 * 若找到，返回第一个满足条件的元素所在行的索引值，否则返回－1；
	 * @param scource 待处理的二维数组
	 * @param columNum 指定的列号
	 * @param str 参照字符串
	 * @return int 第一个满足条件的元素所在行的索引值
	 * @throws UtilException
	 */
	public static final int getMatchedRow(
		String[][] scource,
		int columNum,
		String str)
		throws UtilException {
		return getMatchedRow(scource, columNum, str, true);
	}

	/**
	 * 查找给定二维数组中给定列中是否有元素与给定字符串完全匹配;
	 * identifyCaps为true区分大小写,否则不区分；
	 * 若找到，返回第一个满足条件的元素所在行的索引值，否则返回－1；
	 * @param scource 待处理的二维数组
	 * @param columNum 指定的列号
	 * @param str 参照字符串
	 * @param identifyCaps 是否区分大小写的标记
	 * @return int 第一个满足条件的元素所在行的索引值
	 * @throws UtilException
	 */
	public static final int getMatchedRow(
		String[][] scource,
		int columNum,
		String str,
		boolean identifyCaps)
		throws UtilException {
		return getMatchedRow(scource, columNum, str, identifyCaps, false);
	}

	/**
	 * 查找给定二维数组中给定列中是否有元素与给定字符串相匹配（不分大小写）。
	 * identifyCaps为true区分大小写,否则不区分；
	 * isBlur为true,模糊匹配（前几位和给定的str一样），否则完全匹配 
	 * 若找到，返回第一个满足条件的元素所在行的索引值，否则返回－1；
	 * @param scource 待处理的二维数组
	 * @param columNum 指定的列号
	 * @param str 参照字符串
	 * @param identifyCaps 是否区分大小写的标记
	 * @param isBlur 是否模糊匹配的标记
	 * @return int 第一个满足条件的元素所在行的索引值
	 * @throws UtilException
	 */
	public static final int getMatchedRow(
		String[][] scource,
		int columNum,
		String str,
		boolean identifyCaps,
		boolean isBlur)
		throws UtilException {
		//匹配元素的行索引
		int index = -1;
		//二维数组行数
		int row = scource.length;
		//如果数组为空返回-1
		if (scource == null) {
			return index;
		}
		//如果参数列值不在数组范围之内抛出异常
		if (columNum < 0 || columNum >= scource[0].length) {
			throw new UtilException("列值超出数组范围");
		}
		/**
		 *进行查找
		 */
		//如果区分字符串的大小写
		if (identifyCaps) {
			//如果精确匹配
			if (!isBlur) {
				for (int i = 0; i < row; i++) {
					if (scource[i][columNum].compareTo(str) == 0) {
						return i;
					}
				}
			} else { //处理非精确匹配的情况
				for (int i = 0; i < row; i++) {
					if (scource[i][columNum].compareTo(str) == 0
						|| scource[i][columNum].substring(
							0,
							str.length()).compareTo(
							str)
							== 0) {
						return i;
					}
				}
			}
		}
		//不区分大小写
		else {
			//如果精确匹配
			if (!isBlur) {
				for (int i = 0; i < row; i++) {
					if (scource[i][columNum].compareToIgnoreCase(str) == 0)
						return i;
				}
			} else { //处理非精确匹配的情况
				for (int i = 0; i < row; i++) {
					if (scource[i][columNum].compareToIgnoreCase(str) == 0
						|| scource[i][columNum].substring(
							0,
							str.length()).compareToIgnoreCase(
							str)
							== 0) {
						return i;
					}
				}
			}
		}
		return index;
	}

	/**
	 * 将一个数组中的对象从数组中移出；
	 * @param array 数组
	 * @param index 待移除元素的位置索引值
	 * @return java.lang.Object[]
	 * @throws UtilException
	 */
	public static final Object[] removeObjectFromArray(
		Object[] array,
		int index) throws UtilException {
		//如果为空返回空值
		if (array == null || array.length <= 0) {
			return null;
		}
		if (index < 0 || index > array.length) {
			throw new UtilException("索引值越界！");
		}
		//现将满足条件的元素转移到Vector中
		Vector v = new Vector();
		for (int i = 0; i < array.length; i++) {
			if (i != index) {
				v.add(array[i]);
			}
		}
		//将Vector转换为数组返回
		return v.toArray();
	}

	/**
	 * 将一个数组中的对象替换成另外一个对象；
	 * @param array 数组
	 * @param newObject 新对象
	 * @param index 替换位置的索引值
	 * @return java.lang.Object[]
	 * @throws UtilException
	 */
	public static final Object[] replaceObjectFromArray(
		Object[] array,
		Object newObject,
		int index)
		throws UtilException {
		//如果为空返回空值
		if (array == null || array.length <= 0) {
			return null;
		}
		//在满足条件的情况下替换数组中的元素
		if (index >= 0 && index < array.length) {
			if (array[index].getClass() == newObject.getClass()) {
				array[index] = newObject;
			} else
				throw new UtilException("数据类型不一致");
		} else
			throw new UtilException("索引超出数组范围");
		//返回array数组
		return array;
	}
	
	
	/**
	 * 将集合转换为数组
	 * @param collection
	 * @param clazz
	 * @return clazz指定的对象的数组
	 */
	public static Object CollectionToArray(Collection collection, Class clazz) {
		Object arr = Array.newInstance(clazz, collection.size());
		int i = 0;
		for (Iterator iter = collection.iterator(); iter.hasNext(); i++) {
			Array.set(arr, i, iter.next());
		}

		return arr;
	}

	/**
	 * 将列表转换为数组
	 * @param collection
	 * @param clazz
	 * @return clazz指定的对象的数组
	 */
	public static Object ListToArray(Collection collection, Class clazz) {
		return CollectionToArray(collection, clazz);
	}

	/**
	 * 
	 * @param arr 对象数组,不支持值数组
	 * @return 对象列表
	 */
	public static List ArrayToList(Object arr) {
		List list = new LinkedList();
		for (int i = 0; true; i++) {
			try {
				list.add(Array.get(arr, i));
			} catch (Exception e) {
				break;
			}
		}
		return list;
	}
	
	public static String[] copyVectorToStringArray(Vector v){
		
		int len = v.size();
		
		if ( len < 1 ) return new String[0];
		
		String[] re = new String[len];
		
		Iterator iter = v.iterator();
		
		int i = 0;
		while(iter.hasNext()){
			
			re[i] = (String)iter.next();
			i++;		
		}
				
		return re;
		
	}
	
}
