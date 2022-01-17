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
package venus.ioc;

import org.junit.Assert;
import org.junit.Test;
import user.dao.IUserDao;
import user.dao.impl.UserDao;
import user.service.impl.UserService;
import venus.ioc.annotation.Repository;

import java.lang.reflect.Field;

/**
 * <p> Ioc test </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2019-05-29 16:16
 */
public class IocTest {

    @Test
    public void testBeansLoading(){
        Beans.of().loadClass().stream().forEach(clzz -> System.out.println(clzz.getName()));
    }

    @Test
    public void testLoadClassByAnnotation(){
        Beans.of().loadClassByAnnotation(Repository.class).stream().forEach(clzz -> System.out.println(clzz.getName()));
    }

    @Test
    public void testLoadClassesBySuper(){
        Beans.of().loadClassesBySuper(IUserDao.class).stream().forEach(clzz -> System.out.println(clzz.getName()));
    }

    @Test
    public void testGetBean(){
        Assert.assertEquals("class name diff", "user.dao.impl.UserDao", (Beans.of().getBean(UserDao.class).getClass().getName()));
    }

    @Test
    public void testGetBeans(){
        Beans.of().getBeans().stream().forEach(obj -> System.out.println(obj.getClass().getName()));
    }

    @Test
    public void injectionTest() throws Exception {
        Field field = Beans.of().getBean(UserService.class).getClass().getDeclaredField("userDao");
        field.setAccessible(true);
        Assert.assertNotNull(field.get(Beans.of().getBean(UserService.class)));
    }
}
