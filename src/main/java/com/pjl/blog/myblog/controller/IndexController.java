package com.pjl.blog.myblog.controller;

import com.pjl.blog.myblog.dto.ArticleDto;
import com.pjl.blog.myblog.dto.PaginationDto;
import com.pjl.blog.myblog.dto.ResultDto;
import com.pjl.blog.myblog.enums.CustomizeStatusEnum;
import com.pjl.blog.myblog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private ArticleService articleService;

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/getArticleList")
    public @ResponseBody
    ResultDto<PaginationDto> getArticleList(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                         @RequestParam(value = "size", defaultValue = "10") Integer size,
                                         @RequestParam(value = "tag", required = false) Integer tagId){
        ResultDto<PaginationDto> resultDto;
        try {
            PaginationDto<ArticleDto> paginationDto = articleService.getList(null,page,size,tagId);
            resultDto = new ResultDto<>(CustomizeStatusEnum.SUCCESS_CODE);
            resultDto.setObj(paginationDto);
        } catch (Exception e) {
            e.printStackTrace();
            resultDto = new ResultDto<>(CustomizeStatusEnum.CODE_ERROR);
        }
        return resultDto;
    }

    @GetMapping("/getTimeLine")
    public @ResponseBody ResultDto<List<ArticleDto>> getTimeLine(){

        ResultDto<List<ArticleDto>> resultDto;
        try {
            List<ArticleDto> timeLines = articleService.getTimeLine();
            resultDto = new ResultDto<>(CustomizeStatusEnum.SUCCESS_CODE);
            resultDto.setObj(timeLines);
        } catch (Exception e) {
            e.printStackTrace();
            resultDto = new ResultDto<>(CustomizeStatusEnum.CODE_ERROR);
        }
        return resultDto;
    }

}
