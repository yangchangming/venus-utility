package venus.portal.gbox.util;

import java.io.File;

public class FileTools {
    
    public final static String  FILE_SEPARATOR = "/";
    private final static String NATIVE_FILE_SEPARATOR = File.separator;
    
    /**
     * 删除指定路径的文件
     * @param filePath 文件路径
     * @return boolean 删除是否成功
     */    
    public static boolean delete(String filePath) {
        File file = new File(filePath);
        if (file != null && file.exists()) {
            return file.delete();
        }
        return false;
    }
    
    /**
     * 删除指定路径的文件夹及下面所有文件
     * @param dirPath 文件夹路径
     * @return boolean 删除是否成功
     */
    public static boolean deleteDir(String dirPath) {
        return deleteDir(dirPath,true);
    }
   
    /**
     * 删除指定路径的文件夹及下面所有文件
     * @param dirPath 文件夹路径
     * @param deleteDir 是否删除文件夹
     * @return boolean 删除是否成功
     */    
    public static boolean deleteDir(String dirPath,boolean deleteDir) {
        File file = new File(dirPath);
        if (!file.exists() || !file.isDirectory())
            return false;
        String[] fileList = file.list();
        for (int i = 0; i < fileList.length; i++) {
            if (!dirPath.endsWith(File.separator))
                dirPath += File.separator;
            File delFile = new File(dirPath + fileList[i]);
            if (delFile.exists()){
                if (delFile.isDirectory())
                    deleteDir(delFile.getPath(),deleteDir);
                else
                    delFile.delete();
            }
        }
        if (deleteDir)
            file.delete(); //删除指定目录
        return true;
    }
    
    /**
     * 获得去除扩展名后的文件扩展名
     * @param file 文件
     * @return String 文件名
     */      
    public static String getFilename(File file) { 
        return (file != null) ? getFilename(file.getName()) : ""; 
    }    
    
    /**
     * 获得去除扩展名后的文件扩展名
     * @param filename 文件名
     * @return String 文件名
     */      
    public static String getFilename(String filename) { 
        if ((filename != null) && (filename.length() > 0)) { 
            int i = filename.lastIndexOf('.'); 
            if ((i >-1) && (i < (filename.length()))) { 
                return filename.substring(0,i); 
            } 
        } 
        return filename; 
    }     
    
    /**
     * 获得文件扩展名
     * @param file 文件
     * @return String 文件扩展名
     */
    public static String getExtension(File file) { 
        return (file != null) ? getExtension(file.getName()) : ""; 
    } 
    
    /**
     * 获得文件扩展名
     * @param filename 文件名
     * @return String 文件扩展名
     */    
    public static String getExtension(String filename) { 
        if ((filename != null) && (filename.length() > 0)) { 
            int i = filename.lastIndexOf('.'); 
            if ((i >-1) && (i < (filename.length() - 1))) { 
                return filename.substring(i+1); 
            } 
        } 
        return filename; 
    } 
    
    /**
     * 为文件名字符串重命名
     * @param oldFileName 源文件名（带扩展名）
     * @param newFileName 新文件名
     * @return String 重命名后的文件名
     */
    public static String rename(String oldFileName,String newFileName) {
        if (oldFileName != null && oldFileName.length() > 0 && newFileName != null &&newFileName.length() > 0) {
            return newFileName + "." + getExtension(oldFileName);
        }
        return newFileName;
    }
    
    /**
     * 将文件绝对路径按目录层级分割为目录字符串，如：D:\document\word分割为document,word
     * @param filePath
     * @return String[] 目录字符串
     */
    public static String[] splitFolderName(String filePath) {
        if (filePath == null || "".equals(filePath))
            return null;
        File file = new File(filePath);
        if (!file.isDirectory())
            return null;
        String seperate = NATIVE_FILE_SEPARATOR;
        if("\\".equals(seperate)){
            seperate = "\\\\";
        }
        String[] folder = filePath.split(seperate);
        String[] returnFolder = new String[folder.length - 1];
        for (int i = 1; i < folder.length; i++) {
            returnFolder[i - 1] = folder[i];
        }
        return returnFolder;
    }
    
    /**
     * 将文件绝对路径按目录层级分割为目录字符串，如：D:\document\word分割为document,word
     * @param filePath
     * @return String[] 目录字符串
     */
    public static String[] splitFolderName(String filePath,String separator) {
        if (filePath == null || "".equals(filePath))
            return null;
        File file = new File(filePath);
        if (!file.isDirectory())
            return null;
        String[] folder = filePath.split(separator);
        String[] returnFolder = new String[folder.length - 1];
        for (int i = 1; i < folder.length; i++) {
            returnFolder[i - 1] = folder[i];
        }
        return returnFolder;
    }
    
    /**
     * 将文件按目录层级分割绝对路径，如D:\document\word分割为D:\document，D:\document\word
     * @param filePath
     * @param folderName
     * @return String 绝对路径字符串
     */
    public static String splitAbsolutePath(String filePath,String folderName) {
        if (filePath == null || "".equals(filePath))
            return null;
        File file = new File(filePath);
        if (!file.isDirectory())
            return null;
        return filePath.substring(0, filePath.lastIndexOf(folderName) + folderName.length());   
    }

    public static void main(String[] args) {
        String file = "D:/Music/english/01欧美事业部英语培训材料";
        String s = "Music";
        System.out.println(file.substring(0, file.lastIndexOf(s) + s.length()));
    }

}
