/*
 * Copyright (c) 2006 UFIDA Software Engineering Co, Inc.
 * All Rights Reserved
 *
 * Created date 2006-11-5
 * @author changming.y
 **/
package venus.commons.xmlenum;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import venus.frames.i18n.util.LocaleHolder;
import venus.frames.mainframe.util.PathException;
import venus.frames.mainframe.util.PathMgr;

import java.io.File;
import java.text.MessageFormat;
import java.util.*;

/**
 * Enum repository is used to manage system enum definations, it is configured
 * using an xml file, indicating where the enum defination files are to be loaded
 */
public final class EnumRepository {
// ------------------------------ FIELDS ------------------------------

    public static String ENUM_DEF_EXTENSION = ".enum.xml";

    // log utility
    private static Log logger = LogFactory.getLog(EnumRepository.class);
    private static EnumRepository instance = null;

    private Map<String,Map<String, EnumValueMap>> localeEnumMap = new HashMap<String,Map<String, EnumValueMap>>();           // internal enum defination map

// -------------------------- STATIC METHODS --------------------------

    /**
     * get singleton EnumRepository object
     *
     * @return singleton EnumRepository object
     */
    public static EnumRepository getInstance() {
        if (instance == null) {
            synchronized (EnumRepository.class) {
                if (instance == null) {
                    instance = new EnumRepository();
                }
            }
        }
        return instance;
    }

// --------------------------- CONSTRUCTORS ---------------------------

    // constructor
    private EnumRepository() {
    }

// -------------------------- OTHER METHODS --------------------------

    /**
     * load metadata from a filename
     *
     * @param filename, the enum file name need to load
     */
//    public void loadFromDir(String filename)  {
//
//        String strEnumFile = PathMgr.getConfPath() + "enum/" + filename;
//        loadFromDir(new File(strEnumFile));
//    }
    
    /**
     * load metadata from default dir "conf/enum/"
     *
     * @param filename, the enum dir need to load
     */
    public void loadFromDir()  {

        String strEnumDir = null;
		try {
			strEnumDir = PathMgr.getSingleton().getRealPath("/WEB-INF/conf/enum/");
		} catch (PathException e) {
			logger.error("Get the real path of enum files failed: ", e);
		}
		loadFromDir(new File(strEnumDir));
    }
    
    /**
     * reload metadata from default dir "conf/enum/"
     *
     * @param filename, the enum dir need to load
     */
    public void ReLoadFromDir()  {

        String strEnumDir = PathMgr.getRealRootPath() + "/enum/";
        reLoadFromDir(new File(strEnumDir));
    }

    /**
     * reload metadata from a directory
     *
     * @param dir directory to scan
     */
    public void loadFromDir(File dir) {
        if (dir == null) return;
        if (localeEnumMap.size() > 0  ) return;

        if (!dir.isDirectory()) {
            logger.warn(MessageFormat.format("EnumRepository::loadFromDir: The input parameter \'{0}\' is not a directory.", new Object[]{dir.getPath()}));
            return;
        }
        
        EnumLoader loader = EnumLoader.getInstance();

        initEmumMap(dir, loader);
    }

    /**
     * @param dir
     * @param loader
     */
    private void initEmumMap(File dir, EnumLoader loader) {
        // process the directory contents
        File[] enumDir = dir.listFiles();
        int directoryCount = 0;
        for(int i=0;i<enumDir.length;i++){
            if(enumDir[i].isDirectory()){
            	//判断目录名是否为有效的locale串，不是则跳过
            	String languageName = enumDir[i].getName();
            	if(languageName.split("_").length>1)
            		languageName = languageName.split("_")[0];
            	if(!ArrayUtils.contains(Locale.getISOLanguages(),languageName))
            		continue;
            	
                Map<String,EnumValueMap> enumMap = localeEnumMap.get(enumDir[i].getName());
                if(enumMap==null) enumMap = new HashMap();
                loadFromSubDirs(loader, enumDir[i],enumMap);
                localeEnumMap.put(enumDir[i].getName(), enumMap);
                directoryCount ++;
            }
        }
        if(directoryCount == 0){
            Map<String,EnumValueMap> enumMap = localeEnumMap.get(Locale.getDefault().getLanguage());
            if(enumMap==null) enumMap = new HashMap();
            loadFromSubDirs(loader, dir,enumMap);
            localeEnumMap.put(Locale.getDefault().getLanguage(), enumMap);
        }
    }
    
