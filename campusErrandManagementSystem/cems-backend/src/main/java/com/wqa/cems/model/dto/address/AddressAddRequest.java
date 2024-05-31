package com.wqa.cems.model.dto.address;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
public class AddressAddRequest implements Serializable {
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

    private static final long serialVersionUID = 1L;
}