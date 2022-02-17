package venus.id;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class UUIDUtils {

    /**
     * 产生唯一的字符串
     * @return
     */
    public static String getUUid() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

    /*
     * 返回使用ThreadLocalRandom的UUID，比默认的UUID性能更优
     */
    public static UUID fastUUID() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return new UUID(random.nextLong(), random.nextLong());
    }
}
