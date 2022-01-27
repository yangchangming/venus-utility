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

import com.google.common.base.Utf8;
import com.sun.istack.internal.Nullable;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p> String util </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2022-01-26 16:16
 */
public final class Strings {
    private static Strings instance = new Strings();

    /**
     * 高性能的Split，针对char的分隔符号，比JDK String自带的高效.
     * copy from Commons Lang 3.5 StringUtils 并做优化
     * @see #split(String, char, int)
     */
    public static List<String> split(@Nullable final String str, final char separatorChar) {
        return split(str, separatorChar, 10);
    }

    /**
     * 高性能的Split，针对char的分隔符号，比JDK String自带的高效.
     * copy from Commons Lang 3.5 StringUtils, 做如下优化:
     * 1. 最后不做数组转换，直接返回List.
     * 2. 可设定List初始大小.
     * 3. preserveAllTokens 取默认值false
     *
     * @param expectParts 预估分割后的List大小，初始化数据更精准
     * @return 如果为null返回null, 如果为""返回空数组
     */
    public static List<String> split(@Nullable final String str, final char separatorChar, int expectParts) {
        if (str == null) {
            return null;
        }
        final int len = str.length();
        if (len == 0) {
            return Collections.emptyList();
        }
        final List<String> list = new ArrayList<String>(expectParts);
        int i = 0;
        int start = 0;
        boolean match = false;
        while (i < len) {
            if (str.charAt(i) == separatorChar) {
                if (match) {
                    list.add(str.substring(start, i));
                    match = false;
                }
                start = ++i;
                continue;
            }
            match = true;
            i++;
        }
        if (match) {
            list.add(str.substring(start, i));
        }
        return list;
    }

    /**
     * 如果结尾字符为c, 去除掉该字符.
     */
    public static String removeEnd(final String s, final char c) {
        if (endWith(s, c)) {
            return s.substring(0, s.length() - 1);
        }
        return s;
    }

    /**
     * 判断字符串是否以字母结尾
     *
     * 如果字符串为Null或空，返回false
     */
    public static boolean endWith(@Nullable CharSequence s, char c) {
        if (StringUtils.isEmpty(s)) {
            return false;
        }
        return s.charAt(s.length() - 1) == c;
    }

    ////////// 其他 char 相关 ///////////
    /**
     * String 有replace(char,char)，但缺少单独replace first/last的
     */
    public static String replaceFirst(@Nullable String s, char sub, char with) {
        if (s == null) {
            return null;
        }
        int index = s.indexOf(sub);
        if (index == -1) {
            return s;
        }
        char[] str = s.toCharArray();
        str[index] = with;
        return new String(str);
    }

    /**
     * String 有replace(char,char)替换全部char，但缺少单独replace first/last
     */
    public static String replaceLast(@Nullable String s, char sub, char with) {
        if (s == null) {
            return null;
        }

        int index = s.lastIndexOf(sub);
        if (index == -1) {
            return s;
        }
        char[] str = s.toCharArray();
        str[index] = with;
        return new String(str);
    }

    /**
     * 判断字符串是否以字母开头
     *
     * 如果字符串为Null或空，返回false
     */
    public static boolean startWith(@Nullable CharSequence s, char c) {
        if (StringUtils.isEmpty(s)) {
            return false;
        }
        return s.charAt(0) == c;
    }

    ///////////// 其他 ////////////
    /**
     * 计算字符串被UTF8编码后的字节数 via guava
     *
     * @see Utf8#encodedLength(CharSequence)
     */
    public static int utf8EncodedLength(@Nullable CharSequence sequence) {
        if (StringUtils.isEmpty(sequence)) {
            return 0;
        }
        return Utf8.encodedLength(sequence);
    }

    ///////////// StringBuilderWriter////////////

    public static StringBuilderWriter getBuilderWriter(){
        if (instance==null){
            instance = new Strings();
        }
        return instance.new StringBuilderWriter();
    }

    /**
     * JDK的java.io.StringWriter使用StringBuffer，移植Commons IO使用StringBuilder的版本.
     *
     * https://github.com/apache/commons-io/blob/master/src/main/java/org/apache/commons/io/output/StringBuilderWriter.java
     *
     * {@link Writer} implementation that outputs to a {@link StringBuilder}.
     * <p>
     * <strong>NOTE:</strong> This implementation, as an alternative to <code>java.io.StringWriter</code>, provides an
     * <i>un-synchronized</i> (i.e. for use in a single thread) implementation for better performance. For safe usage with
     * multiple {@link Thread}s then <code>java.io.StringWriter</code> should be used.
     *
     */
    public class StringBuilderWriter extends Writer implements Serializable {
        private static final long serialVersionUID = -146927496096066153L;
        private final StringBuilder builder;

        /**
         * Constructs a new {@link StringBuilder} instance with default capacity.
         */
        public StringBuilderWriter() {
            this.builder = new StringBuilder();
        }

        /**
         * Constructs a new {@link StringBuilder} instance with the specified capacity.
         *
         * @param capacity The initial capacity of the underlying {@link StringBuilder}
         */
        public StringBuilderWriter(final int capacity) {
            this.builder = new StringBuilder(capacity);
        }

        /**
         * Constructs a new instance with the specified {@link StringBuilder}.
         *
         * <p>
         * If {@code builder} is null a new instance with default capacity will be created.
         * </p>
         *
         * @param builder The String builder. May be null.
         */
        public StringBuilderWriter(final StringBuilder builder) {
            this.builder = builder != null ? builder : new StringBuilder();
        }

        /**
         * Appends a single character to this Writer.
         *
         * @param value The character to append
         * @return This writer instance
         */
        @Override
        public Writer append(final char value) {
            builder.append(value);
            return this;
        }

        /**
         * Appends a character sequence to this Writer.
         *
         * @param value The character to append
         * @return This writer instance
         */
        @Override
        public Writer append(final CharSequence value) {
            builder.append(value);
            return this;
        }

        /**
         * Appends a portion of a character sequence to the {@link StringBuilder}.
         *
         * @param value The character to append
         * @param start The index of the first character
         * @param end The index of the last character + 1
         * @return This writer instance
         */
        @Override
        public Writer append(final CharSequence value, final int start, final int end) {
            builder.append(value, start, end);
            return this;
        }

        /**
         * Closing this writer has no effect.
         */
        @Override
        public void close() {
            // no-op
        }

        /**
         * Flushing this writer has no effect.
         */
        @Override
        public void flush() {
            // no-op
        }

        /**
         * Writes a String to the {@link StringBuilder}.
         *
         * @param value The value to write
         */
        @Override
        public void write(final String value) {
            if (value != null) {
                builder.append(value);
            }
        }

        /**
         * Writes a portion of a character array to the {@link StringBuilder}.
         *
         * @param value The value to write
         * @param offset The index of the first character
         * @param length The number of characters to write
         */
        @Override
        public void write(final char[] value, final int offset, final int length) {
            if (value != null) {
                builder.append(value, offset, length);
            }
        }

        /**
         * Returns the underlying builder.
         *
         * @return The underlying builder
         */
        public StringBuilder getBuilder() {
            return builder;
        }

        /**
         * Returns {@link StringBuilder#toString()}.
         *
         * @return The contents of the String builder.
         */
        @Override
        public String toString() {
            return builder.toString();
        }
    }
}
