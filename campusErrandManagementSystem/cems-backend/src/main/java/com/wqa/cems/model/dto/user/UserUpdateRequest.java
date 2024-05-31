package com.wqa.cems.model.dto.user;

import java.io.Serializable;
import lombok.Data;

/**
 * 用户更新请求
 *
*
 */
@Data
public class UserUpdateRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String username;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户角色：user/admin/ban
     */
    private String role;

    /**
     * 手机号
     */
    private String phoneNumber;

    private static final long serialVersionUID = 1L;
}