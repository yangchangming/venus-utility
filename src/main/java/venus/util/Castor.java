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
package venus.util;

import venus.lang.Clazz;
import venus.lang.Datee;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * <p> Cast type of object utils </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2019-06-10 14:15
 */
public final class Castor {

    /**
     * cast string to target class instance
     *
     * @param source
     * @param targetClz
     * @return
     */
    public static Object stringToClzInstance(String source, Class<?> targetClz){
        if (Clazz.isPrimitive(targetClz)){
            if (source==null || "".equals(source)){
                return primitiveToNull(targetClz);
            }
            return stringToPrimitive(source, targetClz);
        }
        return null;
    }

    /**
     * 属性map集合转为自定义类型
     * 1. 必须是自定义类型
     * 2. 自定义类型属性必须为原生类型，暂时不支持属性为自定义类型或者集合
     * 3. map中存放的是自定义类型对象的属性
     * 4. 属性是日期类型，当前支持默认的java.util.Date
     *
     * @param httpParamMap {(age->18),(name->"中本聪"),...}
     * @param targetClz 自定义对象class
     * @return
     */
    public static Object stringToNonPrimitive(Map<String, String> httpParamMap, Class<?> targetClz){
        if (targetClz!=null && !Clazz.isPrimitive(targetClz)){
            return Castor.fillBeanByMap(targetClz, httpParamMap);
        }
        return null;
    }

    /**
     * 属性map集合转为自定义对象集合
     * 1. 集合中的对象必须是自定义类型
     * 2. 目标集合类型 List Array，不保证原有集合对象顺序(todo List目前不支持，无法获取泛型类型)
     * 3. 自定义类型属性必须是原生类型，暂时不支持属性为自定义类型或者集合
     * 4. 属性是日期类型，当前支持默认的java.util.Date
     *
     * @param httpParamMap {(user[0]->((name->"中本聪"),(age->33),...)),(user[1]->((age->22),(name->"中本聪"),...)),...}
     * @param targetClz Array List (todo 分开写)
     * @return
     */
    public static Object stringToNonPrimitiveSet(Map<String, Map<String, String>> httpParamMap, Class<?> targetClz){
        Class<?> componentClz = null;
        if (targetClz.isArray()){
            Object arrObj;
            componentClz = targetClz.getComponentType();
            arrObj = Array.newInstance(componentClz, httpParamMap.size());
            int index = 0;
            Iterator<String> iterator = httpParamMap.keySet().iterator();
            while (iterator.hasNext()){
                String key = iterator.next();
                Array.set(arrObj, index, Castor.fillBeanByMap(componentClz, httpParamMap.get(key)));
                index++;
            }
            return arrObj;

        }else if (Clazz.isImplementsInterface(targetClz, List.class)){
//            Type genType = targetClz.getGenericSuperclass();
//            if (ParameterizedType.class.isInstance(genType)){
//                ParameterizedType parameterizedType = (ParameterizedType)genType;
//                componentClz = (Class<?>)parameterizedType.getActualTypeArguments()[0];
//            }

            final Class<?> _comonentClz = Object.class;
            List result = new ArrayList();
            httpParamMap.keySet().stream().forEach(key -> result.add(Castor.fillBeanByMap(_comonentClz, httpParamMap.get(key))));
            return result;
        }
        return null;
    }

    /**
     * fill bean field by map
     * 1. the field class in map must be primitive
     *
     * @param targetBeanClz
     * @param fieldMap
     * @return
     */
    public static Object fillBeanByMap(final Class<?> targetBeanClz, Map<String, String> fieldMap){
        Object o = Clazz.newInstance(targetBeanClz);
        for (Field field : targetBeanClz.getDeclaredFields()) {
            if (fieldMap.containsKey(field.getName()) && Clazz.isPrimitive(field.getType())){
                Clazz.setFieldValue(field, o, stringToClzInstance(fieldMap.get(field.getName()), field.getType()));
            }
        }
        return o;
    }


    /**
     * cast string to primitive value, source must be String
     *
     * @param source
     * @param targetClz
     * @return
     */
    public static Object stringToPrimitive(String source, Class<?> targetClz) {
        if (Clazz.isPrimitive(targetClz) && source!=null && !"".equals(source)){
            if (targetClz.equals(int.class) || targetClz.equals(Integer.class)){
                return Integer.valueOf(source);
            }else if (targetClz.equals(String.class)){
                return source;
            }else if (targetClz.equals(Double.class) || targetClz.equals(double.class)) {
                return Double.parseDouble(source);
            } else if (targetClz.equals(Float.class) || targetClz.equals(float.class)) {
                return Float.parseFloat(source);
            } else if (targetClz.equals(Long.class) || targetClz.equals(long.class)) {
                return Long.parseLong(source);
            } else if (targetClz.equals(Boolean.class) || targetClz.equals(boolean.class)) {
                return Boolean.parseBoolean(source);
            } else if (targetClz.equals(Short.class) || targetClz.equals(short.class)) {
                return Short.parseShort(source);
            } else if (targetClz.equals(Byte.class) || targetClz.equals(byte.class)) {
                return Byte.parseByte(source);
            } else if (targetClz.equals(java.util.Date.class)){
                return Datee.stringToDate(source);
            }
        }
        return null;
    }

    public static Object primitiveToNull(Class<?> clz) {
        if (clz.equals(int.class) || clz.equals(double.class) || clz.equals(short.class) ||
                clz.equals(long.class) || clz.equals(byte.class) || clz.equals(float.class)) {
            return 0;
        }
        if (clz.equals(boolean.class)) {
            return false;
        }
        if (clz.equals(java.util.Date.class)){
            return null;
        }
        return null;
    }

}
