package com.pjl.blog.myblog.dto;

import lombok.Data;

@Data
public class InsertCommentDto extends BaseDto{

    private Integer articleId;
    private Integer parentId;
    private Integer receiverId;
    private String content;
    private Integer type;

    private String name; //未登录情况下的评论人
    private String mail; //未登录情况下的评论人邮箱
    private String blog; //友链
}
