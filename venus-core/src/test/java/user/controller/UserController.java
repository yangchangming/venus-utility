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
package user.controller;

import org.springframework.web.bind.annotation.RequestBody;
import user.model.User;
import user.service.IUserService;
import venus.ioc.Autowired;
import venus.ioc.Controller;
import venus.mvc.annotation.RequestChain;
import venus.mvc.annotation.RequestMapping;
import venus.mvc.annotation.RequestParam;

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
    @RequestChain({"2","4"})
    public String list(HttpServletRequest request , HttpServletResponse response,
                       @RequestParam(value = "user", required = true) User user,
                       @RequestBody(required = true) int number, long length){


        List result = userService.queryAll();



        request.setAttribute("result_list", result);
        return "user/list";
    }


}
