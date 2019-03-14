package venus.oa.authority.aufunctree.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import venus.frames.base.action.IRequest;
import venus.frames.mainframe.action.HttpRequest;
import venus.frames.mainframe.util.Helper;
import venus.oa.authority.aufunctree.bs.IAuFunctreeBs;
import venus.oa.authority.aufunctree.util.IAuFunctreeConstants;
import venus.oa.authority.aufunctree.vo.AuFunctreeVo;
import venus.oa.util.DateTools;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/auFunctree")
public class AuFunctreeAction implements IAuFunctreeConstants {

    @Autowired
    private IAuFunctreeBs auFunctreeBs;

    /**
     * 从页面表单获取信息注入vo，并插入单条记录
     * 
     * @param _request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/insert")
    public String insert(HttpServletRequest _request, HttpServletResponse response) throws Exception {
        IRequest request = (IRequest)new HttpRequest(_request);
        AuFunctreeVo vo = new AuFunctreeVo();
        if (!Helper.populate(vo, request)) {  //从request中注值进去vo
//            return MessageAgent.sendErrorMessage(request, DEFAULT_MSG_ERROR_STR, MessageStyle.ALERT_AND_BACK);
            return MESSAGE_AGENT_ERROR;
        }
        vo.setUrl(vo.getUrl().trim());
        IAuFunctreeBs bs = auFunctreeBs;
        
//      第一步：判断同一父节点同一级别内的名称是否重复
        String tName = vo.getName();
        String pCode = vo.getParent_code();
        //int pLen = vo.getParent_code().length();
        List myList = bs.queryByCondition("NAME='"+tName+"' AND TOTAL_CODE LIKE'"+pCode+"___'");
        if(myList!=null && myList.size()>0) {
//           return MessageAgent.sendErrorMessage(request, venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Name_repetition_try_other_names"), MessageStyle.ALERT_AND_BACK);
           return MESSAGE_AGENT_ERROR;
        }
        //第二步：插入记录
        //在BS里：判断父节点是否为叶子节点，如果是则将它改为非叶子节点
        //在DAO里：生成主键ID和排序编号SEQ_ID
        vo.setCreate_date(DateTools.getSysTimestamp());  //打创建时间
        bs.insert(vo);  //插入单条记录
//        return request.findForward(FORWARD_LIST_PAGE_KEY);
        return FORWARD_LIST_PAGE_KEY;
    }

    /**
     * 从页面的表单获取单条记录id，并删除单条记录
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/delete")
    public String delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id = request.getParameter(REQUEST_ID_FLAG);
        IAuFunctreeBs bs = auFunctreeBs;
        //判断删除的节点是否叶子节点，如果不是则不允许删除
		AuFunctreeVo vo = bs.find(id);
		if("1".equals(vo.getIs_leaf())) {
		    bs.delete(id);  //删除单条记录
        }else {
//            return MessageAgent.sendErrorMessage(request, venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.You_remove_the_node_contains_child_nodes_please_remove_one_level"), MessageStyle.ALERT_AND_BACK);
            return MESSAGE_AGENT_ERROR;
        }
//        return request.findForward(FORWARD_LIST_PAGE_KEY);
        return FORWARD_LIST_PAGE_KEY;
    }

    /**
     * 从页面的表单获取单条记录id，查出这条记录的值，并跳转到修改页面
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/find")
    public String find(HttpServletRequest request, HttpServletResponse response) throws Exception {
        AuFunctreeVo bean = auFunctreeBs.find(request.getParameter(REQUEST_ID_FLAG));  //通过id获取vo
        request.setAttribute(REQUEST_BEAN_VALUE, bean);  //把vo放入request
        //RmJspHelper.transctPageVo(request);  //翻页重载
//        return request.findForward(FORWARD_UPDATE_KEY);
        return FORWARD_UPDATE_KEY;
    }

    /**
     * 从页面表单获取信息注入vo，并修改单条记录
     * 
     * @param _request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/update")
    public String update(HttpServletRequest _request, HttpServletResponse response) throws Exception {
        IRequest request = (IRequest)new HttpRequest(_request);
        AuFunctreeVo vo = new AuFunctreeVo();
        if (!Helper.populate(vo, request)) {  //从request中注值进去vo
//            return MessageAgent.sendErrorMessage(request, DEFAULT_MSG_ERROR_STR, MessageStyle.ALERT_AND_BACK);
            return MESSAGE_AGENT_ERROR;
        }
        vo.setUrl(vo.getUrl().trim());
        vo.setModify_date(DateTools.getSysTimestamp());  //打修改时间,IP戳
        auFunctreeBs.update(vo);  //更新单条记录
//        return request.findForward(FORWARD_LIST_PAGE_KEY);
        return FORWARD_LIST_PAGE_KEY;
    }

    /**
     * 从页面的表单获取单条记录id，并查看这条记录的详细信息
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/detail")
    public String detail(HttpServletRequest request, HttpServletResponse response) throws Exception {
        AuFunctreeVo bean = auFunctreeBs.find(request.getParameter(REQUEST_ID_FLAG));  //通过id获取vo
        request.setAttribute(REQUEST_BEAN_VALUE, bean);  //把vo放入request
        //RmJspHelper.transctPageVo(request);  //翻页重载
//        return request.findForward(FORWARD_DETAIL_KEY);
        return FORWARD_DETAIL_KEY;
    }
    
    /**
     * 
     * 功能: 排序
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/sort")
    public String sort(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String parent_code = request.getParameter("parent_code");
        String[] newIds = request.getParameterValues("sortList");
        IAuFunctreeBs bs = auFunctreeBs;
        
    	List lOld = bs.queryByCondition("parent_code='"+parent_code+"'","order_code");//旧的列表
    	List lChange = new ArrayList();
    	for(int i=0; i<lOld.size(); i++) {
    	    AuFunctreeVo vo = (AuFunctreeVo) lOld.get(i);
    		if ( !newIds[i].equals(vo.getId()) ) { //如果新旧id不等，则记录下来进行更新
    		    String[] param = new String[2];
    		    param[0] = vo.getOrder_code();
    		    param[1] = newIds[i];
				lChange.add(param);
			}
    	} 
    	if ( lChange.size() > 0 ) {
    	    bs.update(lChange);//更新多条记录
		}
//        return request.findForward(FORWARD_LIST_PAGE_KEY);
        return FORWARD_LIST_PAGE_KEY;
    }
}

