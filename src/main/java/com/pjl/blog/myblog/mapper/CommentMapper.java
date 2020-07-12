package com.pjl.blog.myblog.mapper;

import com.pjl.blog.myblog.model.CommentVO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface CommentMapper {


    @Insert("insert into comment (article_id,parent_id,receiver_id,content,type,creator,like_count,gmt_create,gmt_modified) values " +
            "(#{articleId},#{parentId},#{receiverId},#{content},#{type},#{creator},#{likeCount},#{gmtCreate},#{gmtModified})")
    @Options(keyColumn = "id",useGeneratedKeys = true,keyProperty = "id")
    void insert(CommentVO comment);

    //根据父ID查找评论
    @Select("select * from comment where parent_id = #{parentId} and type = #{type} order by gmt_create asc")
    List<CommentVO> findCommentById(Integer parentId, Integer type);

    //查找需要回复的评论
    @Select("select * from comment where id = #{parentId} ")
    CommentVO findCommentByCommentId(Integer parentId);
}
