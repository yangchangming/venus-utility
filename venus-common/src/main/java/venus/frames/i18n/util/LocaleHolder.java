package venus.frames.i18n.util;

import venus.VenusHelper;

import java.util.Locale;

/**
 * 用于国际化，本地化上下文存储对象(线程绑定)。
 */
public abstract class LocaleHolder {
    private static ThreadLocal<Locale> localeHolder = new ThreadLocal<Locale>();
    public static final String LOCAL_IN_SESSION_KEY="local_in_sesson_key";
    private static final String I18NUTILS_BEAN_ID = "i18nUtils";
    private static I18NUtils i18NUtils = null;

    public static void setLocale(Locale locale) {
        localeHolder.set(locale);
    }

    /**
     * 如果没有设置locale，则采用平台默认locale
     * @return
     */
    public static Locale getLocale() {
        return localeHolder.get()!=null?localeHolder.get():Locale.getDefault();
    }
    
    /**
     * 因i18NUtils在spring容器中是单实例的，所以没必要做成线程安全。
     * @param key
     * @return
     */
    public static String getMessage(String key,Object... params){
        if(i18NUtils==null)
            i18NUtils = (I18NUtils) VenusHelper.getBean(I18NUTILS_BEAN_ID);
        if(params!=null&&params.length>0)
            return i18NUtils.getMessage(key,params);
        return i18NUtils.getMessage(key);
    }

}
