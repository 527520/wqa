package com.wqa.cems.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * @TableName deliveryman
 */
@TableName(value ="deliveryman")
@Data
public class Deliveryman implements Serializable {
    /**
     * 配送员id
     */
    @TableId
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 身份证号
     */
    private String idNumber;

    /**
     * 手机号码
     */
    private String phoneNumber;

    /**
     * 姓名
     */
    private String name;

    /**
     * 性别
     */
    private String sex;

    /**
     * 出生日期
     */
    private Date birthday;

    /**
     * 身份证正面照
     */
    private String idCardFrontImage;

    /**
     * 身份证反面照
     */
    private String idCardBackImage;

    /**
     * 是否在线
     */
    private String isOnline;

    /**
     * 可接单状态(离线，未接单，已接单)
     */
    private String acceptingOrders;

    /**
     * 账号状态(正常，暂停接单，离职)
     */
    private String status;

    /**
     * 完成订单数
     */
    private Integer completedOrders;

    /**
     * 平均评分
     */
    private BigDecimal averageRating;

    /**
     * 
     */
    private Date createTime;

    /**
     * 
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isdelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}