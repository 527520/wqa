package com.wqa.cems.model.dto.user;

import java.io.Serializable;
import lombok.Data;

/**
 * 用户更新个人信息请求
 *
*
 */
@Data
public class UserUpdateMyRequest implements Serializable {

    /**
     * 用户姓名
     */
    private String username;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 手机号
     */
    private String phoneNumber;

    private static final long serialVersionUID = 1L;
}