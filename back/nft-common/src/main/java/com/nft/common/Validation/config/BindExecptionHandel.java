package com.nft.common.Validation.config;

import com.nft.common.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@RestControllerAdvice
public class BindExecptionHandel {
    @ExceptionHandler(BindException.class)
    public ResponseEntity<Result> handle(BindException e) {
        List<FieldError> fieldErrors = e.getFieldErrors();
        Map errors = new LinkedHashMap<>();
        for (FieldError error : fieldErrors) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return new ResponseEntity<>(new Result("400", errors.toString()), HttpStatus.BAD_REQUEST);
    }
}
