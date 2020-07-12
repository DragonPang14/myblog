package com.pjl.blog.myblog.service;

import com.pjl.blog.myblog.dao.CommentDao;
import com.pjl.blog.myblog.dto.CommentDto;
import com.pjl.blog.myblog.dto.ResultDto;
import com.pjl.blog.myblog.enums.NotificationTypeEnum;
import com.pjl.blog.myblog.enums.TargetTypeEnum;
import com.pjl.blog.myblog.mapper.ArticleMapper;
import com.pjl.blog.myblog.mapper.CommentMapper;
import com.pjl.blog.myblog.mapper.UserMapper;
import com.pjl.blog.myblog.model.ArticleVO;
import com.pjl.blog.myblog.model.CommentVO;
import com.pjl.blog.myblog.model.NotificationVO;
import com.pjl.blog.myblog.model.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CommentService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommentDao commentDao;

    @Transactional
    public ResultDto insertComment(CommentVO comment) {
        ResultDto resultDto = new ResultDto();
        NotificationVO notification = null;
        boolean haveFlag = false;
        CommentDto commentDto = new CommentDto();
        if(comment.getType() == TargetTypeEnum.QUESTION_TYPE.getType() ){
            //判断是否找得到评论的主题
            ArticleVO questionById = articleMapper.findArticleById(comment.getParentId());
            haveFlag = questionById != null;
            notification = this.createNotify(comment);
        }else {
            //判断是否找的到回复的评论
            CommentVO commentByCommentId = commentMapper.findCommentByCommentId(comment.getParentId());
            haveFlag =commentByCommentId != null;
            if (haveFlag){
                int userType = userMapper.verifyUserType(commentByCommentId.getCreator());
                //游客不用发消息通知
                if (userType != 3){
                    notification = this.createNotify(comment);
                }
            }
        }
        //找得到评论的目标
        if (haveFlag){
            commentMapper.insert(comment);
            BeanUtils.copyProperties(comment,commentDto);
            commentDto.setCreator(userMapper.findById(comment.getCreator()));
            resultDto.setCode(100);
            resultDto.setObj(commentDto);
            if (notification != null){
                userMapper.insertNotification(notification);
            }
        }else {
            if (comment.getType() == TargetTypeEnum.QUESTION_TYPE.getType()){
                resultDto.setCode(2002);
                resultDto.setMsg("回复主题未找到");
            }else {
                resultDto.setCode(2003);
                resultDto.setMsg("回复评论未找到");
            }
            return resultDto;
        }
        return resultDto;
    }

    //构建通知信息
    private NotificationVO createNotify(CommentVO comment){
        NotificationVO notification = new NotificationVO();
        notification.setReceiveId(comment.getReceiverId());
        notification.setSenderId(comment.getCreator());
        notification.setTargetId(comment.getParentId());
        notification.setTargetType(comment.getType());  //通知目标类型  --文章 ：评论
        notification.setAction(NotificationTypeEnum.COMMENT_ACTION.getCode());
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setNotiContent(comment.getContent());  //评论内容
        return notification;
    }


    public List<CommentDto> getComments(Integer id, Integer commentType) {
        List<CommentDto> commentDtos = commentDao.getCommentByArticleId(id);

        //改为通过xml一次性查询
        /*List<CommentVO> comments = commentMapper.findCommentById(id,commentType);
        if (comments.size() == 0){
            return new ArrayList<>();
        }
        //使用lamdba和stream来过滤评论或评论回复中重复的创建者id
        Set<Integer> commentCreatorId = comments.stream().map(comment -> comment.getCreator()).collect(Collectors.toSet());
        List<UserVO> users = commentCreatorId.stream().map(integer -> {
            UserVO user = userMapper.findById(integer);
            return user;
        }).collect(Collectors.toList());
        Map<Integer,UserVO> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));
        List<CommentDto> commentDtos = comments.stream().map(comment -> {
            CommentDto commentDto = new CommentDto();
            BeanUtils.copyProperties(comment,commentDto);
            commentDto.setUser(userMap.get(comment.getCreator()));
            return commentDto;
        }).collect(Collectors.toList());*/
        return commentDtos;
    }
}
