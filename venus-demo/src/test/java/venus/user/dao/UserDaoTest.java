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
package venus.user.dao;

import junit.framework.TestCase;
import org.junit.Test;
import venus.init.InitializationFactory;
import venus.springsupport.BeanFactoryHelper;
import venus.user.dao.impl.UserDao;

import java.util.List;

/**
 * <p> User dao test </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2018-05-30 16:17
 */
public class UserDaoTest extends TestCase {

    @Test
    public void testQueryAll(){

        InitializationFactory.init();

        Object userDao = BeanFactoryHelper.getBean("userDao");
        List result = ((UserDao)userDao).queryAll("select * from tbl_test");

        System.out.print(result.size());

    }

}
