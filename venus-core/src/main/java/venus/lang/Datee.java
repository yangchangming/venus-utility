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

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

/**
 * <p> Date and Time util </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2019-06-23 11:20
 */
public class Datee {

    /**
     * convert string to java.util.Date
     * 1. convert "2010-1-11" to Mon Jan 11 00:00:00 CST 2010
     * 2. convert "2010-1-11 11:06:33" to Mon Jan 11 11:06:33 CST 2010
     *
     * @param source
     * @return
     */
    public static Date stringToDate(String source){
        if (source==null || "".equals(source)){
            return null;
        }
        Locale locale = Locale.SIMPLIFIED_CHINESE;
        DateFormat df = DateFormat.getDateInstance(2, locale);
        if(isDateTimeFormat(source)){
            df = DateFormat.getDateTimeInstance(2, 2, locale);
        }
        try {
            return df.parse(source);
        } catch (ParseException e) {
            throw new VenusFrameworkException(e.getCause());
        }
    }

    /**
     * such as "2010-1-11"
     *
     * @param source
     * @return
     */
    public static boolean isDateFormat(String source){
        if (source==null || "".equals(source)){
            return false;
        }
        return !source.contains(":") && source.contains("-");
    }

    /**
     * such as "2010-1-11 11:06:33"
     *
     * @param source
     * @return
     */
    public static boolean isDateTimeFormat(String source){
        if (source==null || "".equals(source)){
            return false;
        }
        return source.contains(":") && source.contains("-");
    }


}
