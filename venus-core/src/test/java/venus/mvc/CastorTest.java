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

import java.util.HashMap;
import java.util.Map;

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
        Object o = Castor.stringToNonPrimitive(user, User.class);
        Assert.assertEquals("ychm", ((User)o).getNickName());
        Assert.assertEquals(18.0, ((User)o).getHeight(), 0);
    }

}
