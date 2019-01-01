package venus.frames.mainframe.action;

import org.w3c.dom.Node;
import venus.frames.mainframe.util.ConfMgr;
import venus.frames.mainframe.util.DefaultConfReader;
import venus.frames.mainframe.util.IConfReader;

import java.util.HashMap;
import java.util.List;

public class OperationHintUtil {
	
	private static OperationHintUtil m_oOperationHintUtil ;
	
	private boolean m_bIsPushOperationHint = false ;
	
	public int m_intPushOperationHintsLength = 0;
	
	public String[] m_aryOperationHintForwards = null;
	
	public String[] m_aryOperationHints = null;
	
	public HashMap m_mapOperationHints = null;
	
		
	public OperationHintUtil() {
		
		m_mapOperationHints = new HashMap();
		
		loadConf();
	
	}
	
	
	public static OperationHintUtil getSingleton(){
		
		if (m_oOperationHintUtil == null) {
			
			m_oOperationHintUtil = new OperationHintUtil();
			
			m_oOperationHintUtil.loadConf(); 
			
			
		}
		
		
		return m_oOperationHintUtil;
		
	}
	
	public static String getOperationHintFromForward(String forward){

		if( forward==null && forward.length() <1 ){
			
			return null;
			
		}
		
		forward = forward.toLowerCase();
		
		OperationHintUtil ohu = getSingleton();
		
		
		if(  ohu.m_mapOperationHints.containsKey(forward) ){
			
			return (String) ohu.m_mapOperationHints.get( forward );
			
		
		}else{
			
			
			String re = null;
		
			
			for (int j=0; j < ohu.m_intPushOperationHintsLength; j++) {				
				
				String forwardkey = ohu.m_aryOperationHintForwards[j];				
				
				
				if( forward.indexOf(forwardkey)>= 0 ) {
					
					re = ohu.m_aryOperationHints[j];
					
					
					if(forward.equals(forwardkey) ) {
						
						break;
					
					}
				
				}		
			}
			
			ohu.m_mapOperationHints.put(forward,re);
			return re;
		}
	}

	private void loadConf(){
		
		
		
		IConfReader dcr =					
			ConfMgr.getConfReader(this.getClass().getName());
			
		m_bIsPushOperationHint = dcr.readBooleanAttribute("isPushOperationHint");
		
		
		List childs = dcr.readChildNodesAry("hint");
		
		
		m_intPushOperationHintsLength = childs.size();
		
		m_aryOperationHintForwards = new String[m_intPushOperationHintsLength];
		
		m_aryOperationHints = new String[m_intPushOperationHintsLength];

			
		for (int j=0; j < m_intPushOperationHintsLength; j++) {
			DefaultConfReader dcrp = new DefaultConfReader((Node) childs.get(j));
			m_aryOperationHintForwards[j] = dcrp.readStringAttribute("forward");
			m_aryOperationHints[j] = (String) dcrp.readStringAttribute("msg");

		}


	}
	
	public static boolean isPushOperationHint(){
		return getSingleton().getIsPushOperationHint();
	}

	/**
	 * @return 返回 m_bIsPushOperationHint。
	 */
	public boolean getIsPushOperationHint() {
		return m_bIsPushOperationHint;
	}
	/**
	 * @param isPushOperationHint 要设置的 m_bIsPushOperationHint。
	 */
	public void setIsPushOperationHint(boolean isPushOperationHint) {
		m_bIsPushOperationHint = isPushOperationHint;
	}
}
