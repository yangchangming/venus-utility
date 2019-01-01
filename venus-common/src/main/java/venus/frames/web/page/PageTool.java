package venus.frames.web.page;

import org.apache.commons.lang3.StringUtils;
import venus.frames.base.IGlobalsKeys;
import venus.frames.base.action.IRequest;
import venus.frames.mainframe.currentlogin.IProfile;
import venus.frames.mainframe.currentlogin.ProfileException;
import venus.frames.mainframe.log.ILog;
import venus.frames.mainframe.log.LogMgr;
import venus.frames.mainframe.util.ConfMgr;
import venus.frames.mainframe.util.DefaultConfReader;
import venus.frames.mainframe.util.Helper;
import venus.pub.util.Convertor;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @author wujun
 */
public class PageTool implements IGlobalsKeys {

    public static String PAGE_SAVE_LOCATION;

    public static boolean IS_CLEAR_OTHER_ACTIONS_SAVING = false;


    public static String PAGE_LOCATION_SESSION_KEY = "session";

    public static String PAGE_LOCATION_URL_KEY = "url";

    public static String PAGE_LOCATION_PROFILE_KEY = "profile";

    public static String PAGE_SAVING_ACTION_LIST_KEY = "VENUS_PAGE_SAVING_ACTION_LIST_KEY";

    public static String ORDER_STR_SAVING_ACTION_LIST_KEY = "VENUS_ORDER_STR_SAVING_ACTION_LIST_KEY";


    private static boolean IS_PAGE_SAVE_IN_SESSION = false;

    private static boolean IS_PAGE_SAVE_IN_PROFILE = false;

    private static boolean IS_PAGE_SAVE_IN_URL = false;


    private static List m_aryIgnoreActions = null;


    private static void loadConf() {

        //以本类名为标识从配置文件读取并配置数据(端口与启动任务列表)
        try {
            DefaultConfReader dcr =
                    new DefaultConfReader(
                            ConfMgr.getNode(PageTool.class.getName()));

            if (null == dcr)
                return;

            String[] astr = dcr.readChildStringAry("ignoreAction", "key");
            if (null == astr)
                return;

            m_aryIgnoreActions = Convertor.ArrayToList(astr);

        } catch (NullPointerException e) {
            LogMgr.getLogger(PageTool.class).info("loadConf()：read conf error, NullPointerException");
        }

    }

    private static void getPageSaveLocation() {

        if (IS_PAGE_SAVE_IN_URL == false && IS_PAGE_SAVE_IN_SESSION == false && IS_PAGE_SAVE_IN_PROFILE == false) {

            if (PAGE_SAVE_LOCATION != null) {

                if (PAGE_LOCATION_SESSION_KEY.equalsIgnoreCase(PAGE_SAVE_LOCATION)) {

                    IS_PAGE_SAVE_IN_SESSION = true;

                } else if (PAGE_LOCATION_PROFILE_KEY.equalsIgnoreCase(PAGE_SAVE_LOCATION)) {

                    IS_PAGE_SAVE_IN_PROFILE = true;

                } else if (PAGE_LOCATION_URL_KEY.equalsIgnoreCase(PAGE_SAVE_LOCATION)) {

                    IS_PAGE_SAVE_IN_URL = true;

                }

            } else {

                IS_PAGE_SAVE_IN_URL = true;

            }

            loadConf();

        }

    }

    /**
     * 获取pagevo的工具方法
     * <p/>
     * modify by changming.y on 2007-12-04
     * 解决如下bug
     * 先翻页，点击查询如果查不到结果，去掉查询条件再次查询，
     * 定位到0页，页面无结果
     *
     * @return
     */
    public static PageVo createPageVo(IRequest request, int recordCount) {
        PageVo pageVo = new PageVo();

        int pageNo = Helper.DEFAULT_PAGE_NO;
        int pageSize = Helper.DEFAULT_PAGE_SIZE;

        if (StringUtils.isNotBlank(request.getParameter(PAGE_NO_KEY))) {
            try {
                String s = request.getParameter(PAGE_NO_KEY);
                pageNo = Integer.parseInt(s);
                //解决如下bug
                //先翻页，点击查询如果查不到结果，去掉查询条件再次查询，
                //定位到0页，页面无结果
                if (recordCount != 0 && pageNo == 0)
                    pageNo = Helper.DEFAULT_PAGE_NO;
                else if (recordCount == 0)
                    pageNo = 0;
            } catch (Exception e) {
                getIlog().error("error in createPageVo: ", e);
            }
        }
        if (StringUtils.isNotBlank(request.getParameter(PAGE_SIZE_KEY))) {
            try {
                String s = request.getParameter(PAGE_SIZE_KEY);
                pageSize = Integer.parseInt(s);
            } catch (Exception e) {
                getIlog().error("error in createPageVo: ", e);
            }
        }

        //Fix bug: 先排序后翻页，排序条件丢失
        String orderKey = request.getParameter(ORDER_KEY);
        pageVo.setOrderKey(orderKey);

        pageVo.setRecordCount(recordCount);
        pageVo.setPageSize(pageSize);
        pageVo.setCurrentPage(pageNo);
        pageVo.rePageCount();

        savePageVo(pageVo, request);

        return pageVo;
    }



