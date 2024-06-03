package com.wqa.cems.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class MessageVO implements Serializable {

    private Long id;

    /**
     * 发送者id
     */
    private Long formUserId;

    /**
     * 对方姓名
     */
    private String counterpartName;

    /**
     * 接收者id
     */
    private Long toUserId;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 发送者头像
     */
    private String formAvatar;

    /**
     * 接收者头像
     */
    private String toAvatar;

    /**
     * 发送时间
     */
    private Date time;

    /**
     * 是有读取消息
     */
    private Integer isRead;

    private static final long serialVersionUID = 1L;
}
