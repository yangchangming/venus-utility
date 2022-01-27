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
package venus.base;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * <p> Charsets definition </p>
 * 1. 尽量使用Charsets.UTF8而不是"UTF-8"，减少JDK里的Charset查找消耗
 * 2. 使用JDK7的StandardCharsets，同时留了标准名称的字符串
 *
 * @author calvin
 * @since 2022-01-27 09:56
 */
public class Charsets {

    public static final Charset UTF_8 = StandardCharsets.UTF_8;
    public static final Charset US_ASCII = StandardCharsets.US_ASCII;
    public static final Charset ISO_8859_1 = StandardCharsets.ISO_8859_1;
    public static final String UTF_8_NAME = StandardCharsets.UTF_8.name();
    public static final String ASCII_NAME = StandardCharsets.US_ASCII.name();
    public static final String ISO_8859_1_NAME = StandardCharsets.ISO_8859_1.name();
}
