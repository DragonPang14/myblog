package com.pjl.blog.myblog.dto;

import lombok.Data;

@Data
public class InsertCommentDto extends BaseDto{

    private Integer questionId;
    private Integer parentId;
    private String content;
    private Integer type;
}
