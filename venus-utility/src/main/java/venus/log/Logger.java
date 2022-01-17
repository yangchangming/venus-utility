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
package venus.log;

/**
 * <p> Log Instance </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2018-06-06 23:43
 */
public class Logger {

    public static void debug(org.apache.log4j.Logger logger, String message){
        if (logger.isDebugEnabled()){
            logger.debug(message);
        }
    }


    public static void keyInfo(org.apache.log4j.Logger logger, String message){
        logger.info("");
        logger.info("------------------------------------------------------------------------");
        logger.info(message);
        logger.info("------------------------------------------------------------------------");
        logger.info("");
    }

    public static void keyInfos(org.apache.log4j.Logger logger, String[] messages){
        logger.info("");
        logger.info("------------------------------------------------------------------------");
        for (String message : messages) {
            logger.info(message);
        }
        logger.info("------------------------------------------------------------------------");
        logger.info("");
    }

}
