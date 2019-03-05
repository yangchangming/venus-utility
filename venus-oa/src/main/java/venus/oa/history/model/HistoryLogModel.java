package venus.oa.history.model;

import venus.oa.history.dao.IHistoryLogDao;
import venus.oa.history.model.adaptor.HistoryAdaptor;
import venus.oa.history.vo.HistoryLogVo;
import venus.pub.lang.OID;

public class HistoryLogModel {

	private HistoryAdaptor adaptor;
	private HistoryLogVo logVo;
	private Object srcVo;
	
	/**
	 * @param adaptor 要设置的 adaptor。
	 */
	public void setAdaptor(HistoryAdaptor adaptor) {
		this.adaptor = adaptor;
	}
	
	/**
	 * 生命周期：初始化
	 */
	public void init(){
		logVo = adaptor.assembler(srcVo);
	}
	
	/**
	 * 打标签
	 */
	public void addTag(){
	}
	
	/**
	 * 记录日志
	 * @param vo　原始Vo
	 * @param dao　数据库持久化Dao
	 */
	public OID onLog(Object vo,IHistoryLogDao dao){
		this.srcVo=vo;
		init();
		//TODO 打Tag
		//addTag();
		return dao.insert(logVo);
	}
}

