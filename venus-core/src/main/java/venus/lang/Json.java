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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * <p> Json util </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2019-06-11 09:06
 */
public class Json {

    public static <T> T jsonToObject(Class<T> clz,List<String> jsonList){
        Type genType = clz.getClass().getGenericSuperclass();
        Class templatClazz = null;
        if(ParameterizedType.class.isInstance(genType));{
            ParameterizedType parameterizedType = (ParameterizedType) genType;
            templatClazz = (Class) parameterizedType.getActualTypeArguments()[0];
        }
        List<Object> lst = new ArrayList<Object>();
        if(templatClazz!=null && jsonList!=null) {
            for (String str : jsonList) {
//                Gson gson = new Gson();
//                Object fromJson = gson.fromJson(str, templatClazz);
//                lst.add(fromJson);
            }
        }
        return (T) lst;
    }

    public static void main(String[] args) {
//        List<String> jsonList = null;
//        Class superClazz =  new ArrayList<User>(){}.getClass();
//        List jsonToObject = Json.jsonToObject(superClazz, jsonList);

    }
}
