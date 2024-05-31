package com.wqa.cems.model.dto.address;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

@Data
public class AddressUpdateRequest implements Serializable {
    /**
     * id
     */
    private Long id;

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