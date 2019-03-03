<%@page language="java" contentType="application/x-msdownload" pageEncoding="UTF-8"%>
<%@ page import = "java.io.OutputStream"%>
<%@ page import = "java.net.URLEncoder"%>
<%@ page import  = "java.io.ByteArrayInputStream"%>
<%@ page import = "java.net.InetAddress"%>
<%
      //关于文件下载时采用文件流输出的方式处理：
      String filedisplay = "";
      OutputStream outp = null;
      ByteArrayInputStream in = null;
      StringBuffer str = new StringBuffer();
      if ("1".equals(request.getParameter("type"))) {
        filedisplay = "<fmt:message key='venus.authority.Station_opened_on_a_trusted' bundle='${applicationAuResources}' />ActiveX<fmt:message key='venus.authority.Control' bundle='${applicationAuResources}' />.reg";
        //生成对受信任站站启用ActiveX控件的注册表脚本
        str.append("Windows Registry Editor Version 5.00");
        str.append("\n");
        str.append("\n");
        str.append("[HKEY_CURRENT_USER\\Software\\Microsoft\\Windows\\CurrentVersion\\Internet Settings\\ZoneMap\\Ranges\\Range100]");
        str.append("\n");
        str.append("\"http\"=dword:00000002");
        str.append("\n");
        str.append("\":Range\"=\"").append(InetAddress.getByName(request.getServerName()).getHostAddress()).append("\"");
        str.append("\n");
        str.append("\n");
        str.append("[HKEY_CURRENT_USER\\Software\\Microsoft\\Windows\\CurrentVersion\\Internet Settings\\Zones\\2]");
        str.append("\n");
        str.append("\"DisplayName\"=\"<fmt:message key='venus.authority.Trusted_Sites' bundle='${applicationAuResources}' />\"");
        str.append("\n");
        str.append("\"Description\"=\"<fmt:message key='venus.authority.Recognized_in_the_region_that_contains_your_data_will_not_damage_the_computer_or_Web_site' bundle='${applicationAuResources}' />\"");
        str.append("\n");
        str.append("\"1201\"=dword:00000000");      
      } else { 
        filedisplay = "<fmt:message key='venus.authority.Restore_confidence_in_the_opening_station' bundle='${applicationAuResources}' />ActiveX<fmt:message key='venus.authority.Control' bundle='${applicationAuResources}' />.reg";
        //生成恢复受信任站站启用的ActiveX控件的注册表脚本
        str.append("Windows Registry Editor Version 5.00");
        str.append("\n");
        str.append("\n");
        str.append("[HKEY_CURRENT_USER\\Software\\Microsoft\\Windows\\CurrentVersion\\Internet Settings\\Zones\\2]");
        str.append("\n");
        str.append("\"DisplayName\"=\"<fmt:message key='venus.authority.Trusted_Sites' bundle='${applicationAuResources}' />\"");
        str.append("\n");
        str.append("\"Description\"=\"<fmt:message key='venus.authority.Recognized_in_the_region_that_contains_your_data_will_not_damage_the_computer_or_Web_site' bundle='${applicationAuResources}' />\"");
        str.append("\n");
        str.append("\"1201\"=dword:00000001");
        str.append("\n");
        str.append("\n");
        str.append("[-HKEY_CURRENT_USER\\Software\\Microsoft\\Windows\\CurrentVersion\\Internet Settings\\ZoneMap\\Ranges\\Range100]");
        str.append("\n");
        str.append("\"http\"=dword:00000002");
        str.append("\n");
        str.append("\":Range\"=\"").append(InetAddress.getByName(request.getServerName()).getHostAddress()).append("\"");        
      }
      
      response.setContentType("application/x-download");
      filedisplay = URLEncoder.encode(filedisplay,"UTF-8");
      response.addHeader("Content-Disposition","attachment;filename=" + filedisplay);
     
      try {
          outp = response.getOutputStream();
          in = new ByteArrayInputStream(str.toString().getBytes());
          byte[] b = new byte[1024];
          int i = 0;
          while((i = in.read(b)) > 0) {
              outp.write(b, 0, i);
          }
          outp.flush();
      } catch(Exception e) {
          System.out.println("<fmt:message key='venus.authority.Download_Error' bundle='${applicationAuResources}' />!");
          e.printStackTrace();
      } finally {
          if (in != null) {
              in.close();
              in = null;
          }
          if (outp != null) {
              outp.close();
              out.clear();
              out = pageContext.pushBody();              
              outp = null;
          }
      }
%>

