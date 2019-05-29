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
package user.dao.impl;

import user.dao.IUserDao;
import user.model.User;
import venus.ioc.Repository;

import java.util.List;

/**
 * <p> User Dao implements </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2018-05-29 12:08
 */
@Repository
public class UserDao implements IUserDao {

    public List<User> queryAll(String sql) {
        return null;
    }
}
