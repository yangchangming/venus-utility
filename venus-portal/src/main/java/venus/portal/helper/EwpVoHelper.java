package venus.portal.helper;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.NotReadablePropertyException;
import venus.frames.base.action.IRequest;
import venus.frames.i18n.util.LocaleHolder;
import venus.portal.util.IEwpToolsConstants;
import venus.portal.util.IEwpVoStamp;

import javax.servlet.http.HttpServletRequest;
import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.Map.Entry;

public class EwpVoHelper implements IEwpToolsConstants {

    /**
     * 对Object打修改的时间和IP戳
     * 
     * @param request 来自页面的请求
     * @param myVo 输入一个VO
     */
    public static int markModifyStamp(final IRequest request, Object thisObj) {
        return markModifyStamp((HttpServletRequest) request, thisObj);
    }

    /**
     * 对Object打修改的时间和IP戳
     * 
     * @param request 来自页面的请求
     * @param myVo 输入一个VO
     */
    public static int markModifyStamp(final HttpServletRequest request, Object thisObj) {
        return accessVo(thisObj, new IEwpTransctVoField() {
            public int transctVo(BeanWrapper bw, PropertyDescriptor pd) {
                if (!pd.getName().equals("class")) {
                    if (pd.getName().equals(DESC_MODIFY_DATE)) {
                        if (bw.getPropertyType(pd.getName()).getName().equals("java.sql.Timestamp")) {
                            bw.setPropertyValue(pd.getName(), EwpDateHelper.getSysTimestamp());
                        } else {
                            bw.setPropertyValue(pd.getName(), EwpDateHelper.getSysDateTime());
                        }
                        return 1;
                    } else if (pd.getName().equals(DESC_USABLE_STATUS)) { //数据还活着，加上了打逻辑删除标记启用的戳，数据设为可用
                        bw.setPropertyValue(pd.getName(), STATUS_ENABLE);
                        return 1;
                    } else if (pd.getName().equals(DESC_MODIFY_IP)) {
                        HttpServletRequest req = (HttpServletRequest) request;
                        String modify_ip = req.getRemoteAddr();
                        bw.setPropertyValue(pd.getName(), modify_ip);
                        return 1;
                    } else if (pd.getName().equals(DESC_MODIFY_USER_ID)) {
                        String create_user_id = null;
                        try {
                            create_user_id = EwpJspHelper.getParty_idFromRequest(request);
                        } catch (Exception e) {
                            EwpStringHelper.log(e.getMessage());
                        }
                        bw.setPropertyValue(pd.getName(), create_user_id);
                        return 1;
                    } else {
                        return 0;
                    }
                } else {
                    return 0;
                }
            }
        });
    }

    /**
     * 借助BeanWrapper循环Vo
     * 
     * @param obj 输入一个VO
     * @return 被替换的值个数
     */
    public static int accessVo(Object obj, IEwpTransctVoField transctVoField) {
        int returnCount = 0;
        try {
            BeanWrapper bw = new BeanWrapperImpl(obj);
            PropertyDescriptor pd[] = bw.getPropertyDescriptors();
            for (int i = 0; i < pd.length; i++) {
                try {
                    returnCount += transctVoField.transctVo(bw, pd[i]);
                } catch (ClassCastException e) {
                    continue;
                } catch (NotReadablePropertyException e) {
                    continue;
                }
            }
        } catch (Exception e) {
            EwpStringHelper.log(e.getMessage());
        }
        return returnCount;
    }

    /**
     * 功能: 从request中获取表单值
     *
     * @param request
     * @return
     */
    public static Map getMapFromRequest(IRequest request) {
        return getMapFromRequest(request, null);
    }

    /**
     * 从request中获取表单值
     * 
     * @param request
     * @return
     */
    public static Map getMapFromRequest(HttpServletRequest request) {
        return getMapFromRequest(request, null);
    }

    /**
     * 功能: 从request中获取表单值
     *
     * @param request
     * @param aNeedName 关注的key值
     * @return
     */
    public static Map getMapFromRequest(IRequest request, String[] aNeedName) {
        return getMapFromRequest((HttpServletRequest) request, aNeedName);
    }

