package com.pjl.blog.myblog.provider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pjl.blog.myblog.dto.AccessTokenDto;
import com.pjl.blog.myblog.dto.GitHubUserDto;
import com.pjl.blog.myblog.utils.OkHttpUtils;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GitHubProvider {

    @Value("${github.getaccesstoken.url}")
    private String getAccessUrl;

    @Value("${github.getuser.url}")
    private String getUserUrl;

    /**
     * 获取github的accesstoken
     * @param accessTokenDto
     * @return
     */
    public String getAccessToken(AccessTokenDto accessTokenDto){
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = OkHttpUtils.getInstance();
        RequestBody body = RequestBody.create(mediaType,JSON.toJSONString(accessTokenDto));
        Request request = new Request.Builder()
                .url(getAccessUrl)
                .header("Accept","application/json")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            JSONObject resultBody = JSON.parseObject(response.body().string());
            System.out.println(resultBody);
            return resultBody.getString("access_token");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @desc 获取GitHub用户
     * @param accessToken
     * @return
     */
    public GitHubUserDto getUser(String accessToken){
        OkHttpClient client = OkHttpUtils.getInstance();
        Request request = new Request.Builder()
                .url(getUserUrl)
                .header("Authorization","token " + accessToken)
                .header("Connection","keep-alive")
                .build();
        try {
            Response response = client.newCall(request).execute();
            String resStr = response.body().string();
            System.out.println(resStr);
            GitHubUserDto gitHubUserDto = JSON.parseObject(resStr, GitHubUserDto.class);
            return gitHubUserDto;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