    public static PageVo createPageVo(ServletRequest request, int recordCount) {
        PageVo pageVo = new PageVo();

        int pageNo = Helper.DEFAULT_PAGE_NO;
        int pageSize = Helper.DEFAULT_PAGE_SIZE;

        if (StringUtils.isNotBlank(request.getParameter(PAGE_NO_KEY))) {
            try {
                String s = request.getParameter(PAGE_NO_KEY);
                pageNo = Integer.parseInt(s);
                //解决如下bug
                //先翻页，点击查询如果查不到结果，去掉查询条件再次查询，
                //定位到0页，页面无结果
                if (recordCount != 0 && pageNo == 0)
                    pageNo = Helper.DEFAULT_PAGE_NO;
                else if (recordCount == 0)
                    pageNo = 0;
            } catch (Exception e) {
                getIlog().error("error in createPageVo: ", e);
            }
        }
        if (StringUtils.isNotBlank(request.getParameter(PAGE_SIZE_KEY))) {
            try {
                String s = request.getParameter(PAGE_SIZE_KEY);
                pageSize = Integer.parseInt(s);
            } catch (Exception e) {
                getIlog().error("error in createPageVo: ", e);
            }
        }

        //Fix bug: 先排序后翻页，排序条件丢失
        String orderKey = request.getParameter(ORDER_KEY);
        pageVo.setOrderKey(orderKey);

        pageVo.setRecordCount(recordCount);
        pageVo.setPageSize(pageSize);
        pageVo.setCurrentPage(pageNo);
        pageVo.rePageCount();

        savePageVo(pageVo, request);

        return pageVo;
    }




    /**
     * 获取pagevo的工具方法
     *
     * @param name
     * @return
     */
    public static PageVo createPageVo(IRequest request) {

        Object o = request.getParameter(PAGE_ALLCOUNT_KEY);

        if (o != null && o instanceof String) {

            int recordCount = Integer.parseInt((String) o);

            return createPageVo(request, recordCount);

        }
        return null;
    }

    /**
     * 获取pagevo的工具方法
     *
     * @param name
     * @return
     */
    public static void savePageVo(PageVo pageVo, IRequest request) {

        try {
            getPageSaveLocation();
            request.setAttribute(PAGEVO_KEY, pageVo);
            if (IS_PAGE_SAVE_IN_PROFILE) {
                Set pageSavingActionSet = null;

                IProfile profile = request.getCurrentLoginProfile();

                Object o = profile.getAttribute(PAGE_SAVING_ACTION_LIST_KEY);

                if (o != null) {

                    pageSavingActionSet = (HashSet) o;

                    if (IS_CLEAR_OTHER_ACTIONS_SAVING) {

                        Iterator iters = pageSavingActionSet.iterator();

                        while (iters.hasNext()) {

                            Object key = iters.next();

                            if (m_aryIgnoreActions == null || !m_aryIgnoreActions.contains(key)) {

                                pageSavingActionSet.remove(key);

                                profile.removeAttribute((String) key);

                            }

                        }


                    }

                } else {

                    pageSavingActionSet = new HashSet();

                }

                String pageVoKey = request.getActionPath() + "." + PAGEVO_KEY;

                profile.setAttribute(pageVoKey, pageVo);

                pageSavingActionSet.add(pageVoKey);

                profile.setAttribute(PAGE_SAVING_ACTION_LIST_KEY, pageSavingActionSet);

            }

            if (IS_PAGE_SAVE_IN_SESSION) {

                Set pageSavingActionSet = null;

                HttpSession session = ((HttpServletRequest) request).getSession();

                Object o = session.getAttribute(PAGE_SAVING_ACTION_LIST_KEY);

                if (o != null) {

                    pageSavingActionSet = (HashSet) o;

                    if (IS_CLEAR_OTHER_ACTIONS_SAVING) {

                        Iterator iters = pageSavingActionSet.iterator();

                        while (iters.hasNext()) {

                            Object key = iters.next();

                            if (m_aryIgnoreActions == null || !m_aryIgnoreActions.contains(key)) {

                                pageSavingActionSet.remove(key);

                                session.removeAttribute((String) key);

                            }

                        }


                    }

                } else {

                    pageSavingActionSet = new HashSet();

                }

                String pageVoKey = request.getActionPath() + "." + PAGEVO_KEY;

                session.setAttribute(pageVoKey, pageVo);

                pageSavingActionSet.add(pageVoKey);

                session.setAttribute(PAGE_SAVING_ACTION_LIST_KEY, pageSavingActionSet);


            }


        } catch (Exception e) {

            getIlog().error("error in savePageVo: ", e);

        }

    }

