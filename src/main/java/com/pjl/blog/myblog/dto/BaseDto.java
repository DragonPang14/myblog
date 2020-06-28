package com.pjl.blog.myblog.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class BaseDto implements Serializable {

    private Integer id;
    private Long gmtCreate;
    private Long gmtModified;
}
