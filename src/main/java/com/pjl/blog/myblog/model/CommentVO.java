package com.pjl.blog.myblog.model;

import lombok.Data;

@Data
public class CommentVO extends BaseVO {

    private Integer questionId;
    private Integer parentId;
    private String content;
    private int type;
    private Integer creator;
    private Integer likeCount;
}