    /**
     * 获取pagevo的工具方法
     *
     * @param name
     * @return
     */
    public static void savePageVo(PageVo pageVo, ServletRequest request) {
        if (request instanceof IRequest) {
            savePageVo(pageVo, (IRequest) request);
        }
        request.setAttribute(PAGEVO_KEY, pageVo);
    }


    public static PageVo findPageVo(IRequest request) {
        try {
            getPageSaveLocation();
            Object o = request.getParameter(PAGE_ALLCOUNT_KEY);
            if (o != null && o instanceof String) {
                int recordCount = Integer.parseInt((String) o);
                return createPageVo(request, recordCount);
            }
            o = request.getAttribute(PAGEVO_KEY);
            if (o != null && o instanceof PageVo) {
                return (PageVo) o;
            }
            if (IS_PAGE_SAVE_IN_SESSION) {

                Set pageSavingActionSet = null;

                HttpSession session = ((HttpServletRequest) request).getSession();

                String pageVoKey = request.getActionPath() + "." + PAGEVO_KEY;

                Object po = session.getAttribute(pageVoKey);

                if (po != null && po instanceof PageVo) {

                    return (PageVo) po;

                }

            }

            if (IS_PAGE_SAVE_IN_PROFILE) {

                Set pageSavingActionSet = null;

                IProfile profile;

                try {
                    profile = Helper.getSessionProfile((HttpServletRequest) request);


                } catch (ProfileException e) {

                    getIlog().error("error in findPageVo: ", e);
                    return null;

                }

                try {

                    String pageVoKey = ((IRequest) request).getActionPath() + "." + PAGEVO_KEY;

                    Object po = profile.getAttribute(pageVoKey);

                    return (PageVo) po;

                } catch (Exception e) {

                    getIlog().error("error in findPageVo: ", e);
                    return null;

                }


            }


        } catch (Exception e) {

            getIlog().error("error in findPageVo: ", e);

        }

        return null;


    }


    public static PageVo reloadPageVo(IRequest request) {
        ;
        PageVo pageVo = findPageVo(request);
        if (pageVo != null) savePageVo(pageVo, request);
        return pageVo;

    }

    public static PageVo reloadPageVoByOffset(IRequest request, int offset) {
        PageVo pageVo = findPageVo(request);

        if (pageVo != null && pageVo.getRecordCount() > 0) {

            int recordCnt = pageVo.getRecordCount() + offset;

            if (recordCnt < 0) {
                pageVo.setRecordCount(0);
                pageVo.setCurrentPage(0);
                pageVo.setPageCount(0);
            }

            int pageCunt = (int) Math.ceil((double) recordCnt / pageVo.getPageSize());

            int pageNo = pageVo.getCurrentPage();

            if (recordCnt != pageVo.getRecordCount()) pageVo.setRecordCount(recordCnt);

            if (pageNo > pageCunt) pageVo.setCurrentPage(pageCunt);

            if (pageCunt != pageVo.getPageCount()) pageVo.setPageCount(pageCunt);

            Helper.savePageVo(pageVo, request);

        }

        if (pageVo != null) savePageVo(pageVo, request);

        return pageVo;

    }

