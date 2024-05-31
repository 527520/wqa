package com.wqa.cems.model.dto.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserUpdatePasswordRequest implements Serializable {

    private String userPassword;
    private String checkPassword;
    private String oldPassword;


    private static final long serialVersionUID = 1L;
}
