package com.pjl.blog.myblog.dto;

import com.pjl.blog.myblog.model.UserVO;
import lombok.Data;

import java.util.List;

@Data
public class ArticleDto extends BaseDto {

    private String title;
    private String description;
    private String descriptionStr;
    private String content;
    private Integer creator;
    private Integer commentCount;
    private Integer viewCount;
    private Integer likeCount;
    private String tag;
    private List<TagDto> tagList;
    private UserVO user;
    private int type;
    private String publishToken;
}
