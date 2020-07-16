package com.pjl.blog.myblog.controller;

import com.pjl.blog.myblog.dto.*;
import com.pjl.blog.myblog.enums.CustomizeStatusEnum;
import com.pjl.blog.myblog.exception.CustomizeErrorCode;
import com.pjl.blog.myblog.exception.CustomizeException;
import com.pjl.blog.myblog.model.UserVO;
import com.pjl.blog.myblog.service.ArticleService;
import com.pjl.blog.myblog.service.UserService;
import com.pjl.blog.myblog.utils.FastDfsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private FastDfsUtils fastDfsUtils;

    @GetMapping("/setting/{userId}")
    public String setting(@PathVariable(name = "userId") Integer userId, Model model){
        UserVO user = userService.findById(userId);
        if (user != null){
            model.addAttribute("user",user);
        }else {
            throw new CustomizeException(CustomizeErrorCode.USER_NOT_FOUND);
        }
        return "setting";
    }


    @PostMapping("/registered")
    public @ResponseBody
    ResultDto<UserDto> registered(@RequestBody UserDto userDto){
        ResultDto<UserDto> resultDto;
        if (userService.findByUserName(userDto.getUserName()) > 0){
            resultDto = new ResultDto<>(CustomizeStatusEnum.DUPLICATE_USER_NAME);
        }else if (userService.findByMobile(userDto.getMobile()) > 0){
            resultDto = new ResultDto<>(CustomizeStatusEnum.DUPLICATE_MOBILE);
        }else {
            int isSuccess = userService.registered(userDto);
            if (isSuccess == 1){
                resultDto = new ResultDto<>(CustomizeStatusEnum.SUCCESS_CODE);
            } else {
                resultDto = new ResultDto<>(CustomizeStatusEnum.CODE_ERROR);
            }
        }
        return resultDto;
    }

    @PostMapping("/userLogin")
    public @ResponseBody ResultDto<UserDto> login(@RequestBody UserDto userDto,
                                                  HttpServletResponse response){
        ResultDto<UserDto> resultDto;
        if (userService.findByUserName(userDto.getUserName()) == 0){
            resultDto = new ResultDto<>(CustomizeStatusEnum.USERNAME_ERROR);
        }else {
            String token = userService.login(userDto);
            if (token !=null && !"".equals(token)){
                response.addCookie(new Cookie("pjl-blog-token",token));
                resultDto = new ResultDto<>(CustomizeStatusEnum.SUCCESS_CODE);
            }else {
                resultDto = new ResultDto<>(CustomizeStatusEnum.PASSWORD_ERROR);
            }
        }
        return resultDto;
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        request.getSession().invalidate();
        Cookie cookie = new Cookie("pjl-blog-token",null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }

    @PostMapping("/uploadAvatar")
    public @ResponseBody ResultDto uploadAvatar(AvatarDto avatarDto,
                                                @RequestParam(name = "avatar") MultipartFile avatar){
        try {
            String storePath = fastDfsUtils.uploadFile(avatar);
            avatarDto.setAvatarUrl(storePath);
        } catch (IOException e) {
            e.printStackTrace();
            return ResultDto.errorOf(CustomizeStatusEnum.UPLOAD_ERROR);
        }
        return ResultDto.okOf(avatarDto);
    }

    @PostMapping("/modifyAvatar")
    public @ResponseBody ResultDto modifyAvatar(@RequestBody AvatarDto avatarDto){
        System.out.println("url:"+avatarDto.getAvatarUrl());
        int rows = userService.modifyAvatar(avatarDto);
        if (rows > 0){
            return new ResultDto(CustomizeStatusEnum.SUCCESS_CODE);
        }else {
            return new ResultDto(CustomizeStatusEnum.CODE_ERROR);
        }
    }

    @GetMapping("/notifications")
    public @ResponseBody ResultDto<NotificationDto> notifications(@CookieValue(value = "pjl-blog-token")String token,
                                @RequestParam(value = "page",required = false,defaultValue = "1")Integer page, Model model){
        UserVO user = userService.findByToken(token);
        if (user == null){
            throw new CustomizeException(CustomizeErrorCode.USER_NOT_FOUND);
        }
        PaginationDto<NotificationDto> pagination = userService.notificationsList(user.getId(),page);
        return ResultDto.okOf(pagination);
    }

    @GetMapping("/unReadNotifications")
    public @ResponseBody ResultDto unReadNotifications(@CookieValue(value = "pjl-blog-token")String token){
        UserVO user = userService.findByToken(token);
        if (user == null){
            throw new CustomizeException(CustomizeErrorCode.USER_NOT_FOUND);
        }
        return ResultDto.okOf(userService.totalNotifications(user.getId(),0));
    }

    @GetMapping("/getDraftCount")
    public @ResponseBody ResultDto getDraftCount(@CookieValue(value = "pjl-blog-token")String token){
        UserVO user = userService.findByToken(token);
        if (user == null){
            throw new CustomizeException(CustomizeErrorCode.USER_NOT_FOUND);
        }
        return ResultDto.okOf(userService.totalDraftCount(user.getId(),3));
    }

    @GetMapping("/draftBox")
    public String draftBox(){
        return "draftbox";
    }

    @GetMapping("/getDraftBox")
    public @ResponseBody ResultDto<PaginationDto> getDraftBox(@CookieValue(value = "pjl-blog-token")String token,
                                                           @RequestParam(value = "page", defaultValue = "1") Integer page){
        ResultDto<PaginationDto> resultDto;
        UserVO user = userService.findByToken(token);
        if (user == null){
            resultDto = new ResultDto(CustomizeStatusEnum.UNLOGIN_CODE);
            return resultDto;
        }
        try {
            PaginationDto<ArticleDto> paginationDto = articleService.getDraftByUserId(user.getId(),page);
            resultDto = new ResultDto<>(CustomizeStatusEnum.SUCCESS_CODE);
            resultDto.setObj(paginationDto);
        } catch (Exception e) {
            e.printStackTrace();
            resultDto = new ResultDto<>(CustomizeStatusEnum.CODE_ERROR);
            resultDto.setMsg(e.getMessage());
        }
        return resultDto;
    }

}
