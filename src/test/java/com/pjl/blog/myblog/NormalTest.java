package com.pjl.blog.myblog;

import com.pjl.blog.myblog.service.MailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class NormalTest extends MyblogApplicationTests{


    @Autowired
    private MailService mailService;

    @Test
    public void normalTest(){
        String s = "aaaa";
        int len = s.length();
        if(len < 2)
            System.out.println(s);

        int maxLen = 1;
        int maxBegin = 0;

        boolean dp[][] = new boolean[len][len];
        char[] Schar = s.toCharArray();
        //初始化
        for (int i = 0;i < len;i++){
            dp[i][i] = true;
        }

        for (int i = 0;i < len;i++){
            for (int j = i + 1;j < len;j++){
                if(Schar[i] == Schar[j]){
                    if(j - i < 3)
                        dp[i][j] = true;
                    else
                        dp[i][j] = dp[i+1][j-1];
                }else{
                    dp[i][j] = false;
                }

                if(dp[i][j] == true && j - i + 1 > maxLen){
                    maxLen = j - i + 1;
                    maxBegin = i;
                }
            }
        }


        System.out.println(s.substring(maxBegin,maxLen+maxBegin));

    }

    @Test
    public void springTtest(){
    }

    @Test
    public void springMailTest(){
        mailService.sendSimpleMail("GLDZPang@outlook.com","测试邮件","测试邮件内容测试测试测试测试测试");
    }
}
