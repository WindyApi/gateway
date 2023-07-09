package top.whiteleaf03.api.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ResponseResult {

    /**
     * 状态码
     * status:0   success
     * status:1   auth failed
     * status:3   refuse
     * status:5   error
     */
    @JsonProperty(index = 1)
    private Integer status;

    /**
     * 提示信息
     */
    @JsonProperty(index = 2)
    private String msg;

    /**
     * 数据
     */
    @JsonProperty(index = 3)
    private Object data;

    public ResponseResult(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public ResponseResult(Integer status, Object data) {
        this.status = status;
        this.data = data;
    }

    public ResponseResult(Integer status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 成功访问
     */
    public static ResponseResult success() {
        return new ResponseResult(0, "OK");
    }

    /**
     * 成功访问，并返回数据
     */
    public static ResponseResult success(Object data) {
        return new ResponseResult(0, "OK", data);
    }

    /**
     * 验证失败
     */
    public static ResponseResult authFailed(String msg) {
        return new ResponseResult(1, msg);
    }

    /**
     * 拒绝访问
     */
    public static ResponseResult refuse(String msg) {
        return new ResponseResult(3, msg);
    }

    /**
     * 访问错误
     */
    public static ResponseResult error() {
        return new ResponseResult(5, "ERROR");
    }

    /**
     * 访问错误
     */
    public static ResponseResult error(String msg) {
        return new ResponseResult(5, msg);
    }
}
