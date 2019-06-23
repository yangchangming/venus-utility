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

import org.junit.Assert;
import org.junit.Test;
import user.model.User;
import venus.lang.Clazz;

import java.util.*;

/**
 * <p>  </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2019-06-11 11:06
 */
public class CastorTest {

    @Test
    public void testPrimitive2Null(){
        Assert.assertNull(Castor.primitiveToNull(User.class));
        Assert.assertEquals(false, Castor.primitiveToNull(boolean.class));
        Assert.assertEquals(0, Castor.primitiveToNull(double.class));
    }

    @Test
    public void testString2Primitive(){
        Assert.assertNull(Castor.stringToPrimitive("", double.class));
        Assert.assertNotSame(Integer.class, Castor.stringToPrimitive("22", double.class));
        Assert.assertEquals((float)44, Castor.stringToPrimitive("44", float.class));
    }

    @Test
    public void testStringToClzInstance(){
        Assert.assertEquals(34, Castor.stringToClzInstance("34", Integer.class));
        Assert.assertNull(Castor.stringToClzInstance("name", User.class));
    }

    @Test
    public void testStringToNonPrimitive(){
        Map<String, String> user = new HashMap<>();
        user.put("nickName", "ychm");
        user.put("height", "18");
        user.put("birthday", "2018-11-12 12:18:33");
        Object o = Castor.stringToNonPrimitive(user, User.class);
        Assert.assertEquals("ychm", ((User)o).getNickName());
        Assert.assertEquals(18.0, ((User)o).getHeight(), 0);
        Assert.assertEquals(Date.class, ((User)o).getBirthday().getClass());
    }

    @Test
    public void testAssembleToArray(){
        Map<String, Map<String, String>> requestParms = new HashMap<>();
        Map<String, String> user1 = new HashMap<>();
        Map<String, String> user2 = new HashMap<>();
        user1.put("nickName", "ychm");
        user1.put("height", "18");
        user1.put("birthday", "2018-11-12 12:18:33");
        user2.put("nickName", "wujing");
        user2.put("height", "45");
        user2.put("birthday", "2011-11-01 10:28:33");
        requestParms.put("user[0]", user1);
        requestParms.put("user[1]", user2);
        User[] users = new User[]{};
        Object arrObj = Castor.stringToNonPrimitiveSet(requestParms, users.getClass());
        Assert.assertTrue(arrObj.getClass().isArray());
    }

    @Test
    public void testAssembleToList(){
        Map<String, Map<String, String>> requestParms = new HashMap<>();
        Map<String, String> user1 = new HashMap<>();
        Map<String, String> user2 = new HashMap<>();
        user1.put("nickName", "ychm");
        user1.put("height", "18");
        user1.put("birthday", "2018-11-12 12:18:33");
        user2.put("nickName", "wujing");
        user2.put("height", "45");
        user2.put("birthday", "2011-11-01 10:28:33");
        requestParms.put("user[0]", user1);
        requestParms.put("user[1]", user2);
        List<User> userList = new ArrayList<User>();
        Object obj = Castor.stringToNonPrimitiveSet(requestParms, userList.getClass());
        Assert.assertTrue(Clazz.isImplementsInterface(obj.getClass(), List.class));
    }


    @Test
    public void testClassCast(){
//        if (clazz == null) return;
//
//        if (ValueObject.class.isAssignableFrom(clazz)) {
//            try {
//                if (parent == null) map.putAll(getQuery((ValueObject)clazz.newInstance(), obj, isConvertName));
//            } catch (Exception e) {
//            }
//            return;
//
//        }else if (ValueObject[].class.isAssignableFrom(clazz)) {
//            clazz = clazz.getComponentType();
//            try {
//                if (parent == null) map.putAll(getQuery((ValueObject)clazz.newInstance(), obj, isConvertName));
//            } catch (Exception e) {
//            }
//            return;
//
//        }else if ((Set.class.isAssignableFrom(clazz) || List.class.isAssignableFrom(clazz)) && type instanceof ParameterizedType) {
//            try {
//                type = ((ParameterizedType)type).getActualTypeArguments()[0];
//                if (type instanceof GenericArrayType) clazz = (Class<?>)((GenericArrayType)type).getGenericComponentType();
//                else clazz = (Class<?>)type;
//                if (ValueObject.class.isAssignableFrom(clazz)) {
//                    try {
//                        if (parent == null) map.putAll(getQuery((ValueObject)clazz.newInstance(), obj, isConvertName));
//                    } catch (Exception e) {
//                    }
//                }
//            }catch(Exception e) {
//            }
//            return;
//
//        }else if (Map.class.isAssignableFrom(clazz) && type instanceof ParameterizedType) {
//            try {
//                type = ((ParameterizedType)type).getActualTypeArguments()[1];
//                if (type instanceof GenericArrayType) clazz = (Class<?>)((GenericArrayType)type).getGenericComponentType();
//                else clazz = (Class<?>)type;
//                if (ValueObject.class.isAssignableFrom(clazz)) {
//                    try {
//                        if (parent == null) map.putAll(getQuery((ValueObject)clazz.newInstance(), obj, isConvertName));
//                    } catch (Exception e) {
//                    }
//                }
//            }catch(Exception e) {
//            }
//            return;
//
//        }else if (Enum.class.isAssignableFrom(clazz)) return;
    }





}
