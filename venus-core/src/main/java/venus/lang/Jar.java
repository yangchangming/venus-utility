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

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.stream.Collectors;

/**
 * <p> Jar util </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2019-10-09 16:33
 */
public class Jar {

    /**
     * load all class, from jar file of local file system
     *
     * @param jar
     * @return
     */
    public static Set<Class<?>> loadClassOfJarFile(File jar){
        if (!jar.exists()){
            return null;
        }
        try {
            JarFile jarFile = new JarFile(jar);
            return jarFile.stream().filter(jarEntry -> jarEntry.getName().endsWith(".class"))
                    .map(jarEntry -> loadClassByJarEntry(jarEntry)).collect(Collectors.toSet());
        } catch (IOException e) {
            throw new VenusFrameworkException(e.getCause().getMessage());
        }
    }

    /**
     * load the class, from jarEntity in the jar
     *
     * @param jarEntry
     * @return
     */
    public static Class<?> loadClassByJarEntry(JarEntry jarEntry){
        String jarEntryName = jarEntry.getName();
        String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replaceAll("/", ".");
        return Clazz.loadClass(className);
    }

    /**
     * todo complete
     *
     * @param jar
     * @param sourcePath
     * @throws IOException
     */
    public void buildJar(File jar, File sourcePath) throws IOException {
        JarOutputStream jos = null;
        try {
            jos = new JarOutputStream(new FileOutputStream(jar));
            List<File> fileList = getFiles(sourcePath);
            System.out.println("Jaring " + fileList.size() + " files");
            List<String> lstPaths = new ArrayList<String>(25);
            for (File file : fileList) {
                String path = file.getParent().replace("\\", "/");
                String name = file.getName();

                path = path.substring(sourcePath.getPath().length());
                if (path.startsWith("/")) {
                    path = path.substring(1);
                }
                if (path.length() > 0) {
                    path += "/";
                    if (!lstPaths.contains(path)) {
                        JarEntry entry = new JarEntry(path);
                        jos.putNextEntry(entry);
                        jos.closeEntry();
                        lstPaths.add(path);
                    }
                }
                System.out.println("Adding " + path + name);
                JarEntry entry = new JarEntry(path + name);
                jos.putNextEntry(entry);

                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(file);
                    byte[] byteBuffer = new byte[1024];
                    int bytesRead = -1;
                    while ((bytesRead = fis.read(byteBuffer)) != -1) {
                        jos.write(byteBuffer, 0, bytesRead);
                    }
                    jos.flush();
                } finally {
                    try {
                        fis.close();
                    } catch (Exception e) {
                    }
                }
                jos.closeEntry();
            }
            jos.flush();
        } finally {
            try {
                jos.close();
            } catch (Exception e) {
            }
        }
    }

    /**
     * todo complete
     *
     * @param jar
     * @param outputPath
     * @throws IOException
     */
    public void releaseJar(File jar, File outputPath) throws IOException {
        JarFile jarFile = null;
        try {
            if (outputPath.exists() || outputPath.mkdirs()) {
                jarFile = new JarFile(jar);
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    File path = new File(outputPath + File.separator + entry.getName());
                    if (entry.isDirectory()) {
                        if (!path.exists() && !path.mkdirs()) {
                            throw new IOException("Failed to create output path " + path);
                        }
                    } else {
                        System.out.println("Extracting " + path);

                        InputStream is = null;
                        OutputStream os = null;
                        try {
                            is = jarFile.getInputStream(entry);
                            os = new FileOutputStream(path);

                            byte[] byteBuffer = new byte[1024];
                            int bytesRead = -1;
                            while ((bytesRead = is.read(byteBuffer)) != -1) {
                                os.write(byteBuffer, 0, bytesRead);
                            }
                            os.flush();
                        } finally {
                            try {
                                os.close();
                            } catch (Exception e) {
                            }
                            try {
                                is.close();
                            } catch (Exception e) {
                            }
                        }
                    }
                }
            } else {
                throw new IOException("Output path does not exist/could not be created");
            }
        } finally {
            try {
                jarFile.close();
            } catch (Exception e) {
            }
        }
    }

    public List<File> getFiles(File sourcePath){
        return null;
    }


}
