package com.pjl.blog.myblog.dao;

import com.pjl.blog.myblog.dto.ArticleDto;
import com.pjl.blog.myblog.model.ArticleTagsVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface ArticleDao {

    int saveArticleTags(List<ArticleTagsVO> questionTagsList);

    List<ArticleDto> getArticleList(Integer userId, Integer offset, Integer size, Integer tagId);

    Integer totalCount(Integer userId, Integer tagId);
}
