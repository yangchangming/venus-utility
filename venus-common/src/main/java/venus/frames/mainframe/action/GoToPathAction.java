package venus.frames.mainframe.action;

import venus.frames.base.action.IForward;
import venus.frames.base.action.IRequest;
import venus.frames.base.action.IResponse;


/**
 * 
 * @author
 * @version
 */
public class GoToPathAction {
	
	public static final String GO_TO_PATH_FORWARD = "GoToPath";
	
	public static final String GO_TO_PATH_KEY = "VENUS_GO_TO_PATH_KEY";

	public IForward service(IRequest request, IResponse response) throws Exception {
		
		String parameter = request.getMapping().getParameter();
		
		request.setAttribute( GO_TO_PATH_KEY , parameter );	
		
		return request.findForward(GO_TO_PATH_FORWARD);
	}

}