    /**
     * 从request中获取表单值
     * 
     * @param request
     * @param aNeedName 关注的key值 
     * @return
     */
    public static Map getMapFromRequest(HttpServletRequest request, String[] aNeedName) {
        Map rtMap = new TreeMap();
        Iterator itParms = request.getParameterMap().keySet().iterator();
        while (itParms.hasNext()) {
            String tempKey = (String) itParms.next();
            if (aNeedName != null && !EwpStringHelper.containStringInArray(aNeedName, tempKey)) {
                continue;
            }
            String[] tempArray = request.getParameterValues(tempKey);
            if (tempArray == null || tempArray.length == 0) {
                continue;
            }
            if (tempArray.length == 1) {
                rtMap.put(tempKey, request.getParameter(tempKey));
            } else { //杜绝了相同value的提交值多次被回写
                Set sUniqueValue = new HashSet();
                for (int i = 0; i < tempArray.length; i++) {
                    sUniqueValue.add(tempArray[i]);
                }
                rtMap.put(tempKey, sUniqueValue.toArray(new String[0]));
            }
        }
        return rtMap;
    }

    /**
     * 对实现了IEwpVoStamp接口的VO打创建的时间和IP戳
     * 
     * @param request 来自页面的请求
     * @param myVo 输入一个VO
     */
    public static void markCreateStamp(IRequest request, IEwpVoStamp myVo) {
        myVo.setCreate_date(EwpDateHelper.getSysDateTime());
        HttpServletRequest req = (HttpServletRequest) request;
        String create_ip = req.getRemoteAddr();
        myVo.setCreate_ip(create_ip);
        return;
    }

    /**
     * 对Object打创建的时间和IP戳
     * 
     * @param request 来自页面的请求
     * @param myVo 输入一个VO
     */
    public static int markCreateStamp(final IRequest request, Object thisObj) {
        return markCreateStamp((HttpServletRequest) request, thisObj);
    }

    /**
     * 对Object打创建的时间和IP戳
     * 
     * @param request 来自页面的请求
     * @param myVo 输入一个VO
     */
    public static int markCreateStamp(final HttpServletRequest request, Object thisObj) {
        return accessVo(thisObj, new IEwpTransctVoField() {
            public int transctVo(BeanWrapper bw, PropertyDescriptor pd) {
                if (!pd.getName().equals("class")) {
                    if (pd.getName().equals(DESC_CREATE_DATE)) {
                        if (bw.getPropertyType(pd.getName()).getName().equals("java.sql.Timestamp")) {
                            bw.setPropertyValue(pd.getName(), EwpDateHelper.getSysTimestamp());
                        } else {
                            bw.setPropertyValue(pd.getName(), EwpDateHelper.getSysDateTime());
                        }
                        return 1;
                    } else if (pd.getName().equals(DESC_CREATE_IP)) {
                        HttpServletRequest req = (HttpServletRequest) request;
                        String create_ip = req.getRemoteAddr();
                        bw.setPropertyValue(pd.getName(), create_ip);
                        return 1;
                    } else if (pd.getName().equals(DESC_USABLE_STATUS)) { //加上了打逻辑删除标记启用的戳，数据设为可用
                        bw.setPropertyValue(pd.getName(), STATUS_ENABLE);
                        return 1;
                    } else if (pd.getName().equals(DESC_CREATE_USER_ID)) {
                        String create_user_id = null;
                        try {
                            create_user_id = EwpJspHelper.getParty_idFromRequest(request);
                        } catch (Exception e) {
                            EwpStringHelper.log(e.getMessage());
                        }
                        bw.setPropertyValue(pd.getName(), create_user_id);
                        return 1;
                    } else {
                        return 0;
                    }
                } else {
                    return 0;
                }
            }
        });
    }

    /**
     * 对实现了IEwpVoStamp接口的VO打修改的时间和IP戳
     * 
     * @param request 来自页面的请求
     * @param myVo 输入一个VO
     */
    public static void markModifyStamp(IRequest request, IEwpVoStamp myVo) {
        myVo.setModify_date(EwpDateHelper.getSysDateTime());
        HttpServletRequest req = (HttpServletRequest) request;
        String modify_ip = req.getRemoteAddr();
        myVo.setModify_ip(modify_ip);
        return;
    }

