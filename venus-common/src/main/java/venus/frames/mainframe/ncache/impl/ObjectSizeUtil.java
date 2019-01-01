package venus.frames.mainframe.ncache.impl;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * 计算对象或基础类型的大小
 * 
 * 基础类型的大小如下：boolean:1 byte:1 short:2 char:2 int:4 float:4 long:8 double:8
 * array:数组各元素的和 null reference:4 object reference:8
 * 
 * 获取对象实例的大小需要递归的累加对象各非Static属性的大小
 * 
 */
public class ObjectSizeUtil {
    final private static int BOOLEAN = 1;

    final private static int BYTE = 1;

    final private static int SHORT = 2;

    final private static int CHAR = 2;

    final private static int INT = 4;

    final private static int FLOAT = 4;

    final private static int LONG = 8;

    final private static int DOUBLE = 8;

    final private static int REFERENCE = 4;

    final private static int OBJECT = 8;

    private static ThreadLocal objs = new ThreadLocal();

    private static void init(Object o) {
	Map map = new IdentityHashMap();
	map.put(o, null);
	objs.set(map);
    }

    public static int sizeof(boolean i) {
	return BOOLEAN;
    }

    public static int sizeof(byte b) {
	return BYTE;
    }

    public static int sizeof(short s) {
	return SHORT;
    }

    public static int sizeof(char c) {
	return CHAR;
    }

    public static int sizeof(int i) {
	return INT;
    }

    public static int sizeof(float f) {
	return FLOAT;
    }

    public static int sizeof(long l) {
	return LONG;
    }

    public static int sizeof(double d) {
	return DOUBLE;
    }

    public static int sizeof(Object o) {
	init(o);
	return sizeof0(o);
    }

    private static int sizeof0(Object o) {
	if (o == null)
	    return REFERENCE;
	Object object = null;
	if (o.getClass().getName().equals("java.util.LinkedList")) {
	    object = ((LinkedList) o).toArray();
	} else {
	    object = o;
	}
	int size = OBJECT;
	// if the object is null

	Map map = (Map) objs.get();
	Class c = object.getClass();

	// if it is array
	if (c.isArray()) {
	    int[] dimension = getDimension(object);
	    int len = dimension.length;

	    Object obj = object;

	    int num = 1;
	    for (int j = 0; j < len - 1; j++)
		num *= dimension[j];
	    if (dimension[len - 1] == 0) {
		size += num * REFERENCE;
	    } else {
		num *= dimension[len - 1];
		// 处理递归
		int[] index;

		Class type = c;
		while (type.isArray())
		    type = type.getComponentType();
		// 基本类型的数组
		if (type.isPrimitive()) {
		    size += num * sizeofPrimitive(type);
		}
		// 引用类型数组
		else {

		    for (int k = 0; k < num; k++) {
			index = countToIndex(k, dimension);
			Object temp = obj;
			for (int m = 0; m < len; m++) {
			    temp = Array.get(temp, index[m]);
			}
			// 加入数组中的所有对象

			if (!map.containsKey(temp)) {
			    size += sizeof0(temp);
			    map.put(temp, null);
			}
		    }
		}
	    }
	}

	// all not-static fields
	Field[] fs = getFields(object.getClass());

	for (int i = 0; i < fs.length; i++) {

	    Field f = fs[i];
	    if (!Modifier.isStatic(f.getModifiers())) {

		Class type = f.getType();
		// if it is primitive
		if (type.isPrimitive()) {
		    size += sizeofPrimitive(type);
		}
		// recurtive
		else {
		    Object obj = null;
		    try {
			obj = f.get(object);
		    } catch (IllegalAccessException e) {
			// won''''t be happen
			throw new RuntimeException("impossible");
		    }
		    if (!map.containsKey(obj)) {
			size += sizeof0(obj);
			map.put(obj, null);
		    }
		}
	    }
	}

	return size;
    }

    private static int[] countToIndex(int count, int[] d) {
	int[] res = new int[d.length];
	int c = count;
	int i = 1;
	while (c > 0) {
	    int t = 1;
	    for (int j = i; j < d.length; j++)
		t *= d[j];
	    if (t > c)
		i++;
	    else {
		res[i - 1] = c / t;
		c = c % t;
	    }
	}
	return res;
    }

    private static int sizeofPrimitive(Class c) {
	if (c == boolean.class) {
	    return BOOLEAN;
	} else if (c == byte.class) {
	    return BYTE;
	} else if (c == char.class) {

	    return CHAR;
	} else if (c == short.class) {
	    return SHORT;
	} else if (c == int.class) {
	    return INT;
	} else if (c == float.class) {
	    return FLOAT;
	} else if (c == long.class) {
	    return LONG;
	} else if (c == double.class) {
	    return DOUBLE;
	} else {
	    throw new IllegalArgumentException("Thrown by sizeOfPrimitive()");
	}
    }

    private static int[] getDimension(Object obj) {
	int dimension = 0;
	Class c = obj.getClass();
	while (c.isArray()) {
	    dimension++;
	    c = c.getComponentType();
	}
	int[] res = new int[dimension];

	Object o = obj;
	for (int i = 0; i < dimension - 1; i++) {
	    res[i] = Array.getLength(o);
	    o = Array.get(o, 0);
	}
	res[dimension - 1] = Array.getLength(o);

	return res;
    }

    private static Field[] getFields(Class c) {
	Class superClass = c.getSuperclass();
	Field[] s = null;
	if (superClass != null) {
	    getFields(superClass);
	}
	Field[] fs = c.getDeclaredFields();

	// 设置为可访问的
	Field.setAccessible(fs, true);

	// 合并
	int size = 0;
	if (s != null)
	    size += s.length;
	if (fs != null)
	    size += fs.length;

	Field[] result = new Field[size];
	int index = 0;
	if ((s != null) && (s.length > 0)) {
	    System.arraycopy(s, 0, result, 0, s.length);
	    index += s.length;
	}
	if ((fs != null) && (fs.length > 0)) {
	    System.arraycopy(fs, 0, result, index, fs.length);
	}

	return result;
    }

    public static void main(String[] args) {
	List list1 = new LinkedList();
	List list = new ArrayList();
	list.add("111");
	list.add(list1);
	System.out.println(list.getClass());
	System.out.println(ObjectSizeUtil.sizeof(list));
    }
}
