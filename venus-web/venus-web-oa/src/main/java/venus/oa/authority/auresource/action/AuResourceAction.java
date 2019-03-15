package venus.oa.authority.auresource.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import venus.frames.base.action.IRequest;
import venus.frames.mainframe.action.HttpRequest;
import venus.frames.mainframe.util.Helper;
import venus.frames.web.page.PageVo;
import venus.oa.authority.auresource.bs.IAuResourceBs;
import venus.oa.authority.auresource.util.IAuResourceConstants;
import venus.oa.authority.auresource.vo.AuResourceVo;
import venus.oa.helper.AuHelper;
import venus.oa.util.DateTools;
import venus.oa.util.ProjTools;
import venus.oa.util.VoHelperTools;
import venus.oa.util.common.bs.ICommonBs;
import venus.pub.lang.OID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * resource controller
 */
@Controller
@RequestMapping("/auResource")
public class AuResourceAction implements IAuResourceConstants {

    @Autowired
    private IAuResourceBs auResourceBs;

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
        AuResourceVo vo = new AuResourceVo();
        if (!Helper.populate(vo, request)) {
//            return MessageAgent.sendErrorMessage(request, DEFAULT_MSG_ERROR_STR, MessageStyle.ALERT_AND_BACK);
            return MESSAGE_AGENT_ERROR;
        }
        vo.setCreate_date(DateTools.getSysTimestamp()); //打创建时间戳
        auResourceBs.insert(vo); //插入单条记录
        if ("3".equals(vo.getResource_type())) {
            return "redirect:/auResource/queryAllfield";
        }
        return "redirect:/auResource/queryAll";
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
        auResourceBs.delete(request.getParameter(REQUEST_ID_FLAG)); //删除单条记录
        //RmJspHelper.transctPageVo(request,-deleteCount); //翻页偏移
//        return request.findForward(FORWARD_TO_QUERY_ALL_KEY);
        return "redirect:/auResource/queryAll";
    }


    /**
     * 从页面的表单获取多条记录id，并删除多条记录
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/deleteMulti")
    public String deleteMulti(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String[] id = request.getParameterValues(REQUEST_MULTI_ID_FLAG); //从request获取多条记录id
        //int deleteCount = 0; //定义成功删除的记录数
        if (id != null && id.length != 0) {
            //deleteCount = 
            auResourceBs.delete(id); //删除多条记录
        }
        //RmJspHelper.transctPageVo(request, -deleteCount); //翻页偏移
//        return request.findForward(FORWARD_TO_QUERY_ALL_KEY);
        return "redirect:/auResource/queryAll";
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
        AuResourceVo bean = auResourceBs.find(request.getParameter(REQUEST_ID_FLAG)); //通过id获取vo
        request.setAttribute(REQUEST_BEAN_VALUE, bean); //把vo放入request
        //RmJspHelper.transctPageVo(request); //翻页重载
//        return request.findForward(FORWARD_UPDATE_KEY);
        return FORWARD_UPDATE_KEY;
    }


    /**
     * 测试记录权限的SQL是否能够通过
     * 
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/testSQL")
    public String testSQL(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String tableName = request.getParameter("tableName");
        String fieldName = request.getParameter("fieldName");
        String fieldType = request.getParameter("fieldType");
        String fieldValue = request.getParameter("fieldValue");
        String sqlStr = "select " + fieldName + " from " + tableName + " where 1=2 ";
        ICommonBs MetaDataBs = ProjTools.getCommonBsInstance();
        RowMapper rowMapper = new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                return null;
            }
        };
        boolean isRight = true;
        AuResourceVo auResourceVoForSQL = new AuResourceVo();
        auResourceVoForSQL.setTable_name(tableName);
        auResourceVoForSQL.setField_name(fieldName);
        auResourceVoForSQL.setFilter_type(fieldType);
        auResourceVoForSQL.setValue(fieldValue);
        String strResult= AuHelper.sqlAssembly(auResourceVoForSQL, sqlStr, tableName, tableName);
        try {
            MetaDataBs.doQueryForObject(strResult,rowMapper);
        } catch (Exception e) {
            isRight = false;
            e.printStackTrace();
        }
        request.setAttribute(REQUEST_BEAN_VALUE, String.valueOf(isRight));
//        return request.findForward(FORWARD_TEST_KEY);
        return FORWARD_TEST_KEY;
    }


    /**
     * find default access
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/findDefaultAccess")
    public String findDefaultAccess(HttpServletRequest request, HttpServletResponse response) throws Exception {
        AuResourceVo resVo = null;
        String old_resource_id = request.getParameter("old_resource_id");
        String call_code = request.getParameter("call_code");

        //查询AuResource表
        List lResource = auResourceBs.queryByCondition(
                "CALL_CODE='" + call_code + "' and OLD_RESOURCE_ID='" + old_resource_id + "'");
        if (lResource != null && lResource.size() > 0) {
            resVo = (AuResourceVo) lResource.get(0);
        }

        request.setAttribute(REQUEST_BEAN_VALUE, resVo); //把vo放入request
        request.setAttribute(REQUEST_WRITE_BACK_FORM_VALUES, VoHelperTools.getMapFromRequest((HttpServletRequest) request)); //回写表单
//        return request.findForward(FORWARD_FINDDEFAULTACCESS_KEY);
        // no usage found
        return FORWARD_FINDDEFAULTACCESS_KEY;
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
        AuResourceVo vo = new AuResourceVo();
        if (!Helper.populate(vo, request)) { //从request中注值进去vo
//            return MessageAgent.sendErrorMessage(request, DEFAULT_MSG_ERROR_STR, MessageStyle.ALERT_AND_BACK);
            return MESSAGE_AGENT_ERROR;
        }
        vo.setModify_date(DateTools.getSysTimestamp()); //打修改时间戳
        //int count = 
        auResourceBs.update(vo); //更新单条记录
        if ("3".equals(vo.getResource_type())) {
//            return request.findForward("queryAllfield");
            return "redirect:/auResource/queryAllfield";
        }
//        return request.findForward(FORWARD_TO_QUERY_ALL_KEY);
        return "redirect:/auResource/queryAll";
    }

    /**
     * 更新默认访问方式
     * 
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/updateDefaultAccess")
    public String updateDefaultAccess(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String resource_id = request.getParameter(REQUEST_ID_FLAG);
        String default_access = request.getParameter("default_access");
        if (resource_id == null || resource_id.equals("")) {
            //String old_resource_id = request.getParameter("old_resource_id");
            String old_resource_name = request.getParameter("old_resource_name");
            String call_code = request.getParameter("call_code");
            AuResourceVo vo = new AuResourceVo();
            vo.setResource_type(call_code);
            vo.setIs_public(default_access);
            vo.setAccess_type("1");
            vo.setName(old_resource_name);
            vo.setCreate_date(DateTools.getSysTimestamp());
            OID oid = auResourceBs.insert(vo);//插入单条记录
            resource_id = String.valueOf(oid.longValue());
        } else {
            AuResourceVo vo = auResourceBs.find(resource_id); //通过id获取vo
            vo.setIs_public(default_access);
            vo.setModify_date(DateTools.getSysTimestamp()); //打修改时间戳
            auResourceBs.update(vo); //更新单条记录
        }
        AuResourceVo bean = auResourceBs.find(resource_id);
        request.setAttribute(REQUEST_BEAN_VALUE, bean); //把vo放入request
        request.setAttribute(REQUEST_WRITE_BACK_FORM_VALUES, VoHelperTools.getMapFromRequest(request));
//        return request.findForward(FORWARD_FINDDEFAULTACCESS_KEY);
        // no usage found
        return FORWARD_FINDDEFAULTACCESS_KEY;
    }


    /**
     * 查询全部记录，分页显示，支持页面上触发的后台排序
     *
     * @param _request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/queryAll")
    public String queryAll(HttpServletRequest _request, HttpServletResponse response) throws Exception {
        IRequest request = (IRequest)new HttpRequest(_request);
        IAuResourceBs bs = auResourceBs;
        String condition = "RESOURCE_TYPE='4'";
        PageVo pageVo = Helper.findPageVo(request); //得到当前翻页信息
        if (pageVo != null) {
            pageVo = Helper.updatePageVo(pageVo, request);
        } else {
            int recordCount = bs.getRecordCount(condition);
            pageVo = Helper.createPageVo(request, recordCount);
        }
        String orderStr = Helper.findOrderStr(request); //得到排序信息
        List beans = null; //定义结果集
        if (orderStr != null) {
            beans = bs.queryByCondition(pageVo.getCurrentPage(), pageVo.getPageSize(), condition, orderStr); //查询全部,带排序
            Helper.saveOrderStr(orderStr, request); //保存排序信息
        } else {
            beans = bs.queryByCondition(pageVo.getCurrentPage(), pageVo.getPageSize(), condition); //查询全部
        }
        request.setAttribute(REQUEST_BEANS_VALUE, beans); //把结果集放入request
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
        AuResourceVo bean = auResourceBs.find(request.getParameter(REQUEST_ID_FLAG)); //通过id获取vo
        request.setAttribute(REQUEST_BEAN_VALUE, bean); //把vo放入request
//        return request.findForward(FORWARD_DETAIL_KEY);
        // no forward found
        return FORWARD_DETAIL_KEY;
    }


    /**
     * 简单查询，分页显示，支持表单回写
     * 
     * @param _request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/simpleQuery")
    public String simpleQuery(HttpServletRequest _request, HttpServletResponse response) throws Exception {
        IAuResourceBs bs = auResourceBs;
        IRequest request = (IRequest)new HttpRequest(_request);
        String queryCondition = "resource_type='4'";//request.getParameter(REQUEST_QUERY_CONDITION_VALUE);
        //从request获得查询条件
        String name = request.getParameter("name");
        if (name != null && !"".equals(name)) {
            queryCondition = queryCondition + " and name like '%" + name + "%'";
        }
        PageVo pageVo = Helper.findPageVo(request); //得到当前翻页信息
        if (pageVo != null) {
            pageVo = Helper.updatePageVo(pageVo, request);
        } else {
            int recordCount = bs.getRecordCount(queryCondition);
            pageVo = Helper.createPageVo(request, recordCount);
        }
        List beans = bs.queryByCondition(pageVo.getCurrentPage(), pageVo.getPageSize(), queryCondition); //按条件查询
        request.setAttribute(REQUEST_BEANS_VALUE, beans); //把结果集放入request
        request.setAttribute(REQUEST_WRITE_BACK_FORM_VALUES, VoHelperTools.getMapFromRequest((HttpServletRequest) request)); //回写表单
//        return request.findForward(FORWARD_LIST_PAGE_KEY);
        return FORWARD_LIST_PAGE_KEY;
    }


    /**
     * 参照信息查询，带简单查询，分页显示，支持表单回写
     * 
     *
     * @param _request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/queryReference")
    public String queryReference(HttpServletRequest _request, HttpServletResponse response) throws Exception {
        IRequest request = (IRequest)new HttpRequest(_request);
        IAuResourceBs bs = auResourceBs;
        String queryCondition = request.getParameter(REQUEST_QUERY_CONDITION_VALUE); //从request获得查询条件
        PageVo pageVo = Helper.findPageVo(request); //得到当前翻页信息
        if (pageVo != null) {
            pageVo = Helper.updatePageVo(pageVo, request);
        } else {
            int recordCount = bs.getRecordCount(queryCondition);
            pageVo = Helper.createPageVo(request, recordCount);
        }
        List beans = bs.queryByCondition(pageVo.getCurrentPage(), pageVo.getPageSize(), queryCondition); //按条件查询
        request.setAttribute(REQUEST_BEANS_VALUE, beans); //把结果集放入request
        request.setAttribute(REQUEST_WRITE_BACK_FORM_VALUES, VoHelperTools.getMapFromRequest(request)); //回写表单
        request.setAttribute("inputType", request.getParameter("inputType")); //传送输入方式,checkbox或radio
//        return request.findForward(FORWARD_REFERENCE_KEY);
        // no forward found
        return FORWARD_REFERENCE_KEY;
    }


    /**
     * 查询字段资源全部记录
     *
     * @param _request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/queryAllfield")
    public String queryAllfield(HttpServletRequest _request, HttpServletResponse response) throws Exception {
        IAuResourceBs bs = auResourceBs;
        IRequest request = (IRequest)new HttpRequest(_request);
        String name = request.getParameter("name");
        String condition = " resource_type = '3'";
        if (name != null && !"".equals(name)) {
            condition = condition + " and name like '%" + name + "%'";
        }
        PageVo pageVo = Helper.findPageVo(request);
        if (pageVo != null) {
            pageVo = Helper.updatePageVo(pageVo, request);
        } else {
            int recordCount = bs.getRecordCount(condition);
            pageVo = Helper.createPageVo(request, recordCount);
        }
        String orderStr = Helper.findOrderStr(request);

        List list = new ArrayList(); //定义结果集
        list = bs.queryByCondition(pageVo.getCurrentPage(), pageVo.getPageSize(), condition, orderStr);
        Helper.saveOrderStr(orderStr, request);
        Helper.savePageVo(pageVo, request);
        request.setAttribute(REQUEST_BEANS_VALUE, list);
        request.setAttribute(REQUEST_WRITE_BACK_FORM_VALUES, VoHelperTools.getMapFromRequest((HttpServletRequest) request)); //回写表单
//        return request.findForward("fieldlist");
        return FORWARD_FIELD_LIST_KEY;
    }


    /**
     * 从页面表单获取信息注入vo，并插入单条记录
     * 
     *
     * @param _request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/insertfiled")
    public String insertfiled(HttpServletRequest _request, HttpServletResponse response) throws Exception {
        AuResourceVo vo = new AuResourceVo();
        IRequest request = (IRequest)new HttpRequest(_request);
        if (!Helper.populate(vo, request)) { //从request中注值进去vo
//            return MessageAgent.sendErrorMessage(request, DEFAULT_MSG_ERROR_STR, MessageStyle.ALERT_AND_BACK);
            return MESSAGE_AGENT_ERROR;
        }
        vo.setResource_type("3");
        vo.setEnable_status("1");
        vo.setAccess_type("1");
        vo.setParty_type("1");
        vo.setEnable_date(DateTools.getSysTimestamp());
        vo.setCreate_date(DateTools.getSysTimestamp()); //打创建时间戳
        vo.setModify_date(DateTools.getSysTimestamp()); //打修改时间戳
        auResourceBs.insert(vo); //插入单条记录
//        return request.findForward("queryAllfield");
        return "redirect:/auResource/queryAllfield";
    }


    /**
     * 从页面表单获取信息注入vo，并修改单条记录
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/updatefiled")
    public String updatefiled(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int index = 0;
        String checkme[] = request.getParameterValues("checkme");
        String table_name = request.getParameter("table_name");
        String table_chinesename = request.getParameter("table_chinesename");
        String resource_type = request.getParameter("resource_type");
        String Id[] = request.getParameterValues("Id");
        if (Id != null) {
            index = Id.length;
            String name[] = request.getParameterValues("name");
            String field_chinesename[] = request.getParameterValues("field_chinesename");
            String field_name[] = request.getParameterValues("field_name");
            String help[] = request.getParameterValues("help");
            String value[] = request.getParameterValues("value");
            String filter_type[] = request.getParameterValues("filter_type");
            if (checkme != null) {
                List al = Arrays.asList(checkme);
                for (int i = 0; i < index; i++) {
                    if (al.contains(String.valueOf(i))) {//如果复选框被选择
                        AuResourceVo bean = new AuResourceVo();
                        bean.setName(name[i]);
                        bean.setResource_type(resource_type);
                        bean.setField_name(field_name[i]);
                        bean.setTable_name(table_name);
                        bean.setField_chinesename(field_chinesename[i]);
                        bean.setTable_chinesename(table_chinesename);
                        if (value != null) {
                            bean.setValue(value[i]);
                        }
                        if (filter_type != null) {
                            bean.setFilter_type(filter_type[i]);
                        }
                        bean.setHelp(help[i]);
                        bean.setIs_public(request.getParameter("is_public" + i));
                        bean.setAccess_type("1");
                        bean.setEnable_status("1");
                        bean.setCreate_date(DateTools.getSysTimestamp()); //打创建时间戳
                        //OID count = 
                        auResourceBs.insert(bean); //插入多条记录
                    }
                }
            }
        }
//        return request.findForward(request.getParameter("forward"));
        String forward = request.getParameter("forward");
        if (forward!=null && !"".equals(forward)){
            if ("queryAllfield".equals(forward)) {
                return "redirect:/auResource/queryAllfield";
            }else if ("queryAll".equals(forward)){
                return "redirect:/auResource/queryAll";
            }
        }
        return "redirect:/auResource/queryAll";
    }


    /**
     * 从页面表单获取信息注入vo，并修改单条记录
     *
     * @param _request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/updateafiled")
    public String updateafiled(HttpServletRequest _request, HttpServletResponse response) throws Exception {
        AuResourceVo vo = new AuResourceVo();
        IRequest request = (IRequest)new HttpRequest(_request);
        if (!Helper.populate(vo, request)) { //从request中注值进去vo
//            return MessageAgent.sendErrorMessage(request, DEFAULT_MSG_ERROR_STR, MessageStyle.ALERT_AND_BACK);
            return MESSAGE_AGENT_ERROR;
        }
        AuResourceVo bean = auResourceBs.find(vo.getId()); //通过id获取vo
        bean.setName(vo.getName());
        bean.setField_name(vo.getField_name());
        bean.setTable_name(vo.getTable_name());
        bean.setField_chinesename(vo.getField_chinesename());
        bean.setTable_chinesename(vo.getTable_chinesename());
        bean.setHelp(vo.getHelp());
        bean.setIs_public(vo.getIs_public());
        bean.setModify_date(DateTools.getSysTimestamp()); //打修改时间戳
        auResourceBs.update(bean); //更新单条记录
//        return request.findForward("queryAllfield");
        return "redirect:/auResource/queryAllfield";
    }


    /**
     * 启用
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/enablestatus")
    public String enablestatus(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String pageFlag = request.getParameter("pageFlag");
        AuResourceVo bean = auResourceBs.find(request.getParameter(REQUEST_ID_FLAG)); //通过id获取vo
        bean.setEnable_status("1");
        bean.setEnable_date(DateTools.getSysTimestamp());
        bean.setModify_date(DateTools.getSysTimestamp()); //打修改时间戳
        //int count = 
        auResourceBs.update(bean); //更新单条记录
        if ("4".equals(pageFlag)) {
//            return request.findForward(FORWARD_TO_QUERY_ALL_KEY);
            return "redirect:/auResource/queryAll";
        }
//        return request.findForward("queryAllfield");
        return "redirect:/auResource/queryAllfield";
    }

    /**
     * 禁用
     * 
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/disablestatus")
    public String disablestatus(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String pageFlag = request.getParameter("pageFlag");
        AuResourceVo bean = auResourceBs.find(request.getParameter(REQUEST_ID_FLAG)); //通过id获取vo
        bean.setEnable_status("0");
        bean.setEnable_date(DateTools.getSysTimestamp());
        bean.setModify_date(DateTools.getSysTimestamp()); //打修改时间戳
        auResourceBs.disable(bean); //更新单条记录
        if ("4".equals(pageFlag)) {
//            return request.findForward(FORWARD_TO_QUERY_ALL_KEY);
            return "redirect:/auResource/queryAll";
        }
//        return request.findForward("queryAllfield");
        return "redirect:/auResource/queryAllfield";
    }


    /**
     * 从页面的表单获取单条记录id，查出这条记录的值，并跳转到修改页面
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/initupdatefield")
    public String initupdatefield(HttpServletRequest request, HttpServletResponse response) throws Exception {
        AuResourceVo bean = auResourceBs.find(request.getParameter(REQUEST_ID_FLAG)); //通过id获取vo
        request.setAttribute(REQUEST_BEAN_VALUE, bean); //把vo放入request
        request.setAttribute(REQUEST_WRITE_BACK_FORM_VALUES, VoHelperTools.getMapFromVo(bean)); //回写表单
//        return request.findForward("initfieldupdate");
        return FORWARD_INIT_FIELD_UPDATE;
    }


    /**
     * 查询全部表名
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/initinsert")
    public String initinsert(HttpServletRequest request, HttpServletResponse response) throws Exception {
        IAuResourceBs bs = auResourceBs;

        String resource_type = request.getParameter("resource_type");
        String condition = null;
        String condfield = null;
        if (resource_type != null && !"".equals(resource_type)) {
            condition = " where t.resource_type ='" + resource_type + "'";
            condfield = " and resource_type ='" + resource_type + "'";
        }

        List list = new ArrayList(); //定义结果集
        list = bs.queryAllTableName(condition);
        String name = request.getParameter("name");
        String conditionfield = "";
        if (name != null && !"".equals(name)) {
            conditionfield = name ;
        } else if (list.size() > 0) {
        	conditionfield = ((AuResourceVo) list.get(0)).getTable_name();
        }
        List listfield = new ArrayList(); //定义结果集
        listfield = bs.queryTableField(conditionfield, condfield);
        request.setAttribute(REQUEST_NAME_VALUE, name);
        request.setAttribute(REQUEST_BEAN_FIELD_VALUE, listfield);
        request.setAttribute(REQUEST_BEANS_VALUE, list);
        request.setAttribute(REQUEST_WRITE_BACK_FORM_VALUES, VoHelperTools.getMapFromRequest(request)); //回写表单
//        return request.findForward(request.getParameter("forward"));
        String forward = request.getParameter("forward");
        if (forward!=null && !"".equals(forward)){
            if (REQUEST_QUERY_ALL_ATBLE.equals(forward)){
                return FORWARD_FIELD_INSERT_KEY;
            }else if (REQUEST_QUERY_ALL_RECORD.equals(forward)){
                return FORWARD_RECORD_INSERT_KEY;
            }
        }
        return FORWARD_LIST_PAGE_KEY;
    }


    /**
     * 查询字段资源全部记录
     * 
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/queryTablefield")
    public String queryTablefield(HttpServletRequest request, HttpServletResponse response) throws Exception {
        IAuResourceBs bs = auResourceBs;
        String name = request.getParameter("name");
        String conditionfield = "";//"and b.resource_type = '3'";
        List listfield = new ArrayList(); //定义结果集
        if (name != null && !"".equals(name)) {
            conditionfield = name ;
            listfield = bs.queryTableField(conditionfield,null);
        }
        request.setAttribute(REQUEST_BEANS_VALUE, listfield);
        request.setAttribute(REQUEST_WRITE_BACK_FORM_VALUES, VoHelperTools.getMapFromRequest((HttpServletRequest) request)); //回写表单
//        return request.findForward("tablefieldlist");
        return FORWARD_TABLE_FIELD_LIST;
    }
}

