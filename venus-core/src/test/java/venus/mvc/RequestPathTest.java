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

import java.util.HashMap;
import java.util.Map;

/**
 * <p>  </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2019-06-03 12:45
 */
public class RequestPathTest {

    @Test
    public void testEquals(){

        RequestPath requestPath = new RequestPath("post", "/user/list");
        RequestPath requestPath1 = new RequestPath("post", "/user/list");
        Map map = new HashMap<>();
        map.put(requestPath, "value");
        Assert.assertEquals(true, map.containsKey(requestPath1));

    }
}
