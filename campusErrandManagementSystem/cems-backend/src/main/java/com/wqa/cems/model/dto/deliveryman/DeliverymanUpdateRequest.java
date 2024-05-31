package com.wqa.cems.model.dto.deliveryman;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class DeliverymanUpdateRequest {

    /**
     * 配送员id
     */
    private Long id;

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
     * 出生日期
     */
    private Date birthday;

    /**
     * 账号状态(正常，暂停接单，离职)
     */
    private String status;
}
