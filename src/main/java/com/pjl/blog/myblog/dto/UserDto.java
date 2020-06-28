package com.pjl.blog.myblog.dto;

import lombok.Data;

@Data
public class UserDto {

    private Integer id;
    private String userName;
    private String name;
    private String password;
    private String mobile;
    private String bio;  //简介
    private String mail;
    private int userType;
    private String avatarUrl;

    //评论ip地址
    private String ipAddress;
    //个人网站
    private String blog;

}
