/*
 * 创建日期 2008-7-20
 */
package venus.oa.util;

import venus.frames.mainframe.util.Helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * SQL构建器
 *  2008-7-20
 * @author changming.Y <changming.yang.ah@gmail.com>
 */
public class SqlBuilder {
	private SqlBuilder.Expression expression;
	private List data;
	private List whereData;
	/**
	 * <code>countBegin</code>分页起始条数
	 */
	private int countBegin;
	/**
	 * <code>countEnd</code> 分页结束条数
	 */
	private int countEnd;

	private String sqlContent;

	private String updateSet;

	private String insertField;

	private String insertValue;

	private String dmlOperate;

	private String dmlTable;//insert,delete,update table

	private boolean accessWrite = false;//insert,delete,update table

	private String selectContent;

	private String fromContent;

	private String whereContent;

	private String groupbyContent;

	private String orderContent;

	private String oid;

	public SqlBuilder(){
		data=new ArrayList();
		whereData = new ArrayList();
		expression=new SqlBuilder.Expression(){
			public String bulidSql() {
				//初始化查询条件,因为Hibernate2的HQL语句只支持查询,故此处初始化查询。
				//将来HIbernate3可考虑使用注入的方式来初始化HQL语句。
				return " 1 = 1 ";
			}
		};
	}
	/**
	 * 构建SQL
	 * @return 构建的sql
	 */
	public String bulidSql(){
		return (null==sqlContent)?(null==selectContent?(null==dmlOperate?"":getDmlOperate()):("SELECT "+getSelectContent()))+(null==insertField?"":("("+getInsertField()+")"))+(null==insertValue?"":(" VALUES ("+getInsertValue()+")"))+(null==updateSet?"":(getUpdateSet()+" WHERE "+expression.bulidSql()))+(null==fromContent?"":(" FROM "+getFromContent()+" WHERE "+expression.bulidSql()))+(null==whereContent?(null==insertValue?(null==selectContent?expression.bulidSql():""):""):getWhereContent())+(null==orderContent?"":getOrderContent()):sqlContent;
	}	
	/* （非 Javadoc）
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return bulidSql();
	}
	/**
	 * 与条件
	 */
	public Expression and(Expression left,Expression right){
		return new And(left,right);
	}
	/**
	 * 或条件
	 */
	public Expression or(Expression left,Expression right){
		return new Or(left,right);
	}
	/**
	 * 与条件
	 */
	public Expression and(Expression right){
		return expression=new And(expression,right);
	}
	/**
	 * 或条件
	 */
	public Expression or(Expression right){
		return expression=new Or(expression,right);
	}
	/**
	 * 等于
	 */
	public Expression equal(String property,Object value){
		data.add(value);
		return new Eq(property);
	}
	/**
	 * 不等于
	 */
	public Expression notEqual(String property,Object value){
		data.add(value);
		return new Ne(property);
	}
	/**
	 * 大于
	 */
	public Expression greaterThan(String property,Object value){
		data.add(value);
		return new Gt(property);
	}
	/**
	 * 小于
	 */
	public Expression lessThan(String property,Object value){
		data.add(value);
		return new Lt(property);
	}
	/**
	 * 大于等于
	 */
	public Expression greateOrEqual(String property,Object value){
		data.add(value);
		return new Ge(property);
	}
	/**
	 * 小于等于
	 */
	public Expression lessOrEqual(String property,Object value){
		data.add(value);
		return new Le(property);
	}
	/**
	 * 区间
	 */
	public Expression between(String property,Object lo,Object hi){
		data.add(lo);
		data.add(hi);
		return new Between(property);
	}
	/**
	 * 模糊
	 */
	public Expression like(String property,Object value){
		data.add("%"+value+"%");
		return new Like(property);
	}
	/**
	 * 非空
	 */
	public Expression isNotNull(String property){
		return new IsNotNull(property);
	}
	/**
	 * 空
	 */
	public Expression isNull(String property){
		return new IsNull(property);
	}
	/**
	 * 隶属于
	 */
	public Expression in(String property,Object value[]){
		for(int i=0;i<value.length;i++){
			data.add(value[i]);
		}
		return new In(property,value.length);
	}
	/**
	 * 第三方注入（供自定义实现使用）
	 */
	public Expression otherMethod(String beanName,Object value[]){
		for(int i=0;i<value.length;i++){
			data.add(value[i]);
		}
		return (Expression) Helper.getBean(beanName);
	}
	
