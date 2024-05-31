package com.wqa.cems.model.vo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 已登录用户视图（脱敏）
 *
*
 **/
@Data
public class LoginUserVO implements Serializable {

    /**
     * 用户 id
     */
    private Long id;

    /**
     * 用户姓名
     */
    private String username;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户角色：user/admin/deliveryman
     */
    private String role;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}