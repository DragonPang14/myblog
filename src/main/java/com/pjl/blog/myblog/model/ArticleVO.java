package com.pjl.blog.myblog.model;

import lombok.Data;

import java.util.List;

@Data
public class ArticleVO extends BaseVO{

    private String title;
    private String description;
    private String content;
    private Integer creator;
    private Integer commentCount;
    private Integer viewCount;
    private Integer likeCount;
    private List<TagVO> tags;
    private int type;

}
