package com.wqa.cems.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.checkerframework.checker.formatter.qual.Format;
import org.springframework.format.annotation.DateTimeFormat;

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
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date time;

    /**
     * 是有读取消息
     */
    private Integer isRead;

    private static final long serialVersionUID = 1L;
}
