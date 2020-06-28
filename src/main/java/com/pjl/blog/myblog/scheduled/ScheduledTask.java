package com.pjl.blog.myblog.scheduled;

import com.pjl.blog.myblog.mapper.ArticleMapper;
import com.pjl.blog.myblog.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Component
public class ScheduledTask {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private RedisUtils redisUtils;

    @Value("${VIEWS_COUNT_KEY}")
    private String VIEWS_COUNT_KEY;

    @Transactional(rollbackFor=Exception.class)
    @Scheduled(initialDelay = 1,fixedDelay = 300000)
    public void updateViewsCounts(){
        Set<String> keySet = redisUtils.keys(VIEWS_COUNT_KEY + ":*");
        System.out.println("阅读数定时任务启动=============");
        System.out.println(System.currentTimeMillis());
        if (keySet.size() > 0){
            keySet.forEach(key -> {
                System.out.println(key);
                long viewCount = redisUtils.hSize(key);
                String[] articleId = key.split(":");
                articleMapper.updateViews(Integer.valueOf(articleId[1]),viewCount);
                redisUtils.del(key);
            });
        }
        System.out.println("阅读数定时任务结束=============");
    }

    @Scheduled(cron = "0 0/1 * * * ?")
    public void heartBeat(){
        redisUtils.get("heartBeat");
    }

}
