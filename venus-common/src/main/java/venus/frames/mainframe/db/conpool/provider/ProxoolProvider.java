package venus.frames.mainframe.db.conpool.provider;

import gap.commons.digest.DigestLoader;
import gap.license.exception.InvalidLicenseException;
import org.logicalcobwebs.proxool.ProxoolDataSource;
import org.logicalcobwebs.proxool.ProxoolException;
import org.logicalcobwebs.proxool.ProxoolFacade;
import venus.frames.mainframe.db.conpool.IConProvider;
import venus.frames.mainframe.util.IConfReader;
import venus.pub.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * The Provider of proxool connection pool, just get a
 * connection Due to the mainframe has it's own dbpool frame, so we give
 * up the default configuration of proxool and use ProxoolDataSource to
 * get a connection.
 *
 * @author changming.y
 */
public class ProxoolProvider implements IConProvider {

    private final static String ALIAS = "UDP-Pool";

    private final static String STATISTICS = "1m,15m,1d";

    private String strConnectURI;

    private String name;

    private String password;

    private int maxActive;

    private String drvName;

    private String alias;

    private ProxoolDataSource proxoolDataSource;

    private int maxActiveTime;

    private int simultaneousBuildThrottle;

    private int prototypeCount;

    private int delayTime;

    private int retryCount;

    //最小连接数
    private int minActive;
    //详细信息设置
    private boolean verbose;
    //如果为true,那么每个被执行的SQL语句将会在执行期被log记录(DEBUG LEVEL).
    private boolean trace;

    /**
     * Get the datasource connection for application system.
     */
    public Connection requestConnection(IConfReader icr) throws SQLException {

        if (proxoolDataSource == null) {
//            try {
//                DigestLoader loader = DigestLoader.getLoader();
//                if (loader.isValid() && Math.random() > 0.4)
//                    chksoon(loader);
//                else if (!loader.isValid())
//                    chksoon(loader);
//            } catch (RuntimeException re) {
//                LogMgr.getLogger("udp.use.connection").error(re.getClass().getName());
//                return null;
//            }

            drvName = icr.readStringAttribute("drvName");
            name = icr.readStringAttribute("usrName");
            password = icr.readStringAttribute("pwd");
            strConnectURI = icr.readStringAttribute("conUrl");
            maxActive = icr.readIntAttribute("maxActive");
            alias = icr.readStringAttribute("Alias");
            maxActiveTime = icr.readIntAttribute("maxActiveTime");
            simultaneousBuildThrottle = icr.readIntAttribute("simultaneousBuildThrottle");
            prototypeCount = icr.readIntAttribute("prototypeCount");
            delayTime = icr.readIntAttribute("delayTime");
            retryCount = icr.readIntAttribute("retryCount");
            minActive = icr.readIntAttribute("minActive");
            verbose = icr.readBooleanAttribute("verbose");
            trace = icr.readBooleanAttribute("trace");

            try {
                Class.forName(drvName);
            } catch (ClassNotFoundException e) {
                throw new SQLException("ProxoolProvider:driver class not fount exception:" + drvName);
            }

            proxoolDataSource = new ProxoolDataSource();
            if (null == alias) {
                proxoolDataSource.setAlias(ALIAS);
            }
            else {
                proxoolDataSource.setAlias(ALIAS + "-" + alias);
            }
            proxoolDataSource.setStatistics(STATISTICS);
            proxoolDataSource.setSimultaneousBuildThrottle(maxActive);
            proxoolDataSource.setDriver(drvName);
            proxoolDataSource.setDriverUrl(strConnectURI);
            proxoolDataSource.setUser(name);
            proxoolDataSource.setPassword(password);
            proxoolDataSource.setMaximumConnectionCount(maxActive);
            if (simultaneousBuildThrottle > 0) {
                proxoolDataSource.setSimultaneousBuildThrottle(simultaneousBuildThrottle);
            }
            if (maxActiveTime >= 60000) {
                proxoolDataSource.setMaximumActiveTime(maxActiveTime);
            }
            if (prototypeCount > 0) {
                proxoolDataSource.setPrototypeCount(prototypeCount);
            }
//            proxoolDataSource.setDelayTime(delayTime);
//            proxoolDataSource.setRetryCount(retryCount);
            if (minActive > 0) {
                proxoolDataSource.setMinimumConnectionCount(minActive);
            }
            proxoolDataSource.setVerbose(verbose);
            proxoolDataSource.setTrace(trace);
        }
        return proxoolDataSource.getConnection();
    }

    /*
     * （非 Javadoc）
     *
     * @see venus.frames.mainframe.db.conpool.IConProvider#reset()
     */
    public void reset() {
        try {
            if (null != proxoolDataSource) {
                ProxoolFacade.removeConnectionPool(proxoolDataSource.getAlias());
                proxoolDataSource = null;
            }
        } catch (ProxoolException e) {
        }
    }

    /**
     * @param loader
     */
    private void chksoon(DigestLoader loader) {

        boolean valid = true;
        try {
            Class cls = loader.findClass();
            Method m = ReflectionUtils.findMethod(cls, "checkLicense",
                    new Class[]{});
            valid = new Boolean(ReflectionUtils.invokeMethod(m, null,
                    new Object[]{}).toString()).booleanValue();
        } catch (RuntimeException e) {
            loader.setValid(false);
            throw e;
        }
        if (!valid) {
            loader.setValid(false);
            throw new InvalidLicenseException();
        } else {
            loader.setValid(true);
        }
    }

}