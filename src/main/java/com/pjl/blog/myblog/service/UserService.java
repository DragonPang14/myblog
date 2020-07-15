package com.pjl.blog.myblog.service;

import com.pjl.blog.myblog.dao.UserDao;
import com.pjl.blog.myblog.dto.AvatarDto;
import com.pjl.blog.myblog.dto.NotificationDto;
import com.pjl.blog.myblog.dto.PaginationDto;
import com.pjl.blog.myblog.dto.UserDto;
import com.pjl.blog.myblog.enums.NotificationTypeEnum;
import com.pjl.blog.myblog.enums.TargetTypeEnum;
import com.pjl.blog.myblog.mapper.UserMapper;
import com.pjl.blog.myblog.model.UserVO;
import com.pjl.blog.myblog.utils.CommonUtils;
import com.pjl.blog.myblog.utils.IPUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Value("${DEFAULT_AVATAR}")
    private String DEFAULT_AVATAR;

    @Autowired
    private UserDao userDao;

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
            dbUser.setIpAddress(IPUtils.getIpAddr());
            userMapper.updateUser(dbUser);
            return dbUser.getToken();
        }
        return null;
    }

    public int modifyAvatar(AvatarDto avatarDto) {
        return userMapper.modifyAvatar(avatarDto);
    }

    /**
     * @desc 游客回复，邮箱验证
     * @param mail
     * @param name
     * @return
     */
    public UserVO verfiyByMail(String mail, String name) {
        UserVO userVO = userMapper.findByMail(mail);
        if (userVO != null){
            userVO.setIpAddress(IPUtils.getIpAddr());
            userVO.setName(name);
            userMapper.updateUser(userVO);
        }
        return userVO;
    }


    /**
     * @desc 创建游客
     * @param name
     * @param mail
     * @param link
     * @return
     */
    public UserVO createTourist(String name, String mail, String link) {
        UserVO user = new UserVO();
        user.setName(name);
        user.setMail(mail);
        user.setBlog(link);
        user.setGmtCreate(System.currentTimeMillis());
        user.setGmtModified(user.getGmtCreate());
        user.setAvatarUrl(DEFAULT_AVATAR);
        user.setUserType(3);
        user.setIpAddress(IPUtils.getIpAddr());
        userMapper.registered(user);
        return user;
    }

    public Integer totalNotifications(Integer userId,Integer status){
        return userDao.totalNotification(userId,status);
    }

    public PaginationDto<NotificationDto> notificationsList(Integer userId,Integer page) {
        Integer totalCount = this.totalNotifications(userId,null);
        //计算总页数
        Integer totalPage = CommonUtils.calculateTotalPage(totalCount);
        //计算偏移量
        Integer offset = CommonUtils.calculatePageOffset(totalPage, page, 10);
        PaginationDto<NotificationDto> pagination = new PaginationDto<>();
        List<NotificationDto> notificationsDto = userDao.getNotifications(userId,offset,10);
        notificationsDto.forEach(notificationDto -> {
            notificationDto.setActionStr(NotificationTypeEnum.getRemark(notificationDto.getAction()));
            notificationDto.setTargetTypeStr(TargetTypeEnum.getRemark(notificationDto.getTargetType()));
        });
        pagination.setPageList(notificationsDto);
        pagination.setPagination(totalPage, page);
        return pagination;
    }

    public Object totalDraftCount(Integer userId, int type) {
        return userMapper.countDraft(userId,type);
    }
}
