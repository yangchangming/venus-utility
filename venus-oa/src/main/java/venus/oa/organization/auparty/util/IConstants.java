package venus.oa.organization.auparty.util;

/**
 * @author maxiao
 *
 */
public interface IConstants {
    //OID定义引用
    String OID = "AU_PARTY_ID";
    
    //BS定义引用
    String BS_KEY = "auparty_bs";
    
    //DAO定义引用
    String DAO = "auparty_dao"; 
    
    //团体关系表名
    String TABLENAME="AU_PARTYRELATION";
    //父团体编码的列名
    String COLUMNNAME="CODE";



    String MESSAGE_AGENT_ERROR = "common/common_error";

    String FORWARD_LIST_PAGE_KEY = "authority/au/aurole/listAuRole";

    String FORWARD_TO_QUERY_ALL_KEY = "queryAll";
    String FORWARD_UPDATE_KEY = "authority/au/aurole/insertAuRole.jsp?isModify=1";
    String FORWARD_ADD_KEY = "authority/org/auparty/insert";
    String FORWARD_NEW_KEY = "insertPage";
    String FORWARD_LEFT_KEY = "authority/org/auparty/leftTree";
    String FORWARD_DETAIL_KEY = "authority/au/aurole/detailAuRole";
    String FORWARD_INITROOT_KEY = "authority/org/auparty/initroot";
    String FORWARD_RELATIONTREE_KEY = "relationtreePage";    
    String TYPE_VALUE = "typeId";
    String PROJECT_VALUE = "wy";
    String REQUEST_BEAN_VALUE = "bean";
    String REQUEST_LIST_VALUE = "list";
    String PARTYTYPEBS_KEY = "aupartytype_bs";
    String REQUEST_PARENT_PARTYTYPE = "beanPARENT_PARTYTYPE_KEY";
    String REQUEST_WRITE_BACK_FORM_VALUES = "writeBackFormValues";
    String FORWARD_EMP_KEY = "authority/au/auuser/chooseAuUser";
    String FORWARD_DETAIL_FORPARTY_KEY = "authority/au/auvisitor/detailParty";
    String FORWARD_USER_LIST_KEY = "userRef";
    
    //SQL定义引用
    String INSERT_SQL_AU_PARTY = "insert into AU_PARTY(id,partytype_id,partytype_keyword,is_inherit,is_real,name,email,enable_status,enable_date,create_date,modify_date,owner_org,remark) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
    String UPDATE_SQL_AU_PARTY="update AU_PARTY set NAME=?, EMAIL=?, MODIFY_DATE=?,owner_org=?,remark=? where id=? ";
    String QUERY_SQL_AU_PARTYTYPE_KEYWORD="select keyword from AU_PARTYTYPE ";
    String QUERY_SQL_AU_PARTY="select id,partytype_id,partytype_keyword,is_inherit,is_real,name,email,enable_status,enable_date,create_date,modify_date,owner_org,remark from AU_PARTY ";
    String QUERY_SQL_AU_PARTYRELATION="select id,relationtype_id,relationtype_keyword,parent_partyid,parent_code,partyid,code,name,partytype_id,type_level,order_code,is_leaf,type_is_leaf,is_inherit,is_chief,email,create_date,modify_date from AU_PARTYRELATION ";
    String UPDATE_SQL_AU_PARTYRELATION="update AU_PARTYRELATION set NAME=?, EMAIL=?, MODIFY_DATE=? where id=? ";
    String DISABLE_SQL_AU_PARTY="update AU_PARTY set ENABLE_STATUS=?, MODIFY_DATE=? where id=? ";
    String DELETE_SQL__AU_PARTYRELATION="delete from AU_PARTYRELATION ";
    String UPDATE_SQL_AU_PARTYRELATION_IS_LEAF="update AU_PARTYRELATION set IS_LEAF=?, TYPE_IS_LEAF=?, MODIFY_DATE=? where CODE=? ";
    String SQL_DELETE_BY_ID = "delete from AU_PARTY where ID=?";
    
    String SUB_QUERY=" (select a.id,a.partytype_id,b.name partyname,a.partytype_keyword,a.is_inherit,a.is_real,a.name,a.email,a.enable_status,a.enable_date,a.create_date,a.modify_date,a.owner_org,a.remark from AU_PARTY a,AU_PARTYTYPE b where a.partytype_id=b.id) sub_query ";
   // String SQL_QUERY_PERSON_COUNT="select count(*) from AU_PARTY a,AU_PARTYTYPE b where a.partytype_id=b.id and a.enable_status='1' and b.keyword='1' and a.is_real='1'";
    //String SQL_QUERY_PERSON="select a.id,a.partytype_id,b.name partyname,a.partytype_keyword,a.is_inherit,a.is_real,a.name,a.email,a.enable_status,a.enable_date,a.create_date,a.modify_date,a.owner_org,a.remark from AU_PARTY a,AU_PARTYTYPE b where a.partytype_id=b.id and a.enable_status='1' and b.keyword='1' and a.is_real='1'";
    
    String SQL_QUERY_PERSON_COUNT="select count(distinct a.id) from AU_PARTY a,AU_PARTYTYPE b,au_partyrelation d where a.partytype_id=b.id and a.id = d.partyid and a.enable_status='1' and b.keyword='1' and a.is_real='1'";
    String SQL_QUERY_PERSON="select distinct a.id,a.partytype_id,b.name partyname,a.partytype_keyword,a.is_inherit,a.is_real,a.name,a.email,a.enable_status,a.enable_date,a.create_date,a.modify_date,a.owner_org,a.remark from AU_PARTY a,AU_PARTYTYPE b,au_partyrelation d where a.partytype_id=b.id and a.id = d.partyid and a.enable_status='1' and b.keyword='1' and a.is_real='1' ";    
    
    String QUERY_SQL_ALL="select * from " + SUB_QUERY;
    String GETRECORDCOUNT_SQL = "select count(id) from "+ SUB_QUERY;
    
    //复杂嵌套查询已修改成连接查询，modify by:ganshuo
    String QUERY_SQL_ALL_AU_PARTYRELATION="select a.*, b.name parent_partyname,c.name relationtype_name from AU_PARTYRELATION a left outer join AU_PARTY b on a.parent_partyid=b.id inner join AU_PARTYRELATIONTYPE c on a.relationtype_id=c.id where a.partyid=? order by a.code";		
    //连接查询修改成非问号形式，modify by:liuguohua
    String QUERY_SQL_ALL_AU_PARTYRELATION_BEGIN="select a.*, b.name parent_partyname,c.name relationtype_name from AU_PARTYRELATION a left outer join AU_PARTY b on a.parent_partyid=b.id inner join AU_PARTYRELATIONTYPE c on a.relationtype_id=c.id where a.partyid='";
    //连接查询修改成非问号形式，modify by:liuguohua
    String QUERY_SQL_ALL_AU_PARTYRELATION_END="' order by a.code";
    
    //信息字符串
    String DEFAULT_MSG_ERROR_STR = "Error in populate vo from request.";
}

