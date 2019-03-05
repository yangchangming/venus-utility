package venus.oa.authority.aufunctree.util;

import venus.frames.i18n.util.LocaleHolder;
import venus.oa.util.common.dao.ICommonDao;
import venus.springsupport.BeanFactoryHelper;

import java.util.HashSet;
import java.util.Set;

/**
 * @author changming.Y <changming.yang.ah@gmail.com>
 *
 */
public class AuFunctreeConstants {
    public static String clientLang = null;
    private static Set langSet = new HashSet();
    public static void reset(){
        clientLang = null;
    }
    private String i18n;
    public AuFunctreeConstants(){
        //优先级：数据库支持第一优先；clientLang设置第二优先；浏览器语言第三优先。
        String localLang = null!=clientLang?clientLang:LocaleHolder.getLocale().getLanguage();
        if(null!=localLang&&!"zh".equals(localLang)){
            boolean wetherExt = langSet.contains(localLang);
            if(!wetherExt){
                try{
                    ((ICommonDao)BeanFactoryHelper.getBean("commonDao")).doQueryForInt("select count(id) from AU_FUNCTREE_"+localLang);
                    langSet.add(localLang);
                    wetherExt = true;
                }catch(Exception e){
                    wetherExt = false;
                }
            }            
            if(wetherExt)
                i18n = "_"+localLang;
            else
                i18n = "";
        }else{
            i18n = "";
        }
        
        SQL_INSERT = "insert into AU_FUNCTREE"+i18n+" ( ID, KEYWORD, TYPE, CODE, PARENT_CODE, TOTAL_CODE, NAME, HOT_KEY, HELP, URL, IS_LEAF, TYPE_IS_LEAF, ORDER_CODE, SYSTEM_ID, CREATE_DATE, TREE_LEVEL,IS_SSL,IS_PUBLIC,TREE_ID) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        SQL_DELETE_BY_ID = "delete from AU_FUNCTREE"+i18n+" where ID=?";

        SQL_DELETE_MULTI_BY_IDS = "delete from AU_FUNCTREE"+i18n;

        SQL_FIND_BY_ID = "select AU_FUNCTREE"+i18n+".ID, AU_FUNCTREE"+i18n+".KEYWORD, AU_FUNCTREE"+i18n+".TYPE, AU_FUNCTREE"+i18n+".CODE, AU_FUNCTREE"+i18n+".PARENT_CODE, AU_FUNCTREE"+i18n+".TOTAL_CODE, AU_FUNCTREE"+i18n+".NAME, AU_FUNCTREE"+i18n+".HOT_KEY, AU_FUNCTREE"+i18n+".HELP, AU_FUNCTREE"+i18n+".URL, AU_FUNCTREE"+i18n+".IS_LEAF, AU_FUNCTREE"+i18n+".TYPE_IS_LEAF, AU_FUNCTREE"+i18n+".ORDER_CODE, AU_FUNCTREE"+i18n+".SYSTEM_ID, AU_FUNCTREE"+i18n+".CREATE_DATE, AU_FUNCTREE"+i18n+".MODIFY_DATE, AU_FUNCTREE"+i18n+".TREE_LEVEL,AU_FUNCTREE"+i18n+".IS_SSL,AU_FUNCTREE"+i18n+".IS_PUBLIC from AU_FUNCTREE"+i18n+" where AU_FUNCTREE"+i18n+".ID=?";

        SQL_UPDATE_BY_ID = "update AU_FUNCTREE"+i18n+" set KEYWORD=?, TYPE=?, CODE=?, PARENT_CODE=?, TOTAL_CODE=?, NAME=?, HOT_KEY=?, HELP=?, URL=?, IS_LEAF=?, TYPE_IS_LEAF=?, ORDER_CODE=?, SYSTEM_ID=?, MODIFY_DATE=?,IS_SSL=?,IS_PUBLIC=? where ID=?";

        SQL_COUNT = "select count(*) from AU_FUNCTREE"+i18n;

        SQL_QUERY_ALL = "select AU_FUNCTREE"+i18n+".ID, AU_FUNCTREE"+i18n+".KEYWORD, AU_FUNCTREE"+i18n+".TYPE, AU_FUNCTREE"+i18n+".CODE, AU_FUNCTREE"+i18n+".PARENT_CODE, AU_FUNCTREE"+i18n+".TOTAL_CODE, AU_FUNCTREE"+i18n+".NAME, AU_FUNCTREE"+i18n+".HOT_KEY, AU_FUNCTREE"+i18n+".HELP, AU_FUNCTREE"+i18n+".URL, AU_FUNCTREE"+i18n+".IS_LEAF, AU_FUNCTREE"+i18n+".TYPE_IS_LEAF, AU_FUNCTREE"+i18n+".ORDER_CODE, AU_FUNCTREE"+i18n+".SYSTEM_ID, AU_FUNCTREE"+i18n+".CREATE_DATE, AU_FUNCTREE"+i18n+".MODIFY_DATE, AU_FUNCTREE"+i18n+".TREE_LEVEL,AU_FUNCTREE"+i18n+".IS_SSL,AU_FUNCTREE"+i18n+".IS_PUBLIC from AU_FUNCTREE"+i18n;


        TABLE_NAME = "AU_FUNCTREE"+i18n;

        DEFAULT_DESC_QUERY_WHERE_ENABLE = " WHERE 1=1 ";

        DEFAULT_DESC_ORDER_BY_ID = ""; //" ORDER BY ID DESC ";
    }
    
    public static AuFunctreeConstants getDifferentInstance(){
        return new AuFunctreeConstants();
    }
    //Sql语句
    public  String SQL_INSERT;
    
    public  String SQL_DELETE_BY_ID;
    
    public  String SQL_DELETE_MULTI_BY_IDS;

    public  String SQL_FIND_BY_ID;

    public  String SQL_UPDATE_BY_ID;
    
    public  String SQL_COUNT;
    
    public  String SQL_QUERY_ALL;

    //基本常量
    public  String TABLE_NAME;
    
    public  String DEFAULT_DESC_QUERY_WHERE_ENABLE;
    
    public  String DEFAULT_DESC_ORDER_BY_ID;
    
}
