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
package venus.util;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import venus.base.Charsets;
import venus.base.NotNull;
import venus.base.Nullable;
import venus.base.Platforms;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

/**
 * <p> File utils </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2018-05-11 09:22
 */
public class FileUtil {
    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    // fileVisitor for file deletion on Files.walkFileTree
    private static FileVisitor<Path> deleteFileVisitor = new SimpleFileVisitor<Path>() {
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            Files.delete(file);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            Files.delete(dir);
            return FileVisitResult.CONTINUE;
        }
    };

    //////// 文件读写//////

    /**
     * 读取文件到byte[].
     *
     * @see Files#readAllBytes
     */
    public static byte[] toByteArray(final File file) throws IOException {
        return Files.readAllBytes(file.toPath());
    }

    /**
     * 读取文件到String.
     */
    public static String toString(final File file) throws IOException {
        return com.google.common.io.Files.toString(file, Charsets.UTF_8);
    }

    /**
     * 读取文件的每行内容到List<String>.
     *
     * @see Files#readAllLines
     */
    public static List<String> toLines(final File file) throws IOException {
        return Files.readAllLines(file.toPath(), Charsets.UTF_8);
    }

    /**
     * 简单写入String到File.
     */
    public static void write(final CharSequence data, final File file) throws IOException {
        Validate.notNull(file);
        Validate.notNull(data);

        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath(), Charsets.UTF_8)) {
            writer.append(data);
        }
    }

    /**
     * 追加String到File.
     */
    public static void append(final CharSequence data, final File file) throws IOException {
        Validate.notNull(file);
        Validate.notNull(data);
        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath(), Charsets.UTF_8, StandardOpenOption.APPEND)) {
            writer.append(data);
        }
    }

    /**
     * 打开文件为InputStream.
     *
     * @see Files#newInputStream
     */
    public static InputStream asInputStream(String fileName) throws IOException {
        return asInputStream(getPath(fileName));
    }

    /**
     * 打开文件为InputStream.
     *
     * @see Files#newInputStream
     */
    public static InputStream asInputStream(File file) throws IOException {
        Validate.notNull(file, "file is null");
        return asInputStream(file.toPath());
    }

    /**
     * 打开文件为InputStream.
     *
     * @see Files#newInputStream
     */
    public static InputStream asInputStream(Path path) throws IOException {
        Validate.notNull(path, "path is null");
        return Files.newInputStream(path);
    }

    /**
     * 打开文件为OutputStream.
     *
     * @see Files#newOutputStream
     */
    public static OutputStream asOutputStream(String fileName) throws IOException {
        return asOutputStream(getPath(fileName));
    }

    /**
     * 打开文件为OutputStream.
     *
     * @see Files#newOutputStream
     */
    public static OutputStream asOutputStream(File file) throws IOException {
        Validate.notNull(file, "file is null");
        return asOutputStream(file.toPath());
    }

    /**
     * 打开文件为OutputStream.
     *
     * @see Files#newOutputStream
     */
    public static OutputStream asOutputStream(Path path) throws IOException {
        Validate.notNull(path, "path is null");
        return Files.newOutputStream(path);
    }

    /**
     * 获取File的BufferedReader.
     *
     * @see Files#newBufferedReader
     */
    public static BufferedReader asBufferedReader(String fileName) throws IOException {
        Validate.notBlank(fileName, "filename is blank");
        return asBufferedReader(getPath(fileName));
    }

    public static BufferedReader asBufferedReader(Path path) throws IOException {
        Validate.notNull(path, "path is null");
        return Files.newBufferedReader(path, Charsets.UTF_8);
    }

    /**
     * 获取File的BufferedWriter.
     *
     * @see Files#newBufferedWriter
     */
    public static BufferedWriter asBufferedWriter(String fileName) throws IOException {
        Validate.notBlank(fileName, "filename is blank");
        return Files.newBufferedWriter(getPath(fileName), Charsets.UTF_8);
    }

    /**
     * 获取File的BufferedWriter.
     *
     * @see Files#newBufferedWriter
     */
    public static BufferedWriter asBufferedWriter(Path path) throws IOException {
        Validate.notNull(path, "path is null");
        return Files.newBufferedWriter(path, Charsets.UTF_8);
    }

    ///// 文件操作 /////

    /**
     * 复制文件或目录, not following links.
     *
     * @param from 如果为null，或者是不存在的文件或目录，抛出异常.
     * @param to 如果为null，或者from是目录而to是已存在文件，或相反
     */
    public static void copy(@NotNull File from, @NotNull File to) throws IOException {
        Validate.notNull(from);
        Validate.notNull(to);

        copy(from.toPath(), to.toPath());
    }

    /**
     * 复制文件或目录, not following links.
     *
     * @param from 如果为null，或者是不存在的文件或目录，抛出异常.
     * @param to 如果为null，或者from是目录而to是已存在文件，或相反
     */
    public static void copy(@NotNull Path from, @NotNull Path to) throws IOException {
        Validate.notNull(from);
        Validate.notNull(to);

        if (Files.isDirectory(from)) {
            copyDir(from, to);
        } else {
            copyFile(from, to);
        }
    }

    /**
     * 文件复制
     *
     * @param from 如果为null，或文件不存在或者是目录，，抛出异常
     * @param to 如果to为null，或文件存在但是一个目录，抛出异常
     */
    public static void copyFile(@NotNull File from, @NotNull File to) throws IOException {
        Validate.notNull(from);
        Validate.notNull(to);
        copyFile(from.toPath(), to.toPath());
    }

    /**
     * 文件复制
     *
     * @param from 如果为null，或文件不存在或者是目录，，抛出异常
     * @param to 如果to为null，或文件存在但是一个目录，抛出异常
     */
    public static void copyFile(@NotNull Path from, @NotNull Path to) throws IOException {
        Validate.isTrue(Files.exists(from), "%s is not exist or not a file", from);
        Validate.notNull(to);
        Validate.isTrue(!FileUtil.isDirExists(to), "%s is exist but it is a dir", to);
        Files.copy(from, to);
    }

    /**
     * 复制目录
     */
    public static void copyDir(@NotNull File from, @NotNull File to) throws IOException {
        Validate.isTrue(isDirExists(from), "%s is not exist or not a dir", from);
        Validate.notNull(to);

        copyDir(from.toPath(), to.toPath());
    }

    /**
     * 复制目录
     */
    public static void copyDir(@NotNull Path from, @NotNull Path to) throws IOException {
        Validate.isTrue(isDirExists(from), "%s is not exist or not a dir", from);
        Validate.notNull(to);
        makesureDirExists(to);

        try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(from)) {
            for (Path path : dirStream) {
                copy(path, to.resolve(path.getFileName()));
            }
        }
    }

    /**
     * 文件移动/重命名.
     *
     * @see Files#move
     */
    public static void moveFile(@NotNull File from, @NotNull File to) throws IOException {
        Validate.notNull(from);
        Validate.notNull(to);

        moveFile(from.toPath(), to.toPath());
    }

    /**
     * 文件移动/重命名.
     *
     * @see Files#move
     */
    public static void moveFile(@NotNull Path from, @NotNull Path to) throws IOException {
        Validate.isTrue(isFileExists(from), "%s is not exist or not a file", from);
        Validate.notNull(to);
        Validate.isTrue(!isDirExists(to), "%s is  exist but it is a dir", to);

        Files.move(from, to);
    }

    /**
     * 目录移动/重命名
     */
    public static void moveDir(@NotNull File from, @NotNull File to) throws IOException {
        Validate.isTrue(isDirExists(from), "%s is not exist or not a dir", from);
        Validate.notNull(to);
        Validate.isTrue(!isFileExists(to), "%s is exist but it is a file", to);

        final boolean rename = from.renameTo(to);
        if (!rename) {
            if (to.getCanonicalPath().startsWith(from.getCanonicalPath() + File.separator)) {
                throw new IOException("Cannot move directory: " + from + " to a subdirectory of itself: " + to);
            }
            copyDir(from, to);
            deleteDir(from);
            if (from.exists()) {
                throw new IOException("Failed to delete original directory '" + from + "' after copy to '" + to + '\'');
            }
        }
    }

    /**
     * 创建文件或更新时间戳.
     *
     * @see com.google.common.io.Files#touch
     */
    public static void touch(String filePath) throws IOException {
        touch(new File(filePath));
    }

    /**
     * 创建文件或更新时间戳.
     *
     * @see com.google.common.io.Files#touch
     */
    public static void touch(File file) throws IOException {
        com.google.common.io.Files.touch(file);
    }

    /**
     * 删除文件.
     *
     * 如果文件不存在或者是目录，则不做修改
     */
    public static void deleteFile(@Nullable File file) throws IOException {
        Validate.isTrue(isFileExists(file), "%s is not exist or not a file", file);
        deleteFile(file.toPath());
    }

    /**
     * 删除文件.
     *
     * 如果文件不存在或者是目录，则不做修改
     */
    public static void deleteFile(@Nullable Path path) throws IOException {
        Validate.isTrue(isFileExists(path), "%s is not exist or not a file", path);

        Files.delete(path);
    }

    /**
     * 删除目录及所有子目录/文件
     *
     * @see Files#walkFileTree
     */
    public static void deleteDir(Path dir) throws IOException {
        Validate.isTrue(isDirExists(dir), "%s is not exist or not a dir", dir);

        // 后序遍历，先删掉子目录中的文件/目录
        Files.walkFileTree(dir, deleteFileVisitor);
    }

    /**
     * 删除目录及所有子目录/文件
     */
    public static void deleteDir(File dir) throws IOException {
        Validate.isTrue(isDirExists(dir), "%s is not exist or not a dir", dir);
        deleteDir(dir.toPath());
    }

    /**
     * 判断目录是否存在, from Jodd
     */
    public static boolean isDirExists(String dirPath) {
        if (dirPath == null) {
            return false;
        }
        return isDirExists(getPath(dirPath));
    }

    public static boolean isDirExists(Path dirPath) {
        return dirPath != null && Files.exists(dirPath) && Files.isDirectory(dirPath);
    }

    /**
     * 判断目录是否存在, from Jodd
     */
    public static boolean isDirExists(File dir) {
        if (dir == null) {
            return false;
        }
        return isDirExists(dir.toPath());
    }

    /**
     * 确保目录存在, 如不存在则创建
     */
    public static void makesureDirExists(String dirPath) throws IOException {
        makesureDirExists(getPath(dirPath));
    }

    /**
     * 确保目录存在, 如不存在则创建
     */
    public static void makesureDirExists(File file) throws IOException {
        Validate.notNull(file);
        makesureDirExists(file.toPath());
    }

    /**
     * 确保目录存在, 如不存在则创建.
     *
     * @see Files#createDirectories
     *
     */
    public static void makesureDirExists(Path dirPath) throws IOException {
        Validate.notNull(dirPath);
        Files.createDirectories(dirPath);
    }

    /**
     * 确保父目录及其父目录直到根目录都已经创建.
     *
     */
    public static void makesureParentDirExists(File file) throws IOException {
        Validate.notNull(file);
        makesureDirExists(file.getParentFile());
    }

    /**
     * 判断文件是否存在, from Jodd.
     *
     * @see Files#exists
     * @see Files#isRegularFile
     */
    public static boolean isFileExists(String fileName) {
        if (fileName == null) {
            return false;
        }
        return isFileExists(getPath(fileName));
    }

    /**
     * 判断文件是否存在, from Jodd.
     *
     * @see Files#exists
     * @see Files#isRegularFile
     */
    public static boolean isFileExists(File file) {
        if (file == null) {
            return false;
        }
        return isFileExists(file.toPath());
    }

    /**
     * 判断文件是否存在, from Jodd.
     *
     * @see Files#exists
     * @see Files#isRegularFile
     */
    public static boolean isFileExists(Path path) {
        if (path == null) {
            return false;
        }
        return Files.exists(path) && Files.isRegularFile(path);
    }

    /**
     * 在临时目录创建临时目录，命名为${毫秒级时间戳}-${同一毫秒内的随机数}.
     *
     * @see Files#createTempDirectory
     */
    public static Path createTempDir() throws IOException {
        return Files.createTempDirectory(System.currentTimeMillis() + "-");
    }

    /**
     * 在临时目录创建临时文件，命名为tmp-${random.nextLong()}.tmp
     *
     * @see Files#createTempFile
     */
    public static Path createTempFile() throws IOException {
        return Files.createTempFile("tmp-", ".tmp");
    }

    /**
     * 在临时目录创建临时文件，命名为${prefix}${random.nextLong()}${suffix}
     *
     * @see Files#createTempFile
     */
    public static Path createTempFile(String prefix, String suffix) throws IOException {
        return Files.createTempFile(prefix, suffix);
    }

    private static Path getPath(String filePath) {
        return Paths.get(filePath);
    }

    /**
     * 获取文件名(不包含路径)
     */
    public static String getFileName(@NotNull String fullName) {
        Validate.notEmpty(fullName);
        int last = fullName.lastIndexOf(Platforms.FILE_PATH_SEPARATOR_CHAR);
        return fullName.substring(last + 1);
    }

    /**
     * 获取文件名的扩展名部分(不包含.)
     *
     * @see com.google.common.io.Files#getFileExtension
     */
    public static String getFileExtension(File file) {
        return com.google.common.io.Files.getFileExtension(file.getName());
    }

    /**
     * 获取文件名的扩展名部分(不包含.)
     *
     * @see com.google.common.io.Files#getFileExtension
     */
    public static String getFileExtension(String fullName) {
        return com.google.common.io.Files.getFileExtension(fullName);
    }

    /**
     * fetch all file in baseDir, and not including directory
     *
     * @param baseDir
     * @return
     */
    public static List<File> fetchSubFiles(String baseDir){
        List<File> subFiles = new ArrayList<File>();
        if (baseDir==null || "".equals(baseDir)){
            return subFiles;
        }
        File baseFile = new File(baseDir);
        if (!baseFile.exists() || !baseFile.isDirectory()){
            logger.error("Unsupported file type. Must directory. [" + baseDir + "]");
            return subFiles;
        }
        File[] files = baseFile.listFiles();
        for (File file : files) {
            if (file.isFile()){
                subFiles.add(file);
            }else if (file.isDirectory()){
                List<File> _fileList = fetchSubFiles(file.getPath());
                subFiles.addAll(_fileList);
            }
        }
        return subFiles;
    }

}
