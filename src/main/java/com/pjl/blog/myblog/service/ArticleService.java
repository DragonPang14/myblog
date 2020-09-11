package com.pjl.blog.myblog.service;

import com.alibaba.fastjson.JSON;
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
import com.pjl.blog.myblog.utils.RedisUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ArticleService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private RedisUtils redisUtils;

    @Value("${DRAFT_KEY}")
    private String DRAFT_KEY;

    public PaginationDto<ArticleDto> getList(Integer userId, Integer page, Integer size, Integer tagId) {

        Integer totalCount = articleDao.totalCount(userId, tagId);
        PaginationDto<ArticleDto> pagination = CommonUtils.buildPage(ArticleDto.class,totalCount,page,size);
        List<ArticleDto> articleDtos = pagination.getOffset() == null?null:articleDao.getArticleList(userId, pagination.getOffset(), size, tagId);
        pagination.setPageList(articleDtos);
        return pagination;
    }

    public ArticleDto findArticleById(Integer id, boolean isNotify) {
        ArticleVO articleVO = articleMapper.findArticleById(id);
        if (articleVO == null) {
            throw new CustomizeException(CustomizeErrorCode.ARTICLE_NOT_FOUND);
        }
        if (isNotify) {
            userMapper.readNotification(articleVO.getId());
        }
        return buildArticleDto(articleVO);
    }


    public void createOrUpdate(ArticleDto articleDto, List<Integer> tagIdList) throws IllegalAccessException {
        Integer id;
        int isSuccess;
        String draftKey = DRAFT_KEY + ":" + articleDto.getPublishToken();
        //如果找到了修改的文章或者是about me文章
        if (!StringUtils.isEmpty(articleDto.getId()) ||
                (articleDto.getType() == ArticleTypeEnum.ABOUT_ME.getCode() &&
                        articleMapper.findArticleByType(ArticleTypeEnum.ABOUT_ME.getCode()) != null)) {
            ArticleVO dbArticleVO = articleDto.getType() == ArticleTypeEnum.ABOUT_ME.getCode()?articleMapper.findArticleByType(ArticleTypeEnum.ABOUT_ME.getCode()):
                    articleMapper.findArticleById(articleDto.getId());
            BeanUtils.copyProperties(articleDto,dbArticleVO,"id");
            dbArticleVO.setGmtModified(System.currentTimeMillis());
            isSuccess = articleMapper.updateArticle(dbArticleVO);
            articleMapper.deleteArticleTags(dbArticleVO.getId());
            id = dbArticleVO.getId();
        } else {
            ArticleVO ArticleVO = new ArticleVO();
            BeanUtils.copyProperties(articleDto, ArticleVO);
            ArticleVO.setGmtCreate(System.currentTimeMillis());
            ArticleVO.setGmtModified(ArticleVO.getGmtCreate());
            ArticleVO.setType(articleDto.getType());
            isSuccess = articleMapper.createArticle(ArticleVO);
            id = ArticleVO.getId();
        }
        if (isSuccess > 0){
            //需要删除的hashmap的key
            List<String> fieldNameList = CommonUtils.getObjectUsedFieldName(articleDto);
            redisUtils.hmDel(draftKey,fieldNameList.toArray());
        }
        if(tagIdList.size() > 0){
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
    }

    /**
     * @param tagDto
     * @return
     * @desc save tag method
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
     * @param used
     * @return
     * @desc getTags method
     */
    public List<TagDto> getTags(Integer used) {
        List<TagVO> tags;
        if (used == 1) {
            tags = articleMapper.getUsedTags();
        } else {
            tags = articleMapper.getTags();
        }
        List<TagDto> tagDtos = new ArrayList<>();
        if (tags != null && tags.size() > 0) {
            for (TagVO tag : tags) {
                TagDto tagDto = new TagDto();
                BeanUtils.copyProperties(tag, tagDto);
                tagDtos.add(tagDto);
            }
        }
        return tagDtos;
    }

    /**
     * @param tagName
     * @return
     * @desc 根据标签名查找标签
     */
    public int findTagByName(String tagName) {
        tagName = tagName.toLowerCase();
        int isExists = articleMapper.findTagByName(tagName);
        return isExists;
    }

    /**
     * @param code
     * @return
     * @desc 根据文章类型查找文章
     */
    public ArticleDto findArticleByType(int code) {
        ArticleVO articleVO = articleMapper.findArticleByType(code);
        if (articleVO == null) {
            throw new CustomizeException(CustomizeErrorCode.ARTICLE_NOT_FOUND);
        }
        return buildArticleDto(articleVO);
    }

    /**
     * @param articleVO
     * @return
     * @desc 构建dto
     */
    private ArticleDto buildArticleDto(ArticleVO articleVO) {
        ArticleDto articleDto = new ArticleDto();
        UserVO user = userMapper.findById(articleVO.getCreator());
        List<TagDto> tagDtos = articleMapper.getArticleTags(articleVO.getId());
        BeanUtils.copyProperties(articleVO, articleDto);
        articleDto.setTagList(tagDtos);
        articleDto.setUser(user);
        return articleDto;
    }

    /**
     * @param articleDto
     * @desc redis保存草稿
     */
    public void autoSaveDraft(ArticleDto articleDto) throws IllegalAccessException {
        String draftKey = DRAFT_KEY + ":" + articleDto.getPublishToken();
        Map<String, Object> draftMap = CommonUtils.objectValueToMap(articleDto);
        redisUtils.hmSet(draftKey, draftMap);
    }

    /**
     * @desc redis持久化草稿箱到mysql
     * @param publishToken
     * @param userId
     * @throws IllegalAccessException
     */
    @Transactional
    public void persistenceDraft(String publishToken, Integer userId) throws IllegalAccessException {
        String draftKey = DRAFT_KEY + ":" + publishToken;
        Map<Object,Object> articleMap = redisUtils.hmGet(draftKey);
        int isSuccess;
        if (!CollectionUtils.isEmpty(articleMap)){
            ArticleVO articleVO = JSON.parseObject(JSON.toJSONString(articleMap),ArticleVO.class);
            articleVO.setCreator(userId);
            if (articleVO.getId() != null){
                articleVO.setGmtModified(System.currentTimeMillis());
                isSuccess = articleMapper.updateArticle(articleVO);
            }else {
                articleVO.setGmtCreate(System.currentTimeMillis());
                articleVO.setGmtModified(articleVO.getGmtCreate());
                isSuccess = articleMapper.createArticle(articleVO);
            }
            if (isSuccess > 0){
                //需要删除的hashmap的key
                List<String> fieldNameList = CommonUtils.getObjectUsedFieldName(articleVO);
                fieldNameList.add("publishToken");
                redisUtils.hmDel(draftKey,fieldNameList.toArray());
            }
        }
    }

    /**
     * @desc 获取草稿
     * @param userId
     * @param page
     * @return
     */
    public PaginationDto<ArticleDto> getDraftByUserId(Integer userId, Integer page) {
        Integer totalCount = articleDao.totalCountDraft(userId);
        PaginationDto<ArticleDto> pagination = CommonUtils.buildPage(ArticleDto.class,totalCount,page,20);
        List<ArticleDto> draftDtos = articleDao.getDraftList(userId,pagination.getOffset(),20);
        pagination.setPageList(draftDtos);
        return pagination;
    }

    /**
     * @desc 移除草稿
     * @param draftId
     * @param userId
     */
    public void removeDraft(Integer draftId, Integer userId) {
        articleMapper.removeDraft(draftId,userId);
    }

    /**
     * @desc 获取时间线
     * @return
     */
    public List<ArticleDto> getTimeLine() {
        List<ArticleDto> timeLineDtos = articleDao.getTimeLine();
        return timeLineDtos;
    }


}
