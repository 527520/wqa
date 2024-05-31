package com.wqa.cems.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wqa.cems.model.entity.Order;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * @TableName order
 */
@Data
public class OrderVO implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 配送员
     */
    private DeliverymanVOVO deliveryman;

    /**
     * 任务
     */
    private TaskVO task;

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

    private static final long serialVersionUID = 1L;

    public static OrderVO objToVo(Order order) {
        if (order == null) {
            return null;
        }
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(order, orderVO);
        orderVO.setOrderStatus(order.getStatus());
        return orderVO;
    }
}