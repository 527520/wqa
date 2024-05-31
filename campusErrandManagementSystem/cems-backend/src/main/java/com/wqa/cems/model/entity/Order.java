package com.wqa.cems.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName order
 */
@TableName(value ="`order`")
@Data
public class Order implements Serializable {
    /**
     * id
     */
    @TableId
    private Long id;

    /**
     * 下单用户id
     */
    private Long userId;

    /**
     * 配送员id
     */
    private Long deliverymanId;

    /**
     * 任务id
     */
    private Long taskId;

    /**
     * 订单状态(未支付，已支付，已完成，已取消)
     */
    private String status;

    /**
     * 下单时间
     */
    private Date orderTime;

    /**
     * 取件地址
     */
    private String fetchAddress;

    /**
     * 收货地址
     */
    private String shippingAddress;

    /**
     * 收货人姓名
     */
    private String receiverName;

    /**
     * 收货人电话
     */
    private String receiverPhoneNumber;

    /**
     * 订单金额
     */
    private BigDecimal orderAmount;

    /**
     * 订单备注
     */
    private String orderNote;

    /**
     * 预计送达时间
     */
    private Date estimatedCompletionTime;

    /**
     * 实际完成时间
     */
    private Date completionTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}