    /**
     * 对实现了IEwpVoStamp接口的VO打启用/禁用的时间和IP戳
     * 
     * @param request 来自页面的请求
     * @param myVo 输入一个VO
     */
    public static void markEnableStamp(IRequest request, IEwpVoStamp myVo) {
        myVo.setEnable_date(EwpDateHelper.getSysDateTime());
        HttpServletRequest req = (HttpServletRequest) request;
        String enable_ip = req.getRemoteAddr();
        myVo.setEnable_ip(enable_ip);
        return;
    }

    /**
     * 把VO中的关键字值一律替换成ASCII码表示，同时把null换为""
     * 
     * @param obj 输入一个VO
     * @return 操作次数
     */
    public static int replaceToHtml(Object obj) {
        return replaceToHtml(obj, null);
    }

    /**
     * 把VO中的关键字值一律替换成ASCII码表示，同时把null换为""
     * 
     * @param obj 输入一个VO
     * @param ignoreName 
     * @return 操作次数
     */
    public static int replaceToHtml(Object obj, final String[] ignoreName) {
        return accessVo(obj, new IEwpTransctVoField() {
            public int transctVo(BeanWrapper bw, PropertyDescriptor pd) {
                if (!pd.getName().equals("class")) {
                    if (ignoreName != null && ignoreName.length > 0 && EwpStringHelper.containStringInArray(ignoreName, pd.getName())) {
                        return 0;
                    }
                    String tempValue = (String) bw.getPropertyValue(pd.getName());
                    if (tempValue == null && "java.lang.String".equals(pd.getPropertyType().getName())) {
                        bw.setPropertyValue(pd.getName(), "");
                        return 1;
                    } else if ("java.lang.String".equals(pd.getPropertyType().getName())) {
                        bw.setPropertyValue(pd.getName(), EwpStringHelper.replaceStringToHtml(tempValue));
                        return 1;
                    } else {
                        return 0;
                    }
                } else {
                    return 0;
                }
            }
        });

    }

    /**
     * 回写表单
     *
     * @param mRequest
     * @return
     */
    public static String writeBackMapToForm(Map mRequest) {
        return writeBackMapToForm(mRequest, new String[] {}, "writeBackMapToForm");
    }

    /**
     * 回写表单
     *
     * @param mRequest
     * @param ignoreName 定义哪些key值的input不回写
     * @return
     */
    public static String writeBackMapToForm(Map mRequest, String[] ignoreName, String jsFunctionName) {
        mRequest.remove("checkbox_template"); //不回写列表中checkbox的值
        StringBuffer rtValue = new StringBuffer();
        rtValue.append("  var mForm = new Object();\n");
        rtValue.append("  var indexArray = new Array();\n");
        rtValue.append("  function writeBackMapToForm() {\n");
        Iterator itMRequest = mRequest.entrySet().iterator();
        while (itMRequest.hasNext()) {
            Entry entry = (Entry) itMRequest.next();
            String key = (String) entry.getKey();
            Object tempValue = mRequest.get(key);
            if (key.startsWith("VENUS") || key.startsWith("RANMIN")) {
                continue;
            }
            if (EwpStringHelper.containStringInArray(ignoreName, key)) {
                continue;
            }
            if (tempValue == null) {
                continue;
            }
            String tempValueNew = "";
            if (tempValue instanceof String) { //如果是单值，直接注入
                tempValueNew = EwpStringHelper.replaceStringToScript((String) tempValue); //从数据库中取出来以后需要转换1次
                rtValue.append("    indexArray[indexArray.length] = \"" + key + "\";\n");
                rtValue.append("    mForm[\"" + key + "\"] = \"" + tempValueNew + "\";\n");
            } else if (tempValue instanceof String[]) { //如果是多值，放入数组
                rtValue.append("    indexArray[indexArray.length] = \"" + key + "\";\n");
                String[] myArray = (String[]) tempValue;
                if (key.equals("cmd")) {
                    tempValueNew = EwpStringHelper.replaceStringToScript(myArray[0]);
                    rtValue.append("    mForm[\"" + key + "\"] = \"" + tempValueNew + "\";\n");
                } else {
                    rtValue.append("    mForm[\"" + key + "\"] = [");
                    for (int i = 0; i < myArray.length; i++) {
                        if (i > 0)
                            rtValue.append(",");
                        tempValueNew = EwpStringHelper.replaceStringToScript(myArray[i]);
                        rtValue.append("\"" + tempValueNew + "\"");
                    }
                    rtValue.append("];\n");
                }
            } else if (tempValue instanceof Timestamp) { //如果是时间戳，直接注入
                tempValueNew = EwpStringHelper.replaceStringToScript(tempValue.toString().substring(0, 19));
                rtValue.append("    indexArray[indexArray.length] = \"" + key + "\";\n");
                rtValue.append("    mForm[\"" + key + "\"] = \"" + tempValueNew + "\";\n");
            } else if (tempValue instanceof BigDecimal) {
                tempValueNew = EwpStringHelper.replaceStringToScript(tempValue.toString());
                rtValue.append("    indexArray[indexArray.length] = \"" + key + "\";\n");
                rtValue.append("    mForm[\"" + key + "\"] = \"" + tempValueNew + "\";\n");
            } else if (tempValue instanceof Long) {
                tempValueNew = EwpStringHelper.replaceStringToScript(tempValue.toString());
                rtValue.append("    indexArray[indexArray.length] = \"" + key + "\";\n");
                rtValue.append("    mForm[\"" + key + "\"] = \"" + tempValueNew + "\";\n");
            }  else if (tempValue instanceof Integer) {
                tempValueNew = EwpStringHelper.replaceStringToScript(tempValue.toString());
                rtValue.append("    indexArray[indexArray.length] = \"" + key + "\";\n");
                rtValue.append("    mForm[\"" + key + "\"] = \"" + tempValueNew + "\";\n");
            } else {
                if (tempValue != null) {
                    EwpStringHelper.log(LocaleHolder.getMessage("udp.ewp.unknown_java_type") + tempValue);
                }
                continue;
            }
        }
        rtValue.append("    for(var i=0; i<indexArray.length; i++) {\n");
        rtValue.append("      writeBackValue(indexArray[i]);\n");
        rtValue.append("    }\n");
        rtValue.append("  }\n");
        rtValue.append(jsFunctionName + "();\n");
        return rtValue.toString();
    }

