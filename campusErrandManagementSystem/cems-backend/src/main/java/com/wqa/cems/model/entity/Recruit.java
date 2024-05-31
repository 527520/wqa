package com.wqa.cems.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @TableName recruit
 */
@TableName(value = "recruit")
@Data
public class Recruit implements Serializable {
    /**
     * id
     */
    @TableId
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 招聘内容
     */
    private String content;

    /**
     * 发布时间
     */
    private Date publishTime;

    /**
     * 招聘状态(0：招聘中，1：招聘结束)
     */
    @TableLogic
    private Integer status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}