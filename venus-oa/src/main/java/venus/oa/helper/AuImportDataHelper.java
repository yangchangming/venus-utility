package venus.oa.helper;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import venus.frames.base.exception.BaseApplicationException;
import venus.oa.authority.auauthorize.bs.IAuAuthorizeBS;
import venus.oa.authority.auauthorize.vo.AuAuthorizeVo;
import venus.oa.authority.aufunctree.bs.IAuFunctreeBs;
import venus.oa.authority.aufunctree.vo.AuFunctreeVo;
import venus.oa.authority.auvisitor.bs.IAuVisitorBS;
import venus.oa.authority.auvisitor.vo.AuVisitorVo;
import venus.oa.login.vo.LoginSessionVo;
import venus.oa.organization.auparty.bs.IAuPartyBs;
import venus.oa.organization.auparty.vo.PartyVo;
import venus.oa.util.DateTools;
import venus.oa.util.GlobalConstants;
import venus.oa.util.ProjTools;
import venus.springsupport.BeanFactoryHelper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AuImportDataHelper {

    private static Logger logger=Logger.getLogger(AuImportDataHelper.class);

    /**
     * 增加菜单
     * @param vo
     * @param parent_keyword
     */
    public static void insertFunctree(AuFunctreeVo vo, String parent_keyword){

        if(vo.getParent_code().equals("")){
            getParentCodeByKeywordFromAU_FUNCTREE(vo, parent_keyword);
        }

        IAuFunctreeBs bs = getAuFunctreeBs();
//      第一步：判断同一父节点同一级别内的名称是否重复
        String tName = vo.getName();
        String pCode = vo.getParent_code();
        //int pLen = vo.getParent_code().length();
        List myList = bs.queryByCondition("NAME='" + tName + "' AND TOTAL_CODE LIKE'" + pCode + "___'");
        if(myList!=null && myList.size()>0) {
            //return MessageAgent.sendErrorMessage(request, venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Name_repetition_try_other_names"), MessageStyle.ALERT_AND_BACK);
            //名称重复，请尝试其它名称
            logger.error(tName+"  名称重复，请尝试其它名称");
            throw new BaseApplicationException(tName+"  名称重复，请尝试其它名称") ;
        }
        //第二步：插入记录
        //在BS里：判断父节点是否为叶子节点，如果是则将它改为非叶子节点
        //在DAO里：生成主键ID和排序编号SEQ_ID
        vo.setCreate_date(DateTools.getSysTimestamp());  //打创建时间
        bs.insert(vo);  //插入单条记录
    }
    //查询AU_FUNCTREE表 ， 通过keyword获取Parent_code
    private static void getParentCodeByKeywordFromAU_FUNCTREE(AuFunctreeVo vo, String keyword){
        String sql="select TOTAL_CODE from AU_FUNCTREE where keyword='"+keyword+"' ";
        RowMapper rowMapper=new RowMapper(){

            public Object mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("TOTAL_CODE");  //To change body of implemented methods use File | Settings | File Templates.
            }
        };
        List lParty = ProjTools.getCommonBsInstance().doQuery(sql, rowMapper) ;
        if(lParty.size()!=0){
            vo.setParent_code((String)lParty.get(0));
        }
    }

    /**
     *  增加角色
     * @param vo 只需要对name进行赋值即可
     * @param parentRelId   角色在AU_PARTYRELATION表中对应的ID
     */
   public static void insertAuRole(PartyVo vo, String parentRelId){

       vo.setCreate_date(DateTools.getSysTimestamp());//打创建时间戳
       vo.setPartytype_id(GlobalConstants.getPartyType_role());//团体类型表的主键ID－角色

       //添加新的团体和团体关系
       String relType = GlobalConstants.getRelaType_role();//团体关系类型－角色关系
       getAuPartyBs().addPartyAndRelation(vo, parentRelId, relType);
   }


    /**
     * 角色关联用户
     * @param sql  通过该语句可以搜索出用户的party_id
     * @param parentRelId   角色在AU_PARTYRELATION表中对应的ID
     */
    public static void addMultiRelationAuRoleBySql(String sql, String parentRelId){
        String relType = GlobalConstants.getRelaType_role();//团体关系类型－行政关系
        RowMapper rowMapper=new RowMapper(){

            public Object mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("party_id");  //To change body of implemented methods use File | Settings | File Templates.
            }
        };

        List partyIdList  = ProjTools.getCommonBsInstance().doQuery(sql, rowMapper) ;

        for (int i=0; i<partyIdList.size(); i++) {
            OrgHelper.addPartyRelation((String)partyIdList.get(i), parentRelId, relType);
        }
    }

    /**
     *  角色关联用户
     * @param partyIdList  用户的party_id 集合
     * @param parentRelId  角色在AU_PARTYRELATION表中对应的ID
     */
    public static void  addMultiRelationAuRoleByList(List<String> partyIdList, String parentRelId){
        String relType = GlobalConstants.getRelaType_role();//团体关系类型－行政关系
        for (int i=0; i<partyIdList.size(); i++) {
            OrgHelper.addPartyRelation((String)partyIdList.get(i), parentRelId, relType);
        }
    }

    /**
     * 功能菜单授权,字段级数据权限,记录级数据权限(授权页面为 function.jsp)
     * @param relId 被授权对象的关系ID (AU_PARTYRELATION的ID),例如 1099100800000000022
     * @param pType 被授权对象的类型ID (AU_PARTYTYPE的ID),例如1099100500000000005
     * @param sIds  功能菜单资源的ID(AU_FUNCTREE的ID),例如1099101300000000022,1099101300000000023,1099101300000000024,1099101300000000014,5199101300000000005,5199101300000000006
     * @param sCodes  功能菜单资源的Code(AU_FUNCTREE的total_code) ,例如101003001,101003002,101003003,101002004001,101002012001,101002012002
     * @param sTypes   功能菜单的类型,GlobalConstants中的sTypeMenu=0变量的值  ,例如0,0,0,0,0,0
     * @param sStatus  功能菜单授权状态（拒绝0，允许1 默认设置为2） ,例如1,1,0,1,1,1
     * @param sAccess  功能菜单权限种类（用素数表示，例如：可授权2, 默认为1）  ,例如2,1,2,1,1,1
     *  请注意 sStatus=2,sAccess=1 这种情况避免出现
     */
    public static void saveAuByVisitor(String relId, String pType, String sIds, String sCodes, String sTypes, String sStatus, String sAccess){
        //将团体关系id转化成访问者vo

        IAuVisitorBS visiBs = (IAuVisitorBS)BeanFactoryHelper.getBean("auVisitorBS");
        AuVisitorVo visiVo = visiBs.queryByRelationId(relId, pType);

        //分析表单值
        String[] ids = sIds.length()>0 ? sIds.split(",") : new String[0];
        String[] codes = sCodes.length()>0 ? sCodes.split(",") : new String[0];
        String[] types = sTypes.length()>0 ? sTypes.split(",") : new String[0];
        String[] status = sStatus.length()>0 ? sStatus.split(",") : new String[0];
        String[] access = sAccess.length()>0 ? sAccess.split(",") : new String[0];
        if(ids.length!=codes.length || codes.length!=types.length || types.length!=status.length || status.length!=access.length){
           logger.equals("功能菜单授权saveAuByVisitor方法参数不正确,请检查 relId="+relId+"  sIds="+sIds+" sCodes="+sCodes+"  sTypes="+sTypes+"  sStatus="+sStatus+"  sAccess="+sAccess );
            throw new BaseApplicationException("功能菜单授权saveAuByVisitor方法参数不正确,请检查 relId="+relId+"  sIds="+sIds+" sCodes="+sCodes+"  sTypes="+sTypes+"  sStatus="+sStatus+"  sAccess="+sAccess);
        }

        List voList = new ArrayList();
        for(int i=0; i<ids.length; i++ ) {
            AuAuthorizeVo vo = new AuAuthorizeVo();
            vo.setAccess_type(access[i]);
            vo.setIs_append("0");//没有附加数据
            vo.setVisitor_code(visiVo.getCode());
            vo.setVisitor_id(visiVo.getId());
            vo.setVisitor_type(visiVo.getVisitor_type());
            vo.setAuthorize_status(status[i]);
            vo.setResource_code(codes[i]);
            vo.setResource_id(ids[i]);
            vo.setResource_type(types[i]);
            vo.setCreate_date(DateTools.getSysTimestamp());  //打创建时间,IP戳
            voList.add(vo);
        }

        getAuAuthorizeBS().saveAu(visiVo.getId(), voList, GlobalConstants.getResType_menu(), getLoginSessionVo());//保存授权结果
    }

    /**
     * 机构数据权限授权, 角色数据权限，代理数据权限(授权页面为 deeptree4Au.jsp)
     * @param relId   被授权对象的关系ID (AU_PARTYRELATION的ID),例如 1099100800000000022
     * @param pType   被授权对象的类型ID (AU_PARTYTYPE的ID),例如1099100500000000005
     * @param addCodes    机构数据的Code(AU_PARTYRELATION的code) ,例如10991004000000000010000100001,1099100400000000001000010000300001
     * @param names   机构数据的名称(AU_PARTYRELATION的name),例如用友北京分公司,市场部
     * @param types   机构数据的类型,GlobalConstants中的sTypeOrga=5变量的值  ,例如5,5
     */
    public static void saveOrgAuByRelId(String relId, String pType, String addCodes, String names, String types){
        if(addCodes==null || relId==null || types==null || names==null || pType==null) {
            //缺少参数
            logger.equals("机构数据权限授权saveOrgAuByRelId方法缺少参数,请检查 relId="+relId+"  pType="+pType+" addCodes="+addCodes+"  names="+names+"  types="+types);
            throw new BaseApplicationException("机构数据权限授权saveOrgAuByRelId方法缺少参数,请检查 relId="+relId+"  pType="+pType+" addCodes="+addCodes+"  names="+names+"  types="+types);
        }

        //分析参数，首先取到所有要添加的编号列表
        String[] addCodeArray = addCodes.length()>0 ? addCodes.split(",") : new String[0];
        String[] sTypes = types.length()>0 ? types.split(",") : new String[0];
        String[] sNames = names.length()>0 ? names.split(",") : new String[0];
        if(addCodeArray.length!=sTypes.length || sTypes.length!=sNames.length){
            logger.equals("机构数据权限授权saveOrgAuByRelId方法参数不正确,请检查  addCodes="+addCodes+"  names="+names+"  types="+types );
            throw new BaseApplicationException("机构数据权限授权saveOrgAuByRelId方法参数不正确,请检查 addCodes="+addCodes+"  names="+names+"  types="+types);
        }

        //将团体关系id转化成访问者vo
        IAuVisitorBS visiBs = (IAuVisitorBS)BeanFactoryHelper.getBean("auVisitorBS");
        AuVisitorVo visiVo = visiBs.queryByRelationId(relId, pType);

        //接着取到所有真正取消打勾的节点的编号（即不包括后来又勾上的）
        String delCodeArray[] = new String[0];

        //分析并保存针对组织机构的授权结果
        getAuAuthorizeBS().saveOrgAu(visiVo.getId(), visiVo.getCode(), visiVo.getVisitor_type(), addCodeArray, delCodeArray, sNames, sTypes, getLoginSessionVo());
    }

    /**
     * 组装sessionVo对象
     * @return
     */
    private static  LoginSessionVo getLoginSessionVo(){
        LoginSessionVo sessionVo = new LoginSessionVo();//权限上下文
        sessionVo.setName("超级管理员");   //   AU_PARTY中的Name
        sessionVo.setParty_id("1099100700000000002"); // AU_PARTY中的ID

        return sessionVo;
    }

    private static IAuAuthorizeBS getAuAuthorizeBS(){
        return (IAuAuthorizeBS)BeanFactoryHelper.getBean("auAuthorizeBS");
    }

    private static IAuFunctreeBs getAuFunctreeBs() {
        return (IAuFunctreeBs)BeanFactoryHelper.getBean("auFunctreeBs");
    }

    private static IAuPartyBs getAuPartyBs() {
        return (IAuPartyBs)BeanFactoryHelper.getBean("auPartyBs");
    }

}