    public static PageVo updatePageVo(PageVo pageVo, IRequest request) {
        int pageNo = Helper.DEFAULT_PAGE_NO;
        int pageSize = Helper.DEFAULT_PAGE_SIZE;

        if (request.getParameter(PAGE_NO_KEY) != null) {
            try {
                String s = request.getParameter(PAGE_NO_KEY);
                pageNo = Integer.parseInt(s);
            } catch (Exception e) {
                getIlog().error("error in updatePageVo: ", e);
            }
            pageVo.setCurrentPage(pageNo);
        }

        if (request.getParameter(PAGE_SIZE_KEY) != null) {
            try {
                String s = request.getParameter(PAGE_SIZE_KEY);
                pageSize = Integer.parseInt(s);
            } catch (Exception e) {
                getIlog().error("error in updatePageVo: ", e);
            }
            pageVo.setPageSize(pageSize);
            pageVo.rePageCount();

        }

        if (pageVo != null) savePageVo(pageVo, request);

        return pageVo;

    }

//    public static PageVo updatePageVo(PageVo pageVo, ServletRequest request) {
//
//        if (request instanceof IRequest) {
//
//            return updatePageVo(pageVo, (IRequest) request);
//
//        }
//
//
//        int pageNo = Helper.DEFAULT_PAGE_NO;
//        int pageSize = Helper.DEFAULT_PAGE_SIZE;
//
//        if (request.getParameter(PAGE_NO_KEY) != null) {
//            try {
//                String s = request.getParameter(PAGE_NO_KEY);
//                pageNo = Integer.parseInt(s);
//            } catch (Exception e) {
//                getIlog().error("error in updatePageVo: ", e);
//            }
//            pageVo.setCurrentPage(pageNo);
//        }
//
//        if (request.getParameter(PAGE_SIZE_KEY) != null) {
//            try {
//                String s = request.getParameter(PAGE_SIZE_KEY);
//                pageSize = Integer.parseInt(s);
//            } catch (Exception e) {
//                getIlog().error("error in updatePageVo: ", e);
//            }
//            pageVo.setPageSize(pageSize);
//            pageVo.rePageCount();
//
//        }
//
//        if (pageVo != null) savePageVo(pageVo, request);
//
//        return pageVo;
//
//    }

    public static String reloadOrderStr(IRequest request) {
        ;
        String orderStr = findOrderStr(request);
        if (orderStr != null) saveOrderStr(orderStr, request);
        return orderStr;

    }

    /**
     * 获取pagevo的工具方法
     *
     * @param name
     * @return
     */
    public static void saveOrderStr(String orderStr, IRequest request) {

        try {


            request.setAttribute(ORDER_KEY, orderStr);


            if (IS_PAGE_SAVE_IN_SESSION) {

                Set orderSavingActionSet = null;

                HttpSession session = ((HttpServletRequest) request).getSession();

                Object o = session.getAttribute(ORDER_STR_SAVING_ACTION_LIST_KEY);

                if (o != null) {

                    orderSavingActionSet = (HashSet) o;

                    if (IS_CLEAR_OTHER_ACTIONS_SAVING) {

                        Iterator iters = orderSavingActionSet.iterator();

                        while (iters.hasNext()) {

                            Object key = iters.next();

                            if (m_aryIgnoreActions == null || !m_aryIgnoreActions.contains(key)) {

                                orderSavingActionSet.remove(key);

                            }

                        }


                    }

                } else {

                    orderSavingActionSet = new HashSet();

                }

                String orderKey = ((IRequest) request).getActionPath() + "." + ORDER_KEY;

                session.setAttribute(orderKey, orderStr);

                orderSavingActionSet.add(orderKey);

                session.setAttribute(ORDER_STR_SAVING_ACTION_LIST_KEY, orderSavingActionSet);


            }

            if (IS_PAGE_SAVE_IN_PROFILE) {

                Set orderSavingActionSet = null;

                IProfile profile;

                try {
                    profile = Helper.getSessionProfile((HttpServletRequest) request);
                } catch (ProfileException e) {

                    getIlog().error("error in saveOrderStr: ", e);
                    return;

                }

                Object o = profile.getAttribute(ORDER_STR_SAVING_ACTION_LIST_KEY);

                if (o != null) {

                    orderSavingActionSet = (HashSet) o;

                    if (IS_CLEAR_OTHER_ACTIONS_SAVING) {

                        Iterator iters = orderSavingActionSet.iterator();

                        while (iters.hasNext()) {

                            Object key = iters.next();

                            if (m_aryIgnoreActions == null || !m_aryIgnoreActions.contains(key)) {

                                orderSavingActionSet.remove(key);

                            }

                        }


                    }

                } else {

                    orderSavingActionSet = new HashSet();

                }

                String orderKey = ((IRequest) request).getActionPath() + "." + ORDER_KEY;

                profile.setAttribute(orderKey, orderStr);

                orderSavingActionSet.add(orderKey);

                profile.setAttribute(ORDER_STR_SAVING_ACTION_LIST_KEY, orderSavingActionSet);


            }
        } catch (Exception e) {
            getIlog().error("error in saveOrderStr: ", e);
        }
    }

