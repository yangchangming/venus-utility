package venus.frames.mainframe.util;


import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServletWrapper;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.upload.MultipartRequestHandler;
import org.apache.struts.upload.MultipartRequestWrapper;
import org.apache.struts.util.RequestUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.*;


public class RequestUtil extends org.apache.struts.util.RequestUtils {

    /**
     * Create a map containing all of the parameters supplied for a multipart
     * request, keyed by parameter name. In addition to text and file elements
     * from the multipart body, query string parameters are included as well.
     *
     * @param request The (wrapped) HTTP request whose parameters are to be
     *                added to the map.
     * @param multipartHandler The multipart handler used to parse the request.
     *
     * @return the map containing all parameters for this multipart request.
     */
    private static Map getAllParametersForMultipartRequest(
            HttpServletRequest request,
            MultipartRequestHandler multipartHandler) {
        Map parameters = new HashMap();
        Enumeration genum;

        Hashtable elements = multipartHandler.getAllElements();
        genum = elements.keys();
        while (genum.hasMoreElements()) {
            String key = (String) genum.nextElement();
            parameters.put(key, elements.get(key));
        }

        if (request instanceof MultipartRequestWrapper) {
            request = ((MultipartRequestWrapper)request).getRequest();
            genum = request.getParameterNames();
            while (genum.hasMoreElements()) {
                String key = (String) genum.nextElement();
                parameters.put(key, request.getParameterValues(key));
            }
        } else {
            RequestUtils.log.debug("Gathering multipart parameters for unwrapped request");
        }

        return parameters;
    }


     /**
     * Try to locate a multipart request handler for this request. First, look
     * for a mapping-specific handler stored for us under an attribute. If one
     * is not present, use the global multipart handler, if there is one.
     *
     * @param request The HTTP request for which the multipart handler should
     *                be found.
     * @return the multipart handler to use, or <code>null</code> if none is
     *         found.
     *
     * @exception ServletException if any exception is thrown while attempting
     *                             to locate the multipart handler.
     */
    private static MultipartRequestHandler getMultipartHandler(HttpServletRequest request)
        throws ServletException {

        MultipartRequestHandler multipartHandler = null;
        String multipartClass = (String) request.getAttribute(Globals.MULTIPART_KEY);
        request.removeAttribute(Globals.MULTIPART_KEY);

        // Try to initialize the mapping specific request handler
        if (multipartClass != null) {
            try {
                multipartHandler = (MultipartRequestHandler) RequestUtils.applicationInstance(multipartClass);
            } catch (ClassNotFoundException cnfe) {
                RequestUtils.log.error(
                    "MultipartRequestHandler class \""
                        + multipartClass
                        + "\" in mapping class not found, "
                        + "defaulting to global multipart class");
            } catch (InstantiationException ie) {
                RequestUtils.log.error(
                    "InstantiaionException when instantiating "
                        + "MultipartRequestHandler \""
                        + multipartClass
                        + "\", "
                        + "defaulting to global multipart class, exception: "
                        + ie.getMessage());
            } catch (IllegalAccessException iae) {
                RequestUtils.log.error(
                    "IllegalAccessException when instantiating "
                        + "MultipartRequestHandler \""
                        + multipartClass
                        + "\", "
                        + "defaulting to global multipart class, exception: "
                        + iae.getMessage());
            }

            if (multipartHandler != null) {
                return multipartHandler;
            }
        }

        ModuleConfig moduleConfig = (ModuleConfig) request.getAttribute(Globals.MODULE_KEY);
        multipartClass = moduleConfig.getControllerConfig().getMultipartClass();

        // Try to initialize the global request handler
        if (multipartClass != null) {
            try {
                multipartHandler = (MultipartRequestHandler) RequestUtils.applicationInstance(multipartClass);
            } catch (ClassNotFoundException cnfe) {
                throw new ServletException(
                    "Cannot find multipart class \""
                        + multipartClass
                        + "\""
                        + ", exception: "
                        + cnfe.getMessage());
            } catch (InstantiationException ie) {
                throw new ServletException(
                    "InstantiaionException when instantiating "
                        + "multipart class \""
                        + multipartClass
                        + "\", exception: "
                        + ie.getMessage());
            } catch (IllegalAccessException iae) {
                throw new ServletException(
                    "IllegalAccessException when instantiating "
                        + "multipart class \""
                        + multipartClass
                        + "\", exception: "
                        + iae.getMessage());
            }

            if (multipartHandler != null) {
                return multipartHandler;
            }
        }

        return multipartHandler;
    }
    
    
    /**
     * Populate the properties of the specified JavaBean from the specified
     * HTTP request, based on matching each parameter name (plus an optional
     * prefix and/or suffix) against the corresponding JavaBeans "property
     * setter" methods in the bean's class.  Suitable conversion is done for
     * argument types as described under <code>setProperties()</code>.
     * <p>
     * If you specify a non-null <code>prefix</code> and a non-null
     * <code>suffix</code>, the parameter name must match <strong>both</strong>
     * conditions for its value(s) to be used in populating bean properties.
     * If the request's content type is "multipart/form-data" and the
     * method is "POST", the HttpServletRequest object will be wrapped in
     * a MultipartRequestWrapper object.
     *
     * @param bean The JavaBean whose properties are to be set
     * @param prefix The prefix (if any) to be prepend to bean property
     *               names when looking for matching parameters
     * @param suffix The suffix (if any) to be appended to bean property
     *               names when looking for matching parameters
     * @param request The HTTP request whose parameters are to be used
     *                to populate bean properties
     *
     * @exception ServletException if an exception is thrown while setting
     *            property values
     */
    public static void populate(Object bean, String prefix, String suffix, HttpServletRequest request, Map map, String[] ignoreProperties) throws ServletException {

        // Build a list of relevant request parameters from this request
        HashMap properties = new HashMap();
        // Iterator of parameter names
        Enumeration names = null;
        // Map for multipart parameters
        Map multipartParameters = null;

        String contentType = request.getContentType();
        String method = request.getMethod();
        boolean isMultipart = false;

        if ((contentType != null) && (contentType.startsWith("multipart/form-data")) && (method.equalsIgnoreCase("POST"))) {

            // Get the ActionServletWrapper from the form bean
            ActionServletWrapper servlet;
            if (bean instanceof ActionForm) {
                servlet = ((ActionForm) bean).getServletWrapper();
            } else {
                throw new ServletException(
                        "bean that's supposed to be "
                        + "populated from a multipart request is not of type "
                        + "\"org.apache.struts.action.ActionForm\", but type "
                        + "\""
                        + bean.getClass().getName()
                        + "\"");
            }

            // Obtain a MultipartRequestHandler
            MultipartRequestHandler multipartHandler = getMultipartHandler(request);

            // Set the multipart request handler for our ActionForm.
            // If the bean isn't an ActionForm, an exception would have been
            // thrown earlier, so it's safe to assume that our bean is
            // in fact an ActionForm.
             ((ActionForm) bean).setMultipartRequestHandler(multipartHandler);

            if (multipartHandler != null) {
                isMultipart = true;
                // Set servlet and mapping info
                servlet.setServletFor(multipartHandler);
                multipartHandler.setMapping(
                    (ActionMapping) request.getAttribute(Globals.MAPPING_KEY));
                // Initialize multipart request class handler
                multipartHandler.handleRequest(request);
                //stop here if the maximum length has been exceeded
                Boolean maxLengthExceeded =
                    (Boolean) request.getAttribute(
                        MultipartRequestHandler.ATTRIBUTE_MAX_LENGTH_EXCEEDED);
                if ((maxLengthExceeded != null) && (maxLengthExceeded.booleanValue())) {
                    return;
                }
                //retrive form values and put into properties
                multipartParameters = getAllParametersForMultipartRequest(
                        request, multipartHandler);
                names = Collections.enumeration(multipartParameters.keySet());
            }
        }

        if (!isMultipart) {
            names = request.getParameterNames();
        }

        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            String stripped = name;
            if (prefix != null) {
                if (!stripped.startsWith(prefix)) {
                    continue;
                }
                stripped = stripped.substring(prefix.length());
            }
            if (suffix != null) {
                if (!stripped.endsWith(suffix)) {
                    continue;
                }
                stripped = stripped.substring(0, stripped.length() - suffix.length());
            }
            if (isMultipart) {
                properties.put(stripped, multipartParameters.get(name));
            } else {
            	
            	
            	String[] obj = request.getParameterValues(name);
				
            	if( obj!=null && obj.length >1 ){
            		
            		properties.put(stripped, request.getParameterValues(name));
            	}else{
            		
            		properties.put(stripped, request.getParameter(name));
            	}
                
            }
        }

