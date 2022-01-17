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
package venus.dao;

import org.apache.log4j.Logger;
import org.nutz.dao.Dao;
import org.nutz.dao.impl.NutDao;
import org.nutz.dao.impl.SimpleDataSource;
import org.nutz.lang.Files;
import org.nutz.lang.Mirror;
import org.nutz.lang.Streams;
import venus.exception.VenusFrameworkException;
import venus.lang.Clazz;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * <p> Dos, Dao operation util </p>
 * usage: Dos.of().dao()...
 *
 * 1. only one dos instance represent all rdbms
 * 2. default use druid pool
 * 3. use nutz dao
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2019-06-27 06:39
 */
public final class Dos {
    private static Logger logger = Logger.getLogger(Dos.class);
    private static Dos instance = new Dos();
    private static DataSource dataSource = null;
    private static Dao nutzDao = null;
    private static Class<?> druidFactoryClass;
    private boolean autoCloseWhenFinalize = true;

    static {
        druidFactoryClass = Clazz.loadClass("com.alibaba.druid.pool.DruidDataSourceFactory");
    }

    /**
     * Constructor
     */
    private Dos(){}

    public static Dos of(){
        if (instance==null){
            Dos.instance = new Dos();
        }
        if (Dos.nutzDao==null){
            instance.initial();
        }
        return Dos.instance;
    }

    public Dao dao(){
        if (nutzDao==null){
            Dos.nutzDao = initial();
        }
        return Dos.nutzDao;
    }

    public DataSource getDataSource() {
        if (Dos.dataSource==null){
            Properties properties = new Properties();
            InputStream in = null;
            try {
                in = new FileInputStream(Files.findFile("venus-conf.properties"));
                properties.load(in);
                Dos.dataSource = initialDataSource(properties);
            }catch (IOException e){
                throw new VenusFrameworkException(e.getCause().getMessage());
            }finally {
                if (in!=null)
                    Streams.safeClose(in);
            }
        }
        return Dos.dataSource;
    }

    private Dao initial(){
        Properties properties = new Properties();
        InputStream in = null;
        try {
            in = new FileInputStream(Files.findFile("venus-conf.properties"));
            properties.load(in);
            Dos.dataSource = initialDataSource(properties);
            if (Dos.nutzDao == null) {
                Dos.nutzDao = new NutDao(dataSource);
            }
        }catch (IOException e){
            throw new VenusFrameworkException(e.getCause().getMessage());
        }finally {
            if (in!=null)
                Streams.safeClose(in);
        }
        return Dos.nutzDao;
    }

    private DataSource initialDataSource(Properties properties){
        if (Dos.dataSource!=null){
            return Dos.dataSource;
        }
        if (druidFactoryClass != null) {
            logger.debug("Build DruidDataSource by props");
            Mirror<?> mirror = Mirror.me(druidFactoryClass);
            DataSource ds = (DataSource) mirror.invoke(null, "createDataSource", properties);
            if (!properties.containsKey("maxWait"))
                Mirror.me(ds).setValue(ds, "maxWait", 15*1000);
            return ds;
        }
        logger.debug("Build SimpleDataSource by props");
        return SimpleDataSource.createDataSource(properties);
    }

    private synchronized void close() {
        if (nutzDao == null)
            return;
        try {
            Mirror.me(dataSource).invoke(dataSource, "close");
        }
        catch (Throwable e) {
        }
        Dos.dataSource = null;
        Dos.nutzDao = null;
    }

    protected void finalize() throws Throwable{
        if (autoCloseWhenFinalize){
            close();
        }
        super.finalize();
    }

}
