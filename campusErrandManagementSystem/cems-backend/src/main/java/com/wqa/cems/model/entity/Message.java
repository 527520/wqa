package com.wqa.cems.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName message
 */
@TableName(value ="message")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 发送者id
     */
    private Long formUserId;

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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}