    public static String findOrderStr(IRequest request) {
        try {
            getPageSaveLocation();

            String orstr = request.getParameter(ORDER_KEY);

            if (orstr != null && orstr.trim().length() > 0) {

                return orstr;

            }
            Object o = request.getAttribute(ORDER_KEY);

            if (o != null && o instanceof String) {

                return (String) o;

            }

            if (IS_PAGE_SAVE_IN_SESSION) {

                Set pageSavingActionSet = null;

                HttpSession session = ((HttpServletRequest) request).getSession();

                String pageOrderKey = request.getActionPath() + "." + ORDER_KEY;

                Object so = session.getAttribute(pageOrderKey);

                if (so != null && so instanceof String) {

                    return (String) so;

                }

            }

            if (IS_PAGE_SAVE_IN_PROFILE) {

                Set pageSavingActionSet = null;

                IProfile profile;

                try {
                    profile = Helper.getSessionProfile((HttpServletRequest) request);
                } catch (ProfileException e) {

                    getIlog().error("error in findOrderStr: ", e);
                    return null;

                }

                String pageOrderKey = ((IRequest) request).getActionPath() + "." + ORDER_KEY;

                Object so = profile.getAttribute(pageOrderKey);

                if (so != null && so instanceof String) {

                    return (String) so;

                }


            }


        } catch (Exception e) {

            getIlog().error("error in findOrderStr: ", e);

        }

        return null;

    }

    public static String findOrderStr(ServletRequest request) {

        try {

            getPageSaveLocation();

            if (request instanceof IRequest) {

                return findOrderStr(request);

            }

            Object o = request.getAttribute(ORDER_KEY);

            if (o != null && o instanceof String) {

                return (String) o;

            }

            if (IS_PAGE_SAVE_IN_SESSION) {

                Set pageSavingActionSet = null;

                HttpSession session = ((HttpServletRequest) request).getSession();

                String orderStrKey = ((HttpServletRequest) request).getRequestURL() + "." + ORDER_KEY;

                Object os = session.getAttribute(orderStrKey);

                if (os != null && os instanceof String) {

                    return (String) os;

                }

            }

            if (IS_PAGE_SAVE_IN_PROFILE) {

                Set pageSavingActionSet = null;

                IProfile profile;

                try {
                    profile = Helper.getSessionProfile((HttpServletRequest) request);
                } catch (ProfileException e) {

                    getIlog().error("error in findOrderStr: ", e);
                    return null;

                }

                String orderStrKey = ((HttpServletRequest) request).getRequestURL() + "." + ORDER_KEY;

                Object os = profile.getAttribute(orderStrKey);

                if (os != null && os instanceof String) {

                    return (String) os;

                }

            }


        } catch (Exception e) {

            getIlog().error("error in findOrderStr: ", e);

        }

        return null;

    }

    public static PageVo findPageVo(ServletRequest request) {

        getPageSaveLocation();

        if (request instanceof IRequest) {

            return findPageVo((IRequest) request);

        }

        return null;
    }

    public static void clearAllPageVo(IRequest request) {

        try {

            getPageSaveLocation();

            ((HttpServletRequest) request).removeAttribute(PAGEVO_KEY);


            if (IS_PAGE_SAVE_IN_PROFILE) {

                Set pageSavingActionSet = null;

                IProfile profile = request.getCurrentLoginProfile();

                Object o = profile.getAttribute(PAGE_SAVING_ACTION_LIST_KEY);

                if (o != null) {

                    pageSavingActionSet = (HashSet) o;

                    Iterator iters = pageSavingActionSet.iterator();

                    while (iters.hasNext()) {

                        Object key = iters.next();

                        profile.removeAttribute((String) key);

                    }

                    profile.removeAttribute(PAGE_SAVING_ACTION_LIST_KEY);

                }

            }

            if (IS_PAGE_SAVE_IN_SESSION) {

                Set pageSavingActionSet = null;

                HttpSession session = ((HttpServletRequest) request).getSession();

                Object o = session.getAttribute(PAGE_SAVING_ACTION_LIST_KEY);

                if (o != null) {

                    pageSavingActionSet = (HashSet) o;

                    Iterator iters = pageSavingActionSet.iterator();

                    while (iters.hasNext()) {

                        Object key = iters.next();

                        session.removeAttribute((String) key);

                    }

                    session.removeAttribute(PAGE_SAVING_ACTION_LIST_KEY);

                }

            }


        } catch (Exception e) {

            getIlog().error("error in clearAllPageVo: ", e);

        }


    }


