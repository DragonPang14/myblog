package com.pjl.blog.myblog;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class NormalTest{

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
}
