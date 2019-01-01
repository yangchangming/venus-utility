package venus.frames.mainframe.util;

import venus.frames.base.action.IRequest;

/**
 * 
 * 解决SQL注入时前台拼装SQL的问题。
 * @author changming.Y <changming.yang.ah@gmail.com>
 *
 */
public abstract class QueryBuilder {
    protected String conditions[];
	/**
	 * gap-mainframe.js的function pushCondition(myArray, pageRealValue, operate1, operate2, thisField)方法
	 * @param sf
	 * @param pageRealValue
	 * @param operate1
	 * @param operate2
	 * @param thisField
	 * @return
	 */
	public String pushCondition(IRequest request, String pageRealValue, String operate1, String operate2, String thisField) {
		StringBuffer sf = new StringBuffer();
		if(null == operate1) {
			operate1 = " like '%";
			operate2 = "%' ";
		} else if(null == operate2) {
			operate2 = "";
		}
		if(null == thisField) {
			thisField = pageRealValue;
		}
		if(null == pageRealValue || "".equals(pageRealValue)|| "".equals(request.getParameter(pageRealValue))|| null == request.getParameter(pageRealValue)) {
			return null;
		}
		sf.append(" " + thisField + " " + operate1 + request.getParameter(pageRealValue) + operate2);
		return sf.toString();
	}
	
	public String pushCondition(IRequest request, String pageRealValue, String operate1, String operate2){
		return pushCondition(request,pageRealValue,operate1,operate2,null);
	}
	
	public String pushCondition(IRequest request, String pageRealValue){
		return pushCondition(request,pageRealValue,null,null,null);
	}
	
	/**
	 * 将单个查询条件拼装成where条件，但不带where关键字
	 * @param conditions 单个条件
	 * @return 拼装好的全部条件，用and关联
	 */
	public abstract void andCondition();
	
	public String build(){
	    StringBuffer queryCondition = new StringBuffer();
        boolean addFirstAnd = false;
        andCondition();//组装条件
        for(int i=0; i<conditions.length; i++) {  
            if(null != conditions[i]){
                if(addFirstAnd){
                    queryCondition.append(" and ");                 
                }
                addFirstAnd=true;
                queryCondition.append(conditions[i]);  //组装查询条件
                queryCondition.append(" ");
            }
        }
        return queryCondition.toString();
	}
}

