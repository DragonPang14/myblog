package com.pjl.blog.myblog.model;

import lombok.Data;

@Data
public class UserVO extends BaseVO{

    private String accountId;
    private String name;
    private String token;
    private String avatarUrl;

    //增加注册
    private String userName;
    private String password;
    private String mobile;
    private String bio;  //简介
    private String mail;
    private int userType;  //0:admin 1:reg 2:third 3:tourist 4:associated user

    //评论ip地址
    private String ipAddress;
    //个人网站
    private String blog;
}
