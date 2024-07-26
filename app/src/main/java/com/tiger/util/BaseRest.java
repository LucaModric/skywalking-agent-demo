package com.tiger.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class BaseRest {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());
 
    public BaseRest() {
    }
 
    protected <T> Result<T> success() {
        return this.success(null);
    }
 
    protected <T> Result<T> success(T data) {
        return new Result(ResultCodeEnum.SUCCESS, data);
    }
 
    protected <T> Result<T> failure() {
        return this.failure(ResultCodeEnum.SYSTEM_ERROR, null);
    }
 
    protected <T> Result<T> failure(T data) {
        return this.failure(ResultCodeEnum.SYSTEM_ERROR, data);
    }
 
    protected <T> Result<T> failure(ResultCode resultCode, T data) {
        return new Result(resultCode, data);
    }
 
    protected <T> Result<T> failure(ResultCode resultCode) {
        return new Result(resultCode);
    }
}