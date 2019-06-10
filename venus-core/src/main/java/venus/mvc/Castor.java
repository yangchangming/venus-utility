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
package venus.mvc;

import venus.lang.Clazz;

import java.lang.reflect.Field;
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
     * 属性map集合转为自定义对象
     * 1. 必须是自定义对象
     * 2. 自定义对象属性必须为原生类型，暂时不支持属性为自定义对象或者集合
     * 3. map中存放的是自定义对象的属性
     * 4. todo日期类型？
     *
     * @param httpParamMap {(age->18),(name->"中本聪"),...}
     * @param targetClz 自定义对象class
     * @return
     */
    public static Object stringToNonPrimitive(Map<String, String> httpParamMap, Class<?> targetClz){
        if (targetClz!=null && !Clazz.isPrimitive(targetClz)){
            Object o = Clazz.newInstance(targetClz);
            for (Field field : targetClz.getDeclaredFields()) {
                if (httpParamMap.containsKey(field.getName()) && Clazz.isPrimitive(field.getDeclaringClass())){
                    Clazz.setFieldValue(field, o, stringToClzInstance(httpParamMap.get(field.getName()), field.getDeclaringClass()));
                }
            }
            return o;
        }
        return null;
    }

    /**
     * 属性map集合转为自定义对象集合
     *
     * @param httpParamMap {(user[0]->((name->"中本聪"),(age->33),...)),(user[1]->((age->22),(name->"中本聪"),...)),...}
     * @param targetClz Array or Set or List
     * @return
     */
    public static Object stringSetToNonPrimitive(Map<String, Map<String, String>> httpParamMap, Class<?> targetClz){

        //todo convert
        return null;
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
        return null;
    }

}
