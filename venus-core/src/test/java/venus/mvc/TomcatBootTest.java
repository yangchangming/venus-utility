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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import venus.bootstrap.TomcatBoot;

/**
 * <p> Tomcat bootstrap test case </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2019-06-14 10:13
 */
public class TomcatBootTest {

    @Before
    public void setUp(){
        TomcatBoot.of().start();
    }

    @After
    public void tearDown(){
        TomcatBoot.of().stop();
    }

    @Test
    public void testBoot(){
        TomcatBoot.of().start();
    }
}
