package com.pjl.blog.myblog.dto;

import com.pjl.blog.myblog.model.UserVO;
import lombok.Data;

import java.util.List;

@Data
public class CommentDto extends BaseDto{

    private Integer articleId;
    private Integer parentId;
    private String content;
    private int type;
    private Integer likeCount;
    private List<CommentDto> secondLevelComments;  //二级评论
    private UserVO creator;
    private UserVO receiver;
}
