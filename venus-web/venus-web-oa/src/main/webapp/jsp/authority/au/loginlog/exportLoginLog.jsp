<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="venus.oa.util.DateTools" %>
<%@ page import="java.util.List, java.util.Iterator" %>
<%@ page import="venus.oa.util.StringHelperTools" %>
<%@ page import="venus.oa.login.loginlog.vo.LoginLogVo" %>
<%@ page import="venus.oa.login.loginlog.util.ILoginLogConstants" %>
<%@ page import="jxl.Workbook" %>
<%@ page import="jxl.write.WritableWorkbook" %>
<%@ page import="jxl.write.WritableSheet" %>
<%@ page import="jxl.format.Alignment" %>
<%@ page import="jxl.write.Label" %>
<%@ page import="jxl.write.WritableCellFormat" %>
<%@ page import="venus.frames.web.page.PageVo" %>
<%
	List lResult = null;  //定义结果列表的List变量
	if(session.getAttribute(ILoginLogConstants.REQUEST_BEANS) != null) {  //如果session中有传递来打印页面的List
		lResult = (List)session.getAttribute(ILoginLogConstants.REQUEST_BEANS);  //赋值给resultList
	}
	session.removeAttribute(ILoginLogConstants.REQUEST_BEANS);  //清空session中的List对象
	LoginLogVo resultVo = null;  //定义一个临时的vo变量
	try {
		response.reset();
		response.setContentType("application/msexcel");
	    response.setHeader("Content-disposition", "attachment; filename=" + DateTools.getJoinedSysDateTime() + "export.xls");
        WritableWorkbook wbook = Workbook.createWorkbook(response.getOutputStream());  //建立excel文件
        WritableSheet wsheet = wbook.createSheet(venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.First_Page"), 0); //sheet名称
        WritableCellFormat cellFormatNumber = new WritableCellFormat();
        cellFormatNumber.setAlignment(Alignment.RIGHT);
        
		int rowIndex = 0;
		int columnIndex = 0;
		wsheet.addCell(new Label(columnIndex++, rowIndex, venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Sequence")));
		
		wsheet.addCell(new Label(columnIndex++, rowIndex, venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Primary_key")));
		
		wsheet.addCell(new Label(columnIndex++, rowIndex, venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Login_account")));
		
		wsheet.addCell(new Label(columnIndex++, rowIndex, venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.User_Name")));
		
		wsheet.addCell(new Label(columnIndex++, rowIndex, "IP"+venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Address")));
		
		wsheet.addCell(new Label(columnIndex++, rowIndex, "IE"+venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Version")));
		
		wsheet.addCell(new Label(columnIndex++, rowIndex, venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Operating_system")));
		
		wsheet.addCell(new Label(columnIndex++, rowIndex, venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Host_name")));
		
		//wsheet.addCell(new Label(columnIndex++, rowIndex, "退出类型"));
		
		wsheet.addCell(new Label(columnIndex++, rowIndex, venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Login_time")));
		
		wsheet.addCell(new Label(columnIndex++, rowIndex, venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Exit_time")));
		
		PageVo pageVo = new PageVo();
		if(pageContext.getRequest().getAttribute("VENUS_PAGEVO_KEY") != null) {
			pageVo = (PageVo)pageContext.getRequest().getAttribute("VENUS_PAGEVO_KEY");
		}
		int startIndex = (pageVo.getCurrentPage() - 1) * pageVo.getPageSize() + 1;

        //开始行循环
        for(Iterator itLResult = lResult.iterator(); itLResult.hasNext(); ) {  //循环列
            rowIndex ++;
            columnIndex = 0;
			resultVo = (LoginLogVo) itLResult.next();
            wsheet.addCell(new Label(columnIndex ++ , rowIndex, (startIndex ++) + "" ));
			
			wsheet.addCell(new Label(columnIndex ++ , rowIndex, StringHelperTools.prt(resultVo.getId())));
			
			wsheet.addCell(new Label(columnIndex ++ , rowIndex, StringHelperTools.prt(resultVo.getLogin_id())));
			
			wsheet.addCell(new Label(columnIndex ++ , rowIndex, StringHelperTools.prt(resultVo.getName())));
			
			wsheet.addCell(new Label(columnIndex ++ , rowIndex, StringHelperTools.prt(resultVo.getLogin_ip())));
			
			wsheet.addCell(new Label(columnIndex ++ , rowIndex, StringHelperTools.prt(resultVo.getIe())));
			
			wsheet.addCell(new Label(columnIndex ++ , rowIndex, StringHelperTools.prt(resultVo.getOs())));
			
			wsheet.addCell(new Label(columnIndex ++ , rowIndex, StringHelperTools.prt(resultVo.getHost())));
			
			//wsheet.addCell(new Label(columnIndex ++ , rowIndex, StringHelperTools.prt(resultVo.getLogout_type())));
			
			wsheet.addCell(new Label(columnIndex ++ , rowIndex, StringHelperTools.prt(resultVo.getLogin_time(), 19)));
			
			wsheet.addCell(new Label(columnIndex ++ , rowIndex, StringHelperTools.prt(resultVo.getLogout_time(), 19)));
        }

        wbook.write();
        if(wbook != null) {
            wbook.close();
        }
        if (out != null) {
            out.clear();
            out = pageContext.pushBody();
        }
	} catch(Exception e) {
		e.printStackTrace();
	}
%>

