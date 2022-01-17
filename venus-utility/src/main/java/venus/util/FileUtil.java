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

import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * <p> File utils </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2018-05-11 09:22
 */
public class FileUtil {

    private static final Logger logger = Logger.getLogger(FileUtil.class);

    /**
     * fetch all file in baseDir, and not directory
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
