package com.nft.common.exception;


import com.nft.common.ErrorCode;

/**
 * 自定义异常类
 *
 * @author qimu
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = -1463203495162802463L;
    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }

    public int getCode() {
        return code;
    }
}
