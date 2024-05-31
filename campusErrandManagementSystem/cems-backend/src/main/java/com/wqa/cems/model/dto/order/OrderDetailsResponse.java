package com.wqa.cems.model.dto.order;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderDetailsResponse implements Serializable {
    /**
     * id
     */
    @TableId
    private Long id;

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
    private String orderStatus;

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
     * 订单金额
     */
    private BigDecimal orderAmount;

    /**
     * 订单备注
     */
    private String orderNote;

    /**
     * 实际完成时间
     */
    private Date completionTime;

    /**
     * 服务类型
     */
    private String serviceType;

    /**
     * 物品名称
     */
    private String itemName;

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
    private String taskStatus;

    /**
     * 姓名
     */
    private String deliverymanName;

    /**
     * 平均评分
     */
    private BigDecimal averageRating;

    private static final long serialVersionUID = 1L;
}