    public static void clearPageVo(IRequest request) {

        try {

            getPageSaveLocation();

            ((HttpServletRequest) request).removeAttribute(PAGEVO_KEY);


            if (IS_PAGE_SAVE_IN_PROFILE) {

                Set pageSavingActionSet = null;

                IProfile profile = request.getCurrentLoginProfile();

                String pageVoKey = request.getActionPath() + "." + PAGEVO_KEY;

                Object o = profile.getAttribute(PAGE_SAVING_ACTION_LIST_KEY);

                if (o != null) {

                    pageSavingActionSet = (HashSet) o;

                    pageSavingActionSet.remove(pageVoKey);

                } else {

                    pageSavingActionSet = new HashSet();

                }


                profile.removeAttribute(pageVoKey);

                profile.setAttribute(PAGE_SAVING_ACTION_LIST_KEY, pageSavingActionSet);

            }

            if (IS_PAGE_SAVE_IN_SESSION) {

                Set pageSavingActionSet = null;

                HttpSession session = ((HttpServletRequest) request).getSession();

                String pageVoKey = request.getActionPath() + "." + PAGEVO_KEY;

                Object o = session.getAttribute(PAGE_SAVING_ACTION_LIST_KEY);

                if (o != null) {

                    pageSavingActionSet = (HashSet) o;

                    pageSavingActionSet.remove(pageVoKey);

                } else {

                    pageSavingActionSet = new HashSet();

                }

                session.removeAttribute(pageVoKey);

                session.setAttribute(PAGE_SAVING_ACTION_LIST_KEY, pageSavingActionSet);


            }


        } catch (Exception e) {

            getIlog().error("error in clearPageVo: ", e);

        }


    }

    /**
     * 得到日志记录驱动实例
     *
     * @return ILog LOG驱动实例
     */
    private static ILog getIlog() {
        return LogMgr.getLogger(PageTool.class);
    }


    public static void clearOrderStr(IRequest request) {

        try {

            getPageSaveLocation();

            ((HttpServletRequest) request).removeAttribute(ORDER_KEY);


            if (IS_PAGE_SAVE_IN_PROFILE) {

                Set orderSavingActionSet = null;

                IProfile profile = request.getCurrentLoginProfile();

                String orderStrKey = request.getActionPath() + "." + ORDER_KEY;

                Object o = profile.getAttribute(ORDER_STR_SAVING_ACTION_LIST_KEY);

                if (o != null) {

                    orderSavingActionSet = (HashSet) o;

                    orderSavingActionSet.remove(orderStrKey);

                } else {

                    orderSavingActionSet = new HashSet();

                }


                profile.removeAttribute(orderStrKey);

                profile.setAttribute(ORDER_STR_SAVING_ACTION_LIST_KEY, orderSavingActionSet);

            }

            if (IS_PAGE_SAVE_IN_SESSION) {

                Set orderSavingActionSet = null;

                HttpSession session = ((HttpServletRequest) request).getSession();

                String orderStrKey = request.getActionPath() + "." + ORDER_KEY;

                Object o = session.getAttribute(ORDER_STR_SAVING_ACTION_LIST_KEY);

                if (o != null) {

                    orderSavingActionSet = (HashSet) o;

                    orderSavingActionSet.remove(orderStrKey);

                } else {

                    orderSavingActionSet = new HashSet();

                }

                session.removeAttribute(orderStrKey);

                session.setAttribute(ORDER_STR_SAVING_ACTION_LIST_KEY, orderSavingActionSet);


            }


        } catch (Exception e) {

            getIlog().error("error in clearOrderStr: ", e);

        }


    }


