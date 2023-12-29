package com.nft.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 统一返回对象中，Code码、Info描述
 * @author：戏人看戏，微信：whn3500079813
 * @date: 2023/12/7
 * @Copyright：网站：https://nb.sb || git开源地址：https://gitee.com/nb-sb/nft/tree/master
 */
@Data
public class Result implements Serializable {

    private static final long serialVersionUID = -3826891916021780628L;
    private String code;
    private String info;

    public Result() {

    }

    public static Result buildResult(Constants.ResponseCode code) {
        return new Result(code.getCode(), code.getInfo());
    }

    public static Result buildResult(Constants.ResponseCode code, String info) {
        return new Result(code.getCode(), info);
    }

    public static Result buildResult(String code, String info) {
        return new Result(code, info);
    }

    public static Result buildResult(Constants.ResponseCode code, Constants.ResponseCode info) {
        return new Result(code.getCode(), info.getInfo());
    }

    public static Result buildSuccessResult() {
        return new Result(Constants.ResponseCode.SUCCESS.getCode(), Constants.ResponseCode.SUCCESS.getInfo());
    }

    public static Result buildErrorResult() {
        return new Result(Constants.ResponseCode.UN_ERROR.getCode(), Constants.ResponseCode.UN_ERROR.getInfo());
    }

    public static Result buildErrorResult(String info) {
        return new Result(Constants.ResponseCode.UN_ERROR.getCode(), info);
    }

    public Result(String code, String info) {
        this.code = code;
        this.info = info;
    }
    public static Result error(String info) {
        return new Result(Constants.ResponseCode.ERROR.getCode(), info);
    }
    public static Result success(String info) {
        return new Result(Constants.ResponseCode.SUCCESS.getCode(), info);
    }
    public static Result userNotFinded() {
        return new Result(Constants.ResponseCode.USER_NOT_FOUND.getCode(), Constants.ResponseCode.USER_NOT_FOUND.getInfo());
    }

}
