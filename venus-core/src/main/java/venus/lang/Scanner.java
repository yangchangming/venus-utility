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
package venus.lang;

import venus.exception.VenusFrameworkException;

import java.io.IOException;

/**
 * <p> Scanner for system </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2019-05-28 21:39
 */
public class Scanner {

    /**
     * fetch class path
     *
     * @return
     * @throws IOException
     */
    public static String classPath() {
        String classPath = "";
        try {
            classPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        }catch (NullPointerException exception){
            throw new VenusFrameworkException("Fetch classpath error. " + exception.getMessage());
        }
        return classPath;
    }

}
