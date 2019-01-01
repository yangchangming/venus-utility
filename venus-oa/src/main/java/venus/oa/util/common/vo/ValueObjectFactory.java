/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.oa.util.common.vo;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.DynaBean;
import venus.frames.base.action.IRequest;
import venus.frames.mainframe.util.PathException;
import venus.frames.mainframe.util.PathMgr;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * vo创新工厂
 * @author changming.Y <changming.yang.ah@gmail.com>
 *
 */
public class ValueObjectFactory {
    private static Map dynaClassMapper = new HashMap();
    /**
     * 向动态bean中添加标识接口
     * @param identifyInterface 标识接口
     * @return
     */
    public static DynaBean mantra(Class identifyInterface){
        String className = "DynaBean"+identifyInterface.getSimpleName();
        Class originalClass = null;
        if(dynaClassMapper.containsKey(className)){
            originalClass = (Class) dynaClassMapper.get(className);
        }else{
            ClassPool pool = ClassPool.getDefault();
            try {
                pool.insertClassPath(PathMgr.getSingleton().getRealPath("/WEB-INF/lib/*"));
                pool.insertClassPath(PathMgr.getSingleton().getRealPath("/WEB-INF/classes"));
            } catch (PathException e) {
                e.printStackTrace();
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
            CtClass original = null;
            CtClass newInterfaces[] = null;
            try {
                original = pool.get("org.apache.commons.beanutils.LazyDynaBean");
                newInterfaces = new CtClass[]{pool.get(identifyInterface.getName()),pool.get(Serializable.class.getName())};
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
            if(original.isFrozen())
                original.defrost();
            try{
            original.setInterfaces(newInterfaces);
            }catch(Exception e){e.printStackTrace();}
            original.setName(identifyInterface.getPackage().getName()+"."+className);            
            try {
                originalClass = original.toClass();
            } catch (CannotCompileException e) {
                e.printStackTrace();
            }
            dynaClassMapper.put(className, originalClass);
        }
        
        DynaBean vo = null;
        try {
            vo = (DynaBean)originalClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return vo;
    }
    
    public static DynaBean populate(DynaBean instance, ResultSet rs){
        int num = 0;
        try {
            num = rs.getMetaData().getColumnCount();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < num; i++) {
            String colName = null;
            try {
                colName = rs.getMetaData().getColumnLabel(i + 1);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (colName != null) {
                Object res = null;
                try {
                    res = rs.getObject(colName);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    BeanUtils.setProperty(instance, colName.toLowerCase(), null==res? NullObject.getInstance():res);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return instance;
    }
    
    public static DynaBean populate(DynaBean instance, IRequest request){
        Iterator it = request.getParmsMap().entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String key = (String) entry.getKey();
            Object value = entry.getValue();
            if(null!=value)
            try {
                BeanUtils.setProperty(instance, key.toLowerCase(), value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }
}
