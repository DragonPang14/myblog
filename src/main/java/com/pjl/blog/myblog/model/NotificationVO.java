package com.pjl.blog.myblog.model;

import lombok.Data;

/**
 * @desc 通知pojo
 */
@Data
public class NotificationVO extends BaseVO{
    private Integer targetId;
    private Integer targetType;
    private Integer senderId;
    private Integer receiveId;
    private String notiContent;
    private int action;
    private int status;
}
