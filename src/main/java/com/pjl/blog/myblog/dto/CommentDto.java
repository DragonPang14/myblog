package com.pjl.blog.myblog.dto;

import com.pjl.blog.myblog.model.UserVO;
import lombok.Data;

@Data
public class CommentDto extends BaseDto{

    private Integer parentId;
    private String content;
    private int type;
    private Integer likeCount;
    private UserVO user;
}
