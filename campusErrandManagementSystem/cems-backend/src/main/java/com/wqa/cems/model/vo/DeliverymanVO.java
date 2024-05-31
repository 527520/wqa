package com.wqa.cems.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class DeliverymanVO implements Serializable {
    /**
     * 配送员id
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 手机号码
     */
    private String phoneNumber;

    /**
     * 性别
     */
    private String sex;

    /**
     * 出生日期
     */
    private Date birthday;

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
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    private Integer isdelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}