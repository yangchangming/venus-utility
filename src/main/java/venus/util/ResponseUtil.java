package venus.util;

import java.io.Serializable;

/**
 * @author leronknow <virgil.leehong@gmail.com>
 * @since 2020-05-06 17:13
 * 返回数据Util类，
 * code: 返回码
 * message: 返回提示消息
 * data: 返回具体的数据
 * @param <T>
 *
 */
public class ResponseUtil<T> implements Serializable {
    private int code;
    private String message;
    private T data;

    public ResponseUtil() {}

    public ResponseUtil(ResponseCodeUtil codeUtil) {
        this.code = codeUtil.getCode();
        this.message = codeUtil.getMsg();
    }

    public ResponseUtil(ResponseCodeUtil codeUtil, String message) {
        this.code = codeUtil.getCode();
        this.message = message;
    }

    public ResponseUtil(ResponseCodeUtil codeUtil, T data) {
        this.code = codeUtil.getCode();
        this.data = data;
        this.message = codeUtil.getMsg();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String msg) {
        this.message = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
