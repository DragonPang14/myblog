package com.pjl.blog.myblog.dao;

import com.pjl.blog.myblog.dto.NotificationDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserDao {

    Integer totalNotification(Integer userId,Integer status);

    List<NotificationDto> getNotifications(Integer userId, Integer offset, int size);
}
