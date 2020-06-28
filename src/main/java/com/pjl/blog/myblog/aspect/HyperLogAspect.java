package com.pjl.blog.myblog.aspect;

import com.pjl.blog.myblog.utils.IPUtils;
import com.pjl.blog.myblog.utils.RedisUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(1)
public class HyperLogAspect {

    @Autowired
    private RedisUtils redisUtils;

    @Value("${VIEWS_COUNT_KEY}")
    private String VIEWS_COUNT_KEY;

    /**
     * @desc aop切入点
     */
    @Pointcut("@annotation(com.pjl.blog.myblog.aspect.HyperLogInc)")
    public void pointCut(){
    }

    /**
     * @desc 切入点后执行的内容，即通知，around，即切入点的方法执行前后执行通知，用joinPoint.proceed()来控制切入点方法的执行
     * @return
     */
    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();
        Object articleId = args[0];
        Object obj = null;
        String redisKey = "";
        try {
            String ip = IPUtils.getIpAddr();
            redisKey = VIEWS_COUNT_KEY + ":" + articleId;
            System.out.println("views aop " + redisKey);
            redisUtils.hAdd(redisKey,ip);
            obj = joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return obj;
    }
}
