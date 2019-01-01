package venus.frames.web.page;

import venus.frames.mainframe.util.Helper;

/**
 * @author libaoyu
 *
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class PageVo 
{
	/*******记录总条数*********/
	private int recordCount = 0;
	
	/*******页大小*********/
	private int pageSize = 0;	
	
	/*******页数*********/
	private int pageCount = 0;		

	/*******当前页*********/
	private int currentPage = 0;	
	
	/*******当前排序条件********/
	private String orderKey;
	
	/**
	 * @return 返回 currentPage。
	 */
	public int getCurrentPage() {
		return currentPage;
	}
	/**
	 * @param currentPage 要设置的 currentPage。
	 */
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	/**
	 * @return 返回 pageCount。
	 */
	public int getPageCount() {
		return pageCount;
	}
	/**
	 * @param pageCount 要设置的 pageCount。
	 */
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	/**
	 * @return 返回 pageSize。
	 */
	public int getPageSize() {
		return pageSize;
	}
	/**
	 * @param pageSize 要设置的 pageSize。
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	/**
	 * @return 返回 recordCount。
	 */
	public int getRecordCount() {
		return recordCount;
	}
	/**
	 * @param recordCount 要设置的 recordCount。
	 */
	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}
	
	/**
	 * added by changming.y
	 * @return 返回 orderKey。
	 */
	public String getOrderKey() {
		return orderKey;
	}
	/**
	 * @param orderKey 要设置的 orderKey。
	 */
	public void setOrderKey(String orderKey) {
		this.orderKey = orderKey;
	}
	
	/**
	 * @param 重新计算 PageCount。
	 */
	public void rePageCount() {
		//modify by changming.y, change the operator from ">" to ">=";
		if( (pageSize*(currentPage-1)) >= recordCount ) {
			setCurrentPage((int)Math.ceil((double)recordCount/pageSize));
		}
		if( pageSize <= 0 ){
			setPageSize(Helper.DEFAULT_PAGE_SIZE);
		}
		setPageCount((int)Math.ceil((double)recordCount/pageSize));
	}
}
