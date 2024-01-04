package com.nft.common.exception;

import com.nft.common.APIException;
import com.nft.common.ErrorCode;
import com.nft.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(APIException.class)
    public Result APIException(APIException e) {
        log.error("APIException: " + e.getMessage(), e);
        return Result.error(e.getMessage());
    }
    @ExceptionHandler(BusinessException.class)
    public Result businessExceptionHandler(BusinessException e) {
        log.error("businessException: " + e.getMessage(), e);
        return Result.error(e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public Result runtimeExceptionHandler(RuntimeException e) {
        log.error("runtimeException", e);
        return Result.buildResult(String.valueOf(ErrorCode.SYSTEM_ERROR), e.getMessage());
    }
}
