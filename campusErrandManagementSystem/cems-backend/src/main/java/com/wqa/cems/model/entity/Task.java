package com.wqa.cems.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName task
 */
@TableName(value ="task")
@Data
public class Task implements Serializable {
    /**
     * id
     */
    @TableId
    private Long id;

    /**
     * 发布的用户id
     */
    private Long userId;

    /**
     * 接单配送员id
     */
    private Long deliverymanId;

    /**
     * 服务类型
     */
    private String serviceType;

    /**
     * 物品名称
     */
    private String itemName;

    /**
     * 取件地址
     */
    private String fetchAddress;

    /**
     * 收货地址
     */
    private String shippingAddress;

    /**
     * 物品重量
     */
    private String itemWeight;

    /**
     * 描述信息
     */
    private String description;

    /**
     * 任务状态(待接单，已接单，配送中，已完成，已评论，已取消)
     */
    private String status;

    /**
     * 发布时间
     */
    private Date publishTime;

    /**
     * 用户期望完成时间
     */
    private Date estimatedCompletionTime;

    /**
     * 实际完成时间
     */
    private Date completionTime;

    /**
     * 任务价格
     */
    private BigDecimal price;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDeleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}