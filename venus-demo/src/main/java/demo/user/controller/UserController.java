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
package demo.user.controller;

import demo.user.service.IUserService;
import venus.ioc.annotation.Autowired;
import venus.ioc.annotation.Controller;
import venus.mvc.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p> User Controller </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2018-06-05 14:59
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @RequestMapping("/list")
    public String list(HttpServletRequest request , HttpServletResponse response){

        List result = userService.queryAll();
        request.setAttribute("result_list", result);
        return "user/list";
    }



}
