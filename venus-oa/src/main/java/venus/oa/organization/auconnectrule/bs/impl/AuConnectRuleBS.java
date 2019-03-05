package venus.oa.organization.auconnectrule.bs.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import venus.oa.organization.auconnectrule.bs.IAuConnectRuleBS;
import venus.oa.organization.auconnectrule.dao.IAuConnectRuleDao;
import venus.oa.organization.auconnectrule.util.IConstants;
import venus.oa.organization.auconnectrule.vo.AuConnectRuleVo;
import venus.frames.base.bs.BaseBusinessService;
import venus.frames.base.exception.BaseApplicationException;
//import venus.frames.mainframe.log.ILog;
//import venus.frames.mainframe.log.LogMgr;
import venus.pub.lang.OID;

import java.util.List;

/**
 * 团体连接规则BS
 * @author wumingqiang
 *
 */
@Service
public class AuConnectRuleBS extends BaseBusinessService implements IAuConnectRuleBS, IConstants {

    @Autowired
    private IAuConnectRuleDao auConnectRuleDao;

    /**
     * 查询所有
     * @param no
     * @param size
     * @param orderStr
     * @return
     */
    public List queryAll(int no, int size, String orderStr) {
        return auConnectRuleDao.queryAll(no, size, orderStr);
    }

    /**
     * 按条件查询,返回LIST
     * @param no
     * @param size
     * @param orderStr
     * @param objVo
     * @return
     */
    public List simpleQuery(int no, int size, String orderStr, Object objVo) {
        return auConnectRuleDao.simpleQuery(no, size, orderStr, objVo);
    }

    /**
     * 删除
     * @param id
     * @return
     */
    public int delete(String id) {
        return auConnectRuleDao.delete(id);
    }

    /**
     * 获得记录数
     * @return
     */
    public int getRecordCount() {
        return auConnectRuleDao.getRecordCount();
    }


    /**
     * 按条件获得记录数
     * @param queryCondition
     * @return
     */
    public int getRecordCount(String queryCondition) {
        return auConnectRuleDao.getRecordCount(queryCondition);
    }


    /**
     *  添加
     *
     * @return
     */
    public OID insert(Object objVo) {
        List list = auConnectRuleDao.queryByName(objVo);
        if (list.size()>0) {
//            log.error(venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Groups_to_connect_the_name_to_repeat_the_rules_please_re_edit_")+((AuConnectRuleVo) objVo).getName());
            throw new BaseApplicationException(venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Groups_to_connect_the_name_to_repeat_the_rules_please_re_edit"));
        }
        return auConnectRuleDao.insert(objVo);
    }

    /**
     * 查找
     * @param id
     * @return
     */
    public Object find(String id) {
        return auConnectRuleDao.find(id);
    }

    /**
     * 更新
     * @param objVo
     * @return
     */
    public int update(Object objVo) {
        List list = auConnectRuleDao.queryByName(objVo);
        if (list.size()>1) {
//            log.error(venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Groups_to_connect_the_name_to_repeat_the_rules_please_re_edit_")+((AuConnectRuleVo) objVo).getName());
            throw new BaseApplicationException(venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Groups_to_connect_the_name_to_repeat_the_rules_please_re_edit"));
        }
        else if (list.size()==1){
            AuConnectRuleVo vo = (AuConnectRuleVo)list.get(0);
            if (!vo.getId().equals(((AuConnectRuleVo) objVo).getId())) {
//                log.error(venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Groups_to_connect_the_name_to_repeat_the_rules_please_re_edit_")+((AuConnectRuleVo) objVo).getName());
                throw new BaseApplicationException(venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Groups_to_connect_the_name_to_repeat_the_rules_please_re_edit"));
            }
        }
        return auConnectRuleDao.update(objVo);
    }

    /**
     *按条件查询获取记录数
     * @param m
     * @param n
     * @return
     */
    public int getRecordCount(String m, String n) {
        return auConnectRuleDao.getRecordCount(m, n);
    }
    
    /**
     * 
     * 功能: 根据团体关系类型\父团体类型\子团体类型查询
     *
     * @param objVo
     * @return
     */
    public List queryByType(Object objVo) {
        return auConnectRuleDao.queryByType(objVo);
    }
	
}

