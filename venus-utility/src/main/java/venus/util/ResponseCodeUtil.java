package venus.util;

/**
 * @author leronknow <virgil.leehong@gmail.com>
 * @since 2020-05-06 17:14
 */
/**
 * 定义返回code，供业务系统做相应的业务判断
 */
public enum ResponseCodeUtil {

    SUCCESS(0, "请求成功"),
    FAIL(-1, "其它异常");

    private int code;
    private String msg;

    ResponseCodeUtil(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
