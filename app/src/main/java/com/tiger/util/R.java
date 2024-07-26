package com.tiger.util;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一返回对象R
 *
 * @author tiger
 * @since 2024-07-24 11:25:12
 */
@Data
public class R implements Serializable {
    private static final long serialVersionUID = 187006049226125544L;
    private int code;
    private String message;
    private Object data;

    /**
     * 设置数据
     *
     * @param data 数据
     * @return R
     */
    public R setData(Object data) {
        this.data = data;
        return this;
    }

    /**
     * 操作成功
     *
     * @return R
     */
    public static R ok() {
        R r = new R();
        r.code = 200;
        r.message = "OK";
        return r;
    }

    /**
     * 认证授权失败。 包括密钥信息不正确；数字签名错误；授权已超时
     *
     * @return R
     */
    public static R fail() {
        R r = new R();
        r.code = 401;
        r.message = "fail";
        return r;
    }

    /**
     * 系统异常
     *
     * @return R
     */
    public static R exp() {
        R r = new R();
        r.code = 500;
        r.message = "exception";
        return r;
    }
}