    /**
     * 从vo中获取表单值
     * 
     * @param request
     * @return
     */
    public static Map getMapFromVo(Object obj) {
        Map rtMap = new TreeMap();
        if (obj == null) {
            return rtMap;
        }
        BeanWrapper bw = new BeanWrapperImpl(obj);
        PropertyDescriptor pd[] = bw.getPropertyDescriptors();

        for (int i = 0; i < pd.length; i++) {
            try {
                if (!pd[i].getName().equals("class")) {
                    String tempKey = pd[i].getName();
                    rtMap.put(tempKey, bw.getPropertyValue(pd[i].getName()));
                }
            } catch (ClassCastException e) {
                //e.printStackTrace();
                continue;
            } catch (NotReadablePropertyException e) {
                //e.printStackTrace();
                continue;
            }
        }
        return rtMap;
    }

    /**
     * 功能: 判断2个vo的值是否相等
     *
     * @param vo1
     * @param vo2
     * @return
     */
    public static boolean voEquals(Object vo1, final Object vo2) {
        if (vo1 == vo2) {
            return true;
        }
        final Map mFinalValue = new HashMap();
        mFinalValue.put("tempIndex", "0");
        mFinalValue.put("tempEquals", "1");
        if (vo1 != null && vo2 != null) {
            if (!(vo2.getClass().equals(vo1.getClass()))) {
                return false;
            }
            accessVo(vo1, new IEwpTransctVoField() {
                public int transctVo(BeanWrapper bw, PropertyDescriptor pd) {
                    String currentKey = pd.getName();
                    if (!currentKey.equals("class")) {
                        boolean bEquals = (String.valueOf(mFinalValue.get("tempEquals"))).equals("1") ? true : false;
                        if (bEquals) { //只有true才进行比较
                            BeanWrapper bw2 = new BeanWrapperImpl(vo2);
                            if (bw.getPropertyValue(currentKey) == null) {
                                if (bw2.getPropertyValue(currentKey) != null) {
                                    bEquals = false;
                                }
                            } else {
                                if (!bw.getPropertyValue(currentKey).equals(bw2.getPropertyValue(currentKey))) {
                                    bEquals = false;
                                }
                            }
                            int index = Integer.parseInt(String.valueOf(mFinalValue.get("tempIndex")));
                            mFinalValue.put("tempIndex", String.valueOf(++index));
                            //RmStringHelper.log("      " + index + ": " + bEquals + " -->  vo1[" + currentKey + "]='" + bw.getPropertyValue(currentKey) + "',vo2[" + currentKey + "]='" + bw2.getPropertyValue(currentKey) + "'") ;
                            mFinalValue.put("tempEquals", bEquals ? "1" : "0");
                        }
                        return 1;
                    } else {
                        return 0;
                    }
                }
            });
        } else if (vo1 == null && vo2 == null) {
            return true;
        }
        boolean bEquals = (String.valueOf(mFinalValue.get("tempEquals"))).equals("1") ? true : false;
        return bEquals;
    }

