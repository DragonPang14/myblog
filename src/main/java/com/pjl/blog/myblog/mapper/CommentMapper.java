package com.pjl.blog.myblog.mapper;

import com.pjl.blog.myblog.model.CommentVO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface CommentMapper {


    @Insert("insert into comment (parent_id,content,type,creator,like_count,gmt_create,gmt_modified) values " +
            "(#{parentId},#{content},#{type},#{creator},#{likeCount},#{gmtCreate},#{gmtModified})")
    @Options(keyColumn = "id",useGeneratedKeys = true,keyProperty = "id")
    void insert(CommentVO comment);

    //根据父ID查找评论
    @Select("select * from comment where parent_id = #{parentId} and type = #{type} order by gmt_create asc")
    List<CommentVO> findCommentById(@Param(value = "parentId") Integer parentId, @Param(value = "type") Integer type);

    //查找需要回复的评论
    @Select("select * from comment where id = #{parentId} and type = #{type}")
    CommentVO findCommentByCommentId(@Param(value = "parentId") Integer parentId, @Param(value = "type") Integer type);
}
