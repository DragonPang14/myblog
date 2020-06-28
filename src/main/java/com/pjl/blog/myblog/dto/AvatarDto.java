package com.pjl.blog.myblog.dto;

import lombok.Data;

@Data
public class AvatarDto {
    private Integer id;
    private Integer userId;
    private String avatarUrl;
}
