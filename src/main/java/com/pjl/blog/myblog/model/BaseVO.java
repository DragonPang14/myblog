package com.pjl.blog.myblog.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class BaseVO implements Serializable {

    private Integer id;
    private Long gmtCreate;
    private Long gmtModified;
    private int delFlag;

}
