package com.pjl.blog.myblog.controller;

import com.pjl.blog.myblog.aspect.HyperLogInc;
import com.pjl.blog.myblog.dto.ArticleDto;
import com.pjl.blog.myblog.dto.CommentDto;
import com.pjl.blog.myblog.enums.ArticleTypeEnum;
import com.pjl.blog.myblog.enums.TargetTypeEnum;
import com.pjl.blog.myblog.service.ArticleService;
import com.pjl.blog.myblog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CommentService commentService;

    @HyperLogInc
    @GetMapping("/article/{id}")
    public String article(@PathVariable(name = "id") Integer id,
                           Model model){
        ArticleDto articleDto = articleService.findArticleById(id);
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
}
