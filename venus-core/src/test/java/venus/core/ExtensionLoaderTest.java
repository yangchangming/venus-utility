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
package venus.core;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * <p> ExtensionLoaderTest </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2018-04-26 11:44
 */
public class ExtensionLoaderTest extends TestCase{

    @Test
    public void testLoadExtension(){
        Assert.assertEquals(DaoImpl.class, ExtensionLoader.getExtensionLoader(IDao.class).loadExtension("daoimpl"));
    }

    @Test
    public void testAddExtension(){
        ExtensionLoader.getExtensionLoader(IDao.class).addExtension(DaoImpl.class);
        Assert.assertEquals(2,ExtensionLoader.getExtensionLoader(IDao.class).loadExtensions().size());
    }

    @Test
    public void testLoadExtensionInstance(){
        Assert.assertEquals(DaoImpl.class, ExtensionLoader.getExtensionLoader(IDao.class).loadExtensionInstance("daoimpl").getClass());
    }

    @Test
    public void testLoadExtensions(){
        Assert.assertEquals(2, ExtensionLoader.getExtensionLoader(IDao.class).loadExtensions().size());
    }

}
