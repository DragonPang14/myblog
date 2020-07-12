package com.pjl.blog.myblog;

import com.pjl.blog.myblog.service.MailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

public class NormalTest extends MyblogApplicationTests{


    @Autowired
    private MailService mailService;

    @Test
    public void normalTest(){
        int[] heights = new int[]{1,3,5,2,6,7};
        List heightList = Arrays.asList(heights);
        int[] result = Arrays.copyOf(heights,heights.length);
        Arrays.sort(result);
        System.out.println(result);
    }

    @Test
    public void springTtest(){
    }

    @Test
    public void springMailTest(){
        mailService.sendSimpleMail("GLDZPang@outlook.com","测试邮件","测试邮件内容测试测试测试测试测试");
    }
}