    /**
     * 功能: 克隆自身
     *
     * @param vo1
     * @return
     */
    public static Object voClone(Object vo1) {
        Object vo2 = null;
        try {
            vo2 = vo1.getClass().newInstance();
            final BeanWrapper bw2 = new BeanWrapperImpl(vo2);

            accessVo(vo1, new IEwpTransctVoField() {
                public int transctVo(BeanWrapper bw, PropertyDescriptor pd) {
                    String currentKey = pd.getName();
                    if (!currentKey.equals("class")) {
                        bw2.setPropertyValue(currentKey, bw.getPropertyValue(currentKey));
                        return 1;
                    } else {
                        return 0;
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vo2;
    }

    public static int voHashCode(Object vo) {
        final Object[] hashCode = new Object[] { 0 + "" };
        try {
            accessVo(vo, new IEwpTransctVoField() {
                public int transctVo(BeanWrapper bw, PropertyDescriptor pd) {
                    String currentKey = pd.getName();
                    if (!currentKey.equals("class")) {
                        Object fieldValue = bw.getPropertyValue(currentKey);
                        if (fieldValue != null) {
                            int tempHashCode = Integer.parseInt(hashCode[0].toString());
                            tempHashCode += 29 * tempHashCode + fieldValue.hashCode();
                            hashCode[0] = tempHashCode + "";
                        }
                        return 1;
                    } else {
                        return 0;
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Integer.parseInt(hashCode[0].toString());
    }

    /**
     * 功能: 把vo中的值求出来
     *
     * @param vo
     * @return
     */
    public static String voToString(Object vo) {
        final StringBuffer sb = new StringBuffer();
        final Map mFinalValue = new HashMap();
        mFinalValue.put("tempIndex", "0");
        if (vo != null) {
            //sb.append(vo.getClass().getName() + ":" );
            accessVo(vo, new IEwpTransctVoField() {
                public int transctVo(BeanWrapper bw, PropertyDescriptor pd) {
                    if (!pd.getName().equals("class")) {
                        int index = Integer.parseInt(String.valueOf(mFinalValue.get("tempIndex")));
                        mFinalValue.put("tempIndex", String.valueOf(++index));
                        sb.append("\n" + index + ": " + pd.getName() + "='" + bw.getPropertyValue(pd.getName()) + "'");
                        return 1;
                    } else {
                        return 0;
                    }
                }
            });
        }
        return sb.toString();
    }

    /**
     * 把VO中值为null的数据一律替换成""
     *
     * @param obj
     *            输入一个VO
     * @return 被替换的值个数
     */
    public static int null2Nothing(Object obj) {
        return accessVo(obj, new IEwpTransctVoField() {
            public int transctVo(BeanWrapper bw, PropertyDescriptor pd) {
                if (!pd.getName().equals("class")) {
                    if (bw.getPropertyType(pd.getName()).getName().equals(
                            String.class.getName())
                            && ((String) (bw.getPropertyValue(pd.getName()))) == null) {
                        bw.setPropertyValue(pd.getName(), "");
                        return 1;
                    } else if (bw.getPropertyType(pd.getName()).getName()
                            .equals(String.class.getName())
                            && String
                            .valueOf(bw.getPropertyValue(pd.getName()))
                            .equals("null")) {
                        bw.setPropertyValue(pd.getName(), "");
                        return 1;
                    } else {
                        return 0;
                    }
                } else {
                    return 0;
                }
            }
        });
    }
}
