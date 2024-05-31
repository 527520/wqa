package com.wqa.cems.model.dto.user;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserBecomeDeliverymanRequest implements Serializable {
    private static final long serialVersionUID = 3191241716373120793L;

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
    private String idCardFront;

    /**
     * 身份证反面照
     */
    private String idCardBack;
}
