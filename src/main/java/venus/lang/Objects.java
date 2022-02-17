/*
 *  Copyright 2015-2018 DataVens, Inc.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package venus.lang;

import venus.base.Nullable;

/**
 * <p> Object util </p>
 * 1. Object打印优化，主要解决数组的打印
 * 2. 多个对象的HashCode串联
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2022-01-28 15:14
 */
public class Objects {

    private static final String NULL = "null";

    /**
     * JDK7 引入的Null安全的equals
     */
    public static boolean equals(@Nullable Object a, @Nullable Object b) {
        return com.google.common.base.Objects.equal(a, b);
    }

    /**
     * 多个对象的HashCode串联, 组成新的HashCode
     */
    public static int hashCode(Object... objects) {
        return java.util.Arrays.hashCode(objects);
    }

    /**
     * 对象的toString(), 处理了对象为数组的情况，JDK的默认toString()只打数组的地址如 "[Ljava.lang.Integer;@490d6c15.
     */
    public static String toPrettyString(Object value) {
        if (value == null) {
            return NULL;
        }

        Class<?> type = value.getClass();

        if (type.isArray()) {
            Class componentType = type.getComponentType();

            if (componentType.isPrimitive()) {
                return primitiveArrayToString(value, componentType);
            } else {
                return objectArrayToString(value);
            }
        } else if (value instanceof Iterable) {
            // 因为Collection的处理也是默认调用元素的toString(),
            // 为了处理元素是数组的情况，同样需要重载
            return collectionToString(value);
        }

        return value.toString();
    }

    private static String primitiveArrayToString(Object value, Class componentType) {
        StringBuilder sb = new StringBuilder();

        if (componentType == int.class) {
            sb.append(java.util.Arrays.toString((int[]) value));
        } else if (componentType == long.class) {
            sb.append(java.util.Arrays.toString((long[]) value));
        } else if (componentType == double.class) {
            sb.append(java.util.Arrays.toString((double[]) value));
        } else if (componentType == float.class) {
            sb.append(java.util.Arrays.toString((float[]) value));
        } else if (componentType == boolean.class) {
            sb.append(java.util.Arrays.toString((boolean[]) value));
        } else if (componentType == short.class) {
            sb.append(java.util.Arrays.toString((short[]) value));
        } else if (componentType == byte.class) {
            sb.append(java.util.Arrays.toString((byte[]) value));
        } else if (componentType == char.class) {
            sb.append(java.util.Arrays.toString((char[]) value));
        } else {
            throw new IllegalArgumentException("unsupport array type");
        }

        return sb.toString();
    }

    private static String objectArrayToString(Object value) {
        StringBuilder sb = new StringBuilder();
        sb.append('[');

        Object[] array = (Object[]) value;
        for (int i = 0; i < array.length; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(toPrettyString(array[i]));
        }
        sb.append(']');
        return sb.toString();
    }

    private static String collectionToString(Object value) {
        Iterable iterable = (Iterable) value;
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        int i = 0;
        for (Object o : iterable) {
            if (i > 0) {
                sb.append(',');
            }
            sb.append(toPrettyString(o));
            i++;
        }
        sb.append('}');
        return sb.toString();
    }
}
