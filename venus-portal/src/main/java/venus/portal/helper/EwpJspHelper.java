package venus.portal.helper;

import venus.frames.base.action.IRequest;
import venus.frames.mainframe.currentlogin.ProfileException;
import venus.frames.mainframe.util.Helper;
import venus.frames.web.page.PageVo;
import venus.oa.adapter.IAuthorization;

import javax.servlet.http.HttpServletRequest;


/**
 *@author zhangrenyang 
 *@date  2011-9-28
 */
public class EwpJspHelper {
    /**
     * 重载并保存PageVo
     *
     * @param request
     */
    public static boolean transctPageVo(IRequest request) {
        PageVo pageVo = Helper.findPageVo(request);
        
        if (pageVo != null) {
            Helper.updatePageVo(pageVo, request);
            Helper.savePageVo(pageVo, request);
            Helper.reloadOrderStr(request);
            return true;
        }else{
            
            return false;
        }
    }
    
    /**
     * 用偏移值调整PageVo，并保存
     *
     * @param request
     * @param modifyCount
     */
    public static void transctPageVo(IRequest request, int modifyCount) {
        PageVo pageVo = Helper.reloadPageVoByOffset(request, 1);
        Helper.savePageVo(pageVo, request);
        Helper.reloadOrderStr(request);
    }

    /**
     * 根据总记录数重新计算PageVo，并用偏移值调整PageVo
     *
     * @param request
     * @param modifyCount
     * @param recordCount
     */
    public static void transctPageVo(IRequest request, int modifyCount, int recordCount) {
        PageVo pageVo = Helper.createPageVo(request, recordCount);
        Helper.reloadPageVoByOffset(request, modifyCount);
        Helper.savePageVo(pageVo, request);
        Helper.reloadOrderStr(request);
    }
    
    /**
     * 功能: 从request中获取party_id
     *
     * @param request
     * @return
     * @throws ProfileException
     */
    public static String getParty_idFromRequest(IRequest request) {
        return getParty_idFromRequest((HttpServletRequest)request);
    }
    
    /**
     * 功能: 从request中获取party_id
     *
     * @param request
     * @return
     * @throws ProfileException
     */
    public static String getParty_idFromRequest(HttpServletRequest request) {
        IAuthorization authorization = (IAuthorization) Helper.getBean("authorityAdapter");
        return authorization.fetchUserId(request);
    }
    
    /**
     * 从 request 获取可能为多个值的数组表示, ""不是有效单个值
     *
     * @param request
     * @param inputName
     * @return
     */
    public static String[] getArrayFromRequest(IRequest request, String inputName) {
        String[] returnStrArray = null;
        String tempStr = request.getParameter(inputName);
        if(tempStr != null && tempStr.length() > 0) {
            returnStrArray = tempStr.split(",");
        }
        if(returnStrArray == null) {
            returnStrArray = new String[0];
        }
        return returnStrArray;
    }
    
    
}
