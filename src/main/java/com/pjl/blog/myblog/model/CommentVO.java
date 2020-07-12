package com.pjl.blog.myblog.model;

import lombok.Data;

@Data
public class CommentVO extends BaseVO {

    private Integer articleId;
    private Integer parentId;
    private Integer receiverId;
    private String content;
    private int type;
    private Integer creator;
    private Integer likeCount;
}
