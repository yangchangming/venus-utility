/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.oa.util;

import venus.frames.base.exception.BaseApplicationException;
import venus.frames.mainframe.util.Helper;

import java.lang.reflect.InvocationTargetException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author changming.Y <changming.yang.ah@gmail.com>
 *
 */
public class Encode {
    private static Object codeTool;
    static{
        codeTool = Helper.getBean("Encode");
    }
    public static String decode(String s){
        try {
            return (String) codeTool.getClass().getMethod("decode",new Class[]{String.class}).invoke(codeTool, s);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        throw new BaseApplicationException(venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Can_not_be_decrypted_"));
    }
    
    public static String encode(String s) {
        try {
            return (String) codeTool.getClass().getMethod("encode",new Class[]{String.class}).invoke(codeTool, s);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        throw new BaseApplicationException(venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Can_not_be_encrypted_"));
    }
    public static class MD5{
        public static String decode(String s){
            throw new UnsupportedOperationException();
        }
        public static String encode(String s) {
            try {
                MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                messageDigest.update(s.getBytes());
                byte[] b = messageDigest.digest();
                return toHex(b);
              }
              catch (NoSuchAlgorithmException ex) {
                String message = "没有找到 MD5 算法。";
                throw new RuntimeException(message, ex);
              }
        }
        public static String toHex(byte hash[]) {
            StringBuffer buf = new StringBuffer(hash.length * 2);
            int i;

            for (i = 0; i < hash.length; i++) {
              if ( ( (int) hash[i] & 0xff) < 0x10) {
                buf.append("0");
              }

              buf.append(Long.toString( (int) hash[i] & 0xff, 16));
            }

            return buf.toString();
          }
    }
}

