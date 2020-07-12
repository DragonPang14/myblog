package com.pjl.blog.myblog.dao;

import com.pjl.blog.myblog.dto.CommentDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface CommentDao {

    List<CommentDto> getCommentByArticleId(Integer id);
}
