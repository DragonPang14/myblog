package com.pjl.blog.myblog.model;

import lombok.Data;

@Data
public class TagVO extends BaseVO{

    private String tagName;
    private String remarks;
    private int type;
}
