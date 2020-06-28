package com.pjl.blog.myblog.controller;

import com.pjl.blog.myblog.dto.AccessTokenDto;
import com.pjl.blog.myblog.dto.GitHubUserDto;
import com.pjl.blog.myblog.model.UserVO;
import com.pjl.blog.myblog.provider.GitHubProvider;
import com.pjl.blog.myblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;


@Controller
public class AuthorizeController {

    @Autowired
    private GitHubProvider gitHubProvider;

    @Value("${github.client.secret}")
    private String client_secret;

    @Value("${github.client.id}")
    private String client_id;

    @Value("${callback.url}")
    private String callbackUrl;

    @Autowired
    private UserService userService;

    /**
     * @desc github的OAuth回调方法
     * @param code
     * @param state
     * @return
     */
    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletResponse response){
        AccessTokenDto accessTokenDto = new AccessTokenDto();
        accessTokenDto.setClient_id(client_id);
        accessTokenDto.setRedirect_uri(callbackUrl);
        accessTokenDto.setClient_secret(client_secret);
        accessTokenDto.setCode(code);
        accessTokenDto.setState(state);
        String accessToken= gitHubProvider.getAccessToken(accessTokenDto);
        GitHubUserDto gitHubUser = null;
        if (!StringUtils.isEmpty(accessToken)){
            gitHubUser = gitHubProvider.getUser(accessToken);
        }
        if(gitHubUser != null){
            UserVO user = new UserVO();
            user.setToken(UUID.randomUUID().toString());
            user.setUserName(gitHubUser.getLogin());
            user.setAccountId(String.valueOf(gitHubUser.getId()));
            user.setName(gitHubUser.getName());
            user.setAvatarUrl(gitHubUser.getAvatarUrl());
            userService.insertOrUpdateUser(user);
//            登陆成功
            response.addCookie(new Cookie("pjl-blog-token",user.getToken()));
//            重定向
            return "redirect:/";
        }else {
            return "redirect:/";
        }
    }
}
