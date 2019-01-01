package venus.frames.i18n.util;

import org.springframework.context.MessageSource;

import java.util.Locale;

/**
 * 国际化工具类，从资源文件中获取值。 需要在spring配置文件中做如下配置：
 * 
 *  <bean id="messageSource" class="org.springframework.context.support.ResourceBundleMessageSource">
 *      <property name="basenames">
 *         <list>
 *            <value>app</value>
 *            <value>org.rx.component.i18n.messages</value>
 *         </list>
 *     </property>
 *  </bean>
 *  
 *  <bean id="i18nUtils" class="org.rx.component.i18n.I18NUtils">
 *     <property name="messageSource">
 *         <ref local="messageSource"/>
 *     </property>
 *  </bean>
 * 
 */
public class I18NUtils {
    private MessageSource messageSource;

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * 调用LocaleHolder.getLocale()来获得locale
     * @param key
     * @return
     */
    public String getMessage(String key) {
        return getMessage(key, null, LocaleHolder.getLocale());
    }
    /**
     * 调用LocaleHolder.getLocale()来获得locale
     * @param key
     * @param paras 参数
     * @see LocaleHolder ,
     */
    public String getMessage(String key,Object[] paras) {
        return getMessage(key, paras, LocaleHolder.getLocale());
    }
    /**
     * @param key 
     * @param language  语言代码
     * @return
     */
    public String getMessage(String key, String language) {
        return getMessage(key, null, new Locale(language));
    }

    public String getMessage(String key, Locale locale) {
        return getMessage(key, null, locale);
    }
    public String getMessage(String key,Object[] paras,Locale locale) {
        return messageSource.getMessage(key, paras, locale);
    }

}
