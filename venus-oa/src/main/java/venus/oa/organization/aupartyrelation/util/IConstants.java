/*
 * 创建日期 2006-10-24
 *
 */
package venus.oa.organization.aupartyrelation.util;

/**
 * @author maxiao
 *
 */
public interface IConstants {
    //OID定义引用
    String OID = "AU_PARTYRELATION";
    
    //BS定义引用
    String BS_KEY = "aupartyrelation_bs";
    
    //DAO定义引用
    String DAO = "aupartyrelation_dao";

    String MESSAGE_AGENT_ERROR = "common/common_error";
    String FORWARD_TREE_KEY = "authority/org/aupartyrelation/tree";
    String FORWARD_ADDROOT_KEY = "authority/org/aupartyrelation/addRoot";
    String FORWARD_PARTY_DETAIL_KEY = "queryPartyDetail";
    String FORWARD_LIST_PAGE_KEY = "authority/org/aupartyrelation/searchList";
    
    //团体关系表名
    String TABLENAME="AU_PARTYRELATION";
    
    //父团体编码的列名
    String COLUMNNAME="CODE";
    
    /*--SQL定义引用   start--*/
    String INSERT_AU_PARTYRELATION_SQL = "insert into AU_PARTYRELATION(id,relationtype_id,relationtype_keyword,parent_partyid,parent_code,partyid,code,name,partytype_id,type_level,order_code,is_leaf,type_is_leaf,is_inherit,is_chief,email,create_date,modify_date) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    
    String UPDATE_LEAF_SQL = "update AU_PARTYRELATION set IS_LEAF=?, TYPE_IS_LEAF=?, MODIFY_DATE=? where ID=?";
    
    String UPDATE_AU_PARTYRELATION_SQL = "update AU_PARTYRELATION set RELATIONTYPE_ID=?, RELATIONTYPE_KEYWORD=?, PARENT_PARTYID=?, PARENT_CODE=?, PARTYID=?, CODE=?, NAME=?, PARTYTYPE_ID=?, TYPE_LEVEL=?, ORDER_CODE=?, IS_LEAF=?, TYPE_IS_LEAF=?, IS_INHERIT=?, IS_CHIEF=?, EMAIL=?, CREATE_DATE=?, MODIFY_DATE=? where ID=?";
    
    String QUERY_AU_PARTYRELATION_SQL="select id,relationtype_id,relationtype_keyword,parent_partyid,parent_code,partyid,code,name,partytype_id,type_level,order_code,is_leaf,type_is_leaf,is_inherit,is_chief,email,create_date,modify_date from AU_PARTYRELATION ";
    
    String QUERY_AU_PARTYRELATIONTYPE_SQL="select id,name,keyword,enable_status,enable_date,create_date,modify_date from AU_PARTYRELATIONTYPE ";
    
    String QUERY_AU_PARTY_SQL="select id,partytype_id,partytype_keyword,is_inherit,is_real,name,email,enable_status,enable_date,create_date,modify_date from AU_PARTY  ";
    
    String QUERY_AU_CONNECTRULE="select id,relation_type_id,parent_partytype_id,child_partytype_id,name,create_date,modify_date from AU_CONNECTRULE ";
    
    String DELETE_AU_PARTYRELATION_SQL="delete from AU_PARTYRELATION ";
    
    String SQL_COUNT = "select count(*) from AU_PARTYRELATION";
    
    String SQL_FIND_BY_ID = "select ID,RELATIONTYPE_ID,RELATIONTYPE_KEYWORD,PARENT_PARTYID,PARENT_CODE,PARTYID,CODE,NAME,PARTYTYPE_ID,TYPE_LEVEL,ORDER_CODE,IS_LEAF,TYPE_IS_LEAF,IS_INHERIT,IS_CHIEF,EMAIL,CREATE_DATE,MODIFY_DATE from AU_PARTYRELATION where ID=?";
    
    String SQL_FIND_BY_KEY = "select ID,RELATIONTYPE_ID,RELATIONTYPE_KEYWORD,PARENT_PARTYID,PARENT_CODE,PARTYID,CODE,NAME,PARTYTYPE_ID,TYPE_LEVEL,ORDER_CODE,IS_LEAF,TYPE_IS_LEAF,IS_INHERIT,IS_CHIEF,EMAIL,CREATE_DATE,MODIFY_DATE from AU_PARTYRELATION where PARTYID=? and PARENT_PARTYID = (select PARTYID from AU_PARTYRELATION where id = ? ) and RELATIONTYPE_ID = ?";    
    
    String SQL_FIND_BY_CODE = "select NAME from AU_PARTYRELATION where CODE=?";
    
    String SQL_FIND_PARTYRELATION = " select ID,RELATIONTYPE_ID,RELATIONTYPE_KEYWORD,PARENT_PARTYID,PARENT_CODE,PARTYID,CODE,NAME,PARTYTYPE_ID,TYPE_LEVEL,ORDER_CODE,IS_LEAF,TYPE_IS_LEAF,IS_INHERIT,IS_CHIEF,EMAIL,CREATE_DATE,MODIFY_DATE from AU_PARTYRELATION ";
    
    String SQL_SIMPLEQUERY_SQL="select b.name relationtype_name,a.id,a.relationtype_id,a.relationtype_keyword,a.parent_partyid,a.parent_code,a.partyid,a.code,a.name,a.partytype_id,a.type_level,a.order_code,a.is_leaf,a.type_is_leaf,a.is_inherit,a.is_chief,a.email,a.create_date,a.modify_date from AU_PARTYRELATION a,au_partyrelationtype b where b.id=a.relationtype_id ";
    /*--SQL定义引用   end--*/
    
    //信息字符串
    String DEFAULT_MSG_ERROR_STR = "Error in populate vo from request.";
    String PROJECT_VALUE = "wy";
    String REQUEST_WRITE_BACK_FORM_VALUES = "writeBackFormValues";
}

