package com.pjl.blog.myblog.controller;

import com.pjl.blog.myblog.dto.ArticleDto;
import com.pjl.blog.myblog.dto.ImageResultDto;
import com.pjl.blog.myblog.dto.ResultDto;
import com.pjl.blog.myblog.dto.TagDto;
import com.pjl.blog.myblog.enums.CustomizeStatusEnum;
import com.pjl.blog.myblog.exception.CustomizeErrorCode;
import com.pjl.blog.myblog.exception.CustomizeException;
import com.pjl.blog.myblog.exception.ICustomizeErrorCode;
import com.pjl.blog.myblog.model.UserVO;
import com.pjl.blog.myblog.service.ArticleService;
import com.pjl.blog.myblog.service.UserService;
import com.pjl.blog.myblog.utils.FastDfsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @desc 发布页面
 */
@Controller
public class PublishController {

    @Autowired
    private UserService userService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private FastDfsUtils fastDfsUtils;

    @GetMapping("/publish")
    public String publish(@CookieValue(value = "pjl-blog-token",required = false)String token) {
        if (token == null)
            throw new CustomizeException(CustomizeErrorCode.USER_NOT_LOGIN);
        UserVO userVO = userService.findByToken(token);
        if (userVO != null && userVO.getUserType() != 0)
            throw new CustomizeException(CustomizeErrorCode.USER_UNAUTHORIZED);
        return "publish";
    }

    @GetMapping("/publish/{id}")
    public String editArticle(@PathVariable(name = "id") Integer id,
                               Model model){
        ArticleDto articleDto = articleService.findArticleById(id);
        if (articleDto != null){
            model.addAttribute("articleDto",articleDto);
        }
        return "publish";
    }

    @RequestMapping(value = "/doPublish",method = RequestMethod.POST)
    public @ResponseBody ResultDto doPublish(@RequestBody ArticleDto articleDto,
                               @CookieValue(value = "pjl-blog-token", required = false) String token) {
        try {
            if (token == null) {
                return new ResultDto(CustomizeStatusEnum.UNLOGIN_CODE);
            }
            UserVO user = userService.findByToken(token);
            String tag = articleDto.getTag().substring(0,articleDto.getTag().length() - 1);
            String[] tagIdStr = tag.split(",");
            List<Integer> tagIdList = new ArrayList<>();
            for (String s : tagIdStr) {
                Integer tagId = Integer.valueOf(s);
                tagIdList.add(tagId);
            }
            String[] description = articleDto.getDescription().split("&lt;!--summary--&gt;");
            String[] content = articleDto.getContent().split("<!--summary-->");
            articleDto.setContent(content.length == 1?content[0]:content[1]);
            articleDto.setDescription(description[0]);
            if (user != null) {
                articleDto.setCreator(user.getId());
                articleService.createOrUpdate(articleDto,tagIdList);
            }else{
                return new ResultDto(CustomizeStatusEnum.UNRECOGNIZED_USER);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultDto(CustomizeStatusEnum.CODE_ERROR);
        }
        return new ResultDto(CustomizeStatusEnum.SUCCESS_CODE);
    }

    /**
     * @desc md上传图片，返回图片地址
     * @param image
     * @return
     */
    @PostMapping("/uploadImage")
    public @ResponseBody
    ImageResultDto uploadImage(@RequestParam(value = "editormd-image-file", required = true) MultipartFile image){
        ImageResultDto imageResultDto = new ImageResultDto();
        try {
            String imagePath = fastDfsUtils.uploadFile(image);
            imageResultDto.setSuccess(1);
            imageResultDto.setMsg("上传成功!");
            imageResultDto.setUrl(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
            imageResultDto.setSuccess(0);
            imageResultDto.setMsg("上传失败！");
        }
        return imageResultDto;
    }

    /**
     * @desc 保存标签
     * @param tagDto
     * @return
     */
    @RequestMapping(value = "/saveTag",method = RequestMethod.POST)
    public @ResponseBody ResultDto saveTag(@RequestBody TagDto tagDto){
        ResultDto<TagDto> resultDto;
        try {
            int isExists = articleService.findTagByName(tagDto.getTagName());
            if (isExists == 1){
                resultDto = new ResultDto<>(CustomizeStatusEnum.TAG_EXISTS);
            }else {
                int tagId = articleService.saveTag(tagDto);
                resultDto = tagId == 1?new ResultDto<>(CustomizeStatusEnum.SUCCESS_CODE):
                        new ResultDto<>(CustomizeStatusEnum.CODE_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultDto =  new ResultDto<>(CustomizeStatusEnum.CODE_ERROR);
        }
        return resultDto;
    }

    @RequestMapping(value = "/getTags",method = RequestMethod.GET)
    public @ResponseBody ResultDto getTags(@RequestParam(value = "type",required = false)Integer type){
        ResultDto<List<TagDto>> resultDto;
        List<TagDto> tagDtos;
        try {
            tagDtos = articleService.getTags(type);
        } catch (Exception e) {
            e.printStackTrace();
            resultDto = new ResultDto<>(CustomizeStatusEnum.CODE_ERROR);
            return resultDto;
        }
        resultDto = new ResultDto<>(CustomizeStatusEnum.SUCCESS_CODE);
        resultDto.setObj(tagDtos);
        return resultDto;
    }
}
