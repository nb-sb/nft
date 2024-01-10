package com.nft.common.exception;

/**
 * 错误码
 *
 * @author qimu
 */
public enum ErrorCode {

    /**
     * 成功
     */
    SUCCESS(0, "ok"),
    /**
     * params错误
     */
    PARAMS_ERROR(40000, "请求参数错误"),
    /**
     * 非登录错误
     */
    NOT_LOGIN_ERROR(40100, "未登录"),
    /**
     * 无身份验证错误
     */
    NO_AUTH_ERROR(40101, "无权限"),
    /**
     * 未找到错误
     */
    NOT_FOUND_ERROR(40400, "请求数据不存在"),
    /**
     * 禁止错误
     */
    FORBIDDEN_ERROR(40300, "禁止访问"),
    /**
     * 系统错误
     */
    SYSTEM_ERROR(500, "系统内部异常"),
    /**
     * 操作错误
     */
    OPERATION_ERROR(50001, "操作失败");

    /**
     * 状态码
     */
    private final int code;

    /**
     * 信息
     */
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
