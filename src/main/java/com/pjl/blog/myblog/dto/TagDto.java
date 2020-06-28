package com.pjl.blog.myblog.dto;

import lombok.Data;

@Data
public class TagDto extends BaseDto{

    private String tagName;
    private String remarks;
}
