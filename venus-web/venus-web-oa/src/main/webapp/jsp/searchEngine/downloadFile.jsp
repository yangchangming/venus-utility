<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/jsp/include/global.jsp" %>

<%@ page import="java.io.*" %>
<%@ page import="venus.frames.i18n.util.LocaleHolder"%>

<%	
	//Get the file name from Http Request, the parameter posted by get method or post method.
	String strFile = request.getParameter("file");

	//Get the full file name include file path, maybe you can use a macro method 
	//to get the absolute data path
	String strPath = request.getParameter("path");		
	String browserType=request.getHeader("User-Agent");//得到浏览器等相关信息

	try
	{
		//Obtain input bytes from the file 
		FileInputStream oFIS = new FileInputStream( strPath );
		out.clearBuffer();
		response.reset();
		//Set the content type and header to response.
		response.setContentType("application/octet-stream");
		//response.setHeader( "Content-Disposition","attachment;filename="+strFile );
		if(browserType.indexOf("MSIE")!=-1){   //这里是msie 即微软 ie浏览器(IE识别url编码的中文文件名（非标准，对于Content-Disposition的filename属性，标准的会在url编码前加字符集）会自动转换，而firefox识别iso8859-1自动转换为utf-8，或者标准的url编码的文件名)
			response.setHeader( "Content-Disposition","attachment;filename="+java.net.URLEncoder.encode(new String(strFile.getBytes(),"UTF-8")));
		}else{
			response.setHeader( "Content-Disposition","attachment;filename="+new String(strFile.getBytes(),"ISO8859-1"));
		}

		//A ServletOutputStream object is normally retrieved via the ServletResponse.getOutputStream() method. 
		ServletOutputStream oOs = response.getOutputStream( );
		DataOutputStream oDos = new DataOutputStream( oOs );
		//Provides an output stream for sending binary data to the client. 
		if( null != oFIS ) 
		{
			int nLength;
			byte[] abBuffer= new byte[1024];
			while( ( nLength = oFIS.read( abBuffer, 0, 1024 ) ) > 0 ) 
				oDos.write( abBuffer, 0, nLength );
				
			oOs.close();
			oFIS.close();
			oDos.close();
		}
	}
	catch(FileNotFoundException e)
	{
		out.println(LocaleHolder.getMessage("udp.searchengine.can_not_find_the_specified_file_it_may_be_deleted_or_lose_please_contact_system_administrator"));
	}
%>