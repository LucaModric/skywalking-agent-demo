package com.tiger.util;

public enum ResultCodeEnum implements ResultCode {
    SUCCESS(200, "操作成功！"),
    INVALID_REQUEST(400, "非法参数！"),   
    UNAUTHORIZED(401, "非法访问！"),
    UNAUTHORIZED_TOKEN(402, "不合法的TOKEN！"),
    NOT_FOUND(404, "记录不存在！"),
    ENTITY_TOO_LARGE(413, "请求参数大小超过限制"),
    SYSTEM_ERROR(500, "系统异常，请稍后再试！"),
    DECRYPT_ERROR(400001, "解密失败。"),
    INVALID_FILE_PATTERN(20060, "不合法的文件，空文件或文件名不合法（xls.xlsx）");
 
    private int code;
    private String msg;
 
    private ResultCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
 
    public int getCode() {
        return this.code;
    }
 
    public String getMsg() {
        return this.msg;
    }
}