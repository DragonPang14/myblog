package com.pjl.blog.myblog.service;

import com.pjl.blog.myblog.dao.ArticleDao;
import com.pjl.blog.myblog.dto.ArticleDto;
import com.pjl.blog.myblog.dto.PaginationDto;
import com.pjl.blog.myblog.dto.TagDto;
import com.pjl.blog.myblog.enums.ArticleTypeEnum;
import com.pjl.blog.myblog.exception.CustomizeErrorCode;
import com.pjl.blog.myblog.exception.CustomizeException;
import com.pjl.blog.myblog.mapper.ArticleMapper;
import com.pjl.blog.myblog.mapper.UserMapper;
import com.pjl.blog.myblog.model.ArticleTagsVO;
import com.pjl.blog.myblog.model.ArticleVO;
import com.pjl.blog.myblog.model.TagVO;
import com.pjl.blog.myblog.model.UserVO;
import com.pjl.blog.myblog.utils.CommonUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class ArticleService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleDao articleDao;

    public PaginationDto<ArticleDto> getList(Integer userId, Integer page, Integer size, Integer tagId) {

        Integer totalCount = articleDao.totalCount(userId,tagId);
        Integer totalPage = CommonUtils.calculateTotalPage(totalCount);
        Integer offset = CommonUtils.calculatePageOffset(totalPage,page,size);
        if (offset == null){
            return null;
        }
        PaginationDto<ArticleDto> pagination = new PaginationDto<>();
        List<ArticleDto> articleDtos = articleDao.getArticleList(userId,offset,size,tagId);
        /*改为使用xml一次性查询出文章，和标签
        List<ArticleVO> ArticleVOs = userId == null?
                ArticleMapper.getList(offset, size):ArticleMapper.getListByUserId(userId,offset,size);
        List<articleDto> articleDtos = new ArrayList<>();
        User loginUser = null;
        if (userId != null){
            loginUser = userMapper.findById(userId);
        }
        for (ArticleVO ArticleVO : ArticleVOs) {
            articleDto articleDto = new articleDto();
            BeanUtils.copyProperties(ArticleVO, articleDto);
            if (loginUser != null){
                articleDto.setUser(loginUser);
            }else {
                User user = userMapper.findById(ArticleVO.getCreator());
                articleDto.setUser(user);
            }
            articleDtos.add(articleDto);
        }
        */
        pagination.setPageList(articleDtos);
        pagination.setPagination(totalPage, page);
        return pagination;
    }

    public ArticleDto findArticleById(Integer id) {
        ArticleVO articleVO = articleMapper.findArticleById(id);
        if (articleVO == null) {
            throw new CustomizeException(CustomizeErrorCode.ARTICLE_NOT_FOUND);
        }
        return buildArticleDto(articleVO);
    }


    public void createOrUpdate(ArticleDto articleDto, List<Integer> tagIdList) {
        Integer id;
        if (!StringUtils.isEmpty(articleDto.getId()) ||
                (articleDto.getType() == ArticleTypeEnum.ABOUT_ME.getCode() &&
                        articleMapper.findArticleByType(ArticleTypeEnum.ABOUT_ME.getCode()) != null) ) {
            ArticleVO dbArticleVO = articleMapper.findArticleById(articleDto.getId());
            dbArticleVO.setGmtModified(articleDto.getGmtCreate());
            dbArticleVO.setTitle(articleDto.getTitle());
            dbArticleVO.setDescription(articleDto.getDescription());
            dbArticleVO.setDescriptionStr(articleDto.getDescriptionStr());
            dbArticleVO.setContent(articleDto.getContent());
            articleMapper.updateArticle(dbArticleVO);
            articleMapper.deleteArticleTags(dbArticleVO.getId());
            id = dbArticleVO.getId();
        } else {
            ArticleVO ArticleVO = new ArticleVO();
            BeanUtils.copyProperties(articleDto,ArticleVO);
            ArticleVO.setGmtCreate(System.currentTimeMillis());
            ArticleVO.setGmtModified(ArticleVO.getGmtCreate());
            ArticleVO.setType(articleDto.getType());
            articleMapper.createArticle(ArticleVO);
            id = ArticleVO.getId();
        }
        List<ArticleTagsVO> articleTagsList = new ArrayList<>();
        for (Integer tagId : tagIdList) {
            ArticleTagsVO articleTags = new ArticleTagsVO();
            articleTags.setArticleId(id);
            articleTags.setTagId(tagId);
            articleTags.setGmtCreate(System.currentTimeMillis());
            articleTagsList.add(articleTags);
        }
        //保存问题与标签关系
        articleDao.saveArticleTags(articleTagsList);
    }

    /**
     * @desc save tag method
     * @param tagDto
     * @return
     */
    public int saveTag(TagDto tagDto) {
        TagVO tag = new TagVO();
        tag.setTagName(tagDto.getTagName());
        tag.setRemarks(tagDto.getRemarks());
        tag.setGmtCreate(System.currentTimeMillis());
        tag.setGmtModified(tag.getGmtCreate());
        int isSuccess = articleMapper.saveTag(tag);
        return isSuccess;
    }

    /**
     * @desc getTags method
     * @return
     * @param type
     * @param used
     */
    public List<TagDto> getTags(Integer used) {
        List<TagVO> tags;
        if (used == 1){
            tags = articleMapper.getUsedTags();
        }else {
            tags = articleMapper.getTags();
        }
        List<TagDto> tagDtos = new ArrayList<>();
        if (tags != null && tags.size() > 0 ){
            for (TagVO tag : tags){
                TagDto tagDto = new TagDto();
                BeanUtils.copyProperties(tag,tagDto);
                tagDtos.add(tagDto);
            }
        }
        return tagDtos;
    }

    /**
     * @desc 根据标签名查找标签
     * @param tagName
     * @return
     */
    public int findTagByName(String tagName) {
        tagName = tagName.toLowerCase();
        int isExists = articleMapper.findTagByName(tagName);
        return isExists;
    }

    /**
     * @desc 根据文章类型查找文章
     * @param code
     * @return
     */
    public ArticleDto findArticleByType(int code) {
        ArticleVO articleVO = articleMapper.findArticleByType(code);
        if (articleVO == null) {
            throw new CustomizeException(CustomizeErrorCode.ARTICLE_NOT_FOUND);
        }
        return buildArticleDto(articleVO);
    }

    private ArticleDto buildArticleDto(ArticleVO articleVO){
        ArticleDto articleDto = new ArticleDto();
        UserVO user = userMapper.findById(articleVO.getCreator());
        List<TagDto> tagDtos = articleMapper.getArticleTags(articleVO.getId());
        BeanUtils.copyProperties(articleVO, articleDto);
        articleDto.setTagList(tagDtos);
        articleDto.setUser(user);
        return articleDto;
    }
}