	public interface Expression{
		public String bulidSql();
	}
	private final class And implements Expression{
		private Expression left;
		private Expression right;
		public And(Expression left,Expression right){
			this.left=left;
			this.right=right;
		}
		public String bulidSql(){
			return " ( " + left.bulidSql() + " ) AND ( " + right.bulidSql() + ") ";
		}
	}//And实现
	
	private final class Or implements Expression{
		private Expression left;
		private Expression right;
		public Or(Expression left,Expression right){
			this.left=left;
			this.right=right;
		}
		public String bulidSql(){
			return " ( " + left.bulidSql() + " ) OR ( " + right.bulidSql() + ") ";
		}
	}//Or实现
	
	private final class Eq implements Expression{
		private String property;
		public Eq(String property){
			this.property=property;
		}
		public String bulidSql(){
			return property + " = ?  ";
		}
	}//等于
	
	private final class Ne implements Expression{
		private String property;
		public Ne(String property){
			this.property=property;
		}
		public String bulidSql(){
			return property + "<> ? ";
		}
	}//不等于
	
	private final class Gt implements Expression{
		private String property;
		public Gt(String property){
			this.property=property;
		}
		public String bulidSql(){
			return property + " > ? ";
		}
	}//大于
	
	private final class Lt implements Expression{
		private String property;
		public Lt(String property){
			this.property=property;
		}
		public String bulidSql(){
			return property + " < ? ";
		}
	}//小于
	
	private final class Ge implements Expression{
		private String property;
		public Ge(String property){
			this.property=property;
		}
		public String bulidSql(){
			return property + " >= ? ";
		}
	}//大于等于
	
	private final class Le implements Expression{
		private String property;
		public Le(String property){
			this.property=property;
		}
		public String bulidSql(){
			return property + " <= ? ";
		}
	}//小于等于
	
	private final class Between implements Expression{
		private String property;
		public Between(String property){
			this.property=property;
		}
		public String bulidSql(){
			return property + " BETWEEN (?,?) ";
		}
	}//区间
	
	private final class Like implements Expression{
		private String property;
		public Like(String property){
			this.property=property;
		}
		public String bulidSql(){
			return property + " LIKE ? ";
		}
	}//模糊
	
	private final class IsNotNull implements Expression{
		private String property;
		public IsNotNull(String property){
			this.property=property;
		}
		public String bulidSql(){
			return property + " IS NOT NULL ";
		}
	}//非空
	
	private final class IsNull implements Expression{
		private String property;
		public IsNull(String property){
			this.property=property;
		}
		public String bulidSql(){
			return property + " IS NULL ";
		}
	}// 为空
	
	private final class In implements Expression{
		private String property;
		private int length;
		public In(String property,int length){
			this.property=property;
			this.length=length;
		}
		public String bulidSql(){
			StringBuffer sql=new StringBuffer();
			for(int i=0;i<length;i++){
				sql.append("?");
				if(i!=length-1)
					sql.append(",");
			}
			return property + " IN (" + sql.toString() + ") ";
		}
	}//包含
	/**
	 * @return 返回 countBegin。
	 */
	public int getCountBegin() {
		return countBegin;
	}
	/**
	 * @param countBegin 要设置的 countBegin。
	 */
	public void setCountBegin(int countBegin) {
		this.countBegin = countBegin;
	}
	/**
	 * @return 返回 countEnd。
	 */
	public int getCountEnd() {
		return countEnd;
	}
	/**
	 * @param countEnd 要设置的 countEnd。
	 */
	public void setCountEnd(int countEnd) {
		this.countEnd = countEnd;
	}
	/**
	 * @return 返回 data。
	 */
	public List getData() {
	    List returnData = new ArrayList();
	    returnData.addAll(data);	    
	    returnData.addAll(whereData);
		return returnData;
	}
	
