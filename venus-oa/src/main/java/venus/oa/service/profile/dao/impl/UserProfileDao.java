package venus.oa.service.profile.dao.impl;

import org.springframework.jdbc.core.RowMapper;
import venus.oa.service.profile.dao.IUserProfileDao;
import venus.oa.service.profile.util.IContants;
import venus.oa.service.profile.vo.UserProfileVo;
import venus.oa.util.DateTools;
import venus.oa.util.VoHelperTools;
import venus.frames.base.dao.BaseTemplateDao;
import venus.frames.mainframe.util.Helper;
import venus.pub.lang.OID;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * @author changming.Y <changming.yang.ah@gmail.com>
 *
 */
public class UserProfileDao extends BaseTemplateDao implements IContants,IUserProfileDao {

    /* (non-Javadoc)
     * @see venus.authority.service.profile.dao.IUserProfileDao#find(java.lang.String, java.lang.String)
     */
    public UserProfileVo find(String partyid, String propertykey) {
        StringBuffer strSql = new StringBuffer();
        strSql.append(QUERY_PROFILE_SQL);
        strSql.append(QUERY_DEFAULT_CONDITON);
        strSql.append(QUERY_AND_CONDITON);
        strSql.append("   PARTYID = '" + partyid + "' ");
        strSql.append(QUERY_AND_CONDITON);
        strSql.append("   PROPERTYKEY = '" + propertykey + "' ");
        return (UserProfileVo) queryForObject(strSql.toString(), new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                UserProfileVo vo = new UserProfileVo();
                VoHelperTools.null2Nothing(vo); //把vo中null值替换为""
                Helper.populate(vo, rs);
                return vo;
            }
        });
    }

    /* (non-Javadoc)
     * @see venus.authority.service.profile.dao.IUserProfileDao#insert(venus.authority.service.profile.vo.UserProfileVo)
     */
    public OID insert(UserProfileVo vo) {
        OID oid = Helper.requestOID(TABLE_NAME); //获得oid
        long id = oid.longValue();
        vo.setId(String.valueOf(id));
        VoHelperTools.null2Nothing(vo); //把vo中null值替换为""
        Timestamp time = DateTools.getSysTimestamp();
        Object[] obj = { vo.getId(), vo.getPropertykey(), vo.getValue(),
                time, time, vo.getPartyid(),
                vo.getPartyname(), vo.getDescription(),vo.getCloumn1(), vo.getEnable(),vo.getPropertytype() };
        updateWithUniformArgType(INSERT_PROFILE_SQL, obj);
        return oid;
    }

    /* (non-Javadoc)
     * @see venus.authority.service.profile.dao.IUserProfileDao#updateValueByVo(venus.authority.service.profile.vo.UserProfileVo)
     */
    public int updateValueByVo(UserProfileVo vo) {
        VoHelperTools.null2Nothing(vo); //把vo中null值替换为""
        Object[] obj = { vo.getValue(), 
                DateTools.getSysTimestamp(), vo.getPartyname(),
                vo.getDescription(),vo.getEnable(), vo.getCloumn1(), vo.getPropertytype(),vo.getPartyid(), vo.getPropertykey()};
        return updateWithUniformArgType(SQL_UPDATE_BY_VO, obj);
    }

    /* (non-Javadoc)
     * @see venus.authority.service.profile.dao.IUserProfileDao#delete(venus.authority.service.profile.vo.UserProfileVo)
     */
    public void delete(UserProfileVo vo) {
        Object[] obj = {vo.getPartyid()};
        //TODO 通用删除解决方案
        updateWithUniformArgType(SQL_DELETE_BY_VO, obj);
    }

}

