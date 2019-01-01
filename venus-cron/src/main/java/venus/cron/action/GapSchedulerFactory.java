package venus.cron.action;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.ee.servlet.QuartzInitializerServlet;
import org.quartz.impl.StdSchedulerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

/**
 * @author zhangbaoyu
 */
public class GapSchedulerFactory {
	private static Scheduler scheduler;
	private static StdSchedulerFactory stdSchedulerFactory;

	public static void  init(ServletConfig cfg){
		stdSchedulerFactory = (StdSchedulerFactory)cfg.getServletContext().getAttribute(QuartzInitializerServlet.QUARTZ_FACTORY_KEY);
	}
													 
	public static synchronized Scheduler getScheduler() throws ServletException{
		try {
			if (scheduler == null) {
				scheduler = stdSchedulerFactory.getScheduler();
			}
			return scheduler;
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static synchronized Scheduler getNewScheduler() {
		try {
				scheduler = new StdSchedulerFactory(DefinitionInitializer.CONFIG_FILE).getScheduler();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return scheduler;
	}
	
//	/**
//	   * Gets a reference to the named servlet, attempting to load it 
//	   * through an HTTP request if necessary.  Returns null if there's a problem.
//	   * This method behaves similarly to <tt>ServletContext.getServlet()</tt>
//	   * except, while that method may return null if the 
//	   * named servlet wasn't already loaded, this method tries to load 
//	   * the servlet using a dummy HTTP request.  Only loads HTTP servlets.
//	   *
//	   * @param name the name of the servlet
//	   * @param req the servlet request
//	   * @param context the servlet context
//	   * @return the named servlet, or null if there was a problem
//	   */
//	  public static Servlet getServlet(String name,
//	                                   ServletRequest req,
//	                                   ServletContext context) {
//	    try {
//	      // Try getting the servlet the old fashioned way
//	      Servlet servlet = context.getServlet(name);
//	      if (servlet != null) return servlet;
//
//	      // If getServlet() returned null, we have to load it ourselves.
//	      // Do this by making an HTTP GET request to the servlet.
//	      // Use a raw socket connection so we can set a timeout.
//	      Socket socket = new Socket(req.getServerName(), req.getServerPort());
//	      socket.setSoTimeout(4000);  // wait up to 4 secs for a response
//	      PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
//	      out.println("GET /servlet/" + name + " HTTP/1.0");  // the request
//	      out.println();
//	      try {
//	        socket.getInputStream().read();  // Even one byte means its loaded
//	      }
//	      catch (InterruptedIOException e) { /* timeout: ignore, hope for best */ }
//	      out.close();
//
//	      // Try getting the servlet again.
//	      return context.getServlet(name);
//	    }
//	    catch (Exception e) {
//	      // If there's any problem, return null.
//	      return null;
//	    }
//	  }

}