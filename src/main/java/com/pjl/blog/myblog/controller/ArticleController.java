package com.pjl.blog.myblog.controller;

import com.pjl.blog.myblog.aspect.HyperLogInc;
import com.pjl.blog.myblog.dto.ArticleDto;
import com.pjl.blog.myblog.dto.CommentDto;
import com.pjl.blog.myblog.dto.ResultDto;
import com.pjl.blog.myblog.enums.ArticleTypeEnum;
import com.pjl.blog.myblog.enums.CustomizeStatusEnum;
import com.pjl.blog.myblog.enums.TargetTypeEnum;
import com.pjl.blog.myblog.mapper.UserMapper;
import com.pjl.blog.myblog.model.UserVO;
import com.pjl.blog.myblog.service.ArticleService;
import com.pjl.blog.myblog.service.CommentService;
import com.pjl.blog.myblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @HyperLogInc
    @GetMapping("/article/{id}")
    public String article(@PathVariable(name = "id") Integer id,
                           @RequestParam(value = "isNotify",required = false)boolean isNotify,
                           Model model){
        ArticleDto articleDto = articleService.findArticleById(id,isNotify);
        List<CommentDto> commentDtoList = commentService.getComments(id, TargetTypeEnum.QUESTION_TYPE.getType());
        model.addAttribute("articleDto",articleDto);
        model.addAttribute("commentDtoList",commentDtoList);
        return "article";
    }

    @GetMapping("/about")
    public String about(Model model){
        ArticleDto articleDto = articleService.findArticleByType(ArticleTypeEnum.ABOUT_ME.getCode());
        model.addAttribute("articleDto",articleDto);
        return "about";
    }

    @GetMapping("/removeDraft")
    public @ResponseBody ResultDto removeDraft(@RequestParam(value = "id",required = false)Integer draftId,
                                               @CookieValue(value = "pjl-blog-token", required = false) String token){
        ResultDto resultDto;
        if (token == null) {
            return new ResultDto(CustomizeStatusEnum.UNLOGIN_CODE);
        }
        UserVO user = userService.findByToken(token);
        try {
            articleService.removeDraft(draftId,user.getId());
            resultDto = new ResultDto(CustomizeStatusEnum.SUCCESS_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            resultDto = new ResultDto(CustomizeStatusEnum.CODE_ERROR);
        }
        return resultDto;
    }
}
