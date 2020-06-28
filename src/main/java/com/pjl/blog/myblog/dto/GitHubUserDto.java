package com.pjl.blog.myblog.dto;

import lombok.Data;

@Data
public class GitHubUserDto {
    private String name;
    private String login;
    private long id;
    private String bio;
    private String avatarUrl;

}
