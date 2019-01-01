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
package venus.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import venus.user.dao.IUserDao;
import venus.user.model.User;
import venus.user.service.IUserService;

import java.util.List;

/**
 * <p> User Service implements </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2018-06-05 15:02
 */
@Service
public class UserService implements IUserService {

    @Autowired
    private IUserDao userDao;

    @Override
    @Transactional
    public List<User> queryAll() {
     return userDao.queryAll("select * from runnf_user limit 0,40");
    }
}
