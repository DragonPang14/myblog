package com.pjl.blog.myblog.model;

import lombok.Data;

@Data
public class ArticleTagsVO extends BaseVO {
    private Integer articleId;
    private Integer tagId;
}
