/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.frames.mainframe.oid;

import venus.VenusHelper;
import venus.frames.mainframe.log.LogMgr;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

/**
 * @author changming.y
 * 
 */
public class SYSIDClusterUtil {

	public final static String CLUSTER_PROPERTIES = "/sysid-cluster.properties";

	/**
	 * 在集群环境下，各节点从统一文件目录获取不通的SYSID
	 * 如果不存在sysid-cluster.properties，则返回null，表示非集群环境，默认从conf.xml中获取
	 * 
	 * @return
	 */
	public static String getSID4Cluster() {
		List<String> ipList = getIPs();
		Properties properties = new Properties();
		try {
			InputStream is = new FileInputStream(VenusHelper.getUploadPath()
					+ CLUSTER_PROPERTIES);
			properties.load(is);
			is.close();
			Enumeration enu = properties.propertyNames();
			while (enu.hasMoreElements()) {
				String key = (String) enu.nextElement();
				if ( ipList.contains(key) ){
					LogMgr.getLogger(SYSIDClusterUtil.class.getName()).info(
							"SYSID: " + properties.getProperty(key));
					return properties.getProperty(key);
				}
			}
			return null;
		} catch (IOException e) {
			LogMgr.getLogger(SYSIDClusterUtil.class.getName()).info(
					"no sysid cluster file!");
			return null;
		}
	}

	/**
	 * 获取服务器的IP列表
	 */
	private static List getIPs() {
		Enumeration<NetworkInterface> netInterfaces = null;
		ArrayList<String> ipList = new ArrayList();
		try {
			netInterfaces = NetworkInterface.getNetworkInterfaces();
			while (netInterfaces.hasMoreElements()) {
				NetworkInterface ni = netInterfaces.nextElement();
				Enumeration<InetAddress> ips = ni.getInetAddresses();
				while (ips.hasMoreElements()) {
					ipList.add(ips.nextElement().getHostAddress());
				}
			}
			return ipList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}