    /**
     * @return the selectContent
     */
    public String getSelectContent() {
        return selectContent;
    }
    /**
     * @param selectContent the selectContent to set
     */
    public void setSelectContent(String selectContent) {
        this.selectContent = selectContent;
    }
    /**
     * @return the fromContent
     */
    public String getFromContent() {
        return fromContent;
    }
    /**
     * @param fromContent the fromContent to set
     */
    public void setFromContent(String fromContent) {
        this.fromContent = fromContent;
    }
    /**
     * @return the orderContent
     */
    public String getOrderContent() {
        return " ORDER BY "+orderContent;
    }
    /**
     * @param orderContent the orderContent to set
     */
    public void setOrderContent(String orderContent) {
        this.orderContent = orderContent;
    }
    /**
     * @return the groupbyContent
     */
    public String getGroupbyContent() {
        return groupbyContent;
    }
    /**
     * @param groupbyContent the groupbyContent to set
     */
    public void setGroupbyContent(String groupbyContent) {
        this.groupbyContent = groupbyContent;
    }
    /**
     * @return the sqlContent
     */
    public String getSqlContent() {
        return sqlContent;
    }
    /**
     * @param sqlContent the sqlContent to set
     */
    public void setSqlContent(String sqlContent,Object value[]) {
        this.sqlContent = sqlContent;
        if(null!=value)
            this.data.addAll(Arrays.asList(value));
    }
    /**
     * @return the whereContent
     */
    public String getWhereContent() {
        return whereContent;
    }
    /**
     * @param whereContent the whereContent to set
     */
    public void setWhereContent(String whereContent,Object value[]) {
        this.whereContent = whereContent;
        if(null!=value)
            this.whereData.addAll(Arrays.asList(value));
    }
    /**
     * @return the deleteFrom
     */
    public String getDmlOperate() {
        return dmlOperate;
    }
    /**
     * @param deleteFrom the deleteFrom to set
     */
    public void setDmlOperate(String dmlOperate) {
        this.dmlOperate = dmlOperate;
    }
    /**
     * @return the updateSet
     */
    public String getUpdateSet() {
        return updateSet;
    }
    /**
     * @param updateSet the updateSet to set
     */
    public void setUpdateSet(String updateSet,Object value[]) {
        this.updateSet = updateSet;
        if(null!=value)
            this.data.addAll(Arrays.asList(value));
    }
    
    /**
     * @return the insertField
     */
    public String getInsertField() {
        return insertField;
    }
    /**
     * @param insertField the insertField to set
     */
    public void setInsertField(String insertField) {
        this.insertField = insertField;
    }
    /**
     * @return the insertValue
     */
    public String getInsertValue() {
        return insertValue;
    }
    /**
     * @param insertValue the insertValue to set
     */
    public void setInsertValue(String insertValue,Object value[]) {
        this.insertValue = insertValue;
        if(null!=value)
            this.data.addAll(Arrays.asList(value));
    }
    /**
     * @return the dmlTable
     */
    public String getDmlTable() {
        return dmlTable;
    }
    /**
     * @param dmlTable the dmlTable to set
     */
    public void setDmlTable(String dmlTable) {
        this.dmlTable = dmlTable;
    }
    /**
     * @return the accessWrite
     */
    public boolean isAccessWrite() {
        return accessWrite;
    }
    /**
     * @param accessWrite the accessWrite to set
     */
    public void setAccessWrite(boolean accessWrite) {
        this.accessWrite = accessWrite;
    }
    
    /**
     * @return the oid
     */
    public String getOid() {
        return oid;
    }
    /**
     * @param oid the oid to set
     */
    public void setOid(String oid) {
        this.oid = oid;
    }
}