    /**
     * load metadata from a directory
     *
     * @param dir directory to scan
     */
    public void reLoadFromDir(File dir) {
        if (dir == null) return;

        if (!dir.isDirectory()) {
            logger.warn(MessageFormat.format("EnumRepository::loadFromDir: The input parameter \'{0}\' is not a directory.", new Object[]{dir.getPath()}));
            return;
        }
        
        EnumLoader loader = EnumLoader.getInstance();
        // process the directory contents
        initEmumMap(dir, loader);
        
    }

    /**
     * load metadata from a sub directory recursively
     *
     * @param dir directory to scan
     */
    private void loadFromSubDirs(EnumLoader loader, File dir,Map<String,EnumValueMap> enumMap) {
        String[] files = dir.list();
        if (files == null) return;

        // first pass, load the metafile
        for (int i = 0; i < files.length; i++) {
            String filename = files[i];
            if (filename.endsWith(ENUM_DEF_EXTENSION)) {
                // process the filename content
                File metaFile = new File(dir, filename);
                logger.info("Loading metafile '" + metaFile.getPath() + "'.");

                try {
                    EnumValueMap enumDef = loader.loadEnumDefination(metaFile.toURL().openStream());
                    addEnumDefination(enumDef,enumMap);
                } catch (Exception e) {
                    logger.warn(MessageFormat.format("Could not configure metadata from file : {0}",  new Object[]{metaFile.getPath()}), e);
                    continue;
                }
            }
        }

        // second pass, scan the subdrectory
        for (int i = 0; i < files.length; i++) {
            String filename = files[i];
            File file = new File(dir, filename);
            if (file.isDirectory()) {
                loadFromSubDirs(loader, file,enumMap);
            }
        }
    }

    /**
     * add enum defination to internal map
     *
     * @param enumDef
     */
    private void addEnumDefination(EnumValueMap enumDef,Map<String,EnumValueMap> enumMap) {

        if (enumDef == null)
            return;

        String name = enumDef.getName();
        EnumValueMap old = enumMap.get(name);
        if (old != null) {
            logger.warn("Duplicate EnumValueMap with the name '" + name + "' found, are you sure?");
        }

        enumMap.put(enumDef.getName(), enumDef);
    }

    /**
     * get the corresponding EnumValueMap object according to its enum name
     *
     * @param enumName the name of the enum value map
     * @return the corresponding EnumValueMap object if existed, null if not
     */
    public EnumValueMap getEnumValueMap(String enumName) {
        Map<String,EnumValueMap> enumMap = localeEnumMap.get(LocaleHolder.getLocale().getLanguage());
        if(enumMap==null) {
            logger.warn("The locale:" + LocaleHolder.getLocale().getLanguage() + " is not exists,Set system locale default!");
            enumMap = localeEnumMap.get(Locale.getDefault().getLanguage());
            if(enumMap==null)
                throw new RuntimeException("The default locale: " + Locale.getDefault().getLanguage() + " is not exists");
        }
        return enumMap.get(enumName);
    }

    public String toString() {
        final StringBuffer buf = new StringBuffer();
        buf.append("======== Dumping Enum Repository Contents =========\n");

        Set keys = localeEnumMap.keySet();
        for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
            String key = (String) iterator.next();
            Map enumDef = localeEnumMap.get(key);
            buf.append(enumDef.toString()).append("\n");
        }

        buf.append("================== EOF contect dump ===============");
        return buf.toString();
    }

}

