package venus.oa.taglib;

import fr.improve.struts.taglib.layout.collection.FastCollectionItemTag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTag;

/**
 * This tag is used to add a property to display to a tag that can display collections
 * (the parent tag must be an instanceof BaseCollectionTag)
 * <br><br>
 * header - The key of the title to display for this property<br>
 * property - The property to display<br>
 * <br>
 * @see BaseCollectionTag BaseCollectionTag
 *
 * @author: Jean-Noel Ribette
 **/
public class CollectionItemTag extends FastCollectionItemTag implements BodyTag {
	protected BodyContent bodyContent;
	protected boolean useBody = true; 
	
	public void doInitBody() throws JspException {
		// do nothing.
	}
	public int doAfterBody() throws JspException {
		// do nothing.
		return SKIP_BODY;
	}	
	public void setBodyContent(BodyContent in_content) {
		bodyContent = in_content;
	}
	
	public void release() {
		super.release();
		bodyContent = null;	
	}
	
	public int doStartLayoutTag() throws JspException {
		int lc_result = super.doStartLayoutTag();
		if (lc_result==EVAL_BODY_INCLUDE) {
			return 2;//EVAL_BODY_TAG;//because deprecated
		} else {
			return lc_result;
		}
	}
	
	protected Object buildContent() throws JspException {
		if (bodyContent != null && bodyContent.getString().length() > 0) {
			// The item to add is the body content of the tag.
			Object lc_cell = bodyContent.getString();
			bodyContent.clearBody();
			useBody = true;			
			return lc_cell;
		} else {
			useBody = false;
			return super.buildContent();
		}
	}
	
	protected boolean buildFilter() {		
		if (useBody) {
			return false;
		} else {
			return super.buildFilter();
		}
	}		
	
	protected void initDynamicValues() {
        super.initDynamicValues();
        if(title.contains("<fmt:message ")){
            StringBuffer titleAnswer = new StringBuffer();
            String titleStr[] = title.split("<fmt:message ");
            for(int i=0;i<titleStr.length;i++){
                String titleSplitStr = titleStr[i];
                if(titleSplitStr.contains("/>")){
                    String messageKeyFinder = titleSplitStr.substring(titleSplitStr.indexOf("key"));
                    String messageKey = null;
                    if(messageKeyFinder.contains("'")){
                        messageKey = messageKeyFinder.split("'")[1];
                    }else{
                        messageKey = messageKeyFinder.split("\"")[1];
                    }
                    int strPosition = titleSplitStr.indexOf("/>");
                    titleSplitStr = venus.frames.i18n.util.LocaleHolder.getMessage(messageKey) + titleSplitStr.substring(-1==strPosition?0:(strPosition+2));
                }
                titleAnswer.append(titleSplitStr);
            }
            title = titleAnswer.toString();
        }        
    }
}