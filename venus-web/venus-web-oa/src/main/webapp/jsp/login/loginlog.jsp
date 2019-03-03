<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="venus.authority.helper.LoginHelper"%>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="java.util.StringTokenizer" %>
<%@ page import="venus.authority.util.BrowserDetect" %>
<%@ page import="venus.authority.login.tools.OnlineUserVo" %>
<%@ page import="venus.authority.login.loginlog.bs.ILoginLogBs" %>
<%@ page import="venus.authority.login.loginlog.util.ILoginLogConstants" %>
<%@ page import="venus.authority.au.auuser.bs.IAuUserBs" %>
<%@ page import="venus.authority.au.auuser.util.IAuUserConstants" %>
<%@ page import="venus.authority.au.auuser.vo.AuUserVo" %>
<%@ page import="java.util.List" %>
<%@ page import="venus.frames.mainframe.util.Helper" %>
<%@ page import="venus.pub.lang.OID" %>
<%
		//记录失败登录的历史
		Timestamp loginTime = new Timestamp(session.getCreationTime());//获得session创建时间
		String ip = request.getRemoteAddr();//获得客户端的ip地址
		String host = request.getRemoteHost();//获得客户端电脑的名字，若失败，则返回客户端电脑的ip地址 
		String agent = request.getHeader("user-agent"); 
		String login_mac = (String)session.getAttribute("login_mac");
		String browser = null;
        String os = null;
		if(agent.contains("Mozilla/5.0")){
            if(agent.contentEquals("MSIE")){
                StringTokenizer st = new StringTokenizer(agent,";"); 
                st.nextToken(); 
                browser = st.nextToken(); //得到用户的浏览器名 
                os = st.nextToken(); //得到用户的操作系统名 
            }else{
                StringTokenizer st = new StringTokenizer(agent,"(");
                st.nextToken();
                os = new StringTokenizer(st.nextToken(),")").nextToken(); //得到用户的操作系统名
                if(os.contains(";")){
                    os = os.substring(0,os.indexOf(";"));   
                }
                //String allTokens = agent.replaceAll("\\(.*?\\)","");
                //String tokens[] = allTokens.split("\\s+");
                browser = BrowserDetect.getUserAgentDebugString(request); //得到用户的浏览器名 
            }
        }else{
            StringTokenizer st = new StringTokenizer(agent,";"); 
            st.nextToken(); 
            browser = st.nextToken(); //得到用户的浏览器名 
            os = st.nextToken(); //得到用户的操作系统名 
        } 
		
		OnlineUserVo vo = new OnlineUserVo();
		vo.setLogin_id(request.getParameter("login_id"));
		List logins = ((IAuUserBs) Helper.getBean(IAuUserConstants.BS_KEY)).queryByCondition("LOGIN_ID ='" + vo.getLogin_id() + "'");
		if(logins.size()>0){
		    vo.setName(((AuUserVo)logins.get(0)).getName());
		}		
		vo.setLogin_ip(ip);
		vo.setIe(browser);
		vo.setOs(os);
		vo.setHost(host);
		vo.setLogin_time(loginTime);
		vo.setLogin_state((String)request.getAttribute("Message"));
		vo.setLogin_mac(login_mac);
		OID oid = ((ILoginLogBs) Helper.getBean(ILoginLogConstants.BS_KEY)).insert(vo);
		
		pageContext.forward("/jsp/login/login.jsp");
%>