    public static void clearAllOrderStr(IRequest request) {

        try {

            getPageSaveLocation();

            ((HttpServletRequest) request).removeAttribute(PAGEVO_KEY);


            if (IS_PAGE_SAVE_IN_PROFILE) {

                Set orderSavingActionSet = null;

                IProfile profile = request.getCurrentLoginProfile();

                Object o = profile.getAttribute(ORDER_STR_SAVING_ACTION_LIST_KEY);

                if (o != null) {

                    orderSavingActionSet = (HashSet) o;

                    Iterator iters = orderSavingActionSet.iterator();

                    while (iters.hasNext()) {

                        Object key = iters.next();

                        profile.removeAttribute((String) key);

                    }

                    profile.removeAttribute(ORDER_STR_SAVING_ACTION_LIST_KEY);

                }

            }

            if (IS_PAGE_SAVE_IN_SESSION) {

                Set orderSavingActionSet = null;

                HttpSession session = ((HttpServletRequest) request).getSession();

                Object o = session.getAttribute(ORDER_STR_SAVING_ACTION_LIST_KEY);

                if (o != null) {

                    orderSavingActionSet = (HashSet) o;

                    Iterator iters = orderSavingActionSet.iterator();

                    while (iters.hasNext()) {

                        Object key = iters.next();

                        session.removeAttribute((String) key);

                    }

                    session.removeAttribute(ORDER_STR_SAVING_ACTION_LIST_KEY);

                }

            }

        } catch (Exception e) {

            getIlog().error("error in clearAllOrderStr: ", e);

        }


    }

    /**
     * 获取pagevo的工具方法
     *
     * @param name
     * @return
     */
    public static String getPageParamStr(HttpServletRequest request) {
        String re = "";


        PageVo pageVo = Helper.findPageVo(request);

        if (pageVo != null) {

            re = PAGE_NO_KEY + "=" + pageVo.getCurrentPage() + "&" + PAGE_SIZE_KEY + "=" + pageVo.getPageSize() + "&" + PAGE_ALLCOUNT_KEY + "=" + pageVo.getRecordCount();


            return re;
        }

        String pageNo = request.getParameter(PAGE_NO_KEY);

        String pageSize = request.getParameter(PAGE_SIZE_KEY);

        String recordCount = request.getParameter(PAGE_ALLCOUNT_KEY);

        if (pageNo != null && pageSize != null && recordCount != null) {

            try {

                re = PAGE_NO_KEY + "=" + pageNo + "&" + PAGE_SIZE_KEY + "=" + pageSize + "&" + PAGE_ALLCOUNT_KEY + "=" + recordCount;

            } catch (Exception e) {
                LogMgr.getLogger(PageTool.class).error("error in getPageParamStr(...) ", e);
            }
        }


        return re;
    }


    /**
     * 从Request中提取路径，
     * @param req
     * @param sortAction
     * @return
     */
    private static String changeUrlForAction(HttpServletRequest req, String sortAction) {

        String url;
        String actionStr = sortAction;

        //目前这种改法只支持单列表。20050819 update BY wj
        if (actionStr != null) {
            url = req.getContextPath() + "/" + actionStr;
        } else {
            if (req instanceof IRequest) {
                url = req.getContextPath() + ((IRequest) req).getActionPath() + ".do";
            } else {
                Object reqeustUrl = req.getAttribute("javax.servlet.forward.request_uri");
                if (reqeustUrl != null) {
                    url = reqeustUrl.toString();
                } else {
                    url = req.getRequestURI();
                }
            }
        }
        return url;

    }

    /**
     * 根据request获取所有参数信息并组装成排序按钮需要的url
     * 注意：ORDER_KEY的排序组合需要放到url的最后面
     * modify by changming.y on 2007-12-04
     *
     * @param req
     * @return url
     */
    private static StringBuffer getUrlFromParameters(HttpServletRequest req) {

        Enumeration enu = req.getParameterNames();
        StringBuffer re = new StringBuffer();

        boolean isOrder = false;
        while (enu.hasMoreElements()) {
            String pstr = (String) enu.nextElement();
            //如有ORDER_KEY，把其放入url的最后，因为BasicCollection中的排序判断
            //使用了endWith()
            if (pstr.equals(ORDER_KEY)) {
                isOrder = true;
                continue;
            }
            String value = req.getParameter(pstr);
            re.append(pstr).append("=").append(value);
            if (enu.hasMoreElements()) re.append("&");
        }
        if (isOrder) {
            String orderValue = req.getParameter(ORDER_KEY);
            re.append("&").append(ORDER_KEY).append("=").append(orderValue);
        }
        return re;
    }


