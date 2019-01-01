/*
 * 创建日期 2007-4-18
 * CreateBy zhangbaoyu
 */
package venus.cron.extend;

import venus.frames.base.action.IRequest;
import venus.frames.base.action.IResponse;
import venus.frames.mainframe.util.Helper;
import venus.frames.web.page.PageVo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangbaoyu
 *
 */
public class PageTool {
    
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
        } else {

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
    public static void transctPageVo(IRequest request, int modifyCount,
									 int recordCount) {
        PageVo pageVo = Helper.createPageVo(request, recordCount);
        Helper.reloadPageVoByOffset(request, modifyCount);
        Helper.savePageVo(pageVo, request);
        Helper.reloadOrderStr(request);
    }
    
	/**
	 * 列表数据的翻页显示处理
	 * @param argList
	 * @param a
	 * @param b
	 * @return
	 */
	public static List queryWorkListByPage(List argList, IRequest request,
										   IResponse response ) {
		// 翻页处理
		if (!transctPageVo(request)) { //翻页处理
			transctPageVo(request, 0, 
					argList==null||argList.size()==0?0:argList.size());
		}
		PageVo pageVo = Helper.findPageVo(request); //得到当前翻页信息
		//String orderStr = Helper.findOrderStr(request); //得到排序信息
		int no = pageVo.getCurrentPage();
		int size = pageVo.getPageSize();
		if(argList==null){
			return argList;
		}
		int a = no * size;
		int b = size;
		List resultList = new ArrayList();
		if (argList.size() <= a&&a>=0) {
			return argList;
		}
		for (int i = a; i < (a + b < argList.size() ? a + b : argList.size()); i++) {
			resultList.add(argList.get(i));
		}
		return resultList;
	}
}
