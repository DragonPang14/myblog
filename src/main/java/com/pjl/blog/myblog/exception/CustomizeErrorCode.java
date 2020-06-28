package com.pjl.blog.myblog.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode {
    ARTICLE_NOT_FOUND(2001,"主题没有找到，再确认下你的地址"),

    USER_NOT_FOUND(3001,"用户未找到"),

    USER_NOT_LOGIN(3002,"用户未登录"),

    USER_UNAUTHORIZED(3003,"用户未授权"),

    INCREMENT_ERROR(4001,"递增数不能小于0");
    private Integer code;
    private String Message;

    CustomizeErrorCode(Integer code, String message) {
        this.code = code;
        Message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return Message;
    }
}
