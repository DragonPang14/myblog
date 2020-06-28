package com.pjl.blog.myblog.enums;

public enum ArticleTypeEnum {

    TOP_ARTICLE(0,"置顶文章"),

    NORMAL_ARTICLE(1,"普通文章"),

    ABOUT_ME(2,"关于我")

    ;

    private int code;
    private String remarks;

    ArticleTypeEnum(int code,String remarks){
        this.code = code;
        this.remarks = remarks;
    }

    public int getCode() {
        return code;
    }

    public String getRemarks() {
        return remarks;
    }
}
