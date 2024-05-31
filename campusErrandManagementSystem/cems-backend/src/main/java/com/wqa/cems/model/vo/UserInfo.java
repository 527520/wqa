package com.wqa.cems.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户视图（脱敏）
 *
*
 */
@Data
public class UserInfo implements Serializable {

    /**
     * 用户id
     */
    private Long id;

    /**
     * 用户名
     */
    private String userAccount;

    /**
     * 用户姓名
     */
    private String username;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 角色
     */
    private String role;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}