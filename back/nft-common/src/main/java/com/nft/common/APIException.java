package com.nft.common;

import lombok.Getter;

@Getter
public class APIException extends RuntimeException {
    private int code;
    private String msg;

    // 手动设置异常
    public APIException(Constants.ResponseCode statusCode, String message) {
        // message用于用户设置抛出错误详情，例如：当前价格-5，小于0
        super(message);
        // 状态码
        this.code = Integer.parseInt(statusCode.getCode());
        // 状态码配套的msg
        this.msg = statusCode.getInfo();
    }

    // 默认异常使用APP_ERROR状态码
    public APIException(String message) {
        super(message);
        this.code = Integer.parseInt(Constants.ResponseCode.NO_UPDATE.getCode());
        this.msg = Constants.ResponseCode.NO_UPDATE.getInfo();
    }

}
