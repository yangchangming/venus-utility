package venus.frames.i18n.filter;

import org.apache.commons.lang3.StringUtils;
import venus.frames.i18n.util.LocaleHolder;
import venus.frames.mainframe.log.LogMgr;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;

public class LocalFilter implements Filter {
    
    private static String LOCALE_STRING_SPLIT = "_"; 

    public void destroy() {}

    /**
     * 如果session中没有设置locale串或者locale串不合法，默认采用request的locale，否则采用session中设置的locale。
     */
    public void doFilter(ServletRequest req, ServletResponse res,
            FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)req;
        HttpSession session = request.getSession(true);
        String localString = (String)session.getAttribute(LocaleHolder.LOCAL_IN_SESSION_KEY);
        if(StringUtils.isEmpty(localString)){
            LocaleHolder.setLocale(request.getLocale());
        }
        else{
            String[] localeStrings = localString.split(LOCALE_STRING_SPLIT);
            Locale locale = null;
            switch (localeStrings.length) {
            case 1:
                locale = new Locale(localeStrings[0]);
                break;
            case 2:
                locale = new Locale(localeStrings[0],localeStrings[1]);
                break;
            case 3:
                locale = new Locale(localeStrings[0],localeStrings[1],localeStrings[2]);
                break;
            default:
                locale = request.getLocale();
                break;
            }
            if(StringUtils.isEmpty(locale.getLanguage())||!locale.getLanguage().equals(localeStrings[0])){
                locale = request.getLocale();
                LogMgr.getLogger(this).warn("Given locale from request is not a valid locale,Default to the request  locale");
            }   
            LocaleHolder.setLocale(locale);
        }
        filterChain.doFilter(request, res);
    }

    public void init(FilterConfig arg0) throws ServletException {}
}
