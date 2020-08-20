package com.pjl.blog.myblog.controller.admin;

import com.pjl.blog.myblog.dto.ResultDto;
import com.pjl.blog.myblog.enums.CustomizeStatusEnum;
import com.pjl.blog.myblog.enums.UserType;
import com.pjl.blog.myblog.exception.CustomizeErrorCode;
import com.pjl.blog.myblog.exception.CustomizeException;
import com.pjl.blog.myblog.mapper.UserMapper;
import com.pjl.blog.myblog.model.UserVO;
import com.pjl.blog.myblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminLoginController {

    @Autowired
    private UserService userService;

    @ModelAttribute
    public UserVO get(@CookieValue(value = "pjl-blog-token", required = false) String token){
        if(!StringUtils.isEmpty(token)){
            UserVO user = userService.findByToken(token);
            return user;
        }else {
            return null;
        }
    }

    @GetMapping
    public String adminLoginPage(UserVO userVO) {
        if (userVO != null && UserType.ADMIN_TYPE.getCode() == userVO.getUserType()){
            return "admin/adminIndex";
        }else {
            return "admin/login";
        }
    }

    @GetMapping("/adminIndex")
    public String adminIndex(UserVO user) {
        if (user == null) {
            throw new CustomizeException(CustomizeErrorCode.USER_NOT_LOGIN);
        }
        if (UserType.ADMIN_TYPE.getCode() != user.getUserType()){
            throw new CustomizeException(CustomizeErrorCode.USER_UNAUTHORIZED);
        }
        return "admin/adminIndex";
    }

}
