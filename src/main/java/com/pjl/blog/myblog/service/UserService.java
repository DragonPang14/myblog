package com.pjl.blog.myblog.service;

import com.pjl.blog.myblog.dto.AvatarDto;
import com.pjl.blog.myblog.dto.UserDto;
import com.pjl.blog.myblog.mapper.UserMapper;
import com.pjl.blog.myblog.model.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Value("${DEFAULT_AVATAR}")
    private String DEFAULT_AVATAR;

    /**
     * @desc 登陆更新
     * @param user
     */
    public void insertOrUpdateUser(UserVO user) {
        UserVO dbUser = userMapper.findByAccountId(user.getAccountId());
        if(dbUser != null){
            dbUser.setAvatarUrl(user.getAvatarUrl());
            dbUser.setToken(user.getToken());
            dbUser.setName(user.getName());
            dbUser.setGmtModified(System.currentTimeMillis());
            dbUser.setUserName(user.getUserName());
            userMapper.updateUser(dbUser);
        }else {
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insertUser(user);
        }
    }

    public UserVO findByToken(String token) {
        UserVO dbUser = userMapper.findByToken(token);
        return dbUser;
    }

    /**
     * @desc 注册
     * @param userDto
     * @return 是否注册成功
     */
    public int registered(UserDto userDto) {
        UserVO user = new UserVO();
        BeanUtils.copyProperties(userDto,user);
        user.setGmtCreate(System.currentTimeMillis());
        user.setGmtModified(user.getGmtCreate());
        user.setAvatarUrl(DEFAULT_AVATAR);
        user.setUserType(1);
        return userMapper.registered(user);
    }

    /**
     * @desc 查找重复用户名
     * @param userName
     * @return
     */
    public int findByUserName(String userName) {
        return userMapper.findByUserName(userName);
    }


    public UserVO findById(Integer userId) {
        return userMapper.findById(userId);
    }

    public int findByMobile(String mobile) {
        return userMapper.findByMobile(mobile);
    }

    public String login(UserDto userDto) {
        UserVO dbUser = userMapper.verifyUser(userDto);
        if (dbUser != null){
            dbUser.setToken(UUID.randomUUID().toString());
            userMapper.updateUser(dbUser);
            return dbUser.getToken();
        }
        return null;
    }

    public int modifyAvatar(AvatarDto avatarDto) {
        return userMapper.modifyAvatar(avatarDto);
    }

}
