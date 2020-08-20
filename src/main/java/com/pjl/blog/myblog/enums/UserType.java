package com.pjl.blog.myblog.enums;

public enum UserType {
    ADMIN_TYPE(0,"管理员"),

    REG_TYPE(1,"注册用户"),

    THIRD_TYPE(2,"第三方账号"),

    TOURIST_TYPE(3,"游客"),

    ASSOCIATED_TYPE(4,"关联账号")

    ;
    private int code;
    private String remark;

    UserType(int code, String remark) {
        this.code = code;
        this.remark = remark;
    }

    public int getCode() {
        return code;
    }
}
