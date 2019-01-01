/*
 * 创建日期 2008-10-29
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package venus.oa.service.history.model;

import venus.oa.service.history.dao.IHistoryLogDao;
import venus.oa.service.history.model.adaptor.HistoryAdaptor;
import venus.oa.service.history.vo.HistoryLogVo;
import venus.pub.lang.OID;

/**
 * @author zangjian
 *
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */

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

