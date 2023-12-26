package com.nft.common.Validation.config;

import com.nft.common.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;


@RestControllerAdvice
public class BindExecptionHandel {

    /**
     * 不加 @RequestBody注解，校验失败抛出的则是 BindException
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<Result> handle(BindException e) {
        List<FieldError> fieldErrors = e.getFieldErrors();
        Map errors = new LinkedHashMap<>();
        for (FieldError error : fieldErrors) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return new ResponseEntity<>(new Result("400", errors.toString()), HttpStatus.BAD_REQUEST);
    }
    /**
     * @RequestBody 上校验失败后抛出的异常是 MethodArgumentNotValidException 异常。
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Result> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getFieldErrors();
        Map errors = new LinkedHashMap<>();
        for (FieldError error : fieldErrors) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return new ResponseEntity<>(new Result("400", errors.toString()), HttpStatus.BAD_REQUEST);
    }

    /**
     * @RequestParam 上校验失败后抛出的异常是 ConstraintViolationException
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<Result> ConstraintViolationExceptionHandler(ConstraintViolationException ex) {

        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
        Iterator<ConstraintViolation<?>> iterator = constraintViolations.iterator();
//        Map errors = new LinkedHashMap<>();
        String errors =null;
        while (iterator.hasNext()) {
            ConstraintViolation<?> cvl = iterator.next();
            System.out.println(cvl);
            errors = cvl.getPropertyPath()+ " " + cvl.getMessage();
            break;
        }
        return new ResponseEntity<>(new Result("400", errors), HttpStatus.BAD_REQUEST);
    }




}