        // Set the corresponding properties of our bean
        try {
        	PopulateUtil.copyProperties( properties , bean ,map, ignoreProperties);
        } catch (Exception e) {
            throw new ServletException("BeanUtils.populate", e);
        }

    }
    
    
    
/*
    public static void populate(Object bean, Map properties , String[] ignoreProperties)
        throws IllegalAccessException, InvocationTargetException
    {
        List ignoreList = (ignoreProperties != null) ? Arrays.asList(ignoreProperties) : null;
        if(bean == null || properties == null)
            return;
        if(log.isDebugEnabled())
            log.debug("BeanUtils.populate(" + bean + ", " + properties + ")");
        for(Iterator names = properties.keySet().iterator(); names.hasNext();)
        {
            String name = (String)names.next();
            if( name != null && (ignoreProperties == null || (!ignoreList.contains(name)) ) )
            {
                Object value = properties.get(name);
                BeanUtils.setProperty(bean, name, value);
            }
        }

    }
*/
    
	/**
	 * Bean复制工具方法
	 * @param obj target bean
	 * @param request  source request
	 * @param ignoreProperties   ignore target bean's property name	
	 * @return
	 */
    public static void populate(Object bean, HttpServletRequest request,String[] ignoreProperties)
        throws ServletException {
        populate(bean, null, null, request,null,ignoreProperties);
    }
    
	/**
	 * Bean复制工具方法
	 * @param obj target bean
	 * @param request  source request
	 * @param map rename property map:  key: srcName, value:targetName
	 * @return
	 */
    public static void populate(Object bean, HttpServletRequest request,Map map)
        throws ServletException
    {

        populate(bean, null, null, request,map,null);
    }
    
    
    
	/**
	 * Bean复制工具方法
     *
	 * @param request  source request
	 * @param map rename property map:  key: srcName, value:targetName
	 * @param ignoreProperties   ignore target bean's property name	
	 * @return
	 */
    public static void populate(Object bean, HttpServletRequest request,Map map,String[] ignoreProperties) throws ServletException
    {
        populate(bean, null, null, request,map,ignoreProperties);
    }
    
    
    
    

}