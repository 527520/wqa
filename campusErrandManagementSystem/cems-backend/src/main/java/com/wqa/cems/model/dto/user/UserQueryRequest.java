package com.wqa.cems.model.dto.user;

import com.baomidou.mybatisplus.annotation.TableId;
import com.wqa.cems.common.PageRequest;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户查询请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserQueryRequest extends PageRequest implements Serializable {
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

    private static final long serialVersionUID = 1L;
}