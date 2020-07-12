package com.pjl.blog.myblog.controller;

import com.pjl.blog.myblog.dto.CommentDto;
import com.pjl.blog.myblog.dto.InsertCommentDto;
import com.pjl.blog.myblog.dto.ResultDto;
import com.pjl.blog.myblog.dto.UserDto;
import com.pjl.blog.myblog.enums.CustomizeStatusEnum;
import com.pjl.blog.myblog.enums.TargetTypeEnum;
import com.pjl.blog.myblog.model.CommentVO;
import com.pjl.blog.myblog.model.UserVO;
import com.pjl.blog.myblog.service.CommentService;
import com.pjl.blog.myblog.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    /**
     * @desc 发送主题评论/评论回复
     * @param insertCommentDto
     * @param user
     * @return
     */
    @RequestMapping(value = "/sendComment",method = RequestMethod.POST)
    public @ResponseBody
    ResultDto sendComment(@RequestBody InsertCommentDto insertCommentDto,
                          @SessionAttribute(name = "user",required = false) UserVO user){
        if (user == null){
            if (StringUtils.isEmpty(insertCommentDto.getName()) || StringUtils.isEmpty(insertCommentDto.getMail())){
                return ResultDto.errorOf(CustomizeStatusEnum.UNLOGIN_CODE);
            }else {
                user = userService.verfiyByMail(insertCommentDto.getMail(),insertCommentDto.getName());
                if (user == null){
                    user = userService.createTourist(insertCommentDto.getName(),insertCommentDto.getMail(),insertCommentDto.getBlog());
                }
            }
        }
        CommentVO comment = new CommentVO();
        BeanUtils.copyProperties(insertCommentDto,comment);
        comment.setCreator(user.getId());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(comment.getGmtCreate());
        ResultDto resultDto = commentService.insertComment(comment);
        return resultDto;
    }

    /**
     * @desc 查询评论的回复列表
     * @param id
     * @return
     */
    @RequestMapping(value = "/getCommentsReply/{id}",method = RequestMethod.GET)
    public @ResponseBody ResultDto getCommentsReply(@PathVariable(name = "id")Integer id){
        List<CommentDto> commentDtos = commentService.getComments(id, TargetTypeEnum.COMMENT_TYPE.getType());
        return ResultDto.okOf(commentDtos);
    }

}
