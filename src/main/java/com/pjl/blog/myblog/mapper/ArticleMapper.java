package com.pjl.blog.myblog.mapper;

import com.pjl.blog.myblog.dto.TagDto;
import com.pjl.blog.myblog.model.ArticleVO;
import com.pjl.blog.myblog.model.TagVO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface ArticleMapper {

    @Insert("insert into article (title,description,description_str,content,gmt_create,gmt_modified,creator,type) " +
            "values (#{title},#{description},#{descriptionStr},#{content},#{gmtCreate},#{gmtModified},#{creator},#{type})")
    @Options(useGeneratedKeys = true,keyColumn = "id",keyProperty = "id")
    int createArticle(ArticleVO articleVO);

    @Select("select count(1) from article where creator = #{userId}")
    Integer userArticleCount(@Param(value = "userId") Integer userId);

    @Select("select * from article where id = #{id}")
    ArticleVO findArticleById(@Param(value = "id") Integer id);

    @Update("update article set gmt_modified = #{gmtModified},title = #{title},description = #{description},description_str = #{descriptionStr},content = #{content},type = #{type} where id = #{id}")
    int updateArticle(ArticleVO dbArticle);

    @Update("update article set comment_count = comment_count + #{i} where id = #{id}")
    void incComment(@Param(value = "id") Integer parentId, @Param(value = "i") long i);

    @Insert("insert into tag (tag_name,remarks,gmt_create,gmt_modified) values (#{tagName},#{remarks},#{gmtCreate},#{gmtModified})")
    @Options(useGeneratedKeys = true,keyColumn = "id",keyProperty = "id")
    int saveTag(TagVO tag);

    @Select("select * from tag where 1 = 1 and del_flag = 0 order by type ")
    List<TagVO> getTags();

    @Select("select count(1) from tag where tag_name = #{tagName}")
    @Options(keyColumn = "count(1)")
    int findTagByName(String tagName);

    @Update("update article_tags set del_flag = 1 where article_id = #{id}")
    int deleteArticleTags(Integer id);

    @Update("update article set view_count = view_count + #{count} where id = #{id}")
    int updateViews(Integer id, long count);

    @Select("select t.* from article_tags at left join tag t on at.tag_id = t.id where at.article_id = #{id} and at.del_flag = 0")
    List<TagDto> getArticleTags(Integer id);

    @Select("select * from article where type = #{type} and del_flag = 0")
    ArticleVO findArticleByType(int code);

    @Select("select distinct t.* from tag t inner join article_tags at on t.id = at.tag_id where at.del_flag = 0 and t.del_flag = 0")
    List<TagVO> getUsedTags();

    @Update("<script>update article set del_flag = 1 where creator = #{userId} and type = 3 <if test='draftId != null'>and id = #{draftId}</if></script>")
    void removeDraft(Integer draftId, Integer userId);
}
