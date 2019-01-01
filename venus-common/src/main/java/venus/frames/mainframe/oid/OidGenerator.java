package venus.frames.mainframe.oid;

import venus.frames.mainframe.log.LogMgr;
import venus.pub.lang.LangException;
import venus.pub.lang.OID;

import java.io.Serializable;

/**
 * OID生成器
 * 
 * @author 张文韬
 */
public class OidGenerator implements Serializable  {

	/**
	 * 目前该表的OID最大值
	 */
	private OID m_oidMax = null;

	/**
	 * 构造函数
	 * 根据表当前最大的OID构建OID生成器
	 * @param oidMax 当前表中数值最大的OID对象
	 * @roseuid 3F94F27A02B7
	 */
	public OidGenerator(OID oidMax) {
		this.m_oidMax = oidMax;
	}

	/**
	 * 得到系统ID基数
	 * @return long 系统ID的基数
	 * @roseuid 3F94D5A70382
	 */
	public long getSysPrefix() {
		return OidMgr.getSysPrefix();
	}

	/**
	 * 得到该表的OID基数，取前8位组成的一个长整形数
	 * 
	 * -----------------OID定义----------------------------------------------
	 * OID: 系统ID4（1000-9222）_表ID4_序列号11。共用十九位数字表示一个主键。
	 * @return long 表OID的前八位组成的长整形数
	 * @roseuid 3F94D5BD0372
	 */
	public long getTablePrefix() {
		//处理空指针异常
		if (this.m_oidMax == null) {
			LogMgr.getLogger(this).error("OidGenerator中的getTablePrefix方法出现OID空指针异常！");
			return -1;			
		}
		return this.m_oidMax.longValue()/100000000000L*100000000000L;
	}

	/**
	 * 请求获取OID<br>
	 * 先取到当前OID生成器中OID的最大值，
	 * 然后在此基础上加一获得新的OID
	 * @return venus.pub.lang.OID
	 * @roseuid 3F94F1170015
	 */
	public synchronized OID requestOID() {
		OID res = null;
		//在原先OID最大值得基础上加一得到新的最大OID值
		try {
			res = new OID(getMaxOID().longValue() + 1);
		} catch (LangException e) {
			LogMgr.getLogger(this).error("OidGenerator中的requestOID方法,请求获取OID值失败！",e);
			return null;
		} catch (NullPointerException e) {
			LogMgr.getLogger(this).error("OidGenerator中的requestOID方法,请求获取OID值失败！",e);
			return null;
		}
		//置换当前OID的最大值
		this.m_oidMax = res;
		return res;
	}

	/**
	 * 得到当前OID生成器中OID的最大值
	 * @return venus.pub.lang.OID 当前OID生成器中的最大OID
	 * @roseuid 3F94F1420053
	 */
	public OID getMaxOID() {
		return this.m_oidMax;
	}
}