    /**
     * 不再采用request.getQueryString的方式获取url，因为web容器不会对该方法自动
     * 解码，直接使用getUrlFromParameters方法进行url组装
     * modify by changming.y on 2007-12-04
     *
     * @param req
     * @param sortParam
     * @return url
     */
    private static StringBuffer changeUrlForCmd(HttpServletRequest req, String sortParam) {

        StringBuffer url = getUrlFromParameters(req);
        String cmdStr = sortParam;
//		目前这种改法只支持单列表。20050819 update BY wj		
        if (cmdStr != null) {
            int pos_cmd = url.indexOf("cmd=");

            if (pos_cmd > -1) {
                String requestPathStr_end = url.substring(pos_cmd + 4);
                String requestPathStr_start = url.substring(0, pos_cmd + 4);
                int pos_cmd_1 = requestPathStr_end.indexOf('&');

                if (pos_cmd_1 > -1) {
                    url = new StringBuffer(requestPathStr_start + cmdStr + requestPathStr_end.substring(pos_cmd_1));
                } else {
                    url = new StringBuffer(requestPathStr_start + cmdStr);
                }
            } else {
                if (url.indexOf("?") > -1) {
                    url.append("&cmd=" + cmdStr);
                } else {
                    url.append("cmd=" + cmdStr);
                }
            }
        }
        return url;
    }


    public static String getOrderURL(String orderStrName, HttpServletRequest req, String sortAction, String sortParam) {

        String url = changeUrlForAction(req, sortAction);
        StringBuffer sb = changeUrlForCmd(req, sortParam);

        String OrderStr = findOrderStr(req);

        if (OrderStr != null && OrderStr.length() > 0) {

            int descpos = orderStrName.indexOf(Helper.SORT_SYMBOL_DESC);
            int ascpos = orderStrName.indexOf(Helper.SORT_SYMBOL_ASC);

            if (descpos > 0) {
                orderStrName = orderStrName.substring(0, descpos).trim();
            }

            if (ascpos > 0) {
                orderStrName = orderStrName.substring(0, ascpos).trim();
            }

            if (OrderStr.indexOf(orderStrName) >= 0) {
                if (OrderStr.indexOf(Helper.SORT_SYMBOL_DESC) > 0) {
                    sb = repleaceOrderParam(sb, orderStrName, " " + Helper.SORT_SYMBOL_ASC);
                    return url + "?" + sb;
                }
                sb = repleaceOrderParam(sb, orderStrName, " " + Helper.SORT_SYMBOL_DESC);
                return url + "?" + sb;
            }
            sb = repleaceOrderParam(sb, orderStrName, "");
            return url + "?" + sb;
        }
        if(sb.length() == 0){
            sb = sb.append(Helper.ORDER_KEY + "=" + orderStrName);
        }  else{
            sb = sb.append("&" + Helper.ORDER_KEY + "=" + orderStrName);
        }
        return url + "?" + sb;
    }

    private static StringBuffer repleaceOrderParam(StringBuffer sb, String orderStrName, String sortSymbol) {
        String sbstr = sb.toString();

        if (sbstr.indexOf(Helper.ORDER_KEY) >= 0) {
            int start = sbstr.indexOf("&" + Helper.ORDER_KEY + "=");
            int stop = sbstr.indexOf("&", start + 1);

            if (stop == -1) {
                stop = sb.length();
            }

            sb = sb.delete(start, stop);
            if (start > 0) {
                sb = sb.insert(start,"&" + Helper.ORDER_KEY + "=" + orderStrName + sortSymbol);
            }else{
                sb = sb.insert(start, Helper.ORDER_KEY + "=" + orderStrName + sortSymbol);
            }


        } else {
            sb = sb.append("&" + Helper.ORDER_KEY + "=" + orderStrName + sortSymbol);
        }
        return sb;
    }

    private static String findOrderStr(HttpServletRequest request) {
        String re = "";
        ;
        String orstr = request.getParameter(ORDER_KEY);

        if (orstr != null && orstr.length() > 0) {

            re = orstr;

        } else {

            Object o = request.getAttribute(ORDER_KEY);
            if (o != null && o instanceof String) {

                re = (String) o;

            }
        }
        return re;


    }


}
