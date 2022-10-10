package venus.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Response data object, just used to be API
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2020-02-24 10:50
 */
public class Res extends HashMap<String,Object> {
    private static final long serialVersionUID = 1L;
    public static final String R_CODE = "code";
    public static final String R_MSG = "msg";
    public static final String R_SUCCESS = "success";
    public static final String R_DATA = "data";
    public static final int R_ERROR_CODE = 500;
    public static final int R_LOGIN_TIMEOUT = -100; // 登陆超时
    public static final String R_LOGIN_TIMEOUT_MSG = "登陆超时，请重新登陆";

    public Res() {
        put(R_CODE, 200);
    }

    public static Res error() {
        return error(R_ERROR_CODE, "未知异常，请联系管理员");
    }

    public static Res error(String msg) {
        return error(R_ERROR_CODE, msg);
    }

    public static Res error(int code, String msg) {
        Res r = new Res();
        r.put(R_SUCCESS,false);
        r.put(R_CODE, code);
        r.put(R_MSG, msg);
        return r;
    }

    public static Res unLogin() {
        Res r = new Res();
        r.put(R_SUCCESS,false);
        r.put(R_CODE, R_LOGIN_TIMEOUT);
        r.put(R_MSG, R_LOGIN_TIMEOUT_MSG);
        return r;
    }

    public static Res ok(String msg) {
        Res r = new Res();
        r.put(R_MSG, msg);
        return r;
    }

    public static Res ok(Map<String, Object> map) {
        Res r = new Res();
        r.putAll(map);
        return r;
    }

    public static Res ok() {
        return new Res();
    }

    public Res put(Object data) {
        super.put(R_DATA, data);
        return this;
    }

    public Res data(long totalCount, List rows){
        Map<String, Object> data = new HashMap<>();
        data.put("rows", rows);
        data.put("total", totalCount);
        return put(data);
    }

    @Override
    public Res put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    public Res put(boolean success,int code, String msg) {
        super.put(R_SUCCESS, success);
        super.put(R_CODE, code);
        super.put(R_MSG, msg);
        return this;
    }

    public Res put(boolean success,int code, String msg,Object value) {
        super.put(R_SUCCESS, success);
        super.put(R_CODE, code);
        super.put(R_MSG, msg);
        super.put(R_DATA, value);
        return this;
    }
}

