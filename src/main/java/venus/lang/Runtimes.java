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

import org.apache.commons.lang3.StringUtils;

import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p> Runtime util </p>
 * 1.取得当前进程PID, JVM参数
 * 2.注册JVM关闭钩子, 获得CPU核数
 * 3.通过StackTrace 获得当前方法的类名方法名，调用者的类名方法名(获取StackTrace有消耗，不要滥用)
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2022-01-28 15:19
 */
public class Runtimes {

    private static AtomicInteger shutdownHookThreadIndex = new AtomicInteger(0);

    /////// RuntimeMXBean相关 //////

    /**
     * 获得当前进程的PID
     *
     * 当失败时返回-1
     */
    public static int getPid() {

        // format: "pid@hostname"
        String jvmName = ManagementFactory.getRuntimeMXBean().getName();
        String[] split = jvmName.split("@");
        if (split.length != 2) {
            return -1;
        }

        try {
            return Integer.parseInt(split[0]);
        } catch (Exception e) { // NOSONAR
            return -1;
        }
    }

    /**
     * 返回应用启动到现在的毫秒数
     */
    public static long getUpTime() {
        return ManagementFactory.getRuntimeMXBean().getUptime();
    }

    /**
     * 返回输入的JVM参数列表
     */
    public static String getVmArguments() {
        List<String> vmArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
        return StringUtils.join(vmArguments, " ");
    }

    //////////// Runtime 相关////////////////
    /**
     * 获取CPU核数
     */
    public static int getCores() {
        return Runtime.getRuntime().availableProcessors();
    }

    /**
     * 注册JVM关闭时的钩子程序
     */
    public static void addShutdownHook(Runnable runnable) {
        Runtime.getRuntime().addShutdownHook(
                new Thread(runnable, "Thread-ShutDownHook-" + shutdownHookThreadIndex.incrementAndGet()));
    }

    //////// 通过StackTrace 获得当前方法的调用者 ////
    /**
     * 通过StackTrace，获得调用者的类名.
     *
     * 获取StackTrace有消耗，不要滥用
     */
    public static String getCallerClass() {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        if (stacktrace.length >= 4) {
            StackTraceElement element = stacktrace[3];
            return element.getClassName();
        } else {
            return StringUtils.EMPTY;
        }
    }

    /**
     * 通过StackTrace，获得调用者的"类名.方法名()"
     *
     * 获取StackTrace有消耗，不要滥用
     */
    public static String getCallerMethod() {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        if (stacktrace.length >= 4) {
            StackTraceElement element = stacktrace[3];
            return element.getClassName() + '.' + element.getMethodName() + "()";
        } else {
            return StringUtils.EMPTY;
        }
    }

    /**
     * 通过StackTrace，获得当前方法的类名.
     *
     * 获取StackTrace有消耗，不要滥用
     */
    public static String getCurrentClass() {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        if (stacktrace.length >= 3) {
            StackTraceElement element = stacktrace[2];
            return element.getClassName();
        } else {
            return StringUtils.EMPTY;
        }
    }

    /**
     * 通过StackTrace，获得当前方法的"类名.方法名()"
     *
     * 获取StackTrace有消耗，不要滥用
     */
    public static String getCurrentMethod() {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        if (stacktrace.length >= 3) {
            StackTraceElement element = stacktrace[2];
            return element.getClassName() + '.' + element.getMethodName() + "()";
        } else {
            return StringUtils.EMPTY;
        }
    }
}
