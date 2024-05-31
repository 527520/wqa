package com.wqa.cems.service;

import javax.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 用户服务测试
 *
*
 */
@SpringBootTest
public class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    void userRegister() {
        String userAccount = "yupi";
        String userPassword = "";
        String checkPassword = "123456";
        String username = "吴奇安";
        String phoneNumber = "18179307809";
        try {
            long result = userService.userRegister(userAccount, userPassword, checkPassword, username, phoneNumber);
            Assertions.assertEquals(-1, result);
            userAccount = "yu";
            result = userService.userRegister(userAccount, userPassword, checkPassword, username, phoneNumber);
            Assertions.assertEquals(-1, result);
        } catch (Exception e) {

        }
    }
}
