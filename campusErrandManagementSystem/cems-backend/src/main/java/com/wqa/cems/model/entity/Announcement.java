package com.wqa.cems.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName announcement
 */
@TableName(value ="announcement")
@Data
public class Announcement implements Serializable {
    /**
     * id
     */
    @TableId
    private Long id;

    /**
     * 公告标题
     */
    private String title;

    /**
     * 公告内容
     */
    private String content;

    /**
     * 发布时间
     */
    private Date publishDate;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否有效
     */
    @TableLogic(value = "1", delval = "0")
    private Integer isActive;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}