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
package venus.dao;

import org.junit.Assert;
import org.junit.Test;
import org.nutz.dao.Chain;
import org.nutz.dao.Sqls;

/**
 * <p> Dos Test </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2019-10-05 14:41
 */
public class DosTest {

    @Test
    public void testCreateTable(){
        if (Dos.of().dao().exists("venus_user")){
            Dos.of().dao().drop("venus_user");
        }
        Dos.of().dao().execute(Sqls.create("create table " + "venus_user" + " (id int, name varchar(50), age int)"));
        Assert.assertTrue(Dos.of().dao().exists("venus_user"));
    }

    @Test
    public void insert(){
        testCreateTable();
        String tableName = "venus_user";
        Dos.of().dao().insert(tableName, Chain.make("id", 1).add("name", "wendal").add("age", 30));
        Dos.of().dao().insert(tableName, Chain.make("id", 2).add("name", "zozoh").add("age", 60));
        Dos.of().dao().insert(tableName, Chain.make("id", 3).add("name", "pangwu").add("age", 20));
        Dos.of().dao().insert(tableName, Chain.make("id", 4).add("name", "ywjno").add("age", 10));
        Assert.assertEquals(4, Dos.of().dao().count(tableName));
    }
}
