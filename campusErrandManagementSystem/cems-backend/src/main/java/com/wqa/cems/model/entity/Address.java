package com.wqa.cems.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import lombok.Data;

/**
 * @TableName address
 */
@TableName(value = "address")
@Data
public class Address implements Serializable {
    /**
     * id
     */
    @TableId
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 地址
     */
    private String address;

    /**
     * 收货人
     */
    private String consignee;

    /**
     * 收货人手机号
     */
    private String phoneNumber;

    /**
     * 是否为默认地址
     */
    private Integer isDefault;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return "Address{" +
                "address='" + address + '\'' +
                ", consignee='" + consignee + '\'' +
                ", phoneNumber='" + phoneNumber +
                '}';
